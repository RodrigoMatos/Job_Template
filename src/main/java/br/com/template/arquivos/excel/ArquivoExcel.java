package br.com.template.arquivos.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.template.utils.arquivo.FileUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class ArquivoExcel extends ArquivoExcelGenerico {

    private static final long serialVersionUID = -6223797011250863660L;

    private static final Logger LOGGER = LoggerFactory.getLogger(ArquivoExcel.class);

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
     * @param aba - Aba (Sheet) que ser� auto dimensionada.
     *            Ajusta a largura das colunas da aba de forma automatica.
     * @author isouzaa
     */
    public void AutoDimensionarColunas(Sheet aba) {
        if (aba != null) {
            for (int i = 0; i < this.getNumeroDeColunas(aba); i++) {
                aba.autoSizeColumn(i);
            }
        }
    }

    /**
     * @param indexAba - Indice da aba que ser� auto dimensionada.
     *                 Ajusta a largura das colunas da aba de forma automatica.
     * @author isouzaa
     */
    public void AutoDimensionarColunas(Integer indexAba) {
        this.AutoDimensionarColunas(this.workbook.getSheetAt(indexAba));
    }

    /**
     * @param nomeAba - Nome da aba que ser� auto dimensionada.
     *                Ajusta a largura das colunas da aba de forma automatica.
     * @author isouzaa
     */
    public void AutoDimensionarColunas(String nomeAba) {
        this.AutoDimensionarColunas(this.workbook.getSheet(nomeAba));
    }

    /**
     * @param aba - Aba (Sheet) que ser� utilizada para contar as colunas.
     * @return Retorna a quantidade de colunas de uma aba.
     * @author isouzaa
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
     * @param indiceAba - Indice da aba que ser� utilizada para contar as colunas.
     * @return Retorna a quantidade de colunas de uma aba.
     * @author isouzaa
     */
    public Integer getNumeroDeColunas(Integer indiceAba) {
        return this.getNumeroDeColunas(this.workbook.getSheetAt(indiceAba));
    }

    /**
     * @param nomeAba - Nome da aba que ser� utilizada para contar as colunas.
     * @return Retorna a quantidade de colunas de uma aba.
     * @author isouzaa
     */
    public Integer getNumeroDeColunas(String nomeAba) {
        return this.getNumeroDeColunas(this.workbook.getSheet(nomeAba));
    }

    public Integer getNumeroDeLinhas(String nomeAba) {
        return this.obterAba(nomeAba).getLastRowNum();
    }

    public Integer getNumeroDeLinhas(Integer indiceAba) {
        return this.obterAba(indiceAba).getLastRowNum();
    }

    public Integer getNumeroDeLinhas(Sheet aba) {
        return aba.getPhysicalNumberOfRows();
    }

    public Iterator<Row> getIteratorRows(Sheet aba) {
        return aba.rowIterator();
    }

    /**
     * @return Retorna o workbook do arquivo.
     * @author romatos
     */
    public Workbook getWorkbook() {
        return this.workbook;
    }


    public Integer obterQuantidadeAbas() {
        return this.workbook.getNumberOfSheets();
    }

    public Integer obterIndexAbaAtiva() {
        return this.workbook.getActiveSheetIndex();
    }

    /**
     * @param nomeAba - Nome da aba que ser� retornada
     * @return Retorna um objeto Sheet correspondente a aba do par�metro.
     * @author romatos
     */
    public Sheet obterAba(String nomeAba) {
        return this.workbook.getSheet(nomeAba);
    }

    /**
     * @param index - Indice da aba que ser� retornada
     * @return Retorna um objeto Sheet correspondente a aba do par�metro.
     * @author romatos
     */
    public Sheet obterAba(Integer index) {
        return this.workbook.getSheetAt(index);
    }

    /**
     * @param nomeAba - Nome da aba que ser� criada.
     * @return Retorna a aba crida.
     * @author romatos
     */
    public Sheet addAba(String nomeAba) {
        return this.workbook.createSheet(nomeAba);
    }

    /**
     * @param nomeAba    - Nome da aba que ser� criada a linha.
     * @param indexLinha - Indice da linha que ser� criada.
     * @return Retorna a linha criada
     * @author romatos
     */
    public Row addLinha(String nomeAba, Integer indexLinha) {
        return this.obterAba(nomeAba).createRow(indexLinha);
    }

    /**
     * @param indexAba   - Indice da aba que ser� criada a linha.
     * @param indexLinha - Indice da linha que ser� criada.
     * @return Retorna a linha criada
     * @author romatos
     */
    public Row addLinha(Integer indexAba, Integer indexLinha) {
        return this.obterAba(indexAba).createRow(indexLinha);
    }

    /**
     * @param aba        - Aba que ser� criada a linha.
     * @param indexLinha - Indice da linha que ser� criada.
     * @return Retorna a linha criada
     * @author romatos
     */
    public Row addLinha(Sheet aba, Integer indexLinha) {
        return aba.createRow(indexLinha);
    }

    /**
     * @param aba        - Aba que ir� buscar a linha.
     * @param indexLinha - Indice da linha que ser� obtida.
     * @return Retorna o objeto Row correspondente a linha encontra.
     * @author romatos
     */
    public Row obterLinha(Sheet aba, Integer indexLinha) {

        if (aba.getRow(indexLinha) == null) {
            this.addLinha(aba, indexLinha);
        }
        return aba.getRow(indexLinha);
    }

    /**
     * @param nomeAba    - Nome da aba que ir� buscar a linha.
     * @param indexLinha - Indice da linha que ser� obtida.
     * @return Retorna o objeto Row correspondente a linha encontra.
     * @author romatos
     */
    public Row obterLinha(String nomeAba, Integer indexLinha) {
        return this.obterLinha(this.obterAba(nomeAba), indexLinha);
    }

    /**
     * @param indexAba   - Indice da aba que ir� buscar a linha.
     * @param indexLinha - Indice da linha que ser� obtida.
     * @return Retorna o objeto Row correspondente a linha encontra.
     * @author romatos
     */
    public Row obterLinha(Integer indexAba, Integer indexLinha) {
        return this.obterLinha(this.obterAba(indexAba), indexLinha);
    }

    /**
     * @param indexAba    - Indice da aba que ser� criada a coluna.
     * @param indexLinha  - Indice da linha que ser� criada a coluna.
     * @param indexCelula - Indice da coluna que ser� criada.
     * @return Retorna o objeto Cell correspondente a celula criada.
     * @author romatos
     */
    public Cell addCelula(Integer indexAba, Integer indexLinha, Integer indexCelula) {
        return this.obterLinha(this.obterAba(indexAba), indexLinha).createCell(indexCelula);
    }

    /**
     * @param nomeAba     - Nome da aba que ser� criada a coluna.
     * @param indexLinha  - Indice da linha que ser� criada a coluna.
     * @param indexCelula - Indice da coluna que ser� criada.
     * @return Retorna o objeto Cell correspondente a celula criada.
     * @author romatos
     */
    public Cell addCelula(String nomeAba, Integer indexLinha, Integer indexCelula) {
        return this.obterLinha(this.obterAba(nomeAba), indexLinha).createCell(indexCelula);
    }

    /**
     * @param linha - Objeto Row correspondente a linha que ser� criada a coluna.
     * @param index - Indice da coluna que ser� criada.
     * @return Retorna o objeto Cell correspondente a celula criada.
     * @author romatos
     */
    public Cell addCelula(Row linha, Integer index) {
        return linha.createCell(index);
    }

    /**
     * @param linha - Objeto Row correspondente a linha.
     * @param index - Indice da celula.
     * @return Retorna um objeto Cell.
     * @author romatos
     */
    public Cell obterCelula(Row linha, Integer index) {
        if (this.isEmptyCell(linha.getCell(index))) {
            this.addCelula(linha, index);
        }
        return linha.getCell(index);
    }

    /**
     * @param aba         - Aba que ser� utilizada.
     * @param indexLinha  - Indice da linha.
     * @param indexColuna - Indice da coluna que ir� escrever o valor.
     * @param conteudo    - Conteudo String que ser� escrito na celula.
     * @return Retorna a celula que foi escrita.
     * @author romatos
     */
    public Cell escreverNaCelula(Sheet aba, Integer indexLinha, Integer indexColuna, String conteudo) {
        Row linha = this.obterLinha(aba, indexLinha);
        Cell celula = this.obterCelula(linha, indexColuna);
        return this.escreverNaCelula(celula, conteudo);
    }

    /**
     * @param indexAba    - Indice da aba.
     * @param indexLinha  - Indice da linha.
     * @param indexColuna - Indice da coluna que ir� escrever o valor.
     * @param conteudo    - Conteudo String que ser� escrito na celula.
     * @return Retorna a celula que foi escrita.
     * @author romatos
     */
    public Cell escreverNaCelula(Integer indexAba, Integer indexLinha, Integer indexColuna, String conteudo) {
        return this.escreverNaCelula(this.obterAba(indexAba), indexLinha, indexColuna, conteudo);
    }

    /**
     * @param nomeAba     - Nome da aba.
     * @param indexLinha  - Indice da linha.
     * @param indexColuna - Indice da coluna que ir� escrever o valor.
     * @param conteudo    - Conteudo String que ser� escrito na celula.
     * @return Retorna a celula que foi escrita.
     * @author romatos
     */
    public Cell escreverNaCelula(String nomeAba, Integer indexLinha, Integer indexColuna, String conteudo) {
        return this.escreverNaCelula(this.obterAba(nomeAba), indexLinha, indexColuna, conteudo);
    }

    /**
     * @param aba             - aba que ser� utilizada para realizar a fun��o de mesclar linha/celula.
     * @param indexLinhaDe    - Indice da linha que ir� iniciar a mesclagem.
     * @param indexLinhaPara  - Indice da linha que ir� terminar a mesclagem.
     * @param indexColunaDe   - Indice da coluna que ir� iniciar a mesclagem.
     * @param indexColunaPara - Indice da coluna que ir� terminar a mesclagem.
     * @author romatos
     */
    public void mesclar(Sheet aba, Integer indexLinhaDe, Integer indexLinhaPara, Short indexColunaDe, Short indexColunaPara) {
        aba.addMergedRegion(new CellRangeAddress(indexLinhaPara, indexLinhaPara, indexColunaDe, indexColunaPara));
    }

    /**
     * @param indexAba        - Index da aba que ser� utilizada para realizar a fun��o de mesclar linha/celula.
     * @param indexLinhaDe    - Indice da linha que ir� iniciar a mesclagem.
     * @param indexLinhaPara  - Indice da linha que ir� terminar a mesclagem.
     * @param indexColunaDe   - Indice da coluna que ir� iniciar a mesclagem.
     * @param indexColunaPara - Indice da coluna que ir� terminar a mesclagem.
     * @author romatos
     */
    public void mesclar(Integer indexAba, Integer indexLinhaDe, Integer indexLinhaPara, Short indexColunaDe, Short indexColunaPara) {
        this.obterAba(indexAba).addMergedRegion(new CellRangeAddress(indexLinhaPara, indexLinhaPara, indexColunaDe, indexColunaPara));
    }

    /**
     * @throws Exception Salvar as altera��es realizadas no arquivo.
     * @author romatos
     **/
    public void salvarArquivo() throws Exception {

        if (this.workbook == null) {
            LOGGER.warn("Impossivel salvar arquivo pois o workbook est� nulo.");
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
     * Limpa todos os dados do Objeto zerando as inform��es.
     */
    public void clear() {
        this.fileIn = null;
        this.fileOut = null;
        this.workbook = null;
        this.alteracao = null;
    }

    /**
     * @return Retorna o status do arquivo, se est� em modo de altera��o(TRUE) ou inser��o(FALSE).
     * @author romatos
     */
    public Boolean getAlteracao() {
        return this.alteracao;
    }

    /**
     * @return Retorna TRUE para arquivo XLSX e FALSE para XLS.
     * @author romatos
     */
    public Boolean isXlsx() {
        return this.arquivoXlsx;
    }
}