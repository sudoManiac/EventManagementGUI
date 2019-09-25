package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.sql.*;

public class Main extends Application {
    public static void main(String[] args) { launch(args); }
    Stage window;
    Scene login , marking;
    @Override
    public void start(Stage primaryStage) throws Exception{

        ConnectDB con = new ConnectDB();
        Statement doQuery= con.connect("Fday");

        window = primaryStage;

        LoginForm loginForm = new LoginForm();
        Pair<String,Boolean> Auth = loginForm.doLogin();

        if(Auth.getValue()) {
            ScoreBoard scoreBoard = new ScoreBoard();
            System.out.println(Auth.getKey());
            scoreBoard.display(Auth.getKey());
        }
    }

}
