package hotel.model;

import hotel.model.*;
import hotel.model.enums.*;
import hotel.service.PricingService;
import hotel.service.ReservationService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.IllformedLocaleException;
import java.util.Scanner;
import java.util.List;

public class Menu {
    private Scanner scanner;
    private Hotel hotel;
    private ReservationService reservationService = new ReservationService();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private PricingService pricingService = reservationService.getPricingService();
    private int nquartos = 101;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.hotel = new Hotel("Hotel Java");
        this.reservationService = new ReservationService();

    }

    private void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Se falhar, imprime linhas em branco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private String formatarCliente(Client client) {
        return String.format("%-20s | Doc: %-12s | Tel: %-15s | Email: %s",
                client.getFullName(),
                client.getDocument(),
                client.getPhone(),
                client.getEmail());
    }

    private String formatarReserva(Reservation reservation, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n┌─────────────────────────────────────────────────────────────┐\n");
        sb.append(String.format("│ RESERVA #%d - Código: %-40s │\n", index, reservation.getReservationCode()));
        sb.append("├─────────────────────────────────────────────────────────────┤\n");
        sb.append(String.format("│ Cliente: %-50s │\n", reservation.getClient().getFullName()));
        sb.append(String.format("│ Quarto: #%-3d (%-8s) | Hóspedes: %-3d                │\n",
                reservation.getRoom().getNumber(),
                reservation.getRoom().getType(),
                reservation.getNumberOfGuests()));
        sb.append(String.format("│ Check-in: %-12s | Check-out: %-12s | Noites: %-3d │\n",
                reservation.getCheckInDate().format(dateFormatter),
                reservation.getCheckOutDate().format(dateFormatter),
                reservation.getNights()));
        sb.append(String.format("│ Status: %-15s | Valor Total: %,.2f kz          │\n",
                reservation.getStatus(),
                pricingService.calculateReservationTotal(reservation)));
        sb.append("└─────────────────────────────────────────────────────────────┘");
        return sb.toString();
    }

    public void mostrarMenuPrincipal() {
        while (true) {
            limparTela();
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   SISTEMA DE GESTÃO HOTELEIRA         ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Gerenciar Reservas");
            System.out.println("  2. Gerenciar Quartos");
            System.out.println("  3. Gerenciar Clientes");
            System.out.println("  4. Gerenciar Serviços Adicionais");
            System.out.println("  5. Pagamentos");
            System.out.println("  0. Sair");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int opcao = 0;
            boolean entradaValida = false;
            while (!entradaValida) {
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();
                    entradaValida = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                    scanner.nextLine();
                    System.out.print("> Escolha uma opção: ");
                }
            }

            switch (opcao) {
                case 1:
                    menuReservas();
                    break;
                case 2:
                    menuQuartos();
                    break;
                case 3:
                    menuClientes();
                    break;
                case 4:
                    menuServicos();
                    break;
                case 5:
                    pagamentos();
                    break;
                case 0:
                    System.out.println("\nSistema encerrado com sucesso. Até logo!");
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 5.");
            }
        }
    }

    private void menuReservas() {
        while (true) {
            limparTela();
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      GESTÃO DE RESERVAS               ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Criar Nova Reserva");
            System.out.println("  2. Listar Todas as Reservas");
            System.out.println("  3. Realizar Check-in");
            System.out.println("  4. Realizar Check-out");
            System.out.println("  5. Cancelar Reserva");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int opcao = 0;
            boolean entradaValidaReservas = false;
            while (!entradaValidaReservas) {
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();
                    entradaValidaReservas = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                    scanner.nextLine();
                    System.out.print("> Escolha uma opção: ");
                }
            }

            switch (opcao) {
                case 1:
                    if (hotel.getClients().isEmpty()) {
                        System.out.println("\nAtenção: Não há clientes cadastrados! Por favor, cadastre um cliente primeiro.");
                        return;
                    } else if (hotel.getRooms().isEmpty()) {
                        System.out.println("\nAtenção: Não há quartos disponíveis! Por favor, adicione quartos ao sistema.");
                        return;
                    } else {
                        int id = 0;
                        System.out.println("\nSELEÇÃO DE CLIENTE");
                        System.out.println("────────────────────────────────────────");
                        boolean entradaValida = false;
                        while (!entradaValida) {
                            int clientIndex = 1;
                            for (Client c : hotel.getClients()) {
                                System.out.println(clientIndex + ". " + formatarCliente(c));
                                clientIndex++;
                            }
                            System.out.print("> Selecione o número do cliente: ");
                            try {
                                id = scanner.nextInt();
                                if (id <= 0 || id > hotel.getClients().size()) {
                                    System.out.println("Erro: Cliente não encontrado. Por favor, escolha um número válido da lista.");
                                } else {
                                    entradaValida = true;
                                }
                            } catch (RuntimeException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.next();
                            }
                        }
                        Client client = hotel.getClients().get(id - 1);

                        System.out.println("\nSELEÇÃO DE QUARTO");
                        System.out.println("────────────────────────────────────────");
                        entradaValida = false;
                        while (!entradaValida) {
                            hotel.getRooms().forEach(System.out::println);
                            System.out.print("> Selecione o número do quarto: ");
                            try {
                                id = scanner.nextInt();
                                if (id == 0 || id > hotel.getRooms().size()) {
                                    System.out.println("Erro: Quarto não encontrado. Por favor, escolha um número válido da lista.");
                                } else {
                                    entradaValida = true;
                                }
                            } catch (RuntimeException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.next();
                            }
                        }
                        Room room = hotel.getRooms().get(id - 1);

                        System.out.print("\n> Número de hóspedes: ");
                        int nh = 0;
                        boolean numHospedesValido = false;
                        while (!numHospedesValido) {
                            try {
                                nh = scanner.nextInt();
                                if (nh < 1) {
                                    System.out.println("Erro: O número de hóspedes deve ser maior que 0.");
                                    System.out.print("> Número de hóspedes: ");
                                } else {
                                    numHospedesValido = true;
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                                System.out.print("> Número de hóspedes: ");
                            }
                        }

                        System.out.println("\nDATA DE CHECK-IN");
                        LocalDate checkinDate = null;
                        boolean dataCheckinValida = false;
                        while (!dataCheckinValida) {
                            try {
                                System.out.print("> Dia: ");
                                int dia = scanner.nextInt();
                                System.out.print("> Mês: ");
                                int mes = scanner.nextInt();
                                checkinDate = LocalDate.of(2026, mes, dia);
                                dataCheckinValida = true;
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Erro: Data inválida: " + e.getMessage());
                                scanner.nextLine();
                            }
                        }

                        System.out.println("\nDATA DE CHECK-OUT");
                        LocalDate checkoutDate = null;
                        boolean dataCheckoutValida = false;
                        while (!dataCheckoutValida) {
                            try {
                                System.out.print("> Dia: ");
                                int dia = scanner.nextInt();
                                System.out.print("> Mês: ");
                                int mes = scanner.nextInt();
                                checkoutDate = LocalDate.of(2026, mes, dia);
                                dataCheckoutValida = true;
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Erro: Data inválida: " + e.getMessage());
                                scanner.nextLine();
                            }
                        }

                        LocalDate hoje = LocalDate.now();
                        try {
                            Reservation reservation = new Reservation(checkinDate, checkoutDate, nh, hoje, client, room);
                            reservationService.createReservation(reservation);

                            System.out.println("\n╔════════════════════════════════════════╗");
                            System.out.println("║   RESERVA CRIADA COM SUCESSO          ║");
                            System.out.println("╚════════════════════════════════════════╝");
                            System.out.println("Código da Reserva: " + reservation.getReservationCode());
                            System.out.println("Quarto: " + room.getNumber() + " (" + room.getType() + ")");
                            System.out.println("Check-in: " + checkinDate + " | Check-out: " + checkoutDate);
                            System.out.println("Noites: " + reservation.getNights());
                            System.out.println("Status: " + reservation.getStatus());

                            double lodgingValue = pricingService.calculateLodgingValue(reservation);
                            double servicesTotal = pricingService.calculateServicesTotal(reservation);
                            double reservationTotal = pricingService.calculateReservationTotal(reservation);
                            double balance = pricingService.calculateBalance(reservation);

                            System.out.println("\nDETALHAMENTO DE CUSTOS");
                            System.out.println("────────────────────────────────────────");
                            System.out.println("Hospedagem:");
                            System.out.println("  - Preço Base: " + String.format("%.2f", room.getDailyBasePrice()) + " kz × " +
                                    reservation.getNights() + " noites = " + String.format("%.2f", room.getDailyBasePrice() * reservation.getNights()) + " kz");
                            System.out.println("  - Multiplicador (" + room.getType() + "): " + room.getType().getMultiplier());
                            System.out.println("  - Subtotal Hospedagem: " + String.format("%.2f", lodgingValue) + " kz");

                            System.out.println("\nServiços Adicionais:");
                            if (reservation.getServices().isEmpty()) {
                                System.out.println("  - Nenhum serviço adicional");
                            } else {
                                for (AdditionalService service : reservation.getServices()) {
                                    double charge = service.calculateCharge(reservation.getNights());
                                    System.out.println("  - " + service.getDescription() + " (" + service.getBillingType() + "): " +
                                            String.format("%.2f", charge) + " kz");
                                }
                            }
                            System.out.println("  - Subtotal Serviços: " + String.format("%.2f", servicesTotal) + " kz");

                            System.out.println("\n═══════════════════════════════════════");
                            System.out.println("VALOR TOTAL DA RESERVA: " + String.format("%.2f", reservationTotal) + " kz");
                            System.out.println("Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation)) + " kz");
                            System.out.println("Saldo Restante: " + String.format("%.2f", balance) + " kz");
                            System.out.println("═══════════════════════════════════════\n");
                        } catch (IllegalArgumentException e) {
                            System.out.println("\nErro ao criar reserva: " + e.getMessage());
                            break;
                        }
                    }
                    break;
                case 2:
                    if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        return;
                    } else {
                        System.out.println("\nLISTA DE RESERVAS");
                        System.out.println("════════════════════════════════════════");
                        int reservaIndex = 1;
                        for (Reservation r : reservationService.getAllReservations()) {
                            System.out.println(formatarReserva(r, reservaIndex));
                            reservaIndex++;
                        }
                    }
                    break;
                case 3:
                    if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        break;
                    }
                    System.out.println("\nREALIZAR CHECK-IN");
                    System.out.println("────────────────────────────────────────");
                    int reservaIndex = 1;
                    for (Reservation r : reservationService.getAllReservations()) {
                        System.out.println(formatarReserva(r, reservaIndex));
                        reservaIndex++;
                    }
                    int reserva = 0;
                    boolean reservaValida = false;
                    while (!reservaValida) {
                        System.out.print("> Selecione o número da reserva: ");
                        try {
                            reserva = scanner.nextInt();
                            if (reserva <= 0 || reserva > reservationService.getAllReservations().size()) {
                                System.out.println("Erro: Número de reserva inválido!");
                            } else {
                                reservaValida = true;
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    Reservation reservation = reservationService.getAllReservations().get(reserva - 1);
                    try {
                        reservationService.checkIn(reservation);
                        System.out.println("Sucesso: Check-in realizado com sucesso!");
                    } catch (IllegalStateException e) {
                        System.out.println("Erro ao realizar check-in: " + e.getMessage());
                        break;
                    }
                    break;
                case 4:
                    if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        return;
                    }
                    System.out.println("\nREALIZAR CHECK-OUT");
                    System.out.println("────────────────────────────────────────");
                    reservaIndex = 1;
                    for (Reservation r : reservationService.getAllReservations()) {
                        System.out.println(formatarReserva(r, reservaIndex));
                        reservaIndex++;
                    }
                    int reserva2 = 0;
                    boolean reserva2Valida = false;
                    while (!reserva2Valida) {
                        System.out.print("> Selecione o número da reserva: ");
                        try {
                            reserva2 = scanner.nextInt();
                            if (reserva2 <= 0 || reserva2 > reservationService.getAllReservations().size()) {
                                System.out.println("Erro: Número de reserva inválido!");
                            } else {
                                reserva2Valida = true;
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    Reservation reservation2 = reservationService.getAllReservations().get(reserva2 - 1);
                    try {
                        reservationService.checkOut(reservation2);
                        System.out.println("Sucesso: Check-out realizado com sucesso!");
                    } catch (IllegalStateException e) {
                        System.out.println("Erro ao realizar check-out: " + e.getMessage());
                        break;
                    }
                    break;
                case 5:
                    System.out.println("\nCANCELAR RESERVA");
                    System.out.println("────────────────────────────────────────");
                    reservaIndex = 1;
                    for (Reservation r : reservationService.getAllReservations()) {
                        System.out.println(formatarReserva(r, reservaIndex));
                        reservaIndex++;
                    }
                    int reserva3 = 0;
                    boolean reserva3Valida = false;
                    while (!reserva3Valida) {
                        System.out.print("> Selecione o número da reserva: ");
                        try {
                            reserva3 = scanner.nextInt();
                            if (reserva3 <= 0 || reserva3 > reservationService.getAllReservations().size()) {
                                System.out.println("Erro: Número de reserva inválido!");
                            } else {
                                reserva3Valida = true;
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    Reservation reservation3 = reservationService.getAllReservations().get(reserva3 - 1);
                    try {
                        reservationService.cancelReservation(reservation3);
                        System.out.println("Sucesso: Reserva cancelada com sucesso!");
                    } catch (IllegalStateException e) {
                        System.out.println("Erro ao cancelar reserva: " + e.getMessage());
                        break;
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 5.");
            }
        }
    }

    private void menuQuartos() {
        while (true) {
            limparTela();
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GESTÃO DE QUARTOS               ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Adicionar Novo Quarto");
            System.out.println("  2. Listar Todos os Quartos");
            System.out.println("  3. Alterar Status do Quarto");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int opcao = 0;
            boolean entradaValidaQuartos = false;
            while (!entradaValidaQuartos) {
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();
                    entradaValidaQuartos = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                    scanner.nextLine();
                    System.out.print("> Escolha uma opção: ");
                }
            }

            switch (opcao) {
                case 1:
                    System.out.println("\nADICIONAR NOVO QUARTO");
                    System.out.println("────────────────────────────────────────");
                    System.out.println("Tipos de Quarto Disponíveis:");
                    System.out.println("  1. Standard");
                    System.out.println("  2. Deluxe");
                    System.out.println("  3. Suite");
                    int n = 0;
                    boolean tipoQuartoValido = false;
                    while (!tipoQuartoValido) {
                        System.out.print("> Selecione o tipo: ");
                        try {
                            n = scanner.nextInt();
                            if (n >= 1 && n <= 3) {
                                tipoQuartoValido = true;
                            } else {
                                System.out.println("Erro: Opção inválida! Escolha entre 1 e 3.");
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    switch (n) {
                        case 1:
                            hotel.addRoom(new Room(nquartos, RoomType.STANDARD, 25000.0, 2, RoomStatus.ACTIVE));
                            nquartos++;
                            System.out.println("Sucesso: Quarto Standard #" + (nquartos - 1) + " adicionado com sucesso!");
                            break;
                        case 2:
                            hotel.addRoom(new Room(nquartos, RoomType.DELUXE, 25000.0, 2, RoomStatus.ACTIVE));
                            nquartos++;
                            System.out.println("Sucesso: Quarto Deluxe #" + (nquartos - 1) + " adicionado com sucesso!");
                            break;
                        case 3:
                            hotel.addRoom(new Room(nquartos, RoomType.SUITE, 25000.0, 4, RoomStatus.ACTIVE));
                            nquartos++;
                            System.out.println("Sucesso: Quarto Suite #" + (nquartos - 1) + " adicionado com sucesso!");
                            break;
                        default:
                            System.out.println("Erro: Opção inválida! Escolha entre 1 e 3.");
                    }
                    break;
                case 2:
                    if (hotel.getRooms().isEmpty()) {
                        System.out.println("\nAtenção: Não há quartos cadastrados no hotel.");
                        return;
                    } else {
                        System.out.println("\nLISTA DE QUARTOS");
                        System.out.println("────────────────────────────────────────");
                        hotel.getRooms().forEach(System.out::println);
                    }
                    break;
                case 3:
                    if (hotel.getRooms().isEmpty()) {
                        System.out.println("\nAtenção: Não há quartos cadastrados para alterar.");
                        return;
                    } else {
                        System.out.println("\nALTERAR STATUS DO QUARTO");
                        System.out.println("────────────────────────────────────────");
                        boolean entradaValida = false;
                        int nquarto = 0;
                        int cont = 0;
                        while (!entradaValida) {
                            hotel.getRooms().forEach(System.out::println);
                            System.out.print("> Selecione o número do quarto: ");
                            try {
                                int id = scanner.nextInt();
                                if (id <= 0 || id > hotel.getRooms().size()) {
                                    System.out.println("Erro: Quarto não encontrado. Escolha um número válido da lista.");
                                    break;
                                } else {
                                    entradaValida = true;
                                    nquarto = id;
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            }
                            Room room1 = hotel.getRooms().get(nquarto - 1);
                            while (cont < reservationService.getAllReservations().size()) {
                                Reservation reservation = reservationService.getAllReservations().get(cont);
                                if (reservation.getRoom().equals(room1)) {
                                    System.out.println("Atenção: Este quarto possui reserva ativa. Não é possível alterar o status.");
                                    break;
                                } else {
                                    cont++;
                                }
                            }
                            System.out.println("\nStatus Disponíveis:");
                            System.out.println("  1. ATIVO");
                            System.out.println("  2. INATIVO");
                            System.out.println("  3. MANUTENÇÃO");
                            int status = 0;
                            boolean statusValido = false;
                            while (!statusValido) {
                                System.out.print("> Selecione o novo status: ");
                                try {
                                    status = scanner.nextInt();
                                    if (status >= 1 && status <= 3) {
                                        statusValido = true;
                                    } else {
                                        System.out.println("Erro: Status inválido! Escolha entre 1 e 3.");
                                    }
                                } catch (java.util.InputMismatchException e) {
                                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                    scanner.nextLine();
                                }
                            }
                            switch (status) {
                                case 1:
                                    room1.setStatus(RoomStatus.ACTIVE);
                                    System.out.println("Sucesso: Status alterado para ATIVO!");
                                    break;
                                case 2:
                                    room1.setStatus(RoomStatus.INACTIVE);
                                    System.out.println("Sucesso: Status alterado para INATIVO!");
                                    break;
                                case 3:
                                    room1.setStatus(RoomStatus.MAINTENANCE);
                                    System.out.println("Sucesso: Status alterado para MANUTENÇÃO!");
                                    break;
                                default:
                                    System.out.println("Erro: Status inválido! Escolha entre 1 e 3.");
                                    break;
                            }
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 3.");
            }
        }
    }

    private void menuClientes() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║      GESTÃO DE CLIENTES               ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Cadastrar Novo Cliente");
            System.out.println("  2. Listar Todos os Clientes");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("\nCADASTRO DE NOVO CLIENTE");
                    System.out.println("────────────────────────────────────────");
                    System.out.print("> Nome completo: ");
                    String nome = scanner.nextLine();
                    System.out.print("> Documento (BI/Passaporte): ");
                    String documento = scanner.nextLine();
                    System.out.print("> Email: ");
                    String email = scanner.nextLine();

                    boolean entradaValida = false;
                    String telefone = "";
                    while (!entradaValida) {
                        System.out.print("> Telefone: ");
                        try {
                            int telefone1 = scanner.nextInt();
                            entradaValida = true;
                            telefone = String.valueOf(telefone1);
                        } catch (RuntimeException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números.");
                            scanner.next();
                        }
                    }

                    String idcliente = String.valueOf(hotel.getClients().size() + 1);
                    Client client1 = new Client(idcliente, nome, documento, telefone, email);
                    hotel.getClients().add(client1);
                    System.out.println("Sucesso: Cliente cadastrado com sucesso! ID: " + idcliente);
                    break;
                case 2:
                    if (hotel.getClients().isEmpty()) {
                        System.out.println("\nAtenção: Não há clientes cadastrados no sistema.");
                        return;
                    } else {
                        System.out.println("\nLISTA DE CLIENTES");
                        System.out.println("────────────────────────────────────────");
                        hotel.getClients().forEach(System.out::println);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 2.");
            }
        }
    }

    private void menuServicos() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║   GESTÃO DE SERVIÇOS ADICIONAIS       ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Adicionar Serviço à Reserva");
            System.out.println("  2. Listar Serviços Disponíveis");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int opcao = 0;
            boolean entradaValidaServicos = false;
            while (!entradaValidaServicos) {
                try {
                    opcao = scanner.nextInt();
                    scanner.nextLine();
                    entradaValidaServicos = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                    scanner.nextLine();
                    System.out.print("> Escolha uma opção: ");
                }
            }

            switch (opcao) {
                case 1:
                    if (hotel.getRooms().isEmpty()) {
                        System.out.println("\nAtenção: Não há quartos cadastrados no hotel.");
                        return;
                    } else if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        return;
                    } else {
                        System.out.println("\nADICIONAR SERVIÇO À RESERVA");
                        System.out.println("────────────────────────────────────────");
                        reservationService.getAllReservations().forEach(System.out::println);
                        int reserva = 0;
                        boolean reservaServicosValida = false;
                        while (!reservaServicosValida) {
                            System.out.print("> Selecione o número da reserva: ");
                            try {
                                reserva = scanner.nextInt();
                                if (reserva <= 0 || reserva > reservationService.getAllReservations().size()) {
                                    System.out.println("Erro: Número de reserva inválido!");
                                } else {
                                    reservaServicosValida = true;
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            }
                        }
                        Reservation reservation1 = reservationService.getAllReservations().get(reserva - 1);
                        if (true) {
                            if (reservation1.getStatus() == ReservationStatus.CANCELLED) {
                                System.out.println("Atenção: Esta reserva foi cancelada e não pode ser modificada.");
                                break;
                            }
                            if (reservation1.getStatus() == ReservationStatus.CONFIRMED || reservation1.getStatus() == ReservationStatus.CHECKED_IN) {
                                System.out.println("Atenção: Esta reserva já está confirmada/em andamento e não pode adicionar serviços.");
                                break;
                            } else {
                                System.out.println("\nSERVIÇOS DISPONÍVEIS");
                                System.out.println("────────────────────────────────────────");
                                System.out.println("1. " + ServiceType.TRANSPORT + " - Transporte");
                                System.out.println("2. " + ServiceType.BREAKFAST + " - Café da Manhã");
                                System.out.println("3. " + ServiceType.PARKING + " - Estacionamento");
                                System.out.println("4. " + ServiceType.LAUNDRY + " - Lavandaria");
                                int servico = 0;
                                boolean servicoValido = false;
                                while (!servicoValido) {
                                    System.out.print("> Selecione o serviço: ");
                                    try {
                                        servico = scanner.nextInt();
                                        if (servico >= 1 && servico <= 4) {
                                            servicoValido = true;
                                        } else {
                                            System.out.println("Erro: Opção inválida! Escolha entre 1 e 4.");
                                        }
                                    } catch (java.util.InputMismatchException e) {
                                        System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                        scanner.nextLine();
                                    }
                                }
                                switch (servico) {
                                    case 1:
                                        reservation1.addService(new AdditionalService("Transporte", ServiceType.TRANSPORT, 20000.0, 1, BillingType.FIXED));
                                        System.out.println("Sucesso: Serviço de Transporte adicionado com sucesso!");
                                        break;
                                    case 2:
                                        reservation1.addService(new AdditionalService("Café da Manhã", ServiceType.BREAKFAST, 5000.0, 1, BillingType.PER_NIGHT));
                                        System.out.println("Sucesso: Serviço de Café da Manhã adicionado com sucesso!");
                                        break;
                                    case 3:
                                        reservation1.addService(new AdditionalService("Estacionamento", ServiceType.PARKING, 20000.0, 1, BillingType.FIXED));
                                        System.out.println("Sucesso: Serviço de Estacionamento adicionado com sucesso!");
                                        break;
                                    case 4:
                                        int unidades = 0;
                                        boolean unidadesValido = false;
                                        while (!unidadesValido) {
                                            System.out.print("> Quantas unidades deseja adicionar? ");
                                            try {
                                                unidades = scanner.nextInt();
                                                if (unidades > 0) {
                                                    unidadesValido = true;
                                                } else {
                                                    System.out.println("Erro: O número de unidades deve ser maior que 0.");
                                                }
                                            } catch (java.util.InputMismatchException e) {
                                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                                scanner.nextLine();
                                            }
                                        }
                                        reservation1.addService(new AdditionalService("Lavandaria", ServiceType.LAUNDRY, 2000.0, unidades, BillingType.PER_UNIT));
                                        System.out.println("Sucesso: Serviço de Lavandaria (" + unidades + " unidades) adicionado com sucesso!");
                                        break;
                                    default:
                                        System.out.println("Erro: Opção inválida! Escolha entre 1 e 4.");
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    System.out.println("\nCATÁLOGO DE SERVIÇOS DISPONÍVEIS");
                    System.out.println("────────────────────────────────────────");
                    System.out.println("1. " + ServiceType.TRANSPORT + " - Transporte");
                    System.out.println("2. " + ServiceType.BREAKFAST + " - Café da Manhã");
                    System.out.println("3. " + ServiceType.PARKING + " - Estacionamento");
                    System.out.println("4. " + ServiceType.LAUNDRY + " - Lavandaria");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 2.");
            }
        }
    }

    private void pagamentos() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║          PAGAMENTOS                   ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("  1. Pagamento Total");
            System.out.println("  2. Pagamento Parcial");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");
            System.out.print("> Escolha uma opção: ");

            int oopcao = 0;
            boolean entradaValidaPagamentos = false;
            while (!entradaValidaPagamentos) {
                try {
                    oopcao = scanner.nextInt();
                    scanner.nextLine();
                    entradaValidaPagamentos = true;
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                    scanner.nextLine();
                    System.out.print("> Escolha uma opção: ");
                }
            }

            switch (oopcao) {
                case 1:
                    if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        break;
                    }
                    System.out.println("\nPAGAMENTO TOTAL");
                    System.out.println("────────────────────────────────────────");
                    reservationService.getAllReservations().forEach(System.out::println);
                    int reserva = 0;
                    boolean reservaPagamentoValida = false;
                    while (!reservaPagamentoValida) {
                        System.out.print("> Selecione o número da reserva: ");
                        try {
                            reserva = scanner.nextInt();
                            if (reserva <= 0 || reserva > reservationService.getAllReservations().size()) {
                                System.out.println("Erro: Número de reserva inválido!");
                            } else {
                                reservaPagamentoValida = true;
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    Reservation reservation = reservationService.getAllReservations().get(reserva - 1);
                    if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                        System.out.println("Atenção: Esta reserva foi cancelada. Não é possível efetuar pagamento.");
                        break;
                    }
                    if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                        System.out.println("Atenção: Esta reserva já está totalmente paga.");
                        return;
                    } else {
                        System.out.println("\nRESUMO FINANCEIRO");
                        System.out.println("════════════════════════════════════════");
                        System.out.println("Valor Total: " + String.format("%.2f", pricingService.calculateReservationTotal(reservation)) + " kz");
                        System.out.println("Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation)) + " kz");
                        System.out.println("Saldo Restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                        System.out.println("════════════════════════════════════════");

                        System.out.println("\nMÉTODOS DE PAGAMENTO");
                        System.out.println("────────────────────────────────────────");
                        System.out.println("  1. Cartão de Crédito");
                        System.out.println("  2. Cartão de Débito");
                        System.out.println("  3. Dinheiro");
                        System.out.println("  4. Transferência Bancária");
                        int metodo = 0;
                        boolean metodoValido = false;
                        while (!metodoValido) {
                            System.out.print("> Selecione o método: ");
                            try {
                                metodo = scanner.nextInt();
                                if (metodo >= 1 && metodo <= 4) {
                                    metodoValido = true;
                                } else {
                                    System.out.println("Erro: Método de pagamento inválido! Escolha entre 1 e 4.");
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            }
                        }
                        switch (metodo) {
                            case 1:
                                Payment payment1 = new Payment(pricingService.calculateBalance(reservation), LocalDate.now(), PaymentMethod.CREDIT_CARD, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment1);
                                reservation.setStatus(ReservationStatus.CONFIRMED);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz efetuado com sucesso via Cartão de Crédito!");
                                System.out.println("Sucesso: Reserva confirmada!");
                                break;
                            case 2:
                                Payment payment2 = new Payment(pricingService.calculateBalance(reservation), LocalDate.now(), PaymentMethod.DEBIT_CARD, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment2);
                                reservation.setStatus(ReservationStatus.CONFIRMED);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz efetuado com sucesso via Cartão de Débito!");
                                System.out.println("Sucesso: Reserva confirmada!");
                                break;
                            case 3:
                                Payment payment3 = new Payment(
                                        pricingService.calculateBalance(reservation),
                                        LocalDate.now(),
                                        PaymentMethod.CASH,
                                        PaymentStatus.CONFIRMED
                                );
                                reservation.addPayment(payment3);
                                reservation.setStatus(ReservationStatus.CONFIRMED);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz efetuado com sucesso em Dinheiro!");
                                System.out.println("Sucesso: Reserva confirmada!");
                                break;
                            case 4:
                                Payment payment4 = new Payment(pricingService.calculateBalance(reservation), LocalDate.now(), PaymentMethod.BANK_TRANSFER, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment4);
                                reservation.setStatus(ReservationStatus.CONFIRMED);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz efetuado com sucesso via Transferência Bancária!");
                                System.out.println("Sucesso: Reserva confirmada!");
                                break;
                            default:
                                System.out.println("Erro: Método de pagamento inválido! Escolha entre 1 e 4.");
                        }
                    }
                    break;
                case 2:
                    if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
                        break;
                    }
                    System.out.println("\nPAGAMENTO PARCIAL");
                    System.out.println("────────────────────────────────────────");
                    reservationService.getAllReservations().forEach(System.out::println);
                    reserva = 0;
                    reservaPagamentoValida = false;
                    while (!reservaPagamentoValida) {
                        System.out.print("> Selecione o número da reserva: ");
                        try {
                            reserva = scanner.nextInt();
                            if (reserva <= 0 || reserva > reservationService.getAllReservations().size()) {
                                System.out.println("Erro: Número de reserva inválido!");
                            } else {
                                reservaPagamentoValida = true;
                            }
                        } catch (java.util.InputMismatchException e) {
                            System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                            scanner.nextLine();
                        }
                    }
                    reservation = reservationService.getAllReservations().get(reserva - 1);
                    if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                        System.out.println("Atenção: Esta reserva foi cancelada. Não é possível efetuar pagamento.");
                        break;
                    }
                    if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                        System.out.println("Atenção: Esta reserva já está totalmente paga.");
                        return;
                    } else {
                        System.out.println("\nRESUMO FINANCEIRO");
                        System.out.println("════════════════════════════════════════");
                        System.out.println("Valor Total: " + String.format("%.2f", pricingService.calculateReservationTotal(reservation)) + " kz");
                        System.out.println("Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation)) + " kz");
                        System.out.println("Saldo Restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                        System.out.println("════════════════════════════════════════");

                        System.out.println("\nMÉTODOS DE PAGAMENTO");
                        System.out.println("────────────────────────────────────────");
                        System.out.println("  1. Cartão de Crédito");
                        System.out.println("  2. Cartão de Débito");
                        System.out.println("  3. Dinheiro");
                        System.out.println("  4. Transferência Bancária");
                        int metodoParc = 0;
                        boolean metodoParciaisValido = false;
                        while (!metodoParciaisValido) {
                            System.out.print("> Selecione o método: ");
                            try {
                                metodoParc = scanner.nextInt();
                                if (metodoParc >= 1 && metodoParc <= 4) {
                                    metodoParciaisValido = true;
                                } else {
                                    System.out.println("Erro: Método de pagamento inválido! Escolha entre 1 e 4.");
                                }
                            } catch (java.util.InputMismatchException e) {
                                System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                scanner.nextLine();
                            }
                        }
                        switch (metodoParc) {
                            case 1:
                                int valor = 0;
                                boolean valorValido = false;
                                while (!valorValido) {
                                    System.out.print("> Digite o valor a ser pago: ");
                                    try {
                                        valor = scanner.nextInt();
                                        if (valor > 0) {
                                            valorValido = true;
                                        } else {
                                            System.out.println("Erro: O valor deve ser maior que 0.");
                                        }
                                    } catch (java.util.InputMismatchException e) {
                                        System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                        scanner.nextLine();
                                    }
                                }
                                Payment payment = new Payment(valor, LocalDate.now(), PaymentMethod.CREDIT_CARD, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", (double)valor) + " kz efetuado com sucesso via Cartão de Crédito!");
                                if (pricingService.calculateBalance(reservation) <= 0) {
                                    reservation.setStatus(ReservationStatus.CONFIRMED);
                                    System.out.println("Sucesso: Reserva totalmente paga e confirmada!");
                                } else {
                                    System.out.println("Saldo restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                                }
                                break;
                            case 2:
                                int valor2 = 0;
                                boolean valor2Valido = false;
                                while (!valor2Valido) {
                                    System.out.print("> Digite o valor a ser pago: ");
                                    try {
                                        valor2 = scanner.nextInt();
                                        if (valor2 > 0) {
                                            valor2Valido = true;
                                        } else {
                                            System.out.println("Erro: O valor deve ser maior que 0.");
                                        }
                                    } catch (java.util.InputMismatchException e) {
                                        System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                        scanner.nextLine();
                                    }
                                }
                                Payment payment2 = new Payment(valor2, LocalDate.now(), PaymentMethod.DEBIT_CARD, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment2);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", (double)valor2) + " kz efetuado com sucesso via Cartão de Débito!");
                                if (pricingService.calculateBalance(reservation) <= 0) {
                                    reservation.setStatus(ReservationStatus.CONFIRMED);
                                    System.out.println("Sucesso: Reserva totalmente paga e confirmada!");
                                } else {
                                    System.out.println("Saldo restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                                }
                                break;
                            case 3:
                                int valor3 = 0;
                                boolean valor3Valido = false;
                                while (!valor3Valido) {
                                    System.out.print("> Digite o valor a ser pago: ");
                                    try {
                                        valor3 = scanner.nextInt();
                                        if (valor3 > 0) {
                                            valor3Valido = true;
                                        } else {
                                            System.out.println("Erro: O valor deve ser maior que 0.");
                                        }
                                    } catch (java.util.InputMismatchException e) {
                                        System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                        scanner.nextLine();
                                    }
                                }
                                Payment payment3 = new Payment(valor3, LocalDate.now(), PaymentMethod.CASH, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment3);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", (double)valor3) + " kz efetuado com sucesso em Dinheiro!");
                                if (pricingService.calculateBalance(reservation) <= 0) {
                                    reservation.setStatus(ReservationStatus.CONFIRMED);
                                    System.out.println("Sucesso: Reserva totalmente paga e confirmada!");
                                } else {
                                    System.out.println("Saldo restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                                }
                                break;
                            case 4:
                                int valor4 = 0;
                                boolean valor4Valido = false;
                                while (!valor4Valido) {
                                    System.out.print("> Digite o valor a ser pago: ");
                                    try {
                                        valor4 = scanner.nextInt();
                                        if (valor4 > 0) {
                                            valor4Valido = true;
                                        } else {
                                            System.out.println("Erro: O valor deve ser maior que 0.");
                                        }
                                    } catch (java.util.InputMismatchException e) {
                                        System.out.println("Erro: Entrada inválida! Digite apenas números inteiros.");
                                        scanner.nextLine();
                                    }
                                }
                                Payment payment4 = new Payment(valor4, LocalDate.now(), PaymentMethod.BANK_TRANSFER, PaymentStatus.CONFIRMED);
                                reservation.addPayment(payment4);
                                System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", (double)valor4) + " kz efetuado com sucesso via Transferência Bancária!");
                                if (pricingService.calculateBalance(reservation) <= 0) {
                                    reservation.setStatus(ReservationStatus.CONFIRMED);
                                    System.out.println("Sucesso: Reserva totalmente paga e confirmada!");
                                } else {
                                    System.out.println("Saldo restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
                                }
                                break;
                            default:
                                System.out.println("Erro: Método de pagamento inválido! Escolha entre 1 e 4.");
                        }
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nOpção inválida! Por favor, escolha uma opção entre 0 e 2.");
            }
        }
    }
}