package com.lukas.pm2onto.bo;

import com.lukas.pm2onto.dao.OntologiaDAO;
import com.lukas.pm2onto.dto.ConsultaDTO;
import com.lukas.pm2onto.model.Ontologia;
import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import com.lukas.pm2onto.model.enumerador.TipoEvento;
import com.lukas.pm2onto.model.enumerador.TipoGateway;
import com.lukas.pm2onto.model.enumerador.TipoSubProcesso;
import com.lukas.pm2onto.tiposconsultas.AtributoElemento;
import com.lukas.pm2onto.tiposconsultas.SubTipoElemento;
import com.lukas.pm2onto.tiposconsultas.TipoConsulta;
import com.lukas.pm2onto.tiposconsultas.TipoElemento;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;

/**
 *
 * @author lukas
 */
public class ConsultaOntologiaBO {

    private Ontologia ontologiaSelected;
    private OntModel modeloOntologiaProcesso;
    private List<ConsultaDTO> consultaDTOList;

    public List<TipoConsulta> retornaTiposConsultas() {
        List<TipoConsulta> tipoConsultaList = new ArrayList();

        TipoConsulta tipoConsulta = new TipoConsulta(Short.valueOf("0"), "Básica", "Basic");
        tipoConsultaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("1"), "Avançada", "Advanced");
        tipoConsultaList.add(tipoConsulta);

        return tipoConsultaList;
    }

    public List<TipoElemento> retornaTiposElementos(boolean consideraAtor, boolean consideraPool) {
        List<TipoElemento> tipoElementoList = new ArrayList();

        TipoElemento tipoElemento = new TipoElemento(Short.valueOf("0"), "Activity");
        tipoElementoList.add(tipoElemento);
        tipoElemento = new TipoElemento(Short.valueOf("1"), "Subprocess");
        tipoElementoList.add(tipoElemento);
        tipoElemento = new TipoElemento(Short.valueOf("2"), "Event");
        tipoElementoList.add(tipoElemento);
        tipoElemento = new TipoElemento(Short.valueOf("3"), "Gateway");
        tipoElementoList.add(tipoElemento);
        tipoElemento = new TipoElemento(Short.valueOf("4"), "DataObject");
        tipoElementoList.add(tipoElemento);
        tipoElemento = new TipoElemento(Short.valueOf("5"), "DataStore");
        tipoElementoList.add(tipoElemento);
        if (consideraAtor) {
            tipoElemento = new TipoElemento(Short.valueOf("6"), "Actor");
            tipoElementoList.add(tipoElemento);
        }
        if (consideraPool) {
            tipoElemento = new TipoElemento(Short.valueOf("7"), "Pool");
            tipoElementoList.add(tipoElemento);
        }

        return tipoElementoList;
    }

    public List<SubTipoElemento> retornaSubTiposElementos() {
        List<SubTipoElemento> subTipoElementoList = new ArrayList();
        SubTipoElemento subTipoElemento;

        for (TipoAtividade tipAti : TipoAtividade.values()) {
            subTipoElemento = new SubTipoElemento(tipAti.getId(), tipAti.name(), null,
                    "actv-".concat(tipAti.name()));
            subTipoElementoList.add(subTipoElemento);
        }

        for (TipoSubProcesso tipSub : TipoSubProcesso.values()) {
            subTipoElemento = new SubTipoElemento(tipSub.getId(), tipSub.name(), null,
                    "subProcess-".concat(tipSub.name()));
            subTipoElementoList.add(subTipoElemento);
        }

        for (TipoEvento tipEve : TipoEvento.values()) {
            subTipoElemento = new SubTipoElemento(tipEve.getId(), tipEve.name(), null,
                    "evt-".concat(tipEve.name()));
            subTipoElementoList.add(subTipoElemento);
        }

        for (TipoGateway tipGat : TipoGateway.values()) {
            subTipoElemento = new SubTipoElemento(tipGat.getId(), tipGat.name(), null,
                    "gatw-".concat(tipGat.name()));
            subTipoElementoList.add(subTipoElemento);
        }

        return subTipoElementoList;
    }

    public List<SubTipoElemento> retornaSubTiposElementos(Short idTipoElemento) {
        List<SubTipoElemento> subTipoElementoList;
        SubTipoElemento subTipoElemento;

        switch (idTipoElemento) {
            case 0:
                subTipoElementoList = new ArrayList();

                for (TipoAtividade tipAti : TipoAtividade.values()) {
                    subTipoElemento = new SubTipoElemento(tipAti.getId(), tipAti.name(), null,
                            "actv-".concat(tipAti.name()));
                    subTipoElementoList.add(subTipoElemento);
                }

                break;
            case 1:
                subTipoElementoList = new ArrayList();

                for (TipoSubProcesso tipSub : TipoSubProcesso.values()) {
                    subTipoElemento = new SubTipoElemento(tipSub.getId(), tipSub.name(), null,
                            "subProcess-".concat(tipSub.name()));
                    subTipoElementoList.add(subTipoElemento);
                }

                break;
            case 2:
                subTipoElementoList = new ArrayList();

                for (TipoEvento tipEve : TipoEvento.values()) {
                    subTipoElemento = new SubTipoElemento(tipEve.getId(), tipEve.name(), null,
                            "evt-".concat(tipEve.name()));
                    subTipoElementoList.add(subTipoElemento);
                }

                break;
            case 3:
                subTipoElementoList = new ArrayList();

                for (TipoGateway tipGat : TipoGateway.values()) {
                    subTipoElemento = new SubTipoElemento(tipGat.getId(), tipGat.name(), null,
                            "gatw-");
                    subTipoElementoList.add(subTipoElemento);
                }

                break;
            default:
                subTipoElementoList = null;
                break;
        }

        return subTipoElementoList;
    }

    public List<AtributoElemento> retornaAtributosElementos() {
        List<AtributoElemento> atributoElementoList = new ArrayList();

        AtributoElemento atributoElemento = new AtributoElemento(Short.valueOf("0"), "Id");
        atributoElementoList.add(atributoElemento);
        atributoElemento = new AtributoElemento(Short.valueOf("1"), "Name");
        atributoElementoList.add(atributoElemento);
        atributoElemento = new AtributoElemento(Short.valueOf("2"), "Description");
        atributoElementoList.add(atributoElemento);
        atributoElemento = new AtributoElemento(Short.valueOf("3"), "Documentation");
        atributoElementoList.add(atributoElemento);
        atributoElemento = new AtributoElemento(Short.valueOf("4"), "All");
        atributoElementoList.add(atributoElemento);

        return atributoElementoList;
    }

    public List<TipoConsulta> retornaTiposConsultasIntermediarias() {
        List<TipoConsulta> tipoConsultaIntermediariaList = new ArrayList();

        TipoConsulta tipoConsulta = new TipoConsulta(Short.valueOf("0"), "Atividades/Subprocessos executados por um determinado ator",
                "Activities/Subprocesses performed by a specific actor");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("1"), "Predecessor/Sucessor de um elemento filtrado na consulta",
                "Predecessor/successor element of a filtered element in the query");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("2"), "Elementos que realizam trocas de mensagens",
                "Elements that perform messages exchanges");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("3"), "Elementos identificados como requisitos funcionais",
                "Elements identified as functional requirements");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("4"), "Elementos identificados como requisitos não-funcionais",
                "Elements identified as non-functional requirements");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("5"), "Elementos identificados como regras de negocio",
                "Elements identified as business rules");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("6"), "Elementos que utilizam um artefato como entrada",
                "Elements that use an artifact as input");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("7"), "Atividades que produzem um artefato como saida",
                "Activities that produce an artifact as output");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("8"), "Atividades precedidas por um gateway exclusivo",
                "Activities preceded by an exclusive gateway");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("9"), "Atividades precedidas por um gateway inclusivo",
                "Activities preceded by an inclusive gateway");
        tipoConsultaIntermediariaList.add(tipoConsulta);
        tipoConsulta = new TipoConsulta(Short.valueOf("10"), "Atividades precedidas por um gateway paralelo",
                "Activities preceded by a parallel gateway");
        tipoConsultaIntermediariaList.add(tipoConsulta);

        return tipoConsultaIntermediariaList;
    }

    public List<Ontologia> retornaOntologias() throws Exception {
        List<Ontologia> ontologiaList = null;
        OntologiaDAO dao = new OntologiaDAO();

        try {
            ontologiaList = dao.findAll();
        } catch (Exception e) {
            throw new Exception(e.getCause());
        }

        return ontologiaList;
    }

    public void limpaOntologia() {
        this.ontologiaSelected = null;
        this.modeloOntologiaProcesso = null;
    }

    public void efetuaPesquisaBasica(AtributoElemento atributoElemento, TipoElemento tipoElemento,
            SubTipoElemento subTipoElemento, String filtroPesquisa) throws Exception {
        try {
            String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
            String NS = fonte + "#";
            StringBuilder queryStr;
            Literal id, name, description, documentation;
            String tipo, subTipo;
            ConsultaDTO consultaDTO;
            boolean typeIsResource = false, subTypeIsResource = false;

            //Carrega o modelo da ontologia para consulta
            carregaModeloOntologia();

            consultaDTOList = new ArrayList();

            queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
            queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
            queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
            queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
            queryStr.append("PREFIX j.0: <").append(NS).append("> ");
            queryStr.append("SELECT ?id ?name ?description ?documentation ?type ?sub_type ");
            queryStr.append("WHERE { ");

            //Filtra as classes pelo tipo
            if ((tipoElemento != null && tipoElemento.getNome() != null
                    && !tipoElemento.getNome().isEmpty()) || (subTipoElemento != null && subTipoElemento.getNomeComPrefixo() != null
                    && !subTipoElemento.getNomeComPrefixo().isEmpty())) {
                if (subTipoElemento == null) {
                    if (tipoElemento != null && tipoElemento.getNome() != null && !tipoElemento.getNome().isEmpty()) {
                        queryStr.append("?x rdfs:subClassOf ?sub_type . ");
                        if (tipoElemento.getNome().equals("Actor") || tipoElemento.getNome().equals("Pool")) {
                            queryStr.append("?sub_type rdfs:subClassOf j.0:").append("ProcessElements").append("").append(" . ");
                        } else {
                            queryStr.append("?sub_type rdfs:subClassOf j.0:").append(tipoElemento.getNome()).append("").append(" . ");
                        }
                        queryStr.append("BIND (('").append(tipoElemento.getNome()).append("') AS ?type)  ");
                        typeIsResource = false;
                        subTypeIsResource = true;
                    }
                } else if (subTipoElemento.getNomeComPrefixo() != null && !subTipoElemento.getNomeComPrefixo().isEmpty()) {
                    queryStr.append("?x rdfs:subClassOf j.0:").append(subTipoElemento.getNomeComPrefixo()).append("").append(" . ");
                    queryStr.append("BIND (('").append(subTipoElemento.getNomeComPrefixo()).append("') AS ?sub_type)  ");
                    queryStr.append("j.0:").append(subTipoElemento.getNomeComPrefixo()).append(" rdfs:subClassOf ?type . ");
                    typeIsResource = true;
                    subTypeIsResource = false;
                }
            } else {
                queryStr.append("?x rdfs:subClassOf ?sub_type . ");
                queryStr.append("?sub_type rdfs:subClassOf ?type . ");
                typeIsResource = true;
                subTypeIsResource = true;
            }

            queryStr.append("?x j.0:i1-id            ?id . ");
            queryStr.append("?x j.0:i2-name          ?name . ");
            queryStr.append("?x j.0:i3-description   ?description . ");
            queryStr.append("?x j.0:i4-documentation ?documentation . ");

            //Filta o atributo escolhido.
            if (filtroPesquisa != null && !filtroPesquisa.isEmpty()) {
                if (atributoElemento != null && atributoElemento.getId() != null && atributoElemento.getNome() != null
                        && !atributoElemento.getNome().isEmpty()) {
                    if (atributoElemento.getId() == 4) {
                        //Busca por todos os atributos
                        queryStr.append("FILTER (regex(?id, '").append(filtroPesquisa)
                                .append("') || regex(?name, '").append(filtroPesquisa).append("') || regex(?description, '")
                                .append(filtroPesquisa).append("') || regex(?documentation, '").append(filtroPesquisa).append("'))");
                    } else {
                        queryStr.append("FILTER regex(?").append(atributoElemento.getNome().toLowerCase()).append(", '")
                                .append(filtroPesquisa).append("')");
                    }
                }
            }
            queryStr.append("} ");

            Query query = QueryFactory.create(queryStr.toString());

            try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
                ResultSet rs = queryExec.execSelect();

                while (rs.hasNext()) {
                    QuerySolution sol = rs.nextSolution();

                    id = sol.getLiteral("id");
                    name = sol.getLiteral("name");
                    description = sol.getLiteral("description");
                    documentation = sol.getLiteral("documentation");
                    tipo = typeIsResource ? sol.getResource("type") != null ? sol.getResource("type").getLocalName()
                            : null : sol.getLiteral("type") != null ? sol.getLiteral("type").getString() : null;
                    subTipo = subTypeIsResource ? sol.getResource("sub_type") != null ? sol.getResource("sub_type").getLocalName()
                            : null : sol.getLiteral("sub_type") != null ? sol.getLiteral("sub_type").getString() : null;

                    if (subTipo != null) {
                        subTipo = subTipo.substring(subTipo.indexOf("-") + 1, subTipo.length());
                    }

                    consultaDTO = new ConsultaDTO(id != null ? id.getString() : null, name != null ? name.getString() : null,
                            description != null ? description.getString() : null, documentation != null ? documentation.getString() : null,
                            tipo, subTipo);
                    consultaDTOList.add(consultaDTO);
                }
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void efetuaPesquisaAvancada(TipoConsulta tipoConsultaIntermediariaSelected, String filtroAtorAtividade,
            TipoElemento elementoOrigemSelected, TipoElemento elementoDestinoSelected) throws Exception {
        try {
            //Carrega o modelo da ontologia para consulta
            carregaModeloOntologia();

            consultaDTOList = new ArrayList();

            //Consultas que retornam as informações de dois elementos relacionados.
            if (tipoConsultaIntermediariaSelected.getId() == 0 || tipoConsultaIntermediariaSelected.getId() == 1
                    || tipoConsultaIntermediariaSelected.getId() == 2 || tipoConsultaIntermediariaSelected.getId() == 6
                    || tipoConsultaIntermediariaSelected.getId() == 7 || tipoConsultaIntermediariaSelected.getId() == 8
                    || tipoConsultaIntermediariaSelected.getId() == 9 || tipoConsultaIntermediariaSelected.getId() == 10) {
                switch (tipoConsultaIntermediariaSelected.getId()) {
                    case 0:
                        efetuaPesquisaAtividadesPorAtor(filtroAtorAtividade);
                        break;
                    case 1:
                        efetuaPesquisaSucessoresPredecessores(elementoOrigemSelected, elementoDestinoSelected);
                        break;
                    case 2:
                        efetuaPesquisaTrocasDeMensagens(elementoOrigemSelected, elementoDestinoSelected);
                        break;
                    case 6:
                        efetuaPesquisaElementosUtilizamEntrada(elementoOrigemSelected, filtroAtorAtividade);
                        break;
                    case 7:
                        efetuaPesquisaAtividadesProduzemSaida(filtroAtorAtividade);
                        break;
                    case 8:
                        efetuaPesquisaAtividadesPrecedidasPorGateways(Short.valueOf("8"), filtroAtorAtividade);
                        break;
                    case 9:
                        efetuaPesquisaAtividadesPrecedidasPorGateways(Short.valueOf("9"), filtroAtorAtividade);
                        break;
                    case 10:
                        efetuaPesquisaAtividadesPrecedidasPorGateways(Short.valueOf("10"), filtroAtorAtividade);
                        break;
                    default:
                        break;
                }
                //Consultas sobre Regras de Negócio, Requisitos Funcionais e Não-Funcionais
            } else if (tipoConsultaIntermediariaSelected.getId() == 3 || tipoConsultaIntermediariaSelected.getId() == 4
                    || tipoConsultaIntermediariaSelected.getId() == 5) {
                efetuaPesquisaRequisitos(tipoConsultaIntermediariaSelected);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void efetuaPesquisaAtividadesPorAtor(String filtroAtor) throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, targetName, targetDescription, targetType, superType;
        String sourceType;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("PREFIX j.1: <").append(NS).append("isPerformedBy>");
        queryStr.append("SELECT ?source_name ?source_description ?source_type ?source_super_type ?target_name ?target_description "
                + "?target_type ");
        queryStr.append("WHERE { ");
        //Atividades
        queryStr.append("{ ?x rdfs:subClassOf ?source_type . ");
        queryStr.append("?source_type rdfs:subClassOf j.0:Activity . ");
        queryStr.append("?x j.0:i2-name        ?source_name . ");
        queryStr.append("?x j.0:i3-description ?source_description . ");
        queryStr.append("BIND (('Activity') AS      ?source_super_type) ");
        queryStr.append("?x rdfs:subClassOf    ?activity_restriction . ");
        queryStr.append("?activity_restriction owl:onProperty j.1: . ");
        queryStr.append("?activity_restriction owl:allValuesFrom ?a . ");
        queryStr.append("?a j.0:i2-name        ?target_name . ");
        queryStr.append("?a j.0:i3-description ?target_description . ");
        queryStr.append("BIND (('Actor') AS     ?target_type) ");

        if (filtroAtor != null && !filtroAtor.isEmpty()) {
            queryStr.append("FILTER(regex(?target_name, '").append(filtroAtor).append("') || regex(?target_description, '")
                    .append(filtroAtor).append("'))");
        }

        queryStr.append("} ");

        queryStr.append(" UNION ");
        //Subprocessos
        queryStr.append("{ ?x rdfs:subClassOf ?source_type . ");
        queryStr.append("?source_type rdfs:subClassOf j.0:SubProcess . ");
        queryStr.append("?x j.0:i2-name          ?source_name . ");
        queryStr.append("?x j.0:i3-description   ?source_description . ");
        queryStr.append("BIND (('SubProcess') AS      ?source_super_type) ");
        queryStr.append("?x rdfs:subClassOf      ?activity_restriction . ");
        queryStr.append("?activity_restriction owl:onProperty j.1: . ");
        queryStr.append("?activity_restriction owl:allValuesFrom ?a . ");
        queryStr.append("?a j.0:i2-name          ?target_name . ");
        queryStr.append("?a j.0:i3-description   ?target_description . ");
        queryStr.append("BIND (('Actor') AS      ?target_type) ");

        if (filtroAtor != null && !filtroAtor.isEmpty()) {
            queryStr.append("FILTER(regex(?target_name, '").append(filtroAtor).append("') || regex(?target_description, '")
                    .append(filtroAtor).append("'))");
        }

        queryStr.append("} ");

        queryStr.append("} ");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sol.getResource("source_type") != null ? sol.getResource("source_type").getLocalName()
                        : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = sol.getLiteral("target_type");
                superType = sol.getLiteral("source_super_type");

                if (sourceType != null) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricao(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipo(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(superType != null && !superType.getString().isEmpty()
                        ? superType.getString() : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null ? targetType.toString() : null);

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void efetuaPesquisaTrocasDeMensagens(TipoElemento elementoOrigemSelected, TipoElemento elementoDestinoSelected)
            throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, targetName, targetDescription;
        String sourceType, targetType, sourceSuperType, targetSuperType;
        ConsultaDTO consultaDTO;
        boolean sourceSubTypeIsResource = false, targetSubTypeIsResource = false;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("PREFIX j.1: <").append(NS).append("communicatesWith>");
        queryStr.append("SELECT DISTINCT ?source_name ?source_description ?source_type ?source_super_type "
                + "?target_name ?target_description ?target_type ?target_super_type ");
        queryStr.append("WHERE { ");

        if (elementoOrigemSelected != null) {
            if (elementoOrigemSelected.getId() == 6 || elementoOrigemSelected.getId() == 7) {
                if (elementoOrigemSelected.getId() == 6) {
                    queryStr.append("?x rdfs:subClassOf j.0:Actor . ");
                    queryStr.append("BIND (('Actor') AS ?source_type) ");
                } else {
                    queryStr.append("?x rdfs:subClassOf j.0:Pool . ");
                    queryStr.append("BIND (('Pool') AS ?source_type) ");
                }
            } else {
                queryStr.append("?x rdfs:subClassOf    ?source_type . ");
                queryStr.append("?source_type rdfs:subClassOf j.0:").append(elementoOrigemSelected.getNome()).append(" . ");
                queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
                sourceSubTypeIsResource = true;
            }
        } else {
            queryStr.append("?x rdfs:subClassOf    ?source_type . ");
            queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
            sourceSubTypeIsResource = true;
        }

        queryStr.append("?x j.0:i2-name        ?source_name . ");
        queryStr.append("?x j.0:i3-description ?source_description . ");
        queryStr.append("?x rdfs:subClassOf    ?communicates_with . ");
        queryStr.append("?communicates_with    owl:onProperty j.1: . ");
        queryStr.append("?communicates_with    owl:allValuesFrom ?a . ");
        queryStr.append("?a j.0:i2-name        ?target_name . ");
        queryStr.append("?a j.0:i3-description ?target_description . ");

        if (elementoDestinoSelected != null) {
            if (elementoDestinoSelected.getId() == 6 || elementoDestinoSelected.getId() == 7) {
                if (elementoDestinoSelected.getId() == 6) {
                    queryStr.append("?a rdfs:subClassOf j.0:Actor . ");
                    queryStr.append("BIND (('Actor') AS ?target_type) ");
                } else {
                    queryStr.append("?a rdfs:subClassOf j.0:Pool . ");
                    queryStr.append("BIND (('Pool') AS ?target_type) ");
                }
            } else {
                queryStr.append("?a rdfs:subClassOf    ?target_type . ");
                queryStr.append("?target_type rdfs:subClassOf j.0:").append(elementoDestinoSelected.getNome()).append(" . ");
                queryStr.append("?target_type rdfs:subClassOf ?target_super_type . ");
                targetSubTypeIsResource = true;
            }
        } else {
            queryStr.append("?a rdfs:subClassOf    ?target_type . ");
            queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
            targetSubTypeIsResource = true;
        }

        queryStr.append("}");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sourceSubTypeIsResource ? sol.getResource("source_type") != null
                        ? sol.getResource("source_type").getLocalName() : null
                        : sol.getLiteral("source_type") != null ? sol.getLiteral("type").getString() : null;
                sourceSuperType = sourceSubTypeIsResource ? sol.getResource("source_super_type") != null
                        ? sol.getResource("source_super_type").getLocalName() : null
                        : sol.getLiteral("source_super_type") != null ? sol.getLiteral("source_super_type").getString() : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = targetSubTypeIsResource ? sol.getResource("target_type") != null
                        ? sol.getResource("target_type").getLocalName() : null
                        : sol.getLiteral("target_type") != null ? sol.getLiteral("target_type").getString() : null;
                targetSuperType = targetSubTypeIsResource ? sol.getResource("target_super_type") != null
                        ? sol.getResource("target_super_type").getLocalName() : null
                        : sol.getLiteral("target_super_type") != null ? sol.getLiteral("target_super_type").getString() : null;

                if (sourceSubTypeIsResource && sourceType != null && !sourceType.equals("Actor")
                        && !sourceType.equals("Pool")) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                if (targetSubTypeIsResource && targetType != null && !targetType.equals("Actor")
                        && !targetType.equals("Pool")) {
                    targetType = targetType.substring(targetType.indexOf("-") + 1, targetType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricao(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipo(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(sourceSuperType != null && !sourceSuperType.isEmpty()
                        ? sourceSuperType : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null && !targetType.isEmpty()
                        ? targetType.concat(" ").concat(targetSuperType != null && !targetSuperType.isEmpty()
                        ? targetSuperType : "") : "");

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void efetuaPesquisaSucessoresPredecessores(TipoElemento elementoOrigemSelected, TipoElemento elementoDestinoSelected)
            throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, mainName, mainDescription, targetName, targetDescription;
        String sourceType, mainType, targetType, sourceSuperType, mainSuperType, targetSuperType;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("PREFIX j.1: <").append(NS).append("isPrecededBy>");
        queryStr.append("PREFIX j.2: <").append(NS).append("isSucceededBy>");
        queryStr.append("SELECT DISTINCT ?source_name ?source_description ?source_super_type ?source_type")
                .append(" ?main_name ?main_description ?main_type ?main_super_type ?target_name ?target_description "
                        + "?target_type ?target_super_type ");
        queryStr.append("WHERE { ");
        queryStr.append("?x rdfs:subClassOf    ?main_type . ");
        queryStr.append("?main_type   rdfs:subClassOf ?main_super_type . ");
        queryStr.append("?main_super_type rdfs:subClassOf j.0:ProcessElements . ");
        queryStr.append("?x j.0:i2-name        ?main_name . ");
        queryStr.append("?x j.0:i3-description ?main_description . ");
        //Elemento predecessor
        queryStr.append("?x rdfs:subClassOf    ?is_preceded_by . ");
        queryStr.append("?is_preceded_by owl:onProperty j.1: . ");
        queryStr.append("?is_preceded_by owl:allValuesFrom ?p . ");
        queryStr.append("?p j.0:i2-name        ?source_name . ");
        queryStr.append("?p j.0:i3-description ?source_description . ");
        queryStr.append("?p rdfs:subClassOf    ?source_type . ");

        if (elementoOrigemSelected != null && elementoOrigemSelected.getNome() != null
                && !elementoOrigemSelected.getNome().isEmpty()) {
            queryStr.append("?source_type rdfs:subClassOf j.0:")
                    .append(elementoOrigemSelected.getNome()).append(" . ");
            queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
        } else {
            queryStr.append("?source_type   rdfs:subClassOf ?source_super_type . ");
            queryStr.append("?source_super_type rdfs:subClassOf j.0:ProcessElements . ");
        }

        //Elemento sucessor
        queryStr.append("?x rdfs:subClassOf    ?is_succeeded_by . ");
        queryStr.append("?is_succeeded_by owl:onProperty j.2: . ");
        queryStr.append("?is_succeeded_by owl:allValuesFrom ?s . ");
        queryStr.append("?s j.0:i2-name        ?target_name . ");
        queryStr.append("?s j.0:i3-description ?target_description . ");
        queryStr.append("?s rdfs:subClassOf    ?target_type . ");

        if (elementoDestinoSelected != null && elementoDestinoSelected.getNome() != null
                && !elementoDestinoSelected.getNome().isEmpty()) {
            queryStr.append("?target_type rdfs:subClassOf j.0:")
                    .append(elementoDestinoSelected.getNome()).append(" . ");
            queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
        } else {
            queryStr.append("?target_type   rdfs:subClassOf ?target_super_type . ");
            queryStr.append("?target_super_type rdfs:subClassOf j.0:ProcessElements ");
        }

        queryStr.append(" } ");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sol.getResource("source_type") != null ? sol.getResource("source_type").getLocalName() : null;
                sourceSuperType = sol.getResource("source_super_type") != null ? sol.getResource("source_super_type").getLocalName() : null;
                mainName = sol.getLiteral("main_name");
                mainDescription = sol.getLiteral("main_description");
                mainType = sol.getResource("main_type") != null ? sol.getResource("main_type").getLocalName() : null;
                mainSuperType = sol.getResource("main_super_type") != null ? sol.getResource("main_super_type").getLocalName() : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = sol.getResource("target_type") != null ? sol.getResource("target_type").getLocalName() : null;
                targetSuperType = sol.getResource("target_super_type") != null ? sol.getResource("target_super_type").getLocalName() : null;

                if (sourceType != null) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                if (mainType != null) {
                    mainType = mainType.substring(mainType.indexOf("-") + 1, mainType.length());
                }

                if (targetType != null) {
                    targetType = targetType.substring(targetType.indexOf("-") + 1, targetType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNomeAnt(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricaoAnt(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipoAnt(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(sourceSuperType != null && !sourceSuperType.isEmpty()
                        ? sourceSuperType : "") : null);
                consultaDTO.setNome(mainName != null ? mainName.toString() : null);
                consultaDTO.setDescricao(mainDescription != null ? mainDescription.toString() : null);
                consultaDTO.setTipo(mainType != null && !mainType.isEmpty()
                        ? mainType.concat(" ").concat(mainSuperType != null && !mainSuperType.isEmpty()
                        ? mainSuperType : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null && !targetType.isEmpty()
                        ? targetType.concat(" ").concat(targetSuperType != null && !targetSuperType.isEmpty()
                        ? targetSuperType : "") : null);

                consultaDTOList.add(consultaDTO);
            }
        }

    }

    private void efetuaPesquisaElementosUtilizamEntrada(TipoElemento elementoOrigemSelected, String filtroAtorAtividade)
            throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, targetName, targetDescription;
        String sourceType, sourceSuperType, targetType, targetSuperType;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("PREFIX j.1: <").append(NS).append("usesInput>");
        queryStr.append("SELECT DISTINCT ?source_name ?source_description ?source_type ?source_super_type"
                + " ?target_name ?target_description ?target_type ?target_super_type ");
        queryStr.append("WHERE { ");

        if (elementoOrigemSelected != null && elementoOrigemSelected.getId() != null) {
            queryStr.append("?x rdfs:subClassOf    ?source_type . ");
            queryStr.append("?source_type rdfs:subClassOf j.0:").append(elementoOrigemSelected.getId() == 0 ? "Activity" : "Event")
                    .append(" . ");
            queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
            queryStr.append("?x j.0:i2-name         ?source_name . ");
            queryStr.append("?x j.0:i3-description  ?source_description . ");
            if (filtroAtorAtividade != null && !filtroAtorAtividade.isEmpty()) {
                queryStr.append("FILTER(regex(?source_name, '").append(filtroAtorAtividade)
                        .append("') || regex(?source_description, '")
                        .append(filtroAtorAtividade).append("'))");
            }
            queryStr.append("?x rdfs:subClassOf    ?uses_input_restriction . ");
            queryStr.append("?uses_input_restriction owl:onProperty j.1: . ");
            queryStr.append("?uses_input_restriction owl:onClass ?a . ");
            queryStr.append("?a j.0:i2-name        ?target_name . ");
            queryStr.append("?a j.0:i3-description ?target_description . ");
            queryStr.append("?a rdfs:subClassOf    ?target_type . ");
            queryStr.append("?target_type rdfs:subClassOf j.0:Artifact . ");
            queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
        } else {
            //Atividades
            queryStr.append("{ ?x rdfs:subClassOf   ?source_type . ");
            queryStr.append("?source_type rdfs:subClassOf j.0:Activity . ");
            queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
            queryStr.append("?x j.0:i2-name         ?source_name . ");
            queryStr.append("?x j.0:i3-description  ?source_description . ");
            if (filtroAtorAtividade != null && !filtroAtorAtividade.isEmpty()) {
                queryStr.append("FILTER(regex(?source_name, '").append(filtroAtorAtividade)
                        .append("') || regex(?source_description, '")
                        .append(filtroAtorAtividade).append("'))");
            }
            queryStr.append("?x rdfs:subClassOf    ?uses_input_restriction . ");
            queryStr.append("?uses_input_restriction owl:onProperty j.1: . ");
            queryStr.append("?uses_input_restriction owl:onClass ?a . ");
            queryStr.append("?a j.0:i2-name        ?target_name . ");
            queryStr.append("?a j.0:i3-description ?target_description . ");
            queryStr.append("?a rdfs:subClassOf    ?target_type . ");
            queryStr.append("?target_type rdfs:subClassOf j.0:Artifact . ");
            queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
            queryStr.append("} ");
            queryStr.append(" UNION ");
            //Eventos
            queryStr.append("{ ?x rdfs:subClassOf   ?source_type . ");
            queryStr.append("?source_type rdfs:subClassOf j.0:Event . ");
            queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
            queryStr.append("?x j.0:i2-name         ?source_name . ");
            queryStr.append("?x j.0:i3-description  ?source_description . ");
            if (filtroAtorAtividade != null && !filtroAtorAtividade.isEmpty()) {
                queryStr.append("FILTER(regex(?source_name, '").append(filtroAtorAtividade)
                        .append("') || regex(?source_description, '")
                        .append(filtroAtorAtividade).append("'))");
            }
            queryStr.append("?x rdfs:subClassOf    ?uses_input_restriction . ");
            queryStr.append("?uses_input_restriction owl:onProperty j.1: . ");
            queryStr.append("?uses_input_restriction owl:onClass ?a . ");
            queryStr.append("?a j.0:i2-name        ?target_name . ");
            queryStr.append("?a j.0:i3-description ?target_description . ");
            queryStr.append("?a rdfs:subClassOf    ?target_type . ");
            queryStr.append("?target_type rdfs:subClassOf j.0:Artifact . ");
            queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
            queryStr.append("} ");
        }
        queryStr.append(" } ");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sol.getResource("source_type") != null ? sol.getResource("source_type").getLocalName()
                        : null;
                sourceSuperType = sol.getResource("source_super_type") != null ? sol.getResource("source_super_type").getLocalName()
                        : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = sol.getResource("target_type") != null ? sol.getResource("target_type").getLocalName()
                        : null;
                targetSuperType = sol.getResource("target_super_type") != null ? sol.getResource("target_super_type").getLocalName()
                        : null;

                if (sourceType != null) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricao(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipo(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(sourceSuperType != null && !sourceSuperType.isEmpty()
                        ? sourceSuperType : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null && !targetType.isEmpty()
                        ? targetType.concat(" ").concat(targetSuperType != null && !targetSuperType.isEmpty()
                        ? targetSuperType : "") : null);

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void efetuaPesquisaAtividadesProduzemSaida(String filtroAtorAtividade) throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, targetName, targetDescription;
        String sourceType, sourceSuperType, targetType, targetSuperType;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("PREFIX j.1: <").append(NS).append("producesOutput>");
        queryStr.append("SELECT DISTINCT ?source_name ?source_description ?source_type ?source_super_type ?target_name "
                + "?target_description ?target_type ?target_super_type ");
        queryStr.append("WHERE { ");
        queryStr.append("?x rdfs:subClassOf   ?source_type . ");
        queryStr.append("?source_type rdfs:subClassOf j.0:Activity . ");
        queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
        queryStr.append("?x j.0:i2-name         ?source_name . ");
        queryStr.append("?x j.0:i3-description  ?source_description . ");
        queryStr.append("?x rdfs:subClassOf    ?prod_output_restriction . ");
        queryStr.append("?prod_output_restriction owl:onProperty j.1: . ");
        queryStr.append("?prod_output_restriction owl:onClass ?a . ");
        queryStr.append("?a j.0:i2-name        ?target_name . ");
        queryStr.append("?a j.0:i3-description ?target_description . ");
        queryStr.append("?a rdfs:subClassOf    ?target_type . ");
        queryStr.append("?target_type rdfs:subClassOf j.0:Artifact . ");
        queryStr.append("?target_type rdfs:subClassOf ?target_super_type ");
        if (filtroAtorAtividade != null && !filtroAtorAtividade.isEmpty()) {
            queryStr.append("FILTER(regex(?source_name, '").append(filtroAtorAtividade)
                    .append("') || regex(?source_description, '")
                    .append(filtroAtorAtividade).append("'))");
        }
        queryStr.append(" } ");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sol.getResource("source_type") != null ? sol.getResource("source_type").getLocalName()
                        : null;
                sourceSuperType = sol.getResource("source_super_type") != null ? sol.getResource("source_super_type").getLocalName()
                        : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = sol.getResource("target_type") != null ? sol.getResource("target_type").getLocalName()
                        : null;
                targetSuperType = sol.getResource("target_super_type") != null ? sol.getResource("target_super_type").getLocalName()
                        : null;

                if (sourceType != null) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricao(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipo(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(sourceSuperType != null
                        && !sourceSuperType.isEmpty() ? sourceSuperType : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null && !targetType.isEmpty()
                        ? targetType.concat(" ").concat(targetSuperType != null
                        && !targetSuperType.isEmpty() ? targetSuperType : "") : null);

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void efetuaPesquisaAtividadesPrecedidasPorGateways(Short idTipoConsultaIntermediaria,
            String filtroAtorAtividade)
            throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal sourceName, sourceDescription, targetName, targetDescription;
        String sourceType, sourceSuperType, targetType, targetSuperType;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        if (idTipoConsultaIntermediaria == 9 || idTipoConsultaIntermediaria == 10) {
            queryStr.append("PREFIX j.1: <").append(NS).append(idTipoConsultaIntermediaria == 9
                    ? "isExecutedAfterInclusiveExecutionOf" : "isExecutedAfterParallelExecutionOf").append(">");
        }
        queryStr.append("PREFIX j.2: <").append(NS).append("isPrecededBy>");
        queryStr.append("SELECT DISTINCT ?source_name ?source_description ?source_type ?source_super_type ?target_name ?target_description "
                + "?target_type ?target_super_type ");
        queryStr.append("WHERE { ");
        queryStr.append("?x rdfs:subClassOf           ?source_type . ");
        queryStr.append("?source_type rdfs:subClassOf j.0:Activity . ");
        queryStr.append("?source_type rdfs:subClassOf ?source_super_type . ");
        queryStr.append("?x j.0:i2-name               ?source_name . ");
        queryStr.append("?x j.0:i3-description        ?source_description . ");
        if (idTipoConsultaIntermediaria == 9 || idTipoConsultaIntermediaria == 10) {
            queryStr.append("?x rdfs:subClassOf    ?is_exec_after_flow_restr . ");
            queryStr.append("?is_exec_after_flow_restr owl:onProperty j.1: . ");
            queryStr.append("?is_exec_after_flow_restr owl:allValuesFrom ?a . ");
        } else {
            queryStr.append("?x rdfs:subClassOf ?is_exec_when_output . ");
            queryStr.append("?is_exec_when_output owl:allValuesFrom ?a . ");
            queryStr.append("?a rdfs:subClassOf j.0:GatewaysOutputs . ");
        }
        queryStr.append("?x rdfs:subClassOf          ?is_preceded_by_restriction . ");
        queryStr.append("?is_preceded_by_restriction owl:onProperty j.2: . ");
        queryStr.append("?is_preceded_by_restriction owl:allValuesFrom ?g . ");
        queryStr.append("?g rdfs:subClassOf ?target_type . ");
        queryStr.append("?target_type rdfs:subClassOf j.0:Gateway . ");
        queryStr.append("?target_type rdfs:subClassOf ?target_super_type . ");
        queryStr.append("?g j.0:i2-name              ?target_name . ");
        queryStr.append("?g j.0:i3-description       ?target_description ");
        if (filtroAtorAtividade != null && !filtroAtorAtividade.isEmpty()) {
            queryStr.append("FILTER(regex(?source_name, '").append(filtroAtorAtividade)
                    .append("') || regex(?source_description, '")
                    .append(filtroAtorAtividade).append("'))");
        }

        queryStr.append("}");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                sourceName = sol.getLiteral("source_name");
                sourceDescription = sol.getLiteral("source_description");
                sourceType = sol.getResource("source_type") != null ? sol.getResource("source_type").getLocalName() : null;
                sourceSuperType = sol.getResource("source_super_type") != null ? sol.getResource("source_super_type").getLocalName() : null;
                targetName = sol.getLiteral("target_name");
                targetDescription = sol.getLiteral("target_description");
                targetType = sol.getResource("target_type") != null ? sol.getResource("target_type").getLocalName() : null;
                targetSuperType = sol.getResource("target_super_type") != null ? sol.getResource("target_super_type").getLocalName() : null;

                if (sourceType != null) {
                    sourceType = sourceType.substring(sourceType.indexOf("-") + 1, sourceType.length());
                }

                if (targetType != null) {
                    targetType = targetType.substring(targetType.indexOf("-") + 1, targetType.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(sourceName != null ? sourceName.toString() : null);
                consultaDTO.setDescricao(sourceDescription != null ? sourceDescription.toString() : null);
                consultaDTO.setTipo(sourceType != null && !sourceType.isEmpty()
                        ? sourceType.concat(" ").concat(sourceSuperType != null && !sourceSuperType.isEmpty()
                        ? sourceSuperType : "") : null);
                consultaDTO.setNomeDest(targetName != null ? targetName.toString() : null);
                consultaDTO.setDescricaoDest(targetDescription != null ? targetDescription.toString() : null);
                consultaDTO.setTipoDest(targetType != null && !targetType.isEmpty()
                        ? targetType.concat(" ").concat(targetSuperType != null && !targetSuperType.isEmpty()
                        ? targetSuperType : "") : null);

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void efetuaPesquisaRequisitos(TipoConsulta tipoConsultaIntermediariaSelected) throws Exception {
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(ontologiaSelected.getNome());
        String NS = fonte + "#";
        String tipo, superTipo;
        StringBuilder queryStr = new StringBuilder("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ");
        Literal name, description;
        ConsultaDTO consultaDTO;

        queryStr.append("PREFIX owl: <http://www.w3.org/2002/07/owl#> ");
        queryStr.append("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
        queryStr.append("PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ");
        queryStr.append("PREFIX j.0: <").append(NS).append("> ");
        queryStr.append("SELECT DISTINCT ?name ?description ?type ?super_type ");
        queryStr.append("WHERE { ");

        if (null != tipoConsultaIntermediariaSelected.getId()) {
            switch (tipoConsultaIntermediariaSelected.getId()) {
                case 3:
                    queryStr.append("{ ?x rdfs:subClassOf               ?type . ");
                    queryStr.append("?type rdfs:subClassOf j.0:Activity . ");
                    queryStr.append("?type rdfs:subClassOf ?super_type . ");
                    queryStr.append("?x j.0:i2-name                     ?name . ");
                    queryStr.append("?x j.0:i3-description              ?description . ");
                    queryStr.append("?x j.0:i6-isFunctionalRequirement  ?is_functional_requirement ");
                    queryStr.append("FILTER (?is_functional_requirement = 'true'^^xsd:boolean) } ");
                    queryStr.append(" UNION ");
                    queryStr.append("{ ?x rdfs:subClassOf j.0:ExtendedAttribute . ");
                    queryStr.append("?x rdfs:subClassOf ?type . ");
                    queryStr.append("?x j.0:i2-name     ?name . ");
                    queryStr.append("?x j.0:i3-value    ?description . ");
                    queryStr.append("?x j.0:i6-isFunctionalRequirement  ?is_functional_requirement ");
                    queryStr.append("FILTER (?is_functional_requirement = 'true'^^xsd:boolean) } ");
                    break;
                case 4:
                    queryStr.append("?x rdfs:subClassOf ?type . ");
                    queryStr.append("?x rdfs:subClassOf j.0:ExtendedAttribute . ");
                    queryStr.append("?x j.0:i2-name  ?name . ");
                    queryStr.append("?x j.0:i3-value ?description . ");
                    queryStr.append("?x j.0:i6-isNonFunctionalRequirement  ?is_non_functional_requirement ");
                    queryStr.append("FILTER (?is_non_functional_requirement = 'true'^^xsd:boolean) ");
                    break;
                default:
                    //Atividades
                    queryStr.append("{ ?x rdfs:subClassOf ?type . ");
                    queryStr.append("?type rdfs:subClassOf j.0:Activity . ");
                    queryStr.append("?type rdfs:subClassOf ?super_type . ");
                    queryStr.append("?x j.0:i2-name       ?name . ");
                    queryStr.append("?x j.0:businessRule  ?description . ");
                    queryStr.append("?x j.0:i5-isBusinessRule  ?is_business_rule ");
                    queryStr.append("FILTER (?is_business_rule = 'true'^^xsd:boolean) } ");
                    queryStr.append(" UNION ");
                    //Gateways
                    queryStr.append("{ ?x rdfs:subClassOf ?type . ");
                    queryStr.append("?type rdfs:subClassOf j.0:Gateway . ");
                    queryStr.append("?type rdfs:subClassOf ?super_type . ");
                    queryStr.append("?x j.0:i2-name       ?name . ");
                    queryStr.append("?x j.0:businessRule  ?description . ");
                    queryStr.append("?x j.0:i5-isBusinessRule  ?is_business_rule ");
                    queryStr.append("FILTER (?is_business_rule = 'true'^^xsd:boolean) } ");
                    queryStr.append(" UNION ");
                    //Atributos Estendidos
                    queryStr.append("{ ?x rdfs:subClassOf ?type . ");
                    queryStr.append("?x rdfs:subClassOf j.0:ExtendedAttribute . ");
                    queryStr.append("?x j.0:i2-name     ?name . ");
                    queryStr.append("?x j.0:i3-value    ?description . ");
                    queryStr.append("?x j.0:i5-isBusinessRule  ?is_business_rule ");
                    queryStr.append("FILTER (?is_business_rule = 'true'^^xsd:boolean) } ");
                    break;
            }
        }

        queryStr.append("} ");

        Query query = QueryFactory.create(queryStr.toString());

        try (QueryExecution queryExec = QueryExecutionFactory.create(query, modeloOntologiaProcesso)) {
            ResultSet rs = queryExec.execSelect();

            while (rs.hasNext()) {
                QuerySolution sol = rs.nextSolution();

                name = sol.getLiteral("name");
                description = sol.getLiteral("description");
                tipo = sol.getResource("type") != null ? sol.getResource("type").getLocalName()
                        : null;
                superTipo = sol.getResource("super_type") != null ? sol.getResource("super_type").getLocalName()
                        : null;

                if (tipo != null && !tipo.equals("ExtendedAttribute")) {
                    tipo = tipo.substring(tipo.indexOf("-") + 1, tipo.length());
                }

                consultaDTO = new ConsultaDTO();
                consultaDTO.setNome(name != null ? name.getString() : null);
                consultaDTO.setDescricao(description != null ? description.getString() : null);
                consultaDTO.setTipo(tipo != null && !tipo.isEmpty() ? tipo.concat(" ").concat(superTipo != null
                        && !superTipo.isEmpty() ? superTipo : "") : null);

                consultaDTOList.add(consultaDTO);
            }
        }
    }

    private void carregaModeloOntologia() {
        if (modeloOntologiaProcesso == null) {
            modeloOntologiaProcesso = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
            InputStream in = new ByteArrayInputStream(ontologiaSelected.getArquivo());
            modeloOntologiaProcesso.read(new InputStreamReader(in), "RDF/XML");
        }
    }

    public Ontologia getOntologiaSelected() {
        return ontologiaSelected;
    }

    public void setOntologiaSelected(Ontologia ontologiaSelected) {
        this.ontologiaSelected = ontologiaSelected;
    }

    public List<ConsultaDTO> getConsultaDTOList() {
        return consultaDTOList;
    }

    public void setConsultaDTOList(List<ConsultaDTO> consultaDTOList) {
        this.consultaDTOList = consultaDTOList;
    }
}
