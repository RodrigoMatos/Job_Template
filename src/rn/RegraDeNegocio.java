package rn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.EmailVO;
import model.FtpVO;
import utils.LogUtil;
import dao.OracleDAO;
import ftp.FtpRN;

public class RegraDeNegocio {

	private OracleDAO oracleDAO;
	private FtpRN ftpRN;

	public RegraDeNegocio() {
		this.oracleDAO = new OracleDAO();
		this.ftpRN = new FtpRN();
	}
	
	public void deletarRegistros(String tabela) {

		try {
			LogUtil.Info("DELETANDO REGISTROS (" + tabela + ") ...");
			this.oracleDAO.deletarRegistroTabela(tabela);
			LogUtil.Info("REGISTROS DELETADOS COM SUCESSO (" + tabela + ").");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO DELETAR REGISTRO (" + tabela + "): " + e.getMessage());
		}
	}

	public boolean isAtivo() throws Exception {

		boolean status = false;
		try {
			LogUtil.Info("OBTENDO STATUS DO JOB ...");
			status = this.oracleDAO.isAtivo();
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
			this.oracleDAO.atualizarDataExecucao();
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
			this.oracleDAO.enviarEmail(listaEmail);
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
			listaEmail = (ArrayList<String>) this.oracleDAO.consultarEmailsNotificacao();
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
				emailAtual = this.oracleDAO.enviarEmailComAnexo(email, conteudoEmail);
				this.oracleDAO.anexarEmail(emailAtual);
			}
			LogUtil.Info("EMAIL ENVIADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO ENVIAR EMAIL: " + e.getMessage());
			throw e;
		}
	}
	
	protected FtpVO consultarFTP() throws Exception {

		try {
			LogUtil.Info("CONSULTANDO DADOS DO FTP ...");
			FtpVO ftpVO = this.oracleDAO.consultarFTP();
			LogUtil.Info("DADOS OBTIDO COM SUCESSO.");
			return ftpVO;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO CONSULTAR DADOS DO FTP: " + e.getMessage());
			throw e;
		}
	}

	protected void conectarFTP(FtpVO ftpVO) throws Exception {

		try {
			LogUtil.Info("CONECTANDO COM O SERVIDOR FTP (" + ftpVO.getServidor() + " | " + ftpVO.getUsuario() + ") ...");
			ftpRN.conectar(ftpVO.getServidor(), ftpVO.getUsuario(), ftpVO.getSenha());
			LogUtil.Info("CONEXAO ESTABELECIDA COM O SERVIDOR FTP.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO TENTAR ESTABELECER CONEXAO COM O SERVIDOR FTP: " + e.getMessage());
			throw e;
		}
	}

	protected void upload(String origem, String destino) throws Exception {

		try {
			LogUtil.Info("REALIZANDO UPLOAD DO ARQUIVO (" + origem + " => " + destino + ") ...");
			this.ftpRN.upload(origem, destino);
			LogUtil.Info("UPLOAD DO ARQUIVO RELIZADO COM SUCESSO.");
		} catch (Exception e) {
			this.ftpRN.desconectar();
			LogUtil.Error("ERRO AO REALIZAR UPLOAD DO ARQUIVO: " + e.getMessage());
			throw e;
		}
	}

	protected void desconectarFTP() {

		if (this.ftpRN.isConnected()) {
			try {
				LogUtil.Info("FECHANDO CONEXAO COM SERVIDOR DE FTP ... ");
				this.ftpRN.desconectar();
				LogUtil.Info("CONEXAO FECHADA COM SUCESSO.");
			} catch (Exception e) {
				LogUtil.Error("ERRO AO FECHAR CONEXAO DO FTP:" + e.getMessage());
			}
		}
	}

	protected void enviarArquivosFTP(List<String> arquivos) throws Exception {

		FtpVO ftpVO;
		ftpVO = this.consultarFTP();
		this.conectarFTP(ftpVO);
		for (String arquivo : arquivos) {
			try {
				this.upload(arquivo, ftpVO.getDiretorio());
			} catch (Exception e) {
				// IGNORAR
			}
		}
		this.desconectarFTP();
	}

	public void iniciarProcesso() throws Exception {

	}

}
