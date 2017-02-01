package arquivos.excel;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ArquivoExcelGenerico implements Serializable {

	private static final long serialVersionUID = -7057834817253947024L;

	/**
	 * @author romatos
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteúdo - Conteudo String que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Row linha, Integer indexColuna, String conteudo) {
		if (linha.getCell(indexColuna) == null) {
			linha.createCell(indexColuna);
		}
		return this.escreverNaCelula(linha.getCell(indexColuna), conteudo);
	}

	/**
	 * @author romatos
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteudo - Conteúdo Double que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Row linha, Integer indexColuna, Double conteudo) {
		if (linha.getCell(indexColuna) == null) {
			linha.createCell(indexColuna);
		}
		return this.escreverNaCelula(linha.getCell(indexColuna), conteudo);
	}

	/**
	 * @author romatos
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteudo - Conteúdo Date que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Row linha, Integer indexColuna, Date conteudo) {
		if (linha.getCell(indexColuna) == null) {
			linha.createCell(indexColuna);
		}
		return this.escreverNaCelula(linha.getCell(indexColuna), conteudo);
	}

	/**
	 * @author romatos
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteudo - Conteúdo Boolean que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Row linha, Integer indexColuna, Boolean conteudo) {
		if (linha.getCell(indexColuna) == null) {
			linha.createCell(indexColuna);
		}
		return this.escreverNaCelula(linha.getCell(indexColuna), conteudo);
	}

	/**
	 * @author romatos
	 * @param celula - Celula que irá escrever o valor.
	 * @param conteudo - Conteúdo String que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Cell celula, String conteudo) {
		if (conteudo != null)
			celula.setCellValue(conteudo);
		return celula;
	}

	/**
	 * @author romatos
	 * @param celula - Celula que irá escrever o valor.
	 * @param conteudo - Conteúdo Double que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Cell celula, Double conteudo) {
		if (conteudo != null)
			celula.setCellValue(conteudo);
		return celula;
	}

	/**
	 * @author romatos
	 * @param celula - Celula que irá escrever o valor.
	 * @param conteudo - Conteúdo Date que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Cell celula, Date conteudo) {
		if (conteudo != null)
			celula.setCellValue(conteudo);
		return celula;
	}

	/**
	 * @author romatos
	 * @param celula - Celula que irá escrever o valor.
	 * @param conteudo - Conteúdo Boolean que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Cell celula, Boolean conteudo) {
		if (conteudo != null)
			celula.setCellValue(conteudo);
		return celula;
	}

	/**
	 * @author romatos
	 * @param cell - Celula que será verificada.
	 * @return Retorna true ou false para caso a celula esteja vazia.
	 */
	public Boolean isEmptyCell(Cell cell) {
		if (cell == null || StringUtils.isEmpty(cell.toString().trim())
				|| cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			return true;
		}
		return false;
	}

	/**
	 * @author romatos
	 * @param cell - Celula que será verificada.
	 * @return Retorna o conteúdo em String existente na celula.
	 */
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

	/**
	 * @author romatos
	 * @param cell - Celula que será verificada.
	 * @return Retorna o conteúdo em Date existente na celula.
	 */
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

	/**
	 * @author romatos
	 * @param cell - Celula que será verificada.
	 * @return Retorna o conteúdo em Double existente na celula.
	 */
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

	/**
	 * @author romatos
	 * @param cell - Celula que será verificada.
	 * @return Retorna o conteúdo em Long existente na celula.
	 */
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
