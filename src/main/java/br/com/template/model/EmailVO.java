package br.com.template.model;

import java.io.File;
import java.io.Serializable;

/**
 * @author romatos
 * @version 1.0
 */

public class EmailVO implements Serializable {

	private static final long serialVersionUID = 4962084057112535801L;

	private Integer codigo;
	private String assunto;
	private String mensagem;
	private File arquivoAnexado;
	private String de;
	private String para;

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public File getArquivoAnexado() {
		return arquivoAnexado;
	}

	public void setArquivoAnexado(File arquivoAnexado) {
		this.arquivoAnexado = arquivoAnexado;
	}

	public void setArquivoAnexado(String caminho) {
		this.arquivoAnexado = new File(caminho);
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDe() {
		return de;
	}

	public void setDe(String de) {
		this.de = de;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

}
