package org.inep.robo.threads;

import org.inep.robo.InscritoMunicipioProva;
import org.inep.robo.Main;
import org.inep.robo.MemcachedCache;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Thread1 implements Runnable {

    @Override
    public void run() {
        System.out.println("  CONSULTA INSCRICOES NO BANCO ...");
        long startTime = System.nanoTime();
        long inscricao = 1610377711l;
        for (int x = 0; x < Main.quantRegistros ; x++ ) {
            Main.inscritosdb.add(consultaInscrito(inscricao));
            inscricao++;
        }
        long endTime = System.nanoTime();
        long tempodb = ((endTime - startTime) / 1000000);
        System.out.println("  TEMPO DA BUSCA NO BANCO: " + tempodb + "ms");
        System.out.println("  QUANTIDADE INSCRITOS: " + Main.inscritosdb.size());
        System.out.println("  ------------------- ");

        System.out.println("  COLOCANDO LISTA NO CACHE ... ");
        startTime = System.nanoTime();
        preencheCache(Main.inscritosdb, Main.memCachedCache);
        endTime = System.nanoTime();
        System.out.println("  TEMPO GRAVANDO NO CACHE: " + ((endTime - startTime) / 1000000) + "ms");
        System.out.println("  CACHE PREENCHIDO ");
        System.out.println("  ------------------- ");
    }

    private static InscritoMunicipioProva consultaInscrito(long inscricao) {
        String DB_DRIVER = "org.postgresql.Driver";
        String DB_CONNECTION = "jdbc:postgresql://35.188.62.197:5432/Enem";
        String DB_USER = "postgres";
        String DB_PASSWORD = "postgres";

        String selectSQL = "SELECT * from inscrito_municipio_prova where co_inscricao = ?";
        InscritoMunicipioProva inscrito = null;

        try {
            Class.forName(DB_DRIVER);
            if (Main.conn == null) {
                Main.conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            }
            PreparedStatement ps = Main.conn.prepareStatement(selectSQL);
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
        }
        return inscrito;
    }

    private static void preencheCache(List<InscritoMunicipioProva> inscritosdb, MemcachedCache cache) {
        inscritosdb.forEach(inscrito -> cache.put(inscrito.getCodigoInscricao().toString(), inscrito));
    }
}
