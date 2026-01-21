package hotel.storage;

import hotel.model.Hotel;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StorageManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String HOTEL_FILE = DATA_DIRECTORY + File.separator + "hotel.dat";

    static {
        // Criar diretório de dados se não existir
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Erro ao criar diretório de dados: " + e.getMessage());
        }
    }

    /**
     * Carrega o estado do hotel do ficheiro de persistência
     * Se o ficheiro não existir, retorna null
     */
    public static Hotel loadHotel() {
        if (!Files.exists(Paths.get(HOTEL_FILE))) {
            System.out.println("Ficheiro de dados não encontrado. Iniciando sistema do zero...");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(HOTEL_FILE))) {
            Hotel hotel = (Hotel) ois.readObject();
            System.out.println("✓ Dados carregados com sucesso do ficheiro!");
            return hotel;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return null;
        }
    }

    /**
     * Grava o estado do hotel no ficheiro de persistência
     * Sobrescreve o ficheiro anterior
     */
    public static void saveHotel(Hotel hotel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(HOTEL_FILE))) {
            oos.writeObject(hotel);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Erro ao gravar dados: " + e.getMessage());
        }
    }

    /**
     * Limpa todos os dados persistidos (útil para testes/reset)
     */
    public static void clearData() {
        try {
            if (Files.exists(Paths.get(HOTEL_FILE))) {
                Files.delete(Paths.get(HOTEL_FILE));
                System.out.println("Dados persistidos removidos com sucesso.");
            }
        } catch (IOException e) {
            System.err.println("Erro ao limpar dados: " + e.getMessage());
        }
    }

    /**
     * Verifica se já existem dados persistidos
     */
    public static boolean dataExists() {
        return Files.exists(Paths.get(HOTEL_FILE));
    }
}
