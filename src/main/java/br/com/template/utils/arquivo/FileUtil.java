package br.com.template.utils.arquivo;

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
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import br.com.template.annotations.FieldFileConfig;
import br.com.template.annotations.FileConfig;
import br.com.template.reflexao.ReflexaoUtils;
import br.com.template.utils.DateUtil;
import br.com.template.utils.LogUtil;

/**
 * @author romatos
 * @version 1.0
 */

public final class FileUtil {

	private FileUtil() {
	}

	public static byte[] converteInputStreamToArrayByte(InputStream iStream) throws Exception {
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
			LogUtil.Error("ERRO AO CRIAR DIRET�RIOS (" + file.getAbsolutePath() + ")" + e.getMessage());
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
		while (offset < stream.length && (numRead = is.read(stream, offset, stream.length - offset)) >= 0) {
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
		Enumeration<?> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				LogUtil.Info("DESCOMPACTANDO DIRET�RIO: " + entry.getName());
				criarDirs(arquivoZIP.getParentFile() + System.getProperty("file.separator") + entry.getName());
			} else {
				LogUtil.Info("DESCOMPACTANDO ARQUIVO: " + entry.getName());
				copyInputStream(zipFile.getInputStream(entry), new FileOutputStream(
						new File(arquivoZIP.getParentFile() + System.getProperty("file.separator") + entry.getName())));
			}
		}
		zipFile.close();
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
		Enumeration<?> entries = zipFile.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.isDirectory()) {
				LogUtil.Info("DESCOMPACTANDO DIRET�RIO: " + entry.getName());
				criarDirs(diretorioPara.getAbsoluteFile() + entry.getName());
			} else {
				LogUtil.Info("DESCOMPACTANDO ARQUIVO: " + entry.getName());
				copyInputStream(zipFile.getInputStream(entry),
						new FileOutputStream(new File(diretorioPara.getAbsoluteFile() + entry.getName())));
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

	public static String gerarArquivoByGeneric(String[] headers, List<?> listArquivoEntity) throws Exception {

		if (listArquivoEntity != null && !listArquivoEntity.isEmpty()) {
			Class<?> classe = null;
			List<Field> fields = null;
			FieldFileConfig annotation = null;
			String value = null;
			StringBuilder conteudo = new StringBuilder();
			StringBuilder linha = null;
			Object itemTemp;
			boolean formatter;

			if (headers != null && headers.length > 0) {
				writerHeader(conteudo, headers);
			}

			DecimalFormat formatterNumber = (DecimalFormat) NumberFormat.getInstance(Locale.ROOT);
			DecimalFormatSymbols symbolsFormatter = formatterNumber.getDecimalFormatSymbols();
			symbolsFormatter.setGroupingSeparator('.');
			symbolsFormatter.setDecimalSeparator(',');
			formatterNumber.setDecimalFormatSymbols(symbolsFormatter);

			for (Object arquivoEntity : listArquivoEntity) {
				// OBTER CLASSE O REGISTRO
				classe = arquivoEntity.getClass();
				FileConfig config = classe.getAnnotation(FileConfig.class);
				formatter = config != null ? config.formatter() : true;
				// OBTER CAMPOS DA CLASSE
				fields = ReflexaoUtils.getFields(classe);
				linha = new StringBuilder();
				int indexAtual = 1;
				for (Field field : fields) {
					// OBTER ANOTAÇÕES DE CONFIGURAÇÃO DO CAMPO
					annotation = field.getAnnotation(FieldFileConfig.class);
					if (annotation != null) {
						field.setAccessible(true);
						// OBTER VALOR DO CAMPO
						if (annotation.type().equals(FieldFileConfig.TypeFieldFile.DATA)) {
							value = DateUtil.formatarData((Date) field.get(arquivoEntity),
									annotation.mascaraData().getFormato());
						} else {
							itemTemp = field.get(arquivoEntity);
							if (itemTemp != null) {
								value = itemTemp.toString();
							} else {
								value = null;
							}
						}
						if (value == null) {
							value = "";
						}
						if (annotation.type().equals(FieldFileConfig.TypeFieldFile.NUMERO) && !"".equals(value)) {
							// REGRA PARA TIPO NUMERO, EXIBIR SOMENTE NUMERO:
							value = value.replaceAll("[a-z|A-Z|\\s]", "");// value.replaceAll("\\D+", "");
							if (field.getType().equals(BigDecimal.class)) {
								value = formatterNumber.format(new BigDecimal(value));
							}
						}
						if (formatter && value.length() > annotation.lenght()) {
							// GARANTIR QUE SÓ IRA ESCREVER O TAMANHO
							// CONFIGURADO NA
							// ANOTAÇÃO, MESMO SE O VALOR FOR MAIOR
							value = value.substring(0, annotation.lenght());
						}
						if (annotation.preenchimento() != '?') {
							if (FieldFileConfig.LadoPreencher.ESQUERDA.equals(annotation.ladoPreencher())) {
								// PREENCHER CAMPOS VAZIOS A ESQUERDA (CONFIGURAÇÃO
								// DA
								// ANOTAÇÃO)
								value = StringUtils.leftPad(value, annotation.lenght(), annotation.preenchimento());
							} else {
								// PREENCHER CAMPOS VAZIOS A DIREITA (CONFIGURAÇÃO
								// DA
								// ANOTAÇÃO)
								value = StringUtils.rightPad(value, annotation.lenght(), annotation.preenchimento());
							}
							if (formatter && annotation.start() > indexAtual) {
								// ADICIONAR ESPAÇOS CASO SE EXISTIR DIFERENÇA ENTRE
								// AS
								// POSIÇÕES DAS COLUNAS
								linha.append(StringUtils.rightPad(" ", annotation.start() - indexAtual));
							}
						}
						// ESCREVER COLUNA NA LINHA
						if (formatter && indexAtual > annotation.start()) {
							linha.replace(annotation.start() - 1, (annotation.start() + annotation.lenght() - 1),
									value);
						} else {
							linha.append(value);
						}
						linha.append(";");
						// ATUALIZAR O INDEX ATUAL
						indexAtual = linha.length() + 1;
					}
				}
				// ESCREVER LINHA NO CONTEUDO
				conteudo.append(linha);
				// ADICIONAR QUEBRA DE LINHA
				conteudo.append(System.lineSeparator());
			}
			// RETORNA BYTES DO CONTEUDO GERADO
			return conteudo.toString();
		}
		return null;
	}

	private static void writerHeader(StringBuilder conteudo, String[] headers) {
		for (String header : headers) {
			conteudo.append(header).append(";");
		}
		conteudo.append(System.lineSeparator());
	}

}
