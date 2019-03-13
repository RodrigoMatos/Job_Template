package br.com.template.bancoDeDados;

import java.io.Serializable;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import br.com.template.model.BancoDadosVO;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ConexaoPool implements Serializable {

	private static final long serialVersionUID = -6239447368291477751L;
	private static Map<String, BoneCP> connectionPool;

	/**
	 * M�todo que finaliza um pool de conex�o.
	 * @param String - Chave da conex�o.
	 * @author romatos
	 */
	public static void endDataSource(String chave){
		connectionPool.get(chave).close();
	}

	/**
	 * Retorna uma conex�o do pool referente a chave.
	 * @author romatos
	 * @param String - Chave da conex�o que deseja obter.
	 * @return Connection - Retorna uma conex�o referente ao par�metro 'chave'.
	 */
	public static Connection getConnection(String chave) throws Exception {

		if (connectionPool.get(chave) != null) {
			return connectionPool.get(chave).getConnection();
		} else {
			throw new Exception("POOL N�O INICIALIZADO (" + chave + ").");
		}
	}

	/**
	 * Criar um pool de conex�o.
	 * @author romatos
	 * @param String - Chave da conex�o que deseja iniciar.
	 * @param BancoDadosVO - Configura��es para criar pool.
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