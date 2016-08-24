package rn;

import java.io.File;
import java.util.ArrayList;

import model.EmailVO;
import utils.LogUtil;
import dao.OracleDAO;

public class RegraDeNegocio {

	public RegraDeNegocio() {
	}
	
	public void deletarRegistros(String tabela) {

		try {
			LogUtil.Info("DELETANDO REGISTROS (" + tabela + ") ...");
			OracleDAO.deletarRegistroTabela(tabela);
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

	public void enviarEmail() throws Exception {

		if (LogUtil.logErro.length() > 3500) {
			LogUtil.logErro = new StringBuilder(LogUtil.logErro.substring(LogUtil.logErro.length()-3500));
		}
		try {
			LogUtil.Info("ENVIANDO EMAIL DE ERRO ...");
			ArrayList<String> listaEmail = this.consultarEmailsNotificacao();
			if (listaEmail == null || listaEmail.isEmpty()) {
				LogUtil.Warn("NENHUM ENDERECO DE EMAIL PARA NOTIFICACAO DE ERRO FOI ENCONTRADO. ENVIO DE EMAIL INTERROMPIDO.");
				return;
			}
			OracleDAO.enviarEmail(listaEmail);
			LogUtil.Info("EMAIL DE ERRO ENVIADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO NO ENVIO DE EMAIL: " + e.getLocalizedMessage());
			throw e;
		}
	}


	private ArrayList<String> consultarEmailsNotificacao() throws Exception {

		try {
			LogUtil.Info("CONSULTANDO ENDERECO DE EMAIL PARA NOTIFICAR ERRO ... ");
			ArrayList<String> listaEmail = null;
			listaEmail = (ArrayList<String>) OracleDAO.consultarEmailsNotificacao();
			LogUtil.Info("ENDERECO DE EMAIL CONSULTADO COM SUCESSO (" + listaEmail.size() + ").");
			return listaEmail;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO CONSULTAR EMAIL PARA NOTIFICACAO DE ERRO: " + e.getMessage());
			throw e;
		}
	}

	private void enviarArquivoEmail(File arquivo) throws Exception {

		ArrayList<String> listaEmail = new ArrayList<String>();
		listaEmail.add("cadastroredemovel.br@telefonica.com");
		try {
			LogUtil.Info("ENVIANDO ARQUIVO VIA EMAIL ... ");
			EmailVO conteudoEmail = new EmailVO();
			conteudoEmail.setAssunto("Job - INVENTARIO UNIFICADO");
			conteudoEmail.setMensagem("");
			conteudoEmail.setArquivoAnexado(arquivo);
			EmailVO emailAtual;
			for (String email : listaEmail) {
				emailAtual = OracleDAO.enviarEmailComAnexo(email, conteudoEmail);
				OracleDAO.anexarEmail(emailAtual);
			}
			LogUtil.Info("EMAIL ENVIADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO ENVIAR EMAIL: " + e.getMessage());
			throw e;
		}
	}

}
