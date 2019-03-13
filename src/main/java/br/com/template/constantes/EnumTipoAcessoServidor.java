package br.com.template.constantes;

/**
 * @author romatos
 * @version 1.0
 */

public enum EnumTipoAcessoServidor {

	FTP(1, "FTP"), SFTP(2, "SFTP");

	private Integer type;
	private String descricao;

	EnumTipoAcessoServidor(Integer type, String descricao) {
		this.type = type;
		this.descricao = descricao;
	}

	public Integer getType() {
		return this.type;
	}

	public String getDescricao() {
		return this.descricao;
	}

}
