package sample;
import javafx.scene.control.Alert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidation {
    public Boolean validatePassword(String input) {
        if (input.length() >= 7) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Password Validation Failed");
            alert.setContentText("Password Must be atleast 7 charater long");
            alert.show();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            alert.close();
            return false;
        }
    }

   public boolean validateEmail(String input){
       
       if (input.length() >0){
           return true;
       }
       else{
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setTitle("Email Validation Failed");
           alert.setContentText("Email cannot be blank !");
           alert.show();
           try {
               Thread.sleep(4000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
           alert.close();
           return false;
       }
   }

}

