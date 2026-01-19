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

    /**
     * Create and register a new reservation
     */
    public Reservation createReservation(Reservation reservation) {
        // Check availability
        if (!availabilityService.isRoomAvailable(reservation.getRoom(), reservation, reservations)) {
            throw new IllegalStateException("Room is not available for the requested dates");
        }

        // Add to reservations list
        reservations.add(reservation);
        return reservation;
    }

    /**
     * Transition from CREATED to CONFIRMED
     * Can confirm if balance <= 0 or manually confirmed
     */
    public void confirmReservation(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CREATED) {
            throw new IllegalStateException("Only CREATED reservations can be confirmed");
        }

        if (!pricingService.canConfirmReservation(reservation)) {
            throw new IllegalStateException("Cannot confirm: balance is positive (payment required)");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
    }

    /**
     * Transition to CHECKED_IN (from CONFIRMED)
     */
    public void checkIn(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED reservations can be checked in");
        }

        reservation.setStatus(ReservationStatus.CHECKED_IN);
    }

    /**
     * Transition to CHECKED_OUT (from CHECKED_IN)
     */
    public void checkOut(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
            throw new IllegalStateException("Only CHECKED_IN reservations can be checked out");
        }

        reservation.setStatus(ReservationStatus.CHECKED_OUT);
    }

    /**
     * Cancel reservation (only before CHECKED_IN)
     */
    public void cancelReservation(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CHECKED_IN || 
            reservation.getStatus() == ReservationStatus.CHECKED_OUT ||
            reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel after check-in or if already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
    }

    /**
     * Get all reservations
     */
    public java.util.List<Reservation> getAllReservations() {
        return new java.util.ArrayList<>(reservations);
    }

    /**
     * Get pricing service for calculations
     */
    public PricingService getPricingService() {
        return pricingService;
    }

    /**
     * Get availability service for checking availability
     */
    public AvailabilityService getAvailabilityService() {
        return availabilityService;
    }
}
