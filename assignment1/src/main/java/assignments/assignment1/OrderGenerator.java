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
            System.out.println("1. Generate Order ID");
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

        public static String pilihMenu() {
            System.out.println("-------------------------------------");
            System.out.println();
            System.out.println("Pilih menu:");
            System.out.println("1. Generate Order ID");
            System.out.println("2. Generate Bill");
            System.out.println("3. Keluar");

            System.out.println("-------------------------------------");

            // int menu = Integer.parseInt(input.nextLine());
            String menu = input.nextLine();

            return menu;
        }


        public static boolean validationID(String orderIdWithoutChecksum, String checksumFromInput) {
            // Menghitung checksum
            String checksum = calculateChecksum(orderIdWithoutChecksum);
        
            // Mendapatkan checksum dari hasil perhitungan
            String calculatedChecksum = checksum.substring(checksum.length() - 2);
        
            // Bandingkan checksum dari input dengan checksum yang dihitung
            return calculatedChecksum.equals(checksumFromInput);
        }

        public static String generateBill(String OrderID, String lokasi){
            String harga = "0";
                switch (lokasi.toUpperCase()) {
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

            String bill = "Bill: \n";
            bill += String.format("Order ID: %s \n", OrderID);

            String date = "Tanggal Pemesanan: " + OrderID.substring(4, 6) + "/" + OrderID.substring(6, 8) + "/" + OrderID.substring(8, 12) + "\n";
            bill += date;

            String lokPengiriman = "Lokasi Pengiriman: " + lokasi + '\n';
            bill += lokPengiriman;

            String biayaOngkos = "Biaya Ongkos Kirim: Rp." + harga;
            bill += biayaOngkos;
            
            return bill;
    }

        public static void main(String[] args) {
            String selectMenu;
            String inputResto;
            String inputTp; // Use String because matcher is must use String
            String noTelp;
            String inputOrder;
            String inputAlamat;

            showMenu();
            System.out.print("Pilihan menu:");
            System.out.print(" ");
            selectMenu = input.nextLine();

            while (true) {
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
                    System.out.println("Order ID " + orderId.toUpperCase() + " diterima!");

                    pilihMenu();

                } else if (selectMenu.equals("2")) {
                    while (true) {
                        System.out.print("Order ID: ");
                        inputOrder = input.nextLine();
                        if (inputOrder.length() < 16) {
                        System.out.println("Order ID minimal 16 karakter");
                        continue; // Mengulangi loop untuk meminta input order ID lagi
                    }

                    String orderIdWithoutChecksum = inputOrder.substring(0, 14); // Ambil 14 karakter pertama
                    String checksumFromInput = inputOrder.substring(14, 16); // Ambil 2 karakter terakhir

                    // Lakukan validasi checksum
                    if (!validationID(orderIdWithoutChecksum, checksumFromInput)) {
                        System.out.println("Silahkan masukkan order ID yang valid!");
                        continue; // Mengulangi loop untuk meminta input order ID lagi
                    }

                    System.out.print("Lokasi pengiriman: ");
                    inputAlamat = input.nextLine().toUpperCase();
                    if (!inputAlamat.equals("P") && !inputAlamat.equals("U") && !inputAlamat.equals("T") && !inputAlamat.equals("S") && !inputAlamat.equals("B")) {
                        System.out.println("Harap masukkan lokasi pengiriman yang ada pada jangkauan!");
                        continue;
                    }

                    // String bill = generateBill(orderIdWithoutChecksum, checksumFromInput);
                    String bill = generateBill(inputOrder, inputAlamat);
                    System.out.println(bill);
                }

            }
        }
    }
    }



