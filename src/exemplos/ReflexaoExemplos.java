package exemplos;

import model.BancoDadosVO;
import reflexao.ReflexaoUtils;
import bancoDeDados.ConexaoPool;

/**
 * @author romatos
 * @version 1.0
 */

public class ReflexaoExemplos {

	public static void criarPoolConexao(String chave, BancoDadosVO banco) throws Exception {
		ReflexaoUtils.executarMetodo(ConexaoPool.class, null, "initDataSource", chave, banco);
	}

	public static void finalizarPoolConexao(String chave) throws Exception {
		ReflexaoUtils.executarMetodo(ConexaoPool.class, null, "endDataSource", chave);
	}

}
