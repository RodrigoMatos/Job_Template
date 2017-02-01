package bancoDeDados.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.dbutils.DbUtils;
import bancoDeDados.ConexaoPool;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class DAO implements Serializable {

	private static final long serialVersionUID = -5030758658343312612L;

	/**
	 * @author romatos
	 * @param tabela - Nome da tabela que irá apagar os registros.
	 * @param chave - Chave de conexão do pool.
	 * @throws Exception
	 */
	public static void deletarRegistroTabela(String tabela, String chave) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ").append(tabela);

		try {
			conn = ConexaoPool.getConnection(chave);
			stmt = conn.prepareStatement(sql.toString());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);
		}
	}

	/**
	 * @author romatos
	 * @param consulta - Consulta que será executada.
	 * @param chaveConexao - Chave de conexão do pool.
	 * @return Retorna uma lista de map, cada linha da lista representa uma linha da consulta e cada item do map representa a coluna (chave do map é o nome da coluna da consulta).
	 * @throws Exception
	 */
	public static List<LinkedHashMap<String, Object>> realizarConsultaGenerica(String consulta, String chaveConexao) throws Exception {

		List<LinkedHashMap<String, Object>> resultadoConsulta = new ArrayList<LinkedHashMap<String, Object>>();
		Connection conn = null;
		PreparedStatement stmt = null;
		StringBuilder sql = new StringBuilder(consulta);
		ResultSet rs = null;
		Integer index = 1;
		Integer qtdColumn = null;
		LinkedHashMap<String, Object> registro;
		String coluna = "";
		try {
			conn = ConexaoPool.getConnection(chaveConexao);
			stmt = conn.prepareStatement(sql.toString());
			stmt.setFetchSize(2000);
			rs = stmt.executeQuery();
			while (rs.next()) {
				registro = new LinkedHashMap<String, Object>();
				//PREPARAR CABECALHO (NOME DAS COLUNAS COMO PRIMEIRO REGISTRO.
				if (qtdColumn == null) {
					qtdColumn = rs.getMetaData().getColumnCount();
					for (int i = 0; i < qtdColumn; i++) {
						coluna = rs.getMetaData().getColumnName(index++);
						registro.put(coluna, coluna);
					}
					resultadoConsulta.add(registro);//ADICIONANDO O NOME DAS COLUNAS COMO PRIMEIRO REGISTRO
				}
				index = 1;
				//PREPARAR REGISTRO DA CONSULTA
				for (int i = 0; i < qtdColumn; i++) {
					coluna = rs.getMetaData().getColumnName(index++);
					registro.put(coluna, rs.getObject(coluna));
				}
				resultadoConsulta.add(registro);//ADICIONAR REGISTRO DA CONSULTA
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtils.closeQuietly(conn,stmt,rs);
		}
		return resultadoConsulta;
	}

	/**
	 * @author romatos
	 * @param arquivo - Arquivo que será exportado.
	 * @param consulta - Consulta que será executada.
	 * @param chaveConexao - Chave de conexão do pool.
	 * @param separador - Separador que será utilizado entre as colunas.
	 * @param prefixo - Prefixo que será adicionado antes dos registros de cada coluna.
	 * @param sufixo - Sufixo que será adicionado antes dos registros de cada coluna.
	 * @param escreverCabecalho - Escreve o cabeçalho de acordo com o nome das colunas da consulta.
	 * @return Retorna quantidade de linhas exportadas.
	 * @throws Exception
	 */
	public static Integer exportarConsultaParaCsv(File arquivo, String consulta, String chaveConexao, String separador, String prefixo, String sufixo, boolean escreverCabecalho) throws Exception {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FileWriter fstream = new FileWriter(arquivo);
		BufferedWriter out = new BufferedWriter(fstream);
		Integer qtdColumn = null;
		Boolean primeiraVez;
		Integer linhasExportadas = 0;
		try {
			conn = ConexaoPool.getConnection(chaveConexao);
			stmt = conn.prepareStatement(consulta);
			rs = stmt.executeQuery();
			rs.setFetchSize(500);

			if (escreverCabecalho) {
				// CRIAR CABEÇALHO
				primeiraVez = true;
				qtdColumn = rs.getMetaData().getColumnCount();
				for (int i = 1; i <= qtdColumn; i++) {
					if (primeiraVez) {
						out.write(rs.getMetaData().getColumnName(i));
						primeiraVez = false;
					} else {
						out.write(separador);
						out.write(rs.getMetaData().getColumnName(i));
					}
				}
				out.newLine();
				// FIM CABEÇALHO
			}

			while (rs.next()) {
				primeiraVez = true;
				// ESCREVER CONTEUDO
				for (int i = 1; i <= qtdColumn; i++){
					if (primeiraVez) {
						if (rs.getObject(i) == null)
							out.write(prefixo + "null" + sufixo);
						else
							out.write(prefixo + rs.getObject(i).toString() + sufixo);
						primeiraVez = false;
					} else {
						out.write(separador);
						if (rs.getObject(i) == null)
							out.write(prefixo + "null" + sufixo);
						else
							out.write(prefixo + rs.getObject(i).toString() + sufixo);
					}
				}
				out.newLine();
				linhasExportadas++;
			}
			// FIM ESCREVER CONTEUDO
			out.close();
			fstream.close();
		} catch (Exception e) {
			throw e;
		} finally {
			DbUtils.closeQuietly(conn, stmt, rs);
		}
		return linhasExportadas;
	}

}
