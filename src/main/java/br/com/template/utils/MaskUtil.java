package br.com.template.utils;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

public final class MaskUtil {

	private MaskUtil() {
	}
	
	public static String format(String pattern, Object value) {
        try {
            MaskFormatter mask = new MaskFormatter(pattern);
            mask.setValueContainsLiteralCharacters(false);
            return mask.valueToString(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

	public static String formatarTelefone (String telefone) {
    	if (telefone != null) {
			if (telefone.startsWith("0")) { // 0 do ddd
				if (telefone.length() == 12) { // nono digito
					telefone = telefone.replaceFirst("(.{3})(.{5})(.{4})", "($1) $2-$3");
				} else if (telefone.length() == 11) {
					telefone = telefone.replaceFirst("(.{3})(.{4})(.{4})", "($1) $2-$3");
				}
			} else if (telefone.length() == 11) { // nono digito
				telefone = telefone.replaceFirst("(.{2})(.{5})(.{4})", "($1) $2-$3");
			} else if (telefone.length() == 10) {
				telefone = telefone.replaceFirst("(.{2})(.{4})(.{4})", "($1) $2-$3");
			}
		}
    	return telefone;
    }

}
