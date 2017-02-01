package reflexao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author romatos
 * @version 1.0
 */

public class ExecutorMetodo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @author romatos
	 * @param classe - Classe que possui o m�todo static que ser� executado.
	 * @param nomeMetodo - Nome do m�todo que ser� executado para a entidade.
	 * @param parametros - Par�metros que o metodo espera (Caso n�o exista, deve-se passar null).
	 * @return Retorna o retorno do metodo que ser� executado (Caso seja void ir� retornar null).
	 * @throws Exception
	 */
	public static Object executarMetodoStatic(Class classe, String nomeMetodo, Object... parametros) throws Exception {
		if (classe != null && nomeMetodo != null && !"".equals(nomeMetodo)) {
			return executarMetodo(classe, null, nomeMetodo, parametros);
		} else {
			throw new NullPointerException("Classe ou nomeMetodo est� nulo ou vazio.");
		}
	}

	/**
	 * @author romatos
	 * @param entidade - Objeto que ser� invocado o metodo.
	 * @param nomeMetodo - Nome do metodo que ser� executado para a entidade.
	 * @param parametros - Parametros que o metodo espera (Caso n�o exista, deve-se passar null).
	 * @return Retorna o retorno do metodo que ser� executado (Caso seja void ir� retornar null).
	 * @throws Exception
	 */
	public static Object executarMetodo(Object entidade, String nomeMetodo, Object... parametros) throws Exception {
		if (entidade != null && nomeMetodo != null && !"".equals(nomeMetodo)) {
			return executarMetodo(entidade.getClass(), entidade, nomeMetodo, parametros);
		} else {
			throw new NullPointerException("Entidade ou nomeMetodo est� nulo ou vazio.");
		}
	}

	private static Object executarMetodo(Class classe, Object entidade, String nomeMetodo, Object... parametros) throws Exception {

		Class[] classParametros = null;
		if (parametros != null) {
			classParametros = new Class[parametros.length];
			for (int i = 0; i < parametros.length; i++) {
				classParametros[i] = parametros[i].getClass();
			}
		}
		Method metodo = classe.getMethod(nomeMetodo, classParametros);
		if (metodo == null) {
			throw new Exception("Metodo n�o encontrado.");
		}
		return metodo.invoke(entidade, parametros);
	}

	/**
	 * @author romatos
	 * @param classe - Classe ir� obter os nomes dos metodos.
	 * @param iniciandoCom - Filtro caso precise, ir� obter somente os metodos que iniciem com este filtro (Caso n�o precise, passar nulo). 
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 */
	public static List<String> obterNomeMetodos(Class classe, String iniciandoCom) {

		List<String> listaNomeMetodos = new ArrayList<String>();
		Method[] metodos = classe.getMethods();
		if (metodos.length > 0) {
			for (Method method : metodos) {
				if (iniciandoCom == null) {
					listaNomeMetodos.add(method.getName());
				} else {
					if (method.getName().startsWith(iniciandoCom)) {
						listaNomeMetodos.add(method.getName());
					}
				}
			}
		}
		return listaNomeMetodos;
	}

	/**
	 * @author romatos
	 * @param classe - Classe ir� obter os nomes dos metodos.
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 */
	public static List<String> obterNomeMetodos(Class classe) {
		return obterNomeMetodos(classe, null);
	}

	/**
	 * @author romatos
	 * @param entidade - Entidade que ir� obter o valor do atributo
	 * @param nomeAtributo - Nome do atributo
	 * @return - Retorna o objeto referente ao atributo
	 * @throws Exception
	 * Executa o metodo get do atributo e retorna o resultado.
	 */
	public static Object executarMetodoGet(Object entidade, String nomeAtributo) throws Exception {
		
		String[] metodos;
		Object retornoObject = entidade;
		if (nomeAtributo.contains(".")) {
			metodos = nomeAtributo.split("\\.");
			for (String metodoGET : metodos) {
				if (retornoObject == null) {
					break;
				}
				retornoObject = executarMetodoGet(retornoObject, formatarMetodoGet(metodoGET));
			}
			return retornoObject;
		} else {
			if (entidade != null && nomeAtributo != null && !"".equals(nomeAtributo)) {
				return executarMetodo(entidade.getClass(), entidade, formatarMetodoGet(nomeAtributo), null);
			} else {
				throw new NullPointerException("Entidade ou nomeMetodo est� nulo ou vazio.");
			}
		}
	}

	/**
	 * @author romatos
	 * @param atributo - Nome do atributo de algum objeto
	 * @return - Retorna o metodo get do atributo (formatado, com get na frente e a primeira letra em Upper case).
	 */
	public static String formatarMetodoGet(String atributo) {
		if (!atributo.startsWith("get")) {
			return "get" + atributo.substring(0, 1).toUpperCase() + atributo.substring(1, atributo.length());
		} else {
			return atributo;
		}
	}
}
