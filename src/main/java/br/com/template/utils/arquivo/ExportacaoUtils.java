package br.com.template.utils.arquivo;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import br.com.template.arquivos.excel.ArquivoExcel;
import br.com.template.bancoDeDados.dao.DAO;
import br.com.template.reflexao.ReflexaoUtils;
import br.com.template.utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public final class ExportacaoUtils {

	private ExportacaoUtils() {
	}

	/**
	 * @author romatos
	 * @param arquivo   - Caminho do arquivo que ser� exportado.
	 * @param list      - Lista contendo os registros que ser�o exportados.
	 * @param atributos - Nome dos atributos que ser�o utilizados para preencher os
	 *                  dados do excel.
	 * @param cabecalho - Nome do cabe�alho para o arquivo excel.
	 * @throws Exception
	 */
	public static void exportarListaExcel(String arquivo, List<?> list, List<String> atributos, List<String> cabecalho)
			throws Exception {
		exportarListaExcel(new File(arquivo), list, atributos, cabecalho);
	}

	/**
	 * @author romatos
	 * @param arquivo   - Arquivo que ser� gerado.
	 * @param list      - Lista contendo os registros que ser�o exportados.
	 * @param atributos - Nome dos atributos que ser�o utilizados para preencher os
	 *                  dados do excel.
	 * @param cabecalho - Nome do cabe�alho para o arquivo excel.
	 * @throws Exception
	 */
	public static void exportarListaExcel(File arquivo, List<?> listaExportar, List<String> atributos,
			List<String> cabecalho) throws Exception {

		FileUtil.criarDirs(arquivo.getParentFile());
		ArquivoExcel arquivoExcel = new ArquivoExcel(arquivo);
		Sheet aba = null;
		if (arquivoExcel.getAlteracao()) {
			aba = arquivoExcel.obterAba(0);
		} else {
			aba = arquivoExcel.addAba("Plan1");
		}
		Row linha;
		Cell celula;
		Object valorGet = null;
		int indexCelula = 0;
		int indexLinha = 0;

		if (cabecalho != null && !cabecalho.isEmpty()) {
			// CRIAR CABE�ALHO
			linha = arquivoExcel.addLinha(aba, indexLinha++);
			for (String cab : cabecalho) {
				celula = arquivoExcel.addCelula(linha, indexCelula++);
				arquivoExcel.escreverNaCelula(celula, cab);
			}
		} else if (atributos != null && !atributos.isEmpty()) {
			linha = arquivoExcel.addLinha(aba, indexLinha++);
			for (String cab : atributos) {
				celula = arquivoExcel.addCelula(linha, indexCelula++);
				arquivoExcel.escreverNaCelula(celula, cab);
			}
		}
		// ESCREVER REGISTROS
		for (Object obj : listaExportar) {
			linha = arquivoExcel.addLinha(aba, indexLinha++);
			indexCelula = 0;
			for (String atributo : atributos) {
				celula = arquivoExcel.addCelula(linha, indexCelula++);
				valorGet = ReflexaoUtils.executarMetodoGet(obj, atributo);
				if (valorGet == null) {
					valorGet = "";
				}
				arquivoExcel.escreverNaCelula(celula, valorGet.toString());
			}
		}
		// SALVAR
		arquivoExcel.AutoDimensionarColunas(aba);
		arquivoExcel.salvarArquivo();
	}

	/**
	 * @author romatos
	 * @param arquivo           - Caminho do arquivo que ser� gerado.
	 * @param consulta          - Consulta SQL que ir� conter os arquivo.
	 * @param chaveConexao      - Chave da conex�o do banco que ser� utilizada para
	 *                          consulta.
	 * @param separador         - String que ser� inserida entre cada coluna da
	 *                          consulta.
	 * @param prefixo           - String que ser� inserida antes do texto de cada
	 *                          registro.
	 * @param sufixo            - String que ser� inserida depois do texto de cada
	 *                          registro.
	 * @param escreverCabecalho - Valor que define se ser� adicionado o cabe�alho no
	 *                          arquivo de acordo com a coluna da consulta.
	 * @throws Exception
	 */
	public static void exportarConsultaParaArquivoTexto(String arquivo, String consulta, String chaveConexao,
			String separador, String prefixo, String sufixo, boolean escreverCabecalho) throws Exception {
		File arquivoCsv = new File(arquivo);
		if (arquivoCsv.getParentFile() != null)
			FileUtil.criarDirs(arquivoCsv.getParentFile());
		exportarConsultaParaArquivoTexto(arquivoCsv, consulta, separador, chaveConexao, prefixo, sufixo,
				escreverCabecalho);
	}

	/**
	 * 
	 * @param arquivo           - Arquivo que ser� gerado.
	 * @param consulta          - Consulta SQL que ir� conter os arquivo.
	 * @param chaveConexao      - Chave da conex�o do banco que ser� utilizada para
	 *                          consulta.
	 * @param separador         - String que ser� inserida entre cada coluna da
	 *                          consulta.
	 * @param prefixo           - String que ser� inserida antes do texto de cada
	 *                          registro.
	 * @param sufixo            - String que ser� inserida depois do texto de cada
	 *                          registro.
	 * @param escreverCabecalho - Valor que define se ser� adicionado o cabe�alho no
	 *                          arquivo de acordo com a coluna da consulta.
	 * @throws Exception
	 */
	public static void exportarConsultaParaArquivoTexto(File arquivo, String consulta, String chaveConexao,
			String separador, String prefixo, String sufixo, boolean escreverCabecalho) throws Exception {

		try {
			LogUtil.Info("EXPORTANDO ARQUIVO " + arquivo.getAbsolutePath() + " ...");
			prefixo = prefixo == null ? "" : prefixo;
			sufixo = sufixo == null ? "" : sufixo;
			int qtdLinhas = DAO.exportarConsultaParaCsv(arquivo, consulta, chaveConexao, separador, prefixo, sufixo,
					escreverCabecalho);
			LogUtil.Info("ARQUIVO EXPORTADO COM SUCESSO (" + qtdLinhas + " LINHAS EXPORTADAS).");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO EXPORTAR ARQUIVO: " + e.getMessage());
			throw e;
		}
	}

	/**
	 * @author romatos
	 * @param consulta     - Consulta SQL que ir� conter os arquivo.
	 * @param dirArquivo   - Caminho do arquivo que ser� gerado.
	 * @param chaveConexao - Chave da conex�o do banco que ser� utilizada para
	 *                     consulta.
	 * @throws Exception
	 */
	public static void exportarConsultaParaArquivoExcel(String consulta, String dirArquivo, String chaveConexao)
			throws Exception {
		exportarConsultaParaArquivoExcel(consulta, new File(dirArquivo), chaveConexao);
	}

	/**
	 * @author romatos
	 * @param consulta     - Consulta SQL que ir� conter os arquivo.
	 * @param arquivo      - Arquivo que ser� gerado.
	 * @param chaveConexao - Chave da conex�o do banco que ser� utilizada para
	 *                     consulta.
	 * @throws Exception
	 */
	public static void exportarConsultaParaArquivoExcel(String consulta, File arquivo, String chaveConexao)
			throws Exception {

		FileUtil.criarDirs(arquivo.getParentFile());
		List<LinkedHashMap<String, Object>> lista = DAO.realizarConsultaGenerica(consulta, chaveConexao);

		ArquivoExcel arquivoExcel = new ArquivoExcel(arquivo);
		Sheet aba = null;
		if (arquivoExcel.getAlteracao()) {
			aba = arquivoExcel.obterAba(0);
		} else {
			aba = arquivoExcel.addAba("Plan1");
		}
		Row linha;
		Cell celula;
		Set<String> nomeColunas = null;
		Object valor;
		int indexCelula = 0;
		int indexLinha = 0;

		for (LinkedHashMap<String, Object> registro : lista) {

			if (nomeColunas == null) {// CRIAR CABE�ALHO
				nomeColunas = registro.keySet();
				linha = arquivoExcel.addLinha(aba, indexLinha++);
				for (String coluna : nomeColunas) {
					celula = arquivoExcel.addCelula(linha, indexCelula++);
					arquivoExcel.escreverNaCelula(celula, coluna);
				}
				linha = null;
			}

			indexCelula = 0;
			linha = arquivoExcel.addLinha(aba, indexLinha++);

			for (String coluna : nomeColunas) {
				valor = registro.get(coluna);
				celula = arquivoExcel.addCelula(linha, indexCelula++);
				valor = valor == null ? "" : valor;
				try {
					arquivoExcel.escreverNaCelula(celula, valor.toString());
				} catch (Exception e) {
					LogUtil.Error("Erro ao escrever: " + e.getMessage());
				}
			}
		}
		arquivoExcel.AutoDimensionarColunas(aba);
		arquivoExcel.salvarArquivo();
	}
}
