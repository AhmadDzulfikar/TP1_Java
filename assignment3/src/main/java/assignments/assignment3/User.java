package assignments.assignment3;

import java.util.ArrayList;
// import assignments.assignment2.Order;
import assignments.assignment3.payment.DepeFoodPaymentSystem;
import assignments.assignment2.*;
public class User {
    // Attributes
    private String nama;
    private String nomortelepon;
    private String email;
    private String lokasi;
    public String role;
    private ArrayList<Order> orderHistory = new ArrayList<>();
    private DepeFoodPaymentSystem payment;
    private long saldo;

    public User(String nama, String nomorTelepon, String email, String lokasi, String role, DepeFoodPaymentSystem payment, long saldo){
        // Konstruktor
        this.nama = nama;
        this.nomortelepon = nomorTelepon;
        this.email = email;
        this. lokasi = lokasi;
        this.role = role;
        this.orderHistory = new ArrayList<>();
        this.payment = payment;
        this.saldo = saldo;
    }
    // Getter 
    public String getNama() {
        return nama;
    }

    public String getNomorTelepon() {
        return nomortelepon;
    }
    
    public String getEmail() {
        return email;
    }


    public String getLokasi() {
        return lokasi;
    }
    

    public String getRole() {
        return role;
    }

    public DepeFoodPaymentSystem getPayment() {
        return payment;
    }

    public long getSaldo() {
        return saldo;
    }

    public void setSaldo(long saldo) {
        this.saldo = saldo;
    }

    // Mengembalikan daftar pesanan user dalam orderHistory
    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
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

    // Menambahkan pesanan kedalam orderHistory user
    public void tambahPesanan(Order order) {
        orderHistory.add(order);
    }
    
}
