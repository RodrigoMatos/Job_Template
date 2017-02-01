package exemplos;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.FtpVO;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import utils.ExportacaoUtils;
import utils.ImportacaoUtils;
import arquivos.excel.ArquivoExcel;
import bancoDeDados.ConexaoPool;
import constantes.ConstantesDBAcess;

/**
 * @author romatos
 * @version 1.0
 */

public abstract class ArquivoExcelExemplos {

	public static void criarExcel() {

		try {
			ArquivoExcel arquivo = new ArquivoExcel("D:\\testes\\teste.xls");
			Sheet aba = arquivo.addAba("Aba teste");
			Row linha = arquivo.addLinha(aba, 1);
			Cell celula = arquivo.addCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "Teste");
			arquivo.salvarArquivo();
			aba = arquivo.obterAba(0);
			linha =  arquivo.obterLinha(aba, 0);
			celula = arquivo.obterCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "novoTeste");
			arquivo.salvarArquivo();
			arquivo.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void alterarExcel() {

		try {
			ArquivoExcel arquivo = new ArquivoExcel("D:\\testes\\teste.xls");
			Sheet aba = arquivo.obterAba(0);
			Row linha =  arquivo.obterLinha(aba, 0);
			Cell celula = arquivo.obterCelula(linha, 0);
			arquivo.escreverNaCelula(celula, "novoTeste");
			arquivo.salvarArquivo();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void exportarConsulta() {

		try {
			ConexaoPool.initDataSource("SCIENCE", ConstantesDBAcess.BANCOSCIENCEBCV);
			File arquivo = new File("D:\\testes\\consultaIgor.xlsx");
			ExportacaoUtils.exportarConsultaParaArquivoExcel("SELECT * FROM ( SELECT DISTINCT        ESTADO.SIG_ESTADO AS ESTADO,        ERB.SIG_ERB AS SIGLA,        ERB.NOM_ERB AS \"NOME EQUIPAMENTO\",        'WCDMA' AS TECNOLOGIA,        SITE.NOM_SITE AS \"NOME DO SITE\",        MUNICIPIO.NOM_MUNICIPIO AS DISTRITO,        NVL(MUNICIPIO_.NOM_MUNICIPIO, MUNICIPIO.NOM_MUNICIPIO) AS \"MUNICÍPIO\",        NVL(MUNICIPIO_.NUM_COD_IBGE, MUNICIPIO.NUM_COD_IBGE) AS \"CÓD. IBGE\",        SITE.DES_ENDERECO_SITE AS \"ENDEREÇO\",        B.NOM_BAIRRO AS BAIRRO,        'ATIVADO' AS \"SITUAÇÃO\",        SITE.STR_LATITUDE_SITE AS LATITUDE,        SITE.STR_LONGITUDE_SITE AS LONGITUDE,        SITE.SIG_SITE AS \"SIGLA DO SITE\",        (NVL(SIGLA_LOGICA.NUM_PAIS,'724') || '-'         || LPAD(NVL(SIGLA_LOGICA.NUM_MNC,NVL(ERB.MNC_ERB,ESTADO.MNC)),2,'0')         || '-' || DECODE(NVL(SIGLA_LOGICA.LA_NUMBER,EB.LA_NUMBER),NULL,'000',LPAD(NVL(SIGLA_LOGICA.LA_NUMBER,EB.LA_NUMBER),3,'0'))         || NVL(SIGLA_LOGICA.NUM_DDD,NVL(SETOR.DES_AREA_TARIFACAO, MUNICIPIO.DES_AREA_TARIFACAO)) || '-'         || LPAD(NVL(SIGLA_LOGICA.NUM_FILIACAO_WCDMA,NUM_FILIACAO),4,'0') ||         CASE WHEN (SIGLA_LOGICA.NUM_CGI IS NOT NULL) THEN SUBSTR(SIGLA_LOGICA.NUM_CGI,-1)         ELSE DECODE(TRIM(SETOR.SIG_SETOR),'A','1','B','2','C','3','D','4','E','5','F','6','G','7','H','8','I','9','J','10','X','1','Y','2','Z','3', TRIM(SETOR.SIG_SETOR))         END        ) CGI FROM SIGLA_LOGICA, SETOR, PORTADORA_TECNOLOGIA, BANDA, ERB, SITE, MUNICIPIO, MUNICIPIO MUNICIPIO_, ESTADO, ERB_BANDA EB, BAIRRO B WHERE SIGLA_LOGICA.SEQ_SETOR         = SETOR.SEQ_SETOR AND SIGLA_LOGICA.SEQ_CENTRAL_ERB_BSC = PORTADORA_TECNOLOGIA.SEQ_CENTRAL_ERB_BSC AND SIGLA_LOGICA.NUM_PORTADORA       = PORTADORA_TECNOLOGIA.NUM_PORTADORA AND PORTADORA_TECNOLOGIA.SEQ_BANDA   = BANDA.SEQ_BANDA AND SIGLA_LOGICA.SEQ_ERB             = ERB.SEQ_ERB AND EB.SEQ_ERB                       = ERB.SEQ_ERB AND EB.SEQ_BANDA                     = BANDA.SEQ_BANDA AND ERB.SEQ_SITE                     = SITE.SEQ_SITE AND MUNICIPIO.SEQ_MUNICIPIO_LOCALIDADE = MUNICIPIO_.SEQ_MUNICIPIO(+) AND SITE.SEQ_BAIRRO                  = B.SEQ_BAIRRO(+) AND SETOR.STS_ATIVACAO               <> 'X' AND (ERB.STS_ATIVACAO IN ('A','E') OR (ERB.STS_ATIVACAO = 'D' AND ERB.DTC_DESATIVACAO_ERB >= TO_DATE('29/02/2016','DD/MM/YYYY') AND (ERB.DTC_ATIVACAO_COM_ERB IS NULL OR ERB.DTC_ATIVACAO_COM_ERB < TO_DATE('01/03/2016','DD/MM/YYYY')))) AND SITE.SEQ_MUNICIPIO               = MUNICIPIO.SEQ_MUNICIPIO AND MUNICIPIO.SEQ_ESTADO             = ESTADO.SEQ_ESTADO AND EB.SEQ_SITUACAO                  <> 2 UNION ALL SELECT DISTINCT        ES.SIG_ESTADO AS ESTADO,        E.SIG_ERB AS SIGLA,        E.NOM_ERB AS \"NOME EQUIPAMENTO\",        FC_AGRUPA_DADOS('SELECT T.SIG_TECNOLOGIA FROM CENTRAL_ERB_BSC CEB, TECNOLOGIA T WHERE CEB.SEQ_TECNOLOGIA_ERB = T.SEQ_TECNOLOGIA AND (CEB.STS_ATIVACAO <> ''X'' OR CEB.STS_ATIVACAO IS NULL) AND CEB.SEQ_ERB = ' || E.SEQ_ERB, ',') AS TECNOLOGIA,        S.NOM_SITE AS \"NOME DO SITE\",        M.NOM_MUNICIPIO AS DISTRITO,        NVL(MUNICIPIO_.NOM_MUNICIPIO, M.NOM_MUNICIPIO) AS \"MUNICÍPIO\",        NVL(MUNICIPIO_.NUM_COD_IBGE, M.NUM_COD_IBGE) AS \"CÓD. IBGE\",        S.DES_ENDERECO_SITE AS \"ENDEREÇO\",        B.NOM_BAIRRO AS BAIRRO,        'ATIVADO' AS \"SITUAÇÃO\",        S.STR_LATITUDE_SITE AS LATITUDE,        S.STR_LONGITUDE_SITE AS LONGITUDE,        S.SIG_SITE AS \"SIGLA DO SITE\",        NULL CGI FROM ERB E, SITE S, MUNICIPIO M, MUNICIPIO MUNICIPIO_, ESTADO ES, BAIRRO B, CENTRAL_ERB_BSC CEB WHERE E.SEQ_SITE = S.SEQ_SITE AND S.SEQ_MUNICIPIO = M.SEQ_MUNICIPIO AND M.SEQ_MUNICIPIO_LOCALIDADE = MUNICIPIO_.SEQ_MUNICIPIO(+) AND M.SEQ_ESTADO = ES.SEQ_ESTADO AND E.SEQ_ERB = CEB.SEQ_ERB AND S.SEQ_BAIRRO = B.SEQ_BAIRRO(+) AND (CEB.STS_ATIVACAO <> 'X' OR CEB.STS_ATIVACAO IS NULL) AND (E.STS_ATIVACAO IN ('A','E') OR (E.STS_ATIVACAO = 'D' AND E.DTC_DESATIVACAO_ERB >= TO_DATE('29/02/2016','DD/MM/YYYY') AND (E.DTC_ATIVACAO_COM_ERB IS NULL OR E.DTC_ATIVACAO_COM_ERB < TO_DATE('01/03/2016','DD/MM/YYYY')))) AND CEB.SEQ_TECNOLOGIA_ERB NOT IN (7,43,63) UNION ALL SELECT DISTINCT        ESTADO.SIG_ESTADO AS ESTADO,        ERB.SIG_ERB AS SIGLA,        ERB.NOM_ERB AS \"NOME EQUIPAMENTO\",        'GSM' AS TECNOLOGIA,        SITE.NOM_SITE AS \"NOME DO SITE\",        MUNICIPIO.NOM_MUNICIPIO AS DISTRITO,        NVL(MUNICIPIO_.NOM_MUNICIPIO, MUNICIPIO.NOM_MUNICIPIO) AS \"MUNICÍPIO\",        NVL(MUNICIPIO_.NUM_COD_IBGE, MUNICIPIO.NUM_COD_IBGE) AS \"CÓD. IBGE\",        SITE.DES_ENDERECO_SITE AS \"ENDEREÇO\",        B.NOM_BAIRRO AS BAIRRO,        'ATIVADO' AS \"SITUAÇÃO\",        SITE.STR_LATITUDE_SITE AS LATITUDE,        SITE.STR_LONGITUDE_SITE AS LONGITUDE,        SITE.SIG_SITE AS \"SIGLA DO SITE\",        (NVL(SLG.NUM_PAIS,'724') || '-' || LPAD(NVL(SLG.NUM_MNC,NVL(ERB.MNC_ERB,ESTADO.MNC)),2,'0')         || '-' || DECODE(NVL(SLG.LA_NUMBER,EB.LA_NUMBER),NULL,'000',LPAD(NVL(SLG.LA_NUMBER,EB.LA_NUMBER),3,'0'))         || NVL(SLG.NUM_DDD,NVL(SETOR.DES_AREA_TARIFACAO, MUNICIPIO.DES_AREA_TARIFACAO))         || '-' || LPAD(SLG.NUM_FILIACAO,4,'0') || SLG.NUM_CGI_ULTIMO_DIGITO        ) CGI FROM SIGLA_LOGICA_GSM SLG, SETOR_TECNOLOGIA ST, SETOR, ERB_BANDA EB, BANDA, ERB, SITE, MUNICIPIO, MUNICIPIO MUNICIPIO_, ESTADO, BAIRRO B WHERE SLG.SEQ_SETOR_TECNOLOGIA = ST.SEQ_SETOR_TECNOLOGIA AND ST.SEQ_SETOR               = SETOR.SEQ_SETOR AND SETOR.SEQ_ERB              = ERB.SEQ_ERB AND ERB.SEQ_ERB                = EB.SEQ_ERB AND SLG.SEQ_ERB_BANDA          = EB.SEQ_ERB_BANDA AND EB.SEQ_BANDA               = BANDA.SEQ_BANDA AND ERB.SEQ_SITE               = SITE.SEQ_SITE AND SETOR.STS_ATIVACAO         <> 'X' AND (ERB.STS_ATIVACAO IN ('A','E') OR (ERB.STS_ATIVACAO = 'D' AND ERB.DTC_DESATIVACAO_ERB >= TO_DATE('29/02/2016','DD/MM/YYYY') AND (ERB.DTC_ATIVACAO_COM_ERB IS NULL OR ERB.DTC_ATIVACAO_COM_ERB < TO_DATE('01/03/2016','DD/MM/YYYY')))) AND SITE.SEQ_MUNICIPIO         = MUNICIPIO.SEQ_MUNICIPIO AND MUNICIPIO.SEQ_ESTADO       = ESTADO.SEQ_ESTADO AND MUNICIPIO.SEQ_MUNICIPIO_LOCALIDADE = MUNICIPIO_.SEQ_MUNICIPIO(+) AND SITE.SEQ_BAIRRO            = B.SEQ_BAIRRO(+) AND EB.SEQ_SITUACAO            <> 2 UNION ALL SELECT DISTINCT        ESTADO.SIG_ESTADO AS ESTADO,        ERB.SIG_ERB AS SIGLA,        ERB.NOM_ERB AS \"NOME EQUIPAMENTO\",        'LTE' AS TECNOLOGIA,        SITE.NOM_SITE AS \"NOME DO SITE\",        MUNICIPIO.NOM_MUNICIPIO AS DISTRITO,        NVL(MUNICIPIO_.NOM_MUNICIPIO, MUNICIPIO.NOM_MUNICIPIO) AS \"MUNICÍPIO\",        NVL(MUNICIPIO_.NUM_COD_IBGE, MUNICIPIO.NUM_COD_IBGE) AS \"CÓD. IBGE\",        SITE.DES_ENDERECO_SITE AS \"ENDEREÇO\",        B.NOM_BAIRRO AS BAIRRO,        'ATIVADO' AS \"SITUAÇÃO\",        SITE.STR_LATITUDE_SITE AS LATITUDE,        SITE.STR_LONGITUDE_SITE AS LONGITUDE,        SITE.SIG_SITE AS \"SIGLA DO SITE\",        (NVL(SIGLA_LOGICA_LTE.NUM_PAIS,'724') || '-' ||         LPAD(NVL(SIGLA_LOGICA_LTE.NUM_MNC,NVL(ERB.MNC_ERB,ESTADO.MNC)),2,'0') || '-' ||         NUM_ENB_ID || '-' || LPAD((NUM_CELL_ID),2,'0') || NUM_ECGI_ULTIMO_DIGITO        ) CGI FROM SIGLA_LOGICA_LTE, SETOR, PORTADORA_TECNOLOGIA, BANDA, ERB, SITE, MUNICIPIO, MUNICIPIO MUNICIPIO_, ESTADO, ERB_BANDA EB, BAIRRO B WHERE SIGLA_LOGICA_LTE.SEQ_SETOR         = SETOR.SEQ_SETOR AND SIGLA_LOGICA_LTE.SEQ_CENTRAL_ERB_BSC = PORTADORA_TECNOLOGIA.SEQ_CENTRAL_ERB_BSC AND SIGLA_LOGICA_LTE.NUM_PORTADORA       = PORTADORA_TECNOLOGIA.NUM_PORTADORA AND PORTADORA_TECNOLOGIA.SEQ_BANDA       = BANDA.SEQ_BANDA AND SIGLA_LOGICA_LTE.SEQ_ERB             = ERB.SEQ_ERB AND EB.SEQ_ERB                           = ERB.SEQ_ERB AND EB.SEQ_BANDA                         = BANDA.SEQ_BANDA AND ERB.SEQ_SITE                         = SITE.SEQ_SITE AND SETOR.STS_ATIVACAO                   <> 'X' AND (ERB.STS_ATIVACAO IN ('A','E') OR (ERB.STS_ATIVACAO = 'D' AND ERB.DTC_DESATIVACAO_ERB >= TO_DATE('29/02/2016','DD/MM/YYYY') AND (ERB.DTC_ATIVACAO_COM_ERB IS NULL OR ERB.DTC_ATIVACAO_COM_ERB < TO_DATE('01/03/2016','DD/MM/YYYY')))) AND SITE.SEQ_MUNICIPIO                   = MUNICIPIO.SEQ_MUNICIPIO AND MUNICIPIO.SEQ_ESTADO                 = ESTADO.SEQ_ESTADO AND MUNICIPIO.SEQ_MUNICIPIO_LOCALIDADE   = MUNICIPIO_.SEQ_MUNICIPIO(+) AND SITE.SEQ_BAIRRO                      = B.SEQ_BAIRRO(+) AND EB.SEQ_SITUACAO                      <> 2 ) ORDER BY ESTADO, SIGLA", arquivo, "SCIENCE");
			ConexaoPool.endDataSource("SCIENCE");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportarLista() {

		List<FtpVO> list = new ArrayList<FtpVO>();
		list.add(new FtpVO("serv1", "user1", "senha1"));
		list.add(new FtpVO("serv2", "user2", "senha2"));
		list.add(new FtpVO("serv3", "user3", "senha3"));
		list.add(new FtpVO("serv4", "user4", "senha4"));

		List<String> atributos = new ArrayList<String>();

		atributos.add("servidor");
		atributos.add("usuario");
		atributos.add("senha");

		List<String> cabecalho = new ArrayList<String>();
		cabecalho.add("Servidor");
		cabecalho.add("Usuario");
		cabecalho.add("Senha");

		try {
			ExportacaoUtils.exportarListaExcel("D:\\testes\\lista.xlsx", list, atributos, cabecalho);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void importarExcel() {

		try {
			List<LinkedHashMap<Integer, Object>> list = ImportacaoUtils.importarExcel("D:\\testes\\consulta.xlsx", 0);
			Integer qtd;
			for (LinkedHashMap<Integer, Object> linha : list) {
				qtd = linha.size();
				for (Integer i = 0; i < qtd; i++) {
					System.out.print(linha.get(i) + " | ");
				}
				System.out.println("");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}