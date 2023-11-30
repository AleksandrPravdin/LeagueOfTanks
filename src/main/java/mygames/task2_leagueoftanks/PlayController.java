package mygames.task2_leagueoftanks;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import mygames.task2_leagueoftanks.services.BattleService;
import mygames.task2_leagueoftanks.services.PlayerService;
import mygames.task2_leagueoftanks.services.RasterizationService;


import java.io.FileNotFoundException;
import java.util.ArrayList;


public class PlayController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;
    @FXML
    private Button fireButton;
    @FXML
    private Button driveButton;
    @FXML
    private Button turnTankButton;
    @FXML
    private Button turnTurretButton;
    @FXML
    private Button endPlayersMoveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button turnRRButton;
    @FXML
    private Button turnRDButton;
    @FXML
    private Button turnLDButton;
    @FXML
    private Button turnLLButton;
    @FXML
    private Button turnLUButton;
    @FXML
    private Button turnRUButton;
    @FXML
    private TextField redFuelMeterTF;
    @FXML
    private TextField whiteFuelMeterTF;

    @FXML
    private void initialize() throws FileNotFoundException {

        PlayerService.createPlayers();
        BattleService.initBattleField();

        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));
        anchorPane.setStyle("-fx-base: #b6e7c9;");
        RasterizationService.drawBattleField(canvas.getGraphicsContext2D(), BattleService.getBattleField(), anchorPane);
        buttonControl();
        changePlayersFuel();

        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED,
                e -> {
                    BattleService.findCurrentHex(e.getX(), (e.getY() - 29));
                    switch (PlayerService.getAction()) {
                        case "ChooseTank" -> {
                            if (BattleService.checkTankHex()) {
                                System.out.println(BattleService.getCurrentHex());
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), false);
                                BattleService.markedTankHex();
                                PlayerService.setAction("ChooseButton");
                                activateActionButtons();
                            }
                        }
                        case "ChooseButton" -> {
                            if (BattleService.checkTankHex()) {
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), false);
                                BattleService.markedTankHex();
                            }
                        }
                        case "drive" -> {
                            if (BattleService.drive()) {
                                PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForDrive());
                                changePlayersFuel();
                                RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getDrivingHexes(), true);
                                RasterizationService.removeTankHex(BattleService.getTankHex());
                                RasterizationService.drawGrassHex(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                BattleService.markedTankHex();
                                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                                cancelButton.setVisible(false);
                                PlayerService.setAction("ChooseTank");
                                transferMove();
                            }
                        }
                        case "shoot" -> {
                            switch (BattleService.shoot()) {
                                case 1 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawGrassHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex());
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    cancelButton.setVisible(false);
                                    PlayerService.setAction("ChooseTank");
                                    transferMove();
                                }
                                case 2 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.removeTankHex(BattleService.getCurrentHex());
                                    RasterizationService.drawGrassHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex());
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    cancelButton.setVisible(false);
                                    PlayerService.setAction("ChooseTank");
                                    transferMove();
                                }
                                case 3 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    cancelButton.setVisible(false);
                                    PlayerService.setAction("ChooseTank");
                                    transferMove();
                                }
                                case 4 ->{
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getCurrentHex());
                                    cancelButton.setVisible(false);
                                    PlayerService.setAction("ChooseTank");
                                    transferMove();
                                }
                            }
                        }
                    }
                });

    }

    public void transferMove() {
        if (PlayerService.getPlayerOil() <= 0) {
            PlayerService.setPlayerOil(6);
            if (PlayerService.getPlayer1()) {
                PlayerService.setPlayer1(false);
                PlayerService.setPlayer2(true);
            } else {
                PlayerService.setPlayer1(true);
                PlayerService.setPlayer2(false);
            }
            tankReloading(BattleService.tankReloading());
            changePlayersFuel();
        }
    }

    public void tankReloading(ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), list.get(i));
        }
    }

    public void hideActionButtons() {
        fireButton.setVisible(false);
        driveButton.setVisible(false);
        turnTankButton.setVisible(false);
        turnTurretButton.setVisible(false);
        endPlayersMoveButton.setVisible(false);
    }

    public void activateActionButtons() {
        fireButton.setVisible(true);
        driveButton.setVisible(true);
        turnTankButton.setVisible(true);
        if (BattleService.getTank().getFuelForTurnTurret() != -1) {
            turnTurretButton.setVisible(true);
        }
        endPlayersMoveButton.setVisible(true);
    }

    public void activateTurnButtons() {
        turnRRButton.setVisible(true);
        turnRDButton.setVisible(true);
        turnLDButton.setVisible(true);
        turnLLButton.setVisible(true);
        turnLUButton.setVisible(true);
        turnRUButton.setVisible(true);
    }

    public void hideTurnButtons() {
        turnRRButton.setVisible(false);
        turnRDButton.setVisible(false);
        turnLDButton.setVisible(false);
        turnLLButton.setVisible(false);
        turnLUButton.setVisible(false);
        turnRUButton.setVisible(false);
    }

    private void changePlayersFuel() {
        if (PlayerService.getPlayer1()) {
            redFuelMeterTF.setText("Канистр топлива: " + PlayerService.getPlayerOil());
            whiteFuelMeterTF.setText("Канистр топлива: " + 0);
        } else {
            redFuelMeterTF.setText("Канистр топлива: " + 0);
            whiteFuelMeterTF.setText("Канистр топлива: " + PlayerService.getPlayerOil());
        }
    }

    private void fuelForTurn() {
        if (PlayerService.getAction().equals("turnTank")) {
            PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForTurnTank());
        } else if (PlayerService.getAction().equals("turnTurret")) {
            PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForTurnTurret());
        }
        changePlayersFuel();
    }

    public void buttonControl() {

        redFuelMeterTF.setText("Канистр топлива: V");
        redFuelMeterTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        redFuelMeterTF.setMinHeight(20);
        redFuelMeterTF.setMaxWidth(205);
        redFuelMeterTF.setLayoutX(20);
        redFuelMeterTF.setLayoutY(100);
        redFuelMeterTF.setEditable(false);
        redFuelMeterTF.setDisable(false);
        redFuelMeterTF.setFocusTraversable(false);

        whiteFuelMeterTF.setText("Канистр топлива: P");
        whiteFuelMeterTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        whiteFuelMeterTF.setMinHeight(20);
        whiteFuelMeterTF.setMaxWidth(205);
        whiteFuelMeterTF.setLayoutX(1307);
        whiteFuelMeterTF.setLayoutY(100);
        whiteFuelMeterTF.setEditable(false);
        whiteFuelMeterTF.setDisable(false);
        whiteFuelMeterTF.setFocusTraversable(false);

        fireButton.setText("Стрелять");
        fireButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        fireButton.setMinWidth(150);
        fireButton.setMinHeight(55);
        fireButton.setLayoutX(296);
        fireButton.setLayoutY(647);
        fireButton.setDisable(false);
        fireButton.setVisible(false);
        fireButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (BattleService.ammunitionInStock()) {
                    hideActionButtons();
                    cancelButton.setVisible(true);
                    PlayerService.setAction("shoot");
                    BattleService.findShootingHexes();
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), false);
                }

            }
        });

        driveButton.setText("Ехать");
        driveButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        driveButton.setMinWidth(150);
        driveButton.setMinHeight(55);
        driveButton.setLayoutX(506);
        driveButton.setLayoutY(647);
        driveButton.setDisable(false);
        driveButton.setVisible(false);
        driveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (PlayerService.getPlayerOil() >= BattleService.getTank().getFuelForDrive()) {
                    hideActionButtons();
                    cancelButton.setVisible(true);
                    PlayerService.setAction("drive");
                    BattleService.findDrivingHexes();
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getDrivingHexes(), false);
                }
            }
        });

        turnTankButton.setText("Повернуть танк");
        turnTankButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnTankButton.setMinWidth(150);
        turnTankButton.setMinHeight(55);
        turnTankButton.setLayoutX(716);
        turnTankButton.setLayoutY(647);
        turnTankButton.setDisable(false);
        turnTankButton.setVisible(false);
        turnTankButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (PlayerService.getPlayerOil() >= BattleService.getTank().getFuelForTurnTank()) {
                    changePlayersFuel();
                    hideActionButtons();
                    cancelButton.setVisible(true);
                    PlayerService.setAction("turnTank");
                    activateTurnButtons();
                }
            }
        });

        turnTurretButton.setText("Повернуть башню");
        turnTurretButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnTurretButton.setMinWidth(150);
        turnTurretButton.setMinHeight(55);
        turnTurretButton.setLayoutX(990);
        turnTurretButton.setLayoutY(647);
        turnTurretButton.setDisable(false);
        turnTurretButton.setVisible(false);
        turnTurretButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (PlayerService.getPlayerOil() >= BattleService.getTank().getFuelForTurnTurret()) {
                    changePlayersFuel();
                    hideActionButtons();
                    cancelButton.setVisible(true);
                    PlayerService.setAction("turnTurret");
                    activateTurnButtons();
                }
            }
        });

        turnLUButton.setText("Повернуть ⬉");
        turnLUButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnLUButton.setMinWidth(150);
        turnLUButton.setMinHeight(55);
        turnLUButton.setLayoutX(351);
        turnLUButton.setLayoutY(647);
        turnLUButton.setDisable(false);
        turnLUButton.setVisible(false);
        turnLUButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("LU");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        turnLLButton.setText("Повернуть ←");
        turnLLButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnLLButton.setMinWidth(150);
        turnLLButton.setMinHeight(55);
        turnLLButton.setLayoutX(562);
        turnLLButton.setLayoutY(647);
        turnLLButton.setDisable(false);
        turnLLButton.setVisible(false);
        turnLLButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("LL");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        turnRRButton.setText("Повернуть →");
        turnRRButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnRRButton.setMinWidth(150);
        turnRRButton.setMinHeight(55);
        turnRRButton.setLayoutX(777);
        turnRRButton.setLayoutY(647);
        turnRRButton.setDisable(false);
        turnRRButton.setVisible(false);
        turnRRButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("RR");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        turnRUButton.setText("Повернуть ⬈");
        turnRUButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnRUButton.setMinWidth(150);
        turnRUButton.setMinHeight(55);
        turnRUButton.setLayoutX(995);
        turnRUButton.setLayoutY(647);
        turnRUButton.setDisable(false);
        turnRUButton.setVisible(false);
        turnRUButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("RU");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        turnLDButton.setText("Повернуть ⬋");
        turnLDButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnLDButton.setMinWidth(150);
        turnLDButton.setMinHeight(55);
        turnLDButton.setLayoutX(488);
        turnLDButton.setLayoutY(722);
        turnLDButton.setDisable(false);
        turnLDButton.setVisible(false);
        turnLDButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("LD");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        turnRDButton.setText("Повернуть ⬊");
        turnRDButton.setStyle("-fx-font: 25 arial; -fx-base: #b6e7c9;");
        turnRDButton.setMinWidth(150);
        turnRDButton.setMinHeight(55);
        turnRDButton.setLayoutX(870);
        turnRDButton.setLayoutY(722);
        turnRDButton.setDisable(false);
        turnRDButton.setVisible(false);
        turnRDButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BattleService.turn("RD");
                RasterizationService.removeTankHex(BattleService.getTankHex());
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                fuelForTurn();
                PlayerService.setAction("ChooseTank");
                hideTurnButtons();
                cancelButton.setVisible(false);
                transferMove();
            }
        });

        endPlayersMoveButton.setText("Закончить ход");
        endPlayersMoveButton.setStyle("-fx-font: 25 arial; -fx-base: #B22222;");
        endPlayersMoveButton.setMinWidth(150);
        endPlayersMoveButton.setMinHeight(55);
        endPlayersMoveButton.setLayoutX(670);
        endPlayersMoveButton.setLayoutY(722);
        endPlayersMoveButton.setDisable(false);
        endPlayersMoveButton.setVisible(false);
        endPlayersMoveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                PlayerService.setAction("ChooseTank");
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                PlayerService.setPlayerOil(0);
                transferMove();
                hideActionButtons();
            }
        });

        cancelButton.setText("Отмена");
        cancelButton.setStyle("-fx-font: 25 arial; -fx-base: #B22222;");
        cancelButton.setMinWidth(150);
        cancelButton.setMinHeight(55);
        cancelButton.setLayoutX(695);
        cancelButton.setLayoutY(722);
        cancelButton.setDisable(false);
        cancelButton.setVisible(false);
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                cancelButton.setVisible(false);
                hideTurnButtons();
                if (PlayerService.getAction().equals("drive")) {
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getDrivingHexes(), true);
                } else if (PlayerService.getAction().equals("shoot")) {
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                } else if (PlayerService.getAction().equals("turnTank")) {
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), true);
                } else if (PlayerService.getAction().equals("turnTurret")) {
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), true);
                }
                PlayerService.setAction("ChooseTank");
            }
        });
    }

}