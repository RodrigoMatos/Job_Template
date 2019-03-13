package br.com.template.model;

import java.io.Serializable;

public class ConfigVO implements Serializable {

	private static final long serialVersionUID = -3357291782854371261L;

	private Boolean removerArquivoConfig;

	public ConfigVO() {
	}

	public Boolean getRemoverArquivoConfig() {
		return removerArquivoConfig;
	}

	public void setRemoverArquivoConfig(Boolean removerArquivoConfig) {
		this.removerArquivoConfig = removerArquivoConfig;
	}

}
