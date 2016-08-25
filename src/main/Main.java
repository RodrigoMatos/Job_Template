package main;

import exemplos.ArquivoExcelExemplos;

public class Main {

	public static void main(String[] args) {

		ArquivoExcelExemplos ex = new ArquivoExcelExemplos();
//		ex.descompactarArquivoZIPPara();
		
		ex.criarExcel();
		ex.alterarExcel();
	}

}
