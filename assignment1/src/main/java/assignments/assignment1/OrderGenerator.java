package assignments.assignment1;

import java.util.Scanner;
import java.util.regex.Pattern;

public class OrderGenerator { 
    private static final Scanner input = new Scanner(System.in);
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date

    // Deklarasikan Code 39 Character Set sebagai array
    private static final char[] CODE_39_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
    'U', 'V', 'W', 'X', 'Y', 'Z'};

    public static void showMenu(){
        System.out.println(">>=======================================<<");
        System.out.println("|| ___                 ___             _ ||");
        System.err.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
        System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
        System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
        System.out.println("||          |_|                          ||");
        System.out.println(">>=======================================<<");
        System.out.println();
        System.out.println("Pilih menu:");
        System.err.println("1. Generate Order ID");
        System.out.println("2. Generate Bill");
        System.out.println("3. Keluar");

        System.out.println("-------------------------------------");
    }

    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {
        String orderId = "";

        // 1. (ORDER ID 1-4) Jika panjang inputRestonya >= 4, maka ambil bagian 4 string pertama
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

    public static String generateBill(String OrderID, String lokasi){
    // Cek apakah ID pesanan valid
    if (isValidOrderId(OrderID)) {
        // Jika valid, lakukan proses pembuatan tagihan
        return "Bill";
    } else {
        // Jika tidak valid, tampilkan pesan kesalahan
        return "Masukkan order yang valid";
    }
}

// Method untuk memeriksa validitas ID pesanan
private static boolean isValidOrderId(String orderId) {
    // Validasi berdasarkan aturan tertentu
    // Misalnya, Anda dapat memeriksa panjang atau format ID pesanan
    // Di sini, Anda dapat menambahkan logika validasi yang sesuai dengan kebutuhan aplikasi Anda
    return orderId.length() == 16; // Contoh sederhana: ID pesanan harus memiliki panjang 16 karakter
}

    public static void main(String[] args) {
        String selectMenu;
        String inputResto;
        String inputTp; // Use String because matcher is must use String
        String noTelp;
        String inputOrder;

        showMenu();
        System.out.print("Pilihan menu:");
        System.out.print(" ");
        while (true) {
            selectMenu = input.nextLine();
            if(selectMenu.equals("3")) {
                System.out.println("Terimakasih telah menggunakan DepeFood!");
                break;
            } else if(selectMenu.equals("1")) {
                while (true) {
                    System.out.print("Nama Restoran: ");
                    inputResto = input.nextLine();
                    if (inputResto.length() < 4) {
                        System.out.println("Nama Restoran tidak valid!");
                        continue; // Mengulangi loop untuk meminta input nama restoran lagi
                    }
    
                    System.out.print("Tanggal Pemesanan: ");
                    inputTp = input.nextLine();
                    if (!DATE_PATTERN.matcher(inputTp).matches()) {
                        System.out.println("Tanggal pemesanan dalam format DD/MM/YYYY");
                        continue; // Mengulangi loop untuk meminta input tanggal pemesanan lagi
                    }
    
                    System.out.print("No. Telpon: ");
                    noTelp = input.nextLine();
                    if (!noTelp.matches("\\d+")) { 
                        System.out.println("Harap masukkan nomor telepon dalam bentuk bilangan bulat positif!");
                        continue; // Mengulangi loop untuk meminta input nomor telepon lagi
                    }
    
                    // Jika semua input valid, keluar dari loop dan lanjutkan ke menu selanjutnya
                    break;
                }

                // Generate Order ID pesanan 
                String orderId = generateOrderID(inputResto, inputTp, noTelp);
                System.out.println("Order ID " + orderId.toUpperCase());

            // } else if (selectMenu.equals("2")) {
            //     while (true) {
            //         System.out.print("Order ID: ");
            //         inputOrder = input.nextLine();
            //         // Memanggil generateBill untuk memvalidasi ID pesanan
            //     String bill = generateBill(inputOrder, lokasi);
            //     // Jika ID pesanan valid, lanjutkan
            //     if (!bill.equals("Masukkan order yang valid")) {
            //         // Lakukan proses lain yang diperlukan
            //         break;
            //     } else {
            //         // Jika ID pesanan tidak valid, tampilkan pesan kesalahan
            //         System.out.println("Masukkan order yang valid");
            //     }
            // }
            }
        }
    }
}

