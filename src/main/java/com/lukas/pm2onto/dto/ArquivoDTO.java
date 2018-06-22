package com.lukas.pm2onto.dto;

import java.io.Serializable;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author lukas
 */
public class ArquivoDTO implements Serializable{
    
    private String caminhoArquivo;
    private StreamedContent conteudo;
    
    public ArquivoDTO(){
        
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public StreamedContent getConteudo() {
        return conteudo;
    }

    public void setConteudo(StreamedContent conteudo) {
        this.conteudo = conteudo;
    }    
}
