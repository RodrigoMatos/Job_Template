package exemplos;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

import arquivos.xml.ArquivoXML;

public class XMLExemplos {

	public void lerArquivoXML() {
		
		try {
			Element arquivoXML = ArquivoXML.lerXML("D:\\testes\\teste.xml");
			List elements = ArquivoXML.getFilhos(arquivoXML, "ELEMENT");
			
			Iterator i = elements.iterator();
			Map<String,String> filho;			
			while (i.hasNext()) {
				Element element = (Element) i.next();
				filho = ArquivoXML.getConteudoDoFilho(element);
				System.out.println("Codigo: " + filho.get("CODIGO"));
				System.out.println("Descrição: " + filho.get("DESCRICAO"));
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
