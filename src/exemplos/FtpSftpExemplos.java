package exemplos;

import utils.FileUtil;
import ftp.SftpAcesso;
import model.FtpVO;

public class FtpSftpExemplos {

	public void baixarArquivo(){
		FtpVO ftp = new FtpVO();
		ftp.setServidor("10.72.18.129");
		ftp.setUsuario("wls11");
		ftp.setSenha("wls11");
		
		SftpAcesso sftp = new SftpAcesso();
		
		try {
			sftp.conectar(ftp.getServidor(), ftp.getUsuario(), ftp.getSenha(), null);
			sftp.cd("/home2/weblogic11/");
			FileUtil.criarDirs("D\\TesteSftp\\");
			sftp.download("/home2/weblogic11/RanSharing/aplication_files/sience/ransharing/RS3-Base_Dados_Fisicos-V20160826.xlsx", "D\\TesteSftp\\");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftp.desconectar();
		}
	}
	
}
