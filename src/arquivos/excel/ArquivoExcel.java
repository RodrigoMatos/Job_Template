package arquivos.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.FileUtil;
import utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class ArquivoExcel extends ArquivoExcelGenerico {

	private static final long serialVersionUID = -6223797011250863660L;

	private Workbook workbook;
	private File arquivo;
	private OutputStream fileOut;
	private InputStream fileIn;
	private Boolean alteracao;
	private Boolean arquivoXlsx;

	public ArquivoExcel(File arquivo) throws Exception {
		this.arquivo = arquivo;
		this.instanciar();
	}

	public ArquivoExcel(String arquivo) throws Exception {
		this.arquivo = new File(arquivo);
		this.instanciar();
	}

	private void instanciar() throws Exception {
		this.validarFormato();
		this.criarWorkbook();
	}

	private void validarFormato() throws Exception {

		String extensao = this.arquivo.getAbsolutePath().substring(this.arquivo.getAbsolutePath().lastIndexOf("."), this.arquivo.getAbsolutePath().length());
		if (".xls".equalsIgnoreCase(extensao)) {
			this.arquivoXlsx = Boolean.FALSE;
		} else if (".xlsx".equalsIgnoreCase(extensao)) {
			this.arquivoXlsx = Boolean.TRUE;
		} else {
			throw new Exception("Formato de arquivo invalido.");
		}
	}

	private void criarWorkbook() throws Exception {

		if (this.arquivo.isDirectory()) {
			return;
		}
		if (this.arquivo.exists()) {
			this.alteracao = true;
			if (this.arquivoXlsx) {
				this.workbook = new XSSFWorkbook(new FileInputStream(this.arquivo));
			} else {
				this.workbook = new HSSFWorkbook(new FileInputStream(this.arquivo));
			}
			this.fileIn = new FileInputStream(this.arquivo);
		} else {
			this.alteracao = false;
			this.fileOut = FileUtil.criarArquivoParaEscrita(this.arquivo);
			if (this.arquivoXlsx) {
				this.workbook = new XSSFWorkbook();
			} else {
				this.workbook = new HSSFWorkbook();
			}
		}
	}

	/**
	 * @author isouzaa
	 * @param aba - Aba (Sheet) que será auto dimensionada.
	 * Ajusta a largura das colunas da aba de forma automatica.
	 */
	public void AutoDimensionarColunas(Sheet aba) {
		if (aba != null) {
			for (int i = 0; i < this.getNumeroDeColunas(aba); i++) {
				aba.autoSizeColumn(i);
			}
		}
	}

	/**
	 * @author isouzaa
	 * @param indexAba - Indice da aba que será auto dimensionada.
	 * Ajusta a largura das colunas da aba de forma automatica.
	 */
	public void AutoDimensionarColunas(Integer indexAba) {
		this.AutoDimensionarColunas(this.workbook.getSheetAt(indexAba));
	}

	/**
	 * @author isouzaa
	 * @param nomeAba - Nome da aba que será auto dimensionada.
	 * Ajusta a largura das colunas da aba de forma automatica.
	 */
	public void AutoDimensionarColunas(String nomeAba) {
		this.AutoDimensionarColunas(this.workbook.getSheet(nomeAba));
	}

	/**
	 * @author isouzaa
	 * @param aba - Aba (Sheet) que será utilizada para contar as colunas.
	 * @return Retorna a quantidade de colunas de uma aba.
	 */
	public Integer getNumeroDeColunas(Sheet aba) {

		Integer numeroDeColunas = 0;
		if (aba != null) {
			int numeroDeLinhas = aba.getLastRowNum();
			short numeroUltimaCelula = 0;
			for (int i = 0; i < numeroDeLinhas; i++) {
				numeroUltimaCelula = aba.getRow(i).getLastCellNum();
				if (numeroDeColunas < numeroUltimaCelula) {
					numeroDeColunas = new Integer(numeroUltimaCelula);
				}
			}
		}
		return numeroDeColunas;
	}

	/**
	 * @author isouzaa
	 * @param indiceAba - Indice da aba que será utilizada para contar as colunas.
	 * @return Retorna a quantidade de colunas de uma aba.
	 */
	public Integer getNumeroDeColunas(Integer indiceAba) {
		return this.getNumeroDeColunas(this.workbook.getSheetAt(indiceAba));
	}

	/**
	 * @author isouzaa
	 * @param nomeAba - Nome da aba que será utilizada para contar as colunas.
	 * @return Retorna a quantidade de colunas de uma aba.
	 */
	public Integer getNumeroDeColunas(String nomeAba) {
		return this.getNumeroDeColunas(this.workbook.getSheet(nomeAba));
	}

	/**
	 * @author romatos
	 * @return Retorna o workbook do arquivo.
	 */
	public Workbook getWorkbook() {
		return this.workbook;
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba que será retornada
	 * @return Retorna um objeto Sheet correspondente a aba do parâmetro.
	 */
	public Sheet obterAba(String nomeAba) {
		return this.workbook.getSheet(nomeAba);
	}

	/**
	 * @author romatos
	 * @param index - Indice da aba que será retornada
	 * @return Retorna um objeto Sheet correspondente a aba do parâmetro.
	 */
	public Sheet obterAba(Integer index) {
		return this.workbook.getSheetAt(index);
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba que será criada.
	 * @return Retorna a aba crida.
	 */
	public Sheet addAba(String nomeAba) {
		return this.workbook.createSheet(nomeAba);
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba que será criada a linha.
	 * @param indexLinha - Indice da linha que será criada.
	 * @return Retorna a linha criada
	 */
	public Row addLinha(String nomeAba, Integer indexLinha) {
		return this.obterAba(nomeAba).createRow(indexLinha);
	}

	/**
	 * @author romatos
	 * @param indexAba - Indice da aba que será criada a linha.
	 * @param indexLinha - Indice da linha que será criada.
	 * @return Retorna a linha criada
	 */
	public Row addLinha(Integer indexAba, Integer indexLinha) {
		return this.obterAba(indexAba).createRow(indexLinha);
	}

	/**
	 * @author romatos
	 * @param aba - Aba que será criada a linha.
	 * @param indexLinha - Indice da linha que será criada.
	 * @return Retorna a linha criada
	 */
	public Row addLinha(Sheet aba, Integer indexLinha) {
		return aba.createRow(indexLinha);
	}

	/**
	 * @author romatos
	 * @param aba - Aba que irá buscar a linha.
	 * @param indexLinha - Indice da linha que será obtida.
	 * @return Retorna o objeto Row correspondente a linha encontra.
	 */
	public Row obterLinha(Sheet aba, Integer indexLinha) {

		if (aba.getRow(indexLinha) == null) {
			this.addLinha(aba, indexLinha);
		}
		return aba.getRow(indexLinha);
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba que irá buscar a linha.
	 * @param indexLinha - Indice da linha que será obtida.
	 * @return Retorna o objeto Row correspondente a linha encontra.
	 */
	public Row obterLinha(String nomeAba, Integer indexLinha) {
		return this.obterLinha(this.obterAba(nomeAba), indexLinha);
	}

	/**
	 * @author romatos
	 * @param indexAba - Indice da aba que irá buscar a linha.
	 * @param indexLinha - Indice da linha que será obtida.
	 * @return Retorna o objeto Row correspondente a linha encontra.
	 */
	public Row obterLinha(Integer indexAba, Integer indexLinha) {
		return this.obterLinha(this.obterAba(indexAba), indexLinha);
	}

	/**
	 * @author romatos
	 * @param indexAba - Indice da aba que será criada a coluna.
	 * @param indexLinha - Indice da linha que será criada a coluna.
	 * @param indexCelula - Indice da coluna que será criada.
	 * @return Retorna o objeto Cell correspondente a celula criada.
	 */
	public Cell addCelula(Integer indexAba, Integer indexLinha, Integer indexCelula) {
		return this.obterLinha(this.obterAba(indexAba), indexLinha).createCell(indexCelula);
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba que será criada a coluna.
	 * @param indexLinha - Indice da linha que será criada a coluna.
	 * @param indexCelula - Indice da coluna que será criada.
	 * @return Retorna o objeto Cell correspondente a celula criada.
	 */
	public Cell addCelula(String nomeAba, Integer indexLinha, Integer indexCelula) {
		return this.obterLinha(this.obterAba(nomeAba), indexLinha).createCell(indexCelula);
	}

	/**
	 * @author romatos
	 * @param linha - Objeto Row correspondente a linha que será criada a coluna.
	 * @param index - Indice da coluna que será criada.
	 * @return Retorna o objeto Cell correspondente a celula criada.
	 */
	public Cell addCelula(Row linha, Integer index) {
		return linha.createCell(index);
	}

	/**
	 * @author romatos
	 * @param linha - Objeto Row correspondente a linha. 
	 * @param index - Indice da celula.
	 * @return Retorna um objeto Cell.
	 */
	public Cell obterCelula(Row linha, Integer index) {
		if (this.isEmptyCell(linha.getCell(index))) {
			this.addCelula(linha, index);
		}
		return linha.getCell(index);
	}

	/**
	 * @author romatos
	 * @param aba - Aba que será utilizada.
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteúdo - Conteudo String que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Sheet aba, Integer indexLinha, Integer indexColuna, String conteudo) {
		Row linha = this.obterLinha(aba, indexLinha);
		Cell celula = this.obterCelula(linha, indexColuna);
		return this.escreverNaCelula(celula, conteudo);
	}

	/**
	 * @author romatos
	 * @param indexAba - Indice da aba.
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteúdo - Conteudo String que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(Integer indexAba, Integer indexLinha, Integer indexColuna, String conteudo) {
		return this.escreverNaCelula(this.obterAba(indexAba), indexLinha, indexColuna, conteudo);
	}

	/**
	 * @author romatos
	 * @param nomeAba - Nome da aba.
	 * @param linha - Indice da linha.
	 * @param indexColuna - Indice da coluna que irá escrever o valor.
	 * @param conteúdo - Conteudo String que será escrito na celula.
	 * @return Retorna a celula que foi escrita.
	 */
	public Cell escreverNaCelula(String nomeAba, Integer indexLinha, Integer indexColuna, String conteudo) {
		return this.escreverNaCelula(this.obterAba(nomeAba), indexLinha, indexColuna, conteudo);
	}

	/**
	 * @author romatos
	 * @param aba - aba que será utilizada para realizar a função de mesclar linha/celula.
	 * @param indexLinhaDe - Indice da linha que irá iniciar a mesclagem.
	 * @param indexLinhaPara - Indice da linha que irá terminar a mesclagem.
	 * @param indexColunaDe - Indice da coluna que irá iniciar a mesclagem.
	 * @param indexColunaPara - Indice da coluna que irá terminar a mesclagem.
	 */
	public void mesclar(Sheet aba, Integer indexLinhaDe, Integer indexLinhaPara, Short indexColunaDe, Short indexColunaPara) {
		aba.addMergedRegion(new CellRangeAddress(indexLinhaPara, indexLinhaPara, indexColunaDe, indexColunaPara));
	}

	/**
	 * @author romatos
	 * @param indexAba - Index da aba que será utilizada para realizar a função de mesclar linha/celula.
	 * @param indexLinhaDe - Indice da linha que irá iniciar a mesclagem.
	 * @param indexLinhaPara - Indice da linha que irá terminar a mesclagem.
	 * @param indexColunaDe - Indice da coluna que irá iniciar a mesclagem.
	 * @param indexColunaPara - Indice da coluna que irá terminar a mesclagem.
	 */
	public void mesclar(Integer indexAba, Integer indexLinhaDe, Integer indexLinhaPara, Short indexColunaDe, Short indexColunaPara) {
		this.obterAba(indexAba).addMergedRegion(new CellRangeAddress(indexLinhaPara, indexLinhaPara, indexColunaDe, indexColunaPara));
	}

	/**
	 * @author romatos
	 * @throws Exception
	 * Salvar as alterações realizadas no arquivo.
	 **/
	public void salvarArquivo() throws Exception {

		if (this.workbook == null) {
			LogUtil.Warn("Impossivel salvar arquivo pois o workbook está nulo.");
			return;
		}
		if (this.alteracao) {
			this.fileIn.close();
			this.fileOut = new FileOutputStream(this.arquivo);
		}
		this.workbook.write(this.fileOut);
		this.fileOut.close();
		this.instanciar();
	}

	/**
	 * @author romatos
	 * Limpa todos os dados do Objeto zerando as informções.
	 */
	public void clear() {
		this.fileIn = null;
		this.fileOut = null;
		this.workbook = null;
		this.alteracao = null;
	}

	/**
	 * @author romatos
	 * @return Retorna o status do arquivo, se está em modo de alteração(TRUE) ou inserção(FALSE).
	 */
	public Boolean getAlteracao() {
		return this.alteracao;
	}

	/**
	 * @author romatos
	 * @return Retorna TRUE para arquivo XLSX e FALSE para XLS.
	 */
	public Boolean isXlsx() {
		return this.arquivoXlsx;
	}
}
