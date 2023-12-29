package mygames.task2_leagueoftanks.services;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import mygames.task2_leagueoftanks.tankmodels.AbstractTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.HeavyTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.LightTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.SelfPropelledGuns;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.TankDestroyer;

import java.util.ArrayList;
import java.util.HashMap;

public class RasterizationService {
    public static int x = 276;
    public static int y = 10;
    public static HashMap<String, Group> mapTanks = new HashMap();

    public static void drawTankHex(GraphicsContext g, AnchorPane anchorPane, String currentHex) {
        x = 276;
        y = 10;
        int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
        int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
        for (int i = 0; i <= currentHexY; i++) {
            if (i % 2 == 0) {
                x = 276;
            } else {
                x = 241;
            }
            for (int j = 0; j < currentHexX; j++) {
                x = x + 70;
            }
            if (i == currentHexY) {
                break;
            }
            y = y + 63;
        }

        drawTankHex(g, x, y, anchorPane, BattleService.getBattleField()[currentHexX][currentHexY]);
    }

    public static void drawHpAndStamina(GraphicsContext g, String hex) {
        x = 276;
        y = 10;
        int currentHexX = Integer.parseInt(hex.split(" ")[0]);
        int currentHexY = Integer.parseInt(hex.split(" ")[1]);
        for (int i = 0; i <= currentHexY; i++) {
            if (i % 2 == 0) {
                x = 276;
            } else {
                x = 241;
            }
            for (int j = 0; j < currentHexX; j++) {
                x = x + 70;
            }
            if (i == currentHexY) {
                break;
            }
            y = y + 63;
        }
        drawHpAndStamina(g, x + 2, y + 30, BattleService.getBattleField()[currentHexX][currentHexY]);
    }

    public static int[] countHpAndStamina(String hex) {
        String typeOfTank = hex.substring(1, 2);
        AbstractTank tank = new AbstractTank();
        switch (typeOfTank) {
            case "L" -> tank = new LightTank();
            case "H" -> tank = new HeavyTank();
            case "D" -> tank = new TankDestroyer();
            case "S" -> tank = new SelfPropelledGuns();
            default -> tank = new LightTank();
        }
        int hp = 9;
        int stamina = 9;
        if (hex.length() == 7) {
            hp = tank.getArmor();
            stamina = tank.getAmmunition();
        } else if (hex.length() == 8) {
            hp = tank.getArmor() - Integer.parseInt(hex.substring(7, 8));
            stamina = tank.getAmmunition();
        } else if (hex.length() == 9) {
            hp = tank.getArmor() - Integer.parseInt(hex.substring(7, 8));
            stamina = tank.getAmmunition() - Integer.parseInt(hex.substring(8, 9));
        }
        return new int[]{hp, stamina};
    }

    private static void drawHpAndStamina(GraphicsContext g, int x, int y, String hex) {
        drawGrassHex(g, x - 2, y - 30);
        String player = hex.substring(6, 7);
        int[]hpAndStamina=countHpAndStamina(hex);
        if (player.equals("R")) {
            g.setFill(Color.RED);
        } else {
            g.setFill(Color.WHITE);
        }
        //g.fillOval(x+20,y+60,6,6);
        g.fillPolygon(new double[]{x + 20, x + 25, x + 30, x + 25}, new double[]{y + 40, y + 35, y + 40, y + 45}, 4);
        g.fillText(" " + hpAndStamina[0], x + 27, y + 43);
        g.fillRect(x + 39, y + 35, 4, 10);
        g.fillText(String.valueOf(hpAndStamina[1]), x + 44, y + 43);
    }

    public static void drawTankHex(GraphicsContext g, int x, int y, AnchorPane anchorPane, String hex) {
        g.setLineWidth(2.0);
        Group group = new Group();
        anchorPane.getChildren().add(group);
        drawGrassHex(g, x, y);
        int xx = x + 7;
        int yy = y + 35;
        x = x + 2;
        y = y + 30;
        String typeOfTank = hex.substring(1, 2);
        String tankPosition = hex.substring(2, 4);
        String turretPosition = hex.substring(4, 6);
        String player = hex.substring(6, 7);
        drawHpAndStamina(g, x, y, hex);
        int angleTurnTurret = 0;
        int angleTurnTank = 0;
        switch (tankPosition) {
            case "RR":
                angleTurnTank = 0;
                break;
            case "RD":
                angleTurnTank = 57;
                break;
            case "LD":
                angleTurnTank = 123;
                break;
            case "LL":
                angleTurnTank = 180;
                break;
            case "LU":
                angleTurnTank = 237;
                break;
            case "RU":
                angleTurnTank = 303;
                break;
        }
        switch (turretPosition) {
            case "RR":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 0;
                    case "RD" -> angleTurnTurret = 303;
                    case "LD" -> angleTurnTurret = 237;
                    case "LL" -> angleTurnTurret = 180;
                    case "LU" -> angleTurnTurret = 123;
                    case "RU" -> angleTurnTurret = 57;
                }
                break;
            case "RD":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 57;
                    case "RD" -> angleTurnTurret = 0;
                    case "LD" -> angleTurnTurret = 303;
                    case "LL" -> angleTurnTurret = 237;
                    case "LU" -> angleTurnTurret = 180;
                    case "RU" -> angleTurnTurret = 123;
                }
                break;
            case "LD":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 123;
                    case "RD" -> angleTurnTurret = 57;
                    case "LD" -> angleTurnTurret = 0;
                    case "LL" -> angleTurnTurret = 303;
                    case "LU" -> angleTurnTurret = 237;
                    case "RU" -> angleTurnTurret = 180;
                }
                break;
            case "LL":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 180;
                    case "RD" -> angleTurnTurret = 123;
                    case "LD" -> angleTurnTurret = 57;
                    case "LL" -> angleTurnTurret = 0;
                    case "LU" -> angleTurnTurret = 303;
                    case "RU" -> angleTurnTurret = 237;
                }
                break;
            case "LU":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 237;
                    case "RD" -> angleTurnTurret = 180;
                    case "LD" -> angleTurnTurret = 123;
                    case "LL" -> angleTurnTurret = 57;
                    case "LU" -> angleTurnTurret = 0;
                    case "RU" -> angleTurnTurret = 303;
                }
                break;
            case "RU":
                switch (tankPosition) {
                    case "RR" -> angleTurnTurret = 303;
                    case "RD" -> angleTurnTurret = 237;
                    case "LD" -> angleTurnTurret = 180;
                    case "LL" -> angleTurnTurret = 123;
                    case "LU" -> angleTurnTurret = 57;
                    case "RU" -> angleTurnTurret = 0;
                }
                break;
        }
        switch (typeOfTank) {
            case "L" -> {
                Ellipse caterpillar1 = new Ellipse(x + 31, y + 30, 26, 2);
                Ellipse caterpillar2 = new Ellipse(x + 31, y + 54, 26, 2);
                Rectangle frame = new Rectangle(x + 6, y + 32, 50, 20);
                frame.setFill(Color.DARKGRAY);
                Circle turret = new Circle(x + 38, y + 42, 8);
                switch (player) {
                    case "R" -> turret.setFill(Color.DARKRED);
                    case "B" -> turret.setFill(Color.WHITESMOKE);
                }
                Rectangle gun = new Rectangle(x + 44, y + 40, 18, 4);
                switch (player) {
                    case "R" -> gun.setFill(Color.DARKRED);
                    case "B" -> gun.setFill(Color.WHITESMOKE);
                }
                Line line1 = new Line(x + 10, y + 34, x + 10, y + 50);
                Line line2 = new Line(x + 12, y + 34, x + 12, y + 50);
                group.getChildren().add(caterpillar1);
                group.getChildren().add(caterpillar2);
                group.getChildren().add(frame);
                group.getChildren().add(turret);
                group.getChildren().add(gun);
                group.getChildren().add(line1);
                group.getChildren().add(line2);
                Rotate rotate = new Rotate(angleTurnTank, x + 32, y + 42);
                Rotate rotateTurret = new Rotate(angleTurnTurret, x + 38, y + 42);
                gun.getTransforms().add(rotateTurret);
                group.getTransforms().add(rotate);
            }
            case "H" -> {
                Ellipse caterpillar1 = new Ellipse(x + 32, y + 25, 21, 2);
                Ellipse caterpillar2 = new Ellipse(x + 32, y + 59, 21, 2);
                Rectangle frame = new Rectangle(x + 11, y + 27, 40, 30);
                frame.setFill(Color.DARKGRAY);
                Circle turret = new Circle(x + 34, y + 42, 10);
                switch (player) {
                    case "R" -> turret.setFill(Color.DARKRED);
                    case "B" -> turret.setFill(Color.WHITESMOKE);
                }
                Rectangle gun = new Rectangle(x + 40, y + 40, 18, 4);
                switch (player) {
                    case "R" -> gun.setFill(Color.DARKRED);
                    case "B" -> gun.setFill(Color.WHITESMOKE);
                }
                Line line1 = new Line(x + 13, y + 29, x + 13, y + 41);
                Line line2 = new Line(x + 15, y + 29, x + 15, y + 41);
                Line line3 = new Line(x + 13, y + 43, x + 13, y + 55);
                Line line4 = new Line(x + 15, y + 43, x + 15, y + 55);
                group.getChildren().add(caterpillar1);
                group.getChildren().add(caterpillar2);
                group.getChildren().add(frame);
                group.getChildren().add(turret);
                group.getChildren().add(gun);
                group.getChildren().add(line1);
                group.getChildren().add(line2);
                group.getChildren().add(line3);
                group.getChildren().add(line4);
                Rotate rotate = new Rotate(angleTurnTank, x + 31, y + 42);
                Rotate rotateTurret = new Rotate(angleTurnTurret, x + 34, y + 42);
                gun.getTransforms().add(rotateTurret);
                group.getTransforms().add(rotate);
            }
            case "D" -> {
                Ellipse caterpillar1 = new Ellipse(x + 31, y + 22, 23, 2);
                Ellipse caterpillar2 = new Ellipse(x + 31, y + 52, 23, 2);
                Rectangle frame = new Rectangle(x + 8, y + 24, 46, 26);
                frame.setFill(Color.DARKGRAY);
                Rectangle turret = new Rectangle(x + 24, y + 27, 26, 20);
                switch (player) {
                    case "R" -> turret.setFill(Color.DARKRED);
                    case "B" -> turret.setFill(Color.WHITESMOKE);
                }
                Rectangle gun = new Rectangle(x + 50, y + 35, 10, 4);
                switch (player) {
                    case "R" -> gun.setFill(Color.DARKRED);
                    case "B" -> gun.setFill(Color.WHITESMOKE);
                }
                Line line1 = new Line(x + 11, y + 27, x + 11, y + 47);
                Line line2 = new Line(x + 13, y + 27, x + 13, y + 47);
                group.getChildren().add(caterpillar1);
                group.getChildren().add(caterpillar2);
                group.getChildren().add(frame);
                group.getChildren().add(turret);
                group.getChildren().add(gun);
                group.getChildren().add(line1);
                group.getChildren().add(line2);
                Rotate rotate = new Rotate(angleTurnTank, x + 31, y + 37);
                group.getTransforms().add(rotate);
            }
        }
        mapTanks.put(xx + " " + yy, group);
    }

    public static void removeTankHex(String currentHex) {
        x = 276;
        y = 10;
        int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
        int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
        for (int i = 0; i <= currentHexY; i++) {
            if (i % 2 == 0) {
                x = 276;
            } else {
                x = 241;
            }
            for (int j = 0; j < currentHexX; j++) {
                x = x + 70;
            }
            if (i == currentHexY) {
                break;
            }
            y = y + 63;
        }
        x += 7;
        y += 35;
        mapTanks.get(x + " " + y).setVisible(false);
        mapTanks.remove(currentHex);
    }

    public static void drawGrassHex(final GraphicsContext graphicsContext, int x, int y) {
        graphicsContext.setLineWidth(2.0);
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillPolygon(new double[]{x, x, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokePolygon(new double[]{x, x, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
    }

    public static void drawGrassHex(GraphicsContext graphicsContext, String currentHex) {
        x = 276;
        y = 10;
        int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
        int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
        for (int i = 0; i <= currentHexY; i++) {
            if (i % 2 == 0) {
                x = 276;
            } else {
                x = 241;
            }
            for (int j = 0; j < currentHexX; j++) {
                x = x + 70;
            }
            if (i == currentHexY) {
                break;
            }
            y = y + 63;
        }
        drawGrassHex(graphicsContext, x, y);
    }

    public static void drawForestHex(final GraphicsContext graphicsContext, int x, int y) {
        graphicsContext.setLineWidth(2.0);
        graphicsContext.setFill(Color.DARKGREEN);
        graphicsContext.fillPolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokePolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
    }

    public static void drawStoneHex(final GraphicsContext graphicsContext, int x, int y) {
        graphicsContext.setLineWidth(2.0);
        graphicsContext.setFill(Color.GRAY);
        graphicsContext.fillPolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokePolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
    }

    public static void drawWaterHex(final GraphicsContext graphicsContext, int x, int y) {
        graphicsContext.setLineWidth(2.0);
        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillPolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.strokePolygon(new double[]{x + 0, x + 0, x + 35, x + 70, x + 70, x + 35},
                new double[]{y + 21, y + 63, y + 84, y + 63, y + 21, y}, 6);
    }

    public static void drawBattleField(final GraphicsContext graphicsContext, String[][] battleField, AnchorPane anchorPane) {
        x = 276;
        y = 10;
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 15; j++) {
                if (flag == false && j == 14) {
                    break;
                }
                String hex = battleField[j][i];
                switch (hex.substring(0, 1)) {
                    case "G":
                        drawGrassHex(graphicsContext, x, y);
                        break;
                    case "F":
                        drawForestHex(graphicsContext, x, y);
                        break;
                    case "S":
                        drawStoneHex(graphicsContext, x, y);
                        break;
                    case "W":
                        drawWaterHex(graphicsContext, x, y);
                        break;
                    case "T":
                        drawTankHex(graphicsContext, x, y, anchorPane, hex);
                }
                x = x + 70;
            }
            if (flag == false) {
                flag = true;
                x = 241;
            } else {
                flag = false;
                x = 276;
            }
            y = y + 63;
        }
        x = 276;
        y = 10;
    }

    public static void highlightHex(GraphicsContext graphicsContext, String currentHex, boolean cancelMarked) {
        x = 276;
        y = 10;
        int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
        int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
        for (int i = 0; i <= currentHexY; i++) {
            if (i % 2 == 0) {
                x = 276;
            } else {
                x = 241;
            }
            for (int j = 0; j < currentHexX; j++) {
                x = x + 70;
            }
            if (i == currentHexY) {
                break;
            }
            y = y + 63;
        }
        int xx = x;
        int yy = y;
        if (cancelMarked) {
            graphicsContext.setStroke(Color.GREEN);
            graphicsContext.setLineWidth(2.0);
            graphicsContext.strokePolygon(new double[]{xx, xx, xx + 35, xx + 70, xx + 70, xx + 35},
                    new double[]{yy + 21, yy + 63, yy + 84, yy + 63, yy + 21, yy}, 6);
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.setLineWidth(2.0);
        } else if (PlayerService.getPlayer1()) {
            graphicsContext.setStroke(Color.DARKRED);
            graphicsContext.setLineWidth(2.0);
        } else if (PlayerService.getPlayer2()) {
            graphicsContext.setStroke(Color.WHITESMOKE);
            graphicsContext.setLineWidth(2.0);
        }
        graphicsContext.strokePolygon(new double[]{xx, xx, xx + 35, xx + 70, xx + 70, xx + 35},
                new double[]{yy + 21, yy + 63, yy + 84, yy + 63, yy + 21, yy}, 6);
        x = 276;
        y = 10;
    }

    public static void highlightHexes(GraphicsContext graphicsContext, ArrayList<String> highlightHexes, boolean flag) {
        for (int i = 0; i < highlightHexes.size(); i++) {
            highlightHex(graphicsContext, highlightHexes.get(i), flag);
        }
    }
}