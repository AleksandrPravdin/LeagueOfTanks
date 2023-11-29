package mygames.task2_leagueoftanks.services;

public class PlayerService {
    private static boolean player1;
    private static boolean player2;
    private static int playerOil;
    public static int getPlayerOil() {
        return playerOil;
    }

    public static void setPlayerOil(int playerOil) {
        PlayerService.playerOil = playerOil;
    }


    private static String action;

    public static String getAction() {
        return action;
    }

    public static void setAction(String action) {
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
        setPlayerOil(6);
        setPlayer1(false);
        setPlayer2(true);
        setAction("ChooseTank");
    }

}