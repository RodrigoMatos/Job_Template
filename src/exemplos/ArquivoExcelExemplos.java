package exemplos;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import arquivos.excel.ArquivoExcel;

public class ArquivoExcelExemplos {

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
