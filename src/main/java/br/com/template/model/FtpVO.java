package br.com.template.model;

import java.io.Serializable;

/**
 * @author romatos
 * @version 1.0
 */

public class FtpVO implements Serializable {

	private static final long serialVersionUID = 3139660600249334104L;

	private String servidor;
	private String usuario;
	private String senha;
	private String diretorio;
	private Integer porta;

	public FtpVO() {
	}

	public FtpVO(String servidor, String usuario, String senha, String diretorio, Integer porta) {
		this.servidor = servidor;
		this.usuario = usuario;
		this.senha = senha;
		this.diretorio = diretorio;
		this.porta = porta;
	}

	public FtpVO(String servidor, String usuario, String senha, String diretorio) {
		this.servidor = servidor;
		this.usuario = usuario;
		this.senha = senha;
		this.diretorio = diretorio;
	}

	public FtpVO(String servidor, String usuario, String senha) {
		this.servidor = servidor;
		this.usuario = usuario;
		this.senha = senha;
	}

	public String getServidor() {
		return servidor;
	}

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getDiretorio() {
		return diretorio;
	}

	public void setDiretorio(String diretorio) {
		this.diretorio = diretorio;
	}

	public Integer getPorta() {
		return porta;
	}

	public void setPorta(Integer porta) {
		this.porta = porta;
	}

}
