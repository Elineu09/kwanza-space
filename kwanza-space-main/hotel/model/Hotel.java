package hotel.model;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String name;
    private List<Room> rooms;
    private List <Client> clients;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.clients = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return new ArrayList<>(rooms);
    }

    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public Room findRoomByNumber(int number) {
        for (Room room : rooms) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }

    public List<Client> getClients() {
        return clients;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "name='" + name + '\'' +
                ", rooms=" + rooms.size() +
                ", clients=" + clients.size() +
                '}';
    }
}
