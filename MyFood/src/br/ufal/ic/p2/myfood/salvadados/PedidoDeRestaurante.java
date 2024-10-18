package br.ufal.ic.p2.myfood.salvadados;

import br.ufal.ic.p2.myfood.Pedido;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoDeRestaurante {

    private static final String FILE_PATH = "pedidorPorRestaurante.dat";

    public static void salvarPedidosPorRestaurante(Map<Integer, List<Pedido>> pedidosDoRestaurante) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(pedidosDoRestaurante);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, List<Pedido>> carregarPedidosPorRestaurante() throws IOException,
            ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<Integer, List<Pedido>>) ois.readObject();
        }
    }
}