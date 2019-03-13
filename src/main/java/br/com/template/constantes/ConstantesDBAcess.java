package br.com.template.constantes;

import br.com.template.model.BancoDadosVO;

/**
 * @author romatos
 * @version 1.0
 */

public final class ConstantesDBAcess {

	private ConstantesDBAcess() {
	}

	public static final BancoDadosVO BANCO_1 = new BancoDadosVO
			( "jdbc:oracle:thin:@URL_SERVIDOR:PORTA/SID"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			, true);

	public static final BancoDadosVO BANCO_2 = new BancoDadosVO
			( "jdbc:oracle:thin:@URL_SERVIDOR:PORTA/SID"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			, true);
}
