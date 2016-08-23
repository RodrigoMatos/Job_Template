package ftp;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

public class FtpRN implements Serializable {

	private static final long serialVersionUID = -7842979723746146852L;

	private FTPClient ftp;
	private String servidor;
	private String usuario;
	private String senha;

	public FtpRN() {
		this.ftp = new FTPClient();
	}

	public void conectar(String servidor, String usuario, String senha) throws Exception {

		try {
			this.servidor = servidor;
			this.usuario = usuario;
			this.senha = senha;
			this.ftp.connect(this.servidor);
			this.ftp.login(this.usuario, this.senha);
		} catch (Exception e) {
			throw e;
		}
	}

	public void desconectar() throws Exception {
		try {
			this.ftp.logout();
			this.ftp.disconnect();
		} catch (Exception e) {
			throw e;
		}
	}

	public void cd(String diretorio) throws Exception {
		try {
			this.validarConexao();
			this.ftp.changeWorkingDirectory(diretorio);
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean download(String origem, String destino) throws Exception {

		try {
			this.validarConexao();
			File downloadFile1 = new File(destino);
			OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
			boolean statusDownload = this.ftp.retrieveFile(origem, outputStream1);
			outputStream1.close();
			return statusDownload;
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean upload(String origem, String destino) throws Exception {

		try {
			this.validarConexao();
			this.ftp.enterLocalPassiveMode();
			this.cd(destino);
			InputStream arquivo = new FileInputStream(new File(origem));
			String fileName = origem.substring(origem.lastIndexOf("/")+1, origem.length());
			boolean statusUpload = this.ftp.storeFile(fileName, arquivo);
			arquivo.close();
			return statusUpload;
		} catch (Exception e) {
			throw e;
		}
	}

	public FTPFile[] ls() throws Exception {
		try {
			this.validarConexao();
			FTPFile[] listFiles = this.ftp.listFiles();
			return listFiles;
		} catch (Exception e) {
			throw e;
		}
	}

	public FTPFile[] ls(String diretorio) throws Exception {
		try {
			this.validarConexao();
			FTPFile[] listFiles = this.ftp.listFiles(diretorio);
			return listFiles;
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarConexao() throws Exception {
		if (!this.ftp.isConnected()) {
			this.conectar(this.servidor, this.usuario, this.senha);
		}
	}

	public Boolean isConnected() {
		return this.ftp.isConnected();
	}

}
