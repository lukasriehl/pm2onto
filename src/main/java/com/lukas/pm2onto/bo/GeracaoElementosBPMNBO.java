package com.lukas.pm2onto.bo;

import com.lukas.pm2onto.model.Anotacao;
import com.lukas.pm2onto.model.Artefato;
import com.lukas.pm2onto.model.Atividade;
import com.lukas.pm2onto.model.Ator;
import com.lukas.pm2onto.model.AtributoEstendido;
import com.lukas.pm2onto.model.Elemento;
import com.lukas.pm2onto.model.Evento;
import com.lukas.pm2onto.model.ExecutadoPor;
import com.lukas.pm2onto.model.Gateway;
import com.lukas.pm2onto.model.Grupo;
import com.lukas.pm2onto.model.Processo;
import com.lukas.pm2onto.model.ProduzSaida;
import com.lukas.pm2onto.model.SubProcesso;
import com.lukas.pm2onto.model.SucedidoPor;
import com.lukas.pm2onto.model.UtilizaEntrada;
import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.GatilhoEvento;
import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import com.lukas.pm2onto.model.enumerador.TipoCondicao;
import com.lukas.pm2onto.model.enumerador.TipoEvento;
import com.lukas.pm2onto.model.enumerador.TipoGateway;
import com.lukas.pm2onto.model.enumerador.TipoSubProcesso;
import com.lukas.pm2onto.utils.TextUtils;
import static com.lukas.pm2onto.utils.TextUtils.removeTagsHtml;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBElement;
import org.wfmc._2009.xpdl2.Activities;
import org.wfmc._2009.xpdl2.Activity;
import org.wfmc._2009.xpdl2.ActivitySet;
import org.wfmc._2009.xpdl2.ActivitySets;
import org.wfmc._2009.xpdl2.Artifact;
import org.wfmc._2009.xpdl2.ArtifactInput;
import org.wfmc._2009.xpdl2.Association;
import org.wfmc._2009.xpdl2.BlockActivity;
import org.wfmc._2009.xpdl2.Condition;
import org.wfmc._2009.xpdl2.DataAssociation;
import org.wfmc._2009.xpdl2.DataAssociations;
import org.wfmc._2009.xpdl2.DataObject;
import org.wfmc._2009.xpdl2.DataObjects;
import org.wfmc._2009.xpdl2.DataStoreReference;
import org.wfmc._2009.xpdl2.DataStoreReferences;
import org.wfmc._2009.xpdl2.Description;
import org.wfmc._2009.xpdl2.Documentation;
import org.wfmc._2009.xpdl2.Event;
import org.wfmc._2009.xpdl2.ExpressionType;
import org.wfmc._2009.xpdl2.ExtendedAttribute;
import org.wfmc._2009.xpdl2.ExtendedAttributes;
import org.wfmc._2009.xpdl2.Group;
import org.wfmc._2009.xpdl2.Implementation;
import org.wfmc._2009.xpdl2.Input;
import org.wfmc._2009.xpdl2.InputSet;
import org.wfmc._2009.xpdl2.InputSets;
import org.wfmc._2009.xpdl2.Loop;
import org.wfmc._2009.xpdl2.Output;
import org.wfmc._2009.xpdl2.OutputSet;
import org.wfmc._2009.xpdl2.OutputSets;
import org.wfmc._2009.xpdl2.PackageType;
import org.wfmc._2009.xpdl2.Performer;
import org.wfmc._2009.xpdl2.Performers;
import org.wfmc._2009.xpdl2.ProcessType;
import org.wfmc._2009.xpdl2.Route;
import org.wfmc._2009.xpdl2.SubFlow;
import org.wfmc._2009.xpdl2.Task;
import org.wfmc._2009.xpdl2.Transition;

/**
 *
 * @author lukas
 */
public class GeracaoElementosBPMNBO {

    private String retornaArtefatoPorAssociacao(boolean isSubProcesso, String idProcesso, boolean isEntrada, PackageType pacote,
            String artifactId) {
        DataAssociations dataAssociationSet = null;
        DataStoreReferences dataStoreReferenceSet = null;
        DataObjects dataObjectSet = null;
        String dataAssociationId, idEncontrado = null;
        List<ActivitySet> activitySetList = null;
        List<ProcessType> processTypeList;

        if (isSubProcesso) {
            if (pacote.getWorkflowProcesses() != null && pacote.getWorkflowProcesses().getWorkflowProcess() != null
                    && !pacote.getWorkflowProcesses().getWorkflowProcess().isEmpty()) {
                boolean encontrouActivitySet = false;

                for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                    for (Object o : wfp.getContent()) {
                        if (o instanceof ActivitySets) {
                            ActivitySets activitySets = (ActivitySets) o;
                            for (ActivitySet activitySet : activitySets.getActivitySet()) {
                                if (activitySet.getId().equals(idProcesso)) {
                                    activitySetList = new ArrayList();
                                    activitySetList.add(activitySet);
                                    encontrouActivitySet = true;
                                    break;
                                }
                            }
                        }
                        if (encontrouActivitySet) {
                            break;
                        }
                    }
                    if (encontrouActivitySet) {
                        break;
                    }
                }
            }

            if (activitySetList != null && !activitySetList.isEmpty()) {
                dataAssociationSet = activitySetList.get(0).getDataAssociations();
                dataObjectSet = activitySetList.get(0).getDataObjects();
                dataStoreReferenceSet = activitySetList.get(0).getDataStoreReferences();
            }
        } else {
            processTypeList = pacote.getWorkflowProcesses().getWorkflowProcess() == null
                    || pacote.getWorkflowProcesses().getWorkflowProcess().isEmpty() ? null
                            : pacote.getWorkflowProcesses().getWorkflowProcess().stream().filter(pr -> pr.getId().equals(idProcesso))
                            .collect(Collectors.toList());

            if (processTypeList != null && !processTypeList.isEmpty()) {
                for (ProcessType wfp : processTypeList) {
                    if (wfp.getContent() != null && !wfp.getContent().isEmpty()) {
                        for (Object o : wfp.getContent()) {
                            if (o instanceof DataAssociations) {
                                dataAssociationSet = (DataAssociations) o;
                            }
                            if (o instanceof DataStoreReferences) {
                                dataStoreReferenceSet = (DataStoreReferences) o;
                            }
                            if (o instanceof DataObjects) {
                                dataObjectSet = (DataObjects) o;
                            }
                        }
                    }
                }
            }
        }

        if (dataAssociationSet != null && dataAssociationSet.getDataAssociation() != null
                && !dataAssociationSet.getDataAssociation().isEmpty()) {
            if (isEntrada) {
                dataAssociationId = dataAssociationSet.getDataAssociation().stream().filter(dt -> dt.getTo().equals(artifactId))
                        .findFirst().get().getFrom();
            } else {
                dataAssociationId = dataAssociationSet.getDataAssociation().stream().filter(dt -> dt.getFrom().equals(artifactId))
                        .findFirst().get().getTo();
            }

            if (dataAssociationId != null && !dataAssociationId.isEmpty()) {
                if (dataStoreReferenceSet != null && dataStoreReferenceSet.getDataStoreReference() != null
                        && !dataStoreReferenceSet.getDataStoreReference().isEmpty()) {
                    idEncontrado = dataStoreReferenceSet.getDataStoreReference().stream().filter(dt -> dt.getId().equals(dataAssociationId))
                            .count() == 0 ? null : dataStoreReferenceSet.getDataStoreReference().stream().filter(dt -> dt.getId()
                                            .equals(dataAssociationId)).findFirst().get().getDataStoreRef();
                }
                if ((idEncontrado == null || idEncontrado.isEmpty()) && dataObjectSet != null
                        && dataObjectSet.getDataObject() != null && !dataObjectSet.getDataObject().isEmpty()) {
                    idEncontrado = dataObjectSet.getDataObject().stream().filter(dt -> dt.getId().equals(dataAssociationId))
                            .count() == 0 ? null : dataObjectSet.getDataObject().stream().filter(dt -> dt.getId()
                                            .equals(dataAssociationId)).findFirst().get().getId();
                }
            }
        }

        return idEncontrado;
    }

    public GatilhoEvento retornaGatilhoEvento(String descGatilhoEvento) {
        switch (descGatilhoEvento) {
            case "Message":
                return GatilhoEvento.Message;
            case "Signal":
                return GatilhoEvento.Signal;
            case "Timer":
                return GatilhoEvento.Signal;
            case "Conditional":
                return GatilhoEvento.Condition;
            case "ParallelMultiple":
                return GatilhoEvento.ParallelMultiple;
            case "Multiple":
                return GatilhoEvento.Multiple;
            case "Compensation":
                return GatilhoEvento.Compensation;
            case "Escalation":
                return GatilhoEvento.Scale;
            case "Link":
                return GatilhoEvento.Link;
            case "Cancel":
                return GatilhoEvento.Cancel;
            case "Error":
                return GatilhoEvento.Exception;
            case "Terminate":
                return GatilhoEvento.EndTrigger;
            default:
                return GatilhoEvento.None;
        }
    }

    public TipoGateway retornaTipoGateway(Route propriedade) {
        if (propriedade.getExclusiveType() != null && propriedade.getExclusiveType().equals("Event")) {
            return propriedade.isInstantiate() ? TipoGateway.ExclusiveEventBased : TipoGateway.EventBased;
        } else {
            switch (propriedade.getGatewayType()) {
                case "Parallel":
                    return propriedade.isParallelEventBased()
                            ? TipoGateway.ParallelEventBased : TipoGateway.Parallel;
                case "Inclusive":
                    return TipoGateway.Inclusive;
                case "Complex":
                    return TipoGateway.Complex;
                default:
                    return TipoGateway.Exclusive;
            }
        }
    }

    public TipoAtividade retornaTipoAtividade(Task propriedade) {
        if (propriedade != null) {
            if (propriedade.getTaskUser() != null) {
                return TipoAtividade.User;
            } else if (propriedade.getTaskManual() != null) {
                return TipoAtividade.Manual;
            } else if (propriedade.getTaskScript() != null) {
                return TipoAtividade.Script;
            } else if (propriedade.getTaskSend() != null) {
                return TipoAtividade.Send;
            } else if (propriedade.getTaskReceive() != null) {
                return TipoAtividade.Receive;
            } else if (propriedade.getTaskService() != null) {
                return TipoAtividade.Service;
            } else if (propriedade.getTaskBusinessRule() != null) {
                return TipoAtividade.BusinessRule;
            } else {
                return TipoAtividade.Simple;
            }
        } else {
            return TipoAtividade.Simple;
        }
    }

    public TipoSubProcesso retornaTipSubProcesso(Activity activity, ActivitySet activitySet) {
        if (activitySet.isAdHoc()) {
            return TipoSubProcesso.Adhoc;
        }
        if (activity.isIsATransaction()) {
            return TipoSubProcesso.Transactional;
        }

        if (activity.getContent() != null && !activity.getContent().isEmpty()) {
            for (Object content : activity.getContent()) {
                Loop loop = content instanceof Loop ? (Loop) content : null;
                if (loop != null && loop.getLoopType() != null && !loop.getLoopType().isEmpty()) {
                    if (loop.getLoopType().equals("Standard") && loop.getLoopStandard() != null) {
                        return TipoSubProcesso.Loop;
                    } else if (loop.getLoopType().equals("MultiInstance") && loop.getLoopMultiInstance() != null
                            && loop.getLoopMultiInstance().getMIOrdering() != null
                            && !loop.getLoopMultiInstance().getMIOrdering().isEmpty()) {
                        return loop.getLoopMultiInstance().getMIOrdering().equals("Parallel")
                                ? TipoSubProcesso.ParallelMultipleInstances : TipoSubProcesso.SequenceMultipleInstances;
                    }
                }
//                else {
//                    Implementation implementation = content instanceof Implementation ? (Implementation) content : null;
//                    if (implementation != null && implementation.getSubFlow() != null) {
//                        return TipoSubProcesso.Reusable;
//                    }
//                }
            }

        }
        if (activitySet.isTriggeredByEvent()) {
            return TipoSubProcesso.Event;
        }

        return TipoSubProcesso.Simple;
    }

    public List<Ator> preencheAtor(Processo processo, List<Performer> performerList, List<Ator> atorList) {
        List<Ator> atorListRetorno = new ArrayList();

        if (performerList != null && !performerList.isEmpty()) {
            List<Ator> atorListAux;
            Ator atorAux;
            for (Performer per : performerList) {
                atorListAux = processo.getAtorList().isEmpty() ? null
                        : processo.getAtorList().stream().filter(a -> a.getIdElemento().equals(per.getValue()))
                        .collect(Collectors.toList());
                if (atorListAux == null || atorListAux.isEmpty()) {
                    atorAux = atorList.stream().filter(a -> a.getIdElemento().equals(per.getValue())).count() == 0
                            ? null : atorList.stream().filter(a -> a.getIdElemento().equals(per.getValue())).findFirst().get();

                    if (atorAux != null) {
                        atorListRetorno.add(atorAux);
                    }
                }
            }
        }

        return atorListRetorno;
    }

    public List<AtributoEstendido> preencheAtributosEstendidos(Elemento elemento, List<ExtendedAttribute> atributosEstendidosList) {
        List<AtributoEstendido> atributoEstendidoList = null;

        if (elemento != null && atributosEstendidosList != null && !atributosEstendidosList.isEmpty()) {
            AtributoEstendido atributoEstendido;

            atributoEstendidoList = new ArrayList();

            for (ExtendedAttribute extAtt : atributosEstendidosList) {
                atributoEstendido = new AtributoEstendido();
                //Até o momento só são preenchidos o nome e o valor dos atributos estendidos.
                atributoEstendido.setElemento(elemento);
                atributoEstendido.setIsRegraNegocio(TextUtils.isRegraDeNegocio(extAtt.getName()));
                atributoEstendido.setIsRequisitoFuncional(TextUtils.isRequisitoFuncional(extAtt.getName()));
                atributoEstendido.setIsRequisitoNaoFuncional(TextUtils.isRequisitoNaoFuncional(extAtt.getName()));
                atributoEstendido.setNome(TextUtils.removeTagsHtml(extAtt.getName()));
                atributoEstendido.setValor(TextUtils.removeTagsHtml(extAtt.getValue()));
                atributoEstendidoList.add(atributoEstendido);
            }
        }

        return atributoEstendidoList;
    }

    public List<ExecutadoPor> preencheExecutadoPor(Atividade atividade, List<Ator> atorList,
            List<Performer> performerList) {
        List<ExecutadoPor> executadoPorList = new ArrayList();

        if (performerList != null && !performerList.isEmpty()) {
            Ator ator;
            ExecutadoPor executadoPor;
            for (Performer performer : performerList) {
                ator = atorList != null ? atorList.stream().filter(a -> a.getIdElemento().equals(performer.getValue())).count() > 0
                        ? atorList.stream().filter(a -> a.getIdElemento().equals(performer.getValue()))
                        .findFirst().get() : null : null;
                if (ator != null && atividade != null) {
                    executadoPor = new ExecutadoPor();
                    executadoPor.setAtividade(atividade);
                    executadoPor.setAtor(ator);
                    executadoPorList.add(executadoPor);
                }
            }
        }

        return executadoPorList;
    }

    public List<SucedidoPor> preencheListaSucedidoPor(Processo processo, List<Transition> transitionList) {
        List<SucedidoPor> sucedidoPorList = new ArrayList();
        Elemento elementoOrigem, elementoDestino;
        SucedidoPor sucedidoPor;
        Condition condicao;

        for (Transition transition : transitionList) {
            sucedidoPor = new SucedidoPor();

            sucedidoPor.setIdSucedidoPor(transition.getId());
            sucedidoPor.setNomeTransicao(transition.getName());

            //Define a condição da transição
            condicao = transition.getCondition();
            sucedidoPor.setTipoCondicao(condicao == null || condicao.getType() == null
                    || condicao.getType().isEmpty() ? null : condicao.getType().equals("CONDITION") ? TipoCondicao.Condition
                            : condicao.getType().equals("OTHERWISE") ? TipoCondicao.Other
                            : condicao.getType().equals("EXCEPTION") ? TipoCondicao.Exception
                            : TipoCondicao.DefaultException);
            if (condicao != null && condicao.getContent() != null && !condicao.getContent().isEmpty()) {
                StringBuilder descCondicao = new StringBuilder();

                for (Object obj : condicao.getContent()) {
                    if (obj instanceof JAXBElement) {
                        Object objJAXB = ((JAXBElement) obj).getValue();
                        if (objJAXB instanceof ExpressionType) {
                            ((ExpressionType) objJAXB).getContent().stream().forEach((objAux) -> {
                                descCondicao.append(objAux.toString()).append(";");
                            });
                        }
                    }
                }

                if (descCondicao.toString() != null && !descCondicao.toString().isEmpty()) {
                    sucedidoPor.setDescricaoCondicao(descCondicao.toString());
                }
            }

            List<Elemento> elementosList;

            //Verifica se o elemento de origem é uma atividade
            elementosList = processo.getAtividadeList() == null || processo.getAtividadeList().isEmpty()
                    ? null : processo.getAtividadeList().stream().filter(a -> a.getIdElemento().equals(transition.getFrom()))
                    .collect(Collectors.toList());

            elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);

            if (elementoOrigem == null) {
                //Verifica se o elemento de origem é um subprocesso
                elementosList = processo.getSubProcessoList() == null || processo.getSubProcessoList().isEmpty()
                        ? null : processo.getSubProcessoList().stream().filter(a -> a.getIdElemento().equals(transition.getFrom()))
                        .collect(Collectors.toList());

                elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            if (elementoOrigem == null) {
                //Verifica se o elemento de origem é um evento
                elementosList = processo.getEventoList() == null || processo.getEventoList().isEmpty()
                        ? null : processo.getEventoList().stream().filter(a -> a.getIdElemento().equals(transition.getFrom()))
                        .collect(Collectors.toList());

                elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            if (elementoOrigem == null) {
                //Verifica se o elemento de origem é um gateway
                elementosList = processo.getGatewayList() == null || processo.getGatewayList().isEmpty()
                        ? null : processo.getGatewayList().stream().filter(a -> a.getIdElemento().equals(transition.getFrom()))
                        .collect(Collectors.toList());

                elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            sucedidoPor.setElementoOrigem(elementoOrigem);

            //Verifica se o elemento de destino é uma atividade
            elementosList = processo.getAtividadeList() == null || processo.getAtividadeList().isEmpty()
                    ? null : processo.getAtividadeList().stream().filter(a -> a.getIdElemento().equals(transition.getTo()))
                    .collect(Collectors.toList());

            elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);

            if (elementoDestino == null) {
                //Verifica se o elemento de destino é um subprocesso
                elementosList = processo.getSubProcessoList() == null || processo.getSubProcessoList().isEmpty()
                        ? null : processo.getSubProcessoList().stream().filter(a -> a.getIdElemento().equals(transition.getTo()))
                        .collect(Collectors.toList());

                elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            if (elementoDestino == null) {
                //Verifica se o elemento de destino é um evento
                elementosList = processo.getEventoList() == null || processo.getEventoList().isEmpty()
                        ? null : processo.getEventoList().stream().filter(a -> a.getIdElemento().equals(transition.getTo()))
                        .collect(Collectors.toList());

                elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            if (elementoDestino == null) {
                //Verifica se o elemento de destino é um gateway
                elementosList = processo.getGatewayList() == null || processo.getGatewayList().isEmpty()
                        ? null : processo.getGatewayList().stream().filter(a -> a.getIdElemento().equals(transition.getTo()))
                        .collect(Collectors.toList());

                elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
            }

            sucedidoPor.setElementoDestino(elementoDestino);

            sucedidoPorList.add(sucedidoPor);
        }

        return sucedidoPorList;
    }

    public List<UtilizaEntrada> preencheUtilizaEntrada(boolean isSubProcesso, String idProcesso, PackageType pacote,
            Atividade atividade, List<Artefato> artefatoList, List<InputSet> inputSetList) {
        List<UtilizaEntrada> utilizaEntradaList = new ArrayList();

        if (inputSetList != null && !inputSetList.isEmpty()) {
            UtilizaEntrada utilizaEntrada;
            List<Artefato> artefatoListAux;

            for (InputSet inputSet : inputSetList) {
                if (inputSet.getArtifactInput() != null && !inputSet.getArtifactInput().isEmpty()) {
                    for (ArtifactInput artInp : inputSet.getArtifactInput()) {
                        final String idArtefatoRetorno = retornaArtefatoPorAssociacao(isSubProcesso, idProcesso, true, pacote,
                                artInp.getArtifactId());
                        artefatoListAux = artefatoList == null || artefatoList.isEmpty() || idArtefatoRetorno == null
                                || idArtefatoRetorno.isEmpty() ? null
                                        : artefatoList.stream().filter(art -> art.getIdElemento().equals(idArtefatoRetorno))
                                        .collect(Collectors.toList());
                        if (artefatoListAux != null && !artefatoListAux.isEmpty()) {
                            utilizaEntrada = new UtilizaEntrada();
                            utilizaEntrada.setAtividade(atividade);
                            utilizaEntrada.setArtefato(artefatoListAux.get(0));
                            utilizaEntradaList.add(utilizaEntrada);
                        }
                    }
                }
                if (inputSet.getInput() != null && !inputSet.getInput().isEmpty()) {
                    for (Input inp : inputSet.getInput()) {
                        final String idArtefatoRetorno = retornaArtefatoPorAssociacao(isSubProcesso, idProcesso, true, pacote,
                                inp.getArtifactId());
                        artefatoListAux = artefatoList == null || artefatoList.isEmpty() || idArtefatoRetorno == null
                                || idArtefatoRetorno.isEmpty() ? null : artefatoList.stream().filter(art -> art.getIdElemento().equals(
                                                idArtefatoRetorno)).collect(Collectors.toList());
                        if (artefatoListAux != null && !artefatoListAux.isEmpty()) {
                            utilizaEntrada = new UtilizaEntrada();
                            utilizaEntrada.setAtividade(atividade);
                            utilizaEntrada.setArtefato(artefatoListAux.get(0));
                            utilizaEntradaList.add(utilizaEntrada);
                        }
                    }
                }
            }
        }

        //Busca as associaçoes de Entrada entre Atividade e artefato que ainda nao foram mapeadas
        if (isSubProcesso && pacote.getWorkflowProcesses() != null && pacote.getWorkflowProcesses().getWorkflowProcess() != null
                && !pacote.getWorkflowProcesses().getWorkflowProcess().isEmpty()) {
            ActivitySet actvSet = null;
            boolean encontrouActivitySet = false;            

            for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                for (Object o : wfp.getContent()) {
                    if (o instanceof ActivitySets) {
                        ActivitySets activitySets = (ActivitySets) o;
                        for (ActivitySet activitySet : activitySets.getActivitySet()) {
                            if (activitySet.getId().equals(idProcesso)) {
                                actvSet = activitySet;
                                encontrouActivitySet = true;
                                break;
                            }
                        }
                    }
                    if (encontrouActivitySet) {
                        break;
                    }
                }
                if (encontrouActivitySet) {
                    break;
                }
            }

            if (actvSet != null && actvSet.getAssociations() != null && actvSet.getAssociations().getAssociationAndAny() != null
                    && !actvSet.getAssociations().getAssociationAndAny().isEmpty()) {
                UtilizaEntrada utiEnt;
                
                for (Object ass : actvSet.getAssociations().getAssociationAndAny()) {
                    if (ass instanceof Association) {
                        Association associacao = (Association) ass;

                        if (associacao.getTarget() != null && atividade.getIdElemento().equals(associacao.getTarget())
                                && associacao.getSource() != null) {
                            final Artefato artAux = artefatoList != null && !artefatoList.isEmpty()
                                    ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getSource())).count() > 0
                                    ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getSource())).findFirst()
                                    .get() : null : null;

                            if (artAux != null && (utilizaEntradaList.isEmpty()
                                    || utilizaEntradaList.stream().filter(uti -> uti.getAtividade().getIdElemento()
                                            .equals(atividade.getIdElemento()) && uti.getArtefato().getIdElemento().equals(artAux.getIdElemento())
                                    ).count() == 0)) {
                                utiEnt = new UtilizaEntrada();
                                utiEnt.setArtefato(artAux);
                                utiEnt.setAtividade(atividade);
                                utilizaEntradaList.add(utiEnt);
                            }
                        }
                    }

                }
            }
            //Busca em todos os processos
        } else if (!isSubProcesso && pacote.getAssociations() != null && pacote.getAssociations().getAssociationAndAny() != null
                && !pacote.getAssociations().getAssociationAndAny().isEmpty()) {
            UtilizaEntrada utiEnt;

            for (Object ass : pacote.getAssociations().getAssociationAndAny()) {
                if (ass instanceof Association) {
                    Association associacao = (Association) ass;

                    if (associacao.getTarget() != null && atividade.getIdElemento().equals(associacao.getTarget())
                            && associacao.getSource() != null) {
                        final Artefato artAux = artefatoList != null && !artefatoList.isEmpty()
                                ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getSource())).count() > 0
                                ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getSource())).findFirst()
                                .get() : null : null;

                        if (artAux != null && (utilizaEntradaList.isEmpty()
                                || utilizaEntradaList.stream().filter(uti -> uti.getAtividade().getIdElemento()
                                        .equals(atividade.getIdElemento()) && uti.getArtefato().getIdElemento().equals(artAux.getIdElemento())
                                ).count() == 0)) {
                            utiEnt = new UtilizaEntrada();
                            utiEnt.setArtefato(artAux);
                            utiEnt.setAtividade(atividade);
                            utilizaEntradaList.add(utiEnt);
                        }
                    }
                }

            }
        }

        return utilizaEntradaList;
    }

    public List<ProduzSaida> preencheProduzSaida(boolean isSubProcesso, String idProcesso, PackageType pacote,
            Elemento elemento, List<Artefato> artefatoList, List<OutputSet> outputSetList) {
        List<ProduzSaida> produzSaidaList = new ArrayList();

        if (outputSetList != null && !outputSetList.isEmpty()) {
            ProduzSaida produzSaida;
            List<Artefato> artefatoListAux;

            for (OutputSet outputSet : outputSetList) {
                if (outputSet.getOutput() != null && !outputSet.getOutput().isEmpty()) {
                    for (Output out : outputSet.getOutput()) {
                        final String idArtefatoRetorno = retornaArtefatoPorAssociacao(isSubProcesso, idProcesso, false, pacote,
                                out.getArtifactId());
                        artefatoListAux = artefatoList == null || artefatoList.isEmpty() || idArtefatoRetorno == null
                                || idArtefatoRetorno.isEmpty() ? null : artefatoList.stream().filter(art -> art.getIdElemento()
                                                .equals(idArtefatoRetorno)).collect(Collectors.toList());
                        if (artefatoListAux != null && !artefatoListAux.isEmpty()) {
                            produzSaida = new ProduzSaida();
                            produzSaida.setElemento(elemento);
                            produzSaida.setArtefato(artefatoListAux.get(0));
                            produzSaidaList.add(produzSaida);
                        }
                    }
                }
            }
        }

        //Busca as associaçoes de Saida entre Atividade e artefato que ainda nao foram mapeadas
        if (isSubProcesso && pacote.getWorkflowProcesses() != null && pacote.getWorkflowProcesses().getWorkflowProcess() != null
                && !pacote.getWorkflowProcesses().getWorkflowProcess().isEmpty()) {
            ActivitySet actvSet = null;
            boolean encontrouActivitySet = false;

            for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                for (Object o : wfp.getContent()) {
                    if (o instanceof ActivitySets) {
                        ActivitySets activitySets = (ActivitySets) o;
                        for (ActivitySet activitySet : activitySets.getActivitySet()) {
                            if (activitySet.getId().equals(idProcesso)) {
                                actvSet = activitySet;
                                encontrouActivitySet = true;
                                break;
                            }
                        }
                    }
                    if (encontrouActivitySet) {
                        break;
                    }
                }
                if (encontrouActivitySet) {
                    break;
                }
            }

            if (actvSet != null && actvSet.getAssociations() != null && actvSet.getAssociations().getAssociationAndAny() != null
                    && !actvSet.getAssociations().getAssociationAndAny().isEmpty()) {
                ProduzSaida proSaiAux;

                for (Object ass : actvSet.getAssociations().getAssociationAndAny()) {
                    if (ass instanceof Association) {
                        Association associacao = (Association) ass;

                        if (associacao.getSource() != null && elemento.getIdElemento().equals(associacao.getSource())
                                && associacao.getTarget() != null) {
                            final Artefato artAux = artefatoList != null && !artefatoList.isEmpty()
                                    ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getTarget())).count() > 0
                                    ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getTarget())).findFirst()
                                    .get() : null : null;

                            if (artAux != null && (produzSaidaList.isEmpty()
                                    || produzSaidaList.stream().filter(proSai -> proSai.getElemento().getIdElemento()
                                            .equals(elemento.getIdElemento()) && proSai.getArtefato().getIdElemento()
                                            .equals(artAux.getIdElemento())).count() == 0)) {
                                proSaiAux = new ProduzSaida();
                                proSaiAux.setArtefato(artAux);
                                proSaiAux.setElemento(elemento);
                                produzSaidaList.add(proSaiAux);
                            }
                        }
                    }

                }
            }
            //Busca em todos os processos
        } else if (!isSubProcesso && pacote.getAssociations() != null && pacote.getAssociations().getAssociationAndAny() != null
                && !pacote.getAssociations().getAssociationAndAny().isEmpty()) {
            ProduzSaida proSaiAux;

            for (Object ass : pacote.getAssociations().getAssociationAndAny()) {
                if (ass instanceof Association) {
                    Association associacao = (Association) ass;

                    if (associacao.getSource() != null && elemento.getIdElemento().equals(associacao.getSource())
                            && associacao.getTarget() != null) {
                        final Artefato artAux = artefatoList != null && !artefatoList.isEmpty()
                                ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getTarget())).count() > 0
                                ? artefatoList.stream().filter(art -> art.getIdElemento().equals(associacao.getTarget())).findFirst()
                                .get() : null : null;

                        if (artAux != null && (produzSaidaList.isEmpty()
                                || produzSaidaList.stream().filter(proSai -> proSai.getElemento().getIdElemento()
                                        .equals(elemento.getIdElemento()) && proSai.getArtefato().getIdElemento()
                                        .equals(artAux.getIdElemento())).count() == 0)) {
                            proSaiAux = new ProduzSaida();
                            proSaiAux.setArtefato(artAux);
                            proSaiAux.setElemento(elemento);
                            produzSaidaList.add(proSaiAux);
                        }
                    }
                }

            }
        }

        return produzSaidaList;
    }

    public SubProcesso geraSubProcesso(PackageType pacote, ActivitySet activitySet, List<Artefato> artefatoList, List<Ator> atorList) {
        List<Artefato> artefatoSubProcessList = new ArrayList();
        Map<Grupo, List<Group.Object>> mapGrupos = new HashMap();
        SubProcesso subProcesso = new SubProcesso();

        subProcesso.setIdElemento(activitySet.getId());
        subProcesso.setNome(TextUtils.removeTagsHtml(activitySet.getName()));
        //Busca a Activity referente ao ActivitySet
        Activity activity = null;
        boolean encontrouActivity = false;

        for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
            for (Object conjunto : wfp.getContent()) {
                if (conjunto instanceof Activities) {
                    Activities conjuntoAtividades = (Activities) conjunto;
                    for (Activity ati : conjuntoAtividades.getActivity()) {
                        if (ati.getId() != null && !ati.getId().isEmpty()
                                && activitySet.getId() != null && !activitySet.getId().isEmpty()
                                && ati.getId().equals(activitySet.getId())) {
                            activity = ati;
                            encontrouActivity = true;
                            break;
                        }
                    }
                }
                if (encontrouActivity) {
                    break;
                }
            }
            if (encontrouActivity) {
                break;
            }
        }

        subProcesso.setTipo(activity == null ? null : retornaTipSubProcesso(activity, activitySet));

        if (activitySet.getArtifacts() != null && activitySet.getArtifacts().getArtifactAndAny() != null
                && !activitySet.getArtifacts().getArtifactAndAny().isEmpty()) {

            for (Object o : activitySet.getArtifacts().getArtifactAndAny()) {
                if (o instanceof Artifact) {
                    Artifact objArtefato = (Artifact) o;
                    Artefato artefato;
                    switch (objArtefato.getArtifactType()) {
                        case "Annotation":
                            artefato = new Anotacao();
                            artefato.setIdElemento(objArtefato.getId());
                            artefato.setNome(TextUtils.removeTagsHtml(objArtefato.getName()));
                            artefato.setDescricao(TextUtils.removeTagsHtml(objArtefato.getTextAnnotation()));
                            artefato.setDocumentacao(TextUtils.removeTagsHtml(objArtefato.getTextAnnotation()));
                            artefatoSubProcessList.add(artefato);
                            break;
                        case "Group":
                            Group grupo = (Group) objArtefato.getGroup2();

                            artefato = new Grupo();
                            artefato.setIdElemento(grupo.getId());
                            artefato.setNome(removeTagsHtml(grupo.getName()));
                            artefato.setDescricao(removeTagsHtml(grupo.getName()));
                            artefato.setDocumentacao(removeTagsHtml(grupo.getName()));
                            artefatoSubProcessList.add(artefato);

                            mapGrupos.put((Grupo) artefato, grupo.getObject());
                            break;
                        case "Data Object":
                            DataObject objetoDados = (DataObject) objArtefato.getDataObject();

                            artefato = new Artefato();
                            artefato.setIdElemento(objetoDados.getId());
                            artefato.setTipo(TipoArtefato.DataObject);
                            artefato.setNome(removeTagsHtml(objetoDados.getName()));
                            artefato.setDescricao(objetoDados.getObject() != null
                                    && objetoDados.getObject().getDocumentation() != null
                                            ? removeTagsHtml(objetoDados.getObject().getDocumentation().getValue()) : null);
                            artefato.setDocumentacao(objetoDados.getObject() != null
                                    && objetoDados.getObject().getDocumentation() != null
                                            ? removeTagsHtml(objetoDados.getObject().getDocumentation().getValue()) : null);
                            artefatoSubProcessList.add(artefato);
                            break;
                    }
                }
            }
        }

        if (activitySet.getDataObjects() != null && activitySet.getDataObjects().getDataObject() != null
                && !activitySet.getDataObjects().getDataObject().isEmpty()) {
            for (DataObject dataObject : activitySet.getDataObjects().getDataObject()) {
                Artefato artefato = new Artefato();
                artefato.setIdElemento(dataObject.getId());
                artefato.setTipo(TipoArtefato.DataObject);
                artefato.setNome(TextUtils.removeTagsHtml(dataObject.getName()));
                artefato.setDescricao(dataObject.getObject().getDocumentation() != null
                        ? TextUtils.removeTagsHtml(dataObject.getObject().getDocumentation().getValue()) : null);
                artefato.setDocumentacao(dataObject.getObject().getDocumentation() != null
                        ? TextUtils.removeTagsHtml(dataObject.getObject().getDocumentation().getValue()) : null);

                if (!artefatoSubProcessList.isEmpty() && !artefatoSubProcessList.contains(artefato)) {
                    artefatoSubProcessList.add(artefato);
                }

                if (artefatoSubProcessList.isEmpty() || !artefatoSubProcessList.contains(artefato)) {
                    artefatoSubProcessList.add(artefato);
                }
            }
        }

        if (activitySet.getDataStoreReferences() != null && activitySet.getDataStoreReferences().getDataStoreReference() != null
                && !activitySet.getDataStoreReferences().getDataStoreReference().isEmpty()) {
            Artefato artefatoAux;

            for (DataStoreReference datSto : activitySet.getDataStoreReferences().getDataStoreReference()) {
                artefatoAux = artefatoList != null && !artefatoList.isEmpty()
                        ? artefatoList.stream().filter(a -> a.getIdElemento().equals(datSto.getDataStoreRef())).count() == 0
                        ? null : artefatoList.stream().filter(a -> a.getIdElemento().equals(datSto.getDataStoreRef())).findFirst().get() : null;

                if (artefatoAux != null) {
                    if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefatoAux)) {
                        artefatoList.add(artefatoAux);
                    }

                    if (artefatoSubProcessList.isEmpty() || !artefatoSubProcessList.contains(artefatoAux)) {
                        artefatoSubProcessList.add(artefatoAux);
                    }
                }
            }
        }

        if (activitySet.getDataAssociations() != null && activitySet.getDataAssociations().getDataAssociation() != null
                && !activitySet.getDataAssociations().getDataAssociation().isEmpty()) {
            Artefato artefatoAux;
            for (DataAssociation datAss : activitySet.getDataAssociations().getDataAssociation()) {
                //Busca nos dados de origem de associação
                artefatoAux = artefatoList != null && !artefatoList.isEmpty()
                        ? artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getFrom())).count() == 0
                        ? null : artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getFrom())).findFirst().get() : null;

                if (artefatoAux != null) {
                    if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefatoAux)) {
                        artefatoList.add(artefatoAux);
                    }

                    if (artefatoSubProcessList.isEmpty() || !artefatoSubProcessList.contains(artefatoAux)) {
                        artefatoSubProcessList.add(artefatoAux);
                    }
                }

                //Busca nos dados de destino de associação
                artefatoAux = artefatoList != null && !artefatoList.isEmpty()
                        ? artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getTo())).count() == 0
                        ? null : artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getTo())).findFirst().get() : null;

                if (artefatoAux != null) {
                    if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefatoAux)) {
                        artefatoList.add(artefatoAux);
                    }

                    if (artefatoSubProcessList.isEmpty() || !artefatoSubProcessList.contains(artefatoAux)) {
                        artefatoSubProcessList.add(artefatoAux);
                    }
                }
            }
        }

        if (subProcesso.getArtefatoList() == null) {
            subProcesso.setArtefatoList(new ArrayList());
        }

        subProcesso.getArtefatoList().addAll(artefatoSubProcessList);

        if (activitySet.getActivities() != null && activitySet.getActivities().getActivity() != null
                && !activitySet.getActivities().getActivity().isEmpty()) {
            List<Performer> performerList;

            //Trata as atividades, eventos e gateways
            for (Activity ati : activitySet.getActivities().getActivity()) {
                boolean isAtividade = true, isEvento = false, isGateway = false;
                String descricao = null, documentacao = null;
                TipoAtividade tipoAtividade = null;
                TipoEvento tipoEvento = null;
                GatilhoEvento gatilhoEvento = null;
                TipoGateway tipoGateway = null;
                DirecaoGateway direcaoGateway = null;
                String regraDeNegocio = null;
                List<ExtendedAttribute> atributosEstendidosList = null;
                List<InputSet> inputSetList = null;
                List<OutputSet> outputSetList = null;
                performerList = null;

                for (Object propriedade : ati.getContent()) {

                    //TODO:preencher lista de atributos estendidos
                    if (propriedade instanceof ExtendedAttributes) {
                        atributosEstendidosList = ((ExtendedAttributes) propriedade)
                                .getExtendedAttribute();
                    }
                    if (propriedade instanceof Description) {
                        descricao = TextUtils.removeTagsHtml(((Description) propriedade).getValue());
                    }
                    if (propriedade instanceof Documentation) {
                        documentacao = TextUtils.removeTagsHtml(((Documentation) propriedade).getValue());
                    }
                    if (propriedade instanceof Event) {
                        isEvento = true;
                        isGateway = false;
                        isAtividade = false;

                        //Verifica o tipo do evento (Start, Intermediate ou EndTrigger)
                        if (((Event) propriedade).getStartEvent() != null) {
                            tipoEvento = TipoEvento.Start;
                            //Verifica o gatilho do evento
                            gatilhoEvento = retornaGatilhoEvento(((Event) propriedade).getStartEvent().getTrigger());
                        } else if (((Event) propriedade).getIntermediateEvent() != null) {
                            tipoEvento = TipoEvento.Start;
                            //Verifica o gatilho do evento
                            gatilhoEvento = retornaGatilhoEvento(((Event) propriedade).getIntermediateEvent().getTrigger());
                        } else if (((Event) propriedade).getEndEvent() != null) {
                            tipoEvento = TipoEvento.End;
                            //Verifica o gatilho do evento
                            gatilhoEvento = retornaGatilhoEvento(((Event) propriedade).getEndEvent().getResult());
                        }
                    }
                    if (propriedade instanceof Route) {
                        isEvento = false;
                        isGateway = true;
                        isAtividade = false;

                        //Verifica a direçao do gateway
                        direcaoGateway = ((Route) propriedade).getGatewayDirection().equals("Diverging")
                                ? DirecaoGateway.Diverging : DirecaoGateway.Converging;
                        //Verifica o tipo do gateway
                        tipoGateway = retornaTipoGateway((Route) propriedade);
                    }
                    if ((propriedade instanceof Implementation)
                            && (((Implementation) propriedade).getTask() != null)) {
                        isEvento = false;
                        isGateway = false;
                        isAtividade = true;

                        //Verifica o tipo da atividade
                        Task propriedadeTask = ((Implementation) propriedade).getTask();
                        tipoAtividade = retornaTipoAtividade(propriedadeTask);

                        if (tipoAtividade != null && tipoAtividade.equals(TipoAtividade.BusinessRule)) {
                            regraDeNegocio = propriedadeTask.getTaskBusinessRule() != null
                                    ? propriedadeTask.getTaskBusinessRule().getBusinessRuleTaskImplementation()
                                    : null;
                        }
                    }
                    if (propriedade instanceof BlockActivity) {
                        tipoAtividade = TipoAtividade.SubProcess;
                    }
                    if (propriedade instanceof InputSets) {
                        inputSetList = ((InputSets) propriedade).getInputSet();
                    }
                    if (propriedade instanceof OutputSets) {
                        outputSetList = ((OutputSets) propriedade).getOutputSet();
                    }
                    if (propriedade instanceof Performers) {
                        performerList = ((Performers) propriedade).getPerformer();
                    }
                }

                //Preenche a lista de atores para o processo corrente
                if (subProcesso.getAtorList() == null) {
                    subProcesso.setAtorList(new ArrayList());
                }
                subProcesso.getAtorList().addAll(preencheAtor(subProcesso, performerList, atorList));

                if (isAtividade) {
                    if (subProcesso.getAtividadeList() == null) {
                        subProcesso.setAtividadeList(new ArrayList());
                    }

                    Atividade atividade = new Atividade();
                    atividade.setIdElemento(ati.getId());
                    atividade.setNome(TextUtils.removeTagsHtml(ati.getName()));
                    atividade.setDescricao(descricao);
                    atividade.setDocumentacao(documentacao);
                    atividade.setTipo(tipoAtividade);
                    atividade.setIsRequisitoFuncional(TextUtils.isRequisitoFuncional(tipoAtividade));
                    atividade.setIsRegraDeNegocio(regraDeNegocio != null && !regraDeNegocio.isEmpty());
                    atividade.setRegraDeNegocio(regraDeNegocio);
                    atividade.setAtributoEstendidoList(preencheAtributosEstendidos(atividade,
                            atributosEstendidosList));

                    subProcesso.getAtividadeList().add(atividade);
                    //Preenchimento dos atores envolvidos nas atividades
                    if (subProcesso.getExecutadoPorList() == null) {
                        subProcesso.setExecutadoPorList(new ArrayList());
                    }

                    subProcesso.getExecutadoPorList().addAll(preencheExecutadoPor(atividade, subProcesso.getAtorList(),
                            performerList));

                    if (subProcesso.getUtilizaEntradaList() == null) {
                        subProcesso.setUtilizaEntradaList(new ArrayList());
                    }
                    subProcesso.getUtilizaEntradaList().addAll(preencheUtilizaEntrada(true, activitySet.getId(), pacote, atividade,
                            subProcesso.getArtefatoList(), inputSetList));

                    if (subProcesso.getProduzSaidaList() == null) {
                        subProcesso.setProduzSaidaList(new ArrayList());
                    }
                    subProcesso.getProduzSaidaList().addAll(preencheProduzSaida(true, activitySet.getId(), pacote, atividade,
                            subProcesso.getArtefatoList(), outputSetList));

                } else if (isEvento) {
                    String nomeEvento;

                    if (subProcesso.getEventoList() == null) {
                        subProcesso.setEventoList(new ArrayList());
                    }

                    if (tipoEvento == null) {
                        nomeEvento = TextUtils.removeTagsHtml(ati.getName());
                    } else {
                        nomeEvento = TextUtils.removeTagsHtml(ati.getName());
                        String prefixoNomeEvento = tipoEvento.equals(TipoEvento.Start)
                                ? "Start" : tipoEvento.equals(TipoEvento.Intermediate)
                                ? "Intermediate" : "End";
                        if (nomeEvento == null || nomeEvento.isEmpty()) {
                            nomeEvento = prefixoNomeEvento.concat(subProcesso.getNome() == null
                                    || subProcesso.getNome().isEmpty() ? "Processo_".concat(subProcesso.getIdElemento())
                                            : "Processo".concat(subProcesso.getNome()));
                        }

                    }

                    Evento evento = new Evento();
                    evento.setIdElemento(ati.getId());
                    evento.setNome(nomeEvento);
                    evento.setDescricao(descricao);
                    evento.setDocumentacao(documentacao);
                    evento.setTipo(tipoEvento);
                    evento.setGatilho(gatilhoEvento);
                    evento.setAtributoEstendidoList(preencheAtributosEstendidos(evento,
                            atributosEstendidosList));

                    subProcesso.getEventoList().add(evento);

                    if (subProcesso.getProduzSaidaList() == null) {
                        subProcesso.setProduzSaidaList(new ArrayList());
                    }
                    subProcesso.getProduzSaidaList().addAll(preencheProduzSaida(true, activitySet.getId(), pacote,
                            evento, subProcesso.getArtefatoList(), outputSetList));
                } else if (isGateway) {
                    if (subProcesso.getGatewayList() == null) {
                        subProcesso.setGatewayList(new ArrayList());
                    }

                    Gateway gateway = new Gateway();
                    gateway.setIdElemento(ati.getId());
                    gateway.setNome(TextUtils.removeTagsHtml(ati.getName()));
                    gateway.setDescricao(descricao);
                    gateway.setDocumentacao(documentacao);
                    gateway.setTipo(tipoGateway);
                    gateway.setDirecao(direcaoGateway);
                    gateway.setIsRegraDeNegocio(TextUtils.isRegraDeNegocio(direcaoGateway));
                    if (gateway.getIsRegraDeNegocio()) {
                        gateway.setRegraDeNegocio(TextUtils.removeTagsHtml(gateway.getNome()));
                    }
                    gateway.setAtributoEstendidoList(preencheAtributosEstendidos(gateway,
                            atributosEstendidosList));

                    subProcesso.getGatewayList().add(gateway);
                }
            }
        }

        if (activitySet.getTransitions() != null && activitySet.getTransitions().getTransition() != null
                && !activitySet.getTransitions().getTransition().isEmpty()) {
            //Trata as transições
            subProcesso.setSucedidoPorList(preencheListaSucedidoPor(subProcesso, activitySet.getTransitions().getTransition()));
        }

        if (activitySet.getAssociations() != null && activitySet.getAssociations().getAssociationAndAny() != null
                && !activitySet.getAssociations().getAssociationAndAny().isEmpty()) {
            subProcesso = preencheAnotacoes(subProcesso, activitySet.getAssociations().getAssociationAndAny());
        }

        if (!mapGrupos.isEmpty()) {
            subProcesso = preencheGrupos(subProcesso, mapGrupos);
        }

        return subProcesso;
    }

    public SubProcesso preencheAnotacoes(SubProcesso subProcesso, List<Object> associacoesList) {
        SubProcesso subProcessoRetorno = subProcesso;
        List<Artefato> artefatoList;
        Anotacao anotacao;

        for (Object ass : associacoesList) {
            if (ass instanceof Association) {
                Association associacao = (Association) ass;
                anotacao = null;
                artefatoList = subProcessoRetorno.getArtefatoList() == null
                        || subProcessoRetorno.getArtefatoList().isEmpty() ? null
                                : subProcessoRetorno.getArtefatoList().stream().filter(art -> art instanceof Anotacao
                                        && art.getIdElemento().equals(associacao.getTarget())).collect(Collectors.toList());
                if (artefatoList != null && !artefatoList.isEmpty()) {
                    anotacao = (Anotacao) artefatoList.get(0);
                }

                //Procura o elemento atribuido à anotação nas atividades
                if (subProcessoRetorno.getAtividadeList() != null && !subProcessoRetorno.getAtividadeList().isEmpty()) {
                    Atividade atividade = subProcessoRetorno.getAtividadeList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : subProcessoRetorno.getAtividadeList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (atividade != null && anotacao != null) {
                        subProcessoRetorno.getAtividadeList().get(subProcessoRetorno.getAtividadeList().indexOf(atividade))
                                .setAnotacao(anotacao);
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos eventos
                if (subProcessoRetorno.getEventoList() != null && !subProcessoRetorno.getEventoList().isEmpty()) {
                    Evento evento = subProcessoRetorno.getEventoList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : subProcessoRetorno.getEventoList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (evento != null && anotacao != null) {
                        subProcessoRetorno.getEventoList().get(subProcessoRetorno.getEventoList().indexOf(evento))
                                .setAnotacao(anotacao);
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos gateways
                if (subProcessoRetorno.getGatewayList() != null && !subProcessoRetorno.getGatewayList().isEmpty()) {
                    Gateway gateway = subProcessoRetorno.getGatewayList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : subProcessoRetorno.getGatewayList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (gateway != null && anotacao != null) {
                        subProcessoRetorno.getAtividadeList().get(subProcessoRetorno.getAtividadeList().indexOf(gateway))
                                .setAnotacao(anotacao);
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos artefatos (depósito ou objeto de dados
                if (subProcessoRetorno.getArtefatoList() != null && !subProcessoRetorno.getArtefatoList().isEmpty()) {
                    Artefato artefato = subProcessoRetorno.getArtefatoList().stream()
                            .filter(a -> (a.getTipo().equals(TipoArtefato.DataStore)
                                    || a.getTipo().equals(TipoArtefato.DataObject))
                                    && a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : subProcessoRetorno.getArtefatoList().stream()
                            .filter(a -> (a.getTipo().equals(TipoArtefato.DataStore)
                                    || a.getTipo().equals(TipoArtefato.DataObject))
                                    && a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (artefato != null && anotacao != null) {
                        subProcessoRetorno.getAtividadeList().get(subProcessoRetorno.getAtividadeList().indexOf(artefato))
                                .setAnotacao(anotacao);
                    }
                }

            }
        }

        return subProcessoRetorno;
    }

    public Processo preencheAnotacoes(Processo processo, List<Object> associacoesList,
            List<Artefato> artefatoGeralList) {
        Processo processoRetorno = processo;

        List<Artefato> artefatoList;
        Anotacao anotacao;
        boolean isAnotacaoGeral = false;

        for (Object ass : associacoesList) {
            if (ass instanceof Association) {
                Association associacao = (Association) ass;
                anotacao = null;
                artefatoList = processoRetorno.getArtefatoList() == null
                        || processoRetorno.getArtefatoList().isEmpty() ? null
                                : processoRetorno.getArtefatoList().stream().filter(art -> art instanceof Anotacao
                                        && art.getIdElemento().equals(associacao.getTarget())).collect(Collectors.toList());
                if (artefatoList != null && !artefatoList.isEmpty()) {
                    anotacao = (Anotacao) artefatoList.get(0);
                }

                if (anotacao == null) {
                    artefatoList = artefatoGeralList == null
                            || artefatoGeralList.isEmpty() ? null
                                    : artefatoGeralList.stream().filter(art -> art instanceof Anotacao
                                            && art.getIdElemento().equals(associacao.getTarget())).collect(Collectors.toList());

                    if (artefatoList != null && !artefatoList.isEmpty()) {
                        anotacao = (Anotacao) artefatoList.get(0);
                        isAnotacaoGeral = true;
                    }
                }

                //Procura o elemento atribuido à anotação nas atividades
                if (processoRetorno.getAtividadeList() != null && !processoRetorno.getAtividadeList().isEmpty()) {
                    Atividade atividade = processoRetorno.getAtividadeList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : processoRetorno.getAtividadeList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (atividade != null && anotacao != null) {
                        int indiceAtividade = processoRetorno.getAtividadeList() != null && !processoRetorno.getAtividadeList().isEmpty()
                                ? processoRetorno.getAtividadeList().indexOf(atividade) : -1;

                        if (indiceAtividade != -1) {
                            processoRetorno.getAtividadeList().get(indiceAtividade).setAnotacao(anotacao);
                            if (isAnotacaoGeral) {
                                processoRetorno.getArtefatoList().add(anotacao);
                            }
                        }
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos eventos
                if (processoRetorno.getEventoList() != null && !processoRetorno.getEventoList().isEmpty()) {
                    Evento evento = processoRetorno.getEventoList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : processoRetorno.getEventoList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (evento != null && anotacao != null) {
                        int indiceEvento = processoRetorno.getEventoList() != null && !processoRetorno.getEventoList().isEmpty()
                                ? processoRetorno.getEventoList().indexOf(evento) : -1;

                        if (indiceEvento != -1) {
                            processoRetorno.getEventoList().get(indiceEvento).setAnotacao(anotacao);
                            if (isAnotacaoGeral) {
                                processoRetorno.getArtefatoList().add(anotacao);
                            }
                        }
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos gateways
                if (processoRetorno.getGatewayList() != null && !processoRetorno.getGatewayList().isEmpty()) {
                    Gateway gateway = processoRetorno.getGatewayList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : processoRetorno.getGatewayList().stream()
                            .filter(a -> a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (gateway != null && anotacao != null) {
                        int indiceGateway = processoRetorno.getGatewayList() != null && !processoRetorno.getGatewayList().isEmpty()
                                ? processoRetorno.getGatewayList().indexOf(gateway) : -1;

                        if (indiceGateway != -1) {
                            processoRetorno.getGatewayList().get(indiceGateway).setAnotacao(anotacao);
                            if (isAnotacaoGeral) {
                                processoRetorno.getArtefatoList().add(anotacao);
                            }
                        }
                        continue;
                    }
                }

                //Procura o elemento atribuido à anotação nos artefatos (depósito ou objeto de dados
                if (processoRetorno.getArtefatoList() != null && !processoRetorno.getArtefatoList().isEmpty()) {
                    Artefato artefato = processoRetorno.getArtefatoList().stream()
                            .filter(a -> (a.getTipo().equals(TipoArtefato.DataStore)
                                    || a.getTipo().equals(TipoArtefato.DataObject))
                                    && a.getIdElemento().equals(associacao.getSource())).count() == 0
                            ? null : processoRetorno.getArtefatoList().stream()
                            .filter(a -> (a.getTipo().equals(TipoArtefato.DataStore)
                                    || a.getTipo().equals(TipoArtefato.DataObject))
                                    && a.getIdElemento().equals(associacao.getSource())).findFirst().get();

                    if (artefato != null && anotacao != null) {
                        int indiceArtefato = processoRetorno.getArtefatoList() != null && !processoRetorno.getArtefatoList().isEmpty()
                                ? processoRetorno.getArtefatoList().indexOf(artefato) : -1;

                        if (indiceArtefato != -1) {
                            processoRetorno.getArtefatoList().get(indiceArtefato).setAnotacao(anotacao);
                            if (isAnotacaoGeral) {
                                processoRetorno.getArtefatoList().add(anotacao);
                            }
                        }
                    }
                }

            }
        }

        return processoRetorno;
    }

    public SubProcesso preencheGrupos(SubProcesso subProcesso, Map<Grupo, List<Group.Object>> mapGrupos) {
        SubProcesso subProcessoRetorno = subProcesso;

        List<Group.Object> listaGruposAux;
        for (Grupo grupo : mapGrupos.keySet()) {
            List<Group.Object> listaGrupos = mapGrupos.get(grupo);
            //Analisa as atividades
            if (subProcessoRetorno.getAtividadeList() != null && !subProcessoRetorno.getAtividadeList().isEmpty()) {
                for (Atividade atividade : subProcessoRetorno.getAtividadeList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(atividade.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        atividade.setGrupo(grupo);
                    }
                }
            }

            //Analisa os eventos
            if (subProcessoRetorno.getEventoList() != null && !subProcessoRetorno.getEventoList().isEmpty()) {
                for (Evento evento : subProcessoRetorno.getEventoList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(evento.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        evento.setGrupo(grupo);
                    }
                }
            }

            //Analisa os gateways
            if (subProcessoRetorno.getGatewayList() != null && !subProcessoRetorno.getGatewayList().isEmpty()) {
                for (Gateway gateway : subProcessoRetorno.getGatewayList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(gateway.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        gateway.setGrupo(grupo);
                    }
                }
            }

            //Analisa os artefatos, desde que sejam Depósitos ou Objetos de Dados
            List<Artefato> artefatoAuxList = subProcessoRetorno.getArtefatoList() == null
                    || subProcessoRetorno.getArtefatoList().isEmpty()
                            ? null : subProcessoRetorno.getArtefatoList().stream().filter(a -> a.getTipo().equals(TipoArtefato.DataStore)
                                    || a.getTipo().equals(TipoArtefato.DataObject)).collect(Collectors.toList());

            if (artefatoAuxList != null && !artefatoAuxList.isEmpty()) {
                for (Artefato artefato : artefatoAuxList) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(artefato.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        artefato.setGrupo(grupo);
                    }
                }
            }
        }

        return subProcessoRetorno;
    }

    public Processo preencheGrupos(Processo processo, Map<Grupo, List<Group.Object>> mapGrupos) {
        Processo processoRetorno = processo;

        List<Group.Object> listaGruposAux;
        for (Grupo grupo : mapGrupos.keySet()) {
            List<Group.Object> listaGrupos = mapGrupos.get(grupo);
            //Analisa as atividades
            if (processoRetorno.getAtividadeList() != null && !processoRetorno.getAtividadeList().isEmpty()) {
                for (Atividade atividade : processoRetorno.getAtividadeList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(atividade.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        atividade.setGrupo(grupo);
                    }
                }
            }

            //Analisa os eventos
            if (processoRetorno.getEventoList() != null && !processoRetorno.getEventoList().isEmpty()) {
                for (Evento evento : processoRetorno.getEventoList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(evento.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        evento.setGrupo(grupo);
                    }
                }
            }

            //Analisa os gateways
            if (processoRetorno.getGatewayList() != null && !processoRetorno.getGatewayList().isEmpty()) {
                for (Gateway gateway : processoRetorno.getGatewayList()) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(gateway.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        gateway.setGrupo(grupo);
                    }
                }
            }

            //Analisa os artefatos, desde que sejam Depósitos ou Objetos de Dados
            List<Artefato> artefatoAuxList = processoRetorno.getArtefatoList() == null || processoRetorno.getArtefatoList().isEmpty()
                    ? null : processoRetorno.getArtefatoList().stream().filter(a -> a.getTipo().equals(TipoArtefato.DataStore)
                            || a.getTipo().equals(TipoArtefato.DataObject)).collect(Collectors.toList());

            if (artefatoAuxList != null && !artefatoAuxList.isEmpty()) {
                for (Artefato artefato : artefatoAuxList) {
                    listaGruposAux = listaGrupos != null ? listaGrupos.stream().filter(g
                            -> g.getId().equals(artefato.getIdElemento())).collect(Collectors.toList()) : null;
                    if (listaGruposAux != null && !listaGruposAux.isEmpty()) {
                        artefato.setGrupo(grupo);
                    }
                }
            }
        }

        return processoRetorno;
    }

}
