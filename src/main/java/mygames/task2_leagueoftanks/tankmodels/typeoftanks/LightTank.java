package mygames.task2_leagueoftanks.tankmodels.typeoftanks;

import mygames.task2_leagueoftanks.tankmodels.AbstractTank;

public class LightTank extends AbstractTank {
    public LightTank() {
        this.speed = 4;
        this.damage = 1;
        this.armor = 4;
        this.probabilityPenetration = 0.8;
        this.shotRange = 4;
        this.ammunition = 2;
        this.fuelForFire = 1;
        this.fuelForDrive = 2;
        this.fuelForTurnTurret = 0;
        this.fuelForTurnTank = 1;
    }

}
