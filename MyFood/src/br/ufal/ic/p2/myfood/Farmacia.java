package br.ufal.ic.p2.myfood;


public class Farmacia extends Empresa  {

    private boolean aberto24Horas;
    private int numeroFuncionarios;

    public Farmacia(String tipoEmpresa, String nome, String endereco, boolean aberto24Horas, int numeroFuncionarios) {
        super(tipoEmpresa, nome, endereco);
        this.aberto24Horas = aberto24Horas;
        this.numeroFuncionarios = numeroFuncionarios;
    }

    @Override
    public boolean isMercado() {
        return false;
    }

    @Override
    public boolean isFarmacia(){return true;}

    @Override
    public void setAtributo(String atributo, String valor) {

    }

    public boolean getAberto24Horas() {
        return aberto24Horas;
    }

    public int getNumeroFuncionarios() {
        return numeroFuncionarios;
    }

    @Override
    public String getAtributo(String atributo){
        switch (atributo.toLowerCase()) {
            case "aberto24horas":
                return getAberto24Horas() ? "true" : "false";
            case "numerofuncionarios":
                return "" + getNumeroFuncionarios();
            default:
                return super.getAtributo(atributo);
        }
    }
}
