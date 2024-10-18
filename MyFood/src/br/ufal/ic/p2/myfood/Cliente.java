package br.ufal.ic.p2.myfood;

public class Cliente extends Usuario {
    public Cliente(String nome, String email, String senha, String endereco) {
        super(nome, email, senha, endereco);
    }

    public boolean podeCriarEmpresa() {
        return false; 
    }
    public boolean ehEntregador(){ 
        return false;}

}
