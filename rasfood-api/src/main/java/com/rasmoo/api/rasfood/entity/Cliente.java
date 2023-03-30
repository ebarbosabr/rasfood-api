package com.rasmoo.api.rasfood.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente {

    private String nome;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Endereco> enderecoList = new ArrayList<>();

    @Embedded
    private Contato contato;
    @EmbeddedId
    private ClienteId clienteId = new ClienteId();

    public Cliente(String cpf, String email, String nome) {
        this.clienteId = new ClienteId(cpf, email);
        this.nome = nome;
    }

    public Cliente() {
    }

    public void addEndereco(Endereco endereco){
        endereco.setCliente(this);
        this.enderecoList.add(endereco);
    }

    public String getCpf() {
        return this.clienteId.getCpf();
    }

    public void setCpf(String cpf) {

        this.clienteId.setCpf(cpf);
    }

    public String getEmail() {
        return this.clienteId.getEmail();
    }

    public void setEmail(String email) {

        this.clienteId.setEmail(email);
    }

    public String getNome() {

        return nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public List<Endereco> getEnderecoList() {

        return enderecoList;
    }

    public void setEnderecoList(List<Endereco> enderecoList) {

        this.enderecoList = enderecoList;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cpf='" + this.clienteId.getCpf() + '\'' +
                ", email='" + this.clienteId.getEmail() + '\'' +
                ", nome='" + nome + '\'' +
                ", enderecoList=" + enderecoList +
                ", contato=" + contato +
                '}';
    }
}