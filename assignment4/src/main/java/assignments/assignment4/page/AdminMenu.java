package assignments.assignment4.page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import assignments.assignment3.DepeFood;
import assignments.assignment3.Restaurant;
import assignments.assignment3.User;
import assignments.assignment4.MainApp;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminMenu extends MemberMenu {
    private Stage stage;
    private Scene scene;
    private User user;
    private Scene addRestaurantScene;
    private Scene addMenuScene;
    private Scene viewRestaurantsScene;
    private MainApp mainApp;
    private ListView<String> menuItemsListView = new ListView<>();

    // Define dua comboBox 
    private ComboBox<String> restaurantComboBoxAddMenu = new ComboBox<>();  // Menampilkan daftar nama restoran saat ingin membuat resto
    private ComboBox<String> restaurantComboBoxViewRestaurants = new ComboBox<>(); // Menampilkan daftar nama restoran saat ingin membuat view menu

    public AdminMenu(Stage stage, MainApp mainApp, User user) {
        this.stage = stage;
        this.mainApp = mainApp;
        this.user = user;
        this.scene = createBaseMenu();
        this.addRestaurantScene = createAddRestaurantForm(new Stage());
        this.addMenuScene = createAddMenuForm(new Stage());
        this.viewRestaurantsScene = createViewRestaurantsForm(new Stage());
    }

    public Scene createBaseMenu() {
        // Menampilkan menu untuk Customer

        // Create the layout for the Customer Menu
        VBox menuLayout = new VBox(30);
        menuLayout.setAlignment(Pos.BOTTOM_CENTER);

        // Create the horizontal box for the welcome label
        HBox topLayout = new HBox();
        topLayout.setPadding(new Insets(20));
        topLayout.setAlignment(Pos.TOP_LEFT);

        // Create the horizontal box for buttons
        HBox buttonLayout = new HBox(50);
        menuLayout.setPadding(new Insets(0, 20, 30, 20));
        buttonLayout.setAlignment(Pos.BOTTOM_CENTER);

        // Create welcome label
        Label welcomeLabel = new Label("Welcome " + user.getNama());
        welcomeLabel.setTextFill(Color.WHITE);
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        String currentPath = System.getProperty("user.dir");

        String imageUrl = "file:" + currentPath + "\\src\\main\\java\\assignments\\assignment4\\images\\MainMenuAdmin.png";
        setBackground(menuLayout, imageUrl);

        // Create buttons
        Button buatPesananPage = new Button("TAMBAH RESTO");
        buatPesananPage.setPrefSize(200, 30);
        buatPesananPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        buatPesananPage.setOnAction(e -> {
            refresh();
            // stage.setScene(addRestaurantScene);
            showCreateAddRestaurantDialog();
        });

        Button cetakBillPage = new Button("TAMBAH MENU RESTO");
        cetakBillPage.setPrefSize(200, 30);
        cetakBillPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        cetakBillPage.setOnAction(e -> {
            refresh();
            // stage.setScene(addMenuScene);
            showCreateAddMenuDialog();
        });

        Button bayarBillPage = new Button("LIHAT DAFTAR RESTO");
        bayarBillPage.setPrefSize(200, 30);
        bayarBillPage.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        bayarBillPage.setOnAction(e -> {
            refresh();
            // stage.setScene(viewRestaurantsScene);
            showCreateViewRestaurantsDialog();
        });

        // Logout button
        Button backButton = new Button("LOG OUT");
        backButton.setPrefSize(200, 30);
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold;");
        backButton.setOnAction(e -> showLogoutDialog());
        // backButton.setOnAction(e -> showLogoutDialog());

        // Add buttons to the horizontal box
        buttonLayout.getChildren().addAll(buatPesananPage, cetakBillPage, bayarBillPage, backButton);

        // Add welcome label to the top layout
        topLayout.getChildren().add(welcomeLabel);

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
private Scene createAddRestaurantForm(Stage dialogStage) {
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(20));  // Padding untuk textField
    layout.setStyle("-fx-background-color: #092C5B;");

    // TextField untuk input nama restaurant
    Label nameLabel = new Label("Restaurant Name: ");
    nameLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
    TextField nameField = new TextField();
    nameField.setPromptText("Ex. Holycow!");

    // Button submit restaurant
    Button sButton = new Button("Submit");
    sButton.setPrefSize(800, 0); // Atur tinggi dan lebar tombol
    sButton.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white;");
    sButton.setOnAction(e -> {
        String restaurantName = nameField.getText().trim();
        if (!restaurantName.isEmpty()) {
            handleTambahRestoran(restaurantName);
            nameField.clear();  // This line can be removed as refresh() will handle the clearing
            refresh();
        } else {
            showAlert("Warning", "Warning", "Nama restoran tidak boleh kosong!", Alert.AlertType.WARNING);
        }
    });

    layout.getChildren().addAll(nameLabel, nameField, sButton);

    // Button kembali ke halaman sebelumnya
    Button backButton = new Button("Kembali");
    backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
    backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
    backButton.setOnAction(e -> dialogStage.close());
    layout.getChildren().add(backButton);

    return new Scene(layout, 400, 600);
}

    private void showCreateAddRestaurantDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
        Scene dialogScene = createAddRestaurantForm(dialog);
        dialog.setScene(dialogScene);
        dialog.setTitle("Buat Pesanan");
        dialog.showAndWait();
    }

// FITUR KE - 2 -----------------------------------------------------------------------------------------
    // Method untuk membuat menu 
    private Scene createAddMenuForm(Stage dialogStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: #092C5B;");
        layout.setPadding(new Insets(20));

        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxAddMenu.setPromptText("Select Restaurant");
        restaurantComboBoxAddMenu.setPrefWidth(800); // Atur lebar ComboBox

        // Input Menu
        Label menuLabel = new Label("Menu Item Name: ");
        menuLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        TextField nameField = new TextField();
        nameField.setPromptText("Ex. Tenderloin Steak");

        // Input Harga
        Label priceLabel = new Label("Price: ");
        priceLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        TextField priceField = new TextField();
        priceField.setPromptText("Ex. 300000");

        // Save menu & harga yang dimasukkan
        Button sButton = new Button("Add Menu Item");
        sButton.setPrefSize(800, 0); // Atur tinggi dan lebar tombol
        sButton.setStyle("-fx-background-color: #3F90AE; -fx-text-fill: white;");
        sButton.setOnAction(e -> {
            String restaurantName = restaurantComboBoxAddMenu.getValue();
            String itemName = nameField.getText().trim();
            String priceText = priceField.getText().trim();

            // Validasi jika kosong
            if (restaurantName == null || itemName.isEmpty() || priceText.isEmpty()) {
                showAlert("Warning", "Warning", "Mohon lengkapi semua field!", Alert.AlertType.WARNING);
                return;
            }

            // Validasi angka yang diinput bukan angka
            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                showAlert("Warning", "Warning", "Harga harus berupa angka!", Alert.AlertType.WARNING);
                return;
            }

            handleTambahMenuRestoran(restaurantName, itemName, price);
            nameField.clear();
            priceField.clear();
        });

        layout.getChildren().addAll(restaurantLabel, restaurantComboBoxAddMenu, menuLabel, nameField, priceLabel, priceField, sButton);

        Button backButton = new Button("Kembali");
        backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> dialogStage.close());
        layout.getChildren().add(backButton);

        return new Scene(layout, 400, 600);
    }

    private void showCreateAddMenuDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
        Scene dialogScene = createAddMenuForm(dialog);
        dialog.setScene(dialogScene);
        dialog.setTitle("Tambah menu restoran");
        dialog.showAndWait();
    }

// FITUR KE - 3 -----------------------------------------------------------------------------------------
    // Method untuk membuat view restaurant menunya
    private Scene createViewRestaurantsForm(Stage dialogStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #092C5B;");

        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantLabel.setTextFill(Color.WHITE); // Atur warna teks menjadi putih
        restaurantComboBoxViewRestaurants.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxViewRestaurants.setPromptText("Select Restaurant");
        restaurantComboBoxViewRestaurants.setPrefWidth(800); // Atur lebar ComboBox

        menuItemsListView = new ListView<>();

        restaurantComboBoxViewRestaurants.setOnAction(e -> {
            String selectedRestaurant = restaurantComboBoxViewRestaurants.getValue();
            if (selectedRestaurant != null) {
                Restaurant restaurant = DepeFood.getRestaurantByName(selectedRestaurant); // Mengembalikkan daftar object restaurant
                // mengubah daftar menjadi daftar nama restoran.
                if (restaurant != null) {
                    List<String> menuItems = restaurant.getMenuItems().stream()
                            .map(menu -> menu.getNamaMakanan() + " - " + menu.getHarga())
                            .collect(Collectors.toList());
                    menuItemsListView.setItems(FXCollections.observableArrayList(menuItems));
                }
            }
        });

        layout.getChildren().addAll(restaurantLabel, restaurantComboBoxViewRestaurants, menuItemsListView);

        Button backButton = new Button("Kembali");
        backButton.setPrefSize(100, 0); // Atur tinggi dan lebar tombol
        backButton.setStyle("-fx-background-color: #FDBD98; -fx-text-fill: white;"); // Set warna latar belakang dan teks tombol
        backButton.setOnAction(e -> dialogStage.close());
        layout.getChildren().add(backButton);

        return new Scene(layout, 400, 600);
    }

    private void showCreateViewRestaurantsDialog() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(stage);
        Scene dialogScene = createViewRestaurantsForm(dialog);
        dialog.setScene(dialogScene);
        dialog.setTitle("View");
        dialog.showAndWait();
    }

// FITUR UNTUK HANDLE-HANDLE -----------------------------------------------------------------------------------------
    private void handleTambahRestoran(String nama) {
        String validName = DepeFood.getValidRestaurantName(nama);
        if (!validName.startsWith("Restoran dengan nama")) {
            DepeFood.handleTambahRestoran(validName);
            showAlert("Message", "Message", "Restaurant berhasil teregistrasi!", Alert.AlertType.INFORMATION);
            refresh();
            updateComboBoxRestaurants();
        } else {
            showAlert("Error", "Error", "Restaurant yang anda masukkan sudah ada, tolong buatlah yang baru.", Alert.AlertType.ERROR);
        }
    }

    private void handleTambahMenuRestoran(String restaurantName, String itemName, double price) {
        Restaurant restaurant = DepeFood.getRestaurantByName(restaurantName);
        if (restaurant == null) {
            showAlert("Error", "Error", "Restoran tidak ditemukan!", Alert.AlertType.ERROR);
            return;
        }
    
        // Pengecekan apakah nama makanan sudah ada di restoran
        boolean itemExists = restaurant.getMenuItems().stream()
                .anyMatch(menu -> menu.getNamaMakanan().equalsIgnoreCase(itemName));
        
        if (itemExists) {
            showAlert("Warning", "Warning", "Nama makanan sudah ada di restoran ini!", Alert.AlertType.WARNING);
            return;
        }
    
        // Jika nama makanan belum ada, tambahkan menu baru
        DepeFood.handleTambahMenuRestoran(restaurant, itemName, price);
        showAlert("Information", "Information", "Menu berhasil ditambahkan ke " + restaurantName, Alert.AlertType.INFORMATION);
        refresh();
    }
    

    private void updateComboBoxRestaurants() {
        List<String> restaurantNames = getRestoNames(); // mendapatkan daftar nama restoran yang tersedia.
        restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(restaurantNames));
        // restaurantComboBoxAddMenu.setPromptText("Pilih Restaurant");

        restaurantComboBoxViewRestaurants.setItems(FXCollections.observableArrayList(restaurantNames));
        restaurantComboBoxViewRestaurants.setPromptText("Pilih Restaurant");
    }

    private List<String> getRestoNames() {
        return DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());  // mengunmpulkan hasil dari operasi map menjadi List<String>.
    }

// FITUR SHOW ALERT -----------------------------------------------------------------------------------------
    protected void showAlert(String title, String header, String content, Alert.AlertType c) {
        Alert alert = new Alert(c);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

// POP UP LOG OUT -----------------------------------------------------------------------------------------
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

        // Clear selections and lists to ensure fresh data is shown when the scene is set
        restaurantComboBoxAddMenu.getSelectionModel().clearSelection();
        restaurantComboBoxViewRestaurants.getSelectionModel().clearSelection();
        menuItemsListView.getItems().clear();

        this.addRestaurantScene = createAddRestaurantForm(new Stage());
        this.viewRestaurantsScene = createViewRestaurantsForm(new Stage());
    }
}
