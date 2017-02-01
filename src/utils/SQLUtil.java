package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class SQLUtil {

	public static String clobParaString(Clob clob) {
		String retorno = null;
		if (clob != null) {
			try {
				retorno = clob.getSubString(1, (int) clob.length());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	public static void addDouble(PreparedStatement stmt, int indice, Double valor) throws SQLException {
		if (valor != null) {
			stmt.setDouble(indice, valor);
		} else {
			stmt.setNull(indice, Types.NUMERIC);
		}
	}

	public static void addLong(PreparedStatement stmt, int indice, Long valor) throws SQLException {
		if (valor != null) {
			stmt.setLong(indice, valor);
		} else {
			stmt.setNull(indice, Types.BIGINT);
		}
	}

	public static void addInt(PreparedStatement stmt, int indice, Integer valor) throws SQLException {
		if (valor != null) {
			stmt.setInt(indice, valor);
		} else {
			stmt.setNull(indice, Types.INTEGER);
		}
	}

	public static void addDate(PreparedStatement stmt, int indice, Date valor) throws SQLException {
		if (valor != null) {
			stmt.setTimestamp(indice, new Timestamp(valor.getTime()));
		} else {
			stmt.setNull(indice, Types.TIMESTAMP);
		}
	}

	public static void addClob(PreparedStatement stmt, int indice, String valor) throws SQLException {
		if (valor != null) {
			stmt.setString(indice, valor);
		} else {
			stmt.setNull(indice, Types.CLOB);
		}
	}

	public static void addBlob(PreparedStatement stmt, int indice, File valor) throws SQLException, IOException {
		if (valor != null) {
			FileInputStream fis = new FileInputStream(valor);
			int lenght = (int) valor.length();
			stmt.setBinaryStream(indice, fis, lenght);
			fis.close();
		} else {
			stmt.setNull(indice, Types.BLOB);
		}
	}

	/**
	 * @author romatos
	 * @param classe - Classe do objeto que será preenchido e retornado.
	 * @param rs - ResultSet que contém os dados da consulta.s
	 * @return Retorna o objeto preenchido conforme dados do ResultSet.
	 * Preenche os atribudos do objeto utilizando os metodos sets de acordo com os dados existente no ResultSet.
	 * As colunas devem possuir os mesmos nomes dos atributos do objeto que será preenchido.
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws SQLException
	 */
	public static Object prepararVO(Class<?> classe, ResultSet rs) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException {

		Object objeto = null;
		if (rs != null && classe != null) {
			Constructor<?> construtor = classe.getConstructor();
			objeto = construtor.newInstance();
			Method[] metodos = objeto.getClass().getMethods();

			int qtdColumn = rs.getMetaData().getColumnCount();

			for (int i = 0; i < qtdColumn; i++) {
				setValueVO(metodos, objeto, rs.getMetaData().getColumnName(i+1), rs.getObject(i+1));
			}
		}
		return objeto;
	}

	private static void setValueVO(Method[] metodos, Object objeto, String name, Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		if ("".equals(name)) {
			return;
		}
		String nomeMetodo;
		nomeMetodo = "set" + name.subSequence(0, 1).toString().toUpperCase(); //coloca 1º letra em upper
		if (name.length() > 1) {
			nomeMetodo = nomeMetodo + name.substring(1, name.length()); // coloca o resto do nome do metodo caso seja exista
		}
		for (Method metodo : metodos) {// procura o metodo que deve executar
			if (metodo.getName().equals(nomeMetodo)) {
				metodo.invoke(objeto, params); // executa o metodo desejado
				break;
			}
		}
	}

}
