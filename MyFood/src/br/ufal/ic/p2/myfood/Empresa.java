package br.ufal.ic.p2.myfood;

 
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Empresa implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int contadorId = 1;
    private int id;
    private String tipoEmpresa;
    private String nome;
    private String endereco;
    private List<Entregador> entregadores;

    public Empresa(String tipoEmpresa, String nome, String endereco) {
        this.id = contadorId++;
        this.tipoEmpresa = tipoEmpresa;
        this.nome = nome;
        this.endereco = endereco;
    }

    public List<Entregador> getEntregadores() {
        if (entregadores == null) {
            entregadores = new ArrayList<>();
        }
        return entregadores;
    }

    public void setEntregadores(List<Entregador> entregadores) {
        this.entregadores = entregadores;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() { return endereco; }

    public String   getTipoEmpresa(){ return tipoEmpresa; }

    public abstract boolean isMercado();


    public abstract boolean isFarmacia();

    public abstract void setAtributo(String atributo, String valor);

    public String getAtributo(String atributo){
        switch (atributo) {
            case "nome":
                return getNome();
            case "endereco":
                return getEndereco();
            case "tipoEmpresa":
                return getTipoEmpresa();
            default:
                throw new IllegalArgumentException("Atributo invalido");
        }
    }

}
