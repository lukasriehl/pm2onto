package com.lukas.pm2onto.bo;

import com.lukas.pm2onto.dao.ModeloDAO;
import com.lukas.pm2onto.dao.OntologiaDAO;
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
import com.lukas.pm2onto.model.Modelo;
import com.lukas.pm2onto.model.Ontologia;
import com.lukas.pm2onto.model.Piscina;
import com.lukas.pm2onto.model.Processo;
import com.lukas.pm2onto.model.ProduzSaida;
import com.lukas.pm2onto.model.SubProcesso;
import com.lukas.pm2onto.model.SucedidoPor;
import com.lukas.pm2onto.model.TrocaMensagemCom;
import com.lukas.pm2onto.model.UtilizaEntrada;
import com.lukas.pm2onto.model.enumerador.DirecaoGateway;
import com.lukas.pm2onto.model.enumerador.GatilhoEvento;
import com.lukas.pm2onto.model.enumerador.TipoArtefato;
import com.lukas.pm2onto.model.enumerador.TipoAtividade;
import com.lukas.pm2onto.model.enumerador.TipoEvento;
import com.lukas.pm2onto.model.enumerador.TipoGateway;
import com.lukas.pm2onto.model.enumerador.TipoSubProcesso;
import com.lukas.pm2onto.utils.FileDirUtils;
import com.lukas.pm2onto.utils.TextUtils;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.IOUtils;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.AnnotationProperty;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFList;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.XSD;
import org.wfmc._2009.xpdl2.Activities;
import org.wfmc._2009.xpdl2.Activity;
import org.wfmc._2009.xpdl2.ActivitySet;
import org.wfmc._2009.xpdl2.ActivitySets;
import org.wfmc._2009.xpdl2.Artifact;
import org.wfmc._2009.xpdl2.BlockActivity;
import org.wfmc._2009.xpdl2.DataAssociation;
import org.wfmc._2009.xpdl2.DataAssociations;
import org.wfmc._2009.xpdl2.DataObject;
import org.wfmc._2009.xpdl2.DataObjects;
import org.wfmc._2009.xpdl2.DataStore;
import org.wfmc._2009.xpdl2.DataStoreReference;
import org.wfmc._2009.xpdl2.DataStoreReferences;
import org.wfmc._2009.xpdl2.Description;
import org.wfmc._2009.xpdl2.Documentation;
import org.wfmc._2009.xpdl2.Event;
import org.wfmc._2009.xpdl2.ExtendedAttribute;
import org.wfmc._2009.xpdl2.ExtendedAttributes;
import org.wfmc._2009.xpdl2.Group;
import org.wfmc._2009.xpdl2.Implementation;
import org.wfmc._2009.xpdl2.InputSet;
import org.wfmc._2009.xpdl2.InputSets;
import org.wfmc._2009.xpdl2.Lane;
import org.wfmc._2009.xpdl2.MessageFlow;
import org.wfmc._2009.xpdl2.OutputSet;
import org.wfmc._2009.xpdl2.OutputSets;
import org.wfmc._2009.xpdl2.PackageType;
import org.wfmc._2009.xpdl2.Performer;
import org.wfmc._2009.xpdl2.Performers;
import org.wfmc._2009.xpdl2.Pool;
import org.wfmc._2009.xpdl2.ProcessType;
import org.wfmc._2009.xpdl2.Route;
import org.wfmc._2009.xpdl2.SubFlow;
import org.wfmc._2009.xpdl2.Task;
import org.wfmc._2009.xpdl2.Transitions;

/**
 *
 * @author lukas
 */
public class GeracaoOntologiasBO {

    private List<PackageType> rootPackageProcessosList;
    private OntModel modeloOntologiaProcesso;
    private List<File> arquivosOntologiasList;
    private List<Processo> processosList;
    private Modelo modelo;
    private String nomeOntologia, descricaoOntologia;
    private Map<Grupo, List<Group.Object>> mapGrupos;
    private List<Ator> atorList;
    private List<Artefato> artefatoList;
    private GeracaoElementosBPMNBO geracaoElementosBPMNBO;

    public void preenchePacoteProcesso(List<File> arquivosXpdlList) throws Exception {
        PackageType pacoteProcesso;
        JAXBContext jaxbContext;
        Unmarshaller jaxbUnmarshaller;
        JAXBElement rootElement;

        for (File file : arquivosXpdlList) {
            try {
                jaxbContext = JAXBContext.newInstance(PackageType.class);
                jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                rootElement = (JAXBElement) jaxbUnmarshaller.unmarshal(file);

                pacoteProcesso = (PackageType) rootElement.getValue();

                if (rootPackageProcessosList == null) {
                    rootPackageProcessosList = new ArrayList();
                }
                rootPackageProcessosList.add(pacoteProcesso);
            } catch (JAXBException e) {
                throw new Exception("Erro ao carregar os pacotes dos arquivos XPDL!"
                        .concat(e.getMessage() == null || e.getMessage().isEmpty()
                                ? "" : "Erro".concat(e.getMessage())));
            }
        }
    }

    public boolean geraProcessos() throws Exception {
        Processo processo;

        if (arquivosOntologiasList != null && !arquivosOntologiasList.isEmpty()) {
            arquivosOntologiasList.clear();
        }

        //Pega informações do cabeçalho do primeiro XPDL que foi incluido
        if (rootPackageProcessosList != null && !rootPackageProcessosList.isEmpty()) {
            ModeloDAO modeloDAO = new ModeloDAO();
            geracaoElementosBPMNBO = new GeracaoElementosBPMNBO();

            modelo = new Modelo();

            modelo.setId(modeloDAO.retornaProxIdModelo());
            modelo.setIdModelo(rootPackageProcessosList.get(0).getId());
            modelo.setNome(rootPackageProcessosList.get(0).getName());

            if (rootPackageProcessosList.get(0).getPackageHeader() != null) {
                Date dataCriacao = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(rootPackageProcessosList.get(0)
                        .getPackageHeader()
                        .getCreated().getValue().replace("T", " ").substring(0, 16));
                Date dataModificacao = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(rootPackageProcessosList.get(0).getPackageHeader()
                        .getModificationDate().getValue().replace("T", " ").substring(0, 16));

                modelo.setDataCriacao(dataCriacao);
                modelo.setDataModificacao(dataModificacao);
                modelo.setDescricao(TextUtils.removeTagsHtml(rootPackageProcessosList.get(0)
                        .getPackageHeader().getDescription().getValue()));
                modelo.setDocumentacao(TextUtils.removeTagsHtml(rootPackageProcessosList.get(0)
                        .getPackageHeader().getDocumentation().getValue()));
            }

            /*No momento está pegando os dados do primeiro arquivo XPDL da lista para inclusão nos atributos
                do modelo criado.*/
            for (PackageType pacote : rootPackageProcessosList) {

                //Preenche os Atores
                if (pacote.getParticipants() != null) {
                    atorList = new ArrayList();

                    pacote.getParticipants().getParticipant().stream().map((p) -> {
                        Ator ator = new Ator();
                        ator.setIdElemento(p.getId());
                        ator.setNome(TextUtils.removeTagsHtml(p.getName()));
                        ator.setDescricao(TextUtils.removeTagsHtml(p.getDescription().getValue()));
                        return ator;
                    }).forEach((ator) -> {
                        atorList.add(ator);
                    });
                }

                //Preenche os artefatos
                artefatoList = preencheArtefatos(pacote);

                //Preenche os processos
                for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                    processo = new Processo();
                    processo.setIdElemento(wfp.getId());
                    processo.setNome(wfp.getName());
                    processo.setTipoProcesso(wfp.getProcessType());
                    processo.setArtefatoList(preencheArtefatosProcesso(wfp));

                    if (processosList == null) {
                        processosList = new ArrayList();
                    }

                    processosList.add(processo);
                }

                List<Performer> performerList;

                //Itera sobre as definições de fluxo de processos
                for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                    processo = processosList.stream().filter(p -> p.getIdElemento().equals(wfp.getId()))
                            .findFirst().get();

                    for (Object conjunto : wfp.getContent()) {

                        //Itera sobre as atividades (atividades, eventos ou gateways)
                        if (conjunto instanceof Activities) {
                            Activities conjuntoAtividades = (Activities) conjunto;
                            for (Activity ati : conjuntoAtividades.getActivity()) {
                                boolean isAtividade = true, isEvento = false, isGateway = false,
                                        isSubFlow = false;
                                String descricao = null, documentacao = null, idSubFlow = null,
                                        targetId = null;
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

                                        //Verifica o tipo do evento (Start, Intermediate ou End)
                                        if (((Event) propriedade).getStartEvent() != null) {
                                            tipoEvento = TipoEvento.Start;
                                            //Verifica o gatilho do evento
                                            gatilhoEvento = geracaoElementosBPMNBO.retornaGatilhoEvento(((Event) propriedade).getStartEvent()
                                                    .getTrigger());
                                        } else if (((Event) propriedade).getIntermediateEvent() != null) {
                                            tipoEvento = TipoEvento.Intermediate;
                                            //Verifica o gatilho do evento
                                            gatilhoEvento = geracaoElementosBPMNBO.retornaGatilhoEvento(((Event) propriedade)
                                                    .getIntermediateEvent().getTrigger());

                                            if (((Event) propriedade).getIntermediateEvent().getTarget() != null) {
                                                targetId = ((Event) propriedade).getIntermediateEvent().getTarget();
                                            }
                                        } else if (((Event) propriedade).getEndEvent() != null) {
                                            tipoEvento = TipoEvento.End;
                                            //Verifica o gatilho do evento
                                            gatilhoEvento = geracaoElementosBPMNBO.retornaGatilhoEvento(((Event) propriedade).getEndEvent()
                                                    .getResult());
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
                                        tipoGateway = geracaoElementosBPMNBO.retornaTipoGateway((Route) propriedade);
                                    }
                                    if ((propriedade instanceof Implementation)) {
                                        isEvento = false;
                                        isGateway = false;
                                        isAtividade = true;

                                        if (((Implementation) propriedade).getTask() != null) {
                                            //Verifica o tipo da atividade
                                            Task propriedadeTask = ((Implementation) propriedade).getTask();
                                            tipoAtividade = geracaoElementosBPMNBO.retornaTipoAtividade(propriedadeTask);

                                            if (tipoAtividade != null && tipoAtividade.equals(TipoAtividade.BusinessRule)) {
                                                regraDeNegocio = propriedadeTask.getTaskBusinessRule() != null
                                                        ? propriedadeTask.getTaskBusinessRule().getBusinessRuleTaskImplementation()
                                                        : null;
                                            }
                                        }

                                        SubFlow subFlow = ((Implementation) propriedade).getSubFlow();
                                        //Define as propriedades de subflow
                                        if (subFlow != null && subFlow.getId() != null && !subFlow.getId().isEmpty()) {
                                            isSubFlow = true;
                                            idSubFlow = subFlow.getId();
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
                                if (processo.getAtorList() == null) {
                                    processo.setAtorList(new ArrayList());
                                }
                                processo.getAtorList().addAll(geracaoElementosBPMNBO.preencheAtor(processo, performerList,
                                        atorList));

                                if (isAtividade) {
                                    if (processo.getAtividadeList() == null) {
                                        processo.setAtividadeList(new ArrayList());
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
                                    atividade.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(
                                            atividade, atributosEstendidosList));
                                    if (isSubFlow) {
                                        atividade.setIsSubFlow(true);
                                        atividade.setIdSubFlow(idSubFlow);
                                    } else {
                                        atividade.setIsSubFlow(false);
                                    }

                                    processo.getAtividadeList().add(atividade);
                                    //Preenchimento dos atores envolvidos nas atividades
                                    if (processo.getExecutadoPorList() == null) {
                                        processo.setExecutadoPorList(new ArrayList());
                                    }

                                    processo.getExecutadoPorList().addAll(geracaoElementosBPMNBO.preencheExecutadoPor(atividade,
                                            processo.getAtorList(), performerList));

                                    if (processo.getUtilizaEntradaList() == null) {
                                        processo.setUtilizaEntradaList(new ArrayList());
                                    }
                                    processo.getUtilizaEntradaList().addAll(geracaoElementosBPMNBO.preencheUtilizaEntrada(false,
                                            wfp.getId(), pacote, atividade, processo.getArtefatoList(), inputSetList));

                                    if (processo.getProduzSaidaList() == null) {
                                        processo.setProduzSaidaList(new ArrayList());
                                    }
                                    processo.getProduzSaidaList().addAll(geracaoElementosBPMNBO.preencheProduzSaida(false,
                                            wfp.getId(), pacote, atividade, processo.getArtefatoList(), outputSetList));

                                } else if (isEvento) {
                                    String nomeEvento;

                                    if (processo.getEventoList() == null) {
                                        processo.setEventoList(new ArrayList());
                                    }

                                    if (tipoEvento == null) {
                                        nomeEvento = TextUtils.removeTagsHtml(ati.getName());
                                    } else {
                                        nomeEvento = TextUtils.removeTagsHtml(ati.getName());
                                        String prefixoNomeEvento = tipoEvento.equals(TipoEvento.Start)
                                                ? "Inicio " : tipoEvento.equals(TipoEvento.Intermediate)
                                                ? "Intermediario " : "Fim ";
                                        if (nomeEvento == null || nomeEvento.isEmpty()) {
                                            nomeEvento = prefixoNomeEvento.concat(processo.getNome() == null
                                                    || processo.getNome().isEmpty() ? "Processo ".concat(processo.getIdElemento())
                                                            : "Processo".concat(processo.getNome()));
                                        }

                                    }

                                    Evento evento = new Evento();
                                    evento.setIdElemento(ati.getId());
                                    evento.setNome(nomeEvento);
                                    evento.setDescricao(descricao);
                                    evento.setDocumentacao(documentacao);
                                    evento.setTipo(tipoEvento);
                                    evento.setGatilho(gatilhoEvento);
                                    evento.setTargetId(targetId);
                                    evento.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(evento,
                                            atributosEstendidosList));

                                    processo.getEventoList().add(evento);

                                    if (processo.getProduzSaidaList() == null) {
                                        processo.setProduzSaidaList(new ArrayList());
                                    }
                                    processo.getProduzSaidaList().addAll(geracaoElementosBPMNBO.preencheProduzSaida(false,
                                            wfp.getId(), pacote, evento, processo.getArtefatoList(), outputSetList));
                                } else if (isGateway) {
                                    if (processo.getGatewayList() == null) {
                                        processo.setGatewayList(new ArrayList());
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
                                    gateway.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(
                                            gateway, atributosEstendidosList));

                                    processo.getGatewayList().add(gateway);
                                }
                            }
                        }
                    }
                }

                //Trata os subprocessos
                for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                    processo = processosList.stream().filter(p -> p.getIdElemento().equals(wfp.getId()))
                            .findFirst().get();
                    for (Object conjunto : wfp.getContent()) {
                        if (conjunto instanceof ActivitySets) {
                            ActivitySets conjuntoAtividades = (ActivitySets) conjunto;
                            if (conjuntoAtividades.getActivitySet() != null
                                    && !conjuntoAtividades.getActivitySet().isEmpty()) {
                                SubProcesso subProcesso;

                                for (ActivitySet activitySet : conjuntoAtividades.getActivitySet()) {
                                    subProcesso = geracaoElementosBPMNBO.geraSubProcesso(pacote, activitySet, artefatoList, atorList);

                                    if (processo.getSubProcessoList() == null) {
                                        processo.setSubProcessoList(new ArrayList());
                                    }

                                    /*Se já mapeou o subprocesso como atividade anteriormente, então deve remover 
                                        da lista de atividades.*/
                                    Atividade atividade = null;

                                    if (processo.getAtividadeList() != null && !processo.getAtividadeList().isEmpty()) {
                                        for (Atividade ati : processo.getAtividadeList()) {
                                            if (ati.getIdElemento().equals(subProcesso.getIdElemento())) {
                                                atividade = ati;
                                                break;
                                            }
                                        }

                                        if (atividade != null) {
                                            subProcesso.setDescricao(atividade.getDescricao());
                                            subProcesso.setDocumentacao(atividade.getDocumentacao());
                                            processo.getAtividadeList().remove(processo.getAtividadeList().indexOf(atividade));
                                        }
                                    }

                                    processo.getSubProcessoList().add(subProcesso);
                                }
                            }

                        }
                    }
                }

                //Trata as transiçoes
                for (ProcessType wfp : pacote.getWorkflowProcesses().getWorkflowProcess()) {
                    processo = processosList.stream().filter(p -> p.getIdElemento().equals(wfp.getId()))
                            .findFirst().get();
                    for (Object conjunto : wfp.getContent()) {
                        //Preenchimento de transiçoes
                        if (conjunto instanceof Transitions) {
                            Transitions conjuntoTransicoes = (Transitions) conjunto;
                            if (conjuntoTransicoes.getTransition() != null && !conjuntoTransicoes.getTransition().isEmpty()) {
                                processo.setSucedidoPorList(geracaoElementosBPMNBO.preencheListaSucedidoPor(processo,
                                        conjuntoTransicoes.getTransition()));
                            }
                        }
                    }
                }

                if (processosList != null && !processosList.isEmpty()) {
                    modelo.setProcessoList(new ArrayList());
                    modelo.getProcessoList().addAll(processosList);

                    //Trata as pools
                    if (pacote.getPools() != null && pacote.getPools().getPool() != null
                            && !pacote.getPools().getPool().isEmpty()) {
                        for (Pool pool : pacote.getPools().getPool()) {
                            if (pool.getProcess() != null && !pool.getProcess().isEmpty()) {
                                processo = processosList.stream().filter(p -> p.getIdElemento().equals(pool.getProcess()))
                                        .findFirst().get();

                                if (processo != null) {
                                    Piscina piscina = new Piscina();
                                    piscina.setIdElemento(pool.getId());
                                    piscina.setNome(pool.getName());
                                    if (pool.getLanes() != null && pool.getLanes().getLane() != null
                                            && !pool.getLanes().getLane().isEmpty()) {
                                        StringBuilder sbLanes = new StringBuilder("Lanes: ");

                                        for (Lane lane : pool.getLanes().getLane()) {
                                            sbLanes.append(lane.getName() != null && !lane.getName().isEmpty()
                                                    ? lane.getName().concat(";") : "");
                                        }
                                        piscina.setDescricao(sbLanes.toString());
                                    }

                                    if (processo.getPiscinaList() == null) {
                                        processo.setPiscinaList(new ArrayList());
                                    }
                                    processo.getPiscinaList().add(piscina);
                                }
                            }
                        }
                    }

                    //Adiciona as anotações e grupos nos elementos do processo
                    for (Processo processoAux : processosList) {
                        if (pacote.getAssociations() != null && pacote.getAssociations().getAssociationAndAny() != null
                                && !pacote.getAssociations().getAssociationAndAny().isEmpty()) {
                            processoAux = geracaoElementosBPMNBO.preencheAnotacoes(processoAux,
                                    pacote.getAssociations().getAssociationAndAny(), artefatoList);
                        }
                        //Define os grupos, se existirem
                        if (!mapGrupos.isEmpty()) {
                            processoAux = geracaoElementosBPMNBO.preencheGrupos(processoAux, mapGrupos);
                        }
                    }
                    //Preenche as troca de mensagens
                    if (pacote.getMessageFlows() != null && pacote.getMessageFlows().getMessageFlowAndAny() != null
                            && !pacote.getMessageFlows().getMessageFlowAndAny().isEmpty()) {
                        modelo.setTrocaMensagemComList(preencheListaTrocaMensagemCom(modelo, processosList,
                                pacote.getMessageFlows().getMessageFlowAndAny()));
                    }
                }

            }

            if (modelo.getProcessoList() != null && !modelo.getProcessoList().isEmpty()) {
                //Trata os subfluxos

                List<Atividade> atividadeSubFlowList;
                List<Integer> indicesOfProcessesToRemoveList = new ArrayList();

                for (Processo prcAux : modelo.getProcessoList()) {
                    atividadeSubFlowList = prcAux.getAtividadeList() != null
                            && !prcAux.getAtividadeList().isEmpty()
                                    ? prcAux.getAtividadeList().stream().filter(ati -> ati.getIsSubFlow() && ati.getIdSubFlow() != null
                                            && !ati.getIdSubFlow().isEmpty()).collect(Collectors.toList()) : null;

                    if (atividadeSubFlowList != null && !atividadeSubFlowList.isEmpty()) {
                        SubProcesso newSubProcess;
                        Processo processToRemove;
                        Evento eventoGatilho;

                        for (Atividade atiSub : atividadeSubFlowList) {
                            //Verifica se a atividade apresenta um evento como gatilho
                            eventoGatilho = prcAux.getEventoList() != null && !prcAux.getEventoList().isEmpty()
                                    ? prcAux.getEventoList().stream().filter(eve -> eve.getTargetId() != null
                                            && !eve.getTargetId().isEmpty() && eve.getTargetId().equals(atiSub.getIdElemento())).count() > 0
                                    ? prcAux.getEventoList().stream().filter(eve -> eve.getTargetId() != null
                                            && !eve.getTargetId().isEmpty() && eve.getTargetId().equals(atiSub.getIdElemento())).findFirst().get()
                                    : null : null;

                            processToRemove = modelo.getProcessoList().stream().filter(pro -> pro.getIdElemento().equals(atiSub.getIdSubFlow()))
                                    .count() > 0 ? modelo.getProcessoList().stream().filter(pro -> pro.getIdElemento().equals(atiSub.getIdSubFlow())).findFirst().get()
                                            : null;

                            if (processToRemove != null) {
                                //int indiceAtividade = -1;
                                newSubProcess = new SubProcesso();

                                if (prcAux.getSubProcessoList() == null || prcAux.getSubProcessoList().isEmpty()) {
                                    prcAux.setSubProcessoList(new ArrayList());
                                }

                                //Preenche os dados do novo subprocesso
                                newSubProcess.setIdElemento(processToRemove.getIdElemento());
                                newSubProcess.setNome(processToRemove.getNome());
                                newSubProcess.setDescricao(processToRemove.getDescricao());
                                newSubProcess.setDocumentacao(processToRemove.getDocumentacao());
                                newSubProcess.setTipo(TipoSubProcesso.Reusable);
                                if (eventoGatilho != null) {
                                    newSubProcess.setEventoGatilho(eventoGatilho);
                                }
                                newSubProcess.setArtefatoList(processToRemove.getArtefatoList());
                                newSubProcess.setAtividadeList(processToRemove.getAtividadeList());
                                newSubProcess.setAtorList(processToRemove.getAtorList());
                                newSubProcess.setAtributoEstendidoList(processToRemove.getAtributoEstendidoList());
                                newSubProcess.setEventoList(processToRemove.getEventoList());
                                newSubProcess.setExecutadoPorList(processToRemove.getExecutadoPorList());
                                newSubProcess.setGatewayList(processToRemove.getGatewayList());
                                newSubProcess.setPiscinaList(processToRemove.getPiscinaList());
                                newSubProcess.setProcesso(prcAux);
                                newSubProcess.setProduzSaidaList(processToRemove.getProduzSaidaList());
                                newSubProcess.setSubProcessoList(processToRemove.getSubProcessoList());
                                newSubProcess.setSucedidoPorList(processToRemove.getSucedidoPorList());
                                newSubProcess.setUtilizaEntradaList(processToRemove.getUtilizaEntradaList());
                                newSubProcess.setIsSubFlow(true);
                                newSubProcess.setIdAtividadeOrigem(atiSub.getIdElemento());

                                prcAux.getSubProcessoList().add(newSubProcess);

//                                    indiceAtividade = prcAux.getAtividadeList().indexOf(atiSub);
//
//                                    if (indiceAtividade != -1) {
//                                        prcAux.getAtividadeList().remove(indiceAtividade);
//                                    }
                                indicesOfProcessesToRemoveList.add(modelo.getProcessoList().indexOf(processToRemove));
                            }
                        }
                    }
                }

                if (!indicesOfProcessesToRemoveList.isEmpty()) {
                    for (Integer idxToRemove : indicesOfProcessesToRemoveList) {
                        if (idxToRemove != -1) {
                            modelo.getProcessoList().remove(idxToRemove.intValue());
                        }
                    }
                }

                //Associa os subprocessos do Tipo Evento ao evento respectivo
                for (Processo processoAux : modelo.getProcessoList()) {
                    List<Evento> eventoSubProcessoList = processoAux.getEventoList() != null
                            && !processoAux.getEventoList().isEmpty()
                                    ? processoAux.getEventoList().stream().filter(eve -> eve.getTargetId() != null
                                            && !eve.getTargetId().isEmpty()).collect(Collectors.toList()) : null;

                    if (eventoSubProcessoList != null && !eventoSubProcessoList.isEmpty()) {
                        for (Evento eve : eventoSubProcessoList) {
                            for (SubProcesso subPro : processoAux.getSubProcessoList()) {
                                if (eve.getTargetId().equals(subPro.getIdElemento()) || subPro.getEventoGatilho() != null) {
                                    subPro.setTipo(TipoSubProcesso.Event);
                                    subPro.setEventoGatilho(subPro.getEventoGatilho() == null ? eve : subPro.getEventoGatilho());
                                }
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    private OntModel preencheConceitosProcesso(OntModel modeloOntologia, String NS, OntClass processoClas, Processo processo,
            boolean isSubProcesso) throws Exception {
        OntModel modeloOntologiaRetorno = modeloOntologia;

        if (processo.getAtorList() != null && !processo.getAtorList().isEmpty()) {
            //Preenchimento dos atores
            for (Ator ator : processo.getAtorList()) {
                OntClass atorSuperClas = modeloOntologiaProcesso.getOntClass(NS + "Actor");

                OntClass atorClas = modeloOntologiaProcesso.getOntClass(NS + (ator.getNome() != null
                        && !ator.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(ator.getNome()))
                                : "actor-".concat(ator.getIdElemento())));

                if (atorClas == null) {
                    atorClas = modeloOntologiaProcesso.createClass(NS + (ator.getNome() != null
                            && !ator.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(ator.getNome()))
                                    : "actor-".concat(ator.getIdElemento())));
                }

                atorClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), ator.getIdElemento());
                atorClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(ator.getNome(), processo));
                atorClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), ator.getDescricao() != null
                        ? ator.getDescricao() : "No Description");
                atorClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"), ator.getDocumentacao() != null
                        ? ator.getDocumentacao() : "No Documentation");

                atorSuperClas.addSubClass(atorClas);
            }
        }

        //Preenchimento das pools
        if (processo.getPiscinaList() != null && !processo.getPiscinaList().isEmpty()) {
            OntClass piscinaSuperClas = modeloOntologiaProcesso.getOntClass(NS + "Pool");

            for (Piscina pis : processo.getPiscinaList()) {
                OntClass piscinaClas = modeloOntologiaProcesso.getOntClass(NS + (pis.getNome() != null
                        && !pis.getNome().isEmpty() ? "pool-".concat(TextUtils.formataNome(pis.getNome()))
                                : "pool-".concat(pis.getIdElemento())));

                if (piscinaClas == null) {
                    piscinaClas = modeloOntologiaProcesso.createClass(NS + (pis.getNome() != null
                            && !pis.getNome().isEmpty() ? "pool-".concat(TextUtils.formataNome(pis.getNome()))
                                    : "pool-".concat(pis.getIdElemento())));
                }

                piscinaClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), pis.getIdElemento());
                piscinaClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(pis.getNome(), processo));
                piscinaClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), pis.getDescricao() != null
                        ? pis.getDescricao() : "No Description");
                piscinaClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"), pis.getDocumentacao() != null
                        ? pis.getDocumentacao() : "No Documentation");

                piscinaClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                piscinaSuperClas.addSubClass(piscinaClas);
            }
        }

        if (processo.getArtefatoList() != null && !processo.getArtefatoList().isEmpty()) {
            for (Artefato art : processo.getArtefatoList()) {
                String sufixoClasse = art.getTipo().equals(TipoArtefato.DataStore)
                        ? "DataStore" : art.getTipo().equals(TipoArtefato.DataObject) ? "DataObject"
                        : art.getTipo().equals(TipoArtefato.Group) ? "Group" : "Annotation";
                OntClass artefatoSuperClas = modeloOntologiaProcesso.getOntClass(NS + sufixoClasse);

                OntClass artefatoClas = modeloOntologiaProcesso.getOntClass(NS + (art.getNome() != null
                        && !art.getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getNome()))
                                : "artft-".concat(art.getIdElemento())));

                if (artefatoClas == null) {
                    artefatoClas = modeloOntologiaProcesso.createClass(NS + (art.getNome() != null
                            && !art.getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getNome()))
                                    : "artft-".concat(art.getIdElemento())));
                }

                artefatoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), art.getIdElemento());
                artefatoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(art.getNome(), processo));
                artefatoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), art.getDescricao() != null
                        ? art.getDescricao() : "No Description");
                artefatoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"), art.getDocumentacao() != null
                        ? art.getDocumentacao() : "No Documentation");

                artefatoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                artefatoSuperClas.addSubClass(artefatoClas);

                if (art.getAtributoEstendidoList() != null && !art.getAtributoEstendidoList().isEmpty()) {
                    OntClass atributoEstendidoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "ExtendedAttribute");
                    RDFNode[] attrClasses = new RDFNode[art.getAtributoEstendidoList().size()];
                    int i = 0;

                    for (AtributoEstendido atrEst : art.getAtributoEstendidoList()) {
                        OntClass atributoEstendidoClas = geraConceitoAtributoEstendido(NS, atrEst, processo, art);

                        attrClasses[i] = atributoEstendidoClas;

                        i++;

                        atributoEstendidoSuperClas.addSubClass(atributoEstendidoClas);

                        atributoEstendidoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isExtendedAttributeOf"), artefatoClas));
                    }

                    if (attrClasses.length > 0) {
                        RDFList listClasses = modeloOntologiaProcesso.createList(attrClasses);

                        artefatoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "hasExtendedAttribute"),
                                modeloOntologiaProcesso.createIntersectionClass(null, listClasses)));
                    }
                }
            }

            //Preenche as propriedades de objeto dos artefatos que possuem grupos
            List<Artefato> artefatoAgrupadoList = processo.getArtefatoList().stream()
                    .filter(art -> art.getGrupo() != null && art.getGrupo().getIdElemento() != null
                            && !art.getGrupo().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (artefatoAgrupadoList != null && !artefatoAgrupadoList.isEmpty()) {
                OntClass artefatoClas;
                OntClass grupoClas;

                for (Artefato art : artefatoAgrupadoList) {
                    artefatoClas = modeloOntologiaProcesso.getOntClass(NS + (art.getNome() != null
                            && !art.getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getNome()))
                                    : "artft-".concat(art.getIdElemento())));
                    grupoClas = modeloOntologiaProcesso.getOntClass(NS + (art.getGrupo().getNome() != null
                            && !art.getGrupo().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getGrupo().getNome()))
                                    : "artft-".concat(art.getGrupo().getIdElemento())));

                    if (artefatoClas != null && grupoClas != null) {
                        artefatoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfGroup"), grupoClas));
                    }
                }
            }

            //Preenche as propriedades de objeto dos artefatos que possuem anotação
            List<Artefato> artefatoAnotacaoList = processo.getArtefatoList().stream()
                    .filter(art -> art.getAnotacao() != null && art.getAnotacao().getIdElemento() != null
                            && !art.getAnotacao().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (artefatoAnotacaoList != null && !artefatoAnotacaoList.isEmpty()) {
                OntClass artefatoClas;
                OntClass anotacaoClas;

                for (Artefato art : artefatoAnotacaoList) {
                    artefatoClas = modeloOntologiaProcesso.getOntClass(NS + (art.getNome() != null
                            && !art.getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getNome()))
                                    : "artft-".concat(art.getIdElemento())));
                    anotacaoClas = modeloOntologiaProcesso.getOntClass(NS + (art.getNome() != null
                            && !art.getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getAnotacao().getNome()))
                                    : "artft-".concat(art.getAnotacao().getIdElemento())));

                    if (anotacaoClas == null) {
                        anotacaoClas = modeloOntologiaProcesso.createClass(NS + (art.getAnotacao().getNome() != null
                                && !art.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(art.getAnotacao().getNome()))
                                        : "artft-".concat(art.getAnotacao().getIdElemento())));
                    }

                    if (artefatoClas != null && anotacaoClas != null) {
                        artefatoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isAnnotatedBy"), anotacaoClas));
                    }
                }
            }
        }

        if (processo.getAtividadeList() != null && !processo.getAtividadeList().isEmpty()) {
            //Preenchimento das atividades
            for (Atividade ati : processo.getAtividadeList()) {
                OntClass atividadeSuperClas;

                if (ati.getTipo() == null) {
                    atividadeSuperClas = modeloOntologiaProcesso.getOntClass(NS + "Activity");
                } else if (modeloOntologiaProcesso.getOntClass(NS + "actv-" + ati.getTipo().name()) == null) {
                    atividadeSuperClas = modeloOntologiaProcesso.createClass(NS + "actv-" + ati.getTipo().name());
                    modeloOntologiaProcesso.getOntClass(NS + "Activity").addSubClass(atividadeSuperClas);
                } else {
                    atividadeSuperClas = modeloOntologiaProcesso.getOntClass(NS + "actv-" + ati.getTipo().name());
                }

                OntClass atividadeClas = modeloOntologiaProcesso.getOntClass(NS + (ati.getNome() != null
                        && !ati.getNome().isEmpty() ? "actv-".concat(TextUtils.formataNome(ati.getNome()))
                                : "actv-".concat(ati.getIdElemento())));

                if (atividadeClas == null) {
                    atividadeClas = modeloOntologiaProcesso.createClass(NS + (ati.getNome() != null
                            && !ati.getNome().isEmpty() ? "actv-".concat(TextUtils.formataNome(ati.getNome()))
                                    : "actv-".concat(ati.getIdElemento())));
                }

                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), ati.getIdElemento());
                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(ati.getNome(), processo));
                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), ati.getDescricao()
                        != null ? ati.getDescricao() : "No Description");
                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"), ati.getDocumentacao() != null
                        ? ati.getDocumentacao() : "No Documentation");

                boolean hasExtAttrRegraNegocio = ati.getAtributoEstendidoList() != null
                        && !ati.getAtributoEstendidoList().isEmpty()
                        && ati.getAtributoEstendidoList().stream().anyMatch(a -> a.getIsRegraNegocio());

                if (ati.getIsRegraDeNegocio() || hasExtAttrRegraNegocio) {
                    String businessRule = ati.getRegraDeNegocio() != null && !ati.getRegraDeNegocio().isEmpty()
                            ? ati.getRegraDeNegocio() : ati.getDescricao() != null && !ati.getDescricao().isEmpty()
                                    ? ati.getDescricao() : ati.getDocumentacao() != null && !ati.getDocumentacao().isEmpty()
                                            ? ati.getDocumentacao() : ati.getNome() != null && !ati.getNome().isEmpty()
                                                    ? ati.getNome() : "No Description for the Business Rule";

                    atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-isBusinessRule"),
                            "true", XSDDatatype.XSDboolean);
                    atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i8-businessRule"),
                            businessRule);
                } else {
                    atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-isBusinessRule"),
                            "false", XSDDatatype.XSDboolean);
                }

                boolean hasExtAttrRF = ati.getAtributoEstendidoList() != null
                        && !ati.getAtributoEstendidoList().isEmpty()
                        && ati.getAtributoEstendidoList().stream().anyMatch(a -> a.getIsRequisitoFuncional());

                boolean hasExtAttrRNF = ati.getAtributoEstendidoList() != null
                        && !ati.getAtributoEstendidoList().isEmpty()
                        && ati.getAtributoEstendidoList().stream().anyMatch(a -> a.getIsRequisitoNaoFuncional());

                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i6-isFunctionalRequirement"),
                        ati.getIsRequisitoFuncional() || hasExtAttrRF ? "true" : "false", XSDDatatype.XSDboolean);
                atividadeClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i7-isNonFunctionalRequirement"),
                        ati.getIsRequisitoNaoFuncional() || hasExtAttrRNF ? "true" : "false", XSDDatatype.XSDboolean);

                atividadeClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                List<ExecutadoPor> executadoPorList = processo.getExecutadoPorList() == null
                        || processo.getExecutadoPorList().isEmpty() ? null
                                : processo.getExecutadoPorList().stream().filter(e -> e.getAtividade().equals(ati))
                                .collect(Collectors.toList());
                if (executadoPorList != null && !executadoPorList.isEmpty()) {
                    modeloOntologiaProcesso = preencheRelacaoAtividadesExecutadas(modeloOntologiaProcesso, NS, false, executadoPorList);
                } else {
                    modeloOntologiaProcesso = preencheRelacaoAtividadesExecutadasSemAtores(modeloOntologiaProcesso, NS, ati, processo.getAtorList());
                }

                atividadeSuperClas.addSubClass(atividadeClas);

                List<UtilizaEntrada> utilizaEntradaList = processo.getUtilizaEntradaList() == null
                        || processo.getUtilizaEntradaList().isEmpty() ? null
                                : processo.getUtilizaEntradaList().stream().filter(e -> e.getAtividade().equals(ati))
                                .collect(Collectors.toList());
                if (utilizaEntradaList != null && !utilizaEntradaList.isEmpty()) {
                    for (UtilizaEntrada utiEnt : utilizaEntradaList) {
                        atividadeClas.addSuperClass(createCardinalityQRestriction(modeloOntologiaProcesso, null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "usesInput"), 1,
                                modeloOntologiaProcesso.getOntClass(NS + (utiEnt.getArtefato().getNome() != null
                                        && !utiEnt.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(utiEnt.getArtefato().getNome()))
                                                : "artft-".concat(utiEnt.getArtefato().getIdElemento())))));
                    }
                }

                List<ProduzSaida> produzSaidaList = processo.getProduzSaidaList() == null
                        || processo.getProduzSaidaList().isEmpty() ? null
                                : processo.getProduzSaidaList().stream().filter(e -> e.getElemento().equals(ati))
                                .collect(Collectors.toList());
                if (produzSaidaList != null && !produzSaidaList.isEmpty()) {
                    for (ProduzSaida proSai : produzSaidaList) {
                        atividadeClas.addSuperClass(createCardinalityQRestriction(modeloOntologiaProcesso, null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "producesOutput"), 1,
                                modeloOntologiaProcesso.getOntClass(NS + (proSai.getArtefato().getNome() != null
                                        && !proSai.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(proSai.getArtefato().getNome()))
                                                : "artft-".concat(proSai.getArtefato().getIdElemento())))));
                    }
                }

                if (ati.getAtributoEstendidoList() != null && !ati.getAtributoEstendidoList().isEmpty()) {
                    OntClass atributoEstendidoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "ExtendedAttribute");
                    RDFNode[] attrClasses = new RDFNode[ati.getAtributoEstendidoList().size()];
                    int i = 0;

                    for (AtributoEstendido atrEst : ati.getAtributoEstendidoList()) {
                        OntClass atributoEstendidoClas = geraConceitoAtributoEstendido(NS, atrEst, processo, ati);

                        attrClasses[i] = atributoEstendidoClas;

                        i++;

                        atributoEstendidoSuperClas.addSubClass(atributoEstendidoClas);

                        atributoEstendidoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isExtendedAttributeOf"), atividadeClas));
                    }

                    if (attrClasses.length > 0) {
                        RDFList listClasses = modeloOntologiaProcesso.createList(attrClasses);

                        atividadeClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "hasExtendedAttribute"),
                                modeloOntologiaProcesso.createIntersectionClass(null, listClasses)));
                    }
                }
            }

            //Preenche as propriedades de objeto das atividades que possuem grupos
            List<Atividade> atividadeAgrupadaList = processo.getAtividadeList().stream()
                    .filter(ati -> ati.getGrupo() != null && ati.getGrupo().getIdElemento() != null
                            && !ati.getGrupo().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (atividadeAgrupadaList != null && !atividadeAgrupadaList.isEmpty()) {
                OntClass atividadeClas;
                OntClass grupoClas;

                for (Atividade ati : atividadeAgrupadaList) {
                    atividadeClas = modeloOntologiaProcesso.getOntClass(NS + (ati.getNome() != null
                            && !ati.getNome().isEmpty() ? "actv-".concat(TextUtils.formataNome(ati.getNome()))
                                    : "actv-".concat(ati.getIdElemento())));
                    grupoClas = modeloOntologiaProcesso.getOntClass(NS + (ati.getGrupo().getNome() != null
                            && !ati.getGrupo().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(ati.getGrupo().getNome()))
                                    : "actv-".concat(ati.getGrupo().getIdElemento())));

                    if (atividadeClas != null && grupoClas != null) {
                        atividadeClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfGroup"), grupoClas));
                    }
                }
            }

            //Preenche as propriedades de objeto das atividades que possuem anotação
            List<Atividade> atividadeAnotacaoList = processo.getAtividadeList().stream()
                    .filter(ati -> ati.getAnotacao() != null && ati.getAnotacao().getIdElemento() != null
                            && !ati.getAnotacao().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (atividadeAnotacaoList != null && !atividadeAnotacaoList.isEmpty()) {
                OntClass atividadeClas;
                OntClass anotacaoClas;

                for (Atividade ati : atividadeAnotacaoList) {
                    atividadeClas = modeloOntologiaProcesso.getOntClass(NS + (ati.getNome() != null
                            && !ati.getNome().isEmpty() ? "actv-".concat(TextUtils.formataNome(ati.getNome()))
                                    : "actv-".concat(ati.getIdElemento())));
                    anotacaoClas = modeloOntologiaProcesso.getOntClass(NS + (ati.getAnotacao().getNome() != null
                            && !ati.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(ati.getAnotacao().getNome()))
                                    : "artft-".concat(ati.getAnotacao().getIdElemento())));

                    if (anotacaoClas == null) {
                        anotacaoClas = modeloOntologiaProcesso.createClass(NS + (ati.getAnotacao().getNome() != null
                                && !ati.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(ati.getAnotacao().getNome()))
                                        : "artft-".concat(ati.getAnotacao().getIdElemento())));
                    }

                    if (atividadeClas != null && anotacaoClas != null) {
                        atividadeClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isAnnotatedBy"), anotacaoClas));
                    }
                }
            }
        }

        if (processo.getEventoList() != null && !processo.getEventoList().isEmpty()) {
            //Preenchimento dos eventos
            for (Evento evento : processo.getEventoList()) {
                OntClass eventoSuperClas;

                if (evento.getTipo() == null) {
                    eventoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "Event");
                } else if (modeloOntologiaProcesso.getOntClass(NS + "evt-" + evento.getTipo().name()) == null) {
                    eventoSuperClas = modeloOntologiaProcesso.createClass(NS + "evt-" + evento.getTipo().name());
                    modeloOntologiaProcesso.getOntClass(NS + "Event").addSubClass(eventoSuperClas);
                } else {
                    eventoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "evt-" + evento.getTipo().name());
                }

                OntClass eventoClas = modeloOntologiaProcesso.getOntClass(NS + (evento.getNome() != null
                        && !evento.getNome().isEmpty() ? "evt-".concat(TextUtils.formataNome(evento.getNome()))
                                : "evt-".concat(evento.getIdElemento())));

                if (eventoClas == null) {
                    eventoClas = modeloOntologiaProcesso.createClass(NS + (evento.getNome() != null
                            && !evento.getNome().isEmpty() ? "evt-".concat(TextUtils.formataNome(evento.getNome()))
                                    : "evt-".concat(evento.getIdElemento())));
                }

                eventoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), evento.getIdElemento());
                eventoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(evento.getNome(), processo));
                eventoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), evento.getDescricao() != null
                        ? evento.getDescricao() : "No Description");
                eventoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"), evento.getDocumentacao() != null
                        ? evento.getDocumentacao() : "No Documentation");

                eventoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                        modeloOntologiaProcesso.getDatatypeProperty(NS + "eventTrigger"),
                        modeloOntologiaProcesso.getOntClass(NS + evento.getGatilho().name())));
                eventoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                eventoSuperClas.addSubClass(eventoClas);

                List<ProduzSaida> produzSaidaList = processo.getProduzSaidaList() == null
                        || processo.getProduzSaidaList().isEmpty() ? null
                                : processo.getProduzSaidaList().stream().filter(e -> e.getElemento().equals(evento))
                                .collect(Collectors.toList());
                if (produzSaidaList != null && !produzSaidaList.isEmpty()) {
                    for (ProduzSaida proSai : produzSaidaList) {
                        eventoClas.addSuperClass(createCardinalityQRestriction(modeloOntologiaProcesso, null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "producesOutput"), 1,
                                modeloOntologiaProcesso.getOntClass(NS + (proSai.getArtefato().getNome() != null
                                        && !proSai.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(proSai.getArtefato().getNome()))
                                                : "artft-".concat(proSai.getArtefato().getIdElemento())))));
                    }
                }

                if (evento.getAtributoEstendidoList() != null && !evento.getAtributoEstendidoList().isEmpty()) {
                    OntClass atributoEstendidoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "ExtendedAttribute");
                    RDFNode[] attrClasses = new RDFNode[evento.getAtributoEstendidoList().size()];
                    int i = 0;

                    for (AtributoEstendido atrEst : evento.getAtributoEstendidoList()) {
                        OntClass atributoEstendidoClas = geraConceitoAtributoEstendido(NS, atrEst, processo, evento);

                        attrClasses[i] = atributoEstendidoClas;

                        i++;

                        atributoEstendidoSuperClas.addSubClass(atributoEstendidoClas);

                        atributoEstendidoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isExtendedAttributeOf"), eventoClas));
                    }

                    if (attrClasses.length > 0) {
                        RDFList listClasses = modeloOntologiaProcesso.createList(attrClasses);

                        eventoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "hasExtendedAttribute"),
                                modeloOntologiaProcesso.createIntersectionClass(null, listClasses)));
                    }
                }
            }

            //Preenche as propriedades de objeto dos eventos que possuem grupos
            List<Evento> eventoAgrupadoList = processo.getEventoList().stream()
                    .filter(eve -> eve.getGrupo() != null && eve.getGrupo().getIdElemento() != null
                            && !eve.getGrupo().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (eventoAgrupadoList != null && !eventoAgrupadoList.isEmpty()) {
                OntClass eventoClas;
                OntClass grupoClas;

                for (Evento eve : eventoAgrupadoList) {
                    eventoClas = modeloOntologiaProcesso.getOntClass(NS + (eve.getNome() != null
                            && !eve.getNome().isEmpty() ? "evt-".concat(TextUtils.formataNome(eve.getNome()))
                                    : "evt-".concat(eve.getIdElemento())));
                    grupoClas = modeloOntologiaProcesso.getOntClass(NS + (eve.getGrupo().getNome() != null
                            && !eve.getGrupo().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(eve.getGrupo().getNome()))
                                    : "artft-".concat(eve.getGrupo().getIdElemento())));

                    if (eventoClas != null && grupoClas != null) {
                        eventoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfGroup"), grupoClas));
                    }
                }
            }

            //Preenche as propriedades de objeto dos eventos que possuem anotação
            List<Evento> eventoAnotacaoList = processo.getEventoList().stream()
                    .filter(eve -> eve.getAnotacao() != null && eve.getAnotacao().getIdElemento() != null
                            && !eve.getAnotacao().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (eventoAnotacaoList != null && !eventoAnotacaoList.isEmpty()) {
                OntClass eventoClas;
                OntClass anotacaoClas;

                for (Evento eve : eventoAnotacaoList) {
                    eventoClas = modeloOntologiaProcesso.getOntClass(NS + (eve.getNome() != null
                            && !eve.getNome().isEmpty() ? "evt-".concat(TextUtils.formataNome(eve.getNome()))
                                    : "eve_".concat(eve.getIdElemento())));
                    anotacaoClas = modeloOntologiaProcesso.getOntClass(NS + (eve.getAnotacao().getNome() != null
                            && !eve.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(eve.getAnotacao().getNome()))
                                    : "artft-".concat(eve.getAnotacao().getIdElemento())));

                    if (anotacaoClas == null) {
                        anotacaoClas = modeloOntologiaProcesso.createClass(NS + (eve.getAnotacao().getNome() != null
                                && !eve.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(eve.getAnotacao().getNome()))
                                        : "artft-".concat(eve.getAnotacao().getIdElemento())));
                    }

                    if (eventoClas != null && anotacaoClas != null) {
                        eventoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isAnnotatedBy"), anotacaoClas));
                    }
                }
            }
        }

        if (processo.getGatewayList() != null && !processo.getGatewayList().isEmpty()) {
            //Preenchimento dos gateways
            for (Gateway gateway : processo.getGatewayList()) {
                OntClass gatewaySuperClas;

                if (gateway.getTipo() == null) {
                    gatewaySuperClas = modeloOntologiaProcesso.getOntClass(NS + "Gateway");
                } else if (modeloOntologiaProcesso.getOntClass(NS + "gatw-" + gateway.getTipo().name()) == null) {
                    gatewaySuperClas = modeloOntologiaProcesso.createClass(NS + "gatw-" + gateway.getTipo().name());
                    modeloOntologiaProcesso.getOntClass(NS + "Gateway").addSubClass(gatewaySuperClas);
                } else {
                    gatewaySuperClas = modeloOntologiaProcesso.getOntClass(NS + "gatw-" + gateway.getTipo().name());
                }

                OntClass gatewayClas = modeloOntologiaProcesso.getOntClass(NS + (gateway.getNome() != null
                        && !gateway.getNome().isEmpty() ? "gatw-".concat(TextUtils.formataNome(gateway.getNome()))
                                : "gatw-".concat(gateway.getIdElemento())));

                if (gatewayClas == null) {
                    gatewayClas = modeloOntologiaProcesso.createClass(NS + (gateway.getNome() != null
                            && !gateway.getNome().isEmpty() ? "gatw-".concat(TextUtils.formataNome(gateway.getNome()))
                                    : "gatw-".concat(gateway.getIdElemento())));
                }

                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), gateway.getIdElemento());
                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                        TextUtils.geraNomeDetalhadoElemento(gateway.getNome(), processo));
                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"),
                        gateway.getDescricao() != null ? gateway.getDescricao() : "No Description");
                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"),
                        gateway.getDocumentacao() != null ? gateway.getDocumentacao() : "No Documentation");

                gatewayClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                        modeloOntologiaProcesso.getDatatypeProperty(NS + "gatewayDirection"),
                        modeloOntologiaProcesso.getOntClass(NS + gateway.getDirecao().name())));

                if (gateway.getIsRegraDeNegocio()) {
                    String businessRule = gateway.getRegraDeNegocio() != null && !gateway.getRegraDeNegocio().isEmpty()
                            ? gateway.getRegraDeNegocio() : gateway.getDescricao() != null && !gateway.getDescricao().isEmpty()
                                    ? gateway.getDescricao() : gateway.getDocumentacao() != null && !gateway.getDocumentacao().isEmpty()
                                            ? gateway.getDocumentacao() : gateway.getNome() != null && !gateway.getNome().isEmpty()
                                                    ? gateway.getNome() : "No Description for the Business Rule";

                    gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-isBusinessRule"),
                            "true", XSDDatatype.XSDboolean);
                    gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i8-businessRule"),
                            businessRule);
                } else {
                    gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-isBusinessRule"),
                            "false", XSDDatatype.XSDboolean);
                }
                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i6-isFunctionalRequirement"),
                        gateway.getIsRequisitoFuncional() ? "true" : "false", XSDDatatype.XSDboolean);
                gatewayClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i7-isNonFunctionalRequirement"),
                        gateway.getIsRequisitoNaoFuncional() ? "true" : "false", XSDDatatype.XSDboolean);

                gatewayClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                gatewaySuperClas.addSubClass(gatewayClas);

                if (gateway.getAtributoEstendidoList() != null && !gateway.getAtributoEstendidoList().isEmpty()) {
                    OntClass atributoEstendidoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "ExtendedAttribute");
                    RDFNode[] attrClasses = new RDFNode[gateway.getAtributoEstendidoList().size()];
                    int i = 0;

                    for (AtributoEstendido atrEst : gateway.getAtributoEstendidoList()) {
                        OntClass atributoEstendidoClas = geraConceitoAtributoEstendido(NS, atrEst, processo, gateway);

                        attrClasses[i] = atributoEstendidoClas;

                        i++;

                        atributoEstendidoSuperClas.addSubClass(atributoEstendidoClas);

                        atributoEstendidoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isExtendedAttributeOf"), gatewayClas));
                    }

                    if (attrClasses.length > 0) {
                        RDFList listClasses = modeloOntologiaProcesso.createList(attrClasses);

                        gatewayClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "hasExtendedAttribute"),
                                modeloOntologiaProcesso.createIntersectionClass(null, listClasses)));
                    }
                }
            }

            //Preenche as propriedades de objeto dos gateways que possuem grupos
            List<Gateway> gatewayAgrupadoList = processo.getGatewayList().stream()
                    .filter(gat -> gat.getGrupo() != null && gat.getGrupo().getIdElemento() != null
                            && !gat.getGrupo().getIdElemento().isEmpty()).collect(Collectors.toList());
            if (gatewayAgrupadoList != null && !gatewayAgrupadoList.isEmpty()) {
                OntClass gatewayClas;
                OntClass grupoClas;

                for (Gateway gat : gatewayAgrupadoList) {
                    gatewayClas = modeloOntologiaProcesso.getOntClass(NS + (gat.getNome() != null
                            && !gat.getNome().isEmpty() ? "gatw-".concat(TextUtils.formataNome(gat.getNome()))
                                    : "gatw-".concat(gat.getIdElemento())));
                    grupoClas = modeloOntologiaProcesso.getOntClass(NS + (gat.getGrupo().getNome() != null
                            && !gat.getGrupo().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(gat.getGrupo().getNome()))
                                    : "artft-".concat(gat.getGrupo().getIdElemento())));

                    if (gatewayClas != null && grupoClas != null) {
                        gatewayClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfGroup"), grupoClas));
                    }
                }
            }

            //Preenche as propriedades de objeto dos gateways que possuem anotação
            List<Gateway> gatewayAnotacaoList = processo.getGatewayList().stream()
                    .filter(gat -> gat.getAnotacao() != null && gat.getAnotacao().getIdElemento() != null
                            && !gat.getAnotacao().getIdElemento().isEmpty()).collect(Collectors.toList());

            if (gatewayAnotacaoList != null && !gatewayAnotacaoList.isEmpty()) {
                OntClass gatewayClas;
                OntClass anotacaoClas;

                for (Gateway gat : gatewayAnotacaoList) {
                    gatewayClas = modeloOntologiaProcesso.getOntClass(NS + (gat.getNome() != null
                            && !gat.getNome().isEmpty() ? "gatw-".concat(TextUtils.formataNome(gat.getNome()))
                                    : "gatw-".concat(gat.getIdElemento())));
                    anotacaoClas = modeloOntologiaProcesso.getOntClass(NS + (gat.getAnotacao().getNome() != null
                            && !gat.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(gat.getAnotacao().getNome()))
                                    : "artft-".concat(gat.getAnotacao().getIdElemento())));

                    if (anotacaoClas == null) {
                        anotacaoClas = modeloOntologiaProcesso.createClass(NS + (gat.getAnotacao().getNome() != null
                                && !gat.getAnotacao().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(gat.getAnotacao().getNome()))
                                        : "artft-".concat(gat.getAnotacao().getIdElemento())));
                    }

                    if (gatewayClas != null && anotacaoClas != null) {
                        gatewayClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isAnnotatedBy"), anotacaoClas));
                    }
                }
            }
        }

        if (processo.getSucedidoPorList() != null && !processo.getSucedidoPorList().isEmpty()) {
            List<Elemento> elementosComMaisDeUmSucessor = new ArrayList(), elementosComMaisDeUmAntecessor = new ArrayList();

            processo.getSucedidoPorList().stream().forEach((sucPor) -> {
                final String prefixoElementoOrigem = sucPor.getElementoOrigem() instanceof Atividade
                        ? "actv-" : sucPor.getElementoOrigem() instanceof Gateway ? "gatw-"
                                : sucPor.getElementoOrigem() instanceof Evento ? "evt-"
                                        : "subProcess-";

                OntClass elementoOrigemClas = modeloOntologiaProcesso.getOntClass(NS + (sucPor.getElementoOrigem().getNome() != null
                        && !sucPor.getElementoOrigem().getNome().isEmpty() ? prefixoElementoOrigem.concat(
                                        TextUtils.formataNome(sucPor.getElementoOrigem().getNome()))
                                : prefixoElementoOrigem.concat(sucPor.getElementoOrigem().getIdElemento())));

                final String prefixoElementoDestino = sucPor.getElementoDestino() instanceof Atividade
                        ? "actv-" : sucPor.getElementoDestino() instanceof Gateway ? "gatw-"
                                : sucPor.getElementoDestino() instanceof Evento ? "evt-"
                                        : "subProcess-";

                OntClass elementoDestinoClas = modeloOntologiaProcesso.getOntClass(NS + (sucPor.getElementoDestino().getNome() != null
                        && !sucPor.getElementoDestino().getNome().isEmpty() ? prefixoElementoDestino.concat(
                                        TextUtils.formataNome(sucPor.getElementoDestino().getNome()))
                                : prefixoElementoDestino.concat(sucPor.getElementoDestino().getIdElemento())));

                if (elementoOrigemClas != null && elementoDestinoClas != null) {

                    if (sucPor.getElementoOrigem() instanceof Evento) {
                        elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isActivatedWhenEventIsTriggered"),
                                elementoOrigemClas));
                    }

                    Gateway gatewayOrigem = sucPor.getElementoOrigem() instanceof Gateway
                            ? (Gateway) sucPor.getElementoOrigem() : null;

                    //Preenche a relação entre atividades e gateway
                    if (((gatewayOrigem != null && gatewayOrigem.getTipo().equals(TipoGateway.Exclusive))
                            && (!gatewayOrigem.getFimFluxo()) && (sucPor.getElementoDestino() instanceof Atividade))
                            || (gatewayOrigem != null && !gatewayOrigem.getFimFluxo() && (gatewayOrigem.getTipo().equals(TipoGateway.Inclusive)
                            || gatewayOrigem.getTipo().equals(TipoGateway.Parallel)) && (sucPor.getElementoDestino() instanceof Atividade))) {
                        SucedidoPor sucedidoPorAux = preencheRelacaoAtividadeGateway(modeloOntologiaProcesso, NS, sucPor, processo.getSucedidoPorList(),
                                sucPor.getElementoOrigem(), elementoDestinoClas, isSubProcesso, processoClas);

                        if (sucedidoPorAux != null && sucedidoPorAux.getElementoOrigem() != null
                                && sucedidoPorAux.getElementoDestino() != null) {
                            processo.getSucedidoPorList().stream().filter((sucPorAux) -> ((sucPorAux.getElementoOrigem() != null && sucPorAux.getElementoOrigem().getIdElemento() != null
                                    && !sucPorAux.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucPorAux.getElementoOrigem().getIdElemento().equals(sucedidoPorAux.getElementoOrigem().getIdElemento()))
                                    && (sucPorAux.getElementoDestino() != null && sucPorAux.getElementoDestino().getIdElemento() != null
                                    && !sucPorAux.getElementoDestino().getIdElemento().isEmpty()
                                    && sucPorAux.getElementoDestino().getIdElemento().equals(sucedidoPorAux.getElementoDestino().getIdElemento())))).forEach((sucPorAux) -> {
                                sucPorAux.getElementoOrigem().setFimFluxo(true);
                            });
                        }
                    }

                    if (sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                            && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                            && processo.getSucedidoPorList().stream().filter(ele -> ele.getElementoOrigem().getIdElemento()
                                    .equals(sucPor.getElementoOrigem().getIdElemento())).count() > 1) {
                        if (!elementosComMaisDeUmSucessor.contains(sucPor.getElementoOrigem())) {
                            elementosComMaisDeUmSucessor.add(sucPor.getElementoOrigem());
                        }
                    } else {
                        elementoOrigemClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isSucceededBy"), elementoDestinoClas));
                    }
                    //Propriedade inversa - isPrecededBy
                    if (sucPor.getElementoDestino() != null && sucPor.getElementoDestino().getIdElemento() != null
                            && !sucPor.getElementoDestino().getIdElemento().isEmpty()
                            && processo.getSucedidoPorList().stream().filter(ele -> ele.getElementoDestino().getIdElemento()
                                    .equals(sucPor.getElementoDestino().getIdElemento())).count() > 1) {
                        if (!elementosComMaisDeUmAntecessor.contains(sucPor.getElementoDestino())) {
                            elementosComMaisDeUmAntecessor.add(sucPor.getElementoDestino());
                        }
                    } else {
                        elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPrecededBy"), elementoOrigemClas));
                    }
                }
            });

            //Trata os elementos que possuem mais de um sucessor
            if (!elementosComMaisDeUmSucessor.isEmpty()) {
                List<SucedidoPor> sucedidoPorAuxList;
                OntClass eleOrigemAux, eleDestinoAux;
                RDFList listClasses;
                RDFNode[] elements;
                int i;

                for (Elemento ele : elementosComMaisDeUmSucessor) {
                    sucedidoPorAuxList = processo.getSucedidoPorList() != null && !processo.getSucedidoPorList().isEmpty()
                            ? processo.getSucedidoPorList().stream().filter(sucPor -> sucPor.getElementoOrigem().getIdElemento()
                                    .equals(ele.getIdElemento()) && sucPor.getElementoDestino() != null).collect(Collectors.toList()) : null;

                    if (sucedidoPorAuxList != null && !sucedidoPorAuxList.isEmpty()) {
                        elements = new RDFNode[sucedidoPorAuxList.size()];

                        i = 0;

                        final String prefixoElementoOrigem = ele instanceof Atividade
                                ? "actv-" : ele instanceof Gateway ? "gatw-"
                                        : ele instanceof Evento ? "evt-"
                                                : "subProcess-";

                        eleOrigemAux = modeloOntologiaProcesso.getOntClass(NS + (ele.getNome() != null
                                && !ele.getNome().isEmpty() ? prefixoElementoOrigem.concat(
                                                TextUtils.formataNome(ele.getNome()))
                                        : prefixoElementoOrigem.concat(ele.getIdElemento())));

                        for (SucedidoPor sucPor : sucedidoPorAuxList) {
                            final String prefixoElementoDestino = sucPor.getElementoDestino() instanceof Atividade
                                    ? "actv-" : sucPor.getElementoDestino() instanceof Gateway ? "gatw-"
                                            : sucPor.getElementoDestino() instanceof Evento ? "evt-"
                                                    : "subProcess-";

                            eleDestinoAux = modeloOntologiaProcesso.getOntClass(NS + (sucPor.getElementoDestino().getNome() != null
                                    && !sucPor.getElementoDestino().getNome().isEmpty() ? prefixoElementoDestino.concat(
                                                    TextUtils.formataNome(sucPor.getElementoDestino().getNome()))
                                            : prefixoElementoDestino.concat(sucPor.getElementoDestino().getIdElemento())));

                            if (eleDestinoAux != null) {
                                elements[i] = eleDestinoAux;
                                i++;
                            }
                        }

                        if (elements.length > 0) {
                            listClasses = modeloOntologiaProcesso.createList(elements);

                            eleOrigemAux.addSuperClass(modeloOntologiaProcesso.
                                    createAllValuesFromRestriction(null, modeloOntologiaProcesso.getObjectProperty(NS + "isSucceededBy"),
                                            modeloOntologiaProcesso.createUnionClass(null, listClasses)));
                        }
                    }

                }
            }

            //Trata os elementos que possuem mais de um antecessor
            if (!elementosComMaisDeUmAntecessor.isEmpty()) {
                List<SucedidoPor> sucedidoPorAuxList;
                OntClass eleOrigemAux, eleDestinoAux;
                RDFList listClasses;
                RDFNode[] elements;
                int i;

                for (Elemento ele : elementosComMaisDeUmAntecessor) {
                    sucedidoPorAuxList = processo.getSucedidoPorList() != null && !processo.getSucedidoPorList().isEmpty()
                            ? processo.getSucedidoPorList().stream().filter(sucPor -> sucPor.getElementoDestino().getIdElemento()
                                    .equals(ele.getIdElemento()) && sucPor.getElementoOrigem() != null).collect(Collectors.toList()) : null;

                    if (sucedidoPorAuxList != null && !sucedidoPorAuxList.isEmpty()) {
                        elements = new RDFNode[sucedidoPorAuxList.size()];

                        i = 0;

                        final String prefixoElementoDestino = ele instanceof Atividade
                                ? "actv-" : ele instanceof Gateway ? "gatw-"
                                        : ele instanceof Evento ? "evt-"
                                                : "subProcess-";

                        eleDestinoAux = modeloOntologiaProcesso.getOntClass(NS + (ele.getNome() != null
                                && !ele.getNome().isEmpty() ? prefixoElementoDestino.concat(
                                                TextUtils.formataNome(ele.getNome()))
                                        : prefixoElementoDestino.concat(ele.getIdElemento())));

                        for (SucedidoPor sucPor : sucedidoPorAuxList) {
                            final String prefixoElementoOrigem = sucPor.getElementoOrigem() instanceof Atividade
                                    ? "actv-" : sucPor.getElementoOrigem() instanceof Gateway ? "gatw-"
                                            : sucPor.getElementoOrigem() instanceof Evento ? "evt-"
                                                    : "subProcess-";

                            eleOrigemAux = modeloOntologiaProcesso.getOntClass(NS + (sucPor.getElementoOrigem().getNome() != null
                                    && !sucPor.getElementoOrigem().getNome().isEmpty() ? prefixoElementoOrigem.concat(
                                                    TextUtils.formataNome(sucPor.getElementoOrigem().getNome()))
                                            : prefixoElementoOrigem.concat(sucPor.getElementoOrigem().getIdElemento())));

                            if (eleOrigemAux != null) {
                                elements[i] = eleOrigemAux;
                                i++;
                            }
                        }

                        if (elements.length > 0) {
                            listClasses = modeloOntologiaProcesso.createList(elements);

                            eleDestinoAux.addSuperClass(modeloOntologiaProcesso.
                                    createAllValuesFromRestriction(null, modeloOntologiaProcesso.getObjectProperty(NS + "isPrecededBy"),
                                            modeloOntologiaProcesso.createUnionClass(null, listClasses)));
                        }
                    }

                }
            }
        }

        return modeloOntologiaRetorno;
    }

    private OntModel preencheRelacaoAtividadesExecutadasSemAtores(OntModel modeloOntologia, String NS,
            Atividade atividade, List<Ator> atoresList) {
        OntModel modeloOntologiaRetorno = modeloOntologia;
        OntClass atividadeClas, atorClas = null;
        RDFNode[] elements;
        int i = 0;

        if (atividade != null && atoresList != null && !atoresList.isEmpty()) {
            atividadeClas = modeloOntologiaRetorno.getOntClass(NS + (atividade.getNome() != null
                    && !atividade.getNome().isEmpty() ? "actv-".concat(TextUtils.formataNome(atividade.getNome()))
                            : "actv-".concat(atividade.getIdElemento())));

            elements = new RDFNode[atoresList.size()];

            for (Ator atorAux : atoresList) {
                atorClas = modeloOntologiaRetorno.getOntClass(NS + (atorAux.getNome() != null
                        && !atorAux.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorAux.getNome()))
                                : "actor-".concat(atorAux.getIdElemento())));

                if (atorClas == null) {
                    atorClas = modeloOntologiaRetorno.createClass(NS + (atorAux.getNome() != null
                            && !atorAux.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorAux.getNome()))
                                    : "actor-".concat(atorAux.getIdElemento())));
                }

                elements[i] = atorClas;
                i++;
            }

            if (elements.length == 1 && atorClas != null) {
                atividadeClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                        modeloOntologiaRetorno.getObjectProperty(NS + "isPerformedBy"), atorClas));
            } else if (elements.length > 1) {
                RDFList listClasses = modeloOntologiaRetorno.createList(elements);

                atividadeClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                        modeloOntologiaRetorno.getObjectProperty(NS + "isPerformedBy"),
                        modeloOntologiaRetorno.createUnionClass(null, listClasses)));
            }
        }

        return modeloOntologiaRetorno;
    }

    private OntModel preencheRelacaoAtividadesExecutadas(OntModel modeloOntologia, String NS,
            boolean isSubProcesso, List<ExecutadoPor> executadoPorList) {
        OntModel modeloOntologiaRetorno = modeloOntologia;
        OntClass atividadeSubProcessoClas, atorClas = null;
        List<Ator> atoresList = new ArrayList();
        Atividade ati;

        if (executadoPorList != null && !executadoPorList.isEmpty()) {
            for (ExecutadoPor exePor : executadoPorList) {
                if (!atoresList.contains(exePor.getAtor())) {
                    atoresList.add(exePor.getAtor());
                }
            }

            ati = executadoPorList.get(0).getAtividade();

            atividadeSubProcessoClas = modeloOntologiaRetorno.getOntClass(NS + (ati.getNome() != null
                    && !ati.getNome().isEmpty() ? (isSubProcesso ? "subProcess-" : "actv-").concat(TextUtils.formataNome(ati.getNome()))
                            : (isSubProcesso ? "subProcess-" : "actv-").concat(ati.getIdElemento())));

            if (atividadeSubProcessoClas != null && !atoresList.isEmpty()) {
                RDFNode[] elements;
                int i = 0;

                elements = new RDFNode[atoresList.size()];

                for (Ator atorAux : atoresList) {
                    atorClas = modeloOntologiaRetorno.getOntClass(NS + (atorAux.getNome() != null
                            && !atorAux.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorAux.getNome()))
                                    : "actor-".concat(atorAux.getIdElemento())));

                    if (atorClas == null) {
                        atorClas = modeloOntologiaRetorno.createClass(NS + (atorAux.getNome() != null
                                && !atorAux.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorAux.getNome()))
                                        : "actor-".concat(atorAux.getIdElemento())));
                    }

                    elements[i] = atorClas;
                    i++;
                }

                if (elements.length == 1 && atorClas != null) {
                    atividadeSubProcessoClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                            modeloOntologiaRetorno.getObjectProperty(NS + "isPerformedBy"), atorClas));
                } else if (elements.length > 1) {
                    RDFList listClasses = modeloOntologiaRetorno.createList(elements);

                    atividadeSubProcessoClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                            modeloOntologiaRetorno.getObjectProperty(NS + "isPerformedBy"),
                            modeloOntologiaRetorno.createUnionClass(null, listClasses)));
                }
            }
        }

        return modeloOntologiaRetorno;
    }

    private OntModel preencheRelacaoExecutoresDeAtividades(OntModel modeloOntologia, String NS,
            List<Processo> processoList) {
        OntModel modeloOntologiaRetorno = modeloOntologia;
        List<Ator> atorAuxList = new ArrayList();
        List<ExecutadoPor> executadoPorList = new ArrayList();
        List<SubProcesso> subProcessoList = new ArrayList();

        for (Processo pro : processoList) {
            if (pro.getSubProcessoList() != null && !pro.getSubProcessoList().isEmpty()) {
                for (SubProcesso sub : pro.getSubProcessoList()) {
                    if (!subProcessoList.contains(sub)) {
                        subProcessoList.add(sub);
                    }

                    //Apenas para subprocessos com mais de um ator
                    if (sub.getAtorList() != null && !sub.getAtorList().isEmpty()
                            && sub.getAtorList().size() > 1) {
                        for (Ator ator : sub.getAtorList()) {
                            if (!atorAuxList.contains(ator)) {
                                atorAuxList.add(ator);
                            }
                        }
                    }

                    if (sub.getExecutadoPorList() != null && !sub.getExecutadoPorList().isEmpty()
                            && sub.getExecutadoPorList().size() > 1) {
                        for (ExecutadoPor exePor : sub.getExecutadoPorList()) {
                            executadoPorList.add(exePor);

                            if (!atorAuxList.contains(exePor.getAtor())) {
                                atorAuxList.add(exePor.getAtor());
                            }
                        }
                    }
                }
            }

            if (pro.getAtorList() != null && !pro.getAtorList().isEmpty()) {
                for (Ator ator : pro.getAtorList()) {
                    if (!atorAuxList.contains(ator)) {
                        atorAuxList.add(ator);
                    }
                }
            }
            if (pro.getExecutadoPorList() != null && !pro.getExecutadoPorList().isEmpty()) {
                for (ExecutadoPor exePor : pro.getExecutadoPorList()) {
                    executadoPorList.add(exePor);
                }
            }
        }

        if (!atorAuxList.isEmpty() && !executadoPorList.isEmpty()) {
            for (Ator ator : atorAuxList) {
                OntClass atorClas = modeloOntologiaProcesso.getOntClass(NS + (ator.getNome() != null
                        && !ator.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(ator.getNome()))
                                : "actor-".concat(ator.getIdElemento())));

                List<ExecutadoPor> executadoPorListAux = executadoPorList.stream().filter(e -> e.getAtor().equals(ator))
                        .collect(Collectors.toList());

                if (executadoPorListAux != null && !executadoPorListAux.isEmpty()) {
                    OntClass atividadeAuxClas;
                    RDFList listClasses;
                    RDFNode[] elements;
                    int i = 0;
                    boolean isSubProcessoAux;
                    String prefixoElemento;

                    elements = new RDFNode[executadoPorListAux.size()];

                    for (ExecutadoPor exePor : executadoPorListAux) {
                        if (exePor.getAtividade() != null) {
                            isSubProcessoAux = !subProcessoList.isEmpty()
                                    ? subProcessoList.stream().filter(sub -> sub.getIdElemento().equals(exePor.getAtividade().getIdElemento())).count() > 0
                                    : false;

                            prefixoElemento = isSubProcessoAux ? "subProcess-" : "actv-";

                            atividadeAuxClas = modeloOntologiaProcesso.getOntClass(NS + (exePor.getAtividade().getNome()
                                    != null && !exePor.getAtividade().getNome().isEmpty() ? prefixoElemento.concat(
                                                    TextUtils.formataNome(exePor.getAtividade().getNome()))
                                            : prefixoElemento.concat(exePor.getAtividade().getIdElemento())));

                            if (atividadeAuxClas != null) {
                                elements[i] = atividadeAuxClas;
                                i++;
                            }
                        }
                    }

                    if (elements.length > 0) {
                        listClasses = modeloOntologiaProcesso.createList(elements);

                        atorClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isExecutorOfActivity"),
                                modeloOntologiaProcesso.createUnionClass(null, listClasses)));
                    }
                }
            }
        }

        return modeloOntologiaRetorno;
    }

    private OntModel preencheRelacaoAtoresProcessos(OntModel modeloOntologia, String NS,
            List<Processo> processoList) {
        OntModel modeloOntologiaRetorno = modeloOntologia;
        List<Ator> atorAuxList = new ArrayList();

        //Preenche os atores
        for (Processo pro : processoList) {
            if (pro.getAtorList() != null && !pro.getAtorList().isEmpty()) {
                for (Ator ato : pro.getAtorList()) {
                    if (!atorAuxList.contains(ato)) {
                        atorAuxList.add(ato);
                    }
                }
            }

            if (pro.getSubProcessoList() != null && !pro.getSubProcessoList().isEmpty()) {
                for (SubProcesso subPro : pro.getSubProcessoList()) {
                    if (subPro.getAtorList() != null && !subPro.getAtorList().isEmpty()) {
                        for (Ator ato : subPro.getAtorList()) {
                            if (!atorAuxList.contains(ato)) {
                                atorAuxList.add(ato);
                            }
                        }
                    }
                }
            }
        }

        if (!atorAuxList.isEmpty()) {
            List<Processo> processoListAux = new ArrayList();
            List<SubProcesso> subProcessoListAux = new ArrayList();
            OntClass atorClas;
            RDFList listClasses;
            RDFNode[] elements;
            int i;

            for (Ator atorAux : atorAuxList) {
                atorClas = modeloOntologiaProcesso.getOntClass(NS + (atorAux.getNome() != null
                        && !atorAux.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorAux.getNome()))
                                : "actor-".concat(atorAux.getIdElemento())));

                for (Processo pro : processoList) {
                    if (pro.getAtorList() != null && !pro.getAtorList().isEmpty()
                            && pro.getAtorList().stream().filter(ato -> ato.getIdElemento()
                                    .equals(atorAux.getIdElemento())).count() > 0
                            && !processoListAux.contains(pro)) {
                        processoListAux.add(pro);
                    }

                    //Busca os subprocessos do ator
                    if (pro.getSubProcessoList() != null && !pro.getSubProcessoList().isEmpty()) {
                        for (SubProcesso subProAux : pro.getSubProcessoList()) {
                            if (subProAux.getAtorList() != null && !subProAux.getAtorList().isEmpty()
                                    && subProAux.getAtorList().stream().filter(ato -> ato.getIdElemento()
                                            .equals(atorAux.getIdElemento())).count() > 0
                                    && !subProcessoListAux.contains(subProAux)) {
                                subProcessoListAux.add(subProAux);
                            }
                        }
                    }
                }

                //Preenche a relação isPartOfProcess
                if (!processoListAux.isEmpty()) {
                    OntClass proClas = null;
                    elements = new RDFNode[processoListAux.size()];
                    i = 0;

                    for (Processo proAux : processoListAux) {
                        proClas = modeloOntologiaProcesso.getOntClass(NS + (proAux.getNome() != null
                                && !proAux.getNome().isEmpty() ? "process-".concat(TextUtils.formataNome(proAux.getNome()))
                                        : "process-".concat(proAux.getIdElemento())));

                        if (proClas != null) {
                            elements[i] = proClas;
                            i++;
                        }
                    }

                    if (elements.length == 1 && proClas != null) {
                        atorClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                                modeloOntologiaRetorno.getObjectProperty(NS + "isPartOfProcess"), proClas));
                    } else if (elements.length > 1) {
                        listClasses = modeloOntologiaRetorno.createList(elements);

                        atorClas.addSuperClass(modeloOntologiaRetorno.createAllValuesFromRestriction(null,
                                modeloOntologiaRetorno.getObjectProperty(NS + "isPartOfProcess"),
                                modeloOntologiaRetorno.createUnionClass(null, listClasses)));
                    }
                }

                //Preenche a relação isPartOfSubProcess
                if (!subProcessoListAux.isEmpty()) {
                    OntClass subProClas = null;
                    elements = new RDFNode[subProcessoListAux.size()];
                    i = 0;

                    for (SubProcesso subProAux : subProcessoListAux) {
                        subProClas = modeloOntologiaProcesso.getOntClass(NS + (subProAux.getNome() != null
                                && !subProAux.getNome().isEmpty() ? "subProcess-".concat(TextUtils.formataNome(subProAux.getNome()))
                                        : "subProcess-".concat(subProAux.getIdElemento())));

                        if (subProClas != null) {
                            elements[i] = subProClas;
                            i++;
                        }
                    }

                    if (elements.length == 1 && subProClas != null) {
                        atorClas.addSuperClass(modeloOntologiaRetorno.createSomeValuesFromRestriction(null,
                                modeloOntologiaRetorno.getObjectProperty(NS + "isPartOfSubProcess"), subProClas));
                    } else if (elements.length > 1) {
                        listClasses = modeloOntologiaRetorno.createList(elements);

                        atorClas.addSuperClass(modeloOntologiaRetorno.createSomeValuesFromRestriction(null,
                                modeloOntologiaRetorno.getObjectProperty(NS + "isPartOfSubProcess"),
                                modeloOntologiaRetorno.createUnionClass(null, listClasses)));
                    }
                }
            }
        }

        return modeloOntologiaRetorno;
    }

    private OntClass createCardinalityQRestriction(
            OntModel model,
            String uri,
            Property prop,
            int cardinality,
            OntClass clas) {
        OntClass klass = model.createCardinalityRestriction(uri, prop, cardinality);
        klass.removeAll(OWL.cardinality);
        klass.addLiteral(OWL2.qualifiedCardinality, cardinality);
        klass.addProperty(OWL2.onClass, clas);
        return klass;
    }

    private void preencheRelacaoAtividadeGatewayExclusivo(OntModel modeloOntologiaProcesso, String NS, SucedidoPor sucedidoPorCorrente,
            List<SucedidoPor> sucedidoPorList, Elemento elementoOrigem, OntClass elementoDestinoClas) {
        List<SucedidoPor> sucedidoPorAnterioresList = sucedidoPorList != null && !sucedidoPorList.isEmpty()
                ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoDestino().getIdElemento()
                        .equals(elementoOrigem.getIdElemento())).collect(Collectors.toList()) : null;

        if (sucedidoPorAnterioresList != null && !sucedidoPorAnterioresList.isEmpty()
                && sucedidoPorAnterioresList.get(0).getElementoOrigem() instanceof Atividade) {

            String nomePropObjeto = "isExecutedWhen_";
            String nomeElemento = sucedidoPorAnterioresList.get(0).getElementoOrigem().getNome() != null
                    && !sucedidoPorAnterioresList.get(0).getElementoOrigem().getNome().isEmpty() ? "actv-".concat(
                    TextUtils.formataNome(sucedidoPorAnterioresList.get(0).getElementoOrigem().getNome()))
                    : "actv-".concat(sucedidoPorAnterioresList.get(0).getElementoOrigem().getIdElemento());

            nomePropObjeto = nomePropObjeto.concat(nomeElemento).concat("_OutputIs");

            ObjectProperty propRelacao = modeloOntologiaProcesso.createObjectProperty(NS + nomePropObjeto);

            if (sucedidoPorCorrente.getNomeTransicao() != null && !sucedidoPorCorrente.getNomeTransicao().isEmpty()) {

                if (TextUtils.verificaDecisaoSim(sucedidoPorCorrente.getNomeTransicao())) {
                    elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            propRelacao, modeloOntologiaProcesso.getOntClass(NS + "OutputIsYes")));
                } else if (TextUtils.verificaDecisaoNao(sucedidoPorCorrente.getNomeTransicao())) {
                    elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            propRelacao, modeloOntologiaProcesso.getOntClass(NS + "OutputIsNo")));
                } else {
                    OntClass clasOutput = modeloOntologiaProcesso.createClass(NS + "OutputIs"
                            .concat(TextUtils.formataNome(sucedidoPorCorrente.getNomeTransicao())));
                    elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            propRelacao, clasOutput));
                    modeloOntologiaProcesso.getOntClass(NS + "GatewaysOutputs").addSubClass(clasOutput);
                }
            } else {
                elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        propRelacao, modeloOntologiaProcesso.getOntClass(NS + "OutputIsUndefined")));
            }
        }
    }

    private SucedidoPor preencheRelacaoAtividadeGateway(OntModel modeloOntologiaProcesso, String NS, SucedidoPor sucedidoPorCorrente,
            List<SucedidoPor> sucedidoPorList, Elemento elementoOrigem, OntClass elementoDestinoClas, boolean isSubProcesso, OntClass processoClas) {
        Gateway gateway = (Gateway) elementoOrigem;

        if (gateway.getTipo() != null && gateway.getTipo().equals(TipoGateway.Exclusive)) {
            preencheRelacaoAtividadeGatewayExclusivo(modeloOntologiaProcesso, NS, sucedidoPorCorrente,
                    sucedidoPorList, elementoOrigem, elementoDestinoClas);
        } else if (gateway.getTipo() != null && (gateway.getTipo().equals(TipoGateway.Parallel)
                || gateway.getTipo().equals(TipoGateway.Inclusive))) {
            boolean fimFluxo;
            boolean isParallel = gateway.getTipo().equals(TipoGateway.Parallel);
            Gateway gatewayFim;

            SucedidoPor sucedidoPor;
            TipoGateway tipoGatewayCorrente;
            List<SucedidoPor> sucedidoPorFluxoList = sucedidoPorList != null
                    && !sucedidoPorList.isEmpty() ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem()
                                    != null && sucPor.getElementoOrigem().getIdElemento() != null && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucPor.getElementoOrigem().getIdElemento().equals(elementoOrigem.getIdElemento()))
                            .collect(Collectors.toList()) : null;
            List<Elemento> elementosIniciaisFluxoList;
            List<Elemento> elementoList = null;

            if (sucedidoPorFluxoList != null && !sucedidoPorFluxoList.isEmpty()) {
                elementosIniciaisFluxoList = new ArrayList();

                sucedidoPorFluxoList.stream().forEach((sucPor) -> {
                    elementosIniciaisFluxoList.add(sucPor.getElementoDestino());
                });
            } else {
                elementosIniciaisFluxoList = null;
            }

            sucedidoPor = sucedidoPorList != null && !sucedidoPorList.isEmpty()
                    ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                            && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                            && sucPor.getElementoOrigem().getIdElemento().equals(elementoOrigem.getIdElemento())).count() > 0
                    ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                            && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                            && sucPor.getElementoOrigem().getIdElemento().equals(elementoOrigem.getIdElemento())).findFirst().get()
                    : null : null;

            if (sucedidoPorList != null && !sucedidoPorList.isEmpty() && sucedidoPor != null
                    && sucedidoPor.getElementoDestino() != null) {
                //Encontra o gateway final do fluxo.
                boolean encontrouGateway = false;

                while (!encontrouGateway) {
                    for (SucedidoPor sucPor : sucedidoPorList) {
                        if (sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                                && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                && sucPor.getElementoOrigem().getIdElemento().equals(sucedidoPor.getElementoDestino()
                                        .getIdElemento())) {
                            sucedidoPor = sucPor;
                            break;
                        }
                    }

                    tipoGatewayCorrente = sucedidoPor.getElementoDestino() != null
                            && sucedidoPor.getElementoDestino() instanceof Gateway
                            && ((Gateway) sucedidoPor.getElementoDestino()) != null
                                    ? ((Gateway) sucedidoPor.getElementoDestino()).getTipo() : null;

                    encontrouGateway = tipoGatewayCorrente != null;
//                            && ((isParallel && tipoGatewayCorrente.equals(TipoGateway.Parallel))
//                            || (!isParallel && tipoGatewayCorrente.equals(TipoGateway.Inclusive)));
                }

                gatewayFim = ((Gateway) sucedidoPor.getElementoDestino());
            } else {
                gatewayFim = null;
            }

            if (gatewayFim != null && elementosIniciaisFluxoList != null && !elementosIniciaisFluxoList.isEmpty()
                    && sucedidoPorList != null && !sucedidoPorList.isEmpty()) {
                boolean isExclusiveGateway, isSuceededByEndEvent;

                for (Elemento ele : elementosIniciaisFluxoList) {
                    fimFluxo = false;

                    sucedidoPor = !sucedidoPorList.isEmpty() ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null
                            && sucPor.getElementoOrigem().getIdElemento() != null
                            && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                            && sucPor.getElementoOrigem().getIdElemento().equals(ele.getIdElemento())).count() > 0
                            ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null
                                    && sucPor.getElementoOrigem().getIdElemento() != null
                                    && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucPor.getElementoOrigem().getIdElemento().equals(ele.getIdElemento())).findFirst().get()
                            : null : null;

                    isSuceededByEndEvent = sucedidoPor != null && sucedidoPor.getElementoDestino() != null
                            && sucedidoPor.getElementoDestino() instanceof Evento
                            && ((Evento) sucedidoPor.getElementoDestino()).getTipo() != null
                            && ((Evento) sucedidoPor.getElementoDestino()).getTipo().equals(TipoEvento.End);

                    while (!fimFluxo && !isSuceededByEndEvent) {
                        if (sucedidoPor != null && sucedidoPor.getElementoOrigem() != null) {
                            if (elementoList == null) {
                                elementoList = new ArrayList();
                            }

                            elementoList.add(sucedidoPor.getElementoOrigem());

                            isExclusiveGateway = sucedidoPor.getElementoOrigem() instanceof Gateway
                                    && ((Gateway) sucedidoPor.getElementoOrigem()).getTipo() != null
                                    && ((Gateway) sucedidoPor.getElementoOrigem()).getTipo().equals(TipoGateway.Exclusive);

                            if (isExclusiveGateway) {
                                final String prefixoElementoDestino = sucedidoPor.getElementoDestino() instanceof Atividade
                                        ? "actv-" : sucedidoPor.getElementoDestino() instanceof Gateway ? "gatw-"
                                                : sucedidoPor.getElementoDestino() instanceof Evento ? "evt-"
                                                        : "subProcess-";

                                OntClass elementoDestinoClasAux = modeloOntologiaProcesso.getOntClass(NS + (sucedidoPor.getElementoDestino()
                                        .getNome() != null && !sucedidoPor.getElementoDestino().getNome().isEmpty()
                                                ? prefixoElementoDestino.concat(TextUtils.formataNome(sucedidoPor.getElementoDestino().getNome()))
                                                : prefixoElementoDestino.concat(sucedidoPor.getElementoDestino().getIdElemento())));

                                preencheRelacaoAtividadeGatewayExclusivo(modeloOntologiaProcesso, NS, sucedidoPor, sucedidoPorList,
                                        sucedidoPor.getElementoOrigem(), elementoDestinoClasAux);
                            }

                            fimFluxo = !sucedidoPor.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucedidoPor.getElementoOrigem().getIdElemento().equals(gatewayFim.getIdElemento());

                            for (SucedidoPor sucPor : sucedidoPorList) {
                                if (sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                                        && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                        && sucPor.getElementoOrigem().getIdElemento().equals(sucedidoPor.getElementoDestino()
                                                .getIdElemento())) {
                                    sucedidoPor = sucPor;
                                    break;
                                }
                            }

                            isSuceededByEndEvent = sucedidoPor.getElementoDestino() != null
                                    && sucedidoPor.getElementoDestino() instanceof Evento
                                    && ((Evento) sucedidoPor.getElementoDestino()).getTipo() != null
                                    && ((Evento) sucedidoPor.getElementoDestino()).getTipo().equals(TipoEvento.End);
                        }
                    }
                }
            }

            //Busca o elemento sucessor ao gateway do fim do fluxo
            sucedidoPor = gatewayFim != null && gatewayFim.getIdElemento() != null
                    && !gatewayFim.getIdElemento().isEmpty()
                    && sucedidoPorList != null && !sucedidoPorList.isEmpty()
                            ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                                    && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucPor.getElementoOrigem().getIdElemento().equals(gatewayFim.getIdElemento())).count() > 0
                            ? sucedidoPorList.stream().filter(sucPor -> sucPor.getElementoOrigem() != null && sucPor.getElementoOrigem().getIdElemento() != null
                                    && !sucPor.getElementoOrigem().getIdElemento().isEmpty()
                                    && sucPor.getElementoOrigem().getIdElemento().equals(gatewayFim.getIdElemento())).findFirst().get()
                            : null : null;

            sucedidoPorCorrente = sucedidoPor;

            boolean defineRelacao = sucedidoPor != null && sucedidoPor.getElementoDestino() != null
                    && (sucedidoPor.getElementoDestino() instanceof Atividade
                    || sucedidoPor.getElementoDestino() instanceof SubProcesso);

            //Preenche o fluxo
            if (defineRelacao && sucedidoPor != null && sucedidoPor.getElementoDestino() != null
                    && elementoList != null && !elementoList.isEmpty()) {
                String nomeFluxo = "flow-".concat(sucedidoPor.getElementoDestino().getNome() != null
                        && !sucedidoPor.getElementoDestino().getNome().isEmpty()
                                ? TextUtils.formataNome(sucedidoPor.getElementoDestino().getNome())
                                : sucedidoPor.getElementoDestino().getIdElemento());

                OntClass flowSuperClas = modeloOntologiaProcesso.getOntClass(NS + (isParallel ? "ParallelFlow" : "InclusiveFlow"));

                OntClass flowClas = modeloOntologiaProcesso.createClass(NS + nomeFluxo);

                flowSuperClas.addSubClass(flowClas);

                //Relaciona o fluxo ao processo/subprocesso à que o mesmo pertence
                flowClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + (isSubProcesso ? "isPartOfSubProcess"
                                : "isPartOfProcess")), processoClas));

                for (Elemento ele : elementoList) {
                    String prefixoElementoCorrente = ele instanceof Atividade
                            ? "actv-" : ele instanceof Gateway ? "gatw-"
                                    : ele instanceof Evento ? "evt-"
                                            : "subProcess-";

                    OntClass elementoCorrenteClas = modeloOntologiaProcesso.getOntClass(NS + (ele.getNome() != null
                            && !ele.getNome().isEmpty() ? prefixoElementoCorrente.concat(
                                            TextUtils.formataNome(ele.getNome()))
                                    : prefixoElementoCorrente.concat(ele.getIdElemento())));

                    elementoCorrenteClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfFlow"), flowClas));
                }

                String prefixoElementoSucessor = sucedidoPor.getElementoDestino() instanceof Atividade
                        ? "actv-" : sucedidoPor.getElementoDestino() instanceof Gateway ? "gatw-"
                                : sucedidoPor.getElementoDestino() instanceof Evento ? "evt-"
                                        : "subProcess-";

                OntClass elementoSucessorClas = modeloOntologiaProcesso.getOntClass(NS + (sucedidoPor.getElementoDestino().getNome() != null
                        && !sucedidoPor.getElementoDestino().getNome().isEmpty() ? prefixoElementoSucessor.concat(
                                        TextUtils.formataNome(sucedidoPor.getElementoDestino().getNome()))
                                : prefixoElementoSucessor.concat(sucedidoPor.getElementoDestino().getIdElemento())));

                if (elementoSucessorClas != null) {
                    elementoSucessorClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            modeloOntologiaProcesso.getObjectProperty(NS + (isParallel ? "isExecutedAfterParallelExecutionOf"
                                    : "isExecutedAfterInclusiveExecutionOf")), flowClas));
                }
            }
        } else {
            sucedidoPorCorrente = null;
        }

        return sucedidoPorCorrente;
    }

    /**
     * Gera a ontologia com base na estrutura de processos preenchida.
     *
     * @return
     * @throws Exception
     */
    public boolean geraOntologia() throws Exception {
        modeloOntologiaProcesso = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
        Model model = ModelFactory.createDefaultModel();

        //Criação do prefixo que será utilizado nos elementos da ontologia
        String fonte = "http://www.semanticweb.org/ontologiaDeProcesso/".concat(nomeOntologia);
        String NS = fonte + "#";

        modeloOntologiaProcesso.createOntology(fonte);

        //Criação das superclasses
        modeloOntologiaProcesso = criaSuperClasses(modeloOntologiaProcesso, NS);

        //Criação das propriedades de dados
        modeloOntologiaProcesso = preenchePropriedadesDados(modeloOntologiaProcesso, NS);

        //Criação das propriedades de objeto
        modeloOntologiaProcesso = preenchePropriedadesObjetos(modeloOntologiaProcesso, NS);

        //Criação da classe de modelo
        OntClass modeloClas = modeloOntologiaProcesso.getOntClass(NS + "Model");

        OntClass clasModelo = modeloOntologiaProcesso.createClass(NS + (modelo.getNome() != null
                && !modelo.getNome().isEmpty() ? "model-".concat(TextUtils.formataNome(modelo.getNome()))
                        : "model-".concat(modelo.getIdModelo())));
        clasModelo.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                modeloOntologiaProcesso.getObjectProperty(NS + "isComposedBy"),
                modeloOntologiaProcesso.getOntClass(NS + "ModelElements")));

        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), modelo.getIdModelo());
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"), modelo.getNome() != null
                ? modelo.getNome() : "No Name");
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), modelo.getDescricao() != null
                ? modelo.getDescricao() : "No Description");
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"),
                modelo.getDocumentacao() != null ? modelo.getDocumentacao() : "No Documentation");
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-author"), modelo.getAutor() != null
                ? modelo.getAutor() : "No Author");
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i6-creationDate"), modelo.getDataCriacao() != null
                ? modelo.getDataCriacao().toString() : "No Creation Date");
        clasModelo.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i7-modificationDate"), modelo.getDataModificacao() != null
                ? modelo.getDataModificacao().toString() : "No Modification Date");

        modeloClas.addSubClass(clasModelo);

        //Criaçao das classes principais (Processo, Subprocesso, Evento, Gateway, Atividade, Ator)
        //Está ignorando o processo padrão dos modelos: MainProcess
        List<Processo> processoList = modelo.getProcessoList().stream().filter(p -> !p.getNome().toUpperCase().equals("MAIN PROCESS")
                && !p.getNome().toUpperCase().equals("PROCESSO PRINCIPAL")).collect(Collectors.toList());

        if (processoList != null && !processoList.isEmpty()) {
            List<SubProcesso> subProcessEventList = new ArrayList();

            for (Processo processo : processoList) {
                OntClass processoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "Process");

                OntClass processoClas = modeloOntologiaProcesso.createClass(NS + (processo.getNome() != null
                        && !processo.getNome().isEmpty() ? "process-".concat(TextUtils.formataNome(processo.getNome()))
                                : "process-".concat(processo.getIdElemento())));

                processoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                        modeloOntologiaProcesso.getObjectProperty(NS + "isComposedBy"),
                        modeloOntologiaProcesso.getOntClass(NS + "ProcessElements")));

                processoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), processo.getIdElemento());
                processoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"), processo.getNome() != null
                        ? processo.getNome() : "No Name");
                processoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), processo.getDescricao() != null
                        ? processo.getDescricao() : "No Description");
                processoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"),
                        processo.getDocumentacao() != null ? processo.getDocumentacao() : "No Documentation");
                processoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "processType"), processo.getTipoProcesso() != null
                        ? processo.getTipoProcesso() : "No Type");

                processoSuperClas.addSubClass(processoClas);

                if (processo.getSubProcessoList() != null && !processo.getSubProcessoList().isEmpty()) {
                    //Preenchimento dos subprocessos

                    OntClass subProcessoSuperClas;

                    for (SubProcesso subProcesso : processo.getSubProcessoList()) {
                        if (subProcesso.getTipo() == null) {
                            subProcessoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "subProcess-"
                                    + TipoSubProcesso.Simple.name());
                            if (subProcessoSuperClas == null) {
                                subProcessoSuperClas = modeloOntologiaProcesso.createClass(NS + "subProcess-"
                                        + TipoSubProcesso.Simple.name());
                                modeloOntologiaProcesso.getOntClass(NS + "SubProcess").addSubClass(subProcessoSuperClas);
                            }

                        } else if (modeloOntologiaProcesso.getOntClass(NS + "subProcess-"
                                + subProcesso.getTipo().name()) == null) {
                            subProcessoSuperClas = modeloOntologiaProcesso.createClass(NS + "subProcess-"
                                    + subProcesso.getTipo().name());
                            modeloOntologiaProcesso.getOntClass(NS + "SubProcess").addSubClass(subProcessoSuperClas);
                        } else {
                            subProcessoSuperClas = modeloOntologiaProcesso.getOntClass(NS + "subProcess-"
                                    + subProcesso.getTipo().name());
                        }

                        OntClass subProcessoClas = modeloOntologiaProcesso.createClass(NS + (subProcesso.getNome() != null
                                && !subProcesso.getNome().isEmpty() ? "subProcess-".concat(TextUtils.formataNome(subProcesso.getNome()))
                                        : "subProcess-".concat(subProcesso.getIdElemento())));

                        subProcessoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"), subProcesso.getIdElemento());
                        subProcessoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                                TextUtils.geraNomeDetalhadoElemento(subProcesso.getNome(), processo));
                        subProcessoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-description"), subProcesso.getDescricao() != null
                                ? subProcesso.getDescricao() : "No Description");
                        subProcessoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"),
                                subProcesso.getDocumentacao() != null ? subProcesso.getDocumentacao() : "No Documentation");

                        subProcessoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isPartOfProcess"), processoClas));

                        subProcessoSuperClas.addSubClass(subProcessoClas);

//                        modeloOntologiaProcesso = preencheConceitosProcesso(modeloOntologiaProcesso, NS, subProcessoClas,
//                                subProcesso, true);
                        if (subProcesso.getEventoGatilho() != null) {
                            subProcessEventList.add(subProcesso);
                        }

                        //Preenche a relação do executor do subprocesso
                        Ator atorSubProcesso;

                        List<ExecutadoPor> executadoPorList = processo.getExecutadoPorList() == null
                                || processo.getExecutadoPorList().isEmpty() ? null
                                        : processo.getExecutadoPorList().stream().filter(e -> (subProcesso.getIsSubFlow()
                                                && e.getAtividade().getIdElemento()
                                                .equals(subProcesso.getIdAtividadeOrigem()))
                                                || (!subProcesso.getIsSubFlow() && e.getAtividade().getIdElemento()
                                                .equals(subProcesso.getIdElemento())))
                                        .collect(Collectors.toList());

                        if (executadoPorList != null && !executadoPorList.isEmpty()) {
                            boolean existeExecutadoPor = false;

                            //Quando o subprocesso é executado por apenas um Ator
                            if (executadoPorList.size() == 1) {
                                atorSubProcesso = executadoPorList.get(0).getAtor();

                                if (atorSubProcesso != null && atorSubProcesso.getIdElemento() != null && !atorSubProcesso.getIdElemento().isEmpty()
                                        && atorSubProcesso.getNome() != null && !atorSubProcesso.getNome().isEmpty()) {
                                    OntClass atorSubProcessoClas = modeloOntologiaProcesso.getOntClass(NS + (atorSubProcesso.getNome() != null
                                            && !atorSubProcesso.getNome().isEmpty() ? "actor-".concat(
                                                            TextUtils.formataNome(atorSubProcesso.getNome()))
                                                    : "actor-".concat(atorSubProcesso.getIdElemento())));

                                    if (atorSubProcessoClas == null) {
                                        atorSubProcessoClas = modeloOntologiaProcesso.createClass(NS + (atorSubProcesso.getNome() != null
                                                && !atorSubProcesso.getNome().isEmpty() ? "actor-".concat(TextUtils.formataNome(atorSubProcesso.getNome()))
                                                        : "actor-".concat(atorSubProcesso.getIdElemento())));
                                    }

                                    subProcessoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                            modeloOntologiaProcesso.getObjectProperty(NS + "isPerformedBy"), atorSubProcessoClas));

                                    if (subProcesso.getAtorList() == null) {
                                        subProcesso.setAtorList(new ArrayList());
                                    }
                                    if (subProcesso.getExecutadoPorList() == null) {
                                        subProcesso.setExecutadoPorList(new ArrayList());
                                    }

                                    if (!subProcesso.getAtorList().contains(atorSubProcesso)) {
                                        subProcesso.getAtorList().add(atorSubProcesso);
                                    }

                                    if (subProcesso.getExecutadoPorList() != null) {
                                        for (ExecutadoPor exePor : subProcesso.getExecutadoPorList()) {
                                            if ((exePor.getAtividade() != null && executadoPorList.get(0).getAtividade() != null
                                                    && exePor.getAtividade().equals(executadoPorList.get(0).getAtividade()))
                                                    && (exePor.getAtor() != null && executadoPorList.get(0).getAtor() != null
                                                    && exePor.getAtor().equals(executadoPorList.get(0).getAtor()))) {
                                                existeExecutadoPor = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!existeExecutadoPor) {
                                        subProcesso.getExecutadoPorList().add(executadoPorList.get(0));
                                    }

                                }
                            } else {
                                for (ExecutadoPor exePorSub : executadoPorList) {
                                    if (subProcesso.getAtorList() == null) {
                                        subProcesso.setAtorList(new ArrayList());
                                    }
                                    if (subProcesso.getExecutadoPorList() == null) {
                                        subProcesso.setExecutadoPorList(new ArrayList());
                                    }

                                    if (!subProcesso.getAtorList().contains(exePorSub.getAtor())) {
                                        subProcesso.getAtorList().add(exePorSub.getAtor());
                                    }

                                    if (subProcesso.getExecutadoPorList() != null) {
                                        for (ExecutadoPor exePor : subProcesso.getExecutadoPorList()) {
                                            if ((exePor.getAtividade() != null && exePorSub.getAtividade() != null
                                                    && exePor.getAtividade().equals(exePorSub.getAtividade()))
                                                    && (exePor.getAtor() != null && exePorSub.getAtor() != null
                                                    && exePor.getAtor().equals(exePorSub.getAtor()))) {
                                                existeExecutadoPor = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!existeExecutadoPor) {
                                        subProcesso.getExecutadoPorList().add(executadoPorList.get(0));
                                    }
                                }
                                preencheRelacaoAtividadesExecutadas(modeloOntologiaProcesso, NS, true,
                                        subProcesso.getExecutadoPorList());
                            }
                        }

                        //Preenche a relação dos artefatos de entrada do subprocesso
                        List<UtilizaEntrada> utilizaEntradaList = processo.getUtilizaEntradaList() == null
                                || processo.getUtilizaEntradaList().isEmpty() ? null
                                        : processo.getUtilizaEntradaList().stream().filter(e -> e.getAtividade().getIdElemento()
                                                .equals(subProcesso.getIdElemento()))
                                        .collect(Collectors.toList());

                        if (utilizaEntradaList != null && !utilizaEntradaList.isEmpty()) {
                            for (UtilizaEntrada utiEnt : utilizaEntradaList) {
                                OntClass artefatoEntradaClas = modeloOntologiaProcesso.getOntClass(NS + (utiEnt.getArtefato().getNome() != null
                                        && !utiEnt.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(utiEnt.getArtefato().getNome()))
                                                : "artft-".concat(utiEnt.getArtefato().getIdElemento())));

                                if (artefatoEntradaClas == null) {
                                    artefatoEntradaClas = modeloOntologiaProcesso.createClass(NS + (utiEnt.getArtefato().getNome() != null
                                            && !utiEnt.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(utiEnt.getArtefato().getNome()))
                                                    : "artft-".concat(utiEnt.getArtefato().getIdElemento())));
                                } else {
                                    RDFNode idArtefato = artefatoEntradaClas.getPropertyValue(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"));
                                    if (idArtefato != null && idArtefato.toString() != null && !idArtefato.toString().equals(utiEnt.getArtefato().getIdElemento())) {
                                        String sufixoProcesso = processo.getNome() != null && !processo.getNome().isEmpty()
                                                ? " for the Process ".concat(processo.getNome()) : " for the Process ".concat(processo.getIdElemento());

                                        if (utiEnt.getArtefato().getNome() != null && !utiEnt.getArtefato().getNome().isEmpty()) {
                                            utiEnt.getArtefato().setNome(utiEnt.getArtefato().getNome().concat(sufixoProcesso));
                                        }

                                        artefatoEntradaClas = modeloOntologiaProcesso.createClass(NS + (utiEnt.getArtefato().getNome() != null
                                                && !utiEnt.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(utiEnt.getArtefato().getNome()))
                                                        : "artft-".concat(utiEnt.getArtefato().getIdElemento())));

                                        //Atualiza o nome do artefato na lista dentro de processo
                                        processo.getArtefatoList().stream().filter(art -> art.getIdElemento().equals(idArtefato.toString()))
                                                .findFirst().get().setNome(utiEnt.getArtefato().getNome());
                                    }
                                }

                                if (artefatoEntradaClas != null) {
                                    subProcessoClas.addSuperClass(createCardinalityQRestriction(modeloOntologiaProcesso, null,
                                            modeloOntologiaProcesso.getObjectProperty(NS + "usesInput"), 1,
                                            artefatoEntradaClas));
                                }
                            }
                        }

                        //Preenche a relação dos artefatos de saída do subprocesso
                        List<ProduzSaida> produzSaidaList = processo.getProduzSaidaList() == null
                                || processo.getProduzSaidaList().isEmpty() ? null
                                        : processo.getProduzSaidaList().stream().filter(e -> e.getElemento().getIdElemento()
                                                .equals(subProcesso.getIdElemento()))
                                        .collect(Collectors.toList());

                        if (produzSaidaList != null && !produzSaidaList.isEmpty()) {
                            for (ProduzSaida proSai : produzSaidaList) {
                                OntClass artefatoSaidaClas = modeloOntologiaProcesso.getOntClass(NS + (proSai.getArtefato().getNome() != null
                                        && !proSai.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(proSai.getArtefato().getNome()))
                                                : "artft-".concat(proSai.getArtefato().getIdElemento())));

                                if (artefatoSaidaClas == null) {
                                    artefatoSaidaClas = modeloOntologiaProcesso.createClass(NS + (proSai.getArtefato().getNome() != null
                                            && !proSai.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(proSai.getArtefato().getNome()))
                                                    : "artft-".concat(proSai.getArtefato().getIdElemento())));
                                } else {
                                    RDFNode idArtefato = artefatoSaidaClas.getPropertyValue(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"));
                                    if (idArtefato != null && idArtefato.toString() != null && !idArtefato.toString().equals(proSai.getArtefato().getIdElemento())) {
                                        String sufixoProcesso = processo.getNome() != null && !processo.getNome().isEmpty()
                                                ? " for the Process ".concat(processo.getNome()) : " for the Process ".concat(processo.getIdElemento());

                                        if (proSai.getArtefato().getNome() != null && !proSai.getArtefato().getNome().isEmpty()) {
                                            proSai.getArtefato().setNome(proSai.getArtefato().getNome().concat(sufixoProcesso));
                                        }

                                        artefatoSaidaClas = modeloOntologiaProcesso.createClass(NS + (proSai.getArtefato().getNome() != null
                                                && !proSai.getArtefato().getNome().isEmpty() ? "artft-".concat(TextUtils.formataNome(proSai.getArtefato().getNome()))
                                                        : "artft-".concat(proSai.getArtefato().getIdElemento())));

                                        //Atualiza o nome do artefato na lista dentro de processo
                                        processo.getArtefatoList().stream().filter(art -> art.getIdElemento().equals(idArtefato.toString()))
                                                .findFirst().get().setNome(proSai.getArtefato().getNome());
                                    }
                                }

                                if (artefatoSaidaClas != null) {
                                    subProcessoClas.addSuperClass(createCardinalityQRestriction(modeloOntologiaProcesso, null,
                                            modeloOntologiaProcesso.getObjectProperty(NS + "producesOutput"), 1,
                                            artefatoSaidaClas));
                                }
                            }
                        }

                        modeloOntologiaProcesso = preencheConceitosProcesso(modeloOntologiaProcesso, NS, subProcessoClas,
                                subProcesso, true);
                    }
                }
                modeloOntologiaProcesso = preencheConceitosProcesso(modeloOntologiaProcesso, NS, processoClas, processo,
                        false);

                //Associa os subprocessos de eventos com seus respectivos eventos
                if (!subProcessEventList.isEmpty()) {
                    for (SubProcesso subProAux : subProcessEventList) {
                        OntClass subProcessoEventoClas = modeloOntologiaProcesso.getOntClass(NS
                                + (subProAux.getNome() != null
                                && !subProAux.getNome().isEmpty() ? "subProcess-".concat(
                                                TextUtils.formataNome(subProAux.getNome()))
                                        : "subProcess-".concat(subProAux.getIdElemento())));

                        OntClass eventoClas = modeloOntologiaProcesso.getOntClass(NS
                                + (subProAux.getEventoGatilho().getNome() != null
                                && !subProAux.getEventoGatilho().getNome().isEmpty() ? "evt-".concat(
                                                TextUtils.formataNome(subProAux.getEventoGatilho().getNome()))
                                        : "evt-".concat(subProAux.getEventoGatilho().getIdElemento())));

                        if (eventoClas == null) {
                            eventoClas = modeloOntologiaProcesso.createClass(NS + (subProAux.getEventoGatilho().getNome() != null
                                    && !subProAux.getEventoGatilho().getNome().isEmpty() ? "evt-".concat(
                                                    TextUtils.formataNome(subProAux.getEventoGatilho().getNome()))
                                            : "evt-".concat(subProAux.getEventoGatilho().getIdElemento())));
                        } else {
                            RDFNode idEvento = eventoClas.getPropertyValue(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"));
                            if (idEvento != null && idEvento.toString() != null && !idEvento.toString().equals(subProAux.getEventoGatilho().getIdElemento())) {
                                String sufixoProcesso = processo.getNome() != null && !processo.getNome().isEmpty()
                                        ? " for the Process ".concat(processo.getNome()) : " for the Process ".concat(processo.getIdElemento());

                                if (subProAux.getEventoGatilho().getNome() != null && !subProAux.getEventoGatilho().getNome().isEmpty()) {
                                    subProAux.getEventoGatilho().setNome(subProAux.getEventoGatilho().getNome().concat(sufixoProcesso));
                                }

                                eventoClas = modeloOntologiaProcesso.createClass(NS + (subProAux.getEventoGatilho().getNome() != null
                                        && !subProAux.getEventoGatilho().getNome().isEmpty() ? "evt-".concat(
                                                        TextUtils.formataNome(subProAux.getEventoGatilho().getNome()))
                                                : "evt-".concat(subProAux.getEventoGatilho().getIdElemento())));
                            }
                        }

                        subProcessoEventoClas.addSuperClass(modeloOntologiaProcesso.createSomeValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "isActivatedWhenEventIsTriggered"), eventoClas));
                    }
                }
            }

            //Preenchimento da propriedade de objeto isExecutorOfActivity, inversa de isPerformedBy
            modeloOntologiaProcesso = preencheRelacaoExecutoresDeAtividades(modeloOntologiaProcesso, NS, processoList);

            //Preenche a relação dos atores com os processos a que pertencem
            modeloOntologiaProcesso = preencheRelacaoAtoresProcessos(modeloOntologiaProcesso, NS, processoList);
        }

        //Preenchimento dos conceitos TrocaMensagemCom
        if (modelo.getTrocaMensagemComList() != null && !modelo.getTrocaMensagemComList().isEmpty()) {
            List<Elemento> elementoComunicaMaisDeUmList = new ArrayList();

            modelo.getTrocaMensagemComList().stream().forEach((troMenCom) -> {
                final String prefixoElementoOrigem = troMenCom.getElementoOrigem() instanceof Atividade
                        ? "actv-" : troMenCom.getElementoOrigem() instanceof Gateway ? "gatw-"
                                : troMenCom.getElementoOrigem() instanceof Evento ? "evt-"
                                        : troMenCom.getElementoOrigem() instanceof SubProcesso ? "subProcess-"
                                                : "pool-";

                OntClass elementoOrigemClas = modeloOntologiaProcesso.getOntClass(NS
                        + (troMenCom.getElementoOrigem().getNome() != null
                        && !troMenCom.getElementoOrigem().getNome().isEmpty() ? prefixoElementoOrigem.concat(
                                        TextUtils.formataNome(troMenCom.getElementoOrigem().getNome()))
                                : prefixoElementoOrigem.concat(troMenCom.getElementoOrigem().getIdElemento())));

                final String prefixoElementoDestino = troMenCom.getElementoDestino() instanceof Atividade
                        ? "actv-" : troMenCom.getElementoDestino() instanceof Gateway ? "gatw-"
                                : troMenCom.getElementoDestino() instanceof Evento ? "evt-"
                                        : troMenCom.getElementoOrigem() instanceof SubProcesso ? "subProcess-"
                                                : "pool-";

                OntClass elementoDestinoClas = modeloOntologiaProcesso.getOntClass(NS
                        + (troMenCom.getElementoDestino().getNome() != null
                        && !troMenCom.getElementoDestino().getNome().isEmpty() ? prefixoElementoDestino.concat(
                                        TextUtils.formataNome(troMenCom.getElementoDestino().getNome()))
                                : prefixoElementoDestino.concat(troMenCom.getElementoDestino().getIdElemento())));

                long qtdeOriOrigem = modelo.getTrocaMensagemComList().stream().filter(troMen -> troMen.getElementoOrigem() != null
                        && troMen.getElementoOrigem().getIdElemento() != null && !troMen.getElementoOrigem().getIdElemento().isEmpty()
                        && troMen.getElementoOrigem().getIdElemento().equals(troMenCom.getElementoOrigem().getIdElemento()))
                        .count();

                long qtdeOriDestino = modelo.getTrocaMensagemComList().stream().filter(troMen -> troMen.getElementoDestino() != null
                        && troMen.getElementoDestino().getIdElemento() != null && !troMen.getElementoDestino().getIdElemento().isEmpty()
                        && troMen.getElementoDestino().getIdElemento().equals(troMenCom.getElementoOrigem().getIdElemento()))
                        .count();

                long qtdeDestOrigem = modelo.getTrocaMensagemComList().stream().filter(troMen -> troMen.getElementoOrigem() != null
                        && troMen.getElementoOrigem().getIdElemento() != null && !troMen.getElementoOrigem().getIdElemento().isEmpty()
                        && troMen.getElementoOrigem().getIdElemento().equals(troMenCom.getElementoDestino().getIdElemento()))
                        .count();

                long qtdeDestDestino = modelo.getTrocaMensagemComList().stream().filter(troMen -> troMen.getElementoDestino() != null
                        && troMen.getElementoDestino().getIdElemento() != null && !troMen.getElementoDestino().getIdElemento().isEmpty()
                        && troMen.getElementoDestino().getIdElemento().equals(troMenCom.getElementoDestino().getIdElemento()))
                        .count();

                if ((qtdeOriOrigem + qtdeOriDestino > 1L) || (qtdeDestOrigem + qtdeDestDestino > 1L)) {
                    if (qtdeOriOrigem + qtdeOriDestino > 1L) {
                        if (elementoComunicaMaisDeUmList.stream()
                                .filter(ele -> ele.getIdElemento().equals(troMenCom.getElementoOrigem().getIdElemento())).count() == 0) {
                            elementoComunicaMaisDeUmList.add(troMenCom.getElementoOrigem());
                        }
                    } else {
                        elementoOrigemClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "communicatesWith"), elementoDestinoClas));
                    }

                    if (qtdeDestOrigem + qtdeDestDestino > 1) {
                        if (elementoComunicaMaisDeUmList.stream()
                                .filter(ele -> ele.getIdElemento().equals(troMenCom.getElementoDestino().getIdElemento())).count() == 0) {
                            elementoComunicaMaisDeUmList.add(troMenCom.getElementoDestino());
                        }
                    } else {
                        elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "communicatesWith"), elementoOrigemClas));
                    }
                } else if (elementoOrigemClas != null && elementoDestinoClas != null) {
                    elementoOrigemClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            modeloOntologiaProcesso.getObjectProperty(NS + "communicatesWith"), elementoDestinoClas));
                    elementoDestinoClas.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                            modeloOntologiaProcesso.getObjectProperty(NS + "communicatesWith"), elementoOrigemClas));
                }
            });

            if (!elementoComunicaMaisDeUmList.isEmpty()) {
                OntClass clasOrigem, clasDestino;
                RDFList listClasses;
                RDFNode[] elementsOrigem, elementsDestino, elements;
                int i;
                List<TrocaMensagemCom> troMenComList;

                for (Elemento ele : elementoComunicaMaisDeUmList) {
                    elementsOrigem = new RDFNode[0];
                    elementsDestino = new RDFNode[0];

                    final String prefixoElementoOrigem = ele instanceof Atividade
                            ? "actv-" : ele instanceof Gateway ? "gatw-"
                                    : ele instanceof Evento ? "evt-"
                                            : ele instanceof SubProcesso ? "subProcess-" : "pool-";

                    clasOrigem = modeloOntologiaProcesso.getOntClass(NS
                            + (ele.getNome() != null
                            && !ele.getNome().isEmpty() ? prefixoElementoOrigem.concat(
                                            TextUtils.formataNome(ele.getNome()))
                                    : prefixoElementoOrigem.concat(ele.getIdElemento())));

                    //Busca elementos da origem
                    troMenComList = modelo.getTrocaMensagemComList().stream().filter(troMenCom -> troMenCom.getElementoOrigem() != null
                            && troMenCom.getElementoOrigem().equals(ele)).collect(Collectors.toList());

                    if (troMenComList != null && !troMenComList.isEmpty()) {
                        elementsDestino = new RDFNode[troMenComList.size()];
                        i = 0;

                        for (TrocaMensagemCom troMenCom : troMenComList) {
                            if (troMenCom.getElementoDestino() != null) {
                                final String prefixoElementoDestino = troMenCom.getElementoDestino() instanceof Atividade
                                        ? "actv-" : troMenCom.getElementoDestino() instanceof Gateway ? "gatw-"
                                                : troMenCom.getElementoDestino() instanceof Evento ? "evt-"
                                                        : ele instanceof SubProcesso ? "subProcess-" : "pool-";

                                clasDestino = modeloOntologiaProcesso.getOntClass(NS
                                        + (troMenCom.getElementoDestino().getNome() != null
                                        && !troMenCom.getElementoDestino().getNome().isEmpty() ? prefixoElementoDestino.concat(
                                                        TextUtils.formataNome(troMenCom.getElementoDestino().getNome()))
                                                : prefixoElementoDestino.concat(troMenCom.getElementoDestino().getIdElemento())));

                                if (clasDestino != null) {
                                    elementsDestino[i] = clasDestino;
                                    i++;
                                }
                            }
                        }
                    }
                    //Busca elementos destino
                    troMenComList = modelo.getTrocaMensagemComList().stream().filter(troMenCom -> troMenCom.getElementoDestino() != null
                            && troMenCom.getElementoDestino().equals(ele)).collect(Collectors.toList());
                    i = 0;

                    if (troMenComList != null && !troMenComList.isEmpty()) {
                        OntClass clasOrigemAux;

                        elementsOrigem = new RDFNode[troMenComList.size()];

                        for (TrocaMensagemCom troMenCom : troMenComList) {
                            if (troMenCom.getElementoOrigem() != null) {
                                final String prefixoElementoOrigemAux = troMenCom.getElementoOrigem() instanceof Atividade
                                        ? "actv-" : troMenCom.getElementoOrigem() instanceof Gateway ? "gatw-"
                                                : troMenCom.getElementoOrigem() instanceof Evento ? "evt-"
                                                        : ele instanceof SubProcesso ? "subProcess-" : "pool-";

                                clasOrigemAux = modeloOntologiaProcesso.getOntClass(NS
                                        + (troMenCom.getElementoOrigem().getNome() != null
                                        && !troMenCom.getElementoOrigem().getNome().isEmpty() ? prefixoElementoOrigemAux.concat(
                                                        TextUtils.formataNome(troMenCom.getElementoOrigem().getNome()))
                                                : prefixoElementoOrigemAux.concat(troMenCom.getElementoOrigem().getIdElemento())));

                                if (clasOrigemAux != null) {
                                    elementsOrigem[i] = clasOrigemAux;
                                    i++;
                                }
                            }
                        }
                    }

                    elements = new RDFNode[elementsOrigem.length + elementsDestino.length];
                    i = 0;

                    for (RDFNode node : elementsOrigem) {
                        elements[i] = node;
                        i++;
                    }

                    for (RDFNode node : elementsDestino) {
                        elements[i] = node;
                        i++;
                    }

                    if (elements.length > 0 && clasOrigem != null) {
                        listClasses = modeloOntologiaProcesso.createList(elements);

                        clasOrigem.addSuperClass(modeloOntologiaProcesso.createAllValuesFromRestriction(null,
                                modeloOntologiaProcesso.getObjectProperty(NS + "communicatesWith"),
                                modeloOntologiaProcesso.createUnionClass(null, listClasses)));
                    }
                }
            }
        }

        //Cria o arquivo para download e inserção no BD
        File arquivoOWL = FileDirUtils.geraArquivo(nomeOntologia.concat(".owl"));

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(arquivoOWL))) {
            modeloOntologiaProcesso.writeAll(bos, "RDF/XML");
        }

        if (arquivosOntologiasList == null) {
            arquivosOntologiasList = new ArrayList();
        }

        arquivosOntologiasList.add(arquivoOWL);

        modeloOntologiaProcesso.add(model);

        salvaDadosOntologia();

        return true;
    }

    public boolean salvaDadosOntologia() throws Exception {
        boolean salvou = true;
        ModeloDAO modeloDAO = new ModeloDAO();
        OntologiaDAO ontologiaDAO = new OntologiaDAO();

        try {
            for (File arquivoOntologia : arquivosOntologiasList) {
                Ontologia ontologia = new Ontologia();

                ontologia.setIdOntologia(ontologiaDAO.retornaProxIdOntologia());
                ontologia.setNome(nomeOntologia);
                ontologia.setDescricao(descricaoOntologia);
                ontologia.setDataCriacao(new Date());

                try (FileInputStream fileInput = new FileInputStream(arquivoOntologia);) {
                    byte[] arquivo = IOUtils.toByteArray(fileInput);
                    ontologia.setArquivo(arquivo);
                }

                ontologia.setModeloList(new ArrayList());
                ontologia.getModeloList().add(modelo);
                modelo.setOntologia(ontologia);

                modeloDAO.salvar(modelo);
            }
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }

        return salvou;
    }

    public void limpaCampos() {
        rootPackageProcessosList = null;
        modeloOntologiaProcesso = null;
        arquivosOntologiasList = null;
        processosList = null;
        nomeOntologia = null;
        descricaoOntologia = null;
        modelo = null;
    }

    private List<Artefato> preencheArtefatos(PackageType pacote) {
        List<Artefato> artefatoListRetorno = new ArrayList();

        mapGrupos = new HashMap();
        //Itera sobre a lista de artefatos
        if (pacote.getArtifacts() != null && pacote.getArtifacts().getArtifactAndAny() != null
                && !pacote.getArtifacts().getArtifactAndAny().isEmpty()) {

            for (Object o : pacote.getArtifacts().getArtifactAndAny()) {
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
                            artefatoListRetorno.add(artefato);
                            break;
                        case "Group":
                            Group grupo = (Group) objArtefato.getGroup2();

                            artefato = new Grupo();
                            artefato.setIdElemento(grupo.getId());
                            artefato.setNome(TextUtils.removeTagsHtml(grupo.getName()));
                            artefato.setDescricao(TextUtils.removeTagsHtml(grupo.getName()));
                            artefato.setDocumentacao(TextUtils.removeTagsHtml(grupo.getName()));
                            artefatoListRetorno.add(artefato);

                            mapGrupos.put((Grupo) artefato, grupo.getObject());
                            break;
                        case "Data Object":
                            DataObject objetoDados = (DataObject) objArtefato.getDataObject();
                            List<ExtendedAttribute> atributosEstendidosList = null;

                            artefato = new Artefato();
                            artefato.setIdElemento(objetoDados.getId());
                            artefato.setTipo(TipoArtefato.DataObject);
                            artefato.setNome(TextUtils.removeTagsHtml(objetoDados.getName()));
                            artefato.setDescricao(objetoDados.getObject() != null
                                    && objetoDados.getObject().getDocumentation() != null
                                            ? TextUtils.removeTagsHtml(objetoDados.getObject().getDocumentation().getValue()) : null);
                            artefato.setDocumentacao(objetoDados.getObject() != null
                                    && objetoDados.getObject().getDocumentation() != null
                                            ? TextUtils.removeTagsHtml(objetoDados.getObject().getDocumentation().getValue()) : null);

                            if (objetoDados.getAny() != null && !objetoDados.getAny().isEmpty()) {
                                for (Object obj : objetoDados.getAny()) {
                                    if (obj instanceof ExtendedAttributes) {
                                        if (atributosEstendidosList == null) {
                                            atributosEstendidosList = new ArrayList();

                                            atributosEstendidosList.addAll(((ExtendedAttributes) obj)
                                                    .getExtendedAttribute());
                                        }
                                    }
                                }
                            }

                            artefatoListRetorno.add(artefato);
                            break;
                    }
                }
            }
        }

        //Itera sobre a lista de Depositos de Dados
        if (pacote.getDataStores() != null && pacote.getDataStores().getDataStore() != null
                && !pacote.getDataStores().getDataStore().isEmpty()) {
            Artefato artefato;
            List<ExtendedAttribute> atributosEstendidosList;

            for (Object o : pacote.getDataStores().getDataStore()) {
                DataStore depositoDados = (DataStore) o;
                atributosEstendidosList = null;

                artefato = new Artefato();

                artefato.setIdElemento(depositoDados.getId());
                artefato.setTipo(TipoArtefato.DataStore);
                artefato.setNome(TextUtils.removeTagsHtml(depositoDados.getName()));
                artefato.setDescricao(depositoDados.getObject() != null
                        && depositoDados.getObject().getDocumentation() != null
                                ? TextUtils.removeTagsHtml(depositoDados.getObject().getDocumentation().getValue())
                                : TextUtils.removeTagsHtml(depositoDados.getName()));
                artefato.setDocumentacao(depositoDados.getObject() != null
                        && depositoDados.getObject().getDocumentation() != null
                                ? TextUtils.removeTagsHtml(depositoDados.getObject().getDocumentation().getValue())
                                : TextUtils.removeTagsHtml(depositoDados.getName()));

                if (depositoDados.getAny() != null && !depositoDados.getAny().isEmpty()) {
                    for (Object obj : depositoDados.getAny()) {
                        if (obj instanceof ExtendedAttributes) {
                            if (atributosEstendidosList == null) {
                                atributosEstendidosList = new ArrayList();

                                atributosEstendidosList.addAll(((ExtendedAttributes) obj)
                                        .getExtendedAttribute());
                            }
                        }
                    }
                }

                artefato.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(artefato,
                        atributosEstendidosList));

                artefatoListRetorno.add(artefato);
            }
        }

        return artefatoListRetorno;
    }

    private List<Artefato> preencheArtefatosProcesso(ProcessType wfp) {
        List<Artefato> artefatoListRetorno = new ArrayList();
        List<ExtendedAttribute> atributosEstendidosList;

        if (wfp.getContent() != null && !wfp.getContent().isEmpty()) {
            for (Object conjunto : wfp.getContent()) {
                if (conjunto instanceof DataObjects) {
                    DataObjects conjuntoObjetoDados = (DataObjects) conjunto;

                    for (DataObject dataObject : conjuntoObjetoDados.getDataObject()) {
                        atributosEstendidosList = null;

                        Artefato artefato = new Artefato();

                        artefato.setIdElemento(dataObject.getId());
                        artefato.setTipo(TipoArtefato.DataObject);
                        artefato.setNome(TextUtils.removeTagsHtml(dataObject.getName()));
                        artefato.setDescricao(dataObject.getObject().getDocumentation() != null
                                ? TextUtils.removeTagsHtml(dataObject.getObject().getDocumentation().getValue()) : null);
                        artefato.setDocumentacao(dataObject.getObject().getDocumentation() != null
                                ? TextUtils.removeTagsHtml(dataObject.getObject().getDocumentation().getValue()) : null);

                        if (dataObject.getAny() != null && !dataObject.getAny().isEmpty()) {
                            for (Object obj : dataObject.getAny()) {
                                if (obj instanceof ExtendedAttributes) {
                                    if (atributosEstendidosList == null) {
                                        atributosEstendidosList = new ArrayList();

                                        atributosEstendidosList.addAll(((ExtendedAttributes) obj)
                                                .getExtendedAttribute());
                                    }
                                }
                            }
                        }

                        artefato.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(artefato,
                                atributosEstendidosList));

                        if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefato)) {
                            artefatoList.add(artefato);
                        }

                        if (artefatoListRetorno.isEmpty() || !artefatoListRetorno.contains(artefato)) {
                            artefatoListRetorno.add(artefato);
                        }
                    }
                }
            }

            for (Object conjunto : wfp.getContent()) {
                if (conjunto instanceof DataAssociations) {
                    DataAssociations conjuntoDataAssociations = (DataAssociations) conjunto;
                    Artefato artefatoAux;

                    for (DataAssociation datAss : conjuntoDataAssociations.getDataAssociation()) {
                        //Busca nos dados de origem de associação
                        artefatoAux = artefatoList != null && !artefatoList.isEmpty()
                                ? artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getFrom())).count() == 0
                                ? null : artefatoList.stream().filter(a -> a.getIdElemento().equals(datAss.getFrom())).findFirst().get() : null;

                        if (artefatoAux != null) {
                            if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefatoAux)) {
                                artefatoList.add(artefatoAux);
                            }

                            if (artefatoListRetorno.isEmpty() || !artefatoListRetorno.contains(artefatoAux)) {
                                artefatoListRetorno.add(artefatoAux);
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

                            if (artefatoListRetorno.isEmpty() || !artefatoListRetorno.contains(artefatoAux)) {
                                artefatoListRetorno.add(artefatoAux);
                            }
                        }
                    }
                }
                if (conjunto instanceof DataStoreReferences) {
                    DataStoreReferences conjuntoDataStore = (DataStoreReferences) conjunto;
                    Artefato artefatoAux;

                    for (DataStoreReference datSto : conjuntoDataStore.getDataStoreReference()) {
                        atributosEstendidosList = null;

                        artefatoAux = artefatoList != null && !artefatoList.isEmpty()
                                ? artefatoList.stream().filter(a -> a.getIdElemento().equals(datSto.getDataStoreRef())).count() == 0
                                ? null : artefatoList.stream().filter(a -> a.getIdElemento().equals(datSto.getDataStoreRef())).findFirst().get() : null;

                        if (artefatoAux != null) {
                            if (datSto.getAny() != null && !datSto.getAny().isEmpty()) {
                                for (Object obj : datSto.getAny()) {
                                    if (obj instanceof ExtendedAttributes) {
                                        if (atributosEstendidosList == null) {
                                            atributosEstendidosList = new ArrayList();

                                            atributosEstendidosList.addAll(((ExtendedAttributes) obj)
                                                    .getExtendedAttribute());
                                        }
                                    }
                                }
                            }

                            artefatoAux.setAtributoEstendidoList(geracaoElementosBPMNBO.preencheAtributosEstendidos(artefatoAux,
                                    atributosEstendidosList));

                            if (artefatoList != null && !artefatoList.isEmpty() && !artefatoList.contains(artefatoAux)) {
                                artefatoList.add(artefatoAux);
                            }

                            if (artefatoListRetorno.isEmpty() || !artefatoListRetorno.contains(artefatoAux)) {
                                artefatoListRetorno.add(artefatoAux);
                            }
                        }
                    }
                }
            }
        }

        return artefatoListRetorno;
    }

    private List<TrocaMensagemCom> preencheListaTrocaMensagemCom(Modelo modelo, List<Processo> processoList,
            List<Object> messageFlowList) {
        List<TrocaMensagemCom> trocaMensagemComList = new ArrayList();
        Elemento elementoOrigem, elementoDestino;
        TrocaMensagemCom trocaMensagemCom;

        for (Object mesFlow : messageFlowList) {
            trocaMensagemCom = new TrocaMensagemCom();
            trocaMensagemCom.setIdTrocaMensagemCom(((MessageFlow) mesFlow).getId());
            trocaMensagemCom.setModelo(modelo);
            trocaMensagemCom.setNome(((MessageFlow) mesFlow).getName());

            final String idElementoOrigem = ((MessageFlow) mesFlow).getSource();
            final String idElementoDestino = ((MessageFlow) mesFlow).getTarget();

            elementoOrigem = null;
            elementoDestino = null;

            for (Processo processo : processoList) {
                List<Elemento> elementosList;

                if (elementoOrigem == null) {
                    //Verifica se o elemento de origem é uma atividade
                    elementosList = processo.getAtividadeList() == null || processo.getAtividadeList().isEmpty()
                            ? null : processo.getAtividadeList().stream().filter(a -> a.getIdElemento().equals(idElementoOrigem))
                            .collect(Collectors.toList());

                    elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);

                    if (elementoOrigem == null) {
                        //Verifica se o elemento de origem é um subprocesso
                        elementosList = processo.getSubProcessoList() == null || processo.getSubProcessoList().isEmpty()
                                ? null : processo.getSubProcessoList().stream().filter(a -> a.getIdElemento().equals(idElementoOrigem))
                                .collect(Collectors.toList());

                        elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoOrigem == null) {
                        //Verifica se o elemento de origem é um evento
                        elementosList = processo.getEventoList() == null || processo.getEventoList().isEmpty()
                                ? null : processo.getEventoList().stream().filter(a -> a.getIdElemento().equals(idElementoOrigem))
                                .collect(Collectors.toList());

                        elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoOrigem == null) {
                        //Verifica se o elemento de origem é um gateway
                        elementosList = processo.getGatewayList() == null || processo.getGatewayList().isEmpty()
                                ? null : processo.getGatewayList().stream().filter(a -> a.getIdElemento().equals(idElementoOrigem))
                                .collect(Collectors.toList());

                        elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoOrigem == null) {
                        //Verifica se o elemento de origem é uma piscina
                        elementosList = processo.getPiscinaList() == null || processo.getPiscinaList().isEmpty()
                                ? null : processo.getPiscinaList().stream().filter(a -> a.getIdElemento().equals(idElementoOrigem))
                                .collect(Collectors.toList());

                        elementoOrigem = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }
                }

                if (elementoDestino == null) {
                    //Verifica se o elemento de destino é uma atividade
                    elementosList = processo.getAtividadeList() == null || processo.getAtividadeList().isEmpty()
                            ? null : processo.getAtividadeList().stream().filter(a -> a.getIdElemento().equals(idElementoDestino))
                            .collect(Collectors.toList());

                    elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);

                    if (elementoDestino == null) {
                        //Verifica se o elemento de destino é um subprocesso
                        elementosList = processo.getSubProcessoList() == null || processo.getSubProcessoList().isEmpty()
                                ? null : processo.getSubProcessoList().stream().filter(a -> a.getIdElemento().equals(idElementoDestino))
                                .collect(Collectors.toList());

                        elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoDestino == null) {
                        //Verifica se o elemento de destino é um evento
                        elementosList = processo.getEventoList() == null || processo.getEventoList().isEmpty()
                                ? null : processo.getEventoList().stream().filter(a -> a.getIdElemento().equals(idElementoDestino))
                                .collect(Collectors.toList());

                        elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoDestino == null) {
                        //Verifica se o elemento de destino é um gateway
                        elementosList = processo.getGatewayList() == null || processo.getGatewayList().isEmpty()
                                ? null : processo.getGatewayList().stream().filter(a -> a.getIdElemento().equals(idElementoDestino))
                                .collect(Collectors.toList());

                        elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }

                    if (elementoDestino == null) {
                        //Verifica se o elemento de destino é uma piscina
                        elementosList = processo.getPiscinaList() == null || processo.getPiscinaList().isEmpty()
                                ? null : processo.getPiscinaList().stream().filter(a -> a.getIdElemento().equals(idElementoDestino))
                                .collect(Collectors.toList());

                        elementoDestino = elementosList == null || elementosList.isEmpty() ? null : elementosList.get(0);
                    }
                }

                if (elementoOrigem != null && elementoDestino != null) {
                    trocaMensagemCom.setElementoOrigem(elementoOrigem);
                    trocaMensagemCom.setElementoDestino(elementoDestino);
                    trocaMensagemComList.add(trocaMensagemCom);
                    break;
                }
            }
        }

        return trocaMensagemComList;
    }

    private OntModel criaSuperClasses(OntModel modelo, String NS) {
        OntModel modeloRetorno = modelo;

        modeloRetorno.createClass(NS + "Model");
        modeloRetorno.createClass(NS + "ModelElements");
        modeloRetorno.createClass(NS + "Process");
        modeloRetorno.createClass(NS + "ProcessElements");
        modeloRetorno.createClass(NS + "Activity");
        // Subclasses da atividade
//        for (TipoAtividade tipAti : TipoAtividade.values()) {
//            OntClass atividadeTip = modeloRetorno.createClass(NS + tipAti.name());
//            modeloRetorno.getOntClass(NS + "Activity").addSubClass(atividadeTip);
//        }
        modeloRetorno.createClass(NS + "SubProcess");
        modeloRetorno.createClass(NS + "Actor");
        modeloRetorno.createClass(NS + "Event");
        //Subclasses do evento
//        for (TipoEvento tipEve : TipoEvento.values()) {
//            OntClass eventoTip = modeloRetorno.createClass(NS + tipEve.name());
//            modeloRetorno.getOntClass(NS + "Event").addSubClass(eventoTip);
//        }
        modeloRetorno.createClass(NS + "Gateway");
        //Subclasses do gateway
//        for (TipoGateway tipGat : TipoGateway.values()) {
//            OntClass gatewayTip = modeloRetorno.createClass(NS + tipGat.name());
//            modeloRetorno.getOntClass(NS + "Gateway").addSubClass(gatewayTip);
//        }

        //Piscina
        modeloRetorno.createClass(NS + "Pool");

        modeloRetorno.createClass(NS + "ExtendedAttribute");

        //Artefato e suas sub-classes
        modeloRetorno.createClass(NS + "Artifact");
        modeloRetorno.createClass(NS + "Annotation");
        modeloRetorno.createClass(NS + "DataStore");
        modeloRetorno.createClass(NS + "Group");
        modeloRetorno.createClass(NS + "DataObject");
        modeloRetorno.getOntClass(NS + "Artifact").addSubClass(modeloRetorno.getOntClass(NS + "Annotation"));
        modeloRetorno.getOntClass(NS + "Artifact").addSubClass(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "Artifact").addSubClass(modeloRetorno.getOntClass(NS + "Group"));
        modeloRetorno.getOntClass(NS + "Artifact").addSubClass(modeloRetorno.getOntClass(NS + "DataObject"));

        //Fluxos paralelos e inclusivos
        modeloRetorno.createClass(NS + "Flow");
        modeloRetorno.createClass(NS + "ParallelFlow");
        modeloRetorno.createClass(NS + "InclusiveFlow");
        modeloRetorno.getOntClass(NS + "Flow").addSubClass(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "Flow").addSubClass(modeloRetorno.getOntClass(NS + "InclusiveFlow"));

        //Disjunções
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "SubProcess"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "Event"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "Gateway"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "Activity").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "Event"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "Gateway"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "SubProcess").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "Gateway"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "Event").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "Gateway").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "ParallelFlow"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "ExtendedAttribute").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "ParallelFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "InclusiveFlow"));
        modeloRetorno.getOntClass(NS + "ParallelFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "ParallelFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "ParallelFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "ParallelFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "InclusiveFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "DataObject"));
        modeloRetorno.getOntClass(NS + "InclusiveFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "InclusiveFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "InclusiveFlow").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "DataObject").addDisjointWith(modeloRetorno.getOntClass(NS + "DataStore"));
        modeloRetorno.getOntClass(NS + "DataObject").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "DataObject").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "DataStore").addDisjointWith(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "DataStore").addDisjointWith(modeloRetorno.getOntClass(NS + "Pool"));

        modeloRetorno.getOntClass(NS + "ModelElements").addSubClass(modeloRetorno.getOntClass(NS + "Process"));
        modeloRetorno.getOntClass(NS + "ModelElements").addSubClass(modeloRetorno.getOntClass(NS + "ProcessElements"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Activity"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "SubProcess"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Actor"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Event"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Gateway"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Artifact"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Pool"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        modeloRetorno.getOntClass(NS + "ProcessElements").addSubClass(modeloRetorno.getOntClass(NS + "Flow"));
        
        modeloRetorno.getOntClass(NS + "ModelElements").addDisjointWith(modeloRetorno.getOntClass(NS + "Model"));

        modeloRetorno.createClass(NS + "AuxiliaryElements");
        
        modeloRetorno.getOntClass(NS + "AuxiliaryElements").addDisjointWith(modeloRetorno.getOntClass(NS + "Model"));
        modeloRetorno.getOntClass(NS + "AuxiliaryElements").addDisjointWith(modeloRetorno.getOntClass(NS + "ModelElements"));

        modeloRetorno.createClass(NS + "EventTriggers");
        modeloRetorno.createClass(NS + "GatewaysDirections");
        modeloRetorno.createClass(NS + "GatewaysOutputs");
        modeloRetorno.createClass(NS + "OutputIsYes");
        modeloRetorno.createClass(NS + "OutputIsNo");
        modeloRetorno.createClass(NS + "OutputIsUndefined");

        modeloRetorno.getOntClass(NS + "AuxiliaryElements").addSubClass(modeloRetorno.getOntClass(NS + "EventTriggers"));
        modeloRetorno.getOntClass(NS + "AuxiliaryElements").addSubClass(modeloRetorno.getOntClass(NS + "GatewaysDirections"));
        modeloRetorno.getOntClass(NS + "AuxiliaryElements").addSubClass(modeloRetorno.getOntClass(NS + "GatewaysOutputs"));

        modeloRetorno.getOntClass(NS + "GatewaysOutputs").addSubClass(modeloRetorno.getOntClass(NS + "OutputIsYes"));
        modeloRetorno.getOntClass(NS + "GatewaysOutputs").addSubClass(modeloRetorno.getOntClass(NS + "OutputIsNo"));
        modeloRetorno.getOntClass(NS + "GatewaysOutputs").addSubClass(modeloRetorno.getOntClass(NS + "OutputIsUndefined"));

        return modeloRetorno;
    }

    private OntModel preenchePropriedadesDados(OntModel modelo, String NS) {
        OntModel modeloRetorno = modelo;

        DatatypeProperty id = modeloRetorno.createDatatypeProperty(NS + "i1-id");
        id.addDomain(XSD.xstring);

        DatatypeProperty nome = modeloRetorno.createDatatypeProperty(NS + "i2-name");
        nome.addDomain(XSD.xstring);

        DatatypeProperty descricao = modeloRetorno.createDatatypeProperty(NS + "i3-description");
        descricao.addDomain(XSD.xstring);

        DatatypeProperty documentacao = modeloRetorno.createDatatypeProperty(NS + "i4-documentation");
        documentacao.addDomain(XSD.xstring);

        DatatypeProperty autor = modeloRetorno.createDatatypeProperty(NS + "i5-author");
        autor.addDomain(XSD.xstring);

        DatatypeProperty valor = modeloRetorno.createDatatypeProperty(NS + "i3-value");
        valor.addDomain(XSD.xstring);

        DatatypeProperty dataCriacao = modeloRetorno.createDatatypeProperty(NS + "i6-creationDate");
        dataCriacao.addDomain(XSD.dateTime);

        DatatypeProperty dataModificacao = modeloRetorno.createDatatypeProperty(NS + "i7-modificationDate");
        dataModificacao.addDomain(XSD.dateTime);

        DatatypeProperty tipoProcesso = modeloRetorno.createDatatypeProperty(NS + "processType");
        tipoProcesso.addDomain(XSD.xstring);

//        OntClass tipoGateway = modeloRetorno.createClass(NS + "GatewayType");
//        //EnumeratedClass tipoGatewayEnum = modeloRetorno.createEnumeratedClass(NS + "TipoGatewayEnum", null);
//        for (TipoGateway tipGat : TipoGateway.values()) {
//            OntClass tipGatClas = modeloRetorno.createClass(NS + tipGat.name());
//            //tipoGatewayEnum.addOneOf(tipoGateway.createIndividual(NS + tipGat.name()));
//            modeloRetorno.getOntClass(NS + "GatewayType").addSubClass(tipGatClas);
//        }
//
//        DatatypeProperty tipoGatewayProp = modeloRetorno.createDatatypeProperty(NS + "gatewayType");
////        tipoGatewayProp.addRange(tipoGatewayEnum);
//        tipoGatewayProp.addRange(tipoGateway);
//
//        //modeloRetorno.getOntClass(NS + "GatewayType").addSubClass(tipoGatewayEnum);
//        modeloRetorno.getOntClass(NS + "Types").addSubClass(tipoGateway);
        OntClass direcaoGateway = modeloRetorno.createClass(NS + "GatewayDirection");
        //EnumeratedClass direcaoGatewayEnum = modeloRetorno.createEnumeratedClass(NS + "DirecaoGatewayEnum", null);
        for (DirecaoGateway dirGat : DirecaoGateway.values()) {
            //direcaoGatewayEnum.addOneOf(direcaoGateway.createIndividual(NS + dirGat.name()));
            OntClass dirGatClas = modeloRetorno.createClass(NS + dirGat.name());
            modeloRetorno.getOntClass(NS + "GatewayDirection").addSubClass(dirGatClas);
        }

        DatatypeProperty direcaoGatewayProp = modeloRetorno.createDatatypeProperty(NS + "gatewayDirection");
        //direcaoGatewayProp.addRange(direcaoGatewayEnum);
        direcaoGatewayProp.addRange(direcaoGateway);

        //modeloRetorno.getOntClass(NS + "GatewayDirection").addSubClass(direcaoGatewayEnum);
        modeloRetorno.getOntClass(NS + "GatewaysDirections").addSubClass(direcaoGateway);

//        OntClass tipoEvento = modeloRetorno.createClass(NS + "EventType");
//        //EnumeratedClass tipoEventoEnum = modeloRetorno.createEnumeratedClass(NS + "TipoEventoEnum", null);
//        for (TipoEvento tipEve : TipoEvento.values()) {
//            OntClass tipEventoClas = modeloRetorno.createClass(NS + tipEve.name());
//            //tipoEventoEnum.addOneOf(tipoEvento.createIndividual(NS + tipEve.name()));
//            modeloRetorno.getOntClass(NS + "EventType").addSubClass(tipEventoClas);
//        }
//
//        DatatypeProperty tipoEventoProp = modeloRetorno.createDatatypeProperty(NS + "eventType");
//        //tipoEventoProp.addRange(tipoEventoEnum);
//        tipoEventoProp.addRange(tipoEvento);
//
//        //modeloRetorno.getOntClass(NS + "EventType").addSubClass(tipoEventoEnum);
//        modeloRetorno.getOntClass(NS + "Types").addSubClass(tipoEvento);
        OntClass gatilhoEvento = modeloRetorno.createClass(NS + "EventTrigger");
        //EnumeratedClass gatilhoEventoEnum = modeloRetorno.createEnumeratedClass(NS + "GatilhoEventoEnum", null);
        for (GatilhoEvento gatEve : GatilhoEvento.values()) {
            OntClass gatEveClas = modeloRetorno.createClass(NS + gatEve.name());
            //gatilhoEventoEnum.addOneOf(gatilhoEvento.createIndividual(NS + gatEve.name()));
            modeloRetorno.getOntClass(NS + "EventTrigger").addSubClass(gatEveClas);
        }

        DatatypeProperty gatilhoEventoProp = modeloRetorno.createDatatypeProperty(NS + "eventTrigger");
        //gatilhoEventoProp.addRange(gatilhoEventoEnum);
        gatilhoEventoProp.addRange(gatilhoEvento);

        //modeloRetorno.getOntClass(NS + "EventTrigger").addSubClass(gatilhoEventoEnum);
        modeloRetorno.getOntClass(NS + "EventTriggers").addSubClass(gatilhoEvento);

//        OntClass tipoAtividade = modeloRetorno.createClass(NS + "ActivityType");
//        //EnumeratedClass tipoAtividadeEnum = modeloRetorno.createEnumeratedClass(NS + "TipoAtividadeEnum", null);
//        for (TipoAtividade tipAti : TipoAtividade.values()) {
//            OntClass tipAtiClas = modeloRetorno.createClass(NS + tipAti.name());
//            //tipoAtividadeEnum.addOneOf(tipoAtividade.createIndividual(NS + tipAti.name()));
//            modeloRetorno.getOntClass(NS + "ActivityType").addSubClass(tipAtiClas);
//        }
//
//        DatatypeProperty tipoAtividadeProp = modeloRetorno.createDatatypeProperty(NS + "activityType");
//        //tipoAtividadeProp.addRange(tipoAtividadeEnum);
//        tipoAtividadeProp.addRange(tipoAtividade);
//
//        //modeloRetorno.getOntClass(NS + "ActivityType").addSubClass(tipoAtividadeEnum);
//        modeloRetorno.getOntClass(NS + "Types").addSubClass(tipoAtividade);
//        OntClass tipoSubProcesso = modeloRetorno.createClass(NS + "SubProcessType");
//        //EnumeratedClass tipoSubProcessoEnum = modeloRetorno.createEnumeratedClass(NS + "TipoSubProcessoEnum", null);
//        for (TipoSubProcesso tipSub : TipoSubProcesso.values()) {
//            OntClass tipSubClas = modeloRetorno.createClass(NS + tipSub.name());
//            //tipoSubProcessoEnum.addOneOf(tipoSubProcesso.createIndividual(NS + tipSub.name()));
//            modeloRetorno.getOntClass(NS + "SubProcessType").addSubClass(tipSubClas);
//        }
//
//        DatatypeProperty tipoSubProcessoProp = modeloRetorno.createDatatypeProperty(NS + "subProcessType");
//        //tipoSubProcessoProp.addRange(tipoSubProcessoEnum);
//        tipoSubProcessoProp.addRange(tipoSubProcesso);
//
//        //modeloRetorno.getOntClass(NS + "SubProcessType").addSubClass(tipoSubProcessoEnum);
//        modeloRetorno.getOntClass(NS + "Types").addSubClass(tipoSubProcesso);
//        OntClass tipoArtefato = modeloRetorno.createClass(NS + "ArtifactType");
//        //EnumeratedClass tipoArtefatoEnum = modeloRetorno.createEnumeratedClass(NS + "TipoArtefatoEnum", null);
//        for (TipoArtefato tipArt : TipoArtefato.values()) {
//            OntClass tipArtClas = modeloRetorno.createClass(NS + tipArt.name());
//            //tipoArtefatoEnum.addOneOf(tipoArtefato.createIndividual(NS + tipArt.name()));
//            modeloRetorno.getOntClass(NS + "ArtifactType").addSubClass(tipArtClas);
//        }
//
//        DatatypeProperty tipoArtefatoProp = modeloRetorno.createDatatypeProperty(NS + "artifactType");
//        //tipoArtefatoProp.addRange(tipoArtefatoEnum);
//        tipoArtefatoProp.addRange(tipoArtefato);
//
//        //modeloRetorno.getOntClass(NS + "ArtifactType").addSubClass(tipoArtefatoEnum);
//        modeloRetorno.getOntClass(NS + "Types").addSubClass(tipoArtefato);
        //Propriedades para Regras de Negocio, Requisitos Funcionais e Nao-Funcionais
        DatatypeProperty regraDeNegocio = modeloRetorno.createDatatypeProperty(NS + "i8-businessRule");
        regraDeNegocio.addDomain(XSD.xstring);

        DatatypeProperty eRegraDeNegocio = modeloRetorno.createDatatypeProperty(NS + "i5-isBusinessRule");
        eRegraDeNegocio.addDomain(XSD.xboolean);
        eRegraDeNegocio.addRange(XSD.xboolean);

        DatatypeProperty eRequisitoFuncional = modeloRetorno.createDatatypeProperty(NS + "i6-isFunctionalRequirement");
        eRequisitoFuncional.addDomain(XSD.xboolean);
        eRequisitoFuncional.addRange(XSD.xboolean);

        DatatypeProperty eRequisitoNaoFuncional = modeloRetorno.createDatatypeProperty(NS + "i7-isNonFunctionalRequirement");
        eRequisitoNaoFuncional.addDomain(XSD.xboolean);
        eRequisitoNaoFuncional.addRange(XSD.xboolean);

        DatatypeProperty eDecisaoSim = modeloRetorno.createDatatypeProperty(NS + "isDecisionYes");
        eDecisaoSim.addDomain(XSD.xboolean);

        DatatypeProperty eDecisaoNao = modeloRetorno.createDatatypeProperty(NS + "isDecisionNo");
        eDecisaoNao.addDomain(XSD.xboolean);

        return modeloRetorno;
    }

    private OntModel preenchePropriedadesObjetos(OntModel modelo, String NS) {
        OntModel modeloRetorno = modelo;

        ObjectProperty eCompostoPor = modeloRetorno.createObjectProperty(NS + "isComposedBy");
        eCompostoPor.addRange(modeloRetorno.getOntClass(NS + "ProcessElements"));
        eCompostoPor.addRange(modeloRetorno.getOntClass(NS + "ModelElements"));

        ObjectProperty executadoPor = modeloRetorno.createObjectProperty(NS + "isPerformedBy");
        executadoPor.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        executadoPor.addDomain(modeloRetorno.getOntClass(NS + "SubProcess"));
        executadoPor.addRange(modeloRetorno.getOntClass(NS + "Actor"));

        ObjectProperty executorDaAtividade = modeloRetorno.createInverseFunctionalProperty(NS + "isExecutorOfActivity");
        executorDaAtividade.addDomain(modeloRetorno.getOntClass(NS + "Actor"));
        executorDaAtividade.addRange(modeloRetorno.getOntClass(NS + "Activity"));

        //Propriedades inversas
        executadoPor.addInverseOf(executorDaAtividade);
        executorDaAtividade.addInverseOf(executadoPor);

        ObjectProperty sucedidoPor = modeloRetorno.createObjectProperty(NS + "isSucceededBy");
        sucedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        sucedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Event"));
        sucedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        sucedidoPor.addDomain(modeloRetorno.getOntClass(NS + "SubProcess"));
        sucedidoPor.addRange(modeloRetorno.getOntClass(NS + "Activity"));
        sucedidoPor.addRange(modeloRetorno.getOntClass(NS + "Event"));
        sucedidoPor.addRange(modeloRetorno.getOntClass(NS + "Gateway"));
        sucedidoPor.addRange(modeloRetorno.getOntClass(NS + "SubProcess"));

        ObjectProperty precedidoPor = modeloRetorno.createInverseFunctionalProperty(NS + "isPrecededBy");
        precedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        precedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Event"));
        precedidoPor.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        precedidoPor.addDomain(modeloRetorno.getOntClass(NS + "SubProcess"));
        precedidoPor.addRange(modeloRetorno.getOntClass(NS + "Activity"));
        precedidoPor.addRange(modeloRetorno.getOntClass(NS + "Event"));
        precedidoPor.addRange(modeloRetorno.getOntClass(NS + "Gateway"));
        precedidoPor.addRange(modeloRetorno.getOntClass(NS + "SubProcess"));

        //Propriedades inversas
        precedidoPor.addInverseOf(sucedidoPor);
        sucedidoPor.addInverseOf(precedidoPor);

        AnnotationProperty tipoCondicaoSucedidoPor = modeloRetorno.createAnnotationProperty(NS + "conditionTypeSucceededBy");
        tipoCondicaoSucedidoPor.addRange(modeloRetorno.getObjectProperty(NS + "isSucceededBy"));
        tipoCondicaoSucedidoPor.addDomain(XSD.xstring);

        AnnotationProperty descCondicaoSucedidoPor = modeloRetorno.createAnnotationProperty(NS + "descConditionSucceededBy");
        descCondicaoSucedidoPor.addRange(modeloRetorno.getObjectProperty(NS + "isSucceededBy"));
        descCondicaoSucedidoPor.addDomain(XSD.xstring);

        modeloRetorno.createObjectProperty(NS + "isPartOfProcess");
        modeloRetorno.createObjectProperty(NS + "isPartOfSubProcess");
        //Propriedade de objeto para os fluxos
        modeloRetorno.createObjectProperty(NS + "isPartOfFlow");

        ObjectProperty utilizaEntrada = modeloRetorno.createObjectProperty(NS + "usesInput");
        utilizaEntrada.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        utilizaEntrada.addDomain(modeloRetorno.getOntClass(NS + "SubProcess"));
        utilizaEntrada.addRange(modeloRetorno.getOntClass(NS + "Artifact"));
//        utilizaEntrada.addRange(modeloRetorno.getOntClass(NS + "DataStore"));
//        utilizaEntrada.addRange(modeloRetorno.getOntClass(NS + "DataObject"));

        ObjectProperty produzSaida = modeloRetorno.createObjectProperty(NS + "producesOutput");
        produzSaida.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        produzSaida.addDomain(modeloRetorno.getOntClass(NS + "Event"));
        produzSaida.addRange(modeloRetorno.getOntClass(NS + "Artifact"));
//        produzSaida.addRange(modeloRetorno.getOntClass(NS + "DataStore"));
//        produzSaida.addRange(modeloRetorno.getOntClass(NS + "DataObject"));

        ObjectProperty trocaMensagemCom = modeloRetorno.createSymmetricProperty(NS + "communicatesWith", true);
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "Event"));
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "SubProcess"));
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "Process"));
        trocaMensagemCom.addDomain(modeloRetorno.getOntClass(NS + "Pool"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "Activity"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "Event"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "Gateway"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "SubProcess"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "Process"));
        trocaMensagemCom.addRange(modeloRetorno.getOntClass(NS + "Pool"));

        ObjectProperty possuiAtributoEstendido = modeloRetorno.createObjectProperty(NS + "hasExtendedAttribute");
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Event"));
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "ProcessElements"));
        possuiAtributoEstendido.addRange(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));

        ObjectProperty ehAtributoEstendidoDe = modeloRetorno.createObjectProperty(NS + "isExtendedAttributeOf");
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Event"));
//        possuiAtributoEstendido.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        ehAtributoEstendidoDe.addDomain(modeloRetorno.getOntClass(NS + "ExtendedAttribute"));
        ehAtributoEstendidoDe.addRange(modeloRetorno.getOntClass(NS + "ProcessElements"));

        possuiAtributoEstendido.addInverseOf(possuiAtributoEstendido);
        ehAtributoEstendidoDe.addInverseOf(possuiAtributoEstendido);

        ObjectProperty anotadoPor = modeloRetorno.createObjectProperty(NS + "isAnnotatedBy");
//        anotadoPor.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
//        anotadoPor.addDomain(modeloRetorno.getOntClass(NS + "Event"));
//        anotadoPor.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
//        anotadoPor.addDomain(modeloRetorno.getIndividual(NS + "DataObject"));
//        anotadoPor.addDomain(modeloRetorno.getIndividual(NS + "DataObject"));
        anotadoPor.addRange(modeloRetorno.getOntClass(NS + "Annotation"));

        ObjectProperty parteDoGrupo = modeloRetorno.createObjectProperty(NS + "isPartOfGroup");
//        parteDoGrupo.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
//        parteDoGrupo.addDomain(modeloRetorno.getOntClass(NS + "Event"));
//        parteDoGrupo.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
//        parteDoGrupo.addDomain(modeloRetorno.getIndividual(NS + "DataObject"));
//        parteDoGrupo.addDomain(modeloRetorno.getIndividual(NS + "DataObject"));
        parteDoGrupo.addRange(modeloRetorno.getOntClass(NS + "Group"));

        ObjectProperty eExecutadoQuandoEventoAtivado = modeloRetorno.createObjectProperty(NS + "isActivatedWhenEventIsTriggered");
        eExecutadoQuandoEventoAtivado.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        eExecutadoQuandoEventoAtivado.addDomain(modeloRetorno.getOntClass(NS + "Gateway"));
        eExecutadoQuandoEventoAtivado.addRange(modeloRetorno.getOntClass(NS + "Event"));

        //Propriedades para relacionar os fluxos com as atividades seguintes a eles
        ObjectProperty eExecutadoAposExecucaoParalela = modeloRetorno.createObjectProperty(NS + "isExecutedAfterParallelExecutionOf");
        eExecutadoAposExecucaoParalela.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        eExecutadoAposExecucaoParalela.addDomain(modeloRetorno.getOntClass(NS + "ParallelFlow"));

        ObjectProperty eExecutadoAposExecucaoInclusiva = modeloRetorno.createObjectProperty(NS + "isExecutedAfterInclusiveExecutionOf");
        eExecutadoAposExecucaoInclusiva.addDomain(modeloRetorno.getOntClass(NS + "Activity"));
        eExecutadoAposExecucaoInclusiva.addDomain(modeloRetorno.getOntClass(NS + "InclusiveFlow"));

        return modeloRetorno;
    }

    private OntClass geraConceitoAtributoEstendido(String NS, AtributoEstendido atrEst, Processo processo,
            Elemento elementoPai) {
        String nomeAtributo = TextUtils.geraNomeAtributoEstendido(atrEst, elementoPai);

        OntClass atributoEstendidoClas = modeloOntologiaProcesso.getOntClass(NS
                + nomeAtributo);

        if (atributoEstendidoClas == null) {
            atributoEstendidoClas = modeloOntologiaProcesso.createClass(NS
                    + nomeAtributo);
        }

        String nomeDetalhadoAtributo = TextUtils.geraNomeDetalhadoAtributoEstendido(atrEst.getNome(),
                elementoPai, processo);

        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i1-id"),
                atrEst.getIdElemento());
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i2-name"),
                nomeDetalhadoAtributo);
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i3-value"), atrEst.getValor() != null
                ? atrEst.getValor() : "No Value", atrEst.getTipoDado().getValor());
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i4-documentation"),
                nomeDetalhadoAtributo);
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i5-isBusinessRule"),
                atrEst.getIsRegraNegocio() ? "true" : "false", XSDDatatype.XSDboolean);
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i6-isFunctionalRequirement"),
                atrEst.getIsRequisitoFuncional() ? "true" : "false", XSDDatatype.XSDboolean);
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i7-isNonFunctionalRequirement"),
                atrEst.getIsRequisitoNaoFuncional() ? "true" : "false", XSDDatatype.XSDboolean);
        atributoEstendidoClas.addProperty(modeloOntologiaProcesso.getDatatypeProperty(NS + "i8-businessRule"),
                atrEst.getIsRegraNegocio() && atrEst.getValor() != null ? atrEst.getValor()
                        : "No Description for the Business Rule");

        return atributoEstendidoClas;
    }

    public List<File> getArquivosOntologiasList() {
        return arquivosOntologiasList;
    }

    public void setArquivosOntologiasList(List<File> arquivosOntologiasList) {
        this.arquivosOntologiasList = arquivosOntologiasList;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public List<Processo> getProcessosList() {
        return processosList;
    }

    public String getNomeOntologia() {
        return nomeOntologia;
    }

    public void setNomeOntologia(String nomeOntologia) {
        this.nomeOntologia = nomeOntologia;
    }

    public String getDescricaoOntologia() {
        return descricaoOntologia;
    }

    public void setDescricaoOntologia(String descricaoOntologia) {
        this.descricaoOntologia = descricaoOntologia;
    }
}
