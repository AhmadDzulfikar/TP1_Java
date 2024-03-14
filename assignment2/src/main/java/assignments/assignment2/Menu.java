package assignments.assignment2;

public class Menu {
     // TODO: tambahkan attributes yang diperlukan untuk class ini
    private String namaMakanan;
    private double harga;
    public Menu(String namaMakanan, double harga){
        // TODO: buat constructor untuk class ini
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

    // TODO: tambahkan methods yang diperlukan untuk class ini
}
