package br.com.template.arquivos.xml;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public abstract class ArquivoXML implements Serializable {

	private static final long serialVersionUID = 8104712020467009444L;

	public static Element lerXML(String dirArquivo) throws JDOMException, IOException {

		return lerXML(new File(dirArquivo));
	}
	
	public static Element lerXML(File fXmlFile) throws JDOMException, IOException {

		SAXBuilder sb = new SAXBuilder();
		Document d = sb.build(fXmlFile);
		return d.getRootElement();
	}

	@SuppressWarnings("rawtypes")
	public static List getFilhos(Element conteudo, String no) {
		return conteudo.getChildren(no);
	}

	public static Map<String, String> getConteudoDoFilho(Element filho) {

		Map<String, String> conteudo = new HashMap<String, String>();
		List<Element> listaFilho = filho.getChildren();

		if (listaFilho != null && !listaFilho.isEmpty()) {
			for (Element temp : listaFilho) {
				if (temp.getValue() != null && !"".equals(temp.getValue())) {
					conteudo.put(temp.getName(), temp.getValue());
				}
			}
		}
		return conteudo;
	}

}
