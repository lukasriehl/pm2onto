package com.lukas.pm2onto.controller;

import com.lukas.pm2onto.bo.ConsultaOntologiaBO;
import com.lukas.pm2onto.consulta.LazyOntologiaDataModel;
import com.lukas.pm2onto.dao.OntologiaDAO;
import com.lukas.pm2onto.dto.ConsultaDTO;
import com.lukas.pm2onto.model.Ontologia;
import com.lukas.pm2onto.tiposconsultas.AtributoElemento;
import com.lukas.pm2onto.tiposconsultas.SubTipoElemento;
import com.lukas.pm2onto.tiposconsultas.TipoConsulta;
import com.lukas.pm2onto.tiposconsultas.TipoElemento;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author lukas
 */
@Named
@ManagedBean
@ViewScoped
public class ConsultaOntologiaController {

    private ConsultaOntologiaBO consultaOntologiaBO;
    private LazyDataModel lazyOntologiaModel;
    private Ontologia ontologiaSelected;
    private List<TipoConsulta> tipoConsultaList;
    private List<TipoElemento> tipoElementoList;
    private List<TipoElemento> tipoElementoDestList;
    private List<SubTipoElemento> subTipoElementoList;
    private List<AtributoElemento> atributoElementoList;
    private List<TipoConsulta> tipoConsultaIntermediariaList;
    private TipoConsulta tipoConsultaSelected; //0 - Básica, 1 - Intermediário, 2 - Avançada
    private TipoElemento tipoElementoSelected;//0 - Atividade, 1 - Subprocesso, 2 - Evento
    //3 - Gateway, 4 - Objeto de Dados, 5 - Depósito de Dados, 6 - Ator
    private SubTipoElemento subTipoElementoSelected;
    private AtributoElemento atributoElementoSelected;//0 - Id, 1 - Nome, 2 - Descrição, 3 - Documentação
    private TipoConsulta tipoConsultaIntermediariaSelected;
    private TipoElemento elementoOrigemSelected;
    private TipoElemento elementoDestinoSelected;
    private String filtroPesquisa;
    private String filtroAtorAtividade;
    private List<ConsultaDTO> consultaDTOList;
    private boolean pesquisaHabilitada;
    private boolean exibeResultados;
    private String msgHelpTipoConsulta = "As consultas são divididas em básicas e avançadas:<br/>Básicas - fornecem "
            + "informações sobre os elementos BPMN, que foram mapeados como classes para a ontologia, independentemente das "
            + "relações definidas para essas classes.<br/>Essas consultas podem usar as seguintes propriedades de dados "
            + "definidas para as classes como filtros: identificador, nome, descrição e documentação. Também é possível "
            + "filtrar as classes SubProcess, Activity, Event, Gateway e Artifact pelo atributo 'type'.<br/>"
            + "Avançadas - fornecem informações sobre as relações e interdependências entre as classes geradas para os elementos "
            + "BPMN. As onze consultas avançadas são construidas a partir das propriedades de objeto definidas para as classes da "
            + "ontologia.";
    private String msgTipoConsultaAvancada = "As consultas avançadas são realizadas com base nas propriedades de objeto definidas "
            + "para as classes da ontologia.";
    private String msgTipoElemento = "Tipo de elemento referente à classe ou à superclasse (para os conceitos que apresentam subdivisões"
            + " da ontologia que será retornada pela consulta";
    private String msgSubTipoElemento = "Subtipo de elemento referente à classe da ontologia que será retornada pela "
            + "consulta";
    private String msgAtributo = "A consulta da classe da ontologia será realizada através da filtragem pelo atributo escolhido. Pode ser realizada a "
            + "filtragem por um atributo ou por todos";
    private String msgFiltroPesquisa = "O filtro é utilizado para buscar os elementos referentes às classes da ontologia de acordo "
            + "com um atributo escolhido para a pesquisa.";
    private String msgTipoElementoOrigem = "Tipo de elemento de origem referente à classe da ontologia que será retornada pela consulta";
    private String msgTipoElementoDestino = "Tipo de elemento de destino referente à classe da ontologia que será retornada pela consulta";

    public ConsultaOntologiaController() {
        try {
            consultaOntologiaBO = new ConsultaOntologiaBO();
            //Define os tipos de consulta
            tipoConsultaList = consultaOntologiaBO.retornaTiposConsultas();
            //Define os tipos de elementos para a consulta
            tipoElementoList = consultaOntologiaBO.retornaTiposElementos(true, true);
            //Define os atributos de elementos para a consulta
            atributoElementoList = consultaOntologiaBO.retornaAtributosElementos();
            //Define os tipos de consultas intermediarias
            tipoConsultaIntermediariaList = consultaOntologiaBO.retornaTiposConsultasIntermediarias();
            pesquisaHabilitada = true;
            exibeResultados = false;
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao carregar as ontologias!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    @PostConstruct
    public void init() {
        //Carrega as ontologias para consulta
        carregaOntologias();
    }

    public void carregaOntologias() {
        try {
            lazyOntologiaModel = new LazyOntologiaDataModel(consultaOntologiaBO.retornaOntologias());
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao carregar as ontologias!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void limpaOpcoesPesquisa() {
        tipoElementoSelected = null;
        subTipoElementoSelected = null;
        atributoElementoSelected = null;
        elementoOrigemSelected = null;
        elementoDestinoSelected = null;
        filtroAtorAtividade = null;
        filtroPesquisa = null;
    }

    public void removeOntologia() {
        if (ontologiaSelected != null) {
            OntologiaDAO ontologiaDAO = new OntologiaDAO();
            ontologiaDAO.remover(ontologiaSelected);
            FacesMessage msg = new FacesMessage("Sucesso", "A ontologia foi removida!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        //Recarrega o grid;
        carregaOntologias();
    }

    public void efetuaPesquisa() {
        try {
            if (ontologiaSelected != null) {
                //pesquisaHabilitada = false;
                consultaOntologiaBO.setOntologiaSelected(ontologiaSelected);

                if (tipoConsultaSelected != null && tipoConsultaSelected.getId() != null) {
                    //Consulta básica
                    if (tipoConsultaSelected.getId() == 0) {
                        consultaOntologiaBO.efetuaPesquisaBasica(atributoElementoSelected, tipoElementoSelected,
                                subTipoElementoSelected, filtroPesquisa);
                        consultaDTOList = consultaOntologiaBO.getConsultaDTOList();
                    } else if (tipoConsultaSelected.getId() == 1 && (tipoConsultaIntermediariaSelected != null
                            && tipoConsultaIntermediariaSelected.getId() != null)) {
                        consultaOntologiaBO.efetuaPesquisaAvancada(tipoConsultaIntermediariaSelected, filtroAtorAtividade,
                                elementoOrigemSelected, elementoDestinoSelected);
                        consultaDTOList = consultaOntologiaBO.getConsultaDTOList();
                    }
                    exibeResultados = true;
                }
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao realizar a consulta sobre a ontologia!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void limpaPesquisa() {
        consultaDTOList = new ArrayList();
        tipoConsultaSelected = null;
        tipoConsultaIntermediariaSelected = null;
        limpaOpcoesPesquisa();
        pesquisaHabilitada = true;
        exibeResultados = false;
        tipoElementoList = consultaOntologiaBO.retornaTiposElementos(true, true);
    }

    public void selecionaTipoConsultaIntermediaria() {
        limpaOpcoesPesquisa();

        if (tipoConsultaIntermediariaSelected != null) {
            if (tipoConsultaIntermediariaSelected.getId().equals(Short.valueOf("1"))
                    || tipoConsultaIntermediariaSelected.getId().equals(Short.valueOf("2"))) {
                boolean consideraAtor = tipoConsultaIntermediariaSelected.getId().equals(Short.valueOf("1"));
                boolean consideraPool = tipoConsultaIntermediariaSelected.getId().equals(Short.valueOf("2"));

                tipoElementoList = consultaOntologiaBO.retornaTiposElementos(consideraAtor, consideraPool);
                tipoElementoDestList = tipoElementoList;
            } else if (tipoConsultaIntermediariaSelected.getId().equals(Short.valueOf("6"))) {
                if (tipoElementoList != null && !tipoElementoList.isEmpty()) {
                    tipoElementoList.clear();
                }
                TipoElemento tipoElemento = new TipoElemento(Short.valueOf("0"), "Activity");
                tipoElementoList.add(tipoElemento);
                tipoElemento = new TipoElemento(Short.valueOf("2"), "Event");
                tipoElementoList.add(tipoElemento);
            }
        }
    }

    public void selecionaTipoElemento() {
        if (tipoElementoSelected != null) {
            subTipoElementoList = consultaOntologiaBO.retornaSubTiposElementos(tipoElementoSelected.getId());
        }
    }

    public String onFlowProcess(FlowEvent event) {
        if (event.getNewStep().equals("pesquisa")) {
            if (ontologiaSelected == null) {
                FacesMessage msg = new FacesMessage(SEVERITY_WARN, "Atenção", "Selecione uma ontologia para realizar a consulta!");
                FacesContext.getCurrentInstance().addMessage(null, msg);

                return event.getOldStep();
            }
        } else if (event.getNewStep().equals("selecaoOntologia")) {
            limpaPesquisa();
            consultaOntologiaBO.limpaOntologia();
        }

        return event.getNewStep();
    }

    public LazyDataModel getLazyOntologiaModel() {
        return lazyOntologiaModel;
    }

    public Ontologia getOntologiaSelected() {
        return ontologiaSelected;
    }

    public void setOntologiaSelected(Ontologia ontologiaSelected) {
        this.ontologiaSelected = ontologiaSelected;
    }

    public List<TipoConsulta> getTipoConsultaList() {
        return tipoConsultaList;
    }

    public TipoConsulta getTipoConsultaSelected() {
        return tipoConsultaSelected;
    }

    public void setTipoConsultaSelected(TipoConsulta tipoConsultaSelected) {
        this.tipoConsultaSelected = tipoConsultaSelected;
    }

    public TipoConsulta getTipoConsultaIntermediariaSelected() {
        return tipoConsultaIntermediariaSelected;
    }

    public void setTipoConsultaIntermediariaSelected(TipoConsulta tipoConsultaIntermediariaSelected) {
        this.tipoConsultaIntermediariaSelected = tipoConsultaIntermediariaSelected;
    }

    public List<TipoElemento> getTipoElementoList() {
        return tipoElementoList;
    }

    public List<TipoElemento> getTipoElementoDestList() {
        return tipoElementoDestList;
    }

    public List<SubTipoElemento> getSubTipoElementoList() {
        return subTipoElementoList;
    }

    public List<AtributoElemento> getAtributoElementoList() {
        return atributoElementoList;
    }

    public List<TipoConsulta> getTipoConsultaIntermediariaList() {
        return tipoConsultaIntermediariaList;
    }

    public void setTipoConsultaIntermediariaList(List<TipoConsulta> tipoConsultaIntermediariaList) {
        this.tipoConsultaIntermediariaList = tipoConsultaIntermediariaList;
    }

    public TipoElemento getTipoElementoSelected() {
        return tipoElementoSelected;
    }

    public void setTipoElementoSelected(TipoElemento tipoElementoSelected) {
        this.tipoElementoSelected = tipoElementoSelected;
    }

    public SubTipoElemento getSubTipoElementoSelected() {
        return subTipoElementoSelected;
    }

    public void setSubTipoElementoSelected(SubTipoElemento subTipoElementoSelected) {
        this.subTipoElementoSelected = subTipoElementoSelected;
    }

    public AtributoElemento getAtributoElementoSelected() {
        return atributoElementoSelected;
    }

    public void setAtributoElementoSelected(AtributoElemento atributoElementoSelected) {
        this.atributoElementoSelected = atributoElementoSelected;
    }

    public TipoElemento getElementoOrigemSelected() {
        return elementoOrigemSelected;
    }

    public void setElementoOrigemSelected(TipoElemento elementoOrigemSelected) {
        this.elementoOrigemSelected = elementoOrigemSelected;
    }

    public TipoElemento getElementoDestinoSelected() {
        return elementoDestinoSelected;
    }

    public void setElementoDestinoSelected(TipoElemento elementoDestinoSelected) {
        this.elementoDestinoSelected = elementoDestinoSelected;
    }

    public String getFiltroPesquisa() {
        return filtroPesquisa;
    }

    public void setFiltroPesquisa(String filtroPesquisa) {
        this.filtroPesquisa = filtroPesquisa;
    }

    public String getFiltroAtorAtividade() {
        return filtroAtorAtividade;
    }

    public void setFiltroAtorAtividade(String filtroAtorAtividade) {
        this.filtroAtorAtividade = filtroAtorAtividade;
    }

    public List<ConsultaDTO> getConsultaDTOList() {
        return consultaDTOList;
    }

    public void setConsultaDTOList(List<ConsultaDTO> consultaDTOList) {
        this.consultaDTOList = consultaDTOList;
    }

    public boolean getPesquisaHabilitada() {
        return pesquisaHabilitada;
    }

    public boolean getExibeResultados() {
        return exibeResultados;
    }

    public String getMsgHelpTipoConsulta() {
        return msgHelpTipoConsulta;
    }

    public String getMsgTipoElementoOrigem() {
        return msgTipoElementoOrigem;
    }

    public void setMsgTipoElementoOrigem(String msgTipoElementoOrigem) {
        this.msgTipoElementoOrigem = msgTipoElementoOrigem;
    }

    public String getMsgTipoElementoDestino() {
        return msgTipoElementoDestino;
    }

    public void setMsgTipoElementoDestino(String msgTipoElementoDestino) {
        this.msgTipoElementoDestino = msgTipoElementoDestino;
    }

    public String getMsgTipoConsultaAvancada() {
        return msgTipoConsultaAvancada;
    }

    public String getMsgTipoElemento() {
        return msgTipoElemento;
    }

    public String getMsgSubTipoElemento() {
        return msgSubTipoElemento;
    }

    public String getMsgAtributo() {
        return msgAtributo;
    }

    public String getMsgFiltroPesquisa() {
        return msgFiltroPesquisa;
    }    
}
