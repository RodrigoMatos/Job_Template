package utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtil {

	public static void closeResult(ResultSet rs) throws SQLException {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException se) {
			throw new SQLException("Erro ao fechar ResultSet.", se);
		}
	}

	public static void closeStatement(Statement stmt) throws SQLException {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se) {
			throw new SQLException("Erro ao fechar Statement.", se);
		}
	}

	public static void closeCallableStatement(CallableStatement cstmt)
			throws SQLException {
		try {
			if (cstmt != null)
				cstmt.close();
		} catch (SQLException se) {
			throw new SQLException("Erro ao fechar CallableStatement.", se);
		}
	}

	public static void closeConnection(Connection conn) throws SQLException {
		try {
			if ((conn != null) && (!conn.isClosed()))
				conn.close();
		} catch (SQLException se) {
			throw new SQLException("Erro ao fechar Connection.", se);
		}
	}
}
