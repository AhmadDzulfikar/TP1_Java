package assignments.assignment3.systemCLI;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

// import assignments.assignment3.*;
import assignments.assignment1.*;
import assignments.assignment3.*;
import assignments.assignment3.payment.CreditCardPayment;

import assignments.assignment1.OrderGenerator;

//Extends abstract class 
public class CustomerSystemCLI extends UserSystemCLI{
    static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date

    //Tambahkan modifier dan metode ini mengoverride dari Abstract class
    boolean handleMenu(int choice){
        switch(choice){
            case 1 -> handleBuatPesanan();
            case 2 -> handleCetakBill();
            case 3 -> handleLihatMenu();
            case 4 -> handleBayarBill();
            case 5 -> handleCekSaldo();
            case 6 -> {
                return false;
            }
            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
        }
        return true;
    }

    //Tambahkan modifier dan metode ini mengoverride dari Abstract class
    @Override
    void displayMenu() {
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Bayar Bill");
        System.out.println("5. Cek Saldo");
        System.out.println("6. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    // Implementasi method untuk handle customer membuat pesanan
    void handleBuatPesanan(){
        String namaResto;
        String tanggalPemesanan;
        String nomorTelponUser = MainMenu.userLoggedIn.getNomorTelepon();
        System.out.println("----------------Buat Pesanan-----------------");
        while (true) {
            // Input nama restoran!
            System.out.print("Nama Restoran: ");
            namaResto = input.nextLine();

            // Untuk mengecek nama restoran sudah terdaftar atau belum
            boolean RestoranDidapati = false;
            Restaurant targetResto = null;
            for (Restaurant resto : MainMenu.getRestoList()) {    // Mengiterasi setiap elemen daftar restoList
                if (resto.getNama().equalsIgnoreCase(namaResto)) {  // Memeriksa apakah nama restoran yang ada di objek resto itu sama dengan yang diinput
                    RestoranDidapati = true;    // Restoran ditemukan 
                    targetResto = resto;
                    break;
                }
            }

            if (!RestoranDidapati) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }

            // Input tanggal pemesanan!
            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalPemesanan = input.nextLine();
            if (!DATE_PATTERN.matcher(tanggalPemesanan).matches()) { // Mencocokan input tanggal pemesanan dengan pola, mereturn true jika benar dan sebaliknya, jika false turun kebawah
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY) !");
                continue;
            }

            // Meminta jumlah pesanan
            System.out.print("Jumlah Pemesanan: ");
            int jumlahPesanan = input.nextInt();
            input.nextLine(); // Membuang newline setelah nextInt()

            Menu[] pesanan = new Menu[jumlahPesanan];
            // Meminta nama makanan untuk setiap pesanan
            System.out.println("Order:");

            // Meminta input user untuk memasukkan pesanan yang mau ditambahkan ke pesanan mereka
            for (int i = 0; i < jumlahPesanan; i++) {
                boolean pesananValid = false;
                while (!pesananValid) {
                    String namaPesanan = input.nextLine();
                    // Memeriksa apakah nama makanan ada di menu restoran
                    for (Menu menu : targetResto.getMenu()) {
                        if (menu.getNamaMakanan().equalsIgnoreCase(namaPesanan)) {
                            pesanan[i] = menu;
                            pesananValid = true;
                            break;
                        }
                    }
                    if (!pesananValid) {
                        System.out.println("Mohon memesan menu yang tersedia di Restoran.");
                    }
                }
            }

            String orderId = OrderGenerator.generateOrderID(namaResto, tanggalPemesanan, nomorTelponUser); // Menggunakan generateOrderID untuk membuat orderId
            Order order = new Order(orderId, tanggalPemesanan, 0, targetResto, pesanan);    // Membuat objek Order baru menggunakan konstruktor Order
            MainMenu.userLoggedIn.tambahPesanan(order); // Menambahkan pesanan ke riwayat pesanan pengguna
            System.out.println("Pesanan dengan ID " + orderId.toUpperCase() + " diterima!");
            break;
        }
    }

    // Implementasi method handle saat customer ingin cetak bill
    void handleCetakBill(){
    System.out.println("----------------Cetak Bill----------------");
    String orderId;
    Order targetOrder;

    do {
        System.out.print("Masukkan Order ID: ");
        orderId = input.nextLine();

        targetOrder = null; // Kalau gaada orderId yang cocok maka akan tetap null
        // Perulangan setiap pesanan user yang terdaftar
        for (Order order : MainMenu.userLoggedIn.getOrderHistory()) {
            if (order.getOrderId().equals(orderId)) {
                targetOrder = order;
                break;
            }
        }

        // Jika tidak hasil dari targetOrdernya masih null maka akan memunculkan output tidak ditemukan
        if (targetOrder == null) {
            System.out.println("Order ID tidak ditemukan. Silahkan coba lagi");
        }
        } while (targetOrder == null);

        // Mencetak output
        System.out.println("\nBill:");
        System.out.println("Order ID: " + targetOrder.getOrderId()); // Class Order
        System.out.println("Tanggal Pemesanan: " + targetOrder.getTanggalPemesanan());   // Class Order
        System.out.println("Restaurant: " + targetOrder.getResto().getNama());  // class Order
        System.out.println("Lokasi Pengiriman: " + MainMenu.userLoggedIn.getLokasi());    // Class User
        System.out.println("Status Pengiriman: " + (targetOrder.isPengirimanSelesai() ? "Finished" : "Not Finished")); // Jika kondisi bernilai true maka return Finished, dan sebaliknya
        
        System.out.println("Pesanan:");
        // Mengambil setiap elemen dalam daftar pesanan
        for (Menu menu : targetOrder.getPesanan()) {
            System.out.println("-" + menu.getNamaMakanan() + " " + (int)menu.getHarga()); 
        }

        int biayaOngkos = calculateDeliveryCost(MainMenu.userLoggedIn.getLokasi());
        System.out.println("Biaya Ongkos Kirim: Rp " + biayaOngkos);

        int totalBiaya = biayaOngkos;
        // Untuk mengakses setiap elemen dalam daftar pesanan dan menghitung semua biayanya
        for (Menu menu : targetOrder.getPesanan()) {
            totalBiaya += menu.getHarga();
        }
        System.out.println("Total Biaya: Rp " + totalBiaya);
    }

      // Mendefine jika lokasi sesuatu maka harganya sekian
    public static int calculateDeliveryCost(String location) {
        switch (location) {
            case "P":
                return 10000;
            case "U":
                return 20000;
            case "T":
                return 35000;
            case "S":
                return 40000;
            case "B":
                return 60000;
            default:
                return 0;
        }
    }

    // Implementasi method untuk handle ketika customer ingin melihat menu
    void handleLihatMenu(){
        System.out.println("-----------------Lihat Menu-----------------");
        String namaResto;
        boolean restoRegistered = false;
        do {
            System.out.print("Nama Restoran: ");
            namaResto = input.nextLine();

            // Mencari nama restoran sesuai dengan nama resto yang di input
            for (Restaurant resto : MainMenu.getRestoList()) {
                if (resto.getNama().equalsIgnoreCase(namaResto)) {
                    restoRegistered = true;
                    // Pengurutan menu dalam restoran
                    int n = resto.getMenu().size(); // Mendapatkan jumlah menu dalam restoran (size)
                    // Mulai Bubble Sort
                    for (int i = 0; i < n - 1; i++) { // for pertama = mengatur berapa kali mengulangi proses pengurutan (mengulangi proses sebanyak i - 1)
                        for (int k = 0; k < n - i - 1; k++) { // for kedua = membandingkan pasangan menu berturut turut dan menukarnya
                            // Membandingkan menu berdasarkan harga, jika harganya sama maka akan dibandingkan dengan huruf
                            if (resto.getMenu().get(k).getHarga() > resto.getMenu().get(k + 1).getHarga() || (resto.getMenu().get(k).getHarga() == resto.getMenu().get(k + 1).getHarga()
                                    && resto.getMenu().get(k).getNamaMakanan().compareToIgnoreCase(resto.getMenu().get(k + 1).getNamaMakanan()) > 0)) {
                                // Menu pertukaran kika kondisi terpenuhi
                                Menu temp = resto.getMenu().get(k); // Menyimpan sementara
                                resto.getMenu().set(k, resto.getMenu().get(k + 1)); // Mindahin menu di posisi K+1 ke K
                                resto.getMenu().set(k + 1, temp); // Nuker posisi K+1 dengan K (sebaliknya juga)
                            }
                        }
                    }
                    // Mencetak hasil dari menu yang sudah diurutkan 
                    System.out.println("Menu:");
                    int menuNumber = 1;
                    for (Menu menu : resto.getMenu()) {
                        System.out.println(menuNumber + ". " + menu.getNamaMakanan() + " " + (int) menu.getHarga());
                        menuNumber++;
                    }
                    break;
                }
            }

            if (!restoRegistered) {
                System.out.println("Restoran tidak ditemukan. Silakan coba lagi.");
            }
        } while (!restoRegistered);
    }

    // // Implementasi method untuk handle ketika customer ingin melihat menu
    void handleBayarBill(){
    System.out.println("----------------Cetak Bill----------------");
    String orderId;
    Order targetOrder;

    do {
        System.out.print("Masukkan Order ID: ");
        orderId = input.nextLine();

        targetOrder = null; // Kalau gaada orderId yang cocok maka akan tetap null
        // Perulangan setiap pesanan user yang terdaftar
        for (Order order : MainMenu.userLoggedIn.getOrderHistory()) {
            if (order.getOrderId().equals(orderId)) {
                targetOrder = order;
                break;
            }
        }

        // Jika tidak hasil dari targetOrdernya masih null maka akan memunculkan output tidak ditemukan
        if (targetOrder == null) {
            System.out.println("Order ID tidak ditemukan. Silahkan coba lagi");
        } else if (targetOrder.isPengirimanSelesai()) {
            System.out.println("Pesanan dengan ID ini sudah lunas!");
            return; // Return to the main menu
        }

        } while (targetOrder == null);



        // Mencetak output
        System.out.println("\nBill:");
        System.out.println("Order ID: " + targetOrder.getOrderId()); // Class Order
        System.out.println("Tanggal Pemesanan: " + targetOrder.getTanggalPemesanan());   // Class Order
        System.out.println("Restaurant: " + targetOrder.getResto().getNama());  // class Order
        System.out.println("Lokasi Pengiriman: " + MainMenu.userLoggedIn.getLokasi());    // Class User
        System.out.println("Status Pengiriman: " + (targetOrder.isPengirimanSelesai() ? "Finished" : "Not Finished")); // Jika kondisi bernilai true maka return Finished, dan sebaliknya
        
        if (targetOrder != null) {
            System.out.println("Pesanan:");
            for (Menu menu : targetOrder.getPesanan()) {
                System.out.println("-" + menu.getNamaMakanan() + " " + (int) menu.getHarga());
            }

        int biayaOngkos = calculateDeliveryCost(MainMenu.userLoggedIn.getLokasi());
        System.out.println("Biaya Ongkos Kirim: Rp " + biayaOngkos);

        int totalBiaya = biayaOngkos;
        // Untuk mengakses setiap elemen dalam daftar pesanan dan menghitung semua biayanya
        for (Menu menu : targetOrder.getPesanan()) {
            totalBiaya += menu.getHarga();
        }
        System.out.println("Total Biaya: Rp " + totalBiaya);

        // Untuk pilihan pembayaran user
        System.out.println("\nOpsi Pembayaran: ");
        System.out.println("1. Credit Card");
        System.out.println("2. Debit");

        System.out.println("Pilihan Metode Pembayaran: ");
        int metodePembayaran = input.nextInt();
        input.nextLine();

        if (metodePembayaran == 1) {
            // ____Membayar menggunakan Credit card
            CreditCardPayment cPayment = new CreditCardPayment();
            long biayaTotalSetelahTransaksi = cPayment.processPayment(totalBiaya);

            // ____Mengubah saldo user
            long saldoPengguna = MainMenu.userLoggedIn.getSaldo();
            saldoPengguna -= biayaTotalSetelahTransaksi;
            MainMenu.userLoggedIn.setSaldo(saldoPengguna);

            // // ____Mengubah saldo restoran
            long saldoRestoran = targetOrder.getResto().getSaldo();
            saldoRestoran += totalBiaya;
            targetOrder.getResto().setSaldo(saldoRestoran);

            // Set status
            targetOrder.setPengirimanSelesai(true);
        
            long biayaTransaksi = biayaTotalSetelahTransaksi - totalBiaya;
            System.out.println("\nBerhasil Membayar Bill sebesar Rp " + totalBiaya + " dengan biaya transaksi sebesar Rp " + biayaTransaksi);
    } else {
        // Update saldo pengguna
            long saldoPengguna = MainMenu.userLoggedIn.getSaldo();
            saldoPengguna -= totalBiaya;
            MainMenu.userLoggedIn.setSaldo(saldoPengguna);

            // Update saldo restoran
            long saldoRestoran = targetOrder.getResto().getSaldo();
            saldoRestoran += totalBiaya;
            targetOrder.getResto().setSaldo(saldoRestoran);

            // Set status pesanan to "Selesai"
            targetOrder.setPengirimanSelesai(true);

            // Print the successful payment message for Debit
            System.out.println("\nBerhasil Membayar Bill sebesar Rp " + totalBiaya);
    }
}   
    }


    // Implementasi method untuk handle ketika customer ingin mengecek saldo yang dimiliki
    void handleCekSaldo(){
        System.out.println("Sisa saldo sebesar Rp " + MainMenu.userLoggedIn.getSaldo());
    }
}
