package hotel.service;

import hotel.model.Reservation;
import hotel.model.Room;
import hotel.model.enums.RoomStatus;
import hotel.model.enums.ReservationStatus;
import java.util.List;

public class AvailabilityService {

    public boolean isRoomAvailable(Room room, Reservation newReservation, List<Reservation> allReservations) {

        if (room.getStatus() != RoomStatus.ACTIVE) {
            return false;
        }

        for (Reservation existingReservation : allReservations) {
            if (existingReservation.getStatus() == ReservationStatus.CONFIRMED ||
                existingReservation.getStatus() == ReservationStatus.CHECKED_IN) {
                
                if (existingReservation.getRoom().getNumber() == room.getNumber()) {
                    if (newReservation.overlaps(existingReservation)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
