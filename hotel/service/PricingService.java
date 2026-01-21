package hotel.service;

import hotel.model.AdditionalService;
import hotel.model.Reservation;
import hotel.model.enums.PaymentStatus;

public class PricingService {

    public double calculateLodgingValue(Reservation reservation) {
        int nights = reservation.getNights();
        double dailyBasePrice = reservation.getRoom().getDailyBasePrice();
        double multiplier = reservation.getRoom().getType().getMultiplier();

        double subtotal = nights * dailyBasePrice;
        return subtotal * multiplier;
    }

    public double calculateServicesTotal(Reservation reservation) {
        double total = 0;
        for (AdditionalService service : reservation.getServices()) {
            total += service.calculateCharge(reservation.getNights());
        }
        return total;
    }

    public double calculateReservationTotal(Reservation reservation) {
        return calculateLodgingValue(reservation) + calculateServicesTotal(reservation);
    }

    public double calculateTotalPaid(Reservation reservation) {
        double total = 0;
        for (var payment : reservation.getPayments()) {
            if (payment.getStatus() == PaymentStatus.CONFIRMED) {
                total += payment.getAmountPaid();
            }
        }
        return total;
    }

    public double calculateBalance(Reservation reservation) {
        double reservationTotal = calculateReservationTotal(reservation);
        double totalPaid = calculateTotalPaid(reservation);
        return reservationTotal - totalPaid;
    }

    public boolean canConfirmReservation(Reservation reservation) {
        return calculateBalance(reservation) <= 0;
    }
}
