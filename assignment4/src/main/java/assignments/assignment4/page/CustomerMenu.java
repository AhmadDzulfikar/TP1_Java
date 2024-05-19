package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import assignments.assignment3.DepeFood;
import assignments.assignment3.Menu;
import assignments.assignment3.Order;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment3.payment.CreditCardPayment;
import assignments.assignment3.payment.DebitPayment;
import assignments.assignment4.MainApp;
import assignments.assignment4.components.BillPrinter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CustomerMenu extends MemberMenu{
    private Stage stage;
    private Scene scene;
    private Scene addOrderScene;
    private Scene printBillScene;
    private Scene payBillScene;
    private Scene cekSaldoScene;
    private BillPrinter billPrinter; // Instance of BillPrinter
    private static Label label = new Label();
    private MainApp mainApp;
    private List<Restaurant> restoList = new ArrayList<>();
    private User user;
    static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");  // Pattern for the date


    private ListView<String> menuItemListView = new ListView<>();
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private ComboBox<String> restaurantComboBoxAddOrder = new ComboBox<>();

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm();
        this.billPrinter = new BillPrinter(stage, mainApp, this.user); // Pass user to BillPrinter constructor
        this.printBillScene = createBillPrinter();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
        DepeFood.setPenggunaLoggedIn(user); // Set userLoggedIn di DepeFood
    }

    @Override
    public Scene createBaseMenu() {
        // Menampilkan menu untuk Customer

        // Create the layout for the Customer Menu
        VBox menuLayout = new VBox(30);
        menuLayout.setAlignment(Pos.BOTTOM_CENTER);

        // Create the horizontal box for buttons
        HBox buttonLayout = new HBox(0);
        menuLayout.setPadding(new Insets(0, 0, 40, 0));
        buttonLayout.setAlignment(Pos.BOTTOM_CENTER);

        // Create buttons
        Button buatPesananPage = new Button("Buat Pesanan");
        buatPesananPage.setOnAction(e -> {
            refresh();
            stage.setScene(addOrderScene);
        });

        Button cetakBillPage = new Button("Cetak Bill");
        cetakBillPage.setOnAction(e -> {
            refresh();
            stage.setScene(printBillScene);
        });

        Button bayarBillPage = new Button("Bayar Bill");
        bayarBillPage.setOnAction(e -> {
            refresh();
            stage.setScene(payBillScene);
        });

        Button cekSaldoPage = new Button("Cek Saldo");
        cekSaldoPage.setOnAction(e -> {
            refresh();
            stage.setScene(cekSaldoScene);
        });

        // Logout button
        Button backButton = new Button("Log Out");
        backButton.setOnAction(e -> mainApp.logout());

        // Add buttons to the horizontal box
        buttonLayout.getChildren().addAll(buatPesananPage, cetakBillPage, bayarBillPage, cekSaldoPage, backButton);

        // Add the button layout and logout button to the vertical layout
        menuLayout.getChildren().addAll(buttonLayout);

        // Create the scene with the layout
        // return new Scene(menuLayout, 950, 527);
        return new Scene(menuLayout, 400, 600);

    }

// FITUR KE - 1 -----------------------------------------------------------------------------------------
    private Scene createTambahPesananForm() {
        // method untuk menampilkan page tambah pesanan
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20));

        // Input Nama Restoran
        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantComboBoxAddOrder.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxAddOrder.setPromptText("Select a restaurant");

        // Membuat input tanggal order
        Label tanggalLabel = new Label("Date (DD/MM/YYYY)");
        TextField tanggalInput = new TextField();
        tanggalInput.setPromptText("DD/MM/YYYY");

        // Membuat listview untuk lihat menu
        menuItemListView = new ListView<>();
        restaurantComboBoxAddOrder.setOnAction(e -> {
            String selectedRestaurant = restaurantComboBoxAddOrder.getValue();
            if (selectedRestaurant != null) {
                Restaurant restaurant = DepeFood.getRestaurantByName(selectedRestaurant);
                if (restaurant != null) {
                    List<String> menuItems = restaurant.getMenuItems().stream()
                        .map(menu -> menu.getNamaMakanan())
                        .collect(Collectors.toList());
                    menuItemListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                    menuItemListView.setItems(FXCollections.observableArrayList(menuItems));
                }
            }
        });

        // Button buat submit orderannya
        Button submitButton = new Button("Buat Pesanan");
        submitButton.setOnAction(e -> {
            String tanggalPemesanan = tanggalInput.getText();

            if (!DATE_PATTERN.matcher(tanggalPemesanan).matches()) { // Mencocokan input tanggal pemesanan dengan pola, mereturn true jika benar dan sebaliknya, jika false turun kebawah
                // System.out.println("Masukkan tanggal sesuai format (DD/MM/YYYY) !");
                showAlert("Error", "Format Invalid", "Format tanggal harus format DD/MM/YYYY", AlertType.ERROR);
                return;
            }

            // if (!isValidDate(tanggalPemesanan)) {
            //     showAlert("Error", "Format Invalid", "Format tanggal harus format DD/MM/YYYY", AlertType.ERROR);
            //     return;
            // }

            String selectedRestaurant = restaurantComboBoxAddOrder.getValue();
            List<String> selectedItems = new ArrayList<>(menuItemListView.getSelectionModel().getSelectedItems());

            if (selectedRestaurant != null && !selectedItems.isEmpty()) {
                handleBuatPesanan(selectedRestaurant, tanggalPemesanan, selectedItems);
            } else {
                showAlert("Error", "Form Tidak Komplit", "Tolong Pilih Restoran dan menunya!", AlertType.ERROR);
            }
        });

        menuLayout.getChildren().addAll(restaurantLabel, restaurantComboBoxAddOrder, tanggalLabel, tanggalInput,menuItemListView, submitButton);

        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(scene));
        menuLayout.getChildren().add(backButton);

        return new Scene(menuLayout, 400, 600);
    }

    // private boolean isValidDate(String date) {
    //     SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    //     dateFormat.setLenient(false);
    //     try {
    //         dateFormat.parse(date);
    //         return true;
    //     } catch (ParseException e) {
    //         return false;
    //     }
    // }

// FITUR KE - 2 -----------------------------------------------------------------------------------------
    private Scene createBillPrinter() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
    
        // Field untuk input orderId
        Label orderIdTitle = new Label("Order ID:");
        TextField orderIdInput = new TextField();
        orderIdInput.setPromptText("Masukkan Order ID");

        // Button untuk submit orderId
        Button submitButton = new Button("Print Bill");
        submitButton.setOnAction(e -> {
            String orderId = orderIdInput.getText();
            Order order = DepeFood.getOrderOrNull(orderId);
    
            if (order != null) {
                showBill(order);
            } else {
                showAlert("Error", "Order Tidak Ditemukan", "Order dengan ID tersebut tidak ditemukan.", AlertType.ERROR);
            }
        });
    
        // Button untuk kembali ke page sebelumnya
        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(scene));
    
        layout.getChildren().addAll(orderIdTitle, orderIdInput, submitButton, backButton);
    
        return new Scene(layout, 400, 300);
    }
    
    private void showBill(Order order) {
        // Membuat layout untuk menampilkan bill
        VBox billLayout = new VBox(10);
        billLayout.getChildren().clear();
        billLayout.setPadding(new Insets(20));
        billLayout.setAlignment(Pos.TOP_LEFT);
    
        // Membuat label untuk setiap detail dari bill
        Label billTitle = new Label("Bill");
        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        Label tanggalPemesananLabel = new Label("Tanggal Pemesanan: " + order.getTanggal());
        Label restaurantLabel = new Label("Restaurant: " + order.getRestaurant().getNama());
        Label lokasiPengirimanLabel = new Label("Lokasi Pengiriman: " + user.getLokasi());
        Label statusPengirimanLabel = new Label("Status Pengiriman: " + (order.getOrderFinished() ? "Finished" : "Not Finished"));
    
        Label pesananTitleLabel = new Label("Pesanan:");
        VBox pesananList = new VBox();
        for (Menu item : order.getItems()) {
            Label itemLabel = new Label("- " + item.getNamaMakanan() + ": Rp " + (int)item.getHarga());
            pesananList.getChildren().add(itemLabel);
        }
    
        Label ongkirLabel = new Label("Biaya Ongkos Kirim: Rp " + order.getOngkir());
        Label totalBiayaLabel = new Label("Total Biaya: Rp " + (int)order.getTotalHarga());
    
        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(printBillScene));
        refresh();
    
        // Menambahkan semua label ke layout
        billLayout.getChildren().addAll(
                billTitle, orderIdLabel, tanggalPemesananLabel, restaurantLabel, lokasiPengirimanLabel, statusPengirimanLabel, 
                pesananTitleLabel, pesananList, ongkirLabel, totalBiayaLabel, backButton
        );
    
        Scene billScene = new Scene(billLayout, 400, 600);
        stage.setScene(billScene);
    }

// FITUR KE - 3 -----------------------------------------------------------------------------------------
private Scene createBayarBillForm() {
    VBox menuLayout = new VBox(10);
    menuLayout.setPadding(new Insets(20));
    menuLayout.setAlignment(Pos.CENTER);

    // Field untuk input orderId
    Label orderIdLabel = new Label("Order ID:");
    TextField orderIdInput = new TextField();
    orderIdInput.setPromptText("Masukkan Order ID");

    // Field untuk memilih opsi pembayaran
    Label paymentOptionLabel = new Label("Pilih Metode Pembayaran:");
    ComboBox<String> paymentOptionComboBox = new ComboBox<>();
    paymentOptionComboBox.setItems(FXCollections.observableArrayList("Credit Card", "Debit"));
    paymentOptionComboBox.setPromptText("Pilih Metode Pembayaran");

    // Button untuk submit
    Button submitButton = new Button("Bayar");
    submitButton.setOnAction(e -> {
        String orderId = orderIdInput.getText();
        String paymentOption = paymentOptionComboBox.getValue();
        if (orderId.isEmpty() || paymentOption == null) {
            showAlert("Error", "Form Tidak Komplit", "Tolong masukkan Order ID dan pilih metode pembayaran!", AlertType.ERROR);
            return;
        }

        Order order = DepeFood.getOrderOrNull(orderId);
        if (order == null) {
            showAlert("Error", "Order Tidak Ditemukan", "Order dengan ID tersebut tidak ditemukan.", AlertType.ERROR);
            return;
        }

        if (order.getOrderFinished()) {
            showAlert("Info", "Order Sudah Lunas", "Order dengan ID tersebut sudah dibayar.", AlertType.INFORMATION);
            return;
        }

        double totalBiaya = order.getTotalHarga();
        double biayaTransaksi = totalBiaya * 0.02;
        double totalPembayaran = totalBiaya + biayaTransaksi;
        
        if (paymentOption.equals("Credit Card") || paymentOption.equals("Debit")) {
                if (paymentOption.equals("Credit Card") && !(user.getPaymentSystem() instanceof CreditCardPayment) ||
                    paymentOption.equals("Debit") && !((user.getPaymentSystem() instanceof DebitPayment))) {
                    System.out.println("User belum memiliki metode pembayaran ini!");
                    showAlert("Error", "Metode pembayaran invalid", "User belum memiliki metode pembayaran ini!", AlertType.ERROR);
                    return;
                }
            }

        if (paymentOption.equals("Debit")) {
            if (totalBiaya < 50000) {
                showAlert("Error", "Jumlah Pesanan Tidak Mencukupi", "Jumlah pesanan < 50000 mohon menggunakan metode pembayaran yang lain", AlertType.ERROR);
                return;
            }
            if (user.getSaldo() < totalPembayaran) {
                showAlert("Error", "Saldo Tidak Mencukupi", "Saldo tidak mencukupi mohon menggunakan metode pembayaran yang lain", AlertType.ERROR);
                return;
            }
        }


        DepeFood.handleBayarBill(orderId, paymentOption);
        showAlert("Berhasil", "Pembayaran Berhasil", String.format("Berhasil membayar bill sebesar Rp" + (int)order.getTotalHarga() + " dengan biaya transaksi sebesar Rp " + (int)biayaTransaksi), AlertType.INFORMATION);
    });

    // Button untuk kembali ke page sebelumnya
    Button backButton = new Button("Kembali");
    backButton.setOnAction(e -> stage.setScene(scene));

    menuLayout.getChildren().addAll(orderIdLabel, orderIdInput, paymentOptionLabel, paymentOptionComboBox, submitButton, backButton);
    return new Scene(menuLayout, 400, 300);
}

// FITUR KE - 4 -----------------------------------------------------------------------------------------
    private Scene createCekSaldoScene() {
        // Method ini untuk menampilkan page cetak saldo
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(20));

        // User Label
        Label userLabel = new Label("Welcome " + user.getNama());
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        userLabel.setAlignment(Pos.CENTER);


        // Saldo Label
        Label textLabel = new Label("Saldo Anda: ");
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        textLabel.setAlignment(Pos.CENTER);

        Label saldoLabel = new Label("Rp " + user.getSaldo());
        saldoLabel.setFont(Font.font("Arial", 16));
        saldoLabel.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(scene));

        // Adding all elements to the VBox layout
        menuLayout.getChildren().addAll(userLabel, textLabel, saldoLabel, backButton);

    return new Scene(menuLayout, 950, 527);
    }

    private void handleBuatPesanan(String namaRestoran, String tanggalPemesanan, List<String> menuItems) {
        String orderId = DepeFood.handleBuatPesanan(namaRestoran, tanggalPemesanan, menuItems.size(), menuItems);
        if (orderId != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Order dengan ID " + orderId + " berhasil ditambahkan");
            System.out.println(orderId);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Gagal membuat pesanan. Coba lagi.");
            alert.showAndWait();
        }
    }

    private void handleBayarBill(String orderID, int pilihanPembayaran) {
        //TODO: Implementasi validasi pembayaran
        try {

        } catch (Exception e) {

        }
    }

    private void updateComboBoxRestaurants() {
        List<String> restaurantNames = getRestoNames();
        restaurantComboBoxAddOrder.setItems(FXCollections.observableArrayList(restaurantNames));
        restaurantComboBoxAddOrder.setPromptText("Select a restaurant");

    }

    private List<String> getRestoNames() {
        return DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());
    }

    protected void showAlert(String title, String header, String content, Alert.AlertType c) {
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // FITUR REFRESH -----------------------------------------------------------------------------------------
    protected void refresh(){
        updateComboBoxRestaurants(); // Refresh the combo box items with the latest restaurant names

        // refresh tambah pesanan
        restaurantComboBoxAddOrder.getSelectionModel().clearSelection();
        this.addOrderScene = createTambahPesananForm();

        // refresh print bill
        this.printBillScene = createBillPrinter();

        // paymentOptionComboBox.getSelectionModel().clearSelection();
        this.payBillScene = createBayarBillForm();
        this.cekSaldoScene = createCekSaldoScene();
    }
}