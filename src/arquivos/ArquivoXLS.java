package arquivos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ArquivoXLS implements Serializable {

	private static final long serialVersionUID = -1189481367870293461L;

	/*********************************** ESCRITA ***********************************/

	
	/*********************************** LEITURA  ***********************************/
	public static Workbook abrirXLS(File arquivo) throws IOException, InvalidFormatException {
		return WorkbookFactory.create(new FileInputStream(arquivo.getAbsolutePath()));
	}

	public static Workbook abrirXLS(String dirArquivo) throws IOException, InvalidFormatException {
		return WorkbookFactory.create(new FileInputStream(dirArquivo));
	}
	
	public static Sheet obterAba(HSSFWorkbook workbook, Integer index) {
		return workbook.getSheetAt(index);
	}

	public static Row obterLinha(Sheet sheet, Integer indexLinha) {
		return sheet.getRow(indexLinha);
	}
	
	public static Cell obterCelula(Row linha, Integer indexCelula) {
		return linha.getCell(indexCelula);
	}
	
	public static Boolean isEmptyCell(Cell cell) {
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
