package mygames.task2_leagueoftanks.services;

import mygames.task2_leagueoftanks.PlayController;
import mygames.task2_leagueoftanks.tankmodels.AbstractTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.HeavyTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.LightTank;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.SelfPropelledGuns;
import mygames.task2_leagueoftanks.tankmodels.typeoftanks.TankDestroyer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class BattleService {
    public static String[][] battleField = new String[15][9];
    public static String[][] hexCenter = new String[15][9];
    public static HashSet<String> tanksThatFired = new HashSet<>();
    public static String currentHex;
    public static String tankHex;
    public static ArrayList<String> drivingHexes = new ArrayList<>();
    public static ArrayList<String> shootingHexes = new ArrayList<>();
    public static AbstractTank tank;
    public static AbstractTank tankTarget;
    public static boolean tankSelected;

    public static boolean isTankSelected() {
        return tankSelected;
    }

    public static void setTankSelected(boolean tankSelected) {
        BattleService.tankSelected = tankSelected;
    }

    public static AbstractTank getTank() {
        return tank;
    }

    public static String[][] getHexCenter() {
        return hexCenter;
    }

    public static ArrayList<String> getDrivingHexes() {
        return drivingHexes;
    }

    public static ArrayList<String> getShootingHexes() {
        return shootingHexes;
    }

    public static String[][] getBattleField() {
        return battleField;
    }

    public static String getCurrentHex() {
        return currentHex;
    }

    public static String getTankHex() {
        return tankHex;
    }

    public static void initBattleField() throws FileNotFoundException {
        File file = new File("src/main/resources/mygames/task2_leagueoftanks/battlemaps/map1");
        Scanner scanner = new Scanner(file);
        for (int i = 0; i < 9; i++) {
            String[] s = scanner.nextLine().split(" ");
            for (int j = 0; j < 15; j++) {
                battleField[j][i] = s[j];
            }
        }
        findHexCenter();
    }

    public static void findHexCenter() {
        int stepOddX = 276;
        int stepEvenX = 241;
        int stepY = 10;
        boolean flag = false;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 15; j++) {
                if (flag == false && j == 14) {
                    break;
                }
                if (!flag) {
                    hexCenter[j][i] = String.valueOf(stepOddX + 35) + " " + String.valueOf(stepY + 42);
                    stepOddX += 70;
                } else {
                    hexCenter[j][i] = String.valueOf(stepEvenX + 35) + " " + String.valueOf(stepY + 42);
                    stepEvenX += 70;
                }
            }
            if (!flag) {
                flag = true;
                stepOddX = 276;
            } else {
                flag = false;
                stepEvenX = 241;
            }
            stepY += 63;
        }
    }

    public static void findCurrentHex(double x, double y) {
        int hexsByX = (int) Math.min(((x - 271) / 70), ((x - 247) / 70));
        int hexsByY = (int) (y - 10) / 70;
        double result = 10000;
        for (int i = hexsByY; i <= hexsByY + 1; i++) {
            if (i == 9) {
                break;
            }
            for (int j = hexsByX; j <= hexsByX + 1; j++) {
                if ((i % 2 == 0) && (j == 14)) {
                    break;
                } else if ((i % 2 != 0) && (j == 15)) {
                    break;
                }
                int hx = Integer.parseInt(hexCenter[j][i].split(" ")[0]);
                int hy = Integer.parseInt(hexCenter[j][i].split(" ")[1]);
                if (Math.sqrt((hx - x) * (hx - x) + (hy - y) * (hy - y)) < result) {
                    result = Math.sqrt((hx - x) * (hx - x) + (hy - y) * (hy - y));
                    currentHex = j + " " + i;
                }
            }
        }
    }

    public static boolean checkTankHex() {
        int x = Integer.parseInt(currentHex.split(" ")[0]);
        int y = Integer.parseInt(currentHex.split(" ")[1]);
        if (battleField[x][y].substring(0, 1).equals("T")) {
            switch (battleField[x][y].substring(1, 2)) {
                case "L" -> tank = new LightTank();
                case "H" -> tank = new HeavyTank();
                case "D" -> tank = new TankDestroyer();
                case "S" -> tank = new SelfPropelledGuns();
                default -> tank = new LightTank();
            }
            tankSelected=true;
            if (PlayerService.getPlayer1() && battleField[x][y].substring(6, 7).equals("R")) {
                return true;
            }
            if (PlayerService.getPlayer2() && battleField[x][y].substring(6, 7).equals("B")) {
                return true;
            }
        }
        return false;
    }

    public static void markedTankHex() {
        tankHex = currentHex;
    }

    public static void findDrivingHexes() {
        drivingHexes.clear();
        int tankHexX = Integer.parseInt(tankHex.split(" ")[0]);
        int tankHexY = Integer.parseInt(tankHex.split(" ")[1]);
        String directionCaterpillar = battleField[tankHexX][tankHexY].substring(2, 4);
        String typeOfTank = battleField[tankHexX][tankHexY].substring(1, 2);
        boolean flag = true;
        switch (typeOfTank) {
            case "L" -> tank = new LightTank();
            case "H" -> tank = new HeavyTank();
            case "D" -> tank = new TankDestroyer();
            case "S" -> tank = new SelfPropelledGuns();
            default -> tank = new LightTank();
        }
        switch (directionCaterpillar) {
            case "RR" -> {
                for (int j = tankHexX + 1; j < 15; j++) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + tankHexY);
                    if (!flag) {
                        break;
                    }
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                }
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                flag = true;
                int kk = 0;
                for (int j = tankHexX - 1; j >= 0; j--) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + tankHexY);
                    kk += 1;
                    if (!flag) {
                        break;
                    }
                    if (kk >= k) {
                        break;
                    }
                }
            }
            case "RD" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i += 1;
                while (j < 15 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i += 1;
                }
                j = tankHexX;
                i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i -= 1;
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                flag = true;
                int kk = 0;
                while (j >= 0 && i >= 0 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    kk += 1;
                    if (kk >= k) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i -= 1;
                }
            }
            case "LD" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i += 1;
                while (j >= 0 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i += 1;
                }
                j = tankHexX;
                i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i -= 1;
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                int kk = 0;
                flag = true;
                while (j < 15 && i >= 0 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    kk += 1;
                    if (kk >= k) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i -= 1;
                }
            }
            case "LL" -> {
                for (int j = tankHexX - 1; j >= 0; j--) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + tankHexY);
                    if (!flag) {
                        break;
                    }
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                }
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                int kk = 0;
                flag = true;
                for (int j = tankHexX + 1; j < 15; j++) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + tankHexY);
                    kk += 1;
                    if (!flag) {
                        break;
                    }
                    if (kk >= k) {
                        break;
                    }
                }
            }
            case "LU" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i -= 1;
                while (j >= 0 && i >= 0 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i -= 1;
                }
                j = tankHexX;
                i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i += 1;
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                int kk = 0;
                flag = true;
                while (j < 15 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    kk += 1;
                    if (kk >= k) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i += 1;
                }
            }
            case "RU" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i -= 1;
                while (j < 15 && i >= 0 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    if (drivingHexes.size() >= tank.getSpeed()) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i -= 1;
                }
                j = tankHexX;
                i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i += 1;
                int k = tank.getSpeed() / 2;
                if (k == 0) {
                    k = 1;
                }
                int kk = 0;
                flag = true;
                while (j >= 0 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G")) {
                        flag = false;
                        if (!hex.equals("F")) {
                            break;
                        }
                    }
                    drivingHexes.add(j + " " + i);
                    kk += 1;
                    if (kk >= k) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i += 1;
                }
            }
        }
    }

    public static void findShootingHexes() {
        shootingHexes.clear();
        int tankHexX = Integer.parseInt(tankHex.split(" ")[0]);
        int tankHexY = Integer.parseInt(tankHex.split(" ")[1]);
        String directionTurret = battleField[tankHexX][tankHexY].substring(4, 6);
        String typeOfTank = battleField[tankHexX][tankHexY].substring(1, 2);
        boolean flag = true;
        switch (typeOfTank) {
            case "L" -> tank = new LightTank();
            case "H" -> tank = new HeavyTank();
            case "D" -> tank = new TankDestroyer();
            case "S" -> tank = new SelfPropelledGuns();
            default -> tank = new LightTank();
        }
        switch (directionTurret) {
            case "RR" -> {
                for (int j = tankHexX + 1; j < 15; j++) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + tankHexY);
                    if (!flag) {
                        break;
                    }
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                }
            }
            case "RD" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i += 1;
                while (j < 15 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + i);
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i += 1;
                }
            }
            case "LD" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i += 1;
                while (j >= 0 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + i);
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i += 1;
                }
            }
            case "LL" -> {
                for (int j = tankHexX - 1; j >= 0; j--) {
                    String hex = battleField[j][tankHexY].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + tankHexY);
                    if (!flag) {
                        break;
                    }
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                }
            }
            case "LU" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 != 0) {
                    j -= 1;
                }
                i -= 1;
                while (j >= 0 && i >= 0 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + i);
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                    if (i % 2 != 0) {
                        j -= 1;
                    }
                    i -= 1;
                }
            }
            case "RU" -> {
                int j = tankHexX;
                int i = tankHexY;
                if (i % 2 == 0) {
                    j += 1;
                }
                i -= 1;
                while (j < 15 && i < 9 && flag) {
                    String hex = battleField[j][i].substring(0, 1);
                    if (!hex.equals("G") && !hex.equals("W")) {
                        flag = false;
                        if (hex.equals("E")) {
                            break;
                        }
                    }
                    shootingHexes.add(j + " " + i);
                    if (shootingHexes.size() >= tank.getShotRange()) {
                        break;
                    }
                    if (i % 2 == 0) {
                        j += 1;
                    }
                    i -= 1;
                }
            }
        }
    }

    public static boolean drive() {
        if (drivingHexes.contains(currentHex)) {
            if (tanksThatFired.contains(tankHex)) {
                tanksThatFired.remove(tankHex);
                tanksThatFired.add(currentHex);
            }
            int tankHexX = Integer.parseInt(tankHex.split(" ")[0]);
            int tankHexY = Integer.parseInt(tankHex.split(" ")[1]);
            String tank = battleField[tankHexX][tankHexY];
            int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
            int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
            battleField[currentHexX][currentHexY] = tank;
            battleField[tankHexX][tankHexY] = "G";
            return true;
        }
        return false;
    }

    public static boolean ammunitionInStock() {
        int tankX = Integer.parseInt(tankHex.split(" ")[0]);
        int tankY = Integer.parseInt(tankHex.split(" ")[1]);
        if (battleField[tankX][tankY].length() == 7) {
            battleField[tankX][tankY] = battleField[tankX][tankY] + 0;
        }
        if (battleField[tankX][tankY].length() == 8) {
            battleField[tankX][tankY] = battleField[tankX][tankY] + 0;
            return true;
        } else if (Integer.parseInt(battleField[tankX][tankY].substring(8, 9)) < tank.getAmmunition()) {
            return true;
        }
        return false;
    }

    public static ArrayList tankReloading() {
        ArrayList<String> list = new ArrayList<>();
        for (String s : tanksThatFired) {
            list.add(s);
            int tankHexX = Integer.parseInt(s.split(" ")[0]);
            int tankHexY = Integer.parseInt(s.split(" ")[1]);
            battleField[tankHexX][tankHexY] = battleField[tankHexX][tankHexY].substring(0, 8) + 0;
        }
        tanksThatFired.clear();
        return list;
    }

    public static int shoot() {
        if (shootingHexes.contains(currentHex)) {
            int tankHexX = Integer.parseInt(tankHex.split(" ")[0]);
            int tankHexY = Integer.parseInt(tankHex.split(" ")[1]);
            int ammunition = Integer.parseInt(battleField[tankHexX][tankHexY].substring(8, 9));
            ammunition += 1;
            tanksThatFired.add(tankHex);
            battleField[tankHexX][tankHexY] = battleField[tankHexX][tankHexY].substring(0, 8) + ammunition;
            int currentHexX = Integer.parseInt(currentHex.split(" ")[0]);
            int currentHexY = Integer.parseInt(currentHex.split(" ")[1]);
            if (battleField[currentHexX][currentHexY].equals("F")) {
                battleField[currentHexX][currentHexY] = "G";
                return 1;
            } else if (battleField[currentHexX][currentHexY].substring(0, 1).equals("T")
            &&((battleField[currentHexX][currentHexY].substring(6, 7).equals("R")&&PlayerService.getPlayer2())
            ||(battleField[currentHexX][currentHexY].substring(6, 7).equals("B")&&PlayerService.getPlayer1()))) {
                switch (battleField[currentHexX][currentHexY].substring(1, 2)) {
                    case "L" -> tankTarget = new LightTank();
                    case "H" -> tankTarget = new HeavyTank();
                    case "D" -> tankTarget = new TankDestroyer();
                    case "S" -> tankTarget = new SelfPropelledGuns();
                    default -> tankTarget = new LightTank();
                }
                Random r = new Random();
                if (battleField[currentHexX][currentHexY].length() == 7) {
                    if (tank.getDamage() >= tankTarget.getArmor()) {
                        battleField[currentHexX][currentHexY] = "G";
                        return 2;
                    }
                    battleField[currentHexX][currentHexY] = battleField[currentHexX][currentHexY] + tank.getDamage();
                } else {
                    int damage = Integer.parseInt(battleField[currentHexX][currentHexY].substring(7, 8));
                    if (damage + tank.getDamage() >= tankTarget.getArmor()) {
                        battleField[currentHexX][currentHexY] = "G";
                        return 2;
                    }
                    damage += tank.getDamage();
                    battleField[currentHexX][currentHexY] = battleField[currentHexX][currentHexY].substring(0, 7) + damage;
                }
                return 4;
            }
            return 3;
        }
        return 0;
    }

    public static void turn(String turn) {
        int tankHexX = Integer.parseInt(tankHex.split(" ")[0]);
        int tankHexY = Integer.parseInt(tankHex.split(" ")[1]);
        if (PlayerService.getAction()==GameState.TURN_TANK) {
            String left = battleField[tankHexX][tankHexY].substring(0, 2);
            String right = battleField[tankHexX][tankHexY].substring(4);
            switch (turn) {
                case "RR" -> battleField[tankHexX][tankHexY] = left + "RR" + right;
                case "RD" -> battleField[tankHexX][tankHexY] = left + "RD" + right;
                case "LD" -> battleField[tankHexX][tankHexY] = left + "LD" + right;
                case "LL" -> battleField[tankHexX][tankHexY] = left + "LL" + right;
                case "LU" -> battleField[tankHexX][tankHexY] = left + "LU" + right;
                case "RU" -> battleField[tankHexX][tankHexY] = left + "RU" + right;
            }
            if (tank.getFuelForTurnTurret() == -1) {
                String leftT = battleField[tankHexX][tankHexY].substring(0, 4);
                String rightT = battleField[tankHexX][tankHexY].substring(6);
                switch (turn) {
                    case "RR" -> battleField[tankHexX][tankHexY] = leftT + "RR" + rightT;
                    case "RD" -> battleField[tankHexX][tankHexY] = leftT + "RD" + rightT;
                    case "LD" -> battleField[tankHexX][tankHexY] = leftT + "LD" + rightT;
                    case "LL" -> battleField[tankHexX][tankHexY] = leftT + "LL" + rightT;
                    case "LU" -> battleField[tankHexX][tankHexY] = leftT + "LU" + rightT;
                    case "RU" -> battleField[tankHexX][tankHexY] = leftT + "RU" + rightT;
                }
            }
        } else if (PlayerService.getAction()==GameState.TURN_TURRET) {
            String left = battleField[tankHexX][tankHexY].substring(0, 4);
            String right = battleField[tankHexX][tankHexY].substring(6);
            switch (turn) {
                case "RR" -> battleField[tankHexX][tankHexY] = left + "RR" + right;
                case "RD" -> battleField[tankHexX][tankHexY] = left + "RD" + right;
                case "LD" -> battleField[tankHexX][tankHexY] = left + "LD" + right;
                case "LL" -> battleField[tankHexX][tankHexY] = left + "LL" + right;
                case "LU" -> battleField[tankHexX][tankHexY] = left + "LU" + right;
                case "RU" -> battleField[tankHexX][tankHexY] = left + "RU" + right;
            }
        }
    }

}
