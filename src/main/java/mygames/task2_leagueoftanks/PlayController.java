package mygames.task2_leagueoftanks;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mygames.task2_leagueoftanks.services.BattleService;
import mygames.task2_leagueoftanks.services.GameState;
import mygames.task2_leagueoftanks.services.PlayerService;
import mygames.task2_leagueoftanks.services.RasterizationService;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.TankDestroyer;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class PlayController {
    int bb = 60;

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
    private TextArea redTankA;
    @FXML
    private TextArea whiteTankA;
    @FXML
    private TextField redTimerTF;
    @FXML
    private TextField whiteTimerTF;
    Timer myTimer = new Timer();

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
                        case CHOOSE_TANK -> {
                            BattleService.setTankSelected(false);
                            if (BattleService.checkTankHex()) {
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), false);
                                BattleService.markedTankHex();
                                PlayerService.setAction(GameState.CHOOSE_BUTTON);
                                activateActionButtons();
                                if (BattleService.getTank().getClass() == TankDestroyer.class) {
                                    turnTurretButton.setVisible(false);
                                } else {
                                    turnTurretButton.setVisible(true);
                                }
                            }
                            if (BattleService.isTankSelected()) {
                                tankCharacteristics();
                            }
                        }
                        case CHOOSE_BUTTON -> {
                            BattleService.setTankSelected(false);
                            if (BattleService.checkTankHex()) {
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), false);
                                BattleService.markedTankHex();
                                if (BattleService.getTank().getClass() == TankDestroyer.class) {
                                    turnTurretButton.setVisible(false);
                                } else {
                                    turnTurretButton.setVisible(true);
                                }
                            }
                            if (BattleService.isTankSelected()) {
                                tankCharacteristics();
                            }
                        }
                        case DRIVE -> {
                            if (BattleService.drive()) {
                                PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForDrive());
                                changePlayersFuel();
                                RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getDrivingHexes(), true);
                                RasterizationService.removeTankHex(BattleService.getTankHex());
                                RasterizationService.drawGrassHex(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                BattleService.markedTankHex();
                                RasterizationService.drawTankHex(canvas.getGraphicsContext2D(), anchorPane, BattleService.getTankHex());
                                cancelButton.setVisible(false);
                                redTankA.setVisible(false);
                                whiteTankA.setVisible(false);
                                PlayerService.setAction(GameState.CHOOSE_TANK);
                                transferMove();
                            }
                        }
                        case SHOOT -> {
                            switch (BattleService.shoot()) {
                                case 1 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawGrassHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex());
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    cancelButton.setVisible(false);
                                    redTankA.setVisible(false);
                                    whiteTankA.setVisible(false);
                                    PlayerService.setAction(GameState.CHOOSE_TANK);
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
                                    redTankA.setVisible(false);
                                    whiteTankA.setVisible(false);
                                    PlayerService.setAction(GameState.CHOOSE_TANK);
                                    checkEndGame();
                                    transferMove();
                                }
                                case 3 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    cancelButton.setVisible(false);
                                    redTankA.setVisible(false);
                                    whiteTankA.setVisible(false);
                                    PlayerService.setAction(GameState.CHOOSE_TANK);
                                    transferMove();
                                }
                                case 4 -> {
                                    PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForFire());
                                    changePlayersFuel();
                                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getTankHex());
                                    RasterizationService.drawHpAndStamina(canvas.getGraphicsContext2D(), BattleService.getCurrentHex());
                                    cancelButton.setVisible(false);
                                    redTankA.setVisible(false);
                                    whiteTankA.setVisible(false);
                                    PlayerService.setAction(GameState.CHOOSE_TANK);
                                    transferMove();
                                }
                            }
                        }
                    }
                });

    }

    private void checkEndGame() {
        if (PlayerService.getPlayer1()) {
            PlayerService.setCountTankP2(PlayerService.getCountTankP2() - 1);
        } else {
            PlayerService.setCountTankP1(PlayerService.getCountTankP1() - 1);
        }
        if (PlayerService.getCountTankP2() == 0 || PlayerService.getCountTankP1() == 0) {
            myTimer.cancel();
            Stage stage = (Stage) redFuelMeterTF.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("end.fxml"));
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
    }

    public void transferMove() {
        if (PlayerService.getPlayerOil() <= 0) {
            PlayerService.setPlayerOil(8);
            if (PlayerService.getPlayer1()) {
                PlayerService.setPlayers1Time(PlayerService.getPlayers1Time() + 3);
                PlayerService.setPlayer1(false);
                PlayerService.setPlayer2(true);
                redTimerTF.setText(returnCorrectTime(PlayerService.getPlayers1Time()));
            } else {
                PlayerService.setPlayers2Time(PlayerService.getPlayers2Time() + 3);
                PlayerService.setPlayer1(true);
                PlayerService.setPlayer2(false);
                whiteTimerTF.setText(returnCorrectTime(PlayerService.getPlayers2Time()));
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
        if (PlayerService.getAction() == GameState.TURN_TANK) {
            PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForTurnTank());
        } else if (PlayerService.getAction() == GameState.TURN_TURRET) {
            PlayerService.setPlayerOil(PlayerService.getPlayerOil() - BattleService.getTank().getFuelForTurnTurret());
        }
        changePlayersFuel();
    }

    public void tankCharacteristics() {
        int j = Integer.parseInt(BattleService.getCurrentHex().split(" ")[0]);
        int i = Integer.parseInt(BattleService.getCurrentHex().split(" ")[1]);
        String player = BattleService.getBattleField()[j][i].substring(6, 7);
        if (player.equals("R")) {
            redTankA.setText(BattleService.getTank().getName() + "\n" +
                    "Броня: " + RasterizationService.countHpAndStamina(BattleService.getBattleField()[j][i])[0] + " из " + BattleService.getTank().getArmor() + "\n" +
                    "Урон: " + BattleService.getTank().getDamage() + "\n" +
                    "Дальность: " + BattleService.getTank().getShotRange() + "\n" +
                    "Скорость: " + BattleService.getTank().getSpeed() + "\n" +
                    "Боекомплект: " + RasterizationService.countHpAndStamina(BattleService.getBattleField()[j][i])[1] + " из " + BattleService.getTank().getAmmunition() + "\n" +
                    "Топливо для\nпередвижения: " + BattleService.getTank().getFuelForDrive() + "\n" +
                    "Топливо для\nвыстрела: " + BattleService.getTank().getFuelForFire() + "\n" +
                    "Топливо для\nповорота башни: " + BattleService.getTank().getFuelForTurnTurret() + "\n" +
                    "Топливо для\nповорота танка: " + BattleService.getTank().getFuelForTurnTank());
            redTankA.setVisible(true);
        } else if (player.equals("B")) {
            whiteTankA.setText(BattleService.getTank().getName() + "\n" +
                    "Броня: " + RasterizationService.countHpAndStamina(BattleService.getBattleField()[j][i])[0] + " из " + BattleService.getTank().getArmor() + "\n" +
                    "Урон: " + BattleService.getTank().getDamage() + "\n" +
                    "Дальность: " + BattleService.getTank().getShotRange() + "\n" +
                    "Скорость: " + BattleService.getTank().getSpeed() + "\n" +
                    "Боекомплект: " + RasterizationService.countHpAndStamina(BattleService.getBattleField()[j][i])[1] + " из " + BattleService.getTank().getAmmunition() + "\n" +
                    "Топливо для\nпередвижения: " + BattleService.getTank().getFuelForDrive() + "\n" +
                    "Топливо для\nвыстрела: " + BattleService.getTank().getFuelForFire() + "\n" +
                    "Топливо для\nповорота башни: " + BattleService.getTank().getFuelForTurnTurret() + "\n" +
                    "Топливо для\nповорота танка: " + BattleService.getTank().getFuelForTurnTank());
            whiteTankA.setVisible(true);
        }
    }

    private String returnCorrectTime(int time) {
        String correctTime = new String();
        String minute = String.valueOf(time % 60);
        if (minute.length() == 1) {
            correctTime += (time / 60) + ":0" + minute;
        }else{
            correctTime += (time / 60) + ":" + minute;
        }
        return correctTime;
    }

    public void buttonControl() {
        redTimerTF.setText(returnCorrectTime(PlayerService.getPlayers1Time()));
        redTimerTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        redTimerTF.setMinHeight(20);
        redTimerTF.setMaxWidth(100);
        redTimerTF.setLayoutX(20);
        redTimerTF.setLayoutY(45);
        redTimerTF.setEditable(false);
        redTimerTF.setDisable(false);
        redTimerTF.setFocusTraversable(false);

        redFuelMeterTF.setText("Канистр топлива: V");
        redFuelMeterTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        redFuelMeterTF.setMinHeight(20);
        redFuelMeterTF.setMaxWidth(205);
        redFuelMeterTF.setLayoutX(20);
        redFuelMeterTF.setLayoutY(100);
        redFuelMeterTF.setEditable(false);
        redFuelMeterTF.setDisable(false);
        redFuelMeterTF.setFocusTraversable(false);

        redTankA.setText("Канистр\n топлива: V");
        redTankA.setStyle("-fx-control-inner-background: #b6e7c9;-fx-font: 20 arial;");
        redTankA.setMinHeight(380);
        redTankA.setMinWidth(213);
        redTankA.setLayoutX(20);
        redTankA.setLayoutY(150);
        redTankA.setEditable(false);
        redTankA.setDisable(false);
        redTankA.setFocusTraversable(false);
        redTankA.setVisible(false);

        whiteFuelMeterTF.setText("Канистр топлива: P");
        whiteFuelMeterTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        whiteFuelMeterTF.setMinHeight(20);
        whiteFuelMeterTF.setMaxWidth(205);
        whiteFuelMeterTF.setLayoutX(1307);
        whiteFuelMeterTF.setLayoutY(100);
        whiteFuelMeterTF.setEditable(false);
        whiteFuelMeterTF.setDisable(false);
        whiteFuelMeterTF.setFocusTraversable(false);
        whiteTankA.setVisible(false);

        whiteTimerTF.setText(returnCorrectTime(PlayerService.getPlayers2Time()));
        whiteTimerTF.setStyle("-fx-background-color: #b6e7c9;-fx-font: 20 arial;");
        whiteTimerTF.setMinHeight(20);
        whiteTimerTF.setMaxWidth(100);
        whiteTimerTF.setLayoutX(1307);
        whiteTimerTF.setLayoutY(45);
        whiteTimerTF.setEditable(false);
        whiteTimerTF.setDisable(false);
        whiteTimerTF.setFocusTraversable(false);

        whiteTankA.setText("Канистр \n топлива: P");
        whiteTankA.setStyle("-fx-control-inner-background: #b6e7c9;-fx-font: 20 arial;");
        whiteTankA.setMinHeight(380);
        whiteTankA.setMinWidth(213);
        whiteTankA.setLayoutX(1307);
        whiteTankA.setLayoutY(150);
        whiteTankA.setEditable(false);
        whiteTankA.setDisable(false);
        whiteTankA.setFocusTraversable(false);

        myTimer.schedule(new TimerTask() {
            public void run() {
                if (PlayerService.getPlayer1()) {
                    PlayerService.setPlayers1Time(PlayerService.getPlayers1Time() - 1);
                    redTimerTF.setText(returnCorrectTime(PlayerService.getPlayers1Time()));
                } else {
                    PlayerService.setPlayers2Time(PlayerService.getPlayers2Time() - 1);
                    whiteTimerTF.setText(returnCorrectTime(PlayerService.getPlayers2Time()));
                }
                if (PlayerService.getPlayers1Time() == 0 || PlayerService.getPlayers2Time() == 0) {
                    myTimer.cancel();
                    Stage stage = (Stage) redFuelMeterTF.getScene().getWindow();
                    FXMLLoader fxmlLoader = new FXMLLoader(MyApplication.class.getResource("end.fxml"));
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
            }
        }, 0, 1000); // каждые 1 секунд

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
                if (BattleService.ammunitionInStock() && PlayerService.getPlayerOil() >= BattleService.getTank().getFuelForFire()) {
                    hideActionButtons();
                    cancelButton.setVisible(true);
                    PlayerService.setAction(GameState.SHOOT);
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
                    PlayerService.setAction(GameState.DRIVE);
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
                    PlayerService.setAction(GameState.TURN_TANK);
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
                    PlayerService.setAction(GameState.TURN_TURRET);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
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
                PlayerService.setAction(GameState.CHOOSE_TANK);
                RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getTankHex(), true);
                PlayerService.setPlayerOil(0);
                redTankA.setVisible(false);
                whiteTankA.setVisible(false);
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
                if (PlayerService.getAction() == GameState.DRIVE) {
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getDrivingHexes(), true);
                } else if (PlayerService.getAction() == GameState.SHOOT) {
                    RasterizationService.highlightHexes(canvas.getGraphicsContext2D(), BattleService.getShootingHexes(), true);
                } else if (PlayerService.getAction() == GameState.TURN_TANK) {
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), true);
                } else if (PlayerService.getAction() == GameState.TURN_TURRET) {
                    RasterizationService.highlightHex(canvas.getGraphicsContext2D(), BattleService.getCurrentHex(), true);
                }
                PlayerService.setAction(GameState.CHOOSE_TANK);
            }
        });
    }

}