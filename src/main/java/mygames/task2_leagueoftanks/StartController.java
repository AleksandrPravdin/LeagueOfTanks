package mygames.task2_leagueoftanks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class StartController {
    @FXML
    AnchorPane anchorPaneStart;
    @FXML
    Button startGameB;
    @FXML
    Button endGameB;

    @FXML
    private void initialize() throws FileNotFoundException {
        anchorPaneStart.setStyle("-fx-base: #b6e7c9;");

        startGameB.setText("Начать игру");
        startGameB.setStyle("-fx-font: 35 arial; -fx-base: #b6e7c9;");
        startGameB.setMinWidth(300);
        startGameB.setMinHeight(85);
        startGameB.setLayoutX(595);
        startGameB.setLayoutY(247);
        startGameB.setDisable(false);
        startGameB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) startGameB.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("view.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load(), 1532, 800);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                stage.setTitle("League of Tanks");
                stage.setScene(scene);
                stage.show();
            }
        });

        endGameB.setText("Выйти");
        endGameB.setStyle("-fx-font: 35 arial; -fx-base: #b6e7c9;");
        endGameB.setMinWidth(300);
        endGameB.setMinHeight(85);
        endGameB.setLayoutX(595);
        endGameB.setLayoutY(367);
        endGameB.setDisable(false);
        endGameB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) endGameB.getScene().getWindow();
                stage.close();
            }
        });
    }
}
