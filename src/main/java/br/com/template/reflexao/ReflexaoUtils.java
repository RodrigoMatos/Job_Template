package br.com.template.reflexao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import br.com.template.exception.ConfigurationException;
import br.com.template.utils.ConvertType;

/**
 * @author romatos
 * @version 1.0
 */

public final class ReflexaoUtils {

	private ReflexaoUtils() {
	}

	/**
	 * @param classe     - Classe que possui o método static que será executado.
	 * @param nomeMetodo - Nome do método que será executado para a entidade.
	 * @param parametros - Parâmetros que o metodo espera (Caso não exista, deve-se
	 *                   passar null).
	 * @return Retorna o retorno do metodo que será executado (Caso seja void irá
	 *         retornar null).
	 * @throws Exception
	 * @author Rodrigo Oliveira Matos
	 */
	public static Object executarMetodoStatic(Class<?> classe, String nomeMetodo, Object... parametros) {

		if (classe != null && StringUtils.isNotBlank(nomeMetodo)) {
			return prepararExecutarMetodo(classe, null, nomeMetodo, parametros);
		} else {
			throw new ConfigurationException(
					"ReflexaoUtils.executarMetodoStatic - Classe ou nomeMetodo está nulo ou vazio.");
		}
	}

	/**
	 * @param entidade   - Objeto que será invocado o metodo.
	 * @param nomeMetodo - Nome do metodo que será executado para a entidade.
	 * @param parametros - Parametros que o metodo espera (Caso não exista, deve-se
	 *                   passar null).
	 * @return Retorna o retorno do metodo que será executado (Caso seja void irá
	 *         retornar null).
	 * @throws Exception
	 * @author Rodrigo Oliveira Matos
	 */
	public static Object executarMetodo(Object entidade, String nomeMetodo, Object... parametros) {

		try {
			if (entidade != null && !StringUtils.isNotBlank(nomeMetodo)) {
				return prepararExecutarMetodo(entidade.getClass(), entidade, nomeMetodo, parametros);
			} else {
				throw new ConfigurationException(
						"ReflexaoUtils.executarMetodo - Entidade ou nome do metodo está nulo ou vazio.");
			}
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	private static Object prepararExecutarMetodo(Class<?> classe, Object entidade, String nomeMetodo,
			Object... parametros) {
		try {
			return invokeMethod(classe, entidade, nomeMetodo, parametros, obterTiposParametro(parametros));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
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
			Class<?>[] classTypeParametros) {
		return invokeMethod(entidade.getClass(), entidade, nomeMetodo, parametros, classTypeParametros);
	}

	private static Object invokeMethod(Class<?> classe, Object entidade, String nomeMetodo, Object[] parametros,
			Class<?>[] classTypeParametros) {

		try {
			Method metodo;
			try {
				metodo = classe.getMethod(nomeMetodo, classTypeParametros);
			} catch (NoSuchMethodException e) {
				metodo = classe.getDeclaredMethod(nomeMetodo, classTypeParametros);
			}
			if (metodo == null) {
				throw new ConfigurationException("ReflexaoUtils.invokeMethod - Metodo não encontrado.");
			}
			return metodo.invoke(entidade, parametros);
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * @param classe       - Classe irá obter os nomes dos metodos.
	 * @param iniciandoCom - Filtro caso precise, irá obter somente os metodos que
	 *                     iniciem com este filtro (Caso não precise, passar nulo).
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 * @author Rodrigo Oliveira Matos
	 */
	public static List<String> obterNomeMetodos(Class<?> classe, String iniciandoCom) {

		List<String> listaNomeMetodos = new ArrayList<>();
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
	 * @param classe - Classe irá obter os nomes dos metodos.
	 * @return Retorna uma lista com os nomes dos metodos da classe.
	 * @author Rodrigo Oliveira Matos
	 */
	public static List<String> obterNomeMetodos(Class<?> classe) {
		return obterNomeMetodos(classe, null);
	}

	public static Object obterValorObjeto(Object objeto, String atributo) {
		try {
			Object value = objeto;
			if (atributo != null && atributo.contains(".")) {
				String[] campos = atributo.split("\\.");
				for (String att : campos) {
					if (value == null || StringUtils.isBlank(value.toString())) {
						return null;
					}
					value = obterValorDeUmCampo(value, att);
				}
			} else {
				value = obterValorDeUmCampo(objeto, atributo);
			}
			if (value == null || StringUtils.isBlank(value.toString())) {
				return null;
			}
			return value;
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	/**
	 * @param entidade     - Entidade que irá obter o valor do atributo
	 * @param nomeAtributo - Nome do atributo
	 * @return - Retorna o objeto referente ao atributo
	 * @throws Exception Executa o metodo get do atributo e retorna o resultado.
	 * @author Rodrigo Oliveira Matos
	 */
	public static Object executarMetodoGet(Object entidade, String nomeAtributo) {

		try {
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
				if (StringUtils.isNotBlank(nomeAtributo)) {
					return prepararExecutarMetodo(entidade.getClass(), entidade, formatarMetodoGet(nomeAtributo));
				} else {
					throw new ConfigurationException(
							"ReflexaoUtils.executarMetodoGet - Entidade ou nomeMetodo está nulo ou vazio.");
				}
			}
		} catch (Exception e) {
			throw new ConfigurationException(e);
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

	public static Object obterNovaInstancia(Class<?> classe) throws InstantiationException, IllegalAccessException {
		return classe.newInstance();
	}

	public static Object obterNovaInstancia(Object entity) throws InstantiationException, IllegalAccessException {
		return obterNovaInstancia(entity.getClass());
	}

	public static void executarMetodoSet(Object entidade, String nomeAtributo, Object value) {

		try {
			String[] metodos;
			Object retornoObject = entidade;
			if (entidade != null && nomeAtributo != null && nomeAtributo.contains(".")) {
				// Obter niveis dos atributos
				metodos = nomeAtributo.split("\\.", 2);
				// Verificar se o objeto está nulo.
				if (executarMetodoGet(entidade, metodos[0]) == null) {
					retornoObject = Class.forName(getField(entidade.getClass(), metodos[0]).getType().getName())
							.newInstance();// Instanciar objeto e obter a referência
					// Setar instância do objeto na entidade.
					setarValorObjeto(entidade, metodos[0], retornoObject);
				} else {
					// Obter a referência do objeto.
					retornoObject = executarMetodoGet(entidade, metodos[0]);
				}
				// Recursividade, setar valor ao objeto no proximo nivel.
				executarMetodoSet(retornoObject, metodos[1], value);
			} else {
				if (entidade != null && StringUtils.isNotBlank(nomeAtributo)) {
					// Setar valor no objeto.
					setarValorObjeto(entidade, nomeAtributo, ConvertType.getValue(entidade, nomeAtributo, value));
				} else {
					throw new ConfigurationException(
							"ReflexaoUtils.executarMetodoSet - Entidade ou nome do metodo está nulo ou vazio.");
				}
			}
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	public static void setarValorObjeto(Object entidade, String nomeAtributo, Object value)
			throws NoSuchFieldException, IllegalAccessException {
		Field f = getField(entidade.getClass(), nomeAtributo);
		if (f != null) {
			f.setAccessible(true);
			f.set(entidade, value);
		} else {
			throw new ConfigurationException(
					String.format("ReflexaoUtils.setarValorObjeto - Campo %s não encontrado na entidade %s",
							nomeAtributo, entidade.getClass()));
		}
	}

	public static Object obterValorDeUmCampo(Object entidade, String nomeAtributo)
			throws NoSuchFieldException, IllegalAccessException {
		if (entidade != null) {
			Field f = getField(entidade.getClass(), nomeAtributo);
			if (f != null) {
				f.setAccessible(true);
				return f.get(entidade);
			}
		}
		return null;
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

	public static void setValuePkEntity(Object entity, Object value) throws Exception {
		getNomeAtributoPkAndSetValueRecursivo(entity, value, entity.getClass());
	}

	public static String getNomeAtributoPk(Class<?> classe) throws Exception {
		return getNomeAtributoPkAndSetValueRecursivo(null, null, classe);
	}

	private static String getNomeAtributoPkAndSetValueRecursivo(Object object, Object value, Class<?> classe)
			throws Exception {

		for (Field field : classe.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) != null) {
				if (object != null) {
					field.setAccessible(true);
					field.set(object, value);
				}
				return field.getName();
			} else if (field.getAnnotation(EmbeddedId.class) != null) {
				if (object != null) {
					field.setAccessible(true);
					field.set(object, value);
				}
				return field.getName();
			}
		}
		if (classe.getSuperclass() != null) {
			// CASO NÃO ENCONTRE A PK NA CLASSE, VERIFICAR NA CLASSE PAI.
			return getNomeAtributoPkAndSetValueRecursivo(object, value, classe.getSuperclass());
		} else {
			// NÃO ENCONTROU NENHUM ATRIBUTO COM A ANOTAÇÃO DE CHAVE PRIMARIA
			throw new ConfigurationException(
					"ReflexaoUtils.getNomeAtributoPk - Atributo PrimaryKey não encontrada na entidade informada");
		}
	}

	public static Object getValorAtributoPk(Object object) throws Exception {
		return getValorAtributoPkRecursivo(object, object.getClass());
	}

	private static Object getValorAtributoPkRecursivo(Object object, Class<?> classe) throws Exception {

		for (Field field : classe.getDeclaredFields()) {
			if (field.getAnnotation(Id.class) != null) {
				field.setAccessible(true);
				return field.get(object);
			} else if (field.getAnnotation(EmbeddedId.class) != null) {
				field.setAccessible(true);
				return field.get(object);
			}
		}
		if (classe.getSuperclass() != null) {
			// CASO NÃO ENCONTRE A PK NA CLASSE, VERIFICAR NA CLASSE PAI.
			return getValorAtributoPkRecursivo(object, classe.getSuperclass());
		} else {
			// NÃO ENCONTROU NENHUM ATRIBUTO COM A ANOTAÇÃO DE CHAVE PRIMARIA
			throw new ConfigurationException(
					"ReflexaoUtils.getValorAtributoPkClasse - Atributo PrimaryKey não encontrada na entidade informada");
		}
	}

	public static String verificarExistenciaPropriedade(String propriedade, Class<?> classe) {

		try {
			if (propriedade.contains(".")) {
				return obterSubClass(classe, propriedade);
			} else {
				ReflexaoUtils.getField(classe, propriedade);
				return propriedade;
			}
		} catch (NoSuchFieldException e) {
			if (classe.getSuperclass() != null) {
				return verificarExistenciaPropriedade(propriedade, classe.getSuperclass());
			}
			return null;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String obterSubClass(Class<?> classe, String propriedade) throws NoSuchFieldException {

		String retorno = null;
		String[] array = propriedade.split("\\.", 2);

		Field field = ReflexaoUtils.getField(classe, array[0]);
		Class<?> classTemp = field.getType();

		if (array.length > 1) {
			retorno = obterSubClass(classTemp, array[1]);
		} else {
			try {
				ReflexaoUtils.getField(classe, propriedade);
				retorno = array[0];
			} catch (Exception e) {
				return null;
			}
		}
		return retorno;
	}

	public static List<String> getFieldsFromClass(Class<?> classe) {
		List<Field> fields = new ArrayList<>();
		do {
			fields.addAll(Arrays.asList(classe.getDeclaredFields()));
			classe = classe.getSuperclass();
		} while (classe != null);

		List<String> fieldsString = new ArrayList<>();
		for (Field field : fields) {
			fieldsString.add(field.getName());
		}
		return fieldsString;
	}

	public static List<Field> getFields(Class<?> classe) {
		List<Field> listFields = new ArrayList<>(0);

		do {
			listFields.addAll(Arrays.asList(classe.getDeclaredFields()));
			classe = classe.getSuperclass();
		} while (classe != null);
		return listFields;
	}

	public static boolean existeAlgumCampoPreenchido(Object entity, String... fields) {
		if (entity != null) {
			for (String field : fields) {
				if (obterValorObjeto(entity, field) != null) {
					return true;
				}
			}
		}
		return false;
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
