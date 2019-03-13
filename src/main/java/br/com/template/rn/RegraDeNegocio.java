package br.com.template.rn;

import java.io.Serializable;

import br.com.template.bancoDeDados.dao.OracleDAO;
import br.com.template.utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class RegraDeNegocio implements Serializable {

	private static final long serialVersionUID = -2821161670762304175L;

	public RegraDeNegocio() { }

	public void deletarRegistros(String tabela, String chave) {

		try {
			LogUtil.Info("DELETANDO REGISTROS (" + tabela + ") ...");
			OracleDAO.deletarRegistroTabela(tabela, chave);
			LogUtil.Info("REGISTROS DELETADOS COM SUCESSO (" + tabela + ").");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO DELETAR REGISTRO (" + tabela + "): " + e.getMessage());
		}
	}

	public boolean isAtivo() throws Exception {

		boolean status = false;
		try {
			LogUtil.Info("OBTENDO STATUS DO JOB ...");
			status = OracleDAO.isAtivo();
			LogUtil.Info("STATUS DO JOB OBTIDO COM SUCESSO (" + status + ").");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO OBTER STATUS DO JOB: " + e.getMessage());
			throw e;
		}
		return status;
	}

	public void atualizarDataExec() throws Exception {

		try {
			LogUtil.Info("ATUALIZANDO DATA DE EXECUCAO ...");
			OracleDAO.atualizarDataExecucao();
			LogUtil.Info("DATA DE EXECUCAO ATUALIZADA COM SUCESSO. ");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO ATUALIZAR A DATA DE EXECUCAO: " + e.getMessage());
			throw e;
		}
	}
}
