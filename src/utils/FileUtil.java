package utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

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
	
	public static byte[] decompressArrayWithoutDeflater(byte[] array) throws Exception{
		ByteArrayInputStream byteInputStream = null;
		ZipInputStream zis = null;
		ByteArrayOutputStream bos = null;
		try {
			byteInputStream = new ByteArrayInputStream(array);
			zis = new ZipInputStream(byteInputStream);
			bos = new ByteArrayOutputStream(array.length);
			while((zis.getNextEntry()) != null) {
				int count;
				byte data[] = new byte[2048];  
				while ((count = zis.read(data, 0, 2048))!= -1) {
					bos.write(data, 0, count);
				}	           
			}
			zis.close();
			bos.flush();
			bos.close();
		} catch(Exception e) {
			throw e;
		}	     
		return bos.toByteArray();
	}
	
	public static byte[] compressListaArquivos(List<File> arquivos) throws Exception{
        
	    // Create a buffer for reading the files 
	    byte[] buf = new byte[1024]; 
	    byte[] stream = null;
	    File arquivoFinal = new File(FileUtil.getNomeArquivoTemp(".zip"));
	    try { 
	    	// Create the ZIP file 
	    	ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arquivoFinal)); 
	    	// Compress the files 
	    	for (File arquivo: arquivos) { 
	    		FileInputStream in = new FileInputStream(arquivo); 
	    		// Add ZIP entry to output stream. 
	    		out.putNextEntry(new ZipEntry(arquivo.getName())); 
	    		// Transfer bytes from the file to the ZIP file 
	    		int len; 
	    		while ((len = in.read(buf)) > 0) { 
	    			out.write(buf, 0, len); 
	    		} 
	    		// Complete the entry 
	    		out.closeEntry(); 
	    		in.close(); 
	    		arquivo.delete();
	    	} 
	    	// Complete the ZIP file 
	    	out.close(); 
	    	InputStream is = new FileInputStream(arquivoFinal);
   			stream = new byte[(int)arquivoFinal.length()];        
   		    int offset = 0;
   		    int numRead = 0;
   		    while (offset < stream.length && (numRead= is.read(stream, offset, stream.length-offset)) >= 0) {
   		        offset += numRead;
   		    }  
   		    is.close();  
   		    arquivoFinal.delete();
	    }catch (IOException e) { 
	    	throw e;
	    } 	 
	    return stream;
    }

	public static byte[] decompressArray(byte[] array, boolean retornaNullErro) throws Exception {	
		
	    Inflater decompressor;
	    ByteArrayOutputStream bos;

        try{
        	
        	decompressor = new Inflater();
            decompressor.setInput(array);

            bos = new ByteArrayOutputStream(array.length);
        	
            byte[] buf = new byte[1024];
            while (!decompressor.finished()){
                int count = decompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
            bos.close();
            
            return bos.toByteArray();
            
        } catch (IOException e){
            throw e;
        } catch (DataFormatException e){
        	if(retornaNullErro){
        		return null;
        	} else {
        		throw e;	
        	}        	           
        }        
    }
}
