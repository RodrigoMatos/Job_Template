package constantes;

import java.util.HashMap;
import java.util.Map;

public class Constantes {
	
	public static final String SEQ_SCIENCE = "SEQ_SCIENCE";
	public static final String SEQ_LEGADO = "SEQ_LEGADO";

	public static final Map<String, Integer> CHAVES_ARQUIVOS = new HashMap<String, Integer>() {
		{
			put(SEQ_SCIENCE, 2);
			put(SEQ_LEGADO, 3);
		}
	};

}
