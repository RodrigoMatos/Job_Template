package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	private DateUtil() {
	}

	public static final String MENSAGEM_ERRO_VALIDACAO_PERIODO = "Erro na validação de período";

	public static Date calcularDiferencaHoras(Date horaInicio, Date horaFim, String patternHoraMinuto) throws Exception {
		try {
			if (horaInicio != null && horaFim != null) {
				Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
				StringBuilder buffer = new StringBuilder(Long.toString(duration.toHours()));
				buffer.append(":").append(Long.toString((duration.toMinutes()) % 60));
				return new SimpleDateFormat(patternHoraMinuto).parse(buffer.toString());
			}
			else {
				throw new Exception("Para calcular intervalo de datas é necessário informar os dois períodos.");
			}
		}
		catch (ParseException ex) {
			throw new Exception("Erro ao tentar usar o parser: " + ex.getMessage());
		}
	}

	public static Long calcularDiferencaHorasEmMinutos(Date horaInicio, Date horaFim) throws Exception {
		if (horaInicio != null && horaFim != null) {
			Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
			return Long.valueOf(duration.toMinutes());
		}
		else {
			throw new Exception("Para calcular intervalo de datas é necessário informar os dois períodos.");
		}
	}

	public static Long calcularDiferencaHorasEmMilissegundos(Date horaInicio, Date horaFim) throws Exception {
		if (horaInicio != null && horaFim != null) {
			Duration duration = Duration.between(horaInicio.toInstant(), horaFim.toInstant());
			return Long.valueOf(duration.toMillis());
		}
		else {
			throw new Exception("Para calcular intervalo de datas é necessário informar os dois períodos.");
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

	public static void validateInterval(Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio != null && dataFim != null) {
			if (dataInicio.equals(dataFim)) {
				throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			}
			if (dataInicio.after(dataFim)) {
				throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			}
		}
	}

	/**
	 * @param data
	 * @return Boolean
	 */

	public static Date addDiasSeFimDeSemana(Date data) {
		return addDiasSeFimDeSemana(data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
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

	public static void validarDataInicialMenorQueFinal(Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio != null && dataFim != null) {
			if (dataInicio.after(dataFim)) {
				throw new Exception(MENSAGEM_ERRO_VALIDACAO_PERIODO);
			}
		}
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

	public static String obterDataPorExtenso(Date data, Locale locale) throws Exception {
		if (data == null || locale == null) {
			throw new Exception("Parametro data ou locale est� null.");
		}
		return (DateFormat.getDateInstance(DateFormat.FULL, locale)).format(data);
	}

}
