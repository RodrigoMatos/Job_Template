package model;

import java.io.File;
import java.io.Serializable;

public class EmailVO implements Serializable {

	private static final long serialVersionUID = 4962084057112535801L;

	private Integer codigo;
	private String assunto;
	private String mensagem;
	private File arquivoAnexado;

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

}
