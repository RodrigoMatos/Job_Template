package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import arquivos.excel.ArquivoExcel;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ImportacaoUtils {

	/**
	 * @author romatos
	 * @param String - Caminho do arquivo que será importado.
	 * @param indexAba - Indice da aba que será importada
	 * @return Retorna uma lista de map, cada linha da lista representa uma linha do excel. Cada item do map representa a coluna (pelo indice).
	 * @throws Exception
	 */
	public static List<LinkedHashMap<Integer, Object>> importarExcel(String dirArquivo, Integer indexAba) throws Exception {
		return importarExcel(new File(dirArquivo), indexAba);
	}

	/**
	 * @author romatos
	 * @param arquivo - Arquivo que será importado.
	 * @param indexAba - Indice da aba que será importada
	 * @return Retorna uma lista de map, cada linha da lista representa uma linha do excel. Cada item do map representa a coluna (pelo indice).
	 * @throws Exception
	 */
	public static List<LinkedHashMap<Integer, Object>> importarExcel(File arquivo, Integer indexAba) throws Exception {
		return importar(arquivo, indexAba);
	}

	/**
	 * @author romatos
	 * @param arquivo - Arquivo que será importado.
	 * @param indexAba - Indice da aba que será importada
	 * @return Retorna uma lista de map, cada linha da lista representa uma linha do excel. Cada item do map representa a coluna (pelo indice).
	 * @throws Exception
	 */
	public static List<LinkedHashMap<Integer, Object>> importarExcel(File arquivo, String nomeAba) throws Exception {
		return importar(arquivo, nomeAba);
	}

	private static List<LinkedHashMap<Integer, Object>> importar(File arquivo, Object dadoAba) throws Exception {

		List<LinkedHashMap<Integer, Object>> list = new ArrayList<LinkedHashMap<Integer, Object>>();
		if (!arquivo.exists() || !arquivo.isFile()) {
			LogUtil.Warn("Arquivo não encontrado (" + arquivo.getAbsolutePath() + ").");
			return list;
		}

		ArquivoExcel arquivoExcel = new ArquivoExcel(arquivo);
		XSSFSheet aba = null;
		if (dadoAba instanceof String) {
			aba = (XSSFSheet) arquivoExcel.obterAba(dadoAba.toString());
		} else {
			aba = (XSSFSheet) arquivoExcel.obterAba(Integer.parseInt(dadoAba.toString()));
		}

		int indexLinha = aba.getFirstRowNum();
		int ultimaLinha = aba.getLastRowNum();
		if (ultimaLinha < 1) {
			LogUtil.Warn("Nenhuma linha encontrada no arquivo (" + arquivo.getAbsolutePath() + ").");
			return list;
		}

		int indexCelula = 0;

		LinkedHashMap<Integer, Object> linha;
		for (indexLinha = 0; indexLinha <= ultimaLinha; indexLinha++) {

			linha = new LinkedHashMap<Integer, Object>();
			for (indexCelula = 0; indexCelula < aba.getRow(indexLinha).getLastCellNum(); indexCelula++) {
				linha.put(indexCelula, aba.getRow(indexLinha).getCell(indexCelula));
			}
			list.add(linha);
		}
		return list;
	}

}
