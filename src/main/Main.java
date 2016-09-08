package main;

import exemplos.*;

public class Main {

	public static void main(String[] args) {

		/*
		ArquivoExcelExemplos ex = new ArquivoExcelExemplos();
		ex.criarExcel();
		ex.alterarExcel();
		 */
		/*
		XMLExemplos ex = new XMLExemplos();
		
		ex.lerArquivoXML();
		*/
		
		FtpSftpExemplos sftp = new FtpSftpExemplos();
		sftp.baixarArquivo();
	}

}
