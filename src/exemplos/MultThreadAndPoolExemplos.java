package exemplos;

import java.sql.Connection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.dbutils.DbUtils;
import thread.ThreadDinamica;
import bancoDeDados.ConexaoPool;
import constantes.ConstantesDBAcess;

/**
 * @author romatos
 * @version 1.0
 */

public class MultThreadAndPoolExemplos {

	public void criarPoolConexaoExemplo() {

		try {
			ConexaoPool.initDataSource("chave", ConstantesDBAcess.BANCOSCIENCEBCV);
			Connection conn = ConexaoPool.getConnection("chave");
			DbUtils.close(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ConexaoPool.endDataSource("chave");
	}

	public void metodoTeste(Integer i) {
		System.out.println("Metodos executado: " + i);
	}

	public void multThread() {

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
