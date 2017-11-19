package org.inep.robo;

import org.jboss.as.quickstarts.datagrid.memcached.Team;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

public class Manager {

    private Logger logger = Logger.getLogger(this.getClass().toString());
    private static final String JDG_HOST = "jdg.host";
    // Memcached specific properties
    public static final String MEMCACHED_PORT = "jdg.memcached.port";
    private static final String PROPERTIES_FILE = "jdg.properties";
    private static final String cacheKey = "inscritos";

    private Console con;
    private MemcachedCache cache;

    public Manager(Console con) {

        insereBanco();

        /*this.con = con;
        cache = new MemcachedCache(jdgProperty(JDG_HOST), Integer.parseInt(jdgProperty(MEMCACHED_PORT)));

        logger.info("VAZIO:  " + (cache.get(cacheKey) == null));
        List<String> inscritos = (List<String>) cache.get(cacheKey);

        logger.info("################### Inscrito teste: " + cache.get("1610477710"));

        //Preenchendo o cache
        //preencheCache(cache, inscritos);

        logger.info("################### Inscrito teste: " + ((InscritoMunicipioProva) cache.get("1610477710")).getNomeInscrito());
*/
    }

    private void preencheCache(MemcachedCache cache, List<String> inscritos) {
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
            logger.info("#### PREENCHENDO CACHE ###");
            while (rs.next()) {
                String inscricao = rs.getString("co_inscricao");
                InscritoMunicipioProva inscrito = new InscritoMunicipioProva(rs.getLong("co_pessoa_fisica"), rs.getInt("co_projeto"), rs.getLong("co_inscricao"), rs.getString("no_inscrito"), rs.getString("nu_cpf"), rs.getString("tp_sexo"), rs.getString("nu_rg"), rs.getInt("co_uf_rg"), rs.getString("sg_uf_rg"), rs.getString("no_orgao_emissor"), rs.getString("sg_orgao_emissor"), new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("dt_nascimento")), rs.getLong("co_municipio_prova"), rs.getLong("co_uf"), rs.getString("sg_uf_rg"), rs.getInt("tp_lingua_estrangeira"), rs.getString("no_lingua_estrangeira"), rs.getInt("id_kit_prova"), rs.getString("no_social"), rs.getInt("tp_ambiente_sanitario"));
                cache.put(inscricao, inscrito);
            }
            logger.info("#### PREENCHIDO ###");
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

    private void insereBanco() {
        String DB_DRIVER = "org.postgresql.Driver";
        String DB_CONNECTION = "jdbc:postgresql://35.188.62.197:5432/Enem";
        String DB_USER = "postgres";
        String DB_PASSWORD = "postgres";

        String selectSQL = "INSERT INTO inscrito_municipio_prova(\n" +
                "            co_pessoa_fisica, co_projeto, co_inscricao, no_inscrito, nu_cpf, \n" +
                "            tp_sexo, nu_rg, co_uf_rg, sg_uf_rg, no_orgao_emissor, sg_orgao_emissor, \n" +
                "            dt_nascimento, co_municipio_prova, co_uf, sg_uf, tp_lingua_estrangeira, \n" +
                "            no_lingua_estrangeira, id_kit_prova, no_social, tp_ambiente_sanitario)\n" +
                "    VALUES (?, ?, ?, ?, ?, \n" +
                "            ?, ?, ?, ?, ?, ?, \n" +
                "            ?, ?, ?, ?, ?, \n" +
                "            ?, ?, ?, ?);";
        Connection conn = null;
        PreparedStatement ps = null;

        long count = 1100855050l;
        long inscricao = 1610377710l;
        for (int x = 0;x < 150000 ;x++ ) {
            //logger.info("INSERINDO...");
            count++; inscricao++;

            try {
                Class.forName(DB_DRIVER);
                InitialContext ctx = new InitialContext();
                //DataSource ds = (DataSource) ctx.lookup("java:jboss/enemDS");
                if (conn == null)
                    conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
                //conn = ds.getConnection();
                ps = conn.prepareStatement(selectSQL);
                ps.setLong(1, count);
                ps.setInt(2, 1610401);
                ps.setLong(3, inscricao);
                ps.setString(4, "BRUNA ALVES DA SILVA " + inscricao);
                ps.setString(5, "60214342344");
                ps.setString(6, "F");
                ps.setString(7, "2002010271616");
                ps.setInt(8, 23);
                ps.setString(9, "CE");
                ps.setString(10, "Secretaria de Segurança Pública");
                ps.setString(11, "SSP");
                ps.setDate(12, new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse("02/09/1991").getTime()));
                ps.setLong(13, 2304400l);
                ps.setInt(14, 23);
                ps.setString(15, "CE");
                ps.setInt(16, 2);
                ps.setString(17, "ESPANHOL");
                ps.setInt(18, 1);
                ps.setString(19, "112233");
                ps.setInt(20, 5);

                int result = ps.executeUpdate();
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
    }

    public void addTeam() {
        logger.info("ADD TEAM");
        /*String teamName = con.readLine(msgEnterTeamName);
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(encode(cacheKey));
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        Team t = new Team(teamName);
        cache.put(encode(teamName), t);
        teams.add(teamName);
        cache.put(cacheKey, teams);*/
    }

    public void addPlayers() {
        logger.info("ADD TEAM");
        /*String teamName = con.readLine(msgEnterTeamName);
        String playerName = null;
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {
            while (!(playerName = con.readLine("Enter player's name (to stop adding, type \"q\"): ")).equals("q")) {
                t.addPlayer(playerName);
            }
            cache.put(encode(teamName), t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }*/
    }

    public void removePlayer() {
        /*String playerName = con.readLine("Enter player's name: ");
        String teamName = con.readLine("Enter player's team: ");
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {
            t.removePlayer(playerName);
            cache.put(encode(teamName), t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }*/
    }

    public void removeTeam() {
        /*String teamName = con.readLine(msgEnterTeamName);
        Team t = (Team) cache.get(encode(teamName));
        if (t != null) {
            cache.remove(encode(teamName));
            @SuppressWarnings("unchecked")
            List<String> teams = (List<String>) cache.get(teamsKey);
            if (teams != null) {
                teams.remove(teamName);
            }
            cache.put(teamsKey, teams);
        } else {
            con.printf(msgTeamMissing, teamName);
        }*/
    }

    public void printTeams() {
        logger.info("PRINT TEAMS");
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(cacheKey);
        if (teams != null) {
            for (String teamName : teams) {
                con.printf(cache.get(encode(teamName)).toString());
            }
        }
    }

    public static void main(String[] args) {
        Console con = System.console();
        Manager manager = new Manager(System.console());
        con.printf("Digite:");

        while (true) {
            String action = con.readLine(">");
            if ("at".equals(action)) {
                manager.addTeam();
            } else if ("ap".equals(action)) {
                manager.addPlayers();
            } else if ("rt".equals(action)) {
                manager.removeTeam();
            } else if ("rp".equals(action)) {
                manager.removePlayer();
            } else if ("p".equals(action)) {
                manager.printTeams();
            } else if ("q".equals(action)) {
                System.exit(0);
            }
        }
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

    public static String encode(String key) {
        try {
            return URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


}
