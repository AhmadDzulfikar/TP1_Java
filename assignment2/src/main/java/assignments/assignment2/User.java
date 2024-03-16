package assignments.assignment2;

import java.util.ArrayList;

public class User {
    private String nama;
    private String nomortelepon;
    private String email;
    private String lokasi;
    private String role;
    private ArrayList<Order> orderHistory = new ArrayList<>();

     // TODO: tambahkan attributes yang diperlukan untuk class ini
    public User(String nama, String nomorTelepon, String email, String lokasi, String role){
        // TODO: buat constructor untuk class ini
        this.nama = nama;
        this.nomortelepon = nomorTelepon;
        this.email = email;
        this. lokasi = lokasi;
        this.role = role;
        this.orderHistory = new ArrayList<>();
    }
    // Getter 
    public String getRole() {
        return role;
    }

    public String getNama() {
        return nama;
    }

    public String getNomorTelepon() {
        return nomortelepon;
    }

    public String getLokasi() {
        return lokasi;
    }

    // Setter
    public void setRole(String role) {
        this.role = role;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setNomorTelepon(String nomorTelepon) {
        this.nomortelepon = nomorTelepon;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public void tambahPesanan(Order order) {
        orderHistory.add(order);
    }

    // Method to get the list of orders
    public ArrayList<Order> getDaftarPesanan() {
        return orderHistory;
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }
}