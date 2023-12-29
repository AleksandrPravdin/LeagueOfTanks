package mygames.task2_leagueoftanks.services;

public class PlayerService {
    private static final int TIME = 300;
    private static final int COUNT_TANK = 5;
    private static boolean player1;
    private static boolean player2;
    private static int players1Time = TIME;
    private static int players2Time = TIME;
    private static int countTankP1=COUNT_TANK;
    private static int countTankP2=COUNT_TANK;
    private static int playerOil;

    public static int getCountTankP1() {
        return countTankP1;
    }

    public static void setCountTankP1(int countTankP1) {
        PlayerService.countTankP1 = countTankP1;
    }

    public static int getCountTankP2() {
        return countTankP2;
    }

    public static void setCountTankP2(int countTankP2) {
        PlayerService.countTankP2 = countTankP2;
    }

    public static int getPlayers1Time() {
        return players1Time;
    }

    public static void setPlayers1Time(int players1Time) {
        PlayerService.players1Time = players1Time;
    }

    public static int getPlayers2Time() {
        return players2Time;
    }

    public static void setPlayers2Time(int players2Time) {
        PlayerService.players2Time = players2Time;
    }

    public static int getPlayerOil() {
        return playerOil;
    }

    public static void setPlayerOil(int playerOil) {
        PlayerService.playerOil = playerOil;
    }


    private static GameState action;

    public static GameState getAction() {
        return action;
    }

    public static void setAction(GameState action) {
        PlayerService.action = action;
    }

    public static boolean getPlayer1() {
        return player1;
    }

    public static void setPlayer1(boolean player1) {
        PlayerService.player1 = player1;
    }

    public static boolean getPlayer2() {
        return player2;
    }

    public static void setPlayer2(boolean player2) {
        PlayerService.player2 = player2;
    }


    public static void createPlayers() {
        setCountTankP1(COUNT_TANK);
        setCountTankP2(COUNT_TANK);
        setPlayers1Time(TIME);
        setPlayers2Time(TIME);
        setPlayerOil(8);
        setPlayer1(false);
        setPlayer2(true);
        setAction(GameState.CHOOSE_TANK);
    }

}