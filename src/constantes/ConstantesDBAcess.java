package constantes;

import model.BancoDadosVO;

public class ConstantesDBAcess {

	public static final BancoDadosVO BANCOSCIENCEBCV = new BancoDadosVO
			("url"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			,true);
	
	public static final BancoDadosVO BANCOSCIENCEPROD = new BancoDadosVO(
			"url"
			, "usuario"
			, "senha"
			, "oracle.jdbc.driver.OracleDriver"
			,1
			,1
			,true);
}
