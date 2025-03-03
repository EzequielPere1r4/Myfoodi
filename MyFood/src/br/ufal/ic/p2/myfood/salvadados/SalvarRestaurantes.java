package br.ufal.ic.p2.myfood.salvadados;

import br.ufal.ic.p2.myfood.Empresa;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SalvarRestaurantes {

    private static final String FILE_PATH = "restaurantes.dat";

    public static void salvarRestaurantesDODono(Map<Integer, List<Empresa>> restaurantesPorDono) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(restaurantesPorDono);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Integer, List<Empresa>> carregarRestaurantesDODono() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (Map<Integer, List<Empresa>>) ois.readObject();
        }
    }
}