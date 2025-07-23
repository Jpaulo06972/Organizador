
package com.todolist.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {
    
    private int id;
    private String titulo;
    private String descricao;
    private boolean concluida;
    private int usuarioId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getTitulo(){
        return titulo;
    }
    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public String getDescricao(){
        return descricao;
    }
    public void setDescricao(String descricao){
        this.descricao = descricao;}


    public boolean isConcluida(){
        return concluida;
    }
    public void setConcluida(boolean concluida){
        this.concluida = concluida;
    }

    public int getUsuarioId(){
        return usuarioId;
    }
    public void setUsuarioId(int usuarioId){
        this.usuarioId = usuarioId;
    }
    
    public LocalDateTime getDataCriacao(){
        return dataCriacao;        
    }
    public void setDataCriacao(LocalDateTime dataCriacao){
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataConclusao(){
        return dataConclusao;        
    }
    public void setDataConclusao(LocalDateTime dataConclusao){
        this.dataConclusao = dataConclusao;
    }

    public Task() {}
    public Task(String titulo, String descricao, boolean concluida, int usuarioId){
        this.titulo = titulo;
        this.descricao = descricao;
        this.concluida = concluida;
        this.usuarioId = usuarioId;
        this.dataConclusao = LocalDateTime.now(); // Define no momento da criação
    }

    @Override
    public String toString() {
        return "ID: " + id +
            "\nTítulo: " + titulo +
            "\nDescrição: " + descricao +
            "\nConcluída: " + concluida +
            "\nData de Criação: " + (dataCriacao != null ? dataCriacao : "N/A") +
            "\nData de Conclusão: " + (dataConclusao != null ? dataConclusao : "N/A") +
            "\n";
    }
      
}

