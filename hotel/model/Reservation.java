package hotel.model;

import hotel.model.enums.ReservationStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String reservationCode;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private ReservationStatus status;
    private int numberOfGuests;
    private LocalDate creationDate;
    private Client client;
    private Room room;
    private List<AdditionalService> services;
    private List<Payment> payments;

    public Reservation(LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests, 
                      LocalDate creationDate, Client client, Room room) {
        // Validate dates
        if (checkOutDate.compareTo(checkInDate) <= 0) {
            throw new IllegalArgumentException("checkOutDate must be strictly after checkInDate");

        }

        // Validate capacity
        if (numberOfGuests > room.getCapacity()) {
            throw new IllegalArgumentException("numberOfGuests cannot exceed room capacity");
        }

        this.reservationCode = UUID.randomUUID().toString();
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = ReservationStatus.CREATED;
        this.numberOfGuests = numberOfGuests;
        this.creationDate = creationDate;
        this.client = client;
        this.room = room;
        this.services = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Client getClient() {
        return client;
    }

    public Room getRoom() {
        return room;
    }

    public List<AdditionalService> getServices() {
        return new ArrayList<>(services);
    }

    public void addService(AdditionalService service) {
        this.services.add(service);
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }

    public int getNights() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public boolean overlaps(Reservation other) {
        // A.checkIn < B.checkOut AND B.checkIn < A.checkOut
        return this.checkInDate.isBefore(other.checkOutDate) && 
               other.checkInDate.isBefore(this.checkOutDate);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationCode='" + reservationCode + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status=" + status +
                ", numberOfGuests=" + numberOfGuests +
                ", creationDate=" + creationDate +
                ", client=" + client.getFullName() +
                ", room=" + room.getNumber() +
                ", services=" + services.size() +
                ", payments=" + payments.size() +
                '}';
    }
}
