package br.ufal.ic.p2.myfood.salvadados;

import br.ufal.ic.p2.myfood.Empresa;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GuardaMercado {

    private static final String FILE_PATH = "mercado.dat";

    public static void salvarMercados(Map<Integer, Empresa> mercado) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(mercado);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, Empresa> carregarMercados() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<Integer, Empresa>) ois.readObject();
        }
    }
}