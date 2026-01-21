package hotel.model;

import hotel.model.enums.RoomType;
import hotel.model.enums.RoomStatus;
import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
        return "Quarto{" +
                "número=" + number +
                ", tipo=" + type +
                ", preçoDiário=" + dailyBasePrice +
                ", capacidade=" + capacity +
                ", status=" + status +
                '}';
    }
}
