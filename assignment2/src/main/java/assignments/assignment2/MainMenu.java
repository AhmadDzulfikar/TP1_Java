package assignments.assignment2;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
// import assignments.assignment1.*;

public class MainMenu {
    private static final Scanner input = new Scanner(System.in);
    private static ArrayList<Restaurant> restoList;
    private static ArrayList<User> userList;
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date
    private static User userLoggedIn;

    // Mendeklarasikan Code 39 Character dengan array
    private static final char[] CODE_39_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static void main(String[] args) {
        restoList = new ArrayList<>();
        initUser();
        boolean programRunning = true;
        printHeader();
        while(programRunning){
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

                userLoggedIn = getUser(nama, noTelp);
                // User userLoggedIn = getUser(nama, noTelp);

                if (userLoggedIn != null) {
                    System.out.println("Selamat datang " + userLoggedIn.getNama());
                } else {
                    System.out.println("Pengguna dengan data tersebut tidak ditemukan!");
                    continue;
                }

                boolean isLoggedIn = true;
                if(userLoggedIn.getRole().equals("Customer")){
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
                }else{
                    while (isLoggedIn){
                        menuAdmin();
                        int commandAdmin = input.nextInt();
                        input.nextLine();

                        switch(commandAdmin){
                            case 1 -> handleTambahRestoran();
                            case 2 -> handleHapusRestoran();
                            case 3 -> isLoggedIn = false;
                            default -> System.out.println("Perintah tidak diketahui, silakan coba kembali");
                        }
                    }
                }
            }else if(command == 2){
                programRunning = false;
            }else{
                System.out.println("Perintah tidak diketahui, silakan periksa kembali.");
            }
        }
        System.out.println("\nTerima kasih telah menggunakan DepeFood ^_^");
    }

    public static User getUser(String nama, String nomorTelepon){
        // TODO: Implementasi method untuk mendapat user dari userList
        User userLoggedIn = null;
        for (User user : userList) {
            if (user.getNama().equals(nama) && user.getNomorTelepon().equals(nomorTelepon)) {
                userLoggedIn = user;
                break;
            }
        }
        return userLoggedIn;
    }

    // Untuk membuat Order IDnya
    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        String orderId = "";

        // 1. (ORDER ID 1-4) Jika panjang inputRestonya >= 4, maka ambil bagian 4 string pertama
        namaRestoran = namaRestoran.toUpperCase();
        if (namaRestoran.length() >= 4) {       
            orderId = namaRestoran.substring(0,4);
        }

        // 2. (ORDER ID 5-12) Menambahkan semua tanggal pemesanan ke dalam orderId 
        String[] tanggalParts = tanggalOrder.split("/");
        for ( String part : tanggalParts) {
            orderId += part;
        }

        // 3. (Order ID 13 & 14) Menjumlah setiap digit no telepon dan dimodulo 100
        //define sum & resultSum menjadi 0, default untuk dijumlahkan nanti
        int sum = 0;
        int resultSum = 0;

        // looping sesuai panjang inputan noTelpnya
        for (int i = 0; i < noTelepon.length(); i++) {
            // charAt untuk mendapatkan nilai character, getNumericValue untuk mengonversi nilai karakter menjadi numerik, lalu dijumlahkan
            sum += Character.getNumericValue(noTelepon.charAt(i));      
        }
        resultSum += sum % 100 ;

        String formattedNumber = String.format("%02d", resultSum);  // Agar menjadi 2 digit dan diawali 0 jika hasilnya adalah 1 digit

        orderId += formattedNumber;

        // 4. (Order ID 15 & 16 )
        // Menghitung checksum
        String checksum = calculateChecksum(orderId);

        // Menggabungkan ID pesanan dengan checksum
        orderId = checksum;

        return orderId;
    }

        // Metode untuk menghasilkan ID pesanan dengan checksum
    // Method untuk menghitung checksum berdasarkan aturan yang diberikan
    private static String calculateChecksum(String orderId) {
        int sumEven = 0;
        int sumOdd = 0;

        // Melakukan iterasi pada 14 karakter sebelumnya
        for (int i = 0; i < orderId.length(); i++) {
            int numericValue = Character.getNumericValue(orderId.charAt(i));
            if (i % 2 == 0) {
                // Karakter pada posisi genap
                sumEven += numericValue;
            } else {
                // Karakter pada posisi ganjil
                sumOdd += numericValue;
            }
        }

        // Menghitung checksum 1 dan checksum 2
        int checksum1 = sumEven % 36;
        int checksum2 = sumOdd % 36;

        // Mendapatkan karakter untuk checksum 1 dan checksum 2
        char checksumChar1 = CODE_39_CHARACTERS[checksum1];
        char checksumChar2 = CODE_39_CHARACTERS[checksum2];

        orderId += checksumChar1;
        orderId += checksumChar2;

        return orderId;
    }

    // Untuk membuat validasi 2 digit terakhir ketika di bagian inputOrderID
    public static boolean validationID(String orderIdWithoutChecksum, String checksumFromInput) {
        // Menghitung checksumnya
        String checksum = calculateChecksum(orderIdWithoutChecksum);
    
        // Mendapatkan checksum hasil dari perhitungan
        String calculatedChecksum = checksum.substring(checksum.length() - 2);
    
        // Membandingkan checksum input dengan checksum yang dihitung
        return calculatedChecksum.equals(checksumFromInput);
    }

    public static void handleBuatPesanan(){
        String namaResto;
        String tanggalPemesanan;
        String nomorTelponUser = userLoggedIn.getNomorTelepon();
        System.out.println("----------------Buat Pesanan-----------------");
        while (true) {
            System.out.print("Nama Restoran: ");
            namaResto = input.nextLine();

            // Untuk mengecek nama restoran sudah terdaftar atau belum
            boolean RestoranDidapati = false;
            for (Restaurant resto : restoList) {
                if (resto.getNama().equalsIgnoreCase(namaResto)) {
                    RestoranDidapati = true;
                    break;
                }
            }

            if (!RestoranDidapati) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
                continue;
            }

            System.out.print("Tanggal Pemesanan (DD/MM/YYYY): ");
            tanggalPemesanan = input.nextLine();
            if (!DATE_PATTERN.matcher(tanggalPemesanan).matches()) {
                System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY) !");
                continue;
            }

            String orderId = generateOrderID(namaResto, tanggalPemesanan, nomorTelponUser);
            System.out.println("Pesanan dengan ID " + orderId.toUpperCase() + " diterima!");
        }
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
        while (true) {
            System.out.print("Masukkan nama restoran: ");
            String namaRestoran = input.nextLine();

            // // Untuk memastikan agar nama restoran unik
            boolean restoTerdaftar = false;
            for (Restaurant resto : restoList) {
                if (resto.getNama().equalsIgnoreCase(namaRestoran)) {
                    restoTerdaftar = true;
                    break;
                }
            }

            if (restoTerdaftar) {
                System.out.println("Restoran dengan nama " + namaRestoran + " sudah pernah terdaftar. Mohon masukkan nama yang berbeda!");
                continue;
            }
            
            // Jika nama restoran tidak 4 huruf maka akan loop untuk meminta ulang
            Restaurant newRestaurant = new Restaurant(namaRestoran);
            if (namaRestoran.length() < 4) {
                System.out.println("Nama restoran tidak valid!");
                continue;
            }
    
            System.out.print("Masukkan jumlah makanan yang ingin ditambahkan: ");
            int jumlahMakanan = input.nextInt();
            input.nextLine(); // Membuang newline setelah nextInt()
    
            System.out.print("Masukkan menu restoran (nama makanan harga):");
            int makananCount = 0;
            while (makananCount < jumlahMakanan) {
                String menuInput = input.nextLine();
                int indexLastSpace = menuInput.lastIndexOf(' ');
                if (indexLastSpace == -1 || indexLastSpace == 0 || indexLastSpace == menuInput.length() - 1) {
                    System.out.print("Format input tidak valid. Masukkan kembali (nama makanan harga):");
                } else {
                    String namaMakanan = menuInput.substring(0, indexLastSpace);
                    String hargaStr = menuInput.substring(indexLastSpace + 1);
                    try {
                        double harga = Double.parseDouble(hargaStr);
                        Menu newMenu = new Menu(namaMakanan, harga);
                        newRestaurant.tambahMenu(newMenu);
                        makananCount++;
                    } catch (NumberFormatException e) {
                        System.out.print("Format input harga tidak valid. Masukkan kembali (nama makanan harga):");
                    }
                }
            }
            restoList.add(newRestaurant);
            System.out.println("Restaurant " + namaRestoran + " berhasil terdaftar");
            break;
        }
    }
    
    public static void handleHapusRestoran(){
        boolean restoDidapati = false;
        while (!restoDidapati) {
            System.out.println("\n____________________________________ HAPUS RESTORAN ____________________________________");
            System.out.print("Nama Restoran: ");
            String namaRestoran = input.nextLine();

            for (int i = 0; i < restoList.size(); i++) {
                Restaurant resto = restoList.get(i);
                if (resto.getNama().equalsIgnoreCase(namaRestoran)) {
                    restoList.remove(i);
                    restoDidapati = true;
                    System.out.println("Restoran berhasil dihapus.");
                    break;
                }
            }

            if (!restoDidapati) {
                System.out.println("Restoran tidak terdaftar pada sistem.");
            }
        }
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
        System.out.println("|| _                 _             _ ||");
        System.out.println("||| . \\ _  _  _ | _>_  __  _| |||");
        System.out.println("||| | |/ .>| . \\/ .>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||_/\\_.|  /\\_.|| \\_/\\_/\\_|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
    }

    public static void startMenu(){
        System.out.println("\nSelamat datang di DepeFood!");
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
        System.out.println("--------------------------------------------");
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