package assignments.assignment3;

public class Order {
    // attributes
    private String orderId;
    private String tanggal; 
    private int ongkir;
    private Restaurant resto;
    private Menu[] items;
    private boolean pengirimanSelesai;

    public Order(String orderId, String tanggal, int ongkir, Restaurant resto, Menu[] items){
        // constructor 
        this.orderId = orderId;
        this.tanggal = tanggal;
        this.ongkir = ongkir; 
        this.resto = resto;
        this.items = items;
        this.pengirimanSelesai = false;
    }

    // Getter & Setter
    //--------
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    //--------
    public String getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    //--------
    public int getOngkir() {
        return ongkir;
    }

    public void setOngkir(int ongkir) {
        this.ongkir = ongkir;
    }

    //--------
    public Restaurant getResto() {
        return resto;
    }

    public void setResto(Restaurant resto) {
        this.resto = resto;
    }

    //--------
    public Menu[] getItems() {
        return items;
    }

    public int getJumlahpesanan() {
        return items.length;
    }

    //--------
    public String getTanggalPemesanan() {
        return tanggal;
    }

    // Getter for pengirimanSelesai
    public boolean isPengirimanSelesai() {
        return pengirimanSelesai; 
    }

    //-------- 
    // Untuk mengatur apakah pengiriman Selesai atau Belum Selesai
    public void setPengirimanSelesai(boolean pengirimanSelesai) {
        this.pengirimanSelesai = pengirimanSelesai;
    }

    // Getter for pesanan
    public Menu[] getPesanan() {
        return items;
    }

    // Getter for lokasi (assuming it's the restaurant's location)
    public String getLokasi() {
        return resto.getNama();
    }
}
