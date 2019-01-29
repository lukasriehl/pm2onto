package com.lukas.pm2onto.utils;

import com.lukas.pm2onto.model.Artefato;
import com.lukas.pm2onto.model.Atividade;
import com.lukas.pm2onto.model.AtributoEstendido;
import com.lukas.pm2onto.model.Elemento;
import com.lukas.pm2onto.model.Evento;
import com.lukas.pm2onto.model.Gateway;
import com.lukas.pm2onto.model.Processo;
import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String formataNome(String nome) {
        return nome != null && !nome.isEmpty()
                ? nome.replaceAll("[?\\/\\s():,;@$]", "_").replace("__", "_").replaceAll("['\"]", "") : "";
    }

    public static String formataNome(String nome, boolean mantemEspacosEmBranco) {
        if (mantemEspacosEmBranco) {
            return nome != null && !nome.isEmpty()
                    ? nome.replaceAll("[?\\/():,;@$]", "_").replace("__", "_").replaceAll("['\"]", "") : "";
        } else {
            return formataNome(nome);
        }
    }

    public static boolean isRequisitoFuncional(TipoAtividade tipoAtividade) {
        return tipoAtividade != null && Arrays.asList(TipoAtividade.User,
                TipoAtividade.Script, TipoAtividade.Service)
                .contains(tipoAtividade);
    }

    public static boolean isRegraDeNegocio(DirecaoGateway direcaoGateway) {
        return direcaoGateway != null && direcaoGateway.equals(DirecaoGateway.Diverging);
    }

    public static boolean isRequisitoFuncional(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("RF", "REQUISITO FUNCIONAL", "REQUISITOFUNCIONAL",
                        "FR", "FUNCTIONAL REQUIREMENT", "FUNCTIONALREQUIREMENT")
                        .contains(valorCampo.toUpperCase());
    }

    public static boolean isRequisitoNaoFuncional(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("RNF", "REQUISITO NAO FUNCIONAL", "REQUISITO NÃO FUNCIONAL",
                        "REQUISITONAOFUNCIONAL", "NFR", "NON FUNCTIONAL REQUIREMENT")
                        .contains(valorCampo.toUpperCase());
    }

    public static boolean isRegraDeNegocio(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("RN", "REGRANEGOCIO", "REGRA NEGOCIO", "REGRA DE NEGÓCIO",
                        "REGRA", "BR", "BUSINESS RULE").contains(valorCampo.toUpperCase());
    }

    public static boolean verificaDecisaoSim(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("SIM", "S", "YES", "Y").contains(valorCampo.toUpperCase());
    }

    public static boolean verificaDecisaoNao(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("NAO", "N", "NÃO", "NO").contains(valorCampo.toUpperCase());
    }

    public static boolean isAtributoComposto(String valorCampo) {
        return valorCampo != null && !valorCampo.isEmpty()
                && Arrays.asList("ATRIBUTO COMPOSTO", "COMPOSED ATTRIBUTE", "ATRIBUTOCOMPOSTO",
                        "COMPOSEDATTRIBUTE", "ATRIBUTOS", "ATTRIBUTES", "ATTRS")
                        .contains(valorCampo.toUpperCase());
    }

    public static Map<String, Object> retornaAtributos(String valorAtributoComposto) {
        String valorAtributoCompostoFormatted;

        Pattern REMOVE_TAGS = Pattern.compile("((&lt;).+?(?<=&quot;&gt;))|((&lt;).+?(?<=&gt;))|(\\n)");

        Matcher m = REMOVE_TAGS.matcher(valorAtributoComposto);

        valorAtributoCompostoFormatted = m.replaceAll("");

        String atributosStr[] = valorAtributoCompostoFormatted.lastIndexOf(";")
                == valorAtributoCompostoFormatted.length() - 1
                ? valorAtributoCompostoFormatted.substring(0,
                        valorAtributoCompostoFormatted.length() - 1).split(";")
                : valorAtributoCompostoFormatted.split(";");

        if (atributosStr.length > 0) {
            Map<String, Object> atributos = new HashMap();
            String detalhesAtributosStr[];

            for (int i = 0; i < atributosStr.length; i++) {
                if (atributosStr[i].contains(":")) {
                    detalhesAtributosStr = atributosStr[i].split(":");

                    atributos.put(detalhesAtributosStr[0],
                            detalhesAtributosStr[1].concat(detalhesAtributosStr.length >= 3
                                    ? "&".concat(detalhesAtributosStr[2]) : ""));
                }
            }

            return atributos;
        } else {
            return null;
        }
    }

    public static String geraNomeDetalhadoElemento(String nomeElemento, Processo processo) {
        String nomeDetalhadoReturn = null;

        if (nomeElemento != null && !nomeElemento.isEmpty()) {
            nomeDetalhadoReturn = nomeElemento.concat(processo.getNome() != null && !processo.getNome().isEmpty()
                    ? " for the Process ".concat(processo.getNome()) : " for the Process ".concat(processo.getIdElemento()));
            nomeDetalhadoReturn = nomeDetalhadoReturn.substring(0, 1).toUpperCase()
                    .concat(nomeDetalhadoReturn.substring(1));
        }

        return nomeDetalhadoReturn == null ? "No Name" : formataNome(nomeDetalhadoReturn, true);
    }

    public static String geraNomeAtributoEstendido(AtributoEstendido atributoEstendido, Elemento elementoPai) {
        StringBuilder sbNomeAtributo = new StringBuilder("extAttr-");

        sbNomeAtributo.append(atributoEstendido.getNome() != null
                && !atributoEstendido.getNome().isEmpty() ? formataNome(atributoEstendido.getNome().substring(0, 1)
                .toUpperCase().concat(atributoEstendido.getNome().substring(1)))
                : elementoPai.getIdElemento());
        sbNomeAtributo.append("_for_");
        sbNomeAtributo.append(elementoPai instanceof Atividade ? "actv-"
                : elementoPai instanceof Evento ? "evt-"
                        : elementoPai instanceof Gateway ? "gatw"
                                : elementoPai instanceof Artefato ? "artft"
                                        : "");
        sbNomeAtributo.append(elementoPai.getNome() != null
                && !elementoPai.getNome().isEmpty() ? formataNome(elementoPai.getNome())
                : elementoPai.getIdElemento());

        return sbNomeAtributo.toString();
    }

    public static String geraNomeDetalhadoAtributoEstendido(String nomeAtributo, Elemento elementoPai,
            Processo processo) {
        String nomeDetalhadoReturn = null;

        if (nomeAtributo != null && !nomeAtributo.isEmpty()) {
            String textoElemento = " for the "
                    .concat(elementoPai instanceof Atividade ? "Activity"
                            : elementoPai instanceof Evento ? "Event"
                                    : elementoPai instanceof Gateway ? "Gateway"
                                            : elementoPai instanceof Artefato ? "Artifact"
                                                    : "Element").concat(" ");
            nomeDetalhadoReturn = nomeAtributo.concat(processo.getNome() != null && !processo.getNome().isEmpty()
                    ? textoElemento.concat(elementoPai.getNome() != null
                            && !elementoPai.getNome().isEmpty()
                            ? elementoPai.getNome() : elementoPai.getIdElemento())
                            .concat(" in the Process ").concat(processo.getNome())
                    : textoElemento.concat(elementoPai.getNome() != null
                            && !elementoPai.getNome().isEmpty()
                            ? elementoPai.getNome() : elementoPai.getIdElemento())
                            .concat(" in the Process ").concat(processo.getIdElemento()));

            nomeDetalhadoReturn = nomeDetalhadoReturn.substring(0, 1).toUpperCase()
                    .concat(nomeDetalhadoReturn.substring(1));
        }

        return nomeDetalhadoReturn == null ? "None" : formataNome(nomeDetalhadoReturn, true);
    }
}
