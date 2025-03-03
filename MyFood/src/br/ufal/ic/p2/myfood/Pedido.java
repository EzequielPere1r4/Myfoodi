package br.ufal.ic.p2.myfood;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contador = 0;
    private int numero;
    private String cliente;
    private String empresa;
    private String estado;
    private List<Produto> produtos;
    private float valor;

    public Pedido(String cliente, String empresa) {
        this.numero = ++contador;
        this.cliente = cliente;
        this.empresa = empresa;
        this.estado = "aberto";
        this.produtos = new ArrayList<>();
        this.valor = 0;
    }



    // Getters e Setters
    public int getNumero() {
        return numero;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEmpresa() {
        return empresa;
    }

    public String getEstado() {
        return estado;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public float getValor() {
        return valor;
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
        valor += produto.getValor(); 
    }

    public void finalizarPedido() {
        this.estado = "preparando";
    }



    // Método para remover o produto do restaurante
    public boolean removerProdutoPorNome(String nomeProduto) {
        for (Iterator<Produto> it = produtos.iterator(); ((Iterator<?>) it).hasNext(); ) {
            Produto produto = it.next();
            if (produto.getNome().equals(nomeProduto)) {
                it.remove();
                valor -= produto.getValor();
                return true;
            }
        }
        return false;
    }

    public void setEstado(String pronto) {
        this.estado = pronto;
    }
}
