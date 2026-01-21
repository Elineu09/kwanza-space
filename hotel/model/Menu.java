package hotel.model;

import hotel.model.*;
import hotel.model.enums.*;
import hotel.storage.StorageManager;
import hotel.service.PricingService;
import hotel.service.ReservationService;
import hotel.util.MenuUtil;
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
        
        // Tentar carregar dados persistidos
        Hotel hotelCarregado = StorageManager.loadHotel();
        if (hotelCarregado != null) {
            this.hotel = hotelCarregado;
            // ReservationService já está sincronizado com o hotel carregado
            this.reservationService = new ReservationService();
        } else {
            // Criar novo hotel se não houver dados persistidos
            this.hotel = new Hotel("Hotel Java");
            this.reservationService = new ReservationService();
        }
    }

    private void salvarDados() {
        StorageManager.saveHotel(hotel);
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
            MenuUtil.exibirTitulo("SISTEMA DE GESTÃO HOTELEIRA");
            System.out.println("  1. Gerenciar Reservas");
            System.out.println("  2. Gerenciar Quartos");
            System.out.println("  3. Gerenciar Clientes");
            System.out.println("  4. Gerenciar Serviços Adicionais");
            System.out.println("  5. Pagamentos");
            System.out.println("  0. Sair");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 5, scanner);

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
                    salvarDados();
                    return;
            }
        }
    }

    private void menuReservas() {
        while (true) {

            MenuUtil.exibirTitulo("GESTÃO DE RESERVAS");
            System.out.println("  1. Criar Nova Reserva");
            System.out.println("  2. Listar Todas as Reservas");
            System.out.println("  3. Realizar Check-in");
            System.out.println("  4. Realizar Check-out");
            System.out.println("  5. Cancelar Reserva");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 5, scanner);

            switch (opcao) {
                case 1:
                    criarNovaReserva();
                    break;
                case 2:
                    listarTodasReservas();
                    break;
                case 3:
                    realizarCheckIn();
                    break;
                case 4:
                    realizarCheckOut();
                    break;
                case 5:
                    cancelarReserva();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void criarNovaReserva() {
        if (hotel.getClients().isEmpty()) {
            System.out.println("\nAtenção: Não há clientes cadastrados! Por favor, cadastre um cliente primeiro.");
            return;
        } else if (hotel.getRooms().isEmpty()) {
            System.out.println("\nAtenção: Não há quartos disponíveis! Por favor, adicione quartos ao sistema.");
            return;
        }

        MenuUtil.exibirSubtitulo("SELEÇÃO DE CLIENTE");
        int clientIndex = 1;
        for (Client c : hotel.getClients()) {
            System.out.println(clientIndex + ". " + formatarCliente(c));
            clientIndex++;
        }
        int idCliente = MenuUtil.lerInteiroBounds("> Selecione o número do cliente: ", 1, hotel.getClients().size(), scanner);
        Client client = hotel.getClients().get(idCliente - 1);

        MenuUtil.exibirSubtitulo("SELEÇÃO DE QUARTO");
        hotel.getRooms().forEach(System.out::println);
        int idQuarto = MenuUtil.lerInteiroBounds("> Selecione o número do quarto: ", 1, hotel.getRooms().size(), scanner);
        Room room = hotel.getRooms().get(idQuarto - 1);

        int nh = MenuUtil.lerInteiroPositivo("\n> Número de hóspedes: ", scanner);

        MenuUtil.exibirSubtitulo("DATA DE CHECK-IN");
        LocalDate checkinDate = MenuUtil.lerData(2026, scanner);

        MenuUtil.exibirSubtitulo("DATA DE CHECK-OUT");
        LocalDate checkoutDate = MenuUtil.lerData(2026, scanner);

        LocalDate hoje = LocalDate.now();
        try {
            Reservation reservation = new Reservation(checkinDate, checkoutDate, nh, hoje, client, room);
            reservationService.createReservation(reservation);

            exibirDetalhesReservaCreated(reservation, room);
            salvarDados();
        } catch (IllegalArgumentException e) {
            System.out.println("\nErro ao criar reserva: " + e.getMessage());
        }
    }

    private void exibirDetalhesReservaCreated(Reservation reservation, Room room) {
        MenuUtil.exibirTitulo("RESERVA CRIADA COM SUCESSO");
        System.out.println("Código da Reserva: " + reservation.getReservationCode());
        System.out.println("Quarto: " + room.getNumber() + " (" + room.getType() + ")");
        System.out.println("Check-in: " + reservation.getCheckInDate() + " | Check-out: " + reservation.getCheckOutDate());
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

        MenuUtil.exibirSeparador();
        System.out.println("VALOR TOTAL DA RESERVA: " + String.format("%.2f", reservationTotal) + " kz");
        System.out.println("Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation)) + " kz");
        System.out.println("Saldo Restante: " + String.format("%.2f", balance) + " kz");
        MenuUtil.exibirSeparador();
    }

    private void listarTodasReservas() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        System.out.println("\nLISTA DE RESERVAS");
        System.out.println("════════════════════════════════════════");
        int reservaIndex = 1;
        for (Reservation r : reservationService.getAllReservations()) {
            System.out.println(formatarReserva(r, reservaIndex));
            reservaIndex++;
        }
    }

    private void realizarCheckIn() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        MenuUtil.exibirSubtitulo("REALIZAR CHECK-IN");
        int reservaIndex = 1;
        for (Reservation r : reservationService.getAllReservations()) {
            System.out.println(formatarReserva(r, reservaIndex));
            reservaIndex++;
        }
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);
        try {
            reservationService.checkIn(reservation);
            System.out.println("Sucesso: Check-in realizado com sucesso!");
            salvarDados();
        } catch (IllegalStateException e) {
            System.out.println("Erro ao realizar check-in: " + e.getMessage());
        }
    }

    private void realizarCheckOut() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        MenuUtil.exibirSubtitulo("REALIZAR CHECK-OUT");
        int reservaIndex = 1;
        for (Reservation r : reservationService.getAllReservations()) {
            System.out.println(formatarReserva(r, reservaIndex));
            reservaIndex++;
        }
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);
        try {
            reservationService.checkOut(reservation);
            System.out.println("Sucesso: Check-out realizado com sucesso!");
            salvarDados();
        } catch (IllegalStateException e) {
            System.out.println("Erro ao realizar check-out: " + e.getMessage());
        }
    }

    private void cancelarReserva() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        MenuUtil.exibirSubtitulo("CANCELAR RESERVA");
        int reservaIndex = 1;
        for (Reservation r : reservationService.getAllReservations()) {
            System.out.println(formatarReserva(r, reservaIndex));
            reservaIndex++;
        }
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);
        try {
            reservationService.cancelReservation(reservation);
            System.out.println("Sucesso: Reserva cancelada com sucesso!");
            salvarDados();
        } catch (IllegalStateException e) {
            System.out.println("Erro ao cancelar reserva: " + e.getMessage());
        }
    }

    private void menuQuartos() {
        while (true) {

            MenuUtil.exibirTitulo("GESTÃO DE QUARTOS");
            System.out.println("  1. Adicionar Novo Quarto");
            System.out.println("  2. Listar Todos os Quartos");
            System.out.println("  3. Alterar Status do Quarto");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 3, scanner);

            switch (opcao) {
                case 1:
                    adicionarNovoQuarto();
                    break;
                case 2:
                    listarTodosQuartos();
                    break;
                case 3:
                    alterarStatusQuarto();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void adicionarNovoQuarto() {
        MenuUtil.exibirSubtitulo("ADICIONAR NOVO QUARTO");
        System.out.println("Tipos de Quarto Disponíveis:");
        System.out.println("  1. Standard");
        System.out.println("  2. Deluxe");
        System.out.println("  3. Suite");
        int n = MenuUtil.lerInteiroBounds("> Selecione o tipo: ", 1, 3, scanner);
        
        RoomType tipo = null;
        double preco = 25000.0;
        int capacidade = 2;
        
        switch (n) {
            case 1:
                tipo = RoomType.STANDARD;
                capacidade = 2;
                hotel.addRoom(new Room(nquartos, tipo, preco, capacidade, RoomStatus.ACTIVE));
                System.out.println("Sucesso: Quarto Standard #" + nquartos + " adicionado com sucesso!");
                nquartos++;
                salvarDados();
                break;
            case 2:
                tipo = RoomType.DELUXE;
                capacidade = 2;
                hotel.addRoom(new Room(nquartos, tipo, preco, capacidade, RoomStatus.ACTIVE));
                System.out.println("Sucesso: Quarto Deluxe #" + nquartos + " adicionado com sucesso!");
                nquartos++;
                salvarDados();
                break;
            case 3:
                tipo = RoomType.SUITE;
                capacidade = 4;
                hotel.addRoom(new Room(nquartos, tipo, preco, capacidade, RoomStatus.ACTIVE));
                System.out.println("Sucesso: Quarto Suite #" + nquartos + " adicionado com sucesso!");
                nquartos++;
                salvarDados();
                break;
        }
    }

    private void listarTodosQuartos() {
        if (hotel.getRooms().isEmpty()) {
            System.out.println("\nAtenção: Não há quartos cadastrados no hotel.");
            return;
        }
        System.out.println("\nLISTA DE QUARTOS");
        System.out.println("────────────────────────────────────────");
        hotel.getRooms().forEach(System.out::println);
    }

    private void alterarStatusQuarto() {
        if (hotel.getRooms().isEmpty()) {
            System.out.println("\nAtenção: Não há quartos cadastrados para alterar.");
            return;
        }
        MenuUtil.exibirSubtitulo("ALTERAR STATUS DO QUARTO");
        hotel.getRooms().forEach(System.out::println);
        int idQuarto = MenuUtil.lerInteiroBounds("> Selecione o número do quarto: ", 1, hotel.getRooms().size(), scanner);
        
        Room room1 = hotel.getRooms().get(idQuarto - 1);
        
        // Verificar se quarto tem reservas ativas
        for (Reservation reservation : reservationService.getAllReservations()) {
            if (reservation.getRoom().equals(room1) && 
                (reservation.getStatus() == ReservationStatus.CONFIRMED || 
                 reservation.getStatus() == ReservationStatus.CHECKED_IN)) {
                System.out.println("Atenção: Este quarto possui reserva ativa. Não é possível alterar o status.");
                return;
            }
        }
        
        System.out.println("\nStatus Disponíveis:");
        System.out.println("  1. ATIVO");
        System.out.println("  2. INATIVO");
        System.out.println("  3. MANUTENÇÃO");
        int status = MenuUtil.lerInteiroBounds("> Selecione o novo status: ", 1, 3, scanner);
        
        switch (status) {
            case 1:
                room1.setStatus(RoomStatus.ACTIVE);
                System.out.println("Sucesso: Status alterado para ATIVO!");
                salvarDados();
                break;
            case 2:
                room1.setStatus(RoomStatus.INACTIVE);
                System.out.println("Sucesso: Status alterado para INATIVO!");
                salvarDados();
                break;
            case 3:
                room1.setStatus(RoomStatus.MAINTENANCE);
                System.out.println("Sucesso: Status alterado para MANUTENÇÃO!");
                salvarDados();
                break;
        }
    }

    private void menuClientes() {
        while (true) {

            MenuUtil.exibirTitulo("GESTÃO DE CLIENTES");
            System.out.println("  1. Cadastrar Novo Cliente");
            System.out.println("  2. Listar Todos os Clientes");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 2, scanner);

            switch (opcao) {
                case 1:
                    cadastrarNovoCliente();
                    break;
                case 2:
                    listarTodosClientes();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void cadastrarNovoCliente() {
        MenuUtil.exibirSubtitulo("CADASTRO DE NOVO CLIENTE");
        System.out.print("> Nome completo: ");
        String nome = scanner.nextLine();
        System.out.print("> Documento (BI/Passaporte): ");
        String documento = scanner.nextLine();
        System.out.print("> Email: ");
        String email = scanner.nextLine();
        int telefone = MenuUtil.lerInteiroPositivo("> Telefone: ", scanner);

        String idcliente = String.valueOf(hotel.getClients().size() + 1);
        Client client1 = new Client(idcliente, nome, documento, String.valueOf(telefone), email);
        hotel.getClients().add(client1);
        System.out.println("Sucesso: Cliente cadastrado com sucesso! ID: " + idcliente);
        salvarDados();
    }

    private void listarTodosClientes() {
        if (hotel.getClients().isEmpty()) {
            System.out.println("\nAtenção: Não há clientes cadastrados no sistema.");
            return;
        }
        System.out.println("\nLISTA DE CLIENTES");
        System.out.println("────────────────────────────────────────");
        hotel.getClients().forEach(System.out::println);
    }

    private void menuServicos() {
        while (true) {
            MenuUtil.exibirTitulo("GESTÃO DE SERVIÇOS ADICIONAIS");
            System.out.println("  1. Adicionar Serviço à Reserva");
            System.out.println("  2. Listar Serviços Disponíveis");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 2, scanner);

            switch (opcao) {
                case 1:
                    adicionarServicoReserva();
                    break;
                case 2:
                    listarServicosDisponiveis();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void adicionarServicoReserva() {
        if (hotel.getRooms().isEmpty()) {
            System.out.println("\nAtenção: Não há quartos cadastrados no hotel.");
            return;
        }
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        System.out.println("\nADICIONAR SERVIÇO À RESERVA");
        System.out.println("────────────────────────────────────────");
        reservationService.getAllReservations().forEach(System.out::println);
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Atenção: Esta reserva foi cancelada e não pode ser modificada.");
            return;
        }
        if (reservation.getStatus() == ReservationStatus.CONFIRMED || reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            System.out.println("Atenção: Esta reserva já está confirmada/em andamento e não pode adicionar serviços.");
            return;
        }

        System.out.println("\nSERVIÇOS DISPONÍVEIS");
        System.out.println("────────────────────────────────────────");
        System.out.println("1. " + ServiceType.TRANSPORT + " - Transporte");
        System.out.println("2. " + ServiceType.BREAKFAST + " - Café da Manhã");
        System.out.println("3. " + ServiceType.PARKING + " - Estacionamento");
        System.out.println("4. " + ServiceType.LAUNDRY + " - Lavandaria");
        int servico = MenuUtil.lerInteiroBounds("> Selecione o serviço: ", 1, 4, scanner);

        switch (servico) {
            case 1:
                reservation.addService(new AdditionalService("Transporte", ServiceType.TRANSPORT, 20000.0, 1, BillingType.FIXED));
                System.out.println("Sucesso: Serviço de Transporte adicionado com sucesso!");
                salvarDados();
                break;
            case 2:
                reservation.addService(new AdditionalService("Café da Manhã", ServiceType.BREAKFAST, 5000.0, 1, BillingType.PER_NIGHT));
                System.out.println("Sucesso: Serviço de Café da Manhã adicionado com sucesso!");
                salvarDados();
                break;
            case 3:
                reservation.addService(new AdditionalService("Estacionamento", ServiceType.PARKING, 20000.0, 1, BillingType.FIXED));
                System.out.println("Sucesso: Serviço de Estacionamento adicionado com sucesso!");
                salvarDados();
                break;
            case 4:
                int unidades = MenuUtil.lerInteiroPositivo("> Quantas unidades deseja adicionar? ", scanner);
                reservation.addService(new AdditionalService("Lavandaria", ServiceType.LAUNDRY, 2000.0, unidades, BillingType.PER_UNIT));
                System.out.println("Sucesso: Serviço de Lavandaria (" + unidades + " unidades) adicionado com sucesso!");
                salvarDados();
                break;
        }
    }

    private void listarServicosDisponiveis() {
        System.out.println("\nCATÁLOGO DE SERVIÇOS DISPONÍVEIS");
        System.out.println("────────────────────────────────────────");
        System.out.println("1. " + ServiceType.TRANSPORT + " - Transporte");
        System.out.println("2. " + ServiceType.BREAKFAST + " - Café da Manhã");
        System.out.println("3. " + ServiceType.PARKING + " - Estacionamento");
        System.out.println("4. " + ServiceType.LAUNDRY + " - Lavandaria");
    }

    private void pagamentos() {
        while (true) {
            MenuUtil.exibirTitulo("PAGAMENTOS");
            System.out.println("  1. Pagamento Total");
            System.out.println("  2. Pagamento Parcial");
            System.out.println("  0. Voltar ao Menu Principal");
            System.out.println("────────────────────────────────────────");

            int opcao = MenuUtil.lerInteiroBounds("> Escolha uma opção: ", 0, 2, scanner);

            switch (opcao) {
                case 1:
                    pagamentoTotal();
                    break;
                case 2:
                    pagamentoParcial();
                    break;
                case 0:
                    return;
            }
        }
    }

    private void pagamentoTotal() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        System.out.println("\nPAGAMENTO TOTAL");
        System.out.println("────────────────────────────────────────");
        reservationService.getAllReservations().forEach(System.out::println);
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Atenção: Esta reserva foi cancelada. Não é possível efetuar pagamento.");
            return;
        }
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            System.out.println("Atenção: Esta reserva já está totalmente paga.");
            return;
        }

        exibirResumoFinanceiro(reservation);
        int metodo = selecionarMetodoPagamento();
        procesarPagamentoTotal(reservation, metodo);
    }

    private void pagamentoParcial() {
        if (reservationService.getAllReservations().isEmpty()) {
            System.out.println("\nAtenção: Não há reservas cadastradas no sistema.");
            return;
        }
        System.out.println("\nPAGAMENTO PARCIAL");
        System.out.println("────────────────────────────────────────");
        reservationService.getAllReservations().forEach(System.out::println);
        int reserva = MenuUtil.lerInteiroBounds("> Selecione o número da reserva: ", 1, reservationService.getAllReservations().size(), scanner);
        Reservation reservation = reservationService.getAllReservations().get(reserva - 1);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Atenção: Esta reserva foi cancelada. Não é possível efetuar pagamento.");
            return;
        }
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            System.out.println("Atenção: Esta reserva já está totalmente paga.");
            return;
        }

        exibirResumoFinanceiro(reservation);
        int metodo = selecionarMetodoPagamento();
        int valor = MenuUtil.lerInteiroPositivo("> Digite o valor a ser pago: ", scanner);
        procesarPagamentoParcial(reservation, metodo, valor);
    }

    private void exibirResumoFinanceiro(Reservation reservation) {
        System.out.println("\nRESUMO FINANCEIRO");
        System.out.println("════════════════════════════════════════");
        System.out.println("Valor Total: " + String.format("%.2f", pricingService.calculateReservationTotal(reservation)) + " kz");
        System.out.println("Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation)) + " kz");
        System.out.println("Saldo Restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
        System.out.println("════════════════════════════════════════");
    }

    private int selecionarMetodoPagamento() {
        System.out.println("\nMÉTODOS DE PAGAMENTO");
        System.out.println("────────────────────────────────────────");
        System.out.println("  1. Cartão de Crédito");
        System.out.println("  2. Cartão de Débito");
        System.out.println("  3. Dinheiro");
        System.out.println("  4. Transferência Bancária");
        return MenuUtil.lerInteiroBounds("> Selecione o método: ", 1, 4, scanner);
    }

    private void procesarPagamentoTotal(Reservation reservation, int metodo) {
        double valor = pricingService.calculateBalance(reservation);
        PaymentMethod paymentMethod = obterMetodoPagamento(metodo);
        Payment payment = new Payment(valor, LocalDate.now(), paymentMethod, PaymentStatus.CONFIRMED);
        reservation.addPayment(payment);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", valor) + " kz efetuado com sucesso!");
        System.out.println("Sucesso: Reserva confirmada!");
        salvarDados();
    }

    private void procesarPagamentoParcial(Reservation reservation, int metodo, int valor) {
        PaymentMethod paymentMethod = obterMetodoPagamento(metodo);
        Payment payment = new Payment(valor, LocalDate.now(), paymentMethod, PaymentStatus.CONFIRMED);
        reservation.addPayment(payment);
        System.out.println("\nSucesso: Pagamento de " + String.format("%.2f", (double)valor) + " kz efetuado com sucesso!");
        if (pricingService.calculateBalance(reservation) <= 0) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            System.out.println("Sucesso: Reserva totalmente paga e confirmada!");
        } else {
            System.out.println("Saldo restante: " + String.format("%.2f", pricingService.calculateBalance(reservation)) + " kz");
        }
        salvarDados();
    }

    private PaymentMethod obterMetodoPagamento(int metodo) {
        return switch (metodo) {
            case 1 -> PaymentMethod.CREDIT_CARD;
            case 2 -> PaymentMethod.DEBIT_CARD;
            case 3 -> PaymentMethod.CASH;
            case 4 -> PaymentMethod.BANK_TRANSFER;
            default -> PaymentMethod.CREDIT_CARD;
        };
    }
}