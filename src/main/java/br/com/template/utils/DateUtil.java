package br.com.template.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.naming.ConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DateUtil {

	private DateUtil() {
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	public static final String MENSAGEM_ERRO_VALIDACAO_PERIODO = "Erro na validação de período";

	public static String obterDataPorExtenso(Date data, Locale locale) throws Exception {
		if (data == null || locale == null) {
			throw new Exception("Parametro data ou locale est� null.");
		}
		return (DateFormat.getDateInstance(DateFormat.FULL, locale)).format(data);
	}

	public static String formatarData(Date data, String formato) {
		if (data != null) {
			return new SimpleDateFormat(formato).format(data);
		}
		return null;
	}

	public static Boolean verificarPeriodo(Date begin, Date end) {
		return DateUtils.truncate(begin, Calendar.DAY_OF_MONTH).before(DateUtils.truncate(end, Calendar.DAY_OF_MONTH))
				|| DateUtils.truncate(begin, Calendar.DAY_OF_MONTH)
						.equals(DateUtils.truncate(end, Calendar.DAY_OF_MONTH));
	}

	public static Date calcularDiferencaHoras(Date horaInicio, Date horaFim, String patternHoraMinuto)
			throws ApplicationException, ConfigurationException {
		try {
			if (horaInicio != null && horaFim != null) {
				Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
				StringBuilder buffer = new StringBuilder(Long.toString(duration.toHours()));
				buffer.append(":").append(Long.toString((duration.toMinutes()) % 60));
				return new SimpleDateFormat(patternHoraMinuto).parse(buffer.toString());
			} else {
				LOGGER.warn("Para calcular o intervalo de datas é necessário informar os dois períodos.");
				throw new ApplicationException(
						"Para calcular o intervalo de datas é necessário informar os dois períodos.", null);
			}
		} catch (ParseException ex) {
			LOGGER.error("Erro ao tentar usar o parser:", ex);
			throw new ConfigurationException("Erro ao tentar usar o parser: " + ex.getMessage());
		}
	}

	public static Long calcularDiferencaHorasEmMinutos(Date horaInicio, Date horaFim) throws Exception {
		if (horaInicio != null && horaFim != null) {
			Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
			return Long.valueOf(duration.toMinutes());
		} else {
			LOGGER.warn("Para calcular o intervalo de datas é necessário informar os dois períodos.");
			throw new Exception("Para calcular o intervalo de datas é necessário informar os dois períodos.", null);
		}
	}

	public static Long calcularDiferencaHorasEmMilissegundos(Date horaInicio, Date horaFim) throws Exception {
		if (horaInicio != null && horaFim != null) {
			Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
			return Long.valueOf(duration.toMillis());
		} else {
			LOGGER.warn("Para calcular o intervalo de datas é necessário informar os dois períodos.");
			throw new Exception("Para calcular o intervalo de datas é necessário informar os dois períodos.", null);
		}
	}

	/**
	 * 
	 * @param quantidadeHorasMinutos
	 * @return
	 */
	public static String formatarTotalHoraMinuto(Long quantidadeHorasMinutos) {
		if (quantidadeHorasMinutos != null) {
			Duration duration = Duration.ofMinutes(quantidadeHorasMinutos);
			LocalTime hora = LocalTime.MIN.plus(duration);
			return hora.toString();
		}
		return null;
	}

	public static LocalDate dateToLocalDate(Date data) {
		return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static Date localDateToDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime dateToLocalDateTime(Date data) {
		return data.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * @param data
	 * @return Boolean
	 */

	public static Date addDiasSeFimDeSemana(Date data) {
		return addDiasSeFimDeSemana(dateToLocalDate(data));
	}

	/**
	 * @param data
	 * @return Boolean
	 */
	@SuppressWarnings("incomplete-switch")
	public static Date addDiasSeFimDeSemana(LocalDate data) {
		switch (data.getDayOfWeek()) {
		case SATURDAY:
			data = data.plusDays(2);
			break;
		case SUNDAY:
			data = data.plusDays(1);
			break;
		}
		return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	public static String convertSeconds(Integer segundos) {
		if (segundos == null) {
			return "00:00:00";
		}
		return convertSeconds(segundos.longValue());
	}

	public static String convertSeconds(Long segundos) {
		if (segundos == null || segundos.equals(0L)) {
			return "00:00:00";
		}
		long segundo = segundos % 60;
		long minutos = segundos / 60;
		long minuto = minutos % 60;
		long hora = minutos / 60;
		return String.format("%02d:%02d:%02d", hora, minuto, segundo);
	}

	public static Integer extrairAnoDaData(Date data) {
		return dateToLocalDate(data).getYear();
	}

	public static Integer extrairMesDaData(Date data) {
		return dateToLocalDate(data).getMonthValue();
	}

	public static Integer extrairDiaDaData(Date data) {
		return dateToLocalDate(data).getDayOfMonth();
	}

	public static Date adicionarDias(Date data, Integer qtdDias) {
		LocalDate lData = dateToLocalDate(data);
		lData = lData.plusDays(qtdDias);
		return localDateToDate(lData);
	}

	public static Date adicionarMes(Date data, Integer qtdDias) {
		LocalDate lData = dateToLocalDate(data);
		lData = lData.plusMonths(qtdDias);
		return localDateToDate(lData);
	}

	public static List<String> obterReferenciasAnteriores(String referenciaInicial,
			Integer quantidadeReferenciasAnteriores) {
		if (referenciaInicial == null || quantidadeReferenciasAnteriores == null
				|| quantidadeReferenciasAnteriores <= 0) {
			return new ArrayList<>();
		}

		if (referenciaInicial.replaceAll("/", "").length() == 8) {
			referenciaInicial = referenciaInicial.substring(3, 10);
		}

		DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().parseStrict().appendPattern("MM/yyyy")
				.parseDefaulting(ChronoField.DAY_OF_MONTH, 1).parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
				.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0).parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				.toFormatter();
		LocalDate data = LocalDate.parse(referenciaInicial, DATEFORMATTER);

		List<String> referencias = new ArrayList<>();

		for (int i = 1; i <= quantidadeReferenciasAnteriores; i++) {
			LocalDate mesAnterior = data.minusMonths(i).with(TemporalAdjusters.lastDayOfMonth());
			referencias.add(mesAnterior.format(DATEFORMATTER));
		}
		return referencias;
	}

	public static List<String> obterReferenciasAnteriores(Date referenciaInicial,
			Integer quantidadeReferenciasAnteriores) {
		if (referenciaInicial == null || quantidadeReferenciasAnteriores <= 0) {
			return new ArrayList<>(0);
		}
		String referencia = referenciaInicial.toLocaleString();
		referencia = referencia.substring(3, 10);
		return obterReferenciasAnteriores(referencia, quantidadeReferenciasAnteriores);
	}

	public static Date criarData(int dia, int mes, int ano) throws ConfigurationException {
		try {
			LocalDate localDate = LocalDate.of(ano, mes, dia);
			return localDateToDate(localDate);
		} catch (DateTimeException e) {
			LOGGER.error("Erro ao tentar criar data inválida:", e);
			throw new ConfigurationException("Erro ao tentar criar data inválida: " + e.getMessage());
		}
	}

	public static Integer calcularDiferencaDatasEmDias(Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio != null && dataFim != null) {
			Period periodo = Period.between(dateToLocalDate(dataInicio), dateToLocalDate(dataFim));
			return periodo.getDays();
		} else {
			LOGGER.warn("Erro ao calcular diferença entre dias");
			throw new Exception("Erro ao calcular diferença entre dias: data início ou fim nulas");
		}
	}

	public static Date getFirstDateOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static int getNumberOfDaysInMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		return day;
	}

	public static Date convertStringToDate(String data) throws Exception {
		if (data == null || StringUtils.isBlank(data)) {
			return null;
		}
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return formatter.parse(data);
		} catch (ParseException e) {
			LOGGER.error("Erro ao converter data para string.", e);
			throw new Exception("Erro ao converter data para string");
		}
	}

	public static Integer obterUltimoDia(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Date obterUltimaDataDoMes(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, obterUltimoDia(date));
		return c.getTime();
	}

	public static Date obterUltimaDataDoMes() {
		return obterUltimaDataDoMes(new Date());
	}

	public static void validateInterval(Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio != null && dataFim != null) {
			if (dataInicio.equals(dataFim)) {
				LOGGER.warn(MENSAGEM_ERRO_VALIDACAO_PERIODO);
				throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			}
			if (dataInicio.after(dataFim)) {
				LOGGER.warn(MENSAGEM_ERRO_VALIDACAO_PERIODO);
				throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			}
		}
	}

	public static boolean isDataInicialMaiorQueFinal(Date dataInicio, Date dataFim) {
		return dataInicio != null && dataFim != null && dataInicio.after(dataFim);
	}

	public static boolean isDataInicialMaiorOuIguallQueFinal(Date dataInicio, Date dataFim) {
		return dataInicio != null && dataFim != null && (dataInicio.after(dataFim) || dataInicio.equals(dataFim));
	}

	public static void validarDataInicialMenorQueFinal(Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio != null && dataFim != null && dataInicio.after(dataFim)) {
			LOGGER.warn(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
		}
	}

	public static Date addAnos(Date data, Integer qtdAnos) throws Exception {
		if (data == null || qtdAnos == null) {
			LOGGER.warn("É NECESSÁRIO INFORMAR A DATA E A QUANTIDADE DE DIAS PARA A OPERAÇÃO.");
			throw new Exception("É NECESSÁRIO INFORMAR A DATA E A QUANTIDADE DE DIAS PARA A OPERAÇÃO.");
		}
		return localDateToDate(dateToLocalDate(data).plusYears(qtdAnos));
	}

}
