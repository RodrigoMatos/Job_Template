package thread;

import java.lang.reflect.Method;

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
			@SuppressWarnings("rawtypes")
			Class[] classParametros = null;
			if (this.parametros != null) {
				classParametros = new Class[this.parametros.length];
				for (int i = 0; i < this.parametros.length; i++) {
					classParametros[i] = this.parametros[i].getClass();
				}
			}
			Method metodoEx = this.entidade.getClass().getMethod(this.metodo, classParametros);
			metodoEx.invoke(this.entidade, this.parametros);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
