package ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpAcesso implements Serializable {

	private static final long serialVersionUID = -2870598128854085312L;

	private JSch jsch;
	private Session session;
	private Channel channel;
	private ChannelSftp sftpChannel;

	public SftpAcesso() {
		this.jsch = new JSch();
	}
	
	
	public void conectar(String servidor, String usuario, String senha) throws Exception {
		conectar(servidor, usuario, senha, null);
	}

	public void conectar(String servidor, String usuario, String senha, Integer porta) throws Exception {

		if (isConnected()) {
			return;
		}
		
		try {
			if (porta == null)
				this.session = jsch.getSession(usuario, servidor);
			else
				this.session = jsch.getSession(usuario, servidor, porta);
			this.session.setConfig("StrictHostKeyChecking", "no");
			this.session.setPassword(senha);
			this.session.connect();

			this.channel = this.session.openChannel("sftp");
			this.channel.connect();
			this.sftpChannel = (ChannelSftp) this.channel;
		} catch (Exception e) {
			throw e;
		}
	}

	public void desconectar() {
		this.sftpChannel.disconnect();
		this.channel.disconnect();
		this.session.disconnect();
	}

	public void cd(String diretorio) throws SftpException {
		try {
			this.sftpChannel.cd(diretorio);
		} catch (SftpException e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	public Vector ls(String diretorio) throws SftpException {
		try {
			return this.sftpChannel.ls(diretorio);
		} catch (SftpException e) {
			throw e;
		}
	}

	public void upload(String origem, String destino) throws Exception {
		try {
			File file = new File(origem);
			sftpChannel.put(new FileInputStream(file), destino + file.getName(), ChannelSftp.OVERWRITE);
		} catch (Exception e) {
			throw e;
		}
	}

	private void get(String origem, String destino) throws SftpException {
		File file = new File(destino);
		this.sftpChannel.get(origem, file.getAbsolutePath(), null, ChannelSftp.OVERWRITE);
	}

	public void download(String origem, String destino) throws SftpException {
		try {
			this.get(origem, destino);
		} catch (SftpException e) {
			throw e;
		}
	}

	public boolean isConnected() {
		if (this.sftpChannel != null)
			return this.sftpChannel.isConnected();
		return false;
	}

	public JSch getJsch() {
		return jsch;
	}

	public Session getSession() {
		return session;
	}

	public Channel getChannel() {
		return channel;
	}

	public ChannelSftp getSftpChannel() {
		return sftpChannel;
	}

	public void executarComando(String comando) {

		try {
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
