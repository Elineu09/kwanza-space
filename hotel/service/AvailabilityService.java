package hotel.service;

import hotel.model.Reservation;
import hotel.model.Room;
import hotel.model.enums.RoomStatus;
import hotel.model.enums.ReservationStatus;
import java.util.List;

public class AvailabilityService {

    /**
     * Check if a room is available for the given reservation.
     * A room is available if:
     * 1. The room status is ACTIVE
     * 2. There are no overlapping confirmed or checked-in reservations
     */
    public boolean isRoomAvailable(Room room, Reservation newReservation, List<Reservation> allReservations) {
        // Check room status
        if (room.getStatus() != RoomStatus.ACTIVE) {
            return false;
        }

        // Check for overlapping reservations
        for (Reservation existingReservation : allReservations) {
            // Only consider confirmed or checked-in reservations
            if (existingReservation.getStatus() == ReservationStatus.CONFIRMED ||
                existingReservation.getStatus() == ReservationStatus.CHECKED_IN ||
                existingReservation.getStatus() == ReservationStatus.CREATED) {
                
                // Check if same room
                if (existingReservation.getRoom().getNumber() == room.getNumber()) {
                    // Check if overlapping
                    if (newReservation.overlaps(existingReservation)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
