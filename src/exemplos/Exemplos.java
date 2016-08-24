package exemplos;

import java.sql.Connection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import thread.ThreadDinamica;
import utils.SQLUtil;
import conexaoBD.ConexaoPool;
import constantes.ConstantesDBAcess;

/* CLASSE PARA TESTES */

public class Exemplos {

	public void criarPoolConexaoExemplo() {

		try {
			// INICIAR POOL BANCO
			ConexaoPool.initDataSource("chave", ConstantesDBAcess.BANCOSCIENCEBCV);
			// OBTENDO CONEXAO
			Connection conn = ConexaoPool.getConnection("chave");
			// FINALIZANDO CONEXAO
			SQLUtil.closeConnection(conn);
			//
		} catch (Exception e) {
			e.printStackTrace();
		}
		// FINALIZANDO POOL
		ConexaoPool.endDataSource("chave");
	}
	
	public void metodoTeste(Integer i) {
		System.out.println("Metodos executado: " + i);
	}

	public void multThreadExemplo() {

		int quantidadeThread = 3;
		int tempoEsperaEntreThread = 0;
		int tamanhoDaFila = 5;

		ThreadPoolExecutor multiThread = new ThreadPoolExecutor(quantidadeThread, quantidadeThread, tempoEsperaEntreThread, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(tamanhoDaFila));

		multiThread.execute(new ThreadDinamica(this, "metodoTeste", 1));
		multiThread.execute(new ThreadDinamica(this, "metodoTeste", 2));
		multiThread.execute(new ThreadDinamica(this, "metodoTeste", 3));
		multiThread.execute(new ThreadDinamica(this, "metodoTeste", 4));
		multiThread.execute(new ThreadDinamica(this, "metodoTeste", 5));
		while (multiThread.getActiveCount() > 0);
		multiThread.shutdown();
	}

}
