package assignments.assignment2;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
// import assignments.assignment1.*;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;

    public static void main(String[] args) {
        boolean programRunning = true;
        restoList = new ArrayList<>(); // Inisialisasi restoList
        initUser();
        while(programRunning){
            printHeader();
            startMenu();
            int command = input.nextInt();
            input.nextLine();

            if(command == 1){
                System.out.println("\nSilakan Login:");
                System.out.print("Nama: ");
                String nama = input.nextLine();
                System.out.print("Nomor Telepon: ");
                String noTelp = input.nextLine();

                // TODO: Validasi input login

                User userLoggedIn; // TODO: lengkapi
                boolean isLoggedIn = true;

                userLoggedIn = getUser(nama, noTelp);

                if (userLoggedIn != null ) {
                    if (userLoggedIn.getRole().equals("Admin")) {
                        // Memanggil method menuAdmin() jika pengguna adalah Admin
                        while (isLoggedIn) {
                            System.out.println("Selamat Datang Admin");
                            menuAdmin();
                            int commandAdmin = input.nextInt();
                            input.nextLine();

                            switch(commandAdmin){
                                case 1 -> handleTambahRestoran();
                                case 2 -> handleHapusRestoran();
                                case 5 -> isLoggedIn = false;
                                default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                } else if(userLoggedIn.getRole().equals("Customer")){
                // if(userLoggedIn != null && userLoggedIn.getRole().equals("Customer")) {
                    while (isLoggedIn){
                        menuCustomer();
                        int commandCust = input.nextInt();
                        input.nextLine();

                        switch(commandCust){
                            case 1 -> handleBuatPesanan();
                            case 2 -> handleCetakBill();
                            case 3 -> handleLihatMenu();
                            case 4 -> handleUpdateStatusPesanan();
                            case 5 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                } else {
                    System.out.println("Peran pengguna tidak valid");
                }
            } else {
                System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                break;
            }
        } else if (command == 2){
            programRunning = false;
        } else {
            System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
        }
    }
    System.out.println("\nTerima kasih telah menggunakan DepeFood ^___^");
}

    public static User getUser(String nama, String nomorTelepon){
        // TODO: Implementasi method untuk mendapat user dari userList
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                return user;
            }
        }
        return null;
    }

    public static void handleBuatPesanan(){
        // TODO: Implementasi method untuk handle ketika customer membuat pesanan
    }

    public static void handleCetakBill(){
        // TODO: Implementasi method untuk handle ketika customer ingin cetak bill
    }

    public static void handleLihatMenu(){
        // TODO: Implementasi method untuk handle ketika customer ingin melihat menu
    }

    public static void handleUpdateStatusPesanan(){
        // TODO: Implementasi method untuk handle ketika customer ingin update status pesanan
    }

    public static void handleTambahRestoran(){
        // TODO: Implementasi method untuk handle ketika admin ingin tambah restoran
        tambahRestoran();
    }
    public static void tambahRestoran() {
        System.out.print("Nama: ");
        String namaRestoran = input.nextLine();

        System.out.print("Jumlah Makanan: ");
        int jumlahMakanan = input.nextInt();
        input.nextLine();

        Restaurant newRestaurant = new Restaurant(namaRestoran); // Membuat instansi baru dari class restaurant 

        for (int i = 0; i < jumlahMakanan; i++) {
            System.out.print("Nama Makanan dan harga: ");
            String inputMakanan = input.nextLine();
        
        StringTokenizer tokenizer = new StringTokenizer(inputMakanan);
        String namaMakanan = "";
        double harga = 0;
        
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            try {
                harga = Double.parseDouble(token);
                break; // Jika token adalah angka, maka berhenti dari loop
            } catch (NumberFormatException e) {
                namaMakanan += token + " ";
            }
        }
        
        // Menghapus spasi ekstra di akhir nama makanan
        namaMakanan = namaMakanan.trim();

        // Membuat instansi Menu baru
        Menu newMenu = new Menu(namaMakanan, harga);
        // Menambahkan menu ke restoran
        newRestaurant.tambahMenu(newMenu);
    }

        // Menambahkan restoran baru ke daftar restoran
        restoList.add(newRestaurant);
        System.out.println("Restoran " + namaRestoran + "! berhasil terdaftar.");
    }
    

    public static void handleHapusRestoran(){
        // TODO: Implementasi method untuk handle ketika admin ingin tambah restoran
    }

    public static void initUser(){
       userList = new ArrayList<User>();
       userList.add(new User("Thomas N", "9928765403", "thomas.n@gmail.com", "P", "Customer"));
       userList.add(new User("Sekar Andita", "089877658190", "dita.sekar@gmail.com", "B", "Customer"));
       userList.add(new User("Sofita Yasusa", "084789607222", "sofita.susa@gmail.com", "T", "Customer"));
       userList.add(new User("Dekdepe G", "080811236789", "ddp2.gampang@gmail.com", "S", "Customer"));
       userList.add(new User("Aurora Anum", "087788129043", "a.anum@gmail.com", "U", "Customer"));

       userList.add(new User("Admin", "123456789", "admin@gmail.com", "-", "Admin"));
       userList.add(new User("Admin Baik", "9123912308", "admin.b@gmail.com", "-", "Admin"));
    }

    public static void printHeader(){
        System.out.println("\n>>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.out.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("Selamat datang di DepeFood!");
        System.out.println("--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Login");
        System.out.println("2. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuAdmin(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Tambah Restoran");
        System.out.println("2. Hapus Restoran");
        System.out.println("3. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }

    public static void menuCustomer(){
        System.out.println("\n--------------------------------------------");
        System.out.println("Pilih menu:");
        System.out.println("1. Buat Pesanan");
        System.out.println("2. Cetak Bill");
        System.out.println("3. Lihat Menu");
        System.out.println("4. Update Status Pesanan");
        System.out.println("5. Keluar");
        System.out.println("--------------------------------------------");
        System.out.print("Pilihan menu: ");
    }
}