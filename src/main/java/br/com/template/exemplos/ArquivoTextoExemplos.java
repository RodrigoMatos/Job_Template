package br.com.template.exemplos;

import java.util.ArrayList;
import java.util.List;

import br.com.template.arquivos.texto.ArquivoTexto;

/**
 * @author romatos
 * @version 1.0
 */

public class ArquivoTextoExemplos {

	public static void escreverAquivoTxt() {

		ArquivoTexto arquivoTxt = new ArquivoTexto("D:\\testes\\TESTE.TXT");
		try {
			arquivoTxt.limparConteudo();
			arquivoTxt.escreverNoArquivo("Escrevendo linha 01. ");
			arquivoTxt.escreverNoArquivo("Escrevendo linha 02. ");
			arquivoTxt.novaLinha(3);
			arquivoTxt.escreverNoArquivo(getListaString());
			arquivoTxt.novaLinha();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<String> getListaString() {

		List<String> linhas = new ArrayList<String>();
		linhas.add("Lista linha 1.");
		linhas.add("Lista linha 2.");
		linhas.add("Lista linha 3.");
		linhas.add("Lista linha 4.");
		linhas.add("Lista linha 5.");
		linhas.add("Lista linha 6.");
		return linhas;
	}

}
