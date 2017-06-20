package org.inep.robo.threads;

import org.inep.robo.InscritoMunicipioProva;
import org.inep.robo.Main;

public class Thread2 implements Runnable {

    @Override
    public void run() {

        System.out.println("  CONSULTA INSCRICOES NO CACHE ... ");
        long startTime = System.nanoTime();
        long inscricao = 1610377711l;
        for (int x = 0;x < Main.quantRegistros ;x++ ) {
            Main.inscritosCache.add((String) Main.memCachedCache.get(String.valueOf(inscricao)));
            inscricao++;
        }
        long endTime = System.nanoTime();
        long tempoCache = ((endTime - startTime) / 1000000);
        System.out.println("  TEMPO DA BUSCA NO CACHE: " + tempoCache + "ms");
        System.out.println("  QUANTIDADE INSCRITOS: " + Main.inscritosCache.size());
        System.out.println("  ------------------- ");

    }
}
