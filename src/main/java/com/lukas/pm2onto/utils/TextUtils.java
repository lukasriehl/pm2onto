package com.lukas.pm2onto.utils;

import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.TipoAtividade;

/**
 *
 * @author lukas
 */
public class TextUtils {

    public static String removeTagsHtml(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                ? valorCampo.replaceAll("\\<.+?\\>", "").replace("<", "").replace(">", "")
                .replaceAll("[áàãâä]", "a")
                .replaceAll("[ÁÀÃÂÄ]", "A")
                .replaceAll("[éèẽêë]", "e")
                .replaceAll("[ÈÉẼÊË]", "E")
                .replaceAll("[íìĩîï]", "i")
                .replaceAll("[ÍÌĨÎÏ]", "I")
                .replaceAll("[óòõôö]", "o")
                .replaceAll("[ÓÒÕÔÖ]", "O")
                .replaceAll("[úùũûü]", "u")
                .replaceAll("[ÚÙÛÛÜ]", "U")
                .replaceAll("ç", "c")
                .replaceAll("Ç", "C") : null;
    }
    
    public static String formataNome(String nome){
        return nome != null && !nome.isEmpty() ? 
                nome.replaceAll("[?\\/\\s():,;@$]", "_").replace("__", "_").replaceAll("['\"]", "") : "";
    }

    public static boolean isRequisitoFuncional(TipoAtividade tipoAtividade) {
        return ((tipoAtividade != null) && (tipoAtividade.equals(TipoAtividade.User)
                || tipoAtividade.equals(TipoAtividade.Script)
                || tipoAtividade.equals(TipoAtividade.Service)));
    }

    public static boolean isRegraDeNegocio(DirecaoGateway direcaoGateway) {
        return direcaoGateway != null && direcaoGateway.equals(DirecaoGateway.Diverging);
    }

    public static boolean isRequisitoFuncional(String valorCampo) {
        return (valorCampo != null && !valorCampo.isEmpty()
                && (valorCampo.toUpperCase().equals("RF")
                || valorCampo.toUpperCase().equals("REQUISITO FUNCIONAL")
                || valorCampo.toUpperCase().equals("REQUISITOFUNCIONAL")));
    }

    public static boolean isRequisitoNaoFuncional(String valorCampo) {
        return (valorCampo != null && !valorCampo.isEmpty()
                && (valorCampo.toUpperCase().equals("RNF")
                || valorCampo.toUpperCase().equals("REQUISITO NAO FUNCIONAL")
                || valorCampo.toUpperCase().equals("REQUISITO NÃO FUNCIONAL")
                || valorCampo.toUpperCase().equals("REQUISITONAOFUNCIONAL")));
    }

    public static boolean isRegraDeNegocio(String valorCampo) {
        return (valorCampo != null && !valorCampo.isEmpty()
                && (valorCampo.toUpperCase().equals("RN") || valorCampo.toUpperCase().equals("REGRA NEGOCIO")
                || (valorCampo.toUpperCase().equals("REGRA"))
                || valorCampo.toUpperCase().equals("REGRA DE NEGOCIO")
                || valorCampo.toUpperCase().equals("REGRA DE NEGÓCIO")));
    }

    public static boolean verificaDecisaoSim(String valorCampo) {
        return (valorCampo != null && !valorCampo.isEmpty()
                && (valorCampo.toUpperCase().equals("SIM") || valorCampo.toUpperCase().equals("S")
                || valorCampo.toUpperCase().equals("YES") || valorCampo.toUpperCase().equals("Y")));
    }

    public static boolean verificaDecisaoNao(String valorCampo) {
        return (valorCampo != null && !valorCampo.isEmpty()
                && (valorCampo.toUpperCase().equals("NAO") || valorCampo.toUpperCase().equals("NÃO")
                || valorCampo.toUpperCase().equals("N") || valorCampo.toUpperCase().equals("NO")));
    }

}
