create database FDay;
use Fday;

create table PerfInfo(
			TeamID int Primary Key not NULL,
            TeamName varchar(50),
            NumOfMembers int,
			batchYear int,
			Perftype varchar(150),
			TotalScore int
			);
create table JudgeInfo (
			JudgeID varchar(100) primary key ,
            JudgeName varchar(50),
			Pass varchar(50)
            );

create table scoreTable(
			JudgeID varchar(100),    
			TeamID INT ,
            Score INT,
            Primary Key(JudgeID,TeamID),
            Foreign Key (TeamID) references perfINfo(TeamID) on delete cascade,
            Foreign Key (judgeID) references judgeiNfo(judgeID) on delete cascade
		);
