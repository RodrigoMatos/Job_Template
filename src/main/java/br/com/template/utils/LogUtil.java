package br.com.template.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author romatos
 * @version 1.0
 */

public final class LogUtil {

	private LogUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

	public static StringBuilder logErro = new StringBuilder();
	public static boolean deuErro = false;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	/**
	 * @author romatos
	 * @param mensagem - Texto que ser� exibido. Exibe um texto INFO no cosole e
	 *                 escreve no arquivo de log.
	 */
	public static void Info(String mensagem) {
		LOGGER.info(" " + mensagem);
		logErro.append(sdf.format(new Date()) + " [INFO]   " + mensagem + "\n");
	}

	/**
	 * @author romatos
	 * @param mensagem - Texto que ser� exibido. Exibe um texto de ERROR no cosole e
	 *                 escreve no arquivo de log.
	 */
	public static void Error(String mensagem) {
		LOGGER.error(mensagem);
		logErro.append(sdf.format(new Date()) + " [ERROR]  " + mensagem + "\n");
		deuErro = true;
	}

	/**
	 * @author romatos
	 * @param mensagem - Texto que ser� exibido. Exibe um texto de ERROR no cosole e
	 *                 escreve no arquivo de log.
	 */
	public static void Error(String mensagem, Throwable e) {
		LOGGER.error(mensagem, e);
		logErro.append(sdf.format(new Date()) + " [ERROR]  " + mensagem + "\n");
		deuErro = true;
	}

	/**
	 * @author romatos
	 * @param mensagem - Texto que ser� exibido. Exibe um texto TRACE no cosole e
	 *                 escreve no arquivo de log.
	 */
	public static void Trace(String mensagem) {
		LOGGER.trace(mensagem);
		logErro.append(sdf.format(new Date()) + " [TRACE]  " + mensagem + "\n");
	}

	/**
	 * @author romatos
	 * @param mensagem - Texto que ser� exibido. Exibe um texto de WARN no cosole e
	 *                 escreve no arquivo de log.
	 */
	public static void Warn(String mensagem) {
		LOGGER.warn(" " + mensagem);
		logErro.append(sdf.format(new Date()) + " [WARN]   " + mensagem + "\n");
	}

}
