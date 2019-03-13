package br.com.template.email;

import java.io.Serializable;
import java.util.List;

import br.com.template.bancoDeDados.dao.OracleDAO;
import br.com.template.model.EmailVO;
import br.com.template.utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public class EmailScience implements Serializable {

	private static final long serialVersionUID = -6329708635358805636L;

	public static List<String> consultarEmailsNotificacao() throws Exception {

		try {
			LogUtil.Info("CONSULTANDO ENDERECO DE EMAIL PARA NOTIFICAR ERRO ... ");
			List<String> listaEmail = null;
			listaEmail = OracleDAO.consultarEmailsNotificacao();
			LogUtil.Info("ENDERECO DE EMAIL CONSULTADO COM SUCESSO (" + listaEmail.size() + ").");
			return listaEmail;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO CONSULTAR EMAIL PARA NOTIFICACAO DE ERRO: " + e.getMessage());
			throw e;
		}
	}

	public static void enviarEmailSemAnexo(EmailVO emailVO) throws Exception {

		try {
			LogUtil.Info("ENVIANDO EMAIL SEM ANEXO ...");
			if (emailVO == null) {
				LogUtil.Warn("EMAIL VAZIO. ENVIO DE EMAIL INTERROMPIDO.");
				return;
			}
			OracleDAO.enviarEmail(emailVO);
			LogUtil.Info("ENVIO DE EMAIL REALIZADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO NO ENVIO DE EMAIL: " + e.getLocalizedMessage());
			throw e;
		}
	}

	public static void enviarEmailComAnexo(EmailVO emailVO) throws Exception {

		try {
			LogUtil.Info("ENVIANDO ARQUIVO VIA EMAIL ... ");
			EmailVO emailAtual = OracleDAO.enviarEmailComAnexo(emailVO);
			OracleDAO.anexarEmail(emailAtual);
			LogUtil.Info("EMAIL ENVIADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO ENVIAR EMAIL: " + e.getMessage());
			throw e;
		}
	}
}
