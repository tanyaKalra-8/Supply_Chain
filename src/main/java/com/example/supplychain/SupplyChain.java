package com.example.supplychain;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;

public class SupplyChain extends Application {

    public static final int width=700,height=600,headerBar=50;

    public Pane bodyPane = new Pane();
    Login login = new Login();

    ProductDetails productDetails = new ProductDetails();
    Button globalLogin;
    Label customerEmail = null;
    Label notAMember = null;
    Label signUp = null;
    String customer = null;

    private GridPane headerBar(){
        TextField searchText = new TextField();
        Button searchButton = new Button("Search");

        Image img = new Image("cart.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(15);
        view.setPreserveRatio(true);

        Button cartButton = new Button("Cart");
        cartButton.setGraphic(view);
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String productName = searchText.getText();

                //clear body and put this new pane in the body
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getProductsByName(productName));
            }
        });
        globalLogin = new Button("Log In");
        globalLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
                globalLogin.setDisable(true);

            }
        });

        customerEmail = new Label("Welcome User!");
        customerEmail.setTextFill(Color.web("#ECF0F1"));

        notAMember = new Label("Not a Member?");
        notAMember.setTextFill(Color.web("#ECF0F1"));

        signUp = new Label("Sign-Up Now!");
        signUp.setTextFill(Color.web("#ECF0F1"));

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(bodyPane.getMinWidth()-260, headerBar);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: #4A53A2");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(cartButton,0,0);
        gridPane.add(searchText,29,0);
        gridPane.add(searchButton, 30,0);
//        gridPane.add(globalLogin,55,0);
//        gridPane.add(customerEmail,54,0);
//        gridPane.add(notAMember,54,1);
//        gridPane.add(signUp,54,2);

        return gridPane;
    }
    private GridPane loginPage(){
        Label emailLabel = new Label("Email");
        Label passwordLabel = new Label("Password");
        Label messageLabel = new Label("");

        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        Button cancelButton = new Button("Cancel");
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String email = emailTextField.getText();
                String password = passwordField.getText();
                String name = customerLogin(email);
//                messageLabel.setText(email + " $$ " + password);

                if(login.customerLogin(email,password)) {
                    customer = name;
                    messageLabel.setText("Login successful");
                    globalLogin.setDisable(true);

                    customerEmail.setText("Welcome " + customer + "!");
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getAllProducts());
                }
                else{
                    messageLabel.setText("Incorrect Email/Password");
                    globalLogin.setDisable(false);
                    passwordField.setText(null);
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getAllProducts());
                globalLogin.setDisable(false);

            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(), bodyPane.getMinHeight());
        gridPane.setVgap(5);
        gridPane.setHgap(5);
//        gridPane.setStyle("-fx-background-color: #C0C0C0");

        gridPane.setAlignment(Pos.CENTER);
        // first is x, second is y
        gridPane.add(emailLabel, 0,0);
        gridPane.add(emailTextField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton,0,2);
        gridPane.add(cancelButton,1,2);
        gridPane.add(messageLabel,1,3);

        return gridPane;
    }

    private GridPane footerBar(){
        Button addToCartButton = new Button("Add To Cart");
        Button buyNowButton = new Button("Buy Now");
//        buyNowButton.setStyle("-fx-background-color: #F8875F; ");
        Label messageLabel = new Label("");
        buyNowButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct = productDetails.getSelectedProduct();
                if(selectedProduct == null) {
                    messageLabel.setText("Ahh! Please select the product : (");
                }
                else if(Order.placeOrder(customer, selectedProduct)) {
                    messageLabel.setText("Order placed! Thanks for shopping with us : )");
                }
                else  {
                    messageLabel.setText("Log-In/Sign-up to place order");
                }
            }
        });


        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(), headerBar-10);
        gridPane.setVgap(5);
        gridPane.setHgap(20);
//        gridPane.setStyle("-fx-background-color: #C0C0C0");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(headerBar+height+5);

        gridPane.add(addToCartButton,0,0);
        gridPane.add(buyNowButton, 1,0);
        gridPane.add(messageLabel,2,0);

        return gridPane;
    }

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(width,height+2*headerBar);

        bodyPane.setMinSize(width,height);
        bodyPane.setTranslateY(headerBar);

        bodyPane.getChildren().addAll(productDetails.getAllProducts());

        root.getChildren().addAll(headerBar(),bodyPane, footerBar());

        return root;
    }

    public String customerLogin(String email) {
        String query = String.format("SELECT first_name FROM customer WHERE email = '%s'",email);

        try{
            DatabaseConnection dbCon = new DatabaseConnection();
            ResultSet rs = dbCon.getQueryTable(query);
            if(rs != null && rs.next()){
                return rs.getString("first_name");
            }
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return null;
    }
    @Override
    public void start(Stage stage) throws IOException {
// FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());
        stage.getIcons().add(new Image("Logo.png"));
        stage.setTitle("Mini Amazon!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}