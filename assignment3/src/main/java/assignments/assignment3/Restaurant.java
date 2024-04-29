package assignments.assignment3;
import assignments.assignment2.*;

import java.util.ArrayList;

public class Restaurant {
    // attributes
    private String nama;
    private ArrayList<Menu> menu;
    
    public Restaurant(String nama){
        // Konstruktor
        this.nama = nama;
        this.menu = new ArrayList<>();
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
}