package mygames.task2_leagueoftanks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mygames.task2_leagueoftanks.services.PlayerService;

import java.io.FileNotFoundException;
import java.io.IOException;

public class EndController {
    @FXML
    AnchorPane anchorPaneEnd;
    @FXML
    Button winB;
    @FXML
    TextField winTF;

    @FXML
    private void initialize() throws FileNotFoundException {
        anchorPaneEnd.setStyle("-fx-base: #b6e7c9;");

        winB.setText("В меню");
        winB.setStyle("-fx-font: 35 arial; -fx-base: #b6e7c9;");
        winB.setMinWidth(300);
        winB.setMinHeight(85);
        winB.setLayoutX(595);
        winB.setLayoutY(547);
        winB.setDisable(false);
        winB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage stage = (Stage) winB.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("start.fxml"));
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

        if(PlayerService.getPlayer1()){
            winTF.setText("ПОБЕДИЛ КРАСНЫЙ");
        }else{
            winTF.setText("ПОБЕДИЛ БЕЛЫЙ");
        }
        winTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 35 arial;");
        winTF.setMaxWidth(360);
        winTF.setMinHeight(85);
        winTF.setLayoutX(565);
        winTF.setLayoutY(247);
        winTF.setEditable(false);
        winTF.setDisable(false);
        winTF.setFocusTraversable(false);
    }
}
