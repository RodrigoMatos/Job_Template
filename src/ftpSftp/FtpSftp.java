package ftpSftp;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;
import model.FtpVO;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import utils.LogUtil;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import constantes.EnumTipoAcessoServidor;

/**
 * @author romatos
 * @version 1.0
 */

public class FtpSftp implements Serializable {

	private static final long serialVersionUID = 7632402305321553528L;

	private FtpVO ftpConfig;
	private EnumTipoAcessoServidor type;

	/************ FTP ************/
	private FTPClient ftp;

	/************ SFTP ***********/
	private JSch jsch;
	private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;

	/**
	 @author romatos
	 @param ftpConfig - Dados de configuração para o acesso ao FTP ou SFTP.
	 @param type - TipoAcessoFTP para definir se é FTP ou SFTP.
	 **/
	public FtpSftp(FtpVO ftpConfig, EnumTipoAcessoServidor type) {
		this.init(ftpConfig, type);
	}

	/**
	 @author romatos
	 @param servidor - Ip do servidor.
	 @param usuario - Usuário para autenticação com o servidor.
	 @param senha - Senha para autenticação com o servidor.
	 @param ftpConfig - Dados de configuração para o acesso ao FTP ou SFTP.
	 @param type - TipoAcessoFTP para definir se é FTP ou SFTP.
	 **/
	public FtpSftp(String servidor, String usuario, String senha, EnumTipoAcessoServidor type) {
		this.init(new FtpVO(servidor, usuario, senha), type);
	}

	/**
	 @author romatos
	 @param servidor - Ip do servidor.
	 @param usuario - Usuário para autenticação com o servidor.
	 @param senha - Senha para autenticação com o servidor.
	 @param ftpConfig - Dados de configuração para o acesso ao FTP ou SFTP.
	 @param diretorio - Diretorio que irá acessar ao conectar com o servidor.
	 @param type - TipoAcessoFTP para definir se é FTP ou SFTP.
	 **/
	public FtpSftp(String servidor, String usuario, String senha, String diretorio, EnumTipoAcessoServidor type) {
		this.init(new FtpVO(servidor, usuario, senha, diretorio), type);
	}

	/**
	 @author romatos
	 @param servidor - Ip do servidor.
	 @param usuario - Usuário para autenticação com o servidor.
	 @param senha - Senha para autenticação com o servidor.
	 @param ftpConfig - Dados de configuração para o acesso ao FTP ou SFTP.
	 @param diretorio - Diretorio que irá acessar ao conectar com o servidor.
	 @param porta - Porta que será utilizada para estabelecer conexão.
	 @param type - TipoAcessoFTP para definir se é FTP ou SFTP.
	 **/
	public FtpSftp(String servidor, String usuario, String senha, String diretorio, Integer porta, EnumTipoAcessoServidor type) {
		this.init(new FtpVO(servidor, usuario, senha, diretorio, porta), type);
	}

	private void init(FtpVO ftpConfig, EnumTipoAcessoServidor type) {

		this.ftpConfig = ftpConfig;
		this.type = type;
		if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
			this.ftp = new FTPClient();
		} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
			this.jsch = new JSch();
		}
	}

	/**
	 @author romatos
	 Conecta com o servidor de FTP ou SFTP de acordo com os dados do ftpConfig.
	 **/
	public void conectar() throws Exception {

		try {
			LogUtil.Info("CONECTANDO COM O SERVIDOR " + this.type.getDescricao() + " (" + this.ftpConfig.getServidor() + " | " + this.ftpConfig.getUsuario() + ") ...");
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				this.conectarFTP();
			} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
				this.conectarSFTP();
			} else {
				throw new Exception("TIPO DE ACESSO INVALIDO. OS TIPOS PERMITIDOS SÃO FTP E SFTP.");
			}
			LogUtil.Info("CONEXAO ESTABELECIDA COM O SERVIDOR " + this.type.getDescricao() + ".");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO TENTAR ESTABELECER CONEXAO COM O SERVIDOR " + this.type.getDescricao() + ": " + e.getMessage());
			throw e;
		}
	}

	/**
	 @author romatos
	 Desativa a conexão com o servidor de FTP/SFTP.
	 **/
	public void desconectar() {

		if (this.isConnected()) {
			try {
				LogUtil.Info("FECHANDO CONEXAO COM SERVIDOR DE " + this.type.getDescricao() + " ... ");
				if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
					this.desconectarFTP();
				} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
					this.desconectarSFTP();
				}
				LogUtil.Info("CONEXAO FECHADA COM SUCESSO.");
			} catch (Exception e) {
				LogUtil.Error("ERRO AO FECHAR CONEXAO DO " + this.type.getDescricao() + ":" + e.getMessage());
			}
		}
	}

	/**
	 @author romatos
	 @param origem - Caminho do arquivo de origem que será enviado para o servidor FTP/SFTP.
	 @param destino - Caminho do diretorio destino que será enviado o arquivo.
	 Realiza o upload de um arquivo para o servidor de FTP/SFTP. 
	 **/
	public void upload(String origem, String destino) throws Exception {

		try {
			if ((origem == null || "".equals(origem)) || (destino == null || "".equals(destino))) {
				LogUtil.Warn("A ORIGEM DO ARQUIVO OU O DESTINO ESTA VAZIO. UPLOAD DO ARQUIVO CANCELADO.");
				return;
			}
			this.validarConexao();
			LogUtil.Info("REALIZANDO UPLOAD " + this.type.getDescricao() + " DO ARQUIVO (" + origem + " => " + destino + ") ...");
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				this.uploadFTP(origem, destino);
			} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
				this.uploadSFTP(origem, destino);
			}
			LogUtil.Info("UPLOAD DO ARQUIVO RELIZADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO REALIZAR UPLOAD DO ARQUIVO: " + e.getMessage());
			throw e;
		}
	}

	/**
	 @author romatos
	 @param arquivos - Caminhos dos arquivos de origem que será enviado para o servidor FTP/SFTP.
	 @param destino - Caminho do diretorio destino que será enviado o arquivo (sem o nome do arquivo).
	 Realiza o upload de varios arquivos para o servidor de FTP/SFTP. Erros serão ignorados.
	 **/
	public void uploadArquivos(List<String> arquivos, String destino) {

		if ((arquivos != null && !arquivos.isEmpty()) && (destino != null && !"".equals(destino))) {
			for (String arquivo : arquivos) {
				try {
					this.upload(arquivo, destino);
				} catch (Exception e) {
					// IGNORAR
				}
			}
		} else {
			LogUtil.Warn("A LISTA DE ARQUIVO OU O DESTINO ESTA VAZIO. UPLOAD DE ARQUIVOS CANCELADO.");
		}
	}

	/**
	 @author romatos
	 @return FTPFile[] - Listagem de arquivos e diretórios existentes no diretório atual.
	 Obs: Este metodo só funciona para FTP
	 **/
	public FTPFile[] ls() throws Exception {

		try {
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				this.validarConexao();
				LogUtil.Info("CONSULTANDO LISTAGEM DE ARQUIVOS/PASTAS ...");
				FTPFile[] lista = this.lsFTP();
				LogUtil.Info("LISTAGEM DE ARQUIVOS/PASTAS REALIZADO COM SUCESSO.");
				return lista;
			}
		} catch (Exception e) {
			LogUtil.Error("ERRO AO LISTAR ARQUIVOS/PASTAS: " + e.getMessage());
			throw e;
		}
		return null;
	}

	/**
	 @author romatos
	 @return Object - Listagem de arquivos e diretórios existentes no diretório atual.
	 O retorno será um objeto FTPFile[] para conexão com servidor FTP.
	 O retorno será um objeto Vector para conexão com servidor SFTP.
	 **/
	public Object ls(String diretorio) throws Exception {

		try {
			this.validarConexao();
			LogUtil.Info("CONSULTANDO LISTAGEM DE ARQUIVOS/PASTAS DO DIRETORIO " + diretorio + " ...");
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				return this.lsFTP(diretorio);
			} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
				return this.lsSFTP(diretorio);
			}
			LogUtil.Info("LISTAGEM DE ARQUIVOS/PASTAS REALIZADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO LISTAR ARQUIVOS/PASTAS: " + e.getMessage());
			throw e;
		}
		return null;
	}

	/**
	 @author romatos
	 @return boolean - Retorna o status do download, true para concluido.
	 @param origem - Nome do arquivo do servidor que será realizado o download (Sem o caminho).
	 @param destino - Caminho do arquivo destino que será realizado o download do arquivo.
	 **/
	public Boolean download(String origem, String destino) throws Exception {

		try {
			Boolean retorno = null;
			this.validarConexao();
			LogUtil.Info("INICIANDO DOWNLOAD (" + origem + " => " + destino + ") ...");
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				retorno = this.downloadFTP(origem, destino);
			} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
				this.downloadSFTP(origem, destino);
			}
			LogUtil.Info("DOWNLOAD REALIZADO COM SUCESSO.");
			return retorno;
		} catch (Exception e) {
			LogUtil.Error("ERRO AO REALIZAR DOWNLOAD DO ARQUIVO (" + origem + " => " + destino + "): " + e.getMessage());
			throw e;
		}
	}

	/**
	 @author romatos
	 @param diretorio - Caminho do arquivo que o FTP/SFTP irá acessar.
	 **/
	public void cd(String diretorio) throws Exception {

		this.validarConexao();
		try {
			LogUtil.Info("ACESSANDO DIRETORIO " + diretorio + " ...");
			if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
				this.cdFTP(diretorio);
			} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
				this.cdSFTP(diretorio);
			}
			LogUtil.Info("DIRETORIO ACESSADO COM SUCESSO.");
		} catch (Exception e) {
			LogUtil.Error("ERRO AO ACESSAR DIRETORIO: " + e.getMessage());
			throw e;
		}
	}

	/**
	 @author romatos
	 Retorna um Objeto Boolean TRUE ou FALSE para o status da conexão com o servidor de FTP/SFTP.
	 **/
	public Boolean isConnected() {

		if (EnumTipoAcessoServidor.FTP.equals(this.type)) {
			if (this.ftp != null) {
				return this.ftp.isConnected();
			}
		} else if (EnumTipoAcessoServidor.SFTP.equals(this.type)) {
			if (this.sftpChannel != null) {
				return this.sftpChannel.isConnected();
			}
		}
		return false;
	}

	private void validarConexao() throws Exception {
		if (!this.isConnected()) {
			this.conectar();
		}
	}

	/**
	 * @author romatos
	 * @return - Retorna os dados do servidor.
	 */
	public FtpVO getFtpConfig() {
		return this.ftpConfig;
	}

	/**
	 * @author romatos
	 * @param ftpConfig - Dados do servidor que será utilizado.
	 */
	public void setFtpConfig(FtpVO ftpConfig) {
		this.ftpConfig = ftpConfig;
	}

	/**
	 * @author romatos
	 * @return - Retorna o tipo de acesso, FTP ou SFTP.
	 */
	public EnumTipoAcessoServidor getType() {
		return this.type;
	}

	/**
	 * @author romatos
	 * @param type - Tipo de acesso ao servidor, FTP ou SFTP.
	 */
	public void setType(EnumTipoAcessoServidor type) {
		this.type = type;
	}

	/**
	 * @author romatos
	 * @return - FTPClient (dados do acesso para tipo FTP)
	 */
	public FTPClient getFtp() {
		return this.ftp;
	}

	/**
	 * @author romatos
	 * @return - JSch (dados de acesso para tipo SFTP).
	 */
	public JSch getJsch() {
		return this.jsch;
	}

	/**
	 * @author romatos
	 * @return - Session (dados de acesso para tipo SFTP).
	 */
	public Session getSession() {
		return this.session;
	}

	/**
	 * @author romatos
	 * @return - Channel (dados de acesso para tipo SFTP).
	 */
	public Channel getChannel() {
		return this.channel;
	}

	/**
	 * @author romatos
	 * @return - ChannelSftp (dados de acesso para tipo SFTP).
	 */
	public ChannelSftp getSftpChannel() {
		return this.sftpChannel;
	}

	/********************************  FTP *************************************/
	private void conectarFTP() throws SocketException, IOException {

		if (this.isConnected()) {
			return;
		}
		this.ftp.connect(this.ftpConfig.getServidor());
		this.ftp.login(this.ftpConfig.getUsuario(), this.ftpConfig.getSenha());
	}

	private void desconectarFTP() throws IOException {

		if (this.isConnected()) {
			return;
		}
		this.ftp.logout();
		this.ftp.disconnect();
	}

	private boolean uploadFTP(String origem, String destino) throws Exception {

		try {
			this.ftp.enterLocalPassiveMode();
			this.cdFTP(destino);
			InputStream arquivo = new FileInputStream(new File(origem));
			String fileName = "";
			String separador = "/";
			if (destino.contains(".")) {
				if (!destino.contains(separador)) {
					separador = "\\";
				}
				fileName = destino.substring(destino.lastIndexOf(separador) + 1, destino.length());
			} else {
				if (!origem.contains(separador)) {
					separador = "\\";
				}
				fileName = origem.substring(origem.lastIndexOf(separador) + 1, origem.length());
			}
			boolean statusUpload = this.ftp.storeFile(fileName, arquivo);
			arquivo.close();
			return statusUpload;
		} catch (Exception e) {
			throw e;
		}
	}

	private boolean downloadFTP(String origem, String destino) throws Exception {

		try {
			File downloadFile = new File(destino);
			OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile));
			boolean statusDownload = this.ftp.retrieveFile(origem, outputStream1);
			outputStream1.close();
			return statusDownload;
		} catch (Exception e) {
			throw e;
		}
	}

	private void cdFTP(String diretorio) throws Exception {
		this.ftp.changeWorkingDirectory(diretorio);
	}

	private FTPFile[] lsFTP() throws Exception {
		FTPFile[] listFiles = this.ftp.listFiles();
		return listFiles;
	}

	private FTPFile[] lsFTP(String diretorio) throws Exception {
		FTPFile[] listFiles = this.ftp.listFiles(diretorio);
		return listFiles;
	}

	/******************************** SFTP ***********************************/
	private void conectarSFTP() throws Exception {

		if (this.isConnected()) {
			return;
		}
		try {
			if ((this.ftpConfig.getUsuario() == null || "".equals(this.ftpConfig.getUsuario()))
					&& (this.ftpConfig.getServidor() != null && !"".equals(this.ftpConfig.getServidor()))) {
				this.session = this.jsch.getSession(this.ftpConfig.getServidor());
			} else if (this.ftpConfig.getPorta() == null) {
				this.session = this.jsch.getSession(this.ftpConfig.getUsuario(), this.ftpConfig.getServidor());
			} else {
				this.session = this.jsch.getSession(this.ftpConfig.getUsuario(), this.ftpConfig.getServidor(), this.ftpConfig.getPorta());
			}
			this.session.setConfig("StrictHostKeyChecking", "no");
			this.session.setPassword(this.ftpConfig.getSenha());
			this.session.connect();
			this.channel = this.session.openChannel("sftp");
			this.channel.connect();
			this.sftpChannel = (ChannelSftp) this.channel;
			if (this.ftpConfig.getDiretorio() != null && !"".equals(this.ftpConfig.getDiretorio())) {
				this.cdSFTP(this.ftpConfig.getDiretorio());
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void desconectarSFTP() {

		if (this.isConnected()) {
			return;
		}
		this.sftpChannel.disconnect();
		this.channel.disconnect();
		this.session.disconnect();
	}

	private void cdSFTP(String diretorio) throws Exception {
		this.sftpChannel.cd(diretorio);
	}

	private Vector lsSFTP(String diretorio) throws SftpException {

		try {
			return this.sftpChannel.ls(diretorio);
		} catch (SftpException e) {
			throw e;
		}
	}

	private void uploadSFTP(String origem, String destino) throws Exception {

		try {
			File file = new File(origem);
			if (destino.contains(".")) {
				this.sftpChannel.put(new FileInputStream(file), destino, ChannelSftp.OVERWRITE);
			} else {
				this.sftpChannel.put(new FileInputStream(file), destino + file.getName(), ChannelSftp.OVERWRITE);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	private void downloadSFTP(String origem, String destino) throws SftpException {

		try {
			File file = new File(destino);
			this.sftpChannel.get(origem, file.getAbsolutePath(), null, ChannelSftp.OVERWRITE);
		} catch (SftpException e) {
			throw e;
		}
	}

	public void executarComandoSFTP(String comando) {

		try {
			this.validarConexao();
			BufferedReader br = null;
			String line = null;
			Channel channel = this.session.openChannel("exec");
			((ChannelExec) channel).setCommand(comando);
			channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			channel.connect();
			br = new BufferedReader(new InputStreamReader(in));
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			if (channel.isClosed()) {
				System.out.println("Exit status for execute ssh command is " + channel.getExitStatus());
			}
			if (in != null) {
				in.close();
				in = null;
			}
			if (br != null) {
				br.close();
				br = null;
			}
			try {
				Thread.sleep(1000);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
