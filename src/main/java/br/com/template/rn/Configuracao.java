package br.com.template.rn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import br.com.template.bancoDeDados.dao.OracleDAO;
import br.com.template.model.ConfigVO;
import br.com.template.utils.LogUtil;
import br.com.template.utils.arquivo.XmlUtils;

public abstract class Configuracao {

	public static ConfigVO parametros = new ConfigVO();
	public static File arquivoXML;

	/**
	 * @author romatos
	 * Carrega os par�metros de configura��o.
	 * @throws Exception
	 */
	public static void carregarConfiguracao() throws Exception {

		try {
			arquivoXML = consultarXMLConfiguracao();

			if (arquivoXML == null) {
				throw new Exception("ARQUIVO XML DE CONFIGURACOES N�O FOI ENCONTRADO.");
			}
			LogUtil.Info("OBTENDO PARAMETROS DE CONFIGURACOES DO ARQUIVO DE CONFIG ...");
			List<Map<String, String>> elementos = XmlUtils.importarElementoXML(arquivoXML, "CONFIGURACOES");
			LogUtil.Info("OBTENDO DADOS DO ELEMENTO ...");
			if (elementos == null || elementos.isEmpty()) {
				throw new Exception("NUNHUM ELEMENTO 'CONFIGURACOES' FOI ENCONTRADO NO ARQUIVO XML.");
			}
			Map<String, String> elemento = elementos.get(0);
			/** CARREGAR DADOS DE CONFIGURA��O **/
			String remover = elemento.get("REMOVER-ARQUIVO-CONFIG");
			if (remover != null) {
				parametros.setRemoverArquivoConfig(new Boolean(remover));
			}
			LogUtil.Info("DADOS DE CONFIGURACOES OBTIDAS COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO DURANTE CONSULTA DOS PARAMETROS DE CONFIGURACOES: " + e.getMessage());
			throw e;
		}
	}

	protected static File consultarXMLConfiguracao() throws Exception {

		LogUtil.Info("CONSULTANDO ARQUIVO DE CONFIGURACAO .XML ...");
		File arquivo = OracleDAO.consultarXmlConfiguracao();
		if (arquivo != null) {
			LogUtil.Info("ARQUIVO OBTIDO COM SUCESSO.");
		} else {
			LogUtil.Info("ARQUIVO DE CONFIGURACAO N�O ENCONTRADO, OBTENDO ARQUIVO PADRAO.");
			arquivo = carregarArquivoConfigPadrao();
		}
		return arquivo;
	}

	protected static File carregarArquivoConfigPadrao() throws IOException {

		InputStream inputStream = null;
		OutputStream outputStream = null;
		File arquivo = null;
		try {
			arquivo = File.createTempFile("configPadraoProjetoAntena.xml", "", new File("."));
			inputStream = arquivo.getClass().getResourceAsStream("/configPadrao.xml");
			outputStream = new FileOutputStream(arquivo);

			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw e;
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
		return arquivo;
	}

	public static void removerArquivoConfig() {
		LogUtil.Info("REMOVENDO ARQUIVO XML DE CONFIGURA��O ...");
		if (arquivoXML.delete()) {
			LogUtil.Info("ARQUIVO XML DE CONFIGURA��O FOI REMOVIDO COM SUCESSO.");
		} else {
			LogUtil.Warn("ARQUIVO XML DE CONFIGURA��O N�O PODE SER REMOVIDO.");
		}
	}

}
