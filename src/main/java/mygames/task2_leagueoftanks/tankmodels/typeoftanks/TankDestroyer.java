package mygames.task2_leagueoftanks.tankmodels.typeoftanks;

import mygames.task2_leagueoftanks.tankmodels.AbstractTank;

public class TankDestroyer extends AbstractTank {
    public TankDestroyer() {
        this.speed = 2;
        this.damage = 3;
        this.armor = 5;
        this.probabilityPenetration = 0.9;
        this.shotRange = 6;
        this.ammunition = 1;
        this.fuelForFire = 2;
        this.fuelForDrive = 2;
        this.fuelForTurnTank = 2;
        this.fuelForTurnTurret = -1;
        this.name="САУ";
    }

}
