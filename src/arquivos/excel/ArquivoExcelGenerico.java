package arquivos.excel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public abstract class ArquivoExcelGenerico implements Serializable {

	private static final long serialVersionUID = -7057834817253947024L;
	
	public void escreverNaCelula(Row linha, String conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public void escreverNaCelula(Row linha, Double conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public void escreverNaCelula(Row linha, Date conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public void escreverNaCelula(Row linha, Boolean conteudo, int indexColuna) {
		linha.createCell(indexColuna).setCellValue(conteudo);
	}

	public Cell escreverNaCelula(Cell celula, String conteudo) {
		celula.setCellValue(conteudo);
		return celula;
	}

	public Cell escreverNaCelula(Cell celula, Double conteudo) {
		celula.setCellValue(conteudo);
		return celula;
	}

	public Cell escreverNaCelula(Cell celula, Date conteudo) {
		celula.setCellValue(conteudo);
		return celula;
	}

	public Cell escreverNaCelula(Cell celula, Boolean conteudo) {
		celula.setCellValue(conteudo);
		return celula;
	}

	public Boolean isEmptyCell(Cell cell) {
		if (cell == null || StringUtils.isEmpty(cell.toString().trim())
				|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return true;
		}
		return false;
	}

	public String getStringCellValue(Cell cell) {
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

	public Double getNumericCellValue(Cell cell) {

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
