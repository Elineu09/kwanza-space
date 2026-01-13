package hotel.model;

public class Client {
    private String id;
    private String fullName;
    private String document;
    private String phone;
    private String email;

    public Client(String id, String fullName, String document, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.phone = phone;
        this.email = null;
    }

    public Client(String id, String fullName, String document, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.phone = phone;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDocument() {
        return document;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id='" + id + '\'' +
                ", fullName='" + fullName + '\'' +
                ", document='" + document + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
