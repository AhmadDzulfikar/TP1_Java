package assignments.assignment4.components.form;

import assignments.assignment3.DepeFood;
import assignments.assignment3.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import assignments.assignment4.MainApp;
import assignments.assignment4.page.AdminMenu;
import assignments.assignment4.page.CustomerMenu;

import java.io.FileInputStream;
import java.util.function.Consumer;

public class LoginForm {
    private Stage stage;
    private MainApp mainApp; // MainApp instance
    private TextField nameInput;
    private TextField phoneInput;
    private User userLoggedIn;

    public LoginForm(Stage stage, MainApp mainApp) { // Pass MainApp instance to constructor
        this.stage = stage;
        this.mainApp = mainApp; // Store MainApp instance
        this.stage.setResizable(false);
    }

    private Scene createLoginForm() {
        // Implementasi method menampilkan komponen form login
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25, 25, 25, 50));

        String currentPath = System.getProperty("user.dir");
        String imageUrl = "file:" + currentPath + "\\src\\main\\java\\assignments\\assignment4\\images\\LoginPageSatu.png";
        setBackground(vbox, imageUrl);

        // Username
        HBox nameBox = new HBox(30);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        nameInput = new TextField(); 
        nameInput.setPromptText("Username");
        nameInput.setPrefSize(300, 40); // Set size for TextField
        nameInput.setFont(Font.font("Arial", FontWeight.NORMAL, 16)); // Set font size for TextField
        nameInput.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        nameBox.getChildren().addAll(nameInput);

        // No Telp
        HBox phoneBox = new HBox(30);
        phoneBox.setAlignment(Pos.CENTER_LEFT);
        phoneInput = new TextField();
        phoneInput.setPromptText("No. Telp");
        phoneInput.setPrefSize(300, 40); // Set size for TextField
        phoneInput.setFont(Font.font("Arial", FontWeight.NORMAL, 16)); // Set font size for TextField
        phoneInput.setStyle("-fx-background-radius: 10; -fx-border-radius: 10;");
        phoneBox.getChildren().addAll(phoneInput);

        // Button Login
        HBox loginBox = new HBox(30);
        loginBox.setAlignment(Pos.CENTER_LEFT);
        Button loginButton = new Button("Login");
        loginButton.setPrefSize(300, 40); // Set size for Button
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 16)); // Set font size for Button
        loginButton.setStyle("-fx-background-color: #FDBD98; -fx-background-radius: 10; -fx-border-radius: 10; -fx-text-fill: white;");
        loginBox.getChildren().addAll(loginButton);
        loginButton.setOnAction(e -> handleLogin());

        // Add all components to vbox
        vbox.getChildren().addAll(nameBox, phoneBox, loginBox);

        return new Scene(vbox, 940, 527);
    }

    public void setBackground(Pane pane, String imageUrl) {
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                                                            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        pane.setBackground(background);
    }

    private void handleLogin() {
        // Implementasi validasi isian form login
        String username = nameInput.getText();
        String phone = phoneInput.getText();
        userLoggedIn = DepeFood.getUser(username, phone);

        // Validasi untuk kolom kosong
        if (username.isEmpty() || phone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in both username and phone number");
            alert.showAndWait();
            return;
        } 

        // Kalau user loginnya salah (gaada di list user)
        if (userLoggedIn == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("User Not Found!");
            alert.showAndWait();
            return;
        } else {
            if (userLoggedIn.getRole().equalsIgnoreCase("Admin")) {
                AdminMenu adminMenu = new AdminMenu(stage, mainApp, userLoggedIn);
                Scene scene = adminMenu.createBaseMenu();
                MainApp.addScene("CustomerScene", scene);
                MainApp.setScene(scene);
            } else {
                CustomerMenu customerMenu = new CustomerMenu(stage, mainApp, userLoggedIn);
                Scene scene = customerMenu.createBaseMenu();
                MainApp.addScene("CustomerScene", scene);
                MainApp.setScene(scene);
            }
            clearTextFields();
        }
    }

    private void clearTextFields() {
        nameInput.clear();
        phoneInput.clear();
    }

    public Scene getScene(){
        return this.createLoginForm();
    }
}

