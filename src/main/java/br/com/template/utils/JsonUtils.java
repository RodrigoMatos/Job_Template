package br.com.template.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

/**
 * Classe que tem metodos utilitarios que utilizam Gson
 *
 * @author Rodrigo Oliveira Matos
 * @version 1.0
 */

public final class JsonUtils {

	private JsonUtils() {
	}

	/**
	 * @author Rodrigo.Matos
	 * @param Object - Objeto que será gerado o json custom (parecido com o do gson)
	 * @return String - retorna o json do objeto.
	 */
	public static String gerarJsonCustom(Object object) {

		if (object == null || jsonCustomIgnoreClass(object.getClass())) {
			return null;
		}
		Class<?> classe = object.getClass();
		StringBuilder json = new StringBuilder();
		List<Field> campos = FieldUtils.getAllFieldsList(classe);
		int qtdRegistros = campos.size();
		int index = 1;

		if (jsonPackageEntity(classe.toString()) && !jsonCustomIgnoreClass(classe)) {
			json.append("{");
			for (Field campo : campos) {
				campo.setAccessible(true);
				if (!jsonCustomIgnoreAnnotation(campo) && !jsonCustomIgnoreAttribute(campo)
						&& !jsonCustomIgnoreField(campo) && !jsonCustomIgnoreClass(campo.getType())) {
					String temp = construirJsonCustom(campo, object);
					if (index <= qtdRegistros && !StringUtils.isEmpty(temp)) {
						if (json.length() > 1) {
							json.append(",");
						}
						json.append(temp);
					}
					index++;
				}
			}
			json.append("}");
		}
		else {
			if (classe.toString().contains("List") || classe.toString().contains("Array")
					|| classe.toString().contains("Collection")) {
				json.append("[");
				Collection lista = (Collection) object;
				for (Object registro : lista) {
					json.append(gerarJsonCustom(registro));
				}
				json.append("]");
			}
			else {
				json.append(object);
			}
		}
		if (json.toString().equals("{}")) {
			return "";
		}
		return json.toString();
	}

	/**
	 * @author Rodrigo.Matos
	 * @param campo - Campo que será verificado
	 * @return boolean Verifica se o campo (field) deve ser ignorado (custom).</br>
	 * Caso seja necessário ignorar um campo, deve-se adicionar neste metodo.
	 */
	public static boolean jsonCustomIgnoreField(Field campo) {
		return campo.getType().isEnum();
	}

	/**
	 * @author Rodrigo.Matos
	 * @param campo - Campo que será verificado
	 * @return boolean Verifica se o campo (field) possui as anotações que devem ser
	 * ignoradas.</br>
	 * Caso seja necessário ignorar uma anotação, deve-se adicionar neste metodo.
	 */
	public static boolean jsonCustomIgnoreAnnotation(Field campo) {
		return false;
		// return campo.isAnnotationPresent(Transient.class) ||
		// campo.isAnnotationPresent(JsonIgnore.class)
		// || campo.isAnnotationPresent(Expose.class);
	}

	/**
	 * @author Rodrigo.Matos
	 * @param campo - Campo que será verificado
	 * @return boolean Verifica se o campo (field) possui os atributos com o nome que
	 * devem ser ignorados.</br>
	 * Caso seja necessário ignorar um atributo, deve-se adicionar neste metodo.
	 */
	public static boolean jsonCustomIgnoreAttribute(Field campo) {
		String name = campo.getName().toLowerCase();
		return name.equals("log") || name.equals("serialversionuid") || name.equals("handler")
				|| name.equals("hibernatelazyinitializer") || name.equals("_filter_signature")
				|| name.equals("_methods_");
	}

	/**
	 * @author Rodrigo.Matos
	 * @param campo - Campo que será verificado
	 * @return boolean Verifica se a classe deve ser ignorada.</br>
	 * Caso seja necessário ignorar uma classe, deve-se adicionar neste metodo.
	 */
	public static boolean jsonCustomIgnoreClass(Class<?> classe) {
		return false;
		// return classe.equals(ValueChangeAuditingEntityAb.class) ||
		// classe.equals(ValueChangeAuditing.class)
		// || classe.equals(ValueChangeAuditingEntity.class) ||
		// classe.equals(ch.qos.logback.classic.Logger.class)
		// || (classe.getInterfaces() != null
		// && Arrays.asList(classe.getInterfaces()).contains(ValueChangeAuditing.class));
	}

	/**
	 * @author Rodrigo.Matos
	 * @param path - Path da classe (getClass().toString)
	 * @return boolean O json custom só será aplicada a entidades do projeto que estejam
	 * nesse caminho, caso não esteja, será utilizada o padrão (como se fosse um
	 * atributo simples).
	 */
	private static boolean jsonPackageEntity(String path) {
		return path.contains("br.com.aegea.gss");
	}

	/**
	 * @author Rodrigo.Matos
	 * @param campo - Campo que será construido o json customizado.
	 * @param object - Object que será utilizado para gerar o json custom.
	 * @return String - Retorna o json custom do campo.
	 */
	private static String construirJsonCustom(Field campo, Object object) {

		try {
			StringBuilder json = new StringBuilder();
			String nomeAtributo = campo.getName();
			Object tipoAtributo = campo.getType();
			Object valorAtributo = campo.get(object);
			String temp = null;
			if (valorAtributo != null) {
				if (jsonPackageEntity(tipoAtributo.toString())) {
					temp = gerarJsonCustom(valorAtributo);
					if (!StringUtils.isEmpty(temp)) {
						json.append(formatarAtributoJsonCustom(nomeAtributo)).append(temp);
					}
				}
				else if (tipoAtributo.toString().contains("List") || tipoAtributo.toString().contains("Array")
						|| tipoAtributo.toString().contains("Collection")) {
					json.append(formatarAtributoJsonCustom(nomeAtributo)).append("[");
					int index = 1;
					int qtdReg = ((List) valorAtributo).size();
					for (Object reg : (List) valorAtributo) {
						temp = gerarJsonCustom(reg);
						if (!StringUtils.isEmpty(temp)) {
							if (index <= qtdReg && index != 1) {
								json.append(",");
							}
							if (jsonPackageEntity(reg.getClass().toString())) {
								json.append("{").append(temp).append("}");
							}
							else {
								json.append(temp);
							}
						}
						index++;
					}
					json.append(nomeAtributo).append("]");
				}
				else {
					if (tipoAtributo.toString().contains("String")
							&& (valorAtributo == null || StringUtils.isEmpty(valorAtributo.toString()))) {
						return null;
					}
					json.append(formatarAtributoJsonCustom(nomeAtributo));
					if (tipoAtributo.toString().contains("String")) {
						json.append("\"").append(valorAtributo.toString().replace("\"", "\\\"")).append("\"");
					}
					else {
						json.append(valorAtributo);

					}
				}
			}
			return json.toString();
		}
		catch (Exception e) {
			return null;
		}
	}

	/**
	 * @author Rodrigo.Matos
	 * @param atributo - Atributo que será formatado para gerar o json custom
	 * @return String - Retorna o texto do atributo formatado no padrão do json custom.
	 */
	private static String formatarAtributoJsonCustom(String atributo) {
		return new StringBuilder().append("\"").append(atributo).append("\"").append(":").toString();
	}

}
