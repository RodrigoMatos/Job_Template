package utils;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileUtil {

	public static final String PATH_CRIACAO_ARQUIVOS = "/opt/processum/servicos/consolidacao/result/";
	
	public static byte[] finallyOutPutStream(FileOutputStream fos,File file) throws Exception{
		byte[] stream = null;
		if(fos != null){
   			fos.close();
   			InputStream is = new FileInputStream(file);
   			stream = new byte[(int)file.length()];        
   		    int offset = 0;
   		    int numRead = 0;
   		    while (offset < stream.length && (numRead= is.read(stream, offset, stream.length-offset)) >= 0) {
   		        offset += numRead;
   		    }  
   		    is.close();	   		    
		}
		return stream;
	}
	
	public static byte[] converteInputStreamToArrayByte(InputStream iStream) throws Exception{
		int i = 0;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
    	while ((i = iStream.read()) != -1) {
    		baos.write(i);
    	}    	
        return baos.toByteArray();
	}
	
	public static String getNomeArquivoTemp(String extensao){
    	StringBuilder nomeArquivo = new StringBuilder(PATH_CRIACAO_ARQUIVOS+"arquivoTemporario-");
    	nomeArquivo.append(Long.valueOf(new Date().getTime()).toString());
    	nomeArquivo.append(Integer.valueOf(nomeArquivo.hashCode()).toString());
    	nomeArquivo.append(extensao);
    	return nomeArquivo.toString();
    }
	
	public static void bzip2Compress(File source) throws Exception {

		BZip2CompressorOutputStream output = null;
		FileInputStream input = null;
		try {
			LogUtil.Info("INICIANDO COMPRESSAO DO ARQUIVO PARA .bz2 ...");
			File destination = new File(source.toString() + ".bz2");
			output = new BZip2CompressorOutputStream(new FileOutputStream(destination));
			input = new FileInputStream(source);
			IOUtils.copy(input, output);
	        LogUtil.Info("COMPRESSAO DO ARQUIVO REALIZADA COM SUCESSO.");
		} catch (IOException e) {
			LogUtil.Error("AO COMPRIMIR O ARQUIVO: " + e.getMessage());
			throw e;
		} finally {
			if (input != null)
				input.close();
			if (output != null)
				output.close();
		}
	}

	
	public static FileWriter prepararArquivoParaEscrita(File file) throws IOException {

		try {
			LogUtil.Info("PREPARANDO ARQUIVO PARA ESCRITA ...");
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			LogUtil.Info("ARQUIVO PRONTO PARA ESCRITA. ");
			return fw;
		} catch (IOException e) {
			LogUtil.Error("AO PREPARAR ARQUIVO PARA ESCRITA: " + e.getMessage());
			throw e;
		}
	}
	
	public static BufferedWriter escreverNoArquivo(FileWriter fw, List<String> lines) throws IOException {

		try {
			BufferedWriter bw = new BufferedWriter(fw);
			LogUtil.Info("ESCREVENDO NO ARQUIVO ...");
			int linhas = 0;
			for (String line : lines) {
				bw.write(line);
				bw.newLine();
				linhas++;
			}
			LogUtil.Info("CONCLUIDO PREENCHIMENTO DO ARQUIVO (" + linhas + ")." );
			return bw;
		} catch (IOException e) {
			LogUtil.Error("ERRO AO ESCREVER NO ARQUIVO: " + e.getMessage());
			throw e;
		}
	}
	
	public static void fecharArquivo(BufferedWriter bw) throws IOException {

		try {
			LogUtil.Info("FINALIZANDO O ARQUIVO ...");
			bw.close();
			LogUtil.Info("ARQUIVO FINALIZADO COM SUCESSO.");
		} catch (IOException e) {
			LogUtil.Error("AO FINALIZAR O ARQUIVO: " + e.getMessage());
			throw e;
		}
	}

	public static File criarArquivo(String arquivo) throws Exception {

		File file = new File(arquivo);
		try {
			if (!file.exists()) {
				LogUtil.Info("CRIANDO NOVO ARQUIVO ... ");
				file.createNewFile();
				LogUtil.Info("ARQUIVO CRIADO COM SUCESSO. ");
			}
			return file;
		} catch (Exception e) {
			LogUtil.Error("AO CRIAR ARQUIVO: " + e.getMessage());
			throw e;
		}
	}
	
	public static void criarDirs(String diretorio) {
		File file = new File(diretorio);
		file.mkdirs();
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e) {
			LogUtil.Error("ERRO AO CRIAR DIRETÓRIOS (" + diretorio + ")" + e.getMessage());
		}
	}
}
