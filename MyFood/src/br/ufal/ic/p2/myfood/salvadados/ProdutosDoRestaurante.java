package br.ufal.ic.p2.myfood.salvadados;

import br.ufal.ic.p2.myfood.Produto;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdutosDoRestaurante {

    private static final String FILE_PATH = "produtoDoRestaurante.dat";

    public static void salvarProdutoDoRestaurante(Map<Integer, List<Produto>> produtosDoRestaurante) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(produtosDoRestaurante);
        }
    }
//troca aq
    @SuppressWarnings("unchecked")
    public static Map<Integer, List<Produto>> carregarProdutoDoRestaurante() throws IOException,
            ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<Integer, List<Produto>>) ois.readObject();
        }
    }
}