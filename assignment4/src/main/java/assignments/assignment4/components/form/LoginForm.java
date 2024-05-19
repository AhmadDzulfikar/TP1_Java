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
import javafx.scene.layout.Pane;
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
    }

    private Scene createLoginForm() {
        // Implementasi method menampilkan komponen form login
        GridPane grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        String currentPath = System.getProperty("user.dir");

        String imageUrl = "file:" + currentPath + "\\src\\main\\java\\assignments\\assignment4\\images\\haiyya2.png";
        setBackground(grid, imageUrl);

        Label welcomeLabel = new Label("Welcome to DepeFood");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20)); // Set font size and style
        grid.add(welcomeLabel, 0, 0, 2, 1); // Spanning two columns

        // Username
        Label namLabel = new Label("Username: ");
        grid.add(namLabel, 0, 1);

        nameInput = new TextField(); 
        nameInput.setPromptText("Ex. Thomas N");
        grid.add(nameInput, 1, 1);

        // No Telp
        Label phonLabel = new Label("Phone: ");
        grid.add(phonLabel, 0, 2);

        phoneInput = new TextField();
        phoneInput.setPromptText("Ex. 9928765403");
        grid.add(phoneInput, 1, 2);

        // Button Login
        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> handleLogin());
        grid.add(loginButton, 1, 3);

        return new Scene(grid, 400, 600);
    }

    public void setBackground(Pane grid, String imageUrl) {
        Image image = new Image(imageUrl);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, 
                                                            BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        grid.setBackground(background);
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

        }
    }

    public Scene getScene(){
        return this.createLoginForm();
    }
}

