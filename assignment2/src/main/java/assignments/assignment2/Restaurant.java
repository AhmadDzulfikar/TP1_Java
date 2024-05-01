package assignments.assignment2;
import java.util.ArrayList;

public class Restaurant {
    // attributes
    private String nama;
    private ArrayList<Menu> menu;
    // private long saldo;
    
    public Restaurant(String nama){
        // Konstruktor
        this.nama = nama;
        this.menu = new ArrayList<>();
        // this.saldo = saldo;
    }

    // Metode untuk menambahkan menu baru
    public void tambahMenu(Menu itemMenu) {
        menu.add(itemMenu);
    }

    // getter Nama
    public String getNama() {
        return nama;
    }

    // getter Menu
    public ArrayList<Menu> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Menu> menu) {
        this.menu = menu;
    }

    // public long getSaldo() {
    //     return saldo;
    // }

    // public void setSaldo(long saldo) {
    //     this.saldo = saldo;
    // }

}