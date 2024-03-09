    package assignments.assignment1;

    import java.util.Scanner;
    import java.util.regex.Pattern;

    public class OrderGenerator { 
        private static final Scanner input = new Scanner(System.in);
        private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date

        // Mendeklarasikan Code 39 Character dengan array
        private static final char[] CODE_39_CHARACTERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'};

        public static void showMenu(){                                                  // Menampilkan output menu
            System.out.println(">>=======================================<<");
            System.out.println("|| ___                 ___             _ ||");
            System.err.println("||| . \\ ___  ___  ___ | __>___  ___  _| |||");
            System.out.println("||| | |/ ._>| . \\/ ._>| _>/ . \\/ . \\/ . |||");
            System.out.println("|||___/\\___.|  _/\\___.|_| \\___/\\___/\\___|||");
            System.out.println("||          |_|                          ||");
            System.out.println(">>=======================================<<");
            System.out.println();
            System.out.println("Pilih menu:");
            System.out.println("1. Generate Order ID");
            System.out.println("2. Generate Bill");
            System.out.println("3. Keluar");

            System.out.println("-------------------------------------");
        }

        public static void pilihMenu() {
            System.out.println("-------------------------------------");
            System.out.println("Pilih menu:");
            System.out.println("1. Generate Order ID");
            System.out.println("2. Generate Bill");
            System.out.println("3. Keluar");

            System.out.println("-------------------------------------");
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

        // Mencetak  bill
        public static String generateBill(String OrderID, String lokasi){
            lokasi = lokasi.toUpperCase();
            String harga = "0"; // Declare harga dari 0
                switch (lokasi) { 
                    case "P":
                        harga = "10.000";
                        break;
                    case "U":
                        harga = "20.000";
                        break;
                    case "T":
                        harga = "35.000";
                        break;
                    case "S":
                        harga = "40.000";
                        break;
                    case "B":
                        harga = "60.000";
                        break;
                    default:
                    System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!");
                        break;
                }
            
            // Membuat outputnya
            String date = OrderID.substring(4, 6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(8, 12);
            // String lokPengiriman = "Lokasi Pengiriman: " + lokasi;
            // String biayaOngkos = "Biaya Ongkos Kirim: Rp." + harga;
            String solution = "Bill:\n" + //
                "Order ID: " + OrderID + "\n" + //
                "Tanggal Pemesanan: " + date + "\n" + //
                "Lokasi Pengiriman: " + lokasi + "\n" + //
                "Biaya Ongkos Kirim: Rp " + harga + "\n" + //
                "";
            
            return solution;
    }

        public static void main(String[] args) {
            // Mendeklarasikan variabel
            String selectMenu;
            String inputResto;
            String inputTp; // Use String because matcher is must use String
            String noTelp;
            String inputOrder;
            String inputAlamat;

            // Menampilkan menunya dan inputan untuk pilih menu
            showMenu();
            
            while (true) {
                System.out.print("Pilihan menu:");
                System.out.print(" ");
                selectMenu = input.nextLine();
                // 3. EXIT ----------------------------------------------------------------------------------------------
                if(selectMenu.equals("3")) {    // Kalau pilih angka 3 maka akan keluar dari program
                    System.out.println("Terimakasih telah menggunakan DepeFood!");
                    break;
                // 1. GENERATE ORDER ID -------------------------------------------------------------------------------------
                } else if(selectMenu.equals("1")) { // Kalau pilih angka 1 maka akan lanjut untuk membuat Order ID
                    while (true) {
                        System.out.print("Nama Restoran: ");   
                        inputResto = input.nextLine();
                        if (inputResto.length() < 4) {
                            System.out.println("Nama Restoran tidak valid!");
                            continue; // Looping untuk meminta input nama restoran lagi
                        }
        
                        System.out.print("Tanggal Pemesanan: ");
                        inputTp = input.nextLine();
                        if (!DATE_PATTERN.matcher(inputTp).matches()) {
                            System.out.println("Tanggal pemesanan dalam format DD/MM/YYYY");
                            continue; // Looping untuk meminta input tanggal pemesanan lagi
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
                    System.out.println("Order ID " + orderId.toUpperCase() + " diterima!");
                    pilihMenu();

                    // GENERATE BILL ------------------------------------------------------------------------------------------
                } else if (selectMenu.equals("2")) {
                    while (true) {
                        System.out.print("Order ID: ");
                        inputOrder = input.nextLine();
                        if (inputOrder.length() < 16) {
                        System.out.println("Order ID minimal 16 karakter");
                        continue; // Looping untuk meminta input order ID lagi
                    }

                    String orderIdWithoutChecksum = inputOrder.substring(0, 14); // Mengambil 14 karakter pertama
                    String checksumFromInput = inputOrder.substring(14, 16); // Mengambil 2 karakter terakhir

                    // Untuk melakukan validasi checksum
                    if (!validationID(orderIdWithoutChecksum, checksumFromInput)) {
                        System.out.println("Silahkan masukkan order ID yang valid!");
                        continue; // Mengulangi loop untuk meminta input order ID lagi
                    }

                    // Input lokasi pengiriman
                    System.out.print("Lokasi pengiriman: ");
                    inputAlamat = input.nextLine().toUpperCase();   // Agar ketika input huruf kecil akan otomatis ke capslock
                    if (!inputAlamat.equals("P") && !inputAlamat.equals("U") && !inputAlamat.equals("T") && !inputAlamat.equals("S") && !inputAlamat.equals("B")) {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!");
                        continue;
                    }

                    // String bill = generateBill(orderIdWithoutChecksum, checksumFromInput);
                    String solution = generateBill(inputOrder, inputAlamat);
                    System.out.println(solution);
                    pilihMenu();
                    break;
                }
            }
        }
    }
}



