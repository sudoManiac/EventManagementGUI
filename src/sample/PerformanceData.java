package sample;

public class PerformanceData {

    private int TeamID;
    private String TeamName;
    private String PerfType;
    private int TotalScore;
    private int BatchYear;
    private int NumOfMembers;
    private int Score;


    public int getNumOfMembers() {
        return NumOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        NumOfMembers = numOfMembers;
    }

    public int getTeamID() {
        return TeamID;
    }

    public void setTeamID(int teamID) {
        TeamID = teamID;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public String getPerfType() {
        return PerfType;
    }

    public void setPerfType(String perfType) {
        PerfType = perfType;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    public void setTotalScore(int totalScore) {
        TotalScore = totalScore;
    }

    public int getBatchYear() {
        return BatchYear;
    }

    public void setBatchYear(int batchYear) {
        BatchYear = batchYear;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    PerformanceData(int teamID,String teamName,int batchYear,int numOfMembers,String perfType,int totalScore,int score) {
        TeamID=teamID;
        TeamName=teamName;
        BatchYear=batchYear;
        NumOfMembers = numOfMembers;
        PerfType=perfType;
        TotalScore=totalScore;
        Score = score;
    }


}