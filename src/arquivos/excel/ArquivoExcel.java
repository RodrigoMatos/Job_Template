package arquivos.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.FileUtil;

public class ArquivoExcel extends ArquivoExcelGenerico {

	private static final long serialVersionUID = -6223797011250863660L;

	private XSSFWorkbook workbook;
	private File arquivo;
	private OutputStream  fileOut;
	private InputStream  fileIn;
	private Boolean alteracao;

	public ArquivoExcel(File arquivo) throws Exception {
		this.arquivo = arquivo;
		this.criarWorkbook();
	}

	public ArquivoExcel(String arquivo) throws Exception {
		this.arquivo = new File(arquivo);
		this.criarWorkbook();
	}

	private void criarWorkbook() throws Exception {
		if (this.arquivo.isDirectory()) {
			return;
		}
		if (this.arquivo.exists()) {
			this.alteracao = true;
			this.workbook = new XSSFWorkbook(new FileInputStream(arquivo));
			this.fileIn = new FileInputStream(this.arquivo);
		} else {
			this.alteracao = false;
			this.fileOut = FileUtil.criarArquivoParaEscrita(this.arquivo);
			this.workbook = new XSSFWorkbook();
			System.out.println();
		}
	}

	public XSSFWorkbook getWorkbook() {
		return workbook;
	}

	public XSSFSheet obterAba(String nomeAba) {
		return this.workbook.getSheet(nomeAba);
	}

	public XSSFSheet obterAba(Integer index) {
		return this.workbook.getSheetAt(index);
	}

	public XSSFSheet addAba(String nomeAba) {
		return this.workbook.createSheet(nomeAba);
	}

	public Row addLinha(XSSFSheet aba, Integer index) {
		return aba.createRow(index);
	}

	public Row obterLinha(XSSFSheet aba, Integer index) {
		if (aba.getRow(index) == null) {
			this.addLinha(aba, index);
		}
		return aba.getRow(index);
	}

	public Cell addCelula(Row linha, Integer index) {
		return linha.createCell(index);
	}

	public Cell obterCelula(Row linha, Integer index) {
		if (this.isEmptyCell(linha.getCell(index))) {
			this.addCelula(linha, index);
		} 
		return linha.getCell(index);
	}

	public void salvarArquivo() throws IOException {
		if (this.alteracao) {
			this.fileIn.close();
			this.fileOut = new FileOutputStream(this.arquivo);
		}
		this.workbook.write(fileOut);
		this.fileOut.close();
		this.clear();
	}

	private void clear() {
		this.fileIn = null;
		this.fileOut = null;
		this.workbook = null;
		this.alteracao = null;
	}
}
