package hotel.service;

import hotel.model.Reservation;
import hotel.model.enums.ReservationStatus;

public class ReservationService {
    private AvailabilityService availabilityService;
    private PricingService pricingService;
    private java.util.List<Reservation> reservations;

    public ReservationService() {
        this.availabilityService = new AvailabilityService();
        this.pricingService = new PricingService();
        this.reservations = new java.util.ArrayList<>();
    }

    public Reservation createReservation(Reservation reservation) {
        if (!availabilityService.isRoomAvailable(reservation.getRoom(), reservation, reservations)) {
            throw new IllegalStateException("Quarto não está disponível para as datas solicitadas");
        }

        reservations.add(reservation);
        return reservation;
    }

    public void confirmReservation(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CREATED) {
            throw new IllegalStateException("Apenas reservas com status CRIADA podem ser confirmadas");
        }

        if (!pricingService.canConfirmReservation(reservation)) {
            throw new IllegalStateException("Não é possível confirmar: pagamento pendente");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    public void checkIn(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Apenas reservas CONFIRMADAS podem fazer check-in");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
    }

    public void checkOut(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Apenas reservas em CHECK-IN podem fazer check-out");
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
    }

    public void cancelReservation(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CHECKED_IN || 
            reservation.getStatus() == ReservationStatus.CHECKED_OUT ||
            reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Não é possível cancelar após check-in ou se já foi cancelada");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    public java.util.List<Reservation> getAllReservations() {
        return new java.util.ArrayList<>(reservations);
    }

    public PricingService getPricingService() {
        return pricingService;
    }

    public AvailabilityService getAvailabilityService() {
        return availabilityService;
    }
}
