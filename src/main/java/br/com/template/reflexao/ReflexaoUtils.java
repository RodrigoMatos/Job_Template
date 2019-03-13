package br.com.template.reflexao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import br.com.template.utils.ConvertType;

/**
 * @author romatos
 * @version 1.0
 */

public final class ReflexaoUtils {

	private ReflexaoUtils() {
	}

	/**
	 * @param classe - Classe que possui o m�todo static que ser� executado.
	 * @param nomeMetodo - Nome do m�todo que ser� executado para a entidade.
	 * @param parametros - Par�metros que o metodo espera (Caso n�o exista, deve-se
	 *                   passar null).
	 * @return Retorna o retorno do metodo que ser� executado (Caso seja void ir�
	 *         retornar null).
	 * @author Rodrigo Oliveira Matos
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static Object executarMetodoStatic(Class<?> classe, String nomeMetodo, Object... parametros)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		if (classe != null && nomeMetodo != null && !"".equals(nomeMetodo)) {
			return prepararExecutarMetodo(classe, null, nomeMetodo, parametros);
		} else {
			throw new NullPointerException(
					"ReflexaoUtils.executarMetodoStatic - Classe ou nomeMetodo est� nulo ou vazio.");
		}
	}

	/**
	 * @param entidade   - Objeto que ser� invocado o metodo.
	 * @param nomeMetodo - Nome do metodo que ser� executado para a entidade.
	 * @param parametros - Parametros que o metodo espera (Caso n�o exista, deve-se
	 *                   passar null).
	 * @return Retorna o retorno do metodo que ser� executado (Caso seja void ir�
	 *         retornar null).
	 * @throws Exception
	 * @author Rodrigo Oliveira Matos
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static Object executarMetodo(Object entidade, String nomeMetodo, Object... parametros)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		if (entidade != null && nomeMetodo != null && !"".equals(nomeMetodo)) {
			return prepararExecutarMetodo(entidade.getClass(), entidade, nomeMetodo, parametros);
		} else {
			throw new NullPointerException(
					"ReflexaoUtils.executarMetodo - Entidade ou nome do metodo est� nulo ou vazio.");
		}
	}

	private static Object prepararExecutarMetodo(Class<?> classe, Object entidade, String nomeMetodo,
			Object... parametros) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(classe, entidade, nomeMetodo, parametros, obterTiposParametro(parametros));
	}

	private static Class<?>[] obterTiposParametro(Object[] parametros) {
		Class<?>[] classParametros = null;
		if (parametros != null) {
			classParametros = new Class[parametros.length];
			for (int i = 0; i < parametros.length; i++) {
				classParametros[i] = parametros[i].getClass();
			}
		}
		return classParametros;
	}

	public static Object invokeMethod(Object entidade, String nomeMetodo, Object[] parametros,
			Class<?>[] classTypeParametros)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(entidade.getClass(), entidade, nomeMetodo, parametros, classTypeParametros);
	}

	private static Object invokeMethod(Class<?> classe, Object entidade, String nomeMetodo, Object[] parametros,
			Class<?>[] classTypeParametros)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		Method metodo;
		try {
			metodo = classe.getMethod(nomeMetodo, classTypeParametros);
		} catch (NoSuchMethodException e) {
			metodo = classe.getDeclaredMethod(nomeMetodo, classTypeParametros);
		}
		if (metodo == null) {
			throw new NoSuchMethodException("ReflexaoUtils.invokeMethod - Metodo n�o encontrado.");
		}
		return metodo.invoke(entidade, parametros);
	}

	/**
	 * @param classe       - Classe ir� obter os nomes dos metodos.
	 * @param iniciandoCom - Filtro caso precise, ir� obter somente os metodos que
	 *                     iniciem com este filtro (Caso n�o precise, passar nulo).
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 * @author Rodrigo Oliveira Matos
	 */
	public static List<String> obterNomeMetodos(Class<?> classe, String iniciandoCom) {

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
	 * @param classe - Classe ir� obter os nomes dos metodos.
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 * @author Rodrigo Oliveira Matos
	 */
	public static List<String> obterNomeMetodos(Class<?> classe) {
		return obterNomeMetodos(classe, null);
	}

	/**
	 * @param entidade     - Entidade que ir� obter o valor do atributo
	 * @param nomeAtributo - Nome do atributo
	 * @return - Retorna o objeto referente ao atributo
	 * @throws Exception Executa o metodo get do atributo e retorna o resultado.
	 * @author Rodrigo Oliveira Matos
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 */
	public static Object executarMetodoGet(Object entidade, String nomeAtributo)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		if (entidade == null) {
			return null;
		}
		String[] metodos;
		Object retornoObject = entidade;
		if (nomeAtributo != null && nomeAtributo.contains(".")) {
			metodos = nomeAtributo.split("\\.");
			for (String metodoGET : metodos) {
				if (retornoObject == null) {
					break;
				}
				retornoObject = executarMetodoGet(retornoObject, formatarMetodoGet(metodoGET));
			}
			return retornoObject;
		} else {
			if (nomeAtributo != null && StringUtils.isNotBlank(nomeAtributo)) {
				return prepararExecutarMetodo(entidade.getClass(), entidade, formatarMetodoGet(nomeAtributo));
			} else {
				throw new NullPointerException(
						"ReflexaoUtils.executarMetodoGet - Entidade ou nomeMetodo est� nulo ou vazio.");
			}
		}
	}

	/**
	 * @param atributo - Nome do atributo de algum objeto
	 * @return - Retorna o metodo get do atributo (formatado, com get na frente e a
	 *         primeira letra em Upper case).
	 * @author Rodrigo Oliveira Matos
	 */
	public static String formatarMetodoGet(String atributo) {
		if (!atributo.startsWith("get")) {
			return "get" + atributo.substring(0, 1).toUpperCase() + atributo.substring(1, atributo.length());
		} else {
			return atributo;
		}
	}

	public static Field getField(Class<?> classe, String atributo) throws NoSuchFieldException {
		try {
			return classe.getDeclaredField(atributo);
		} catch (NoSuchFieldException e) {
			if (classe.getSuperclass() != null) {
				return getField(classe.getSuperclass(), atributo);
			} else {
				throw e;
			}
		}
	}

	public static void executarMetodoSet(Object entidade, String nomeAtributo, Object value) throws Exception {

		if (entidade == null) {
			throw new NullPointerException(
					"ReflexaoUtils.executarMetodoSet - Entidade ou nome do metodo est� nulo ou vazio.");
		}
		String[] metodos;
		Object retornoObject = entidade;
		if (nomeAtributo != null && nomeAtributo.contains(".")) {
			// Obter niveis dos atributos
			metodos = nomeAtributo.split("\\.", 2);
			// Verificar se o objeto est� nulo.
			if (executarMetodoGet(entidade, metodos[0]) == null) {
				retornoObject = Class.forName(getField(entidade.getClass(), metodos[0]).getType().getName())
						.newInstance();// Instanciar objeto e obter a refer�ncia
				// Setar inst�ncia do objeto na entidade.
				setarValorObjeto(entidade, metodos[0], retornoObject);
			} else {
				// Obter a refer�ncia do objeto.
				retornoObject = executarMetodoGet(entidade, metodos[0]);
			}
			// Recursividade, setar valor ao objeto no proximo nivel.
			executarMetodoSet(retornoObject, metodos[1], value);
		} else {
			if (nomeAtributo != null && StringUtils.isNotBlank(nomeAtributo)) {
				// Setar valor no objeto.
				setarValorObjeto(entidade, nomeAtributo, ConvertType.getValue(entidade, nomeAtributo, value));
			} else {
				throw new NullPointerException(
						"ReflexaoUtils.executarMetodoSet - Entidade ou nome do metodo est� nulo ou vazio.");
			}
		}
	}

	public static void setarValorObjeto(Object entidade, String nomeAtributo, Object value) throws Exception {
		Field f = getField(entidade.getClass(), nomeAtributo);
		if (f != null) {
			f.setAccessible(true);
			f.set(entidade, value);
		} else {
			throw new Exception(String.format("ReflexaoUtils.setarValorObjeto - Campo %s n�o encontrado na entidade %s",
					nomeAtributo, entidade.getClass()));
		}

	}

	/**
	 * @param atributo - Nome do atributo de algum objeto
	 * @return - Retorna o metodo get do atributo (formatado, com set na frente e a
	 *         primeira letra em Upper case).
	 * @author Rodrigo Oliveira Matos
	 */
	public static String formatarMetodoSet(String atributo) {
		if (!atributo.startsWith("set")) {
			return "set" + atributo.substring(0, 1).toUpperCase() + atributo.substring(1, atributo.length());
		} else {
			return atributo;
		}
	}

	public static boolean valuesEquals(Object obj1, Object obj2) throws IllegalAccessException {
		if (obj1 == null && obj2 == null) {
			return true;
		}
		if (!compareValidObj(obj1, obj2)) {
			return false;
		}
		return compareFields(obj1, obj2);
	}

	private static boolean compareFields(Object obj1, Object obj2) throws IllegalAccessException {
		if (obj1 instanceof List || obj1 instanceof Collection) {
			List<?> list1 = ((List<?>) obj1);
			List<?> list2 = ((List<?>) obj2);
			if (list1.size() != list2.size()) {
				return false;
			}
			for (int i = 0; i < list1.size(); i++) {
				if (!compareFields(list1.get(i), list2.get(i))) {
					return false;
				}
			}
		} else {
			Field[] fields = obj1.getClass().getDeclaredFields();
			if (fields != null && fields.length > 0) {
				Object value1;
				Object value2;
				for (Field f : fields) {
					f.setAccessible(true);
					value1 = f.get(obj1);
					value2 = f.get(obj2);
					if ((value1 != null || value2 != null)) {
						if (!compareValidObj(value1, value2)) {
							return false;
						} else {
							if (value1.getClass().isPrimitive() && value1 != value2) {
								return false;
							} else if (!value1.getClass().getPackage().getName().startsWith("java.lang")) {
								if (!compareFields(value1, value2)) {
									return false;
								}
							} else if (!value1.equals(value2)) {
								return false;
							}
						}
					}
				}
			} else {
				if (obj1.getClass().isPrimitive()) {
					return obj1 == obj2;
				} else {
					return obj1.equals(obj2);
				}
			}
		}
		return true;
	}

	private static boolean compareValidObj(Object obj1, Object obj2) {
		if (obj1 == null && obj2 == null) {
			return true;
		} else if (obj1 == null || obj2 == null) {
			return false;
		}
		return true;
	}

}
