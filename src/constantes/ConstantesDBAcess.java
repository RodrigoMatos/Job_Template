package constantes;

import model.BancoDadosVO;

public class ConstantesDBAcess {

	public static final BancoDadosVO BANCOSCIENCEBCV = new BancoDadosVO
			("jdbc:oracle:thin:@URL_SERVIDOR:PORTA/SID"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			,true);
	
	public static final BancoDadosVO BANCOSCIENCEPROD = new BancoDadosVO(
			"jdbc:oracle:thin:@URL_SERVIDOR:PORTA/SID"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			,1
			,1
			,true);
}
