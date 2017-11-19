package org.inep.robo;

import infinispan.com.mchange.v2.c3p0.ComboPooledDataSource;
import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.Configuration;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Main {

    public static Logger logger = Logger.getLogger("Main");
    public static final String JDG_HOST = "jdg.host";
    // Memcached specific properties
    public static final String MEMCACHED_PORT = "jdg.memcached.port";
    public static final String PROPERTIES_FILE = "jdg.properties";
    public static final String CACHE_KEY = "inscritos";

    public static MemcachedCache memCachedCache;
    public static List<InscritoMunicipioProva> inscritosdb = new ArrayList<InscritoMunicipioProva>();
    public static List<String> inscritosCache = new ArrayList<String>();
    public static List<String> inscritosHotRod = new ArrayList<String>();

    public static Connection conn = null;
    public static int quantRegistros = 40;

    public static String IP_HOTROD = "127.0.0.1";
    public static String IP_POSTGRES = "127.0.0.1";
    //public static String IP_POSTGRES = "35.188.62.197";

    public static void main(String[] args) {

        AnsiConsole.systemInstall();

        memCachedCache = new MemcachedCache(jdgProperty(JDG_HOST), Integer.parseInt(jdgProperty(MEMCACHED_PORT)));
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.tcpNoDelay(true)
        .connectionPool()
                .numTestsPerEvictionRun(3)
                .testOnBorrow(false)
                .testOnReturn(false)
                .testWhileIdle(true)
                .addServer()
                .host(IP_HOTROD)
                .port(11222);
        RemoteCacheManager rmc = new RemoteCacheManager(builder.build());

        if (args != null && args[0] != null) {
            quantRegistros = Integer.valueOf(args[0]);
        }

        System.out.println(ansi().fg(GREEN).a("  ## CONSULTA DE INSCRITOS ##").reset());
        System.out.println(ansi().fg(GREEN).a(" ------------------- ").reset());


        System.out.println(ansi().fg(GREEN).a("  CONSULTA INSCRICOES NO BANCO ...").reset());
        long startTime = System.nanoTime();
        long inscricao = 1610377711l;
        for (int x = 0;x < quantRegistros ;x++ ) {
            inscritosdb.add(consultaInscrito(inscricao));
            inscricao++;
        }
        long endTime = System.nanoTime();
        long tempodb = ((endTime - startTime) / 1000000);
        System.out.println(ansi().fg(GREEN).a("  TEMPO DA BUSCA NO BANCO: " + tempodb + "ms").reset());
        System.out.println(ansi().fg(GREEN).a("  QUANTIDADE INSCRITOS: " + inscritosdb.size()).reset());
        System.out.println(ansi().fg(GREEN).a("  ------------------- ").reset());


        System.out.println(ansi().fg(GREEN).a("  COLOCANDO LISTA NO CACHE (MEMCACHED)... ").reset());
        startTime = System.nanoTime();
        preencheCache(inscritosdb, memCachedCache);
        endTime = System.nanoTime();
        System.out.println(ansi().fg(GREEN).a("  TEMPO GRAVANDO NO CACHE: " + ((endTime - startTime) / 1000000) + "ms").reset());
        System.out.println(ansi().fg(GREEN).a("  CACHE PREENCHIDO ").reset());
        System.out.println(ansi().fg(GREEN).a("  ------------------- ").reset());


        System.out.println(ansi().fg(GREEN).a("  COLOCANDO LISTA NO CACHE (HOTROD)... ").reset());
        startTime = System.nanoTime();
        preencheCache(inscritosdb, rmc);
        endTime = System.nanoTime();
        System.out.println(ansi().fg(GREEN).a("  TEMPO GRAVANDO NO CACHE: " + ((endTime - startTime) / 1000000) + "ms").reset());
        System.out.println(ansi().fg(GREEN).a("  CACHE PREENCHIDO ").reset());
        System.out.println(ansi().fg(GREEN).a("  ------------------- ").reset());


        System.out.println(ansi().fg(GREEN).a("  CONSULTA INSCRICOES NO CACHE (MEMCACHED) ... ").reset());
        startTime = System.nanoTime();
        inscricao = 1610377711l;
        for (int x = 0;x < quantRegistros ;x++ ) {
            inscritosCache.add((String) memCachedCache.get(String.valueOf(inscricao)));
            inscricao++;
        }
        endTime = System.nanoTime();
        long tempoCache = ((endTime - startTime) / 1000000);
        System.out.println(ansi().fg(GREEN).a("  TEMPO DA BUSCA NO CACHE: " + tempoCache + "ms").reset());
        System.out.println(ansi().fg(GREEN).a("  QUANTIDADE INSCRITOS: " + inscritosCache.size()).reset());
        System.out.println(ansi().fg(GREEN).a("  ------------------- ").reset());



        System.out.println(ansi().fg(GREEN).a("  CONSULTA INSCRICOES NO CACHE (HOTROD) ... ").reset());
        startTime = System.nanoTime();
        inscricao = 1610377711l;
        for (int x = 0;x < quantRegistros ;x++ ) {
            inscritosHotRod.add((String) rmc.getCache().get(String.valueOf(inscricao)));
            inscricao++;
        }
        endTime = System.nanoTime();
        long tempoHotRod = ((endTime - startTime) / 1000000);
        System.out.println(ansi().fg(GREEN).a("  TEMPO DA BUSCA NO CACHE: " + tempoHotRod + "ms").reset());
        System.out.println(ansi().fg(GREEN).a("  QUANTIDADE INSCRITOS: " + inscritosHotRod.size()).reset());
        System.out.println(ansi().fg(GREEN).a("  ------------------- ").reset());



        System.out.println(ansi().fg(YELLOW).a("  DB: " + tempodb + "ms" + "   CACHE: " + tempoCache + "ms").reset());
        System.out.println(ansi().fg(YELLOW).a("  PERCENTUAL: " + ((float)tempoCache * 100.0f) / (float)tempodb + "%").reset());


        System.exit(0);
    }




    private static InscritoMunicipioProva consultaInscrito(long inscricao) {

        InscritoMunicipioProva inscrito = null;

        try {

            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass( "org.postgresql.Driver" );
            cpds.setJdbcUrl( "jdbc:postgresql://"+IP_POSTGRES+":5432/Enem" );
            cpds.setUser("postgres");
            cpds.setPassword("postgres");
            cpds.setInitialPoolSize(10);
            cpds.setMinPoolSize(10);
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(30);

            String DB_DRIVER = "org.postgresql.Driver";
            String DB_CONNECTION = "jdbc:postgresql://"+IP_POSTGRES+":5432/Enem";
            String DB_USER = "postgres";
            String DB_PASSWORD = "postgres";

            String selectSQL = "SELECT * from inscrito_municipio_prova where co_inscricao = ?";

            Class.forName(DB_DRIVER);
            if (conn == null) {
                //conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                conn = cpds.getConnection();
            }
            PreparedStatement ps = conn.prepareStatement(selectSQL);
            ps.setLong(1, inscricao);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                inscrito = new InscritoMunicipioProva(rs.getLong("co_pessoa_fisica"), rs.getInt("co_projeto"), rs.getLong("co_inscricao"), rs.getString("no_inscrito"), rs.getString("nu_cpf"), rs.getString("tp_sexo"), rs.getString("nu_rg"), rs.getInt("co_uf_rg"), rs.getString("sg_uf_rg"), rs.getString("no_orgao_emissor"), rs.getString("sg_orgao_emissor"), new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("dt_nascimento")), rs.getLong("co_municipio_prova"), rs.getLong("co_uf"), rs.getString("sg_uf_rg"), rs.getInt("tp_lingua_estrangeira"), rs.getString("no_lingua_estrangeira"), rs.getInt("id_kit_prova"), rs.getString("no_social"), rs.getInt("tp_ambiente_sanitario"));
            }
            ps.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        return inscrito;
    }

    private static void preencheCache(List<InscritoMunicipioProva> inscritosdb, MemcachedCache cache) {
        inscritosdb.forEach(inscrito -> cache.put(inscrito.getCodigoInscricao().toString(), inscrito.valorString()));
        //inscritosdb.forEach(inscrito -> cache.put(inscrito.getCodigoInscricao().toString(), inscrito));
    }

    private static void preencheCache(List<InscritoMunicipioProva> inscritosdb, RemoteCacheManager cache) {
        inscritosdb.forEach(inscrito -> cache.getCache().put(inscrito.getCodigoInscricao().toString(), inscrito.valorString()));
        //inscritosdb.forEach(inscrito -> cache.put(inscrito.getCodigoInscricao().toString(), inscrito));
    }

    public static String jdgProperty(String name) {
        Properties props = new Properties();
        try {
            props.load(Manager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return props.getProperty(name);
    }
}
