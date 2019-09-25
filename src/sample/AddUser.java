package sample;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AddUser {

    public boolean doSignup() {
        GridPane gridPane = new GridPane();
        Label headerLabel = new Label("Judge Login Form");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        GridPane.setConstraints(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Add name Label
        Label nameLabel = new Label("Username : ");
        gridPane.add(nameLabel, 0, 2);

        // Add name Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1, 2);

        // Email Label
        Label emailLabel = new Label("Email ID : ");
        gridPane.add(emailLabel, 0, 3);

        // Add Email Text Field
        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 3);


        // Add Password Label
        Label passwordLabel = new Label("Password : ");
        gridPane.add(passwordLabel, 0, 4);

        // Add Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 4);



        // Add login Button
        Stage SignupWindow = new Stage();
        Button signupButton = new Button("Sign Up");
        signupButton.setDefaultButton(true);
        AtomicBoolean success = new AtomicBoolean(false);
        AtomicReference<String> Pass = new AtomicReference<>(passwordField.getText());
        AtomicReference<String> UserName = new AtomicReference<>(nameField.getText());
        AtomicReference<String> Email = new AtomicReference<>(emailField.getText());
        signupButton.setOnAction(e->{
            UserName.set(nameField.getText());
            
//          System.out.println("INput is ::::"+Email+" "+UserName+" "+ Pass);
            FormValidation formValidation = new FormValidation();
            Boolean emailValidate = false;
            if(formValidation.validateEmail(emailField.getText()) ){
                Email.set(emailField.getText());
                emailValidate = true;
            }
           
            if(emailValidate && formValidation.validatePassword(passwordField.getText())) {
                Pass.set(passwordField.getText());
                success.set(performSignup(UserName.get(), Email.get(), Pass.get()));

                if(success.get()==true) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setContentText("User : " + Email.get() + " Added Successfully!");
                    alert.show();
                    try {
                    Thread.sleep(4000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    alert.close();
                }
                setScoreTableReady(Email.get());
                SignupWindow.close();
            }

        });

        gridPane.add(signupButton, 1, 5, 1, 1);
        GridPane.setHalignment(signupButton, HPos.CENTER);
        GridPane.setMargin(signupButton, new Insets(20, 0,20,0));

        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(100, 100, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        Scene signup = new Scene(gridPane);

        SignupWindow.initModality(Modality.APPLICATION_MODAL);

        SignupWindow.setTitle("Judge Signup");
        SignupWindow.setScene(signup);
        SignupWindow.showAndWait();

        return success.get();
    }


    private boolean performSignup(String Name, String Email, String Pass) {
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "insert into JudgeInfo values('" + Email + "','" + Name + "','" + Pass + "');" ;

        try {
            doQuery.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setScoreTableReady(String judgeID){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "select TeamID From PerfInfo ;";
        try {
            ResultSet teams = doQuery.executeQuery(query);
            while(teams.next()){
                Statement Q2 = con.connect("Fday");
                query = "insert into scoreTable values('"+judgeID+"',"+teams.getInt(1)+ ",0);";
                Q2.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}