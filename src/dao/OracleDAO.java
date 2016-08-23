package dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

import model.EmailVO;
import model.FtpVO;
import utils.LogUtil;
import utils.SQLUtil;
import conexaoBD.ConexaoPool;
import constantes.Configuracao;

public class OracleDAO {
	
	private String chave;
	
	public OracleDAO() {
		this.chave = "SCIENCE";
	}
	
	public void deletarRegistroTabela(String tabela) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(tabela);

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	public boolean isAtivo() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		boolean status = false;

		sql.append(" SELECT STR_BLOQUEADO FROM SERVICO_SCIENCE WHERE SEQ_SERVICO_SCIENCE = ").append(Configuracao.PARAM_SEQ_SERVICO_SCIENCE);

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				String retorno = rs.getString("STR_BLOQUEADO");
				if (retorno != null && retorno.toUpperCase().equals("N"))
					status = true;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeResult(rs);
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
		return status;
	}

	public void atualizarDataExecucao() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("     CALLMAP.CONTROLE_SERVICO  ");
		sql.append(" SET  ");
		sql.append("     DAT_ULTIMO_ACESSO  = SYSDATE ");
		sql.append(" WHERE COD_SERVICO = ").append(Configuracao.PARAM_COD_SERVICO);

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	public ArrayList<String> consultarEmailsNotificacao() throws Exception {

		ArrayList<String> listaEmails = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		sql.append(" SELECT VALOR_APPLICATION_CONFIG  ");
		sql.append(" FROM APPLICATION_CONFIG  ");
		sql.append(" WHERE SIG_APPLICATION_CONFIG = '").append(Configuracao.PARAM_EMAIL_NOTIF).append("'");

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			String emails = "";

			while (rs.next()) {
				emails = rs.getString("VALOR_APPLICATION_CONFIG");
			}
			StringTokenizer st = new StringTokenizer(emails, ";");
			while (st.hasMoreElements()) {
				System.out.println();
				listaEmails.add(st.nextElement().toString().trim());
			}
			return listaEmails;
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeResult(rs);
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	public void enviarEmail(ArrayList<String> listaEmail) throws Exception {

		if (listaEmail == null || listaEmail.isEmpty()) {
			return;
		}
		Connection conn = null;
		PreparedStatement stmt = null;
		LogUtil.Info(" FALHA NO JOB, ENVIANDO EMAIL PARA: " + listaEmail);

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CALLMAP.E_MAIL (COD_EMAIL, DES_DE, DES_PARA, DES_ASSUNTO, DES_MESSAGEM) ");
		sql.append(" VALUES (CALLMAP.SEQ_EMAIL.NEXTVAL, ? ,?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			for (String email : listaEmail) {
				stmt.setString(1, "SMAP_SCIENCE_REQUEST@vivo.com.br");
				stmt.setString(2, email);
				stmt.setString(3, "Falha na Execução do JOB INVENTARIO UNIFICADO");
				stmt.setString(4, "Log de execução:\n" + LogUtil.logErro.toString().replaceAll("\n+", "\n"));
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}


	public EmailVO enviarEmailComAnexo(String email, EmailVO emailConteudo) throws Exception {

		if (email == null || "".equals(email)) {
			throw new Exception("CONTEUDO DO EMAIL VAZIO.");
		}
		emailConteudo.setCodigo(this.getSequenceEmail());
		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CALLMAP.E_MAIL (COD_EMAIL, DES_DE, DES_PARA, DES_ASSUNTO, DES_MESSAGEM) ");
		sql.append(" VALUES (?, ?, ?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, emailConteudo.getCodigo());
			stmt.setString(2, "SMAP_SCIENCE_REQUEST@vivo.com.br");
			stmt.setString(3, email);
			stmt.setString(4, emailConteudo.getAssunto());
			stmt.setString(5, emailConteudo.getMensagem());
			stmt.execute();
			return emailConteudo;
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	private Integer getSequenceEmail() throws Exception {

		Integer cod = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CALLMAP.SEQ_EMAIL.NEXTVAL FROM DUAL ");

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				cod = rs.getInt(1);
			}
			return cod;
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeResult(rs);
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	public void anexarEmail(EmailVO email) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO CALLMAP.E_MAIL_ANEXO (COD_ANEXO, COD_EMAIL, ARQUIVO_ANEXADO, NOM_ARQUIVO_ANEXADO) ");
		sql.append(" VALUES (CALLMAP.SEQ_E_MAIL_ANEXO.NEXTVAL, ?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			FileInputStream fis = new FileInputStream(email.getArquivoAnexado());
			int lenght = (int) email.getArquivoAnexado().length();
			stmt.setInt(1, email.getCodigo());
			stmt.setBinaryStream(2, fis, lenght);
			stmt.setString(3, email.getArquivoAnexado().getName());
			stmt.execute();
			fis.close();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}
	
	public FtpVO consultarFTP() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		FtpVO ftpRetorno = null;

		sql.append(" SELECT NOM_LOGIN_FTP, STR_SENHA_FTP, STR_HOST_FTP, STR_DIRETORIO ");
		sql.append(" FROM SERVICO_SCIENCE WHERE SEQ_SERVICO_SCIENCE = ").append(Configuracao.PARAM_SEQ_SERVICO_SCIENCE);

		try {
			conn = ConexaoPool.getConnection(this.chave);
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			if (rs.next()) {
				ftpRetorno = new FtpVO();
				ftpRetorno.setUsuario(rs.getString("NOM_LOGIN_FTP"));
				ftpRetorno.setSenha(rs.getString("STR_SENHA_FTP"));
				ftpRetorno.setServidor(rs.getString("STR_HOST_FTP"));
				ftpRetorno.setDiretorio(rs.getString("STR_DIRETORIO"));
			}
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeResult(rs);
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
		return ftpRetorno;
	}

}
