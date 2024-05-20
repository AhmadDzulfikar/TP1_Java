package assignments.assignment4.page;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
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

    private Stage printBillDialogStage;

    private ListView<String> menuItemListView = new ListView<>();
    private ComboBox<String> restaurantComboBox = new ComboBox<>();
    private ComboBox<String> restaurantComboBoxAddOrder = new ComboBox<>();

    public CustomerMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user; // Store the user
        this.scene = createBaseMenu();
        this.addOrderScene = createTambahPesananForm(new Stage());
        this.billPrinter = new BillPrinter(stage, mainApp, this.user); // Pass user to BillPrinter constructor
        this.printBillScene = createBillPrinter(new Stage());
        this.payBillScene = createBayarBillForm(new Stage());
        this.cekSaldoScene = createCekSaldoScene(new Stage());
        DepeFood.setPenggunaLoggedIn(user); // Set userLoggedIn di DepeFood
    }

    @Override
    public Scene createBaseMenu() {
        // Menampilkan menu untuk Customer

        // Create the layout for the Customer Menu
        VBox menuLayout = new VBox(30);
        menuLayout.setAlignment(Pos.BOTTOM_CENTER);

        // Create the horizontal box for buttons
        HBox buttonLayout = new HBox(50);
        menuLayout.setPadding(new Insets(0, 20, 30, 20));
        buttonLayout.setAlignment(Pos.BOTTOM_CENTER);

        String currentPath = System.getProperty("user.dir");

        String imageUrl = "file:" + currentPath + "\\src\\main\\java\\assignments\\assignment4\\images\\MainMenuDua.png";
        setBackground(menuLayout, imageUrl);

        // Create buttons
        Button buatPesananPage = new Button("BUAT PESANAN");
        buatPesananPage.setPrefSize(200, 30);
        buatPesananPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        buatPesananPage.setOnAction(e -> {
            refresh();
            // stage.setScene(addOrderScene);
            showTambahPesananDialog();
        });

        Button cetakBillPage = new Button("CETAK BILL");
        cetakBillPage.setPrefSize(200, 30);
        cetakBillPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        cetakBillPage.setOnAction(e -> {
            refresh();
            // stage.setScene(printBillScene);
            showTcreateBillPrinterDialog();
        });

        Button bayarBillPage = new Button("BAYAR BILL");
        bayarBillPage.setPrefSize(200, 30);
        bayarBillPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        bayarBillPage.setOnAction(e -> {
            refresh();
            // stage.setScene(payBillScene);
            showccreateBayarBillFormDialog();
        });

        Button cekSaldoPage = new Button("CEK SALDO");
        cekSaldoPage.setPrefSize(200, 30);
        cekSaldoPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        cekSaldoPage.setOnAction(e -> {
            refresh();
            // stage.setScene(cekSaldoScene);
            showCreateCekSaldoSceneDialog();
        });

        // Logout button
        Button backButton = new Button("LOG OUT");
        backButton.setPrefSize(200, 30);
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        // backButton.setOnAction(e -> mainApp.logout());
        backButton.setOnAction(e -> showLogoutDialog());

        // Add buttons to the horizontal box
        buttonLayout.getChildren().addAll(buatPesananPage, cetakBillPage, bayarBillPage, cekSaldoPage, backButton);

        // Add the button layout and logout button to the vertical layout
        menuLayout.getChildren().addAll(buttonLayout);

        // Create the scene with the layout
        // return new Scene(menuLayout, 950, 527);
        return new Scene(menuLayout, 940, 527);

    }

    public void setBackground(Pane pane, String imageUrl) {
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                                                            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);
    }

// FITUR KE - 1 -----------------------------------------------------------------------------------------
    private Scene createTambahPesananForm(Stage dialogStage) {
        // method untuk menampilkan page tambah pesanan
        VBox menuLayout = new VBox(10);
        menuLayout.setPadding(new Insets(20));
        menuLayout.setStyle("-fx-background-color: #092C5B;");

        // Input Nama Restoran
        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        restaurantComboBoxAddOrder.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxAddOrder.setPromptText("Select a restaurant");
        restaurantComboBoxAddOrder.setPrefWidth(800); // Atur lebar ComboBox

        // Membuat input tanggal order
        Label tanggalLabel = new Label("Date (DD/MM/YYYY)");
        tanggalLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
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
        submitButton.setPrefSize(800, 40); // Atur tinggi dan lebar tombol
        submitButton.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white;");
        submitButton.setOnAction(e -> {
            String tanggalPemesanan = tanggalInput.getText();

            if (!DATE_PATTERN.matcher(tanggalPemesanan).matches()) { // Mencocokan input tanggal pemesanan dengan pola, mereturn true jika benar dan sebaliknya, jika false turun kebawah
                showAlert("Error", "Format Invalid", "Format tanggal harus format DD/MM/YYYY", AlertType.ERROR);
                return;
            }
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
        backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> dialogStage.close());
        menuLayout.getChildren().add(backButton);

        return new Scene(menuLayout, 400, 600);
    }

    private void showTambahPesananDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
        Scene dialogScene = createTambahPesananForm(dialog);
        dialog.setScene(dialogScene);
        dialog.setTitle("Buat Pesanan");
        dialog.showAndWait();
    }

// FITUR KE - 2 -----------------------------------------------------------------------------------------
    private Scene createBillPrinter(Stage dialogStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #092C5B;");
        layout.setAlignment(Pos.CENTER_LEFT);
    
        // Field untuk input orderId
        Label orderIdTitle = new Label("Order ID:");
        orderIdTitle.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        TextField orderIdInput = new TextField();
        orderIdInput.setPromptText("Masukkan Order ID");

        // Button untuk submit orderId
        Button submitButton = new Button("Print Bill");
        submitButton.setPrefSize(200, 0); // Atur tinggi dan lebar tombol
        submitButton.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px;");
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
        backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> dialogStage.close());
        layout.getChildren().addAll(orderIdTitle, orderIdInput, submitButton, backButton);
    
        return new Scene(layout, 400, 600);
    }

    private void showTcreateBillPrinterDialog() {
        printBillDialogStage = new Stage();
        printBillDialogStage.initModality(Modality.WINDOW_MODAL);
        printBillDialogStage.initOwner(stage);
        Scene dialogScene = createBillPrinter(printBillDialogStage);
        printBillDialogStage.setScene(dialogScene);
        printBillDialogStage.setTitle("Cetak Bill");
        printBillDialogStage.showAndWait();
    }
    
    private void showBill(Order order) {
        // Membuat layout untuk menampilkan bill
        VBox billLayout = new VBox(10);
        billLayout.getChildren().clear();
        billLayout.setPadding(new Insets(20));
        billLayout.setAlignment(Pos.CENTER);
        billLayout.setStyle("-fx-background-color: #092C5B;");
    
        // Membuat label untuk setiap detail dari bill
        Label billTitle = new Label("Bill");
        billTitle.setTextFill(Color.WHITE); // Set warna teks menjadi putih
        Label orderIdLabel = new Label("Order ID: " + order.getOrderId());
        orderIdLabel.setTextFill(Color.WHITE);
        Label tanggalPemesananLabel = new Label("Tanggal Pemesanan: " + order.getTanggal());
        tanggalPemesananLabel.setTextFill(Color.WHITE);
        Label restaurantLabel = new Label("Restaurant: " + order.getRestaurant().getNama());
        restaurantLabel.setTextFill(Color.WHITE);
        Label lokasiPengirimanLabel = new Label("Lokasi Pengiriman: " + user.getLokasi());
        lokasiPengirimanLabel.setTextFill(Color.WHITE);
        Label statusPengirimanLabel = new Label("Status Pengiriman: " + (order.getOrderFinished() ? "Finished" : "Not Finished"));
        statusPengirimanLabel.setTextFill(Color.WHITE);
    
        Label pesananTitleLabel = new Label("Pesanan:");
        pesananTitleLabel.setTextFill(Color.WHITE);
        VBox pesananList = new VBox();
        pesananList.setAlignment(Pos.CENTER);
        for (Menu item : order.getItems()) {
            Label itemLabel = new Label("- " + item.getNamaMakanan() + ": Rp " + (int)item.getHarga());
            itemLabel.setTextFill(Color.WHITE);
            pesananList.getChildren().add(itemLabel);
        }
    
        Label ongkirLabel = new Label("Biaya Ongkos Kirim: Rp " + order.getOngkir());
        ongkirLabel.setTextFill(Color.WHITE);
        Label totalBiayaLabel = new Label("Total Biaya: Rp " + (int)order.getTotalHarga());
        totalBiayaLabel.setTextFill(Color.WHITE);
    
        Button backButton = new Button("Kembali");
        backButton.setPrefSize(800, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> stage.setScene(scene));
        backButton.setTextFill(Color.WHITE); // Set warna teks menjadi putih
        refresh();
    
        // Menambahkan semua label ke layout
        billLayout.getChildren().addAll(
                billTitle, orderIdLabel, tanggalPemesananLabel, restaurantLabel, lokasiPengirimanLabel, statusPengirimanLabel, 
                pesananTitleLabel, pesananList, ongkirLabel, totalBiayaLabel, backButton
        );
    
        Scene billScene = new Scene(billLayout, 400, 600);
        stage.setScene(billScene);
        printBillDialogStage.close();
    }

// FITUR KE - 3 -----------------------------------------------------------------------------------------
private Scene createBayarBillForm(Stage dialogStage) {
    VBox menuLayout = new VBox(10);
    menuLayout.setPadding(new Insets(20));
    menuLayout.setAlignment(Pos.CENTER_LEFT);
    menuLayout.setStyle("-fx-background-color: #092C5B;");

    // Field untuk input orderId
    Label orderIdLabel = new Label("Order ID:");
    orderIdLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
    TextField orderIdInput = new TextField();
    orderIdInput.setPromptText("Masukkan Order ID");

    // Field untuk memilih opsi pembayaran
    Label paymentOptionLabel = new Label("Pilih Metode Pembayaran:");
    paymentOptionLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
    ComboBox<String> paymentOptionComboBox = new ComboBox<>();
    paymentOptionComboBox.setItems(FXCollections.observableArrayList("Credit Card", "Debit"));
    paymentOptionComboBox.setPromptText("Pilih Metode Pembayaran");
    paymentOptionComboBox.setPrefWidth(800); // Atur lebar ComboBox

    // Button untuk submit
    Button submitButton = new Button("Bayar");
    submitButton.setPrefSize(800, 0); // Atur tinggi dan lebar tombol
    submitButton.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white;");
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
    backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
    backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
    backButton.setOnAction(e -> dialogStage.close());

    menuLayout.getChildren().addAll(orderIdLabel, orderIdInput, paymentOptionLabel, paymentOptionComboBox, submitButton, backButton);
    return new Scene(menuLayout, 400, 600);
}

private void showccreateBayarBillFormDialog() {
    printBillDialogStage = new Stage();
    printBillDialogStage.initModality(Modality.WINDOW_MODAL);
    printBillDialogStage.initOwner(stage);
    Scene dialogScene = createBayarBillForm(printBillDialogStage);
    printBillDialogStage.setScene(dialogScene);
    printBillDialogStage.setTitle("Cetak Bill");
    printBillDialogStage.showAndWait();
}

// FITUR KE - 4 -----------------------------------------------------------------------------------------
    private Scene createCekSaldoScene(Stage dialogStage) {
        // Method ini untuk menampilkan page cetak saldo
        VBox menuLayout = new VBox(10);
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setPadding(new Insets(20));
        menuLayout.setStyle("-fx-background-color: #092C5B;");

        // User Label
        Label userLabel = new Label("Welcome " + user.getNama());
        userLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        userLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        userLabel.setAlignment(Pos.CENTER);

        // Saldo Label
        Label textLabel = new Label("Saldo Anda: ");
        textLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        textLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        textLabel.setAlignment(Pos.CENTER);

        // Format saldo dengan titik sebagai pemisah ribuan
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        String formattedSaldo = numberFormat.format(user.getSaldo());

        Label saldoLabel = new Label("Rp " + formattedSaldo);
        saldoLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        saldoLabel.setFont(Font.font("Arial", 20));
        saldoLabel.setAlignment(Pos.CENTER);

        // Back button
        Button backButton = new Button("Kembali");
        backButton.setPrefSize(800, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> dialogStage.close());

        // Adding all elements to the VBox layout
        menuLayout.getChildren().addAll(userLabel, textLabel, saldoLabel, backButton);

    return new Scene(menuLayout, 400, 600);
    }

    private void showCreateCekSaldoSceneDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
        Scene dialogScene = createCekSaldoScene(dialog);
        dialog.setScene(dialogScene);
        dialog.setTitle("Buat Pesanan");
        dialog.showAndWait();
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

    private void showLogoutDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Apakah anda ingin keluar?");
        alert.setContentText("Tekan OK untuk logout, atau Cancel untuk stay.");

        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(okButton, cancelButton);

        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                DepeFood.setPenggunaLoggedIn(null); // Clear logged in user
                mainApp.logout(); // Show the login page
            }
        });
    }

    // FITUR REFRESH -----------------------------------------------------------------------------------------
    protected void refresh(){
        updateComboBoxRestaurants(); // Refresh the combo box items with the latest restaurant names

        // refresh tambah pesanan
        restaurantComboBoxAddOrder.getSelectionModel().clearSelection();
        this.addOrderScene = createTambahPesananForm(new Stage());

        // refresh print bill
        this.printBillScene = createBillPrinter(new Stage());

        // paymentOptionComboBox.getSelectionModel().clearSelection();
        this.payBillScene = createBayarBillForm(new Stage());
        this.cekSaldoScene = createCekSaldoScene(new Stage());
    }
}