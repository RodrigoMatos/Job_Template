package br.com.template.utils.arquivo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class XmlUtils {

	public static List<Map<String,String>> importarElementoXML(String arquivo, String elemento) throws Exception {
		File arquivoXML = new File(arquivo);
		return importarElementoXML(arquivoXML, elemento);
	}

	public static List<Map<String,String>> importarElementoXML(File arquivo, String elemento) throws Exception {
		List<Map<String,String>> elementoXML = obterDadosXML(arquivo, elemento);
		return elementoXML;
	}

	private static List<Map<String,String>> obterDadosXML(File arquivo, String elemento) throws Exception {

		List<Map<String, String>> listElementosXML = new ArrayList<Map<String, String>>();
		Element arquivoXML = lerXML(arquivo);
		List elements = getFilhos(arquivoXML, elemento);

		Iterator i = elements.iterator();
		Map<String, String> filho;
		while (i.hasNext()) {
			Element element = (Element) i.next();
			filho = getConteudoDoFilho(element);
			listElementosXML.add(filho);
		}
		return listElementosXML;
	}

	public static Element lerXML(String dirArquivo) throws JDOMException, IOException {
		return lerXML(new File(dirArquivo));
	}

	public static Element lerXML(File fXmlFile) throws JDOMException, IOException {

		SAXBuilder sb = new SAXBuilder();
		Document d = sb.build(fXmlFile);
		Element conteudo = d.getRootElement();
		return conteudo;
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
