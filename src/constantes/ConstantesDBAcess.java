package constantes;

import model.BancoDadosVO;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ConstantesDBAcess {

	public static final BancoDadosVO BANCOSCIENCEBCV = new BancoDadosVO
			( "jdbc:oracle:thin:@10.240.2.12:1521:SMAPBCV"
			, "usr_science"
			, "PKNGR"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			, true);

	public static final BancoDadosVO BANCOSCIENCEPROD = new BancoDadosVO
			( "jdbc:oracle:thin:@URL_SERVIDOR:PORTA/SID"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			, true);
}
