package br.com.template.exemplos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.template.utils.arquivo.FileUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class CompactarArquivoExemplos {

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

	public void descompactarArquivoZIP() {

		try {
			FileUtil.descompactarArquivo(new File("D:\\testes\\teste.zip"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void descompactarArquivoZIPPara() {

		try {
			FileUtil.descompactarArquivoPara(new File("D:\\testes\\teste.zip"), "D:\\");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
