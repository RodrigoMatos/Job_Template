package exemplos;

import constantes.EnumTipoAcessoServidor;
import model.FtpVO;
import utils.arquivo.FileUtil;
import ftpSftp.FtpSftp;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class FtpSftpExemplos {

	public static void baixarArquivo() {

		FtpVO ftp = new FtpVO();
		ftp.setServidor("10.72.18.129");
		ftp.setUsuario("wls11");
		ftp.setSenha("wls11");

		FtpSftp sftp = new FtpSftp(ftp, EnumTipoAcessoServidor.SFTP);

		try {
			sftp.conectar();
			sftp.cd("/home2/weblogic11/");
			FileUtil.criarDirs("D:\\TesteSftp\\");
			sftp.download("/home2/weblogic11/RanSharing/aplication_files/sience/ransharing/RS3-Base_Dados_Fisicos-V20160826.xlsx", "D:\\TesteSftp\\");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftp.desconectar();
		}
	}

}
