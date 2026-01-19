package hotel.model;

import hotel.model.*;
import hotel.model.enums.*;
import hotel.service.PricingService;
import hotel.service.ReservationService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.List;

public class Menu {
    private Scanner scanner;
    private Hotel hotel;
    private ReservationService reservationService=new ReservationService();
    private DateTimeFormatter dateFormatter;
    private PricingService pricingService = reservationService.getPricingService();
    private int nquartos=101;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.hotel = new Hotel("Hotel Java");
        this.reservationService = new ReservationService();
        Room room101 = new Room(this.nquartos, RoomType.STANDARD, 25000.0, 2, RoomStatus.ACTIVE);
        nquartos++;
        Room room102 = new Room(this.nquartos, RoomType.DELUXE, 25000.0, 2, RoomStatus.ACTIVE);
        nquartos++;
        Room room103 = new Room(this.nquartos, RoomType.SUITE, 25000.0, 4, RoomStatus.ACTIVE);
        nquartos++;
        hotel.addRoom(room101);
        hotel.addRoom(room102);
        hotel.addRoom(room103);
    }

    public void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== Sistema de Gestão Hoteleira ===");
            System.out.println("1. Gerenciar Reservas");
            System.out.println("2. Gerenciar Quartos");
            System.out.println("3. Gerenciar Clientes");
            System.out.println("4. Gerenciar Serviços Adicionais");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar o buffer

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
                case 0:
                    System.out.println("Encerrando o sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuReservas() {
        while (true) {
            System.out.println("\n=== Gestão de Reservas ===");
            System.out.println("1. Nova Reserva");
            System.out.println("2. Listar Reservas");
            System.out.println("3. Confirmar Reserva");
            System.out.println("4. Realizar Check-in");
            System.out.println("5. Realizar Check-out");
            System.out.println("6. Cancelar Reserva");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (hotel.getClients().isEmpty()) {
                       System.out.println("Não existem clientes para fazer uma nova reserva!");
                       return;
                    }else if (hotel.getRooms().isEmpty()) {
                        System.out.println("Não existem quartos para fazer uma nova reserva");
                        return;
                    }else{
                        int id=0;
                        System.out.println("Escolha o cliente (1,2,3...)");
                        boolean entradaValida = false;
                        while (!entradaValida) {
                            hotel.getClients().forEach(System.out::println);
                             try {
                                 id=scanner.nextInt();
                               if (id==0 || id>hotel.getClients().size()) {
                                    System.out.println("O cliente não existe. Por favor, tente novamente.");
                                }else {
                                    entradaValida = true;
                                }
                            } catch (RuntimeException e) {
                                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números inteiros.");
                                scanner.next();
                            }
                        }
                        Client client=hotel.getClients().get(id-1);
                        System.out.println("Escolha o cliente (1,2,3...)");
                         entradaValida = false;
                        while (!entradaValida) {
                            hotel.getRooms().forEach(System.out::println);
                            try {
                                id=scanner.nextInt();
                                if (id==0 || id>hotel.getRooms().size()) {
                                    System.out.println("O quarto não existe. Por favor, tente novamente.");
                                }else {
                                    entradaValida = true;
                                }
                            } catch (RuntimeException e) {
                                System.out.println("Erro: Entrada inválida. Por favor, digite apenas números inteiros.");
                                scanner.next();
                            }
                        }
                        Room room=hotel.getRooms().get(id-1);
                        System.out.println("Digite O numero de hospedes");
                        int nh =scanner.nextInt();
                        System.out.println("Digite o dia do chekin");
                        int dia=scanner.nextInt();
                        System.out.println("Digite o mes do chekin");
                        int mes=scanner.nextInt();
                        System.out.println("Digite o ano do chekin");
                        int ano=scanner.nextInt();
                        LocalDate checkinDate=LocalDate.of(ano, mes, dia);
                        System.out.println("Digite o dia do checkout");
                        dia=scanner.nextInt();
                        System.out.println("Digite o mes do checkout");
                        mes=scanner.nextInt();
                        System.out.println("Digite o ano do checkout");
                        ano=scanner.nextInt();
                        LocalDate checkoutDate=LocalDate.of(ano, mes, dia);
                        LocalDate hoje=LocalDate.now();
                        Reservation reservation = new Reservation(checkinDate,checkoutDate,nh,hoje,client,room);
                        try {
                            reservationService.createReservation(reservation);
                            System.out.println("Reserva criada: " + reservation.getReservationCode());
                            System.out.println("  Quarto: " + room.getNumber() + " (" + room.getType() + ")");
                            System.out.println("  Entrada: " + checkinDate + " | Saída: " + checkoutDate);
                            System.out.println("  Noites: " + reservation.getNights());
                            System.out.println("  Status: " + reservation.getStatus() + "\n");
                        } catch (Exception e) {
                            System.out.println("Erro: " + e.getMessage() + "\n");
                        }
                    }
                    break;
                case 2:
                    if(reservationService.getAllReservations().isEmpty()){
                        System.out.println("Não existem reservas para listar!");
                        return;
                    }else{
                    reservationService.getAllReservations().forEach(System.out::println);

                    }
                    break;
                case 3:
                    //confirmarReserva();
                    // podemos procurar pelo id da reserva ou pelo quarto, o mais seguro seria pelo id da reserva
                    break;
                case 4:
                   // realizarCheckIn();
                    // podemos procurar pelo id da reserva ou pelo quarto, o mais seguro seria pelo id da reserva
                    break;
                case 5:
                   // realizarCheckOut();
                    // podemos procurar pelo id da reserva ou pelo quarto, o mais seguro seria pelo id da reserva
                    break;
                case 6:
                    //cancelarReserva();
                    // podemos procurar pelo id da reserva ou pelo quarto, o mais seguro seria pelo id da reserva
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuQuartos() {
        while (true) {
            System.out.println("\n=== Gestão de Quartos ===");
            System.out.println("1. Adicionar Quarto");
            System.out.println("2. Listar Quartos");
            System.out.println("3. Alterar Status do Quarto");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    int n;
                    System.out.println("Escolha o tipo de quarto a ser criado");
                    System.out.println("1-Standard");
                    System.out.println("2-Deluxe");
                    System.out.println("3-Suite");
                    n=scanner.nextInt();
                    switch (n){
                        case 1:
                         hotel.addRoom(new Room(nquartos, RoomType.STANDARD, 25000.0, 2, RoomStatus.ACTIVE));
                         nquartos++;
                         break;
                        case 2:
                         hotel.addRoom(new Room(nquartos, RoomType.DELUXE, 25000.0, 2, RoomStatus.ACTIVE));
                         nquartos++;
                         break;
                        case 3:
                         hotel.addRoom(new Room(nquartos, RoomType.SUITE, 25000.0, 4, RoomStatus.ACTIVE));
                         nquartos++;
                         break;
                        default:
                         System.out.println("Opção inválida!");
                    }
                    break;
                case 2:
                    if(hotel.getRooms().isEmpty()){
                        System.out.println("O hotel não possui quartos!");
                        return;
                    }else{
                      hotel.getRooms().forEach(System.out::println);
                    }
                    break;
                case 3:
                    if (hotel.getRooms().isEmpty()) {
                        System.out.println("Não existem quartos para alterar o status!");
                        return;
                    }else {
                        //pedir o numero do quarto
                        //procurar o quarto
                        //verificar se tem uma reserva confirmada ou no chek in, se tiver comparar os quartos
                        // se nao for no quarto pretendido mudamos o status, se for nao mudamos
                    }
                    //alterarStatusQuarto();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuClientes() {
        while (true) {
            System.out.println("\n=== Gestão de Clientes ===");
            System.out.println("1. Cadastrar Cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o nome do cliente");
                    String nome=scanner.nextLine();
                    System.out.println("Digite o documento do cliente");
                    String documento=scanner.nextLine();
                    boolean entradaValida = false;
                    String telefone = "";
                    System.out.println("Digite o email do cliente");
                    String email=scanner.nextLine();
                    while (!entradaValida) {
                        System.out.println("Digite o telefone do cliente");
                        try {
                           int telefone1=scanner.nextInt();
                            entradaValida = true;
                            telefone=String.valueOf(telefone1);
                        } catch (RuntimeException e) {
                            System.out.println("Erro: Entrada inválida. Por favor, digite apenas números inteiros.");
                            scanner.next();
                        }
                    }
                    String idcliente=String.valueOf(hotel.getClients().size()+1);
                    Client client1 = new Client(idcliente , nome, documento, telefone, email);
                    hotel.getClients().add(client1);
                    break;
                case 2:
                    if (hotel.getClients().isEmpty()) {
                        System.out.println("O hotel não possui clientes cadastrados!");
                        return;
                    }else{
                        hotel.getClients().forEach(System.out::println);
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuServicos() {
        while (true) {
            System.out.println("\n=== Gestão de Serviços Adicionais ===");
            System.out.println("1. Adicionar Serviço à Reserva");
            System.out.println("2. Listar Serviços dispoiveis");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    if (hotel.getRooms().isEmpty()) {
                        System.out.println("Não exixtem quartos no hotel");
                        return;
                    }else if (reservationService.getAllReservations().isEmpty()) {
                        System.out.println("Não existem reservas no hotel");
                        return;
                    }else {
                        System.out.println("Selecione a reserva (1,2,3...)");
                        reservationService.getAllReservations().forEach(System.out::println);
                        int reserva=scanner.nextInt();
                        if (reserva==0 || reserva>reservationService.getAllReservations().size()) {
                            System.out.println("Valor invalido");
                        }else {
                            Reservation reservation1=reservationService.getAllReservations().get(reserva-1);
                            if (reservation1.getStatus()==ReservationStatus.CONFIRMED || reservation1.getStatus()==ReservationStatus.CHECKED_IN) {
                                System.out.println("A reserva já não pode ser alterada");
                            }else {
                                System.out.println("Selecione o serviço (1,2,3...)");
                                System.out.println(ServiceType.TRANSPORT);
                                System.out.println(ServiceType.BREAKFAST);
                                System.out.println(ServiceType.PARKING);
                                System.out.println(ServiceType.LAUNDRY);
                                int servico=scanner.nextInt();
                                switch (servico) {
                                    case 1:
                                        reservation1.addService(new AdditionalService("Transporte", ServiceType.TRANSPORT, 20000.0, 1, BillingType.FIXED));
                                        break;
                                    case 2:
                                        reservation1.addService(new AdditionalService("Café da Manhã", ServiceType.BREAKFAST, 5000.0, 1, BillingType.PER_NIGHT));
                                        break;
                                    case 3:
                                        reservation1.addService(new AdditionalService("ESTACIONAMENTO", ServiceType.PARKING, 20000.0, 1, BillingType.FIXED));
                                        break;
                                    case 4:
                                        System.out.println("Quantas unidades deseja adcionar?");
                                        int unidades=scanner.nextInt();
                                        reservation1.addService(new AdditionalService("LAVANDARIA", ServiceType.LAUNDRY, 2000.0, unidades, BillingType.PER_UNIT));
                                        break;
                                    default:
                                        System.out.println("Opção invalida");
                                }
                            }
                        }
                    }
                    break;
                case 2:
                    System.out.println(ServiceType.TRANSPORT);
                    System.out.println(ServiceType.BREAKFAST);
                    System.out.println(ServiceType.PARKING);
                    System.out.println(ServiceType.LAUNDRY);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    // Métodos auxiliares seriam implementados aqui:
    // - criarNovaReserva()
    // - listarReservas()
    // - confirmarReserva()
    // - realizarCheckIn()
    // - realizarCheckOut()
    // - cancelarReserva()
    // - adicionarQuarto()
    // - listarQuartos()
    // - alterarStatusQuarto()
    // - cadastrarCliente()
    // - listarClientes()
    // - adicionarServicoReserva()
    // - listarServicosReserva()


}