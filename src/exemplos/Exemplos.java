package exemplos;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import thread.ThreadDinamica;
import utils.FileUtil;
import utils.SQLUtil;
import arquivos.excel.ArquivoExcel;
import bancoDeDados.ConexaoPool;

import com.eas.compression.CompressionException;

import constantes.ConstantesDBAcess;

/* CLASSE PARA TESTES */

public class Exemplos {

	public void criarPoolConexaoExemplo() {

		try {
			ConexaoPool.initDataSource("chave", ConstantesDBAcess.BANCOSCIENCEBCV);
			Connection conn = ConexaoPool.getConnection("chave");
			SQLUtil.closeConnection(conn);
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

	public void compactarArquivosBZ() {
		try {
			FileUtil.compactarArquivoBZ("D:\\teste.txt");
		} catch (CompressionException e) {
			e.printStackTrace();
		}
	}

	public void compactarArquivosZIP() {
		List<File> arquivos = new ArrayList<File>();
		arquivos.add(new File("D:\\teste\\arquivo1.txt"));
		arquivos.add(new File("D:\\teste\\arquivo2.txt"));
		try {
			FileUtil.compactarArquivosZip(arquivos, "D:\\teste\\teste.zip");
		} catch (Exception e1) {
			e1.printStackTrace();
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
	
	public void criarExcel() {
		try {
			ArquivoExcel arquivo = new ArquivoExcel("D:\\testes\\teste.xlsx");
			XSSFSheet aba = arquivo.addAba("Aba teste");
			Row linha = arquivo.addLinha(aba, 1);
			Cell celula = arquivo.addCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "Teste");
			arquivo.salvarArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void alterarExcel() {
		try {
			ArquivoExcel arquivo = new ArquivoExcel("D:\\testes\\teste.xlsx");
			XSSFSheet aba = arquivo.obterAba(0);
			Row linha = arquivo.obterLinha(aba, 0);
			Cell celula = arquivo.obterCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "novoTeste");
			arquivo.salvarArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
