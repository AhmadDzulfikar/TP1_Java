// package assignments.assignment4.page;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import assignments.assignment3.DepeFood;
// import assignments.assignment3.Restaurant;
// import assignments.assignment3.User;
// import assignments.assignment4.MainApp;
// import javafx.collections.FXCollections;
// import javafx.geometry.Insets;
// import javafx.geometry.Pos;
// import javafx.scene.Scene;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.ComboBox;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.control.TextField;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.VBox;
// import javafx.scene.text.Font;
// import javafx.scene.text.FontWeight;
// import javafx.stage.Stage;

// public class AdminMenu extends MemberMenu {
//     private Stage stage;
//     private Scene scene;
//     private User user;
//     private Scene addRestaurantScene;
//     private Scene addMenuScene;
//     private Scene viewRestaurantsScene;
//     private MainApp mainApp;
//     private ListView<String> menuItemsListView = new ListView<>();

//     // Define dua comboBox 
//     private ComboBox<String> restaurantComboBoxAddMenu = new ComboBox<>();  // Menampilkan daftar nama restoran saat ingin membuat resto
//     private ComboBox<String> restaurantComboBoxViewRestaurants = new ComboBox<>(); // Menampilkan daftar nama restoran saat ingin membuat view menu

//     public AdminMenu(Stage stage, MainApp mainApp, User user) {
//         this.stage = stage;
//         this.mainApp = mainApp;
//         this.user = user;
//         this.scene = createBaseMenu();
//         this.addRestaurantScene = createAddRestaurantForm();
//         this.addMenuScene = createAddMenuForm();
//         this.viewRestaurantsScene = createViewRestaurantsForm();
//     }

//     // Halaman utama sebagai mainMenu dari page page lainnya
//     @Override
//     public Scene createBaseMenu() {
//         GridPane grid = new GridPane();
//         grid.setAlignment(Pos.CENTER);
//         grid.setHgap(10);
//         grid.setVgap(10);
//         grid.setPadding(new Insets(25, 25, 25, 25));

//         VBox menuLayout = new VBox(10);
//         Label welcomeLabel = new Label("Welcome " + user.getNama());
//         welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//         welcomeLabel.setAlignment(Pos.CENTER);
//         grid.add(welcomeLabel, 0, 0, 2, 1);

//         Button addRestaurantButton = new Button("Tambah Restaurant");
//         addRestaurantButton.setOnAction(e -> stage.setScene(addRestaurantScene));
//         menuLayout.getChildren().add(addRestaurantButton);

//         Button addMenuButton = new Button("Tambah Menu Restaurant");
//         addMenuButton.setOnAction(e -> stage.setScene(addMenuScene));
//         menuLayout.getChildren().add(addMenuButton);

//         Button viewRestaurantButton = new Button("Lihat Daftar Restaurant");
//         viewRestaurantButton.setOnAction(e -> stage.setScene(viewRestaurantsScene));
//         menuLayout.getChildren().add(viewRestaurantButton);

//         Button backButton = new Button("Log Out");
//         backButton.setOnAction(e -> mainApp.logout());
//         menuLayout.getChildren().add(backButton);

//         menuLayout.setAlignment(Pos.CENTER);
//         grid.add(menuLayout, 0, 1);

//         return new Scene(grid, 400, 600);
//     }

//     private Scene createAddRestaurantForm() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new Insets(20));  // Padding untuk textField

//         // TextField untuk input nama restaurant
//         Label nameLabel = new Label("Restaurant Name: ");
//         TextField nameField = new TextField();
//         nameField.setPromptText("Ex. Holycow!");

//         // Button submit restaurant
//         Button sButton = new Button("Submit");
//         sButton.setOnAction(e -> {
//             String restaurantName = nameField.getText().trim();
//             if (!restaurantName.isEmpty()) {
//                 handleTambahRestoran(restaurantName);
//                 nameField.clear();
//             } else {
//                 showAlert("Warning", "Warning", "Nama restoran tidak boleh kosong!", Alert.AlertType.WARNING);
//             }
//         });

//         layout.getChildren().addAll(nameLabel, nameField, sButton);

//         // Button kembali ke halaman sebelumnya
//         Button backButton = new Button("Kembali");
//         backButton.setOnAction(e -> stage.setScene(scene));
//         layout.getChildren().add(backButton);

//         return new Scene(layout, 400, 600);
//     }

//     // Method untuk membuat menu 
//     private Scene createAddMenuForm() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new Insets(20));

//         Label restaurantLabel = new Label("Restaurant Name: ");
//         restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(getRestoNames()));
//         restaurantComboBoxAddMenu.setPromptText("Select Restaurant");

//         // Input Menu
//         Label nameLabel = new Label("Menu Item Name: ");
//         TextField nameField = new TextField();
//         nameField.setPromptText("Ex. Tenderloin Steak");

//         // Input Harga
//         Label priceLabel = new Label("Price: ");
//         TextField priceField = new TextField();
//         priceField.setPromptText("Ex. 300000");

//         // Save menu & harga yang dimasukkan
//         Button sButton = new Button("Add Menu Item");
//         sButton.setOnAction(e -> {
//             String restaurantName = restaurantComboBoxAddMenu.getValue();
//             String itemName = nameField.getText().trim();
//             String priceText = priceField.getText().trim();

//             // Validasi jika kosong
//             if (restaurantName == null || itemName.isEmpty() || priceText.isEmpty()) {
//                 showAlert("Warning", "Warning", "Mohon lengkapi semua field!", Alert.AlertType.WARNING);
//                 return;
//             }

//             // Validasi angka yang diinput bukan angka
//             double price;
//             try {
//                 price = Double.parseDouble(priceText);
//             } catch (NumberFormatException ex) {
//                 showAlert("Warning", "Warning", "Harga harus berupa angka!", Alert.AlertType.WARNING);
//                 return;
//             }

//             handleTambahMenuRestoran(restaurantName, itemName, price);
//             nameField.clear();
//             priceField.clear();
//         });

//         layout.getChildren().addAll(restaurantLabel, restaurantComboBoxAddMenu, nameLabel, nameField, priceLabel, priceField, sButton);

//         Button backButton = new Button("Kembali");
//         backButton.setOnAction(e -> stage.setScene(scene));
//         layout.getChildren().add(backButton);

//         return new Scene(layout, 400, 600);
//     }

//     // Method untuk membuat view restaurant menunya
//     private Scene createViewRestaurantsForm() {
//         VBox layout = new VBox(10);
//         layout.setPadding(new Insets(20));

//         Label restaurantLabel = new Label("Restaurant Name: ");
//         restaurantComboBoxViewRestaurants.setItems(FXCollections.observableArrayList(getRestoNames()));
//         restaurantComboBoxViewRestaurants.setPromptText("Select Restaurant");

//         menuItemsListView = new ListView<>();

//         restaurantComboBoxViewRestaurants.setOnAction(e -> {
//             String selectedRestaurant = restaurantComboBoxViewRestaurants.getValue();
//             if (selectedRestaurant != null) {
//                 Restaurant restaurant = DepeFood.getRestaurantByName(selectedRestaurant); // Mengembalikkan daftar object restaurant
//                 // mengubah daftar menjadi daftar nama restoran.
//                 if (restaurant != null) {
//                     List<String> menuItems = restaurant.getMenuItems().stream()
//                             .map(menu -> menu.getNamaMakanan() + " - " + menu.getHarga())
//                             .collect(Collectors.toList());
//                     menuItemsListView.setItems(FXCollections.observableArrayList(menuItems));
//                 }
//             }
//         });

//         layout.getChildren().addAll(restaurantLabel, restaurantComboBoxViewRestaurants, menuItemsListView);

//         Button backButton = new Button("Kembali");
//         backButton.setOnAction(e -> stage.setScene(scene));
//         layout.getChildren().add(backButton);

//         return new Scene(layout, 400, 600);
//     }

//     private void handleTambahRestoran(String nama) {
//         String validName = DepeFood.getValidRestaurantName(nama);
//         if (!validName.startsWith("Restoran dengan nama")) {
//             DepeFood.handleTambahRestoran(validName);
//             showAlert("Message", "Message", "Restaurant berhasil teregistrasi!", Alert.AlertType.INFORMATION);
//             updateComboBoxRestaurants();
//         } else {
//             showAlert("Error", "Error", "Restaurant yang anda masukkan sudah ada, tolong buatlah yang baru.", Alert.AlertType.ERROR);
//         }
//     }

//     private void handleTambahMenuRestoran(String restaurantName, String itemName, double price) {
//         Restaurant restaurant = DepeFood.getRestaurantByName(restaurantName);
//         if (restaurant == null) {
//             showAlert("Error", "Error", "Restoran tidak ditemukan!", Alert.AlertType.ERROR);
//             return;
//         }

//         DepeFood.handleTambahMenuRestoran(restaurant, itemName, price);
//         showAlert("Information", "Information", "Menu berhasil ditambahkan ke " + restaurantName, Alert.AlertType.INFORMATION);
//     }

//     private void updateComboBoxRestaurants() {
//         List<String> restaurantNames = getRestoNames(); // mendapatkan daftar nama restoran yang tersedia.
//         restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(restaurantNames));
//         restaurantComboBoxAddMenu.setPromptText("Pilih Restaurant");

//         restaurantComboBoxViewRestaurants.setItems(FXCollections.observableArrayList(restaurantNames));
//         restaurantComboBoxViewRestaurants.setPromptText("Pilih Restaurant");
//     }

//     private List<String> getRestoNames() {
//         return DepeFood.getRestoList().stream().map(Restaurant::getNama).collect(Collectors.toList());  // mengunmpulkan hasil dari operasi map menjadi List<String>.
//     }

//     protected void showAlert(String title, String header, String content, Alert.AlertType c) {
//         Alert alert = new Alert(c);
//         alert.setTitle(title);
//         alert.setHeaderText(header);
//         alert.setContentText(content);
//         alert.showAndWait();
//     }

//     protected void refresh(){
//         //TODO: Implemenetasi method ini untuk merefresh data yang dimiliki aplikasi
//         // Hint: Method ini digunakan pada *seluruh method* yang membutuhkan update
//     }
// }

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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        this.addRestaurantScene = createAddRestaurantForm();
        this.addMenuScene = createAddMenuForm();
        this.viewRestaurantsScene = createViewRestaurantsForm();
    }

    // Halaman utama sebagai mainMenu dari page page lainnya
    @Override
    public Scene createBaseMenu() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        VBox menuLayout = new VBox(10);
        Label welcomeLabel = new Label("Welcome " + user.getNama());
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcomeLabel.setAlignment(Pos.CENTER);
        grid.add(welcomeLabel, 0, 0, 2, 1);

        Button addRestaurantButton = new Button("Tambah Restaurant");
        addRestaurantButton.setOnAction(e -> {
            refresh();
            stage.setScene(addRestaurantScene);
        });
        menuLayout.getChildren().add(addRestaurantButton);

        Button addMenuButton = new Button("Tambah Menu Restaurant");
        addMenuButton.setOnAction(e -> {
            refresh();
            stage.setScene(addMenuScene);
        });
        menuLayout.getChildren().add(addMenuButton);

        Button viewRestaurantButton = new Button("Lihat Daftar Restaurant");
        viewRestaurantButton.setOnAction(e -> {
            refresh();
            stage.setScene(viewRestaurantsScene);
        });
        menuLayout.getChildren().add(viewRestaurantButton);

        Button backButton = new Button("Log Out");
        backButton.setOnAction(e -> mainApp.logout());
        menuLayout.getChildren().add(backButton);

        menuLayout.setAlignment(Pos.CENTER);
        grid.add(menuLayout, 0, 1);

        return new Scene(grid, 400, 600);
    }

// FITUR KE - 1 -----------------------------------------------------------------------------------------
    private Scene createAddRestaurantForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));  // Padding untuk textField

        // TextField untuk input nama restaurant
        Label nameLabel = new Label("Restaurant Name: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Ex. Holycow!");

        // Button submit restaurant
        Button sButton = new Button("Submit");
        sButton.setOnAction(e -> {
            String restaurantName = nameField.getText().trim();
            if (!restaurantName.isEmpty()) {
                handleTambahRestoran(restaurantName);
                nameField.clear();
            } else {
                showAlert("Warning", "Warning", "Nama restoran tidak boleh kosong!", Alert.AlertType.WARNING);
            }
        });

        layout.getChildren().addAll(nameLabel, nameField, sButton);

        // Button kembali ke halaman sebelumnya
        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(scene));
        layout.getChildren().add(backButton);

        return new Scene(layout, 400, 600);
    }

// FITUR KE - 2 -----------------------------------------------------------------------------------------
    // Method untuk membuat menu 
    private Scene createAddMenuForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxAddMenu.setPromptText("Select Restaurant");

        // Input Menu
        Label nameLabel = new Label("Menu Item Name: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Ex. Tenderloin Steak");

        // Input Harga
        Label priceLabel = new Label("Price: ");
        TextField priceField = new TextField();
        priceField.setPromptText("Ex. 300000");

        // Save menu & harga yang dimasukkan
        Button sButton = new Button("Add Menu Item");
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

        layout.getChildren().addAll(restaurantLabel, restaurantComboBoxAddMenu, nameLabel, nameField, priceLabel, priceField, sButton);

        Button backButton = new Button("Kembali");
        backButton.setOnAction(e -> stage.setScene(scene));
        layout.getChildren().add(backButton);

        return new Scene(layout, 400, 600);
    }

// FITUR KE - 3 -----------------------------------------------------------------------------------------
    // Method untuk membuat view restaurant menunya
    private Scene createViewRestaurantsForm() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        Label restaurantLabel = new Label("Restaurant Name: ");
        restaurantComboBoxViewRestaurants.setItems(FXCollections.observableArrayList(getRestoNames()));
        restaurantComboBoxViewRestaurants.setPromptText("Select Restaurant");

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
        backButton.setOnAction(e -> stage.setScene(scene));
        layout.getChildren().add(backButton);

        return new Scene(layout, 400, 600);
    }

// FITUR UNTUK HANDLE-HANDLE -----------------------------------------------------------------------------------------
    private void handleTambahRestoran(String nama) {
        String validName = DepeFood.getValidRestaurantName(nama);
        if (!validName.startsWith("Restoran dengan nama")) {
            DepeFood.handleTambahRestoran(validName);
            showAlert("Message", "Message", "Restaurant berhasil teregistrasi!", Alert.AlertType.INFORMATION);
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

        DepeFood.handleTambahMenuRestoran(restaurant, itemName, price);
        showAlert("Information", "Information", "Menu berhasil ditambahkan ke " + restaurantName, Alert.AlertType.INFORMATION);
        refresh();
    }

    private void updateComboBoxRestaurants() {
        List<String> restaurantNames = getRestoNames(); // mendapatkan daftar nama restoran yang tersedia.
        restaurantComboBoxAddMenu.setItems(FXCollections.observableArrayList(restaurantNames));
        restaurantComboBoxAddMenu.setPromptText("Pilih Restaurant");

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

// FITUR REFRESH -----------------------------------------------------------------------------------------
    protected void refresh(){
        updateComboBoxRestaurants(); // Refresh the combo box items with the latest restaurant names

        // Clear selections and lists to ensure fresh data is shown when the scene is set
        restaurantComboBoxAddMenu.getSelectionModel().clearSelection();
        restaurantComboBoxViewRestaurants.getSelectionModel().clearSelection();
        menuItemsListView.getItems().clear();
    }
}
