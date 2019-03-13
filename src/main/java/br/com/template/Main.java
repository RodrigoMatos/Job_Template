package br.com.template;

import br.com.template.exemplos.ArquivoExcelExemplos;

public class Main {

	public static void main(String[] args) {

		/**
		 * Dados da ultima alteração.
		 * @author romatos
		 * @version 16/01/2017 
		 */
		
//		try {
//			String dirArquivo = "D:/testes/arquivoTeste.xls";
////			ConexaoPool.initDataSource("BCV", ConstantesDBAcess.BANCOSCIENCEBCV);
////			ExportacaoUtils.exportarConsultaParaArquivoExcel("Select 1, 'teobaldo', 18, 'zzz' from dual",  dirArquivo,"BCV");
//			FTPUtils sftp = new FTPUtils(new FtpVO("10.72.18.129", "wls11", "wls11"), TipoAcessoFTP.SFTP);
//			
//			sftp.conectar();
//			sftp.cd("/home2/weblogic11/JOBS_SCIENCE/");
//			sftp.upload(dirArquivo, "/home2/weblogic11/JOBS_SCIENCE/");
//			
//			sftp.download("arquivoTeste.xls", "D:/testes/arquivoTesteDownload.xls");
//			sftp.desconectar();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
////			ConexaoPool.endDataSource("BCV");
//		}

//		FtpVO ftp = new FtpVO("server", "usuario", "senha");
//		Object servidor = null;
//		try {
//			servidor = ExecutorMetodo.executarMetodo(ftp, "getServidor");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (servidor == null) {
//			servidor = "null";
//		}
//		System.out.println(servidor.toString());

//		ArquivoTextoExemplos.escreverAquivoTxt();
		ArquivoExcelExemplos.alterarExcel();
//		ArquivoExcelExemplos.exportarLista();
//		ArquivoExcelExemplos.exportarConsulta();

		System.exit(0);
	}

}
