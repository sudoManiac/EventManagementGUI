package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import java.sql.*;


public class ScoreBoard {
    public void display(String JudgeID) throws SQLException {


        /// forming table columns

        TableView<PerformanceData> table = new TableView<>();

        TableColumn<PerformanceData, Integer> teamIDColumn = new TableColumn<>("Team ID");
        teamIDColumn.setMinWidth(30);
        teamIDColumn.setCellValueFactory(new PropertyValueFactory<>("teamID"));

        TableColumn<PerformanceData, String> teamNameColumn = new TableColumn<>("Team Name");
        teamNameColumn.setMinWidth(80);
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        TableColumn<PerformanceData, Integer> batchYearColumn = new TableColumn<>("Batch Year");
        batchYearColumn.setMinWidth(30);
        batchYearColumn.setCellValueFactory(new PropertyValueFactory<>("batchYear"));

        TableColumn<PerformanceData, Integer> numOfMemColumn = new TableColumn<>("Team Size");
        numOfMemColumn.setMinWidth(30);
        numOfMemColumn.setCellValueFactory(new PropertyValueFactory<>("numOfMembers"));

        TableColumn<PerformanceData, String> perfTypeColumn = new TableColumn<>("Performance Type");
        perfTypeColumn.setMinWidth(90);
        perfTypeColumn.setCellValueFactory(new PropertyValueFactory<>("perfType"));

        TableColumn<PerformanceData, Integer> totalScoreColumn = new TableColumn<>("Total Score");
        totalScoreColumn.setMinWidth(30);
        totalScoreColumn.setCellValueFactory(new PropertyValueFactory<>("totalScore"));

        TableColumn<PerformanceData, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setMinWidth(20);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));


        /////
        ///// EDITABLE OPTION ADDED
        /////
        ////

        //////// Score editable
        scoreColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        scoreColumn.setOnEditCommit(e -> {
            if(e.getNewValue()!=null) {
                int index = e.getTablePosition().getRow();
                e.getTableView().getItems().get(index).setScore(e.getNewValue());
                int teamid = e.getTableView().getItems().get(index).getTeamID();

                int newScore = e.getNewValue();
                editScore(JudgeID, teamid, newScore);

                try {
                    updateTotalScore();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                PerformanceData team = null;
                try {
                    team = getPerfInfo(teamid);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                e.getTableView().getItems().get(index).setTotalScore(team.getTotalScore());
                table.refresh();
            }
        });

        //////// team name editable

        teamNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        teamNameColumn.setOnEditCommit(e -> {
            if(e.getNewValue()!=null) {
                int index = e.getTablePosition().getRow();
                e.getTableView().getItems().get(index).setTeamName(e.getNewValue());
                int teamid = e.getTableView().getItems().get(index).getTeamID();
                String newName = e.getNewValue();
                editTeamName(teamid, newName);

                e.getTableView().getItems().get(index).setTeamName(newName);
                table.refresh();
            }
        });

        //////// Batch Year EDITABLE
        batchYearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        batchYearColumn.setOnEditCommit(e -> {
            if(e.getNewValue()!=null) {
                int index = e.getTablePosition().getRow();
                e.getTableView().getItems().get(index).setBatchYear(e.getNewValue());
                int newBatchYear = e.getNewValue();
                int teamid = e.getTableView().getItems().get(index).getTeamID();


                editBatchYear(teamid, newBatchYear);

                e.getTableView().getItems().get(index).setBatchYear(newBatchYear);
                table.refresh();
            }
        });

//////// Team members EDITABLE
        numOfMemColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numOfMemColumn.setOnEditCommit(e -> {
            if(e.getNewValue()!=null) {
                int index = e.getTablePosition().getRow();
                e.getTableView().getItems().get(index).setNumOfMembers(e.getNewValue());
                int newSize = e.getNewValue();
                int teamid = e.getTableView().getItems().get(index).getTeamID();

                editNumofMem(teamid, newSize);

                e.getTableView().getItems().get(index).setNumOfMembers(newSize);
                table.refresh();
            }
        });


        perfTypeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        perfTypeColumn.setOnEditCommit(e -> {
            if(e.getNewValue()!=null) {
                int index = e.getTablePosition().getRow();
                e.getTableView().getItems().get(index).setPerfType(e.getNewValue());
                String newPerf = e.getNewValue();
                int teamid = e.getTableView().getItems().get(index).getTeamID();

                editPerfType(teamid, newPerf);

                e.getTableView().getItems().get(index).setPerfType(newPerf);
                table.refresh();
            }
        });
        ////// editable zone ends



        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(true);

        ObservableList<PerformanceData> teams = getDataList(JudgeID);

        table.setItems(teams);

        table.getColumns().addAll(teamIDColumn, teamNameColumn, batchYearColumn,numOfMemColumn, perfTypeColumn, totalScoreColumn, scoreColumn);
        table.setPrefSize(800, 500);
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Button addTeamButton = new Button("Add Team");
        addTeamButton.setOnAction(e->{
            AddTeam addTeam = new AddTeam();
            int TeamID = addTeam.newTeam();

            try {
                updateTotalScore();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            try {
                table.getItems().add(getPerfInfo(TeamID));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        });


        Button deleteTeamButton = new Button("Delete Team");

        deleteTeamButton.setOnAction(e->{
            ObservableList<PerformanceData> teamSelected,allTeams;
            allTeams = table.getItems();
            teamSelected = table.getSelectionModel().getSelectedItems();
            for (int i=0;i< teamSelected.size();i++ ) {
                int teamID = teamSelected.get(i).getTeamID();
                deleteTeam(teamID);
                System.out.println(teamID);
            }
            teamSelected.forEach(allTeams::remove);

            try {
                updateTotalScore();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

//        Label userinfo = new Label("Hello "+ JudgeID);
//
//        GridPane.setConstraints(userinfo, 1, 1);
        GridPane.setConstraints(table, 1, 2);
        GridPane.setConstraints(addTeamButton, 1, 3);
        GridPane.setConstraints(deleteTeamButton, 1, 4);
        grid.getChildren().addAll(table,addTeamButton,deleteTeamButton);

        ColumnConstraints columnOneConstraints = new ColumnConstraints(250, 300, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        Scene tab = new Scene(grid, 800, 600);
        Stage window = new Stage();
        window.setScene(tab);
        window.showAndWait();
    }

    private ObservableList<PerformanceData> getDataList(String JudgeID) throws SQLException {
        ObservableList<PerformanceData> perfData = FXCollections.observableArrayList();

        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");
        String query = "select perfInfo.TeamID,TeamName,BatchYear,NumOfMembers,PerfType,TotalScore,Score from perfInfo Left JOIN ScoreTable ON perfInfo.TeamID=ScoreTable.TeamID where JudgeID='" + JudgeID + "';";
        ResultSet tabledata = null;

        tabledata = doQuery.executeQuery(query);
        while(tabledata.next()){
            PerformanceData team = new PerformanceData(tabledata.getInt(1), tabledata.getString(2), tabledata.getInt(3),tabledata.getInt(4), tabledata.getString(5), tabledata.getInt(6), tabledata.getInt(7));

            perfData.add(team);
        }
        con.end();

        return perfData;
    }

    private void editScore(String JudgeID,int TeamID,int newScore){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "update scoreTable set score = " + newScore +" where TeamID = " + TeamID + " && JudgeID = '" +JudgeID + "';" ;
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.end();
    }

    private void editBatchYear(int TeamID,int newYear){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "update perfInfo set BatchYear = " + newYear +" where TeamID = " + TeamID + ";" ;
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.end();
    }

    private void editNumofMem(int TeamID,int newYear){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "update perfInfo set NumofMembers = " + newYear +" where TeamID = " + TeamID + ";" ;
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.end();
    }

    private void editTeamName(int TeamID,String newName){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "update perfInfo set TeamName = '" + newName +"' where TeamID = " + TeamID + ";" ;
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.end();
    }
    private void editPerfType(int TeamID,String newName){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        String query = "update perfInfo set perfType = '" + newName +"' where TeamID = " + TeamID + ";" ;
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.end();
    }
    private  void updateTotalScore() throws SQLException{
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");

        ResultSet teamIDs = doQuery.executeQuery("select TeamID from PerfInfo;");
        while (teamIDs.next()) {
            Statement tempQuery = con.connect("Fday");
            ResultSet x = tempQuery.executeQuery("select SUM(score) from scoreTable where TeamID = " + teamIDs.getInt(1) + ";") ;

            int Tscore = 0;
            if (x.next())
                Tscore = x.getInt(1);

            Statement tempQ2 = con.connect("Fday");
            String query = "update PerfInfo set TotalScore ="+Tscore+" where TeamID = " + teamIDs.getInt(1) + ";" ;
            tempQ2.executeUpdate(query);
        }
    }

    private void deleteTeam(Integer TeamID){
        ConnectDB con = new ConnectDB();
        Statement doQuery = con.connect("Fday");
        String query = "delete from scoreTable where TeamID = "+TeamID+";";
        try {
            doQuery.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Statement doQueryMain = con.connect("Fday");
        String queryMain = "delete from PerfInfo where TeamID = "+TeamID+";";
        try {
            doQueryMain.executeUpdate(queryMain);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PerformanceData getPerfInfo(Integer TeamID) throws SQLException{
        ConnectDB con = new ConnectDB();
        Statement doQuery =  con.connect("Fday");
        ResultSet x = doQuery.executeQuery("select * from PerfInfo where TeamID ="+TeamID+";");
        PerformanceData team = null;
        if(x.next())
            team = new PerformanceData(x.getInt(1),x.getString(2),x.getInt(3),x.getInt(4),x.getString(5),x.getInt(6),0);
        return team;
    }
}