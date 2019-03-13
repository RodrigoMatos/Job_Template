package br.com.template.exemplos;

import br.com.template.bancoDeDados.ConexaoPool;
import br.com.template.model.BancoDadosVO;
import br.com.template.reflexao.ReflexaoUtils;

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
