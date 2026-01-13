package hotel.model.enums;

public enum RoomType {
    STANDARD(1.00),
    DELUXE(1.15),
    SUITE(1.30);

    private final double multiplier;

    RoomType(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
