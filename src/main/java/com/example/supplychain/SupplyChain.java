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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    Button globalSignUp;
    Button globalLogout;
    Button backButton;
    Label customerEmail = null;
    Label notAMember = null;
    String customer= null;
    String Cname= null;

    int customerID = 0;
    int cID = 0;
    boolean loginDone = false;


    private GridPane rightHeaderBar(){

        globalLogin = new Button("Log In");
        globalLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(loginPage());
                globalLogin.setDisable(true);

            }
        });
        ButtonShadow.draw(globalLogin);

        globalSignUp = new Button("Sign Up");
        globalSignUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(signUpPage());
            }
        });
        ButtonShadow.draw(globalSignUp);

        globalLogout = new Button("Logout");

        globalLogout.setDisable(true);
        globalLogout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                customerEmail.setText("Welcome User!");
                loginDone=false;
                customer= null;
                Cname=null;
                globalLogin.setDisable(false);
                globalLogout.setDisable(true);

            }
        });
        ButtonShadow.draw(globalLogout);

        customerEmail = new Label("Welcome User!");
        customerEmail.setTextFill(Color.web("#ECF0F1"));

        notAMember = new Label("       Or");
        notAMember.setTextFill(Color.web("#ECF0F1"));

        Label space = new Label("  ");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(bodyPane.getMinWidth(), headerBar+20);
        gridPane.setAlignment(Pos.BASELINE_RIGHT);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: #4A53A2");

        gridPane.add(globalLogout,3,0);
        gridPane.add(globalLogin,2,0);
        gridPane.add(space,4,0);
        gridPane.add(customerEmail,1,0);
        gridPane.add(notAMember,2,1);
        gridPane.add(globalSignUp,3,1);


        return gridPane;
    }

    private GridPane leftHeaderBar(){
        TextField searchText = new TextField();
        Button searchButton = new Button("Search");
        ButtonShadow.draw(searchButton);
        Label message = new Label();
        message.setTextFill(Color.web("#ECF0F1"));

        Image img = new Image("cart.png");
        ImageView view = new ImageView(img);
        view.setFitHeight(15);
        view.setPreserveRatio(true);

        Button cartButton = new Button("Cart");
        cartButton.setGraphic(view);
        ButtonShadow.draw(cartButton);
        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(loginDone) {
                    message.setText("");
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getCartProducts(customerID));
                    backButton.setDisable(false);
                }
                else{
                    message.setText("Login/sign up");
                }
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String productName = searchText.getText();

                //clear body and put this new pane in the body
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getProductsByName(productName));
            }
        });

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(bodyPane.getMinWidth()-260, headerBar);
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setStyle("-fx-background-color: #4A53A2");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(cartButton,0,0);
        gridPane.add(message,1,0);
        gridPane.add(searchText,28,0);
        gridPane.add(searchButton, 29,0);

        return gridPane;
    }
    private GridPane signUpPage() {
        Label emailLabel = new Label("Email*");
        Label passwordLabel = new Label("Create Password*");
        Label first_nameLabel = new Label("First Name*");
        Label last_nameLabel = new Label("Last Name");
        Label addressLabel = new Label("Address*");
        Label mobileLabel = new Label("Phone No.*");
        Label messageLabel = new Label("");
//        Label mandatory = new Label("*");
//        mandatory.setTextFill(Color.web("#F3081A"));
//        mandatory.setAlignment(Pos.TOP_LEFT);

        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField first_nameTextField = new TextField();
        TextField last_nameTextField = new TextField();
        TextField addressTextField = new TextField();
        TextField mobileTextField = new TextField();

        Button signUpButton = new Button("Sign Up");
        ButtonShadow.draw(signUpButton);
        Button cancelButton = new Button("Cancel");
        ButtonShadow.draw(cancelButton);

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String email = emailTextField.getText();
                String password = passwordField.getText();
                String first_name = first_nameTextField.getText();
                String last_name = last_nameTextField.getText();
                String address = addressTextField.getText();
                String mobile = mobileTextField.getText();

//                password = Signup.getEncryptedPassword(password);

                if(Signup.customerSignup(email,password,first_name,last_name,address,mobile)) {
                    messageLabel.setText("Sign-In successful");
                    bodyPane.getChildren().clear();
                    bodyPane.getChildren().add(productDetails.getAllProducts());
                }
                else{
                    messageLabel.setText("Please fill all Details Carefully!");
                    passwordField.setText(null);
                }
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getAllProducts());
                globalSignUp.setDisable(false);

            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(), bodyPane.getMinHeight());
        gridPane.setVgap(5);
        gridPane.setHgap(5);
//        gridPane.setStyle("-fx-background-color: #C0C0C0");

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(emailLabel, 0,0);
        gridPane.add(emailTextField,1,0);
        gridPane.add(passwordLabel,0,1);
        gridPane.add(passwordField,1,1);
        gridPane.add(first_nameLabel,0,2);
        gridPane.add(first_nameTextField,1,2);
        gridPane.add(last_nameLabel,0,3);
        gridPane.add(last_nameTextField,1,3);
        gridPane.add(addressLabel,0,4);
        gridPane.add(addressTextField,1,4);
        gridPane.add(mobileLabel,0,5);
        gridPane.add(mobileTextField,1,5);
        gridPane.add(signUpButton,0,6);
        gridPane.add(cancelButton,1,6);
        gridPane.add(messageLabel,1,7);


        return gridPane;
    }
    private GridPane loginPage(){
        Label emailLabel = new Label("Email");
        Label passwordLabel = new Label("Password");
        Label messageLabel = new Label("");
        Label signupMessage = new Label("Not a member yet?");

        TextField emailTextField = new TextField();
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        ButtonShadow.draw(loginButton);
        Button cancelButton = new Button("Cancel");
        ButtonShadow.draw(cancelButton);
        Button signupButton = new Button("Sign Up");
        ButtonShadow.draw(signupButton);
        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(signUpPage());
                globalLogin.setDisable(false);
                globalSignUp.setDisable(true);
            }
        });
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String email = emailTextField.getText();
                String password = passwordField.getText();
                Cname = customerLogin(email);
                cID= customerID(email);

                if(login.customerLogin(email,password)) {
                    customerID = cID;
                    customer = Cname;
                    messageLabel.setText("Login successful");
                    loginDone = true;
                    globalLogout.setDisable(false);
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
        gridPane.add(signupMessage,1,4);
        gridPane.add(signupButton,1,5);

        return gridPane;
    }

    private GridPane footerBar(){
        Button addToCartButton = new Button("Add To Cart");
        ButtonShadow.draw(addToCartButton);
        Button buyNowButton = new Button("Buy Now");
        ButtonShadow.draw(buyNowButton);
        backButton = new Button("Back");
        ButtonShadow.draw(backButton);
        backButton.setDisable(true);

//        buyNowButton.setStyle("-fx-background-color: #F8875F; ");
        Label messageLabel = new Label("");

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bodyPane.getChildren().clear();
                bodyPane.getChildren().add(productDetails.getAllProducts());
                backButton.setDisable(true);
            }
        });
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

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct = productDetails.getSelectedProduct();
                if(selectedProduct == null) {
                    messageLabel.setText("Ahh! Please select the product : (");
                }
                else if(Cart.cartProduct(customer, selectedProduct)) {
                    messageLabel.setText("Product Added to Cart : )");
                }
                else  {
                    messageLabel.setText("Log-In/Sign-up to Add to Cart");
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(bodyPane.getMinWidth(), headerBar-5);
        gridPane.setVgap(5);
        gridPane.setHgap(20);
//        gridPane.setStyle("-fx-background-color: #C0C0C0");

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setTranslateY(headerBar+height+10);

        gridPane.add(backButton,0,0);
        gridPane.add(addToCartButton,1,0);
        gridPane.add(buyNowButton, 2,0);
        gridPane.add(messageLabel,3,0);

        return gridPane;
    }

    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(width,height+2*headerBar);

        bodyPane.setMinSize(width,height);
        bodyPane.setTranslateY(headerBar+10);

        bodyPane.getChildren().addAll(productDetails.getAllProducts());

        root.getChildren().addAll(rightHeaderBar(),leftHeaderBar(),bodyPane, footerBar());

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

    public int customerID(String email) {
        String query = String.format("SELECT customer_id FROM customer WHERE email = '%s'",email);

        try{
            DatabaseConnection dbCon = new DatabaseConnection();
            ResultSet rs = dbCon.getQueryTable(query);
            if(rs != null && rs.next()){
                return rs.getInt("customer_id");
            }
        }
        catch(Exception e) {
            e.getStackTrace();
        }
        return 0;
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