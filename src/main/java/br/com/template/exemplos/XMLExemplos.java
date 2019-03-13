package br.com.template.exemplos;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;

import br.com.template.utils.arquivo.XmlUtils;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class XMLExemplos {

	public static void lerArquivoXML() {

		try {
			Element arquivoXML = XmlUtils.lerXML("D:\\testes\\teste.xml");
			List elements = XmlUtils.getFilhos(arquivoXML, "ELEMENT");

			Iterator i = elements.iterator();
			Map<String, String> filho;
			while (i.hasNext()) {
				Element element = (Element) i.next();
				filho = XmlUtils.getConteudoDoFilho(element);
				System.out.println("Codigo: " + filho.get("CODIGO"));
				System.out.println("Descri��o: " + filho.get("DESCRICAO"));
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
