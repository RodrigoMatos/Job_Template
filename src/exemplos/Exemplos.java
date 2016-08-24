package exemplos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import thread.ThreadDinamica;
import utils.FileUtil;
import utils.SQLUtil;
import arquivos.ArquivoXLS;
import bancoDeDados.ConexaoPool;

import com.eas.compression.CompressionException;

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
	
	
	public void compactarArquivosZIP() {
		List<File> arquivos = new ArrayList<File>();
		arquivos.add(new File("D:\\SMARTSTEPS_JOB.log"));
		arquivos.add(new File("D:\\COMMIT 14JOBS .sql"));
		try {
			FileUtil.compactarArquivosZip(arquivos, "D:\\teste.zip");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void compactarArquivosBZ() {
		try {
			FileUtil.compactarArquivoBZ("D:\\teste.txt");
		} catch (CompressionException e) {
			e.printStackTrace();
		}
	}
	
	public void descompactarArquivoZIP(){

		try {
			FileUtil.descompactarArquivo(new File("D:\\testes\\teste.zip"));
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void descompactarArquivoZIPPara(){

		try {
			FileUtil.descompactarArquivoPara(new File("D:\\testes\\teste.zip"),"D:\\");
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void lerXLS() {
//		try {
//			HSSFWorkbook w = ArquivoXLS.abrirXLS("D:\\testes\\teste.xls");
//			org.apache.poi.ss.usermodel.Sheet s = ArquivoXLS.obterAba(w, 0);
//			Row r = ArquivoXLS.obterLinha(s, 1);
//			ArquivoXLS.obterCelula(r, 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

//	public void criarXLS() {
//		try {
//			FileInputStream f = ArquivoXLS.criarArquivoXLS("D:\\testes\\teste.xls");
//			HSSFWorkbook workbook = new HSSFWorkbook(f);
//			HSSFSheet s = ArquivoXLS.obterAba(workbook, 0);
//			Row r = ArquivoXLS.obterLinha(s, 1);
//			ArquivoXLS.addConteudoColuna(r, "teste", 5);
//			FileOutputStream conteudo; 
//			IOUtils.copy(f, conteudo);
//			ArquivoXLS.salvarAlteracoes(workbook, conteudo);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
