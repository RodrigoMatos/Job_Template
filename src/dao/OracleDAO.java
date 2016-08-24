package dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import model.EmailVO;
import model.FtpVO;
import utils.SQLUtil;
import conexaoBD.ConexaoPool;
import constantes.Configuracao;

public class OracleDAO extends DAO {

	private static final long serialVersionUID = 3622455715697490967L;

	protected static String chave = "SCIENCE";

	public OracleDAO() {
	}

	public static boolean isAtivo() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		boolean status = false;

		sql.append(" SELECT STR_BLOQUEADO FROM SERVICO_SCIENCE WHERE SEQ_SERVICO_SCIENCE = ").append(Configuracao.PARAM_SEQ_SERVICO_SCIENCE);

		try {
			conn = ConexaoPool.getConnection(chave);
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

	public static void atualizarDataExecucao() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE  ");
		sql.append("     CALLMAP.CONTROLE_SERVICO  ");
		sql.append(" SET  ");
		sql.append("     DAT_ULTIMO_ACESSO  = SYSDATE ");
		sql.append(" WHERE COD_SERVICO = ").append(Configuracao.PARAM_COD_SERVICO);

		try {
			conn = ConexaoPool.getConnection(chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	public static List<String> consultarEmailsNotificacao() throws Exception {

		List<String> listaEmails = new ArrayList<String>();
		StringBuilder sql = new StringBuilder();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		sql.append(" SELECT VALOR_APPLICATION_CONFIG  ");
		sql.append(" FROM APPLICATION_CONFIG  ");
		sql.append(" WHERE SIG_APPLICATION_CONFIG = '").append(Configuracao.PARAM_EMAIL_NOTIF).append("'");

		try {
			conn = ConexaoPool.getConnection(chave);
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

	public static void enviarEmail(EmailVO emailVO) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CALLMAP.E_MAIL (COD_EMAIL, DES_DE, DES_PARA, DES_ASSUNTO, DES_MESSAGEM) ");
		sql.append(" VALUES (CALLMAP.SEQ_EMAIL.NEXTVAL, ? ,?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, emailVO.getDe());
			stmt.setString(2, emailVO.getPara());
			stmt.setString(3, emailVO.getAssunto());
			stmt.setString(4, emailVO.getMensagem());
			stmt.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}


	public static EmailVO enviarEmailComAnexo(EmailVO emailVO) throws Exception {

		emailVO.setCodigo(getSequenceEmail());
		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO CALLMAP.E_MAIL (COD_EMAIL, DES_DE, DES_PARA, DES_ASSUNTO, DES_MESSAGEM) ");
		sql.append(" VALUES (?, ?, ?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.setInt(1, emailVO.getCodigo());
			stmt.setString(2, emailVO.getDe());
			stmt.setString(3, emailVO.getPara());
			stmt.setString(4, emailVO.getAssunto());
			stmt.setString(5, emailVO.getMensagem());
			stmt.execute();
			return emailVO;
		} catch (Exception e) {
			throw e;
		} finally {
			SQLUtil.closeStatement(stmt);
			SQLUtil.closeConnection(conn);
		}
	}

	protected static Integer getSequenceEmail() throws Exception {

		Integer cod = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT CALLMAP.SEQ_EMAIL.NEXTVAL FROM DUAL ");

		try {
			conn = ConexaoPool.getConnection(chave);
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

	public static void anexarEmail(EmailVO email) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;

		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO CALLMAP.E_MAIL_ANEXO (COD_ANEXO, COD_EMAIL, ARQUIVO_ANEXADO, NOM_ARQUIVO_ANEXADO) ");
		sql.append(" VALUES (CALLMAP.SEQ_E_MAIL_ANEXO.NEXTVAL, ?, ?, ?) ");

		try {
			conn = ConexaoPool.getConnection(chave);
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
	
	public static FtpVO consultarFTP() throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StringBuilder sql = new StringBuilder();
		FtpVO ftpRetorno = null;

		sql.append(" SELECT NOM_LOGIN_FTP, STR_SENHA_FTP, STR_HOST_FTP, STR_DIRETORIO ");
		sql.append(" FROM SERVICO_SCIENCE WHERE SEQ_SERVICO_SCIENCE = ").append(Configuracao.PARAM_SEQ_SERVICO_SCIENCE);

		try {
			conn = ConexaoPool.getConnection(chave);
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
