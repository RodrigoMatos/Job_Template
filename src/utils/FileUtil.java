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

/**
 * @author romatos
 * @version 1.0
 */

public abstract class FileUtil {

	public static byte[] converteInputStreamToArrayByte(InputStream iStream) throws Exception{
		int i = 0;        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();        
    	while ((i = iStream.read()) != -1) {
    		baos.write(i);
    	}    	
        return baos.toByteArray();
	}

	public static FileWriter prepararArquivoParaEscrita(File file, boolean manterConteudo) throws IOException {
		FileWriter fw = new FileWriter(file.getAbsoluteFile(), manterConteudo);
		return fw;
	}

	public static BufferedWriter escreverNoArquivo(FileWriter fw, List<String> lines) throws IOException {

		BufferedWriter bw = new BufferedWriter(fw);
		int linhas = 0;
		int tamanho = lines.size();
		for (String line : lines) {
			bw.write(line);
			linhas++;
			if (linhas < tamanho) {
				bw.newLine();
			}
		}
		return bw;
	}

	public static BufferedWriter novaLinha(FileWriter fw) throws IOException {
		BufferedWriter bw = new BufferedWriter(fw);
		bw.newLine();
		return bw;
	}

	public static BufferedWriter escreverNoArquivo(FileWriter fw, String linha) throws IOException {
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(linha);
		return bw;
	}

	public static void exportarArquivo(String arquivo, List<String> linhas) throws IOException {
		exportarArquivo(new File(arquivo), linhas);
	}

	public static void exportarArquivo(File arquivo, List<String> linhas) throws IOException {

		FileWriter conteudo = prepararArquivoParaEscrita(arquivo, false);
		for (String linha : linhas) {
			conteudo.write(linha);
			conteudo.write(System.getProperty("line.separator"));
		}
		conteudo.close();
	}

	public static void fecharArquivo(BufferedWriter bw) throws IOException {
		bw.close();
	}

	public static File criarArquivo(String arquivo) throws Exception {
		return criarArquivo(new File(arquivo));
	}

	public static File criarArquivo(File file) throws Exception {

		if (!file.exists()) {
			criarDirs(file.getParentFile());
			file.createNewFile();
		}
		return file;
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
			LogUtil.Error("ERRO AO CRIAR DIRETÓRIOS (" + file.getAbsolutePath() + ")" + e.getMessage());
		}
	}

	public static byte[] compactarArquivosZip(List<File> arquivos, String dirArquivo) throws Exception {
		return compactarArquivosZip(arquivos, new File(dirArquivo));
	}

	public static byte[] compactarArquivosZip(List<File> arquivos, File arquivoFinal) throws Exception {

		byte[] buf = new byte[1024];
		byte[] stream = null;
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
		return stream;
	}

	public static void descompactarArquivo(String dirArquivoZIP) throws ZipException, IOException {
		descompactarArquivo(new File(dirArquivoZIP));
	}

	public static void descompactarArquivo(File arquivoZIP) throws ZipException, IOException {

		ZipFile zipFile = new ZipFile(arquivoZIP);
		Enumeration entries = zipFile.entries();
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
	}

	public static void descompactarArquivoPara(File arquivoZIP, String diretorioPara) throws ZipException, IOException {
		descompactarArquivoPara(arquivoZIP, new File(diretorioPara));
	}

	public static void descompactarArquivoPara(File arquivoZIP, File diretorioPara) throws ZipException, IOException {

		if (diretorioPara == null || diretorioPara.isFile()) {
			throw new IOException("Caminho informado invalido, informe um diretório.");
		}
		criarDirs(diretorioPara);

		ZipFile zipFile = new ZipFile(arquivoZIP);
		Enumeration entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				LogUtil.Info("DESCOMPACTANDO DIRETÓRIO: " + entry.getName());
				criarDirs(diretorioPara.getAbsoluteFile() + entry.getName());
			} else {
				LogUtil.Info("DESCOMPACTANDO ARQUIVO: " + entry.getName());
				copyInputStream(zipFile.getInputStream(entry), new FileOutputStream(new File(diretorioPara.getAbsoluteFile() + entry.getName())));
			}
		}
		zipFile.close();
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

		CompressionHandler gz = new GZipHandler();
		File sourceFile = new File(dirArquivo);
		File outFile = gz.compress(new File[] { sourceFile }, null);
		return outFile.getAbsolutePath();
	}

	public static byte[] getBytes(String arquivo) throws IOException {
		return getBytes(new File(arquivo));
	}

	public static byte[] getBytes(File arquivo) throws IOException {

		int len = (int) arquivo.length();
		byte[] sendBuf = new byte[len];
		FileInputStream inFile = new FileInputStream(arquivo);
		inFile.read(sendBuf, 0, len);
		inFile.close();
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

	public static void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}
