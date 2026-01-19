package hotel.model;

import hotel.model.enums.RoomType;
import hotel.model.enums.RoomStatus;

public class Room {
    private int number;
    private RoomType type;
    private double dailyBasePrice;
    private int capacity;
    private RoomStatus status;

    public Room(int number, RoomType type, double dailyBasePrice, int capacity, RoomStatus status) {
        this.number = number;
        this.type = type;
        this.dailyBasePrice = dailyBasePrice;
        this.capacity = capacity;
        this.status = status;
    }

    public int getNumber() {
        return number;
    }

    public RoomType getType() {
        return type;
    }

    public double getDailyBasePrice() {
        return dailyBasePrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", type=" + type +
                ", dailyBasePrice=" + dailyBasePrice +
                ", capacity=" + capacity +
                ", status=" + status +
                '}';
    }
}
