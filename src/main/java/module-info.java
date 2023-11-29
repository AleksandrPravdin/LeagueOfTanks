module mygames.task2_leagueoftanks {
    requires javafx.controls;
    requires javafx.fxml;


    opens mygames.task2_leagueoftanks to javafx.fxml;
    exports mygames.task2_leagueoftanks;
    exports mygames.task2_leagueoftanks.services;
    opens mygames.task2_leagueoftanks.services to javafx.fxml;
}