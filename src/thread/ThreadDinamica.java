package thread;

import reflexao.ExecutorMetodo;

/**
 * @author romatos
 * @version 1.0
 */

public class ThreadDinamica extends Thread {

	private Object[] parametros;
	private Object entidade;
	private String metodo;

	/**
	 * @param Object - Instância do objeto que possui o método que será executado pela thread.
	 * @param String - Nome do método (existente na entidade) que será executado.
	 * @param Object... - Parâmetros para o método que será executado.
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
			ExecutorMetodo.executarMetodo(this.entidade, this.metodo, this.parametros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
