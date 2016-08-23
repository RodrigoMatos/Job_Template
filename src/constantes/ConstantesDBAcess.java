package constantes;

import model.BancoDadosVO;

public class ConstantesDBAcess {

	public static final BancoDadosVO BANCOSCIENCEBCV = new BancoDadosVO
			("jdbc:oracle:thin:@10.240.2.12:1521/SMAPPRD"
			, "science"
			, "vivobaba2012"
			, "oracle.jdbc.driver.OracleDriver"
			, 1
			, 1
			,true);
	
	// BASE DE PRODUÇÃO
	public static final BancoDadosVO BANCOSCIENCEPROD = new BancoDadosVO(
//			"jdbc:oracle:thin:@10.240.44.11:1521/SMAPPRD"
			"jdbc:oracle:thin:@SMAPPRD-clu-scan:1521/SMAPPRD"
			, "usr_science"
			, "PKNGR"
			, "oracle.jdbc.driver.OracleDriver"
			,1
			,1
			,true);
}
