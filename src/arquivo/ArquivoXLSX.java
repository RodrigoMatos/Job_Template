package arquivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.FileUtil;

public class ArquivoXLSX {

	/*********************************** ESCRITA ***********************************/

	public static FileOutputStream criarArquivoXLSX(String dirArquivo) throws FileNotFoundException {
		FileUtil.criarDirs(dirArquivo);
		File arquivo = new File(dirArquivo);
		FileOutputStream fileOut = new FileOutputStream(arquivo);
		return fileOut;
	}

	public static Sheet criarAba(Workbook wb, String nomeAba) {
		if (wb != null) {
			return wb.createSheet(nomeAba);
		} else {
			return null;
		}
	}

	public static Row criarLinha(Sheet aba, short indexLinha) {
		return aba.createRow(indexLinha);
	}

	public static void addConteudoColuna(Row linha, String conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public static void addConteudoColuna(Row linha, Double conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public static void addConteudoColuna(Row linha, Date conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public static void addConteudoColuna(Row linha, Boolean conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public static void salvarAlteracoes(Workbook wb, FileOutputStream conteudo) throws IOException {
		wb.write(conteudo);
		conteudo.close();
	}

	/*********************************** LEITURA ***********************************/

	public static XSSFWorkbook abrirXLSX(String dirArquivo) throws IOException {
		return new XSSFWorkbook(new FileInputStream(dirArquivo));
	}

	public static Sheet obterAba(XSSFWorkbook workbook, short indexAba) {
		return workbook.getSheetAt(0);
	}

	public static Row obterLinha(Sheet worksheet, int indexLinha) {
		return worksheet.getRow(indexLinha);
	}

	public static Cell obterCelula(Row linha, int indexCelula) {
		return linha.getCell(indexCelula);
	}

	public static boolean isEmptyCell(Cell cell) {
		if (cell == null || StringUtils.isEmpty(cell.toString().trim())
				|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return true;
		}
		return false;
	}

	public static String getStringCellValue(Cell cell) {
		if (isEmptyCell(cell)) {
			return null;
		}
		String value = null;
		if (cell != null) {
			if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
				value = cell.getStringCellValue().trim();
			} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
				value = Double.valueOf(cell.getNumericCellValue()).intValue() + "";
			}
		}
		return value;
	}
	
	public Date getDateCellValue(Cell cell) throws ParseException {
		if (isEmptyCell(cell)) {
			return null;
		}
		if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			try {
				return format.parse(cell.getStringCellValue());
			} catch (ParseException e) {
				throw new ParseException(e.getLocalizedMessage(), 0);
			}
		} else {
			return cell.getDateCellValue();
		}
	}

	public static Double getNumericCellValue(Cell cell) {

		Double value = null;
		if (isEmptyCell(cell)) {
			value = 0.0;
			return value;
		}
		try {
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					value = new Double(cell.getStringCellValue().replace(",","."));
				} else {
					value = cell.getNumericCellValue();
				}
			}
		} catch (Exception e) {
			value = 0.0;
		}
		return value;
	}
	
	public Long getLongCellValue(Cell cell) {		

		Long value = null;
		if (isEmptyCell(cell)) {
			value = new Long(0);
			return value;
		}
		try {
			if (cell != null) {
				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					value = new Long(cell.getStringCellValue());
				} else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					Double numero = cell.getNumericCellValue();
					value = Math.round(numero);
				} else {
					value = Long.parseLong(cell.getStringCellValue());
				}
			}
		} catch (Exception e) {
			value = new Long(0);
		}
		return value;
	}

}
