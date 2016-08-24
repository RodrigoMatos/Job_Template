package main;

import conexaoBD.ConexaoPool;
import constantes.ConstantesDBAcess;

public class Main {

	public static void main(String[] args) {
		
		/* INICIAR POOL BANCO */
		try {
			ConexaoPool.initDataSource("chave", ConstantesDBAcess.BANCOSCIENCEBCV);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		/* FINALIZANDO POOL */
		ConexaoPool.endDataSource("chave");
		
	}

}
