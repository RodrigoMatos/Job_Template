package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);

	public static StringBuilder logErro = new StringBuilder();
	public static boolean deuErro = false;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

	public static void Info(String mensagem) {
		LOGGER.info(" " + mensagem);
		logErro.append(sdf.format(new Date()) + " [INFO]   " + mensagem + "\n");
	}
	
	public static void Info(String mensagem, String flagNaoLogar) {
		LOGGER.info(" " + mensagem);
	}

	public static void Error(String mensagem) {
		LOGGER.error(mensagem);
		logErro.append(sdf.format(new Date()) + " [ERROR]  " + mensagem + "\n");
		deuErro = true;
	}

	public static void Trace(String mensagem) {
		LOGGER.trace(mensagem);
		logErro.append(sdf.format(new Date()) + " [TRACE]  " + mensagem + "\n");
	}

	public static void Warn(String mensagem) {
		LOGGER.warn(" " + mensagem);
		logErro.append(sdf.format(new Date()) + " [WARN]   " + mensagem + "\n");
	}

}
