package br.ufal.ic.p2.myfood;

 
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import br.ufal.ic.p2.myfood.salvadados.GuardaPedidos;
import br.ufal.ic.p2.myfood.salvadados.SalarUsuarios;

public class Entrega implements Serializable {
    private int id; 
    private int idPedido; 
    private int idEntregador; 
    private String destino; 

      public Entrega(int id, int idPedido, int idEntregador, String destino) {
        this.id = id;
        this.idPedido = idPedido;
        this.idEntregador = idEntregador;
        this.destino = destino;
    }

     public int getId() {
        return id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdEntregador() {
        return idEntregador;
    }

    public String getDestino() {
        return destino;
    }
 
     public String getAtributo(String atributo) throws ClassNotFoundException, IOException{

        Map<Integer, Pedido> pedidos = GuardaPedidos.carregarPedidos();
        Pedido pedido = pedidos.get(idPedido);
        Map<Integer, Usuario> usuarios = SalarUsuarios.carregarUsuarios();
        Usuario entregador = usuarios.get(idEntregador); 
        pedidos.clear();///////
        usuarios.clear();//////

        switch (atributo.toLowerCase()) {
            case "pedido":
                return String.valueOf(getIdPedido());
            case "entregador":
                if (entregador != null) {
                    return entregador.getNome(); 
                }
                throw new IllegalArgumentException("Atributo invalido");
            case "cliente":
                if (pedido != null) {
                    return pedido.getCliente();
                }
                throw new IllegalArgumentException("Atributo invalido");
            case "empresa":
                if (pedido != null) {
                    return pedido.getEmpresa(); 
                }
                throw new IllegalArgumentException("Atributo invalido");
            case "destino":


            
                return getDestino();
            default:
            throw new IllegalArgumentException("Atributo nao existe");
        }
    }

   
}
