package br.com.template.thread;

import br.com.template.reflexao.ReflexaoUtils;

/**
 * @author romatos
 * @version 1.0
 */

public class ThreadDinamica extends Thread {

	private Object[] parametros;
	private Object entidade;
	private String metodo;

	/**
	 * @param Object - Inst�ncia do objeto que possui o m�todo que ser� executado pela thread.
	 * @param String - Nome do m�todo (existente na entidade) que ser� executado.
	 * @param Object... - Par�metros para o m�todo que ser� executado.
	 * @author romatos
	 */
	public ThreadDinamica(Object entidade, String metodo, Object... parametros) {
		this.entidade = entidade;
		this.metodo = metodo;
		this.parametros = parametros;
	}

	@Override
	public void run() {
		try {
			ReflexaoUtils.executarMetodo(this.entidade, this.metodo, this.parametros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
