package model;

import java.io.Serializable;

/**
 * @author romatos
 * @version 1.0
 */

public class BancoDadosVO implements Serializable {
	
	private static final long serialVersionUID = -7127614751869751996L;
	private String urlBancoDados;
	private String usuario;
	private String senha;
	private String classForName;
	private Integer qtdConexoes;
	private Integer qtdParticoes;
	private Boolean autoCommit;
	
	/**
	 * @param String - URL do banco de dados para acesso.
	 * @param String - Usuario para acesso ao banco de dados.
	 * @param String - Senha para acesso ao banco de dados.
	 * @param String - Nome do drive para acesso ao banco de dados.
	 * @param Integer - Quantidade de conexões por partição (em caso de utilizar pool de conexões).
	 * @param Integer - Quantidade de partições (em caso de utilizar pool de conexões).
	 * @param boolean - Valor lógico para definir o autocommit (em caso de utilizar pool de conexões).
	 * @param String - Comando (select) para o teste de conexão (em caso de utilizar pool de conexões).
	 * @author romatos
	 */
	public BancoDadosVO(String urlBancoDados, String usuario, String senha, String classForName, Integer qtdConexoes, Integer qtdParticoes, Boolean autoCommit) {
		this.urlBancoDados = urlBancoDados;
		this.usuario = usuario;
		this.senha = senha;
		this.classForName = classForName;
		this.qtdConexoes = qtdConexoes;
		this.qtdParticoes = qtdParticoes;
		this.autoCommit = autoCommit;
	}

	/**
	 * @author romatos
	 * @return String - Url do banco de dados.
	 */
	public String getUrlBancoDados() {
		return urlBancoDados;
	}
	
	/**
	 * @author romatos
	 * @param String - URL do banco de dados para acesso.
	 */
	public void setUrlBancoDados(String urlBancoDados) {
		this.urlBancoDados = urlBancoDados;
	}
	
	/**
	 * @author romatos
	 * @return String - Usuario do banco de dados.
	 */
	public String getUsuario() {
		return usuario;
	}
	
	/**
	 * @author romatos
	 * @param String - Usuario para acesso ao banco de dados.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @author romatos
	 * @return String - Senha do banco de dados.
	 */
	public String getSenha() {
		return senha;
	}
	
	/**
	 * @author romatos
	 * @param String - Senha para acesso ao banco de dados.
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	/**
	 * @author romatos
	 * @return String - Driver do banco de dados.
	 */
	public String getClassForName() {
		return classForName;
	}
	
	/**
	 * @author romatos
	 * @param String - Nome do drive para acesso ao banco de dados.
	 */
	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}
	
	/**
	 * @author romatos
	 * @return Integer - Quantidade de conexões por partições.
	 */
	public Integer getQtdConexoes() {
		return qtdConexoes;
	}

	/**
	 * @author romatos
	 * @param Integer - Quantidade de conexões por partição (em caso de utilizar pool de conexões).
	 */
	public void setQtdConexoes(Integer qtdConexoes) {
		this.qtdConexoes = qtdConexoes;
	}

	/**
	 * @author romatos
	 * @return Integer - Quantidade de partições para acesso ao banco de dados.
	 */
	public Integer getQtdParticoes() {
		return qtdParticoes;
	}

	/**
	 * @author romatos
	 * @param Integer - Quantidade de partições (em caso de utilizar pool de conexões).
	 */
	public void setQtdParticoes(Integer qtdParticoes) {
		this.qtdParticoes = qtdParticoes;
	}
	
	/**
	 * @author romatos
	 * @return Boolean - Status do commit do pool de conexão.
	 */
	public Boolean isAutoCommit() {
		return autoCommit;
	}

	/**
	 * @author romatos
	 * @param Boolean - Valor lógico para definir o autocommit (em caso de utilizar pool de conexões).
	 */
	public void setAutoCommit(Boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((classForName == null) ? 0 : classForName.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result
				+ ((urlBancoDados == null) ? 0 : urlBancoDados.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BancoDadosVO other = (BancoDadosVO) obj;
		if (classForName == null) {
			if (other.classForName != null)
				return false;
		} else if (!classForName.equals(other.classForName))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		if (urlBancoDados == null) {
			if (other.urlBancoDados != null)
				return false;
		} else if (!urlBancoDados.equals(other.urlBancoDados))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
}
