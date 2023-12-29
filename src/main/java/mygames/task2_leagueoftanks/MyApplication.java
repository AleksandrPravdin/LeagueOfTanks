package mygames.task2_leagueoftanks;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1532, 800);
        stage.setTitle("League of Tanks");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}