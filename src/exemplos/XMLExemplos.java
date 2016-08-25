package exemplos;

import org.jdom2.Element;

import arquivos.xml.ArquivoXML;

public class XMLExemplos {

	public void lerArquivoXML() {
		
		try {
			Element arquivoXML = ArquivoXML.lerXML("D:\\teste\\teste.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
