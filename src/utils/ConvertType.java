package utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import reflexao.ReflexaoUtils;

public final class ConvertType {

	private ConvertType() {
	}

	public static Long getLong(Object object) {
		if (object != null) {
			return Long.valueOf(object.toString());
		}
		return null;
	}

	public static Date getDate(Object object) {
		if (object != null) {
			return (Timestamp) object;
		}
		return null;
	}

	public static Timestamp getTimestamp(Object object) {
		if (object != null) {
			return (Timestamp) object;
		}
		return null;
	}

	public static Integer getInteger(Object object) {
		if (object != null) {
			return Integer.valueOf(object.toString());
		}
		return null;
	}

	public static Double getDouble(Object object) {
		if (object != null) {
			return Double.valueOf(object.toString());
		}
		return null;
	}

	public static Float getFloat(Object object) {
		if (object != null) {
			return Float.valueOf(object.toString());
		}
		return null;
	}

	public static BigDecimal getBigDecimal(Object object) {
		if (object != null) {
			return new BigDecimal(object.toString());
		}
		return null;
	}

	public static String getString(Object object) {
		if (object != null) {
			return object.toString();
		}
		return null;
	}

	public static Boolean getBoolean(Object object) {
		if (object != null) {
			if ("1".equals(object.toString()) || "true".equalsIgnoreCase(object.toString())) {
				return true;
			}
		}
		return false;
	}

	public static Object getValue(Object entity, String atributo, Object valor) throws NoSuchFieldException {

		Field field = ReflexaoUtils.getField(entity.getClass(), atributo);

		if (field.getType().equals(String.class)) {
			// Lob annotation = field.getAnnotation(Lob.class);
			// if (annotation == null) {
			return getString(valor);
			// }
			// else {
			// Clob clob = (Clob) valor;
			// return IOUtils.toString(clob.getCharacterStream());
			// }
		}
		else if (field.getType().equals(Long.class)) {
			return getLong(valor);
		}
		else if (field.getType().equals(Integer.class)) {
			return getInteger(valor);
		}
		else if (field.getType().equals(BigDecimal.class)) {
			return getBigDecimal(valor);
		}
		else if (field.getType().equals(Float.class)) {
			return getFloat(valor);
		}
		else if (field.getType().equals(Double.class)) {
			return getDouble(valor);
		}
		else if (field.getType().equals(Date.class)) {
			return getDate(valor);
		}
		else if (field.getType().equals(Timestamp.class)) {
			return getTimestamp(valor);
		}
		else if (field.getType().equals(Boolean.class)) {
			return getBoolean(valor);
		}
		return valor;
	}

}
