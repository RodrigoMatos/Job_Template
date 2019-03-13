package br.com.template.arquivos.texto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.List;

import br.com.template.utils.arquivo.FileUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class ArquivoTexto implements Serializable {

	private static final long serialVersionUID = 7748513800306491475L;

	private File arquivo;
	private FileWriter fw;
	private BufferedWriter bw;
	private Boolean manterConteudo;

	/**
	 * @author romatos
	 * @param arquivo - Caminho do arquivo que ser� utilizado.
	 */
	public ArquivoTexto(String arquivo) {
		this.arquivo = new File(arquivo);
		this.manterConteudo = true;
	}

	/**
	 * @author romatos
	 * @param arquivo - Arquivo que ser� utilizado.
	 */
	public ArquivoTexto(File arquivo) {
		this.arquivo = arquivo;
		this.manterConteudo = true;
	}

	/**
	 * @author romatos
	 * @param linha - Conte�do que ser� escrito na linha.
	 * @throws Exception
	 * Escreve uma linha no arquivo.
	 */
	public void escreverNoArquivo(String linha) throws Exception {

		this.verificarArquivoPreparado();
		this.bw = FileUtil.escreverNoArquivo(this.fw, linha);
		this.bw.close();
		this.limparCache();
	}

	/**
	 * @author romatos
	 * @param lines - Lista com os conte�dos que ser�o escritos no arquivo.
	 * @throws Exception
	 * Escreve varias linhas no arquivo.
	 */
	public void escreverNoArquivo(List<String> lines) throws Exception {

		this.verificarArquivoPreparado();
		this.bw = FileUtil.escreverNoArquivo(this.fw, lines);
		this.bw.close();
		this.limparCache();
	}

	/**
	 * @author romatos
	 * @throws Exception
	 * Adicionar nova linha no arquivo.
	 */
	public void novaLinha() throws Exception {

		this.verificarArquivoPreparado();
		this.bw = FileUtil.novaLinha(this.fw);
		this.bw.close();
		this.limparCache();
	}

	/**
	 * @author romatos
	 * @param quantidade - Quantidades de linhas que ser�o adicionadas.
	 * @throws Exception
	 */
	public void novaLinha(Integer quantidade) throws Exception {

		for (int i = 0; i < quantidade; i++) {
			this.novaLinha();
		}
	}

	private void verificarArquivoPreparado() throws Exception {

		if (!this.arquivo.exists()) {
			FileUtil.criarArquivo(this.arquivo);
		}
		if (this.fw == null) {
			this.fw = FileUtil.prepararArquivoParaEscrita(this.arquivo, this.manterConteudo);
		}
	}

	private void limparCache() {
		this.fw = null;
		this.bw = null;
	}

	/**
	 * @author romatos
	 * @return - Retorna o arquivo em uso.
	 */
	public File getArquivo() {
		return this.arquivo;
	}

	/**
	 * @author romatos
	 * @param arquivo - Arquivo que ser� utilizado.
	 */
	public void setArquivo(File arquivo) {
		this.arquivo = arquivo;
		this.limparCache();
	}

	/**
	 * @author romatos
	 * @param arquivo - caminho do arquivo que ser� utilizado.
	 */
	public void setArquivo(String arquivo) {
		this.arquivo = new File(arquivo);
		this.limparCache();
	}

	/**
	 * @author romatos
	 * @throws Exception 
	 * Apaga todo o conteudo do arquivo.
	 */
	public void limparConteudo() throws Exception {
		this.manterConteudo = false;
		this.escreverNoArquivo("");
		this.manterConteudo = true;
	}

}
