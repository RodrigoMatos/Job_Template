package bancoDeDados.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.commons.dbutils.DbUtils;

import bancoDeDados.ConexaoPool;

public abstract class DAO implements Serializable {
	
	private static final long serialVersionUID = -5030758658343312612L;

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
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(stmt);
		}
	}

}
