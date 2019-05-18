package com.lukas.pm2onto.controller;

import com.lukas.pm2onto.bo.GeracaoOntologiasBO;
import com.lukas.pm2onto.dto.ArquivoDTO;
import com.lukas.pm2onto.utils.FileDirUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_WARN;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author lukas
 */
@Named
@ViewScoped
public class GeracaoOntologiaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<File> arquivosXpdlList;
    private List<String> nomesArquivosXpdlList;
    private String arquivoSelecionado;
    private GeracaoOntologiasBO geracaoOntologiasBO;
    private String nomeOntologia, descricaoOntologia;
    private List<ArquivoDTO> arquivosOntologiasList;

    public GeracaoOntologiaController() {
    }

    @PostConstruct
    public void iniciaForm() {
        removeArquivosEDiretorios(false);
    }

    public void realizaUploadArquivos(FileUploadEvent event) {
        try {
            File arquivoXPDL = FileDirUtils.geraArquivo(event.getFile().getFileName());

            FileUtils.writeByteArrayToFile(arquivoXPDL, event.getFile().getContents());

            arquivosXpdlList.add(arquivoXPDL);

            nomesArquivosXpdlList.add(arquivoXPDL.getPath());

            FacesMessage msg = new FacesMessage("Sucesso", "O arquivo ".concat(event.getFile().getFileName()) + " foi carregado!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao realizar o upload do arquivo!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void removeArquivoXPDL() {
        try {
            if (arquivoSelecionado != null && !arquivoSelecionado.isEmpty()) {
                arquivosXpdlList = arquivosXpdlList.stream().filter(f -> !f.getAbsolutePath().equals(arquivoSelecionado))
                        .collect(Collectors.toList());
                nomesArquivosXpdlList.remove(arquivoSelecionado);

                FileDirUtils.removeArquivoOuDiretorio(new File(arquivoSelecionado));

                FacesMessage msg = new FacesMessage("Sucesso", "O arquivo ".concat(arquivoSelecionado) + " foi removido!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao remover arquivo!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void removeArquivosOWL() {
        try {
            if (arquivosOntologiasList != null && !arquivosOntologiasList.isEmpty()) {
                for (ArquivoDTO arqDTO : arquivosOntologiasList) {
                    if (arqDTO.getConteudo() != null && arqDTO.getConteudo().getStream() != null) {
                        arqDTO.getConteudo().getStream().close();
                    }
                }
            }

            if (geracaoOntologiasBO.getArquivosOntologiasList() != null
                    && !geracaoOntologiasBO.getArquivosOntologiasList().isEmpty()) {
                for (File arq : geracaoOntologiasBO.getArquivosOntologiasList()) {
                    FileDirUtils.removeArquivoOuDiretorio(arq);
                }

                geracaoOntologiasBO.getArquivosOntologiasList().clear();
            }

            FacesMessage msg = new FacesMessage(SEVERITY_WARN, "Aviso",
                    "O(s) arquivo(s) em OWL da(s) ontologia(s) gerada(s) foi(ram) removido(s)!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao remover o(s) arquivo(s) em OWL da(s) ontologia(s) geradas!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void removeArquivosEDiretorios(boolean exibeMensagem) {
        File diretorioTemporario = null;

        try {
            diretorioTemporario = FileDirUtils.getDiretorioTemporario();
            FileDirUtils.removeArquivoOuDiretorio(diretorioTemporario);

            arquivosXpdlList = new ArrayList();
            nomesArquivosXpdlList = new ArrayList();
            arquivoSelecionado = null;
            arquivosOntologiasList = new ArrayList();

            if (exibeMensagem) {
                FacesMessage msg = new FacesMessage("Sucesso", new StringBuilder("O(s) arquivo(s) do Upload foram removidos com sucesso da pasta temporária ")
                        .append(diretorioTemporario.getAbsolutePath())
                        .append("gerada!").toString());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } catch (IOException ex) {
            if (exibeMensagem) {
                FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", new StringBuilder("Erro ao remover arquivo(s) do Upload da pasta temporária ")
                        .append(diretorioTemporario != null ? diretorioTemporario.getAbsolutePath() : "")
                        .append("gerada!")
                        .append(ex.getMessage() == null || ex.getMessage().isEmpty() ? ""
                                : "\nErro:".concat(ex.getMessage())).toString());
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        }
    }

    public void geraOntologias() {
        try {
            geracaoOntologiasBO = new GeracaoOntologiasBO();

            if (arquivosOntologiasList != null && !arquivosOntologiasList.isEmpty()) {
                arquivosOntologiasList.clear();
            }

            if (arquivosXpdlList == null || arquivosXpdlList.isEmpty()) {
                FacesMessage msg = new FacesMessage(SEVERITY_WARN, "Atenção", "Selecione ao menos um arquivo XPDL "
                        + "para a geração de Ontologia(s)!!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            } else {
                geracaoOntologiasBO.preenchePacoteProcesso(arquivosXpdlList);
                geracaoOntologiasBO.setNomeOntologia(nomeOntologia);
                geracaoOntologiasBO.setDescricaoOntologia(descricaoOntologia);
                geracaoOntologiasBO.geraProcessos();
                if (geracaoOntologiasBO.getModelo().getProcessoList() != null
                        && !geracaoOntologiasBO.getModelo().getProcessoList().isEmpty()) {
                    geracaoOntologiasBO.geraOntologia();

                    if (geracaoOntologiasBO.getArquivosOntologiasList() != null
                            && !geracaoOntologiasBO.getArquivosOntologiasList().isEmpty()) {
                        int indice = 1;

                        for (File file : geracaoOntologiasBO.getArquivosOntologiasList()) {
                            ArquivoDTO arqDTO = new ArquivoDTO();
                            arqDTO.setCaminhoArquivo(String.valueOf(indice).concat("-").concat(file.getName()));
                            arqDTO.setConteudo(new DefaultStreamedContent(new FileInputStream(file),
                                    "OWL/XML", file.getName()));

                            if (arquivosOntologiasList == null) {
                                arquivosOntologiasList = new ArrayList();
                            }

                            arquivosOntologiasList.add(arqDTO);
                        }

                        RequestContext.getCurrentInstance().addCallbackParam("gerouOntologia", this);
                    }
                }
            }
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(SEVERITY_ERROR, "Erro", "Erro ao gerar a ontologia!!"
                    .concat(e.getMessage() == null || e.getMessage().isEmpty() ? "" : "\n".concat(e.getMessage())));
            FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    }

    public List<File> getArquivosXpdlList() {
        return arquivosXpdlList;
    }

    public List<String> getNomesArquivosXpdlList() {
        return nomesArquivosXpdlList;
    }

    public String getArquivoSelecionado() {
        return arquivoSelecionado;
    }

    public void setArquivoSelecionado(String arquivoSelecionado) {
        this.arquivoSelecionado = arquivoSelecionado;
    }

    public String getNomeArquivo(String nomeArquivo) {
        int indiceSeparador = nomeArquivo.lastIndexOf("/");

        if (indiceSeparador != -1) {
            return nomeArquivo.substring(indiceSeparador + 1, nomeArquivo.length());
        } else {
            return nomeArquivo;
        }
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

    public List<ArquivoDTO> getArquivosOntologiasList() {
        return arquivosOntologiasList;
    }
}
