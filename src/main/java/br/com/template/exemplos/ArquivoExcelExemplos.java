package br.com.template.exemplos;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import br.com.template.arquivos.excel.ArquivoExcel;
import br.com.template.bancoDeDados.ConexaoPool;
import br.com.template.constantes.ConstantesDBAcess;
import br.com.template.model.FtpVO;
import br.com.template.utils.arquivo.ExportacaoUtils;
import br.com.template.utils.arquivo.ImportacaoUtils;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ArquivoExcelExemplos {

	public static void criarExcel() {

		try {
			ArquivoExcel arquivo = new ArquivoExcel("D:\\testes\\teste.xls");
			Sheet aba = arquivo.addAba("Aba teste");
			Row linha = arquivo.addLinha(aba, 1);
			Cell celula = arquivo.addCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "Teste");
			arquivo.salvarArquivo();
			aba = arquivo.obterAba(0);
			linha =  arquivo.obterLinha(aba, 0);
			celula = arquivo.obterCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "novoTeste");
			arquivo.salvarArquivo();
			arquivo.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void alterarExcel() {

		try {
			ArquivoExcel arquivo = new ArquivoExcel("/home/solutis.net.br/rodrigo.matos/Downloads/teste.xls");
			Sheet aba = arquivo.obterAba(0);
			Row linha =  arquivo.obterLinha(aba, 0);
			Cell celula = arquivo.obterCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "novoTeste");
			arquivo.salvarArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportarConsulta() {

		try {
			ConexaoPool.initDataSource("SCIENCE", ConstantesDBAcess.BANCO_1);
			File arquivo = new File("D:\\testes\\consultaIgor.xlsx");
			ExportacaoUtils.exportarConsultaParaArquivoExcel("SELECT 1 FROM DUAL", arquivo, "BANCO_1");
			ConexaoPool.endDataSource("SCIENCE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportarLista() {

		List<FtpVO> list = new ArrayList<FtpVO>();
		list.add(new FtpVO("serv1", "user1", "senha1"));
		list.add(new FtpVO("serv2", "user2", "senha2"));
		list.add(new FtpVO("serv3", "user3", "senha3"));
		list.add(new FtpVO("serv4", "user4", "senha4"));

		List<String> atributos = new ArrayList<String>();

		atributos.add("servidor");
		atributos.add("usuario");
		atributos.add("senha");

		List<String> cabecalho = new ArrayList<String>();
		cabecalho.add("Servidor");
		cabecalho.add("Usuario");
		cabecalho.add("Senha");

		try {
			ExportacaoUtils.exportarListaExcel("D:\\testes\\lista.xlsx", list, atributos, cabecalho);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void importarExcel() {

		try {
			List<LinkedHashMap<Integer, Object>> list = ImportacaoUtils.importarExcel("D:\\testes\\consulta.xlsx", 0);
			Integer qtd;
			for (LinkedHashMap<Integer, Object> linha : list) {
				qtd = linha.size();
				for (Integer i = 0; i < qtd; i++) {
					System.out.print(linha.get(i) + " | ");
				}
				System.out.println("");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}