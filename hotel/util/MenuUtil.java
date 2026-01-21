package hotel.util;

import java.util.Scanner;
import java.time.LocalDate;
import java.util.InputMismatchException;

/**
 * Classe utilitária com métodos estáticos para suportar operações comuns do Menu
 */
public class MenuUtil {
    
    /**
     * Lê um inteiro do scanner com validação
     * @param prompt Mensagem exibida ao utilizador
     * @param scanner Scanner para entrada
     * @return Inteiro válido digitado pelo utilizador
     */
    public static int lerInteiro(String prompt, Scanner scanner) {
        int valor = 0;
        boolean valido = false;
        while (!valido) {
            System.out.print(prompt);
            try {
                valor = scanner.nextInt();
                scanner.nextLine();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                scanner.nextLine();
            }
        }
        return valor;
    }

    /**
     * Lê um inteiro dentro de um intervalo específico
     * @param prompt Mensagem exibida ao utilizador
     * @param minimo Valor mínimo permitido
     * @param maximo Valor máximo permitido
     * @param scanner Scanner para entrada
     * @return Inteiro válido dentro do intervalo
     */
    public static int lerInteiroBounds(String prompt, int minimo, int maximo, Scanner scanner) {
        int valor;
        boolean valido = false;
        while (!valido) {
            valor = lerInteiro(prompt, scanner);
            if (valor < minimo || valor > maximo) {
                System.out.println("Erro: Escolha um número entre " + minimo + " e " + maximo + ".");
                continue;
            }
            return valor;
        }
        return 0;
    }

    /**
     * Lê um inteiro positivo
     * @param prompt Mensagem exibida ao utilizador
     * @param scanner Scanner para entrada
     * @return Inteiro positivo
     */
    public static int lerInteiroPositivo(String prompt, Scanner scanner) {
        int valor;
        boolean valido = false;
        while (!valido) {
            valor = lerInteiro(prompt, scanner);
            if (valor <= 0) {
                System.out.println("Erro: O valor deve ser maior que 0.");
                continue;
            }
            return valor;
        }
        return 0;
    }

    /**
     * Lê uma data no formato dia/mês do scanner
     * @param ano Ano a usar (fixo)
     * @param scanner Scanner para entrada
     * @return LocalDate com a data lida
     */
    public static LocalDate lerData(int ano, Scanner scanner) {
        LocalDate data = null;
        boolean valido = false;
        while (!valido) {
            try {
                System.out.print("> Dia: ");
                int dia = scanner.nextInt();
                System.out.print("> Mês: ");
                int mes = scanner.nextInt();
                data = LocalDate.of(ano, mes, dia);
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Erro: Data inválida: " + e.getMessage());
                scanner.nextLine();
            }
        }
        return data;
    }

    /**
     * Exibe um título de seção formatado
     * @param titulo Texto do título
     */
    public static void exibirTitulo(String titulo) {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║ " + String.format("%-38s", titulo) + " ║");
        System.out.println("╚════════════════════════════════════════╝");
    }

    /**
     * Exibe um subtítulo de seção
     * @param subtitulo Texto do subtítulo
     */
    public static void exibirSubtitulo(String subtitulo) {
        System.out.println("\n" + subtitulo);
        System.out.println("────────────────────────────────────────");
    }

    /**
     * Exibe um separador financeiro
     */
    public static void exibirSeparador() {
        System.out.println("════════════════════════════════════════");
    }

    /**
     * Valida a seleção de um item em uma lista
     * @param escolha Número escolhido (1-based)
     * @param tamanho Tamanho da lista
     * @return true se válido, false caso contrário
     */
    public static boolean validarSelecao(int escolha, int tamanho) {
        return escolha > 0 && escolha <= tamanho;
    }

    /**
     * Exibe mensagem de erro para seleção inválida
     */
    public static void erroSelecaoInvalida() {
        System.out.println("Erro: Seleção inválida!");
    }

    /**
     * Pausa a execução para o utilizador ler a mensagem
     */
    public static void pausar(Scanner scanner) {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }
}
