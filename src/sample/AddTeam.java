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
import javafx.stage.Stage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class AddTeam {
    public Integer newTeam(){
        GridPane gridPane = new GridPane();
        Label headerLabel = new Label("Add Team");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label nameLabel = new Label("Team Name : ");
        gridPane.add(nameLabel, 0, 2);

        // Add Email Text Field
        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1, 2);

        // Add Password Label
        Label perfLabel= new Label("Performance Type: ");

        gridPane.add(perfLabel, 0, 3);
        // Add Password Field
         TextField perfField = new TextField();
        perfField.setPrefHeight(40);
        gridPane.add(perfField, 1, 3);

        Label batchLabel= new Label("Batch Year :");
        gridPane.add(batchLabel, 0, 4);
        TextField batchField = new TextField();

        perfField.setPrefHeight(40);
        gridPane.add(batchField, 1, 4);

        Label numOfmemLabel= new Label("Team Size :");
        gridPane.add(numOfmemLabel, 0, 5);
        TextField numOfmemField = new TextField();
        gridPane.add(numOfmemField, 1, 5);

        perfField.setPrefHeight(40);
        // Add login Button

        Button addButton = new Button("Add");
        addButton.setPrefHeight(40);
        addButton.setDefaultButton(true);
        addButton.setPrefWidth(100);
        gridPane.add(addButton,1 ,6,1,1);
        GridPane.setHalignment(addButton, HPos.CENTER);
        GridPane.setMargin(addButton, new Insets(20, 10,20,0));

//         Alignment
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 170, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        Stage addWindow = new Stage();
        Scene logger = new Scene(gridPane, 500, 400);

        AtomicReference<String> TeamName= new AtomicReference<>("");
        AtomicReference<String> PerfType= new AtomicReference<>("");
        AtomicReference<String> BatchYear= new AtomicReference<>("");
        AtomicInteger TeamID = new AtomicInteger();
        AtomicInteger NumofMembers = new AtomicInteger();

        addButton.setOnAction(e->{
            TeamName.set(nameField.getText());
            PerfType.set(perfField.getText());
            BatchYear.set(batchField.getText());
            NumofMembers.set(Integer.parseInt(numOfmemField.getText()));

            try {
                TeamID.set(performADD(TeamName.get(), PerfType.get(), BatchYear.get(),NumofMembers.get()));
                setScoreTableReady(TeamID.get());
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            addWindow.close();
        });

        addWindow.setScene(logger);
        addWindow.showAndWait();

        return  TeamID.get();
    }

    private Integer performADD(String Name,String PerfType,String BatchYear, int NumofMembers) throws SQLException {
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");
        ResultSet x = doQuery.executeQuery("select count(*) from PerfInfo;");
        int TeamID = 1;
        if(x.next())
            TeamID = x.getInt(1)+1;

        while(true) {
            String query = "insert into Perfinfo values(" + TeamID + ",'" + Name +"',"+NumofMembers+"," + BatchYear + ",'" + PerfType + "',0);";
            try {
                doQuery.executeUpdate(query);
               break;
            } catch (Exception e) {
                TeamID = TeamID + 1;
               continue;
            }
        }
        return TeamID;
    }


    private static void setScoreTableReady(Integer TeamID){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "select judgeID From JudgeInfo ;";
        try {
            ResultSet judges = doQuery.executeQuery(query);
            while(judges.next()){
                Statement Q2 = con.connect("Fday");
                query = "insert into scoreTable value('"+judges.getString(1)+"',"+TeamID+ ",0);" ;
                Q2.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

