package br.com.template.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import br.com.template.enuns.FormatoDataEnum;

@Retention(RUNTIME)
@Target({ FIELD })
public @interface FieldFileConfig {

	public enum TypeFieldFile {
		TEXTO, NUMERO, DATA, DECIMAL
	}

	public enum LadoPreencher {
		ESQUERDA, DIREITA
	}

	TypeFieldFile type() default TypeFieldFile.TEXTO;

	LadoPreencher ladoPreencher() default LadoPreencher.ESQUERDA;

	int start();

	int lenght();

	char preenchimento() default ' ';

	FormatoDataEnum mascaraData() default FormatoDataEnum.dd_MM_yyyy;
}
