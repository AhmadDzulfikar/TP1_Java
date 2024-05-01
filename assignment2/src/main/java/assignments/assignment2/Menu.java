package assignments.assignment2;

public class Menu {
    // attributes
    private String namaMakanan;
    private double harga;
    // Konstruktor
    public Menu(String namaMakanan, double harga){
        this.namaMakanan = namaMakanan;
        this.harga = harga;
    }

    // Metode untuk mendapatkan nama makanan
    public String getNamaMakanan() {
        return namaMakanan;
    }

    // Metode untuk mendapatkan harga makanan
    public double getHarga() {
        return harga;
    }
}   
