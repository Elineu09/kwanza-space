package hotel.app;

import hotel.model.*;
import hotel.model.enums.*;
import hotel.service.ReservationService;
import hotel.service.PricingService;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  KWANZA PALACE - Sistema de Reservas");
        System.out.println("========================================\n");

        // 1. Criar o hotel
        Hotel hotel = new Hotel("Kwanza Palace");
        System.out.println("Criado: " + hotel.getName());

        // 2. Criar e adicionar quartos
        Room room101 = new Room(101, RoomType.STANDARD, 100.0, 2, RoomStatus.ACTIVE);
        Room room102 = new Room(102, RoomType.DELUXE, 150.0, 2, RoomStatus.ACTIVE);
        Room room201 = new Room(201, RoomType.SUITE, 250.0, 4, RoomStatus.ACTIVE);

        hotel.addRoom(room101);
        hotel.addRoom(room102);
        hotel.addRoom(room201);

        System.out.println("Adicionados " + hotel.getRooms().size() + " quartos ao hotel\n");

        // 3. Criar um cliente
        Client client1 = new Client("C001", "John Doe", "DOC123456", "+1234567890", "john@example.com");
        System.out.println("Cliente Criado: " + client1.getFullName() + " (ID: " + client1.getId() + ")\n");

        // 4. Criar um serviço de reserva
        ReservationService reservationService = new ReservationService();
        PricingService pricingService = reservationService.getPricingService();

        // 5. CENÁRIO 1: Criar uma reserva válida
        System.out.println("--- CENÁRIO 1: Criando uma Reserva ---");
        LocalDate checkIn = LocalDate.of(2026, 1, 15);
        LocalDate checkOut = LocalDate.of(2026, 1, 18);
        
        Reservation reservation1 = new Reservation(
                checkIn,
                checkOut,
                2,  // Número de hóspedes
                LocalDate.now(),
                client1,
                room102
        );

        try {
            reservationService.createReservation(reservation1);
            System.out.println("Reserva criada: " + reservation1.getReservationCode());
            System.out.println("  Quarto: " + room102.getNumber() + " (" + room102.getType() + ")");
            System.out.println("  Entrada: " + checkIn + " | Saída: " + checkOut);
            System.out.println("  Noites: " + reservation1.getNights());
            System.out.println("  Status: " + reservation1.getStatus() + "\n");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }

        // 6. Adicionar serviços adicionais
        System.out.println("--- CENÁRIO 2: Adicionando Serviços Adicionais ---");
        AdditionalService breakfast = new AdditionalService(
                "Café da Manhã",
                ServiceType.BREAKFAST,
                20.0,
                1,
                BillingType.PER_NIGHT
        );
        
        AdditionalService parking = new AdditionalService(
                "Estacionamento",
                ServiceType.PARKING,
                15.0,
                1,
                BillingType.FIXED
        );

        reservation1.addService(breakfast);
        reservation1.addService(parking);
        System.out.println("Adicionados " + reservation1.getServices().size() + " serviços\n");

        // 7. Calcular preços
        System.out.println("--- CENÁRIO 3: Cálculo de Preços ---");
        double lodgingValue = pricingService.calculateLodgingValue(reservation1);
        double servicesTotal = pricingService.calculateServicesTotal(reservation1);
        double reservationTotal = pricingService.calculateReservationTotal(reservation1);
        double balance = pricingService.calculateBalance(reservation1);

        System.out.println("Cálculo da Hospedagem:");
        System.out.println("  Preço Base: " + room102.getDailyBasePrice() + " kz × " + reservation1.getNights() + " noites = " + 
                           (room102.getDailyBasePrice() * reservation1.getNights()) + " kz");
        System.out.println("  Multiplicador de Tipo de Quarto (" + room102.getType() + "): " + room102.getType().getMultiplier());
        System.out.println("  Valor da Hospedagem: " + String.format("%.2f", lodgingValue) + " kz");

        System.out.println("\nServiços Adicionais:");
        for (AdditionalService service : reservation1.getServices()) {
            double charge = service.calculateCharge(reservation1.getNights());
            System.out.println("  - " + service.getDescription() + " (" + service.getBillingType() + "): " + 
                             String.format("%.2f", charge) + " kz");
        }
        System.out.println("  Total de Serviços: " + String.format("%.2f", servicesTotal) + " kz");

        System.out.println("\nResumo da Reserva:");
        System.out.println("  Valor Total: " + String.format("%.2f", reservationTotal) + " kz");
        System.out.println("  Total Pago: " + String.format("%.2f", pricingService.calculateTotalPaid(reservation1)) + " kz");
        System.out.println("  Saldo: " + String.format("%.2f", balance) + " kz\n");

        // 8. Processar um pagamento
        System.out.println("--- CENÁRIO 4: Processamento de Pagamento ---");
        Payment payment1 = new Payment(
                150.0,
                LocalDate.now(),
                PaymentMethod.CREDIT_CARD,
                PaymentStatus.CONFIRMED
        );

        reservation1.addPayment(payment1);
        System.out.println("Pagamento registrado: " + payment1.getAmountPaid() + " kz");
        System.out.println("  Método: " + payment1.getMethod());
        System.out.println("  Status: " + payment1.getStatus());

        double newBalance = pricingService.calculateBalance(reservation1);
        System.out.println("  Novo Saldo: " + String.format("%.2f", newBalance) + " kz\n");

        // 9. Processar pagamento restante e confirmar
        System.out.println("--- CENÁRIO 5: Pagamento Final e Confirmação ---");
        Payment payment2 = new Payment(
                newBalance,
                LocalDate.now(),
                PaymentMethod.CASH,
                PaymentStatus.CONFIRMED
        );

        reservation1.addPayment(payment2);
        System.out.println("Pagamento final: " + String.format("%.2f", payment2.getAmountPaid()) + " kz");
        System.out.println("  Saldo Final: " + String.format("%.2f", pricingService.calculateBalance(reservation1)) + " kz\n");

        // Confirmar reserva
        try {
            reservationService.confirmReservation(reservation1);
            System.out.println("Reserva Confirmada!");
            System.out.println("  Status: " + reservation1.getStatus() + "\n");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }

        // 10. Entrada do hóspede
        System.out.println("--- CENÁRIO 6: Check-in ---");
        try {
            reservationService.checkIn(reservation1);
            System.out.println("Hóspede foi registrado com sucesso");
            System.out.println("  Status: " + reservation1.getStatus() + "\n");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }

        // 11. Saída do hóspede
        System.out.println("--- CENÁRIO 7: Check-out ---");
        try {
            reservationService.checkOut(reservation1);
            System.out.println("Hóspede foi registrado na saída com sucesso");
            System.out.println("  Status: " + reservation1.getStatus() + "\n");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage() + "\n");
        }

        // 12. TRATAMENTO DE ERROS: Tentar cenários inválidos
        System.out.println("--- CENÁRIO 8: Tratamento de Erros ---");

        // Erro 1: Datas inválidas (saída <= entrada)
        System.out.println("Teste 1 - Datas Inválidas (saída <= entrada):");
        try {
            Reservation invalidRes = new Reservation(
                    LocalDate.of(2026, 2, 20),
                    LocalDate.of(2026, 2, 20),  // Mesmo dia - inválido
                    1,
                    LocalDate.now(),
                    client1,
                    room101
            );
        } catch (IllegalArgumentException e) {
            System.out.println("  Erro Capturado: " + e.getMessage());
        }

        // Erro 2: Excedendo capacidade do quarto
        System.out.println("\nTeste 2 - Excedendo Capacidade do Quarto:");
        try {
            Reservation overCapacityRes = new Reservation(
                    LocalDate.of(2026, 2, 1),
                    LocalDate.of(2026, 2, 5),
                    5,  // Capacidade do quarto é 2
                    LocalDate.now(),
                    client1,
                    room101
            );
        } catch (IllegalArgumentException e) {
            System.out.println("  Erro Capturado: " + e.getMessage());
        }

        // Erro 3: Tentar confirmar uma reserva que ainda tem saldo
        System.out.println("\nTeste 3 - Confirmando Reserva com Saldo Pendente:");
        Client client2 = new Client("C002", "Jane Smith", "DOC789012", "+9876543210");
        Reservation res2 = new Reservation(
                LocalDate.of(2026, 2, 10),
                LocalDate.of(2026, 2, 12),
                1,
                LocalDate.now(),
                client2,
                room101
        );

        try {
            reservationService.createReservation(res2);
            // Não adicionar pagamento - apenas tentar confirmar
            reservationService.confirmReservation(res2);
        } catch (IllegalStateException e) {
            System.out.println("  Erro Capturado: " + e.getMessage());
        }

        // Erro 4: Tentar entrada sem confirmar
        System.out.println("\nTeste 4 - Check-in Sem Confirmação:");
        try {
            reservationService.checkIn(res2);
        } catch (IllegalStateException e) {
            System.out.println("  Erro Capturado: " + e.getMessage());
        }

        // Erro 5: Reservas sobrepostas
        System.out.println("\nTeste 5 - Reservas Sobrepostas (Mesmo Quarto, Datas Conflitantes):");
        Client client3 = new Client("C003", "Bob Wilson", "DOC345678", "+5555555555");
        
        // Criar uma reserva confirmada primeiro
        Reservation res3 = new Reservation(
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 3, 5),
                1,
                LocalDate.now(),
                client3,
                room102
        );

        try {
            reservationService.createReservation(res3);
            // Definir manualmente como CONFIRMADA para bloquear o quarto
            res3.setStatus(ReservationStatus.CONFIRMED);
            System.out.println("Primeira reserva CONFIRMADA");

            // Tentar criar uma reserva sobreposta
            Reservation overlappingRes = new Reservation(
                    LocalDate.of(2026, 3, 3),
                    LocalDate.of(2026, 3, 7),
                    1,
                    LocalDate.now(),
                    client3,
                    room102
            );

            reservationService.createReservation(overlappingRes);
            System.out.println("NÃO DEVERIA CHEGAR AQUI - reserva sobreposta criada!");
        } catch (IllegalStateException e) {
            System.out.println("  ✓ Erro Capturado: " + e.getMessage());
        }

        System.out.println("\n========================================");
        System.out.println("  Demonstração Completa");
        System.out.println("========================================");
    }
}
