package assignments.assignment2;
import java.util.ArrayList;

public class Restaurant {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String nama;
    private ArrayList<Menu> menu;
    public Restaurant(String nama){
        // TODO: buat constructor untuk class ini
        this.nama = nama;
        this.menu = new ArrayList<>();
    }

    // Metode untuk menambahkan menu baru
    public void tambahMenu(Menu itemMenu) {
        menu.add(itemMenu);
    }

    public String getNama() {
        return nama;
    }

    public ArrayList<Menu> getMenu() {
        return menu;
    }
    
    // TODO: tambahkan methods yang diperlukan untuk class ini
}
