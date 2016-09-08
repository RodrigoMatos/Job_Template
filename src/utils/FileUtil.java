package utils;

import java.io.BufferedWriter;
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
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;

import com.eas.compression.CompressionException;
import com.eas.compression.CompressionHandler;
import com.eas.compression.GZipHandler;

public class FileUtil {

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
	
	public static File criarArquivo(File file) throws Exception {

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
		criarDirs(file);
	}

	public static void criarDirs(File file) {
		file.mkdirs();
		try {
			FileUtils.forceMkdir(file);
		} catch (IOException e) {
			LogUtil.Error("ERRO AO CRIAR DIRET�RIOS (" + file.getAbsolutePath() + ")" + e.getMessage());
		}
	}

	public static byte[] compactarArquivosZip(List<File> arquivos, String dirArquivo) throws Exception {
		return compactarArquivosZip(arquivos, new File(dirArquivo));
	}
	
	public static byte[] compactarArquivosZip(List<File> arquivos, File arquivoFinal) throws Exception {

		byte[] buf = new byte[1024];
		byte[] stream = null;
		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arquivoFinal));
			for (File arquivo : arquivos) {
				FileInputStream in = new FileInputStream(arquivo);
				out.putNextEntry(new ZipEntry(arquivo.getName()));
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				out.closeEntry();
				in.close();
			}
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

	public static void descompactarArquivo(String dirArquivoZIP) throws ZipException, IOException {
		descompactarArquivo(new File(dirArquivoZIP));
	}

	public static void descompactarArquivo(File arquivoZIP) throws ZipException, IOException {

		ZipFile zipFile = new ZipFile(arquivoZIP);
		Enumeration entries;
		try {
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					LogUtil.Info("DESCOMPACTANDO DIRET�RIO: " + entry.getName());
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

	public static void descompactarArquivoPara(File arquivoZIP, String diretorioPara) throws ZipException, IOException {
		descompactarArquivoPara(arquivoZIP, new File(diretorioPara));
	}

	public static void descompactarArquivoPara(File arquivoZIP, File diretorioPara) throws ZipException, IOException {
		
		if (diretorioPara == null || diretorioPara.isFile()) {
			throw new IOException("Caminho informado invalido, informe um diret�rio.");
		}
		criarDirs(diretorioPara);

		ZipFile zipFile = new ZipFile(arquivoZIP);
		Enumeration entries;
		try {
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				if (entry.isDirectory()) {
					LogUtil.Info("DESCOMPACTANDO DIRET�RIO: " + entry.getName());
					criarDirs(diretorioPara.getAbsoluteFile() + entry.getName());
				} else {
					LogUtil.Info("DESCOMPACTANDO ARQUIVO: " + entry.getName());
					copyInputStream(zipFile.getInputStream(entry), new FileOutputStream(new File(diretorioPara.getAbsoluteFile() + entry.getName())));
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

	public static byte[] getBytes(String arquivo) throws IOException {
		return getBytes(new File(arquivo));
	}
	public static byte[] getBytes(File arquivo) throws IOException {

		int len = (int) arquivo.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = null;
		try {
			inFile = new FileInputStream(arquivo);
			inFile.read(sendBuf, 0, len);
			inFile.close();
		} catch (FileNotFoundException fnfex) {
			throw fnfex;
		} catch (IOException ioex) {
			throw ioex;
		}
		return sendBuf;
	}

	public static FileOutputStream criarArquivoParaEscrita(String dirArquivo) throws FileNotFoundException {
		return criarArquivoParaEscrita(new File(dirArquivo));
	}

	public static FileOutputStream criarArquivoParaEscrita(File arquivo) throws FileNotFoundException {
		FileUtil.criarDirs(arquivo.getParentFile());
		FileOutputStream fileOut = new FileOutputStream(arquivo);
		return fileOut;
	}
}
