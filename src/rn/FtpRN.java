package rn;

import java.io.Serializable;
import java.util.List;

import model.FtpVO;
import utils.LogUtil;
import bancoDeDados.dao.OracleDAO;
import ftp.FtpAcesso;

public class FtpRN implements Serializable {

	private static final long serialVersionUID = 4161049651311326198L;

	private FtpAcesso ftpRN;
	
	public FtpRN(FtpAcesso ftpRN) {
		this.ftpRN = ftpRN;
	}

	public FtpVO consultarFTP() throws Exception {

		try {
			LogUtil.Info("CONSULTANDO DADOS DO FTP ...");
			FtpVO ftpVO = OracleDAO.consultarFTP();
			LogUtil.Info("DADOS OBTIDO COM SUCESSO.");
			return ftpVO;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO CONSULTAR DADOS DO FTP: " + e.getMessage());
			throw e;
		}
	}
	
	public void conectarFTP(FtpVO ftpVO) throws Exception {

		try {
			LogUtil.Info("CONECTANDO COM O SERVIDOR FTP (" + ftpVO.getServidor() + " | " + ftpVO.getUsuario() + ") ...");
			ftpRN.conectar(ftpVO.getServidor(), ftpVO.getUsuario(), ftpVO.getSenha());
			LogUtil.Info("CONEXAO ESTABELECIDA COM O SERVIDOR FTP.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO TENTAR ESTABELECER CONEXAO COM O SERVIDOR FTP: " + e.getMessage());
			throw e;
		}
	}
	
	public void upload(String origem, String destino) throws Exception {

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

	public void desconectarFTP() {

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

	public void enviarArquivosFTP(List<String> arquivos) throws Exception {

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

}
