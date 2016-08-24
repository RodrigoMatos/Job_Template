package utils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.eas.compression.CompressionException;
import com.eas.compression.CompressionHandler;
import com.eas.compression.GZipHandler;

public class FileUtil {

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
	
	public static byte[] compactarArquivosZip(List<File> arquivos, String dirZip) throws Exception{

		// Create a buffer for reading the files
		byte[] buf = new byte[1024];
		byte[] stream = null;
		File arquivoFinal = new File(dirZip);
		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arquivoFinal));
			// Compress the files
			for (File arquivo : arquivos) {
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
			}
			// Complete the ZIP file
			out.close();
			InputStream is = new FileInputStream(arquivoFinal);
			stream = new byte[(int) arquivoFinal.length()];
			int offset = 0;
			int numRead = 0;
   		    while (offset < stream.length && (numRead= is.read(stream, offset, stream.length-offset)) >= 0) {
   		        offset += numRead;
			}
			is.close();
		} catch (IOException e) {
			throw e;
		}
		return stream;
	}
	
	public static void descompactarArquivo(File arquivoZIP) throws ZipException, IOException {

		ZipFile zipFile = new ZipFile(arquivoZIP);
		Enumeration entries;
		try {
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					LogUtil.Info("DESCOMPACTANDO DIRETÓRIO: " + entry.getName());
					criarDirs(arquivoZIP.getParentFile() + System.getProperty("file.separator") + entry.getName());
				} else {
					LogUtil.Info("DESCOMPACTANDO ARQUIVO: " + entry.getName());
					copyInputStream(zipFile.getInputStream(entry), new FileOutputStream(new File(arquivoZIP.getParentFile() + System.getProperty("file.separator") + entry.getName())));
				}
			}
			zipFile.close();
		} catch (IOException ioe) {
			LogUtil.Error("ERRO AO DESCOMPACTAR: " + ioe.getMessage());
			throw ioe;
		}
	}
	
	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	public static byte[] decompressArray(byte[] array, boolean retornaNullErro) throws Exception {	

		Inflater decompressor;
		ByteArrayOutputStream bos;
		try {
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
		} catch (IOException e) {
			throw e;
		} catch (DataFormatException e) {
			if (retornaNullErro) {
				return null;
			} else {
				throw e;
			}
		}
	}
	
	public static String compactarArquivoBZ(String dirArquivo) throws CompressionException {

		String caminho = null;
		try {
			CompressionHandler gz = new GZipHandler();
			File sourceFile = new File(dirArquivo);
			File outFile = gz.compress(new File[] { sourceFile }, null);
			caminho = outFile.getAbsolutePath();
		} catch (CompressionException e) {
			e.printStackTrace();
			throw e;
		}
		return caminho;
	}

	public static byte[] getBytes(File file) throws IOException {

		int len = (int) file.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(file);
			inFile.read(sendBuf, 0, len);
			inFile.close();
		} catch (FileNotFoundException fnfex) {
			throw fnfex;
		} catch (IOException ioex) {
			throw ioex;
		}
		return sendBuf;
	}
}
