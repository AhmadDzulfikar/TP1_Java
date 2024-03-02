package assignments.assignment1;

import java.util.Scanner;
import java.util.regex.Pattern;

public class OrderGenerator { 
    private static final Scanner input = new Scanner(System.in);
    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date


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
        // String orderId = "";

        return "TP";
    }

    public static String generateBill(String OrderID, String lokasi){
        return "Bill";
    }

    public static void main(String[] args) {
        String selectMenu;
        String inputResto;
        String inputTp; // Use String because matcher is must use String
        String noTelp;

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
            }
        }
    }
}

