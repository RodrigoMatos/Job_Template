package br.com.template.enuns;

import lombok.Getter;

/**
 * @author romatos
 * @version 1.0
 */

@Getter
public enum EnumTipoAcessoServidor {

	FTP(1, "FTP"), SFTP(2, "SFTP");

	private Integer type;
	private String descricao;

	EnumTipoAcessoServidor(Integer type, String descricao) {
		this.type = type;
		this.descricao = descricao;
	}

}
