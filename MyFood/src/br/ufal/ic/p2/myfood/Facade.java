package br.ufal.ic.p2.myfood;

 
import java.io.IOException;


public class Facade {

    private Sistema sistema;

    
    public Facade() throws IOException, ClassNotFoundException {
        sistema = new Sistema();
    }

    public void zerarSistema() {
        sistema.zerarSistema();
    }

    public void criarUsuario(String nome, String email, String senha, String endereco){
        sistema.criarUsuario(nome, email, senha, endereco);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String cpf){
        sistema.criarUsuario(nome, email, senha, endereco, cpf);
    }

    public void criarUsuario(String nome, String email, String senha, String endereco, String veiculo, String placa){
        sistema.criarUsuario(nome, email, senha, endereco, veiculo, placa);
    }

    public void cadastrarEntregador(int idEmpresa, int idEntregador){
        sistema.cadastrarEntregador(idEmpresa, idEntregador);
    }

    public String getEntregadores(int idEmpresa){
        return sistema.getEntregadores(idEmpresa);
    }

    public String getEmpresas(int idEntregador){
        return sistema.getEmpresas(idEntregador);
    }

    public int login(String email, String senha){
        return sistema.login(email, senha);
    }




//2 
    public String getAtributoUsuario(int id, String atributo){
        return sistema.getAtributoUsuario(id, atributo);
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String tipoCozinha){
        return sistema.criarEmpresa(tipoEmpresa, idDono, nome, endereco, tipoCozinha);
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, String abre, String fecha,
                            String tipoMercado){
        return sistema.criarEmpresa(tipoEmpresa, idDono, nome, endereco, abre, fecha, tipoMercado);
    }

    public void alterarFuncionamento(int mercadoId, String abre, String fecha){
        sistema.alterarFuncionamento(mercadoId, abre, fecha);
    }

    public int criarEmpresa(String tipoEmpresa, int idDono, String nome, String endereco, boolean aberto24Horas,int numeroFuncionarios){
        return sistema.criarEmpresa(tipoEmpresa, idDono, nome, endereco, aberto24Horas, numeroFuncionarios);
    }


    public String getEmpresasDoUsuario(int idDono){
        return sistema.getEmpresasDoUsuario(idDono);
    }

    public int getIdEmpresa(int idDono, String nome, int indice){
        return sistema.getIdEmpresa(idDono, nome, indice);
    }

    public String getAtributoEmpresa(int empresaId, String atributo){
        return sistema.getAtributoEmpresa(empresaId, atributo);
    }




    //3
    public int criarProduto(int empresa, String nome, float valor, String categoria){
        return sistema.criarProduto(empresa, nome, valor, categoria);
    }

    public void editarProduto(int produto, String nome, float valor, String categoria){
        sistema.editarProduto(produto, nome, valor, categoria);
    }

    public String getProduto(String nome, int empresa, String atributo){
        return sistema.getProduto(nome, empresa, atributo);
    }

    public String listarProdutos(int empresa){
        return sistema.listarProdutos(empresa);
    }






    //4
    public int criarPedido(int clienteId, int empresaId){
        return sistema.criarPedido(clienteId, empresaId);
    }

    public void adicionarProduto(int numero, int produto){
        sistema.adicionarProduto(numero, produto);
    }

    public String getPedidos(int numeroPedido, String atributo){
        return sistema.getPedidos(numeroPedido, atributo);
    }

    public void fecharPedido(int numeroPedido){
        sistema.fecharPedido(numeroPedido);
    }

    public void removerProduto(int numeroPedido, String nomeProduto){
        sistema.removerProduto(numeroPedido, nomeProduto);
    }

    public int getNumeroPedido(int clienteId, int empresaId, int indice) {
        return sistema.getNumeroPedido(clienteId, empresaId, indice);
    }

    public void liberarPedido(int numero){
        sistema.liberarPedido(numero);
    }

    public int obterPedido(int idEntregador){
        return sistema.obterPedido(idEntregador);
    }


    //5



    public int criarEntrega(int idPedido, int idEntregador, String destino) {
        return sistema.criarEntrega(idPedido, idEntregador, destino);
    }

    public String getEntrega(int id, String atributo) throws ClassNotFoundException, IOException{
        return sistema.getEntrega(id, atributo);
    }

    public int getIdEntrega(int pedido){
        return sistema.getIdEntrega(pedido);
    }

    public void entregar(int idEntrega){
         sistema.entregar(idEntrega);
    }



    public void encerrarSistema() throws IOException {
        sistema.encerrarSistema();
    }
}
