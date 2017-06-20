package org.inep.robo;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Console;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Path("/rest")
public class Backend {

    private Logger logger = Logger.getLogger(this.getClass().toString());
    private static final String JDG_HOST = "jdg.host";
    // Memcached specific properties
    public static final String MEMCACHED_PORT = "jdg.memcached.port";
    private static final String PROPERTIES_FILE = "jdg.properties";
    private static final String CACHE_KEY = "inscritos";

    private MemcachedCache cache;
    List<InscritoMunicipioProva> inscritosdb = new ArrayList<InscritoMunicipioProva>();
    List<InscritoMunicipioProva> inscritosCache = new ArrayList<InscritoMunicipioProva>();
    List<String> inscricoes = new ArrayList<String>();

    @GET
    @Path("/dados/{inscricao}")
    @Produces("application/json")
    public Response getDados(@PathParam("inscricao") String inscricao) {

        cache = new MemcachedCache(jdgProperty(JDG_HOST), Integer.parseInt(jdgProperty(MEMCACHED_PORT)));

        logger.info("---------- INICIANDO CONSULTA DE INSCRITOS --------");
        if (cache.get("inscricoesOK") == null) {
            logger.info(" ------------------- ");
            logger.info("  INSCRICAO NAO ENCONTRADA - CACHE VAZIO");

            logger.info("  CONSULTA DADOS DO DB ");
            long startTime = System.nanoTime();
            consultaDados();
            long endTime = System.nanoTime();
            logger.info(" TEMPO DA BUSCA NO BANCO: " + ((endTime - startTime) / 1000000));
            logger.info(" ------------------- ");

            logger.info("  COLOCANDO TODA LISTA NO CACHE ... ");
            long startTime2 = System.nanoTime();
            //preencheCache(inscritosdb, cache);
            cache.put("inscricoes", inscritosdb);
            cache.put("inscricoesOK", true);
            long endTime2 = System.nanoTime();
            logger.info(" TEMPO GRAVANDO NO CACHE: " + ((endTime2 - startTime2) / 1000000));
            logger.info("  CACHE PREENCHIDO ");
        } else {
            logger.info(" ------------------- ");

            logger.info("  CONSULTA LISTA DE INSCRICOES NO CACHE ");
            long startTime = System.nanoTime();
            List<String> inscricoes = (List<String>) cache.get("inscricoes");
            long endTime = System.nanoTime();
            logger.info(" TEMPO DA BUSCA DE INSCRICOES: " + ((endTime - startTime) / 1000000));

            logger.info("  CONSULTA LISTA INTEIRA ");
            long startTime2 = System.nanoTime();
            List<InscritoMunicipioProva> inscritosCache = (List<InscritoMunicipioProva>) cache.get("inscricoes");
            //inscricoes.forEach(v -> inscritosCache.add((InscritoMunicipioProva) cache.get(v)));
            long endTime2 = System.nanoTime();
            logger.info(" TEMPO DA BUSCA NO CACHE: " + ((endTime2 - startTime2) / 1000000));
            logger.info(" ------------------- ");
        }

        logger.info("SIZE inscritosdb: " + inscritosdb.size());
        logger.info("SIZE inscritosCache: " + inscritosCache.size());

        inscritosdb = null;
        inscritosCache = null;

        return Response.status(Response.Status.OK).type(MediaType.TEXT_HTML_TYPE).build();
    }

    private void consultaDados() {
        String DB_DRIVER = "org.postgresql.Driver";
        String DB_CONNECTION = "jdbc:postgresql://localhost:5432/Enem";
        String DB_USER = "postgres";
        String DB_PASSWORD = "postgres";

        String selectSQL = "SELECT * from inscrito_municipio_prova";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName(DB_DRIVER);
            InitialContext ctx = new InitialContext();
            if (conn == null)
                conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            ps = conn.prepareStatement(selectSQL);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String inscricao = rs.getString("co_inscricao");
                InscritoMunicipioProva inscrito = new InscritoMunicipioProva(rs.getLong("co_pessoa_fisica"), rs.getInt("co_projeto"), rs.getLong("co_inscricao"), rs.getString("no_inscrito"), rs.getString("nu_cpf"), rs.getString("tp_sexo"), rs.getString("nu_rg"), rs.getInt("co_uf_rg"), rs.getString("sg_uf_rg"), rs.getString("no_orgao_emissor"), rs.getString("sg_orgao_emissor"), new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("dt_nascimento")), rs.getLong("co_municipio_prova"), rs.getLong("co_uf"), rs.getString("sg_uf_rg"), rs.getInt("tp_lingua_estrangeira"), rs.getString("no_lingua_estrangeira"), rs.getInt("id_kit_prova"), rs.getString("no_social"), rs.getInt("tp_ambiente_sanitario"));
                inscritosdb.add(inscrito);
                inscricoes.add(inscricao);
            }
            ps.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void preencheCache(List<InscritoMunicipioProva> inscritos, MemcachedCache cache) {
        inscritos.forEach(codigoInscricao -> cache.put(codigoInscricao.toString(), inscritos));
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
