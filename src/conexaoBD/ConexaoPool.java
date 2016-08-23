package conexaoBD;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import model.BancoDadosVO;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * @author romatos
 * @version 1.0
 */

public class ConexaoPool {
	
	private static Map<String, BoneCP> connectionPool;
	
	/**
	 * Método que finaliza um pool de conexão.
	 * @param String - Chave da conexão.
	 * @author romatos
	 */
	public static void endDataSource(String chave){
		connectionPool.get(chave).close();
	}

	/**
	 * Retorna uma conexão do pool referente a chave.
	 * @author romatos
	 * @param String - Chave da conexão que deseja obter.
	 * @return Connection - Retorna uma conexão referente ao parâmetro 'chave'.
	 */
	public static Connection getConnection(String chave) throws Exception {
		if (connectionPool.get(chave) != null) {
			return connectionPool.get(chave).getConnection();
		} else {
			throw new Exception("POOL NÃO INICIALIZADO (" + chave + ").");
		}
	}
	
	/**
	 * Criar um pool de conexão.
	 * @author romatos
	 * @param String - Chave da conexão que deseja iniciar.
	 * @param BancoDadosVO - Configurações para criar pool.
	 */
	public static void initDataSource(String chave, BancoDadosVO banco) throws Exception {
		try {
			Class.forName(banco.getClassForName());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		try {
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(banco.getUrlBancoDados());
			config.setUsername(banco.getUsuario());
			config.setPassword(banco.getSenha());

            config.setMinConnectionsPerPartition(banco.getQtdConexoes());
            config.setMaxConnectionsPerPartition(banco.getQtdConexoes());
            config.setPartitionCount(banco.getQtdParticoes());

            config.setDefaultAutoCommit(banco.isAutoCommit());
            
			if (connectionPool == null) {
				connectionPool = new HashMap<String, BoneCP>();
			}
			connectionPool.put(chave, new BoneCP(config));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	
}