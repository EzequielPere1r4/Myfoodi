package br.ufal.ic.p2.myfood;


public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante(String tipoEmpresa, String nome, String endereco, String tipoCozinha) {
        super(tipoEmpresa, nome, endereco);
        this.tipoCozinha = tipoCozinha;
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    @Override
    public boolean isMercado() {
        return false;
    }

    @Override
    public boolean isFarmacia(){return false;}

    @Override
    public void setAtributo(String atributo, String valor ){

    }

    public String getAtributo(String atributo){
        if ("tipoCozinha".equalsIgnoreCase(atributo)) {
            return getTipoCozinha();
        }
        return super.getAtributo(atributo);
    }


    //produtos










    
}