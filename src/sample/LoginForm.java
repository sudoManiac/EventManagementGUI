package sample;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.sql.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Thread.sleep;

public class LoginForm {

    public Pair<String, Boolean> doLogin() {
        GridPane gridPane = new GridPane();
        Label headerLabel = new Label("Judge Login Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

//         Add Email Label
        Label emailLabel = new Label("Email ID : ");
        gridPane.add(emailLabel, 0, 2);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 2);

//         Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 3);
        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 3);

        // Add login Button
        Button loginButton = new Button("Login");
        loginButton.setPrefHeight(40);
        loginButton.setDefaultButton(true);
        loginButton.setPrefWidth(100);
        gridPane.add(loginButton, 1,4,1,1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setMargin(loginButton, new Insets(20, 10,20,0));

        Button addUserButton = new Button("Sign Up");
        addUserButton.setOnAction(e->{
            AddUser addUser = new AddUser();
            Boolean signup = addUser.doSignup() ;

        });
        addUserButton.setPrefHeight(40);
        addUserButton.setPrefWidth(100);

        gridPane.add(addUserButton, 1, 5);
        GridPane.setHalignment(addUserButton, HPos.CENTER);
        GridPane.setMargin(addUserButton, new Insets(20, 0,20,0));

//         Alignment
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        Stage loginWindow = new Stage();
        Scene logger = new Scene(gridPane, 1000, 600);

        AtomicReference<String> Email= new AtomicReference<>("");
        AtomicReference<String> Pass= new AtomicReference<>("");
        AtomicReference<Boolean> auth = new AtomicReference<>(new Boolean(false));
        AtomicReference<Pair<String, Boolean>> AuthInfo = new AtomicReference<>(new Pair<String, Boolean>("",false));

        loginButton.setOnAction(e->{
            Email.set(emailField.getText());
            Pass.set(passwordField.getText());
            System.out.println(Email.get()+" "+Pass.get());

            auth.set(performlogin(Email.get(), Pass.get()));

            AuthInfo.set(new Pair<String, Boolean>(Email.get(), auth.get()));

            if(auth.get()==false){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Wrong Credentials");
                a.setHeaderText("Error : ");
                a.setContentText("Oops! Seems Like you Entered Wrong Credentials");
                a.show();

                try {
                    sleep(3000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                a.close();
            }
            else
                loginWindow.close();
        });

        loginWindow.setScene(logger);
        loginWindow.showAndWait();

        return  AuthInfo.get();
    }

    public  Boolean performlogin(String Email,String Pass){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "select * From JudgeInfo where JudgeID='"+Email+"';" ;

        try{
            ResultSet checkpass = doQuery.executeQuery(query);
            if(checkpass.next()) {
                if (checkpass.getString(3).equals(Pass)) {
                    System.out.println("success login!!");
                    return true;
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
