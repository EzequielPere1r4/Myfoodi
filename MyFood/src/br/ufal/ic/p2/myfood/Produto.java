package br.ufal.ic.p2.myfood;

import java.io.Serializable;

public class Produto implements Serializable {
    private static int idCounter = 0;
    private int id;
    private String nome;
    private float valor;
    private String categoria;
    private static final long serialVersionUID = 1L;
    // construtor
    public Produto(String nome, float valor, String categoria) {
        this.id = ++idCounter;
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public float getValor() {
        return valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getAtributo(String atributo) {
        switch (atributo) {
            case "nome":
                return getNome();
            case "categoria":
                return getCategoria();
            default:
                throw new IllegalArgumentException("Atributo nao existe");
        }
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome invalido");
        }
        this.nome = nome;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }


     public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria invalida");
        }
        this.categoria = categoria;
    }

    
}