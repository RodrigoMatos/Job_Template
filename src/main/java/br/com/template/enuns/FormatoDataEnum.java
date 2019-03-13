package br.com.template.enuns;

import lombok.Getter;

@Getter
public enum FormatoDataEnum {

	dd_MM_yyyy("dd/MM/yyyy"),
	MM_yyyy("MM/yyyy"),
	HH_MM_SS("HH:mm:ss"),
	dd_MM_yyyy_HH_mm_ss("dd/MM/yyyy HH:mm:ss"), 
	dd_MM_yyyy_HH_mm("dd/MM/yyyy HH:mm"),
	ddMMyyyy_HHmmss("ddMMyyyy_HHmmss"), 
	ddMMyyyy("ddMMyyyy"), 
	yyyyMMdd("yyyyMMdd");

	private String formato;
	
	private FormatoDataEnum(String formato) {
		this.formato = formato;
	} 

}
