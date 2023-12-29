package mygames.task2_leagueoftanks.tankmodels.typeoftanks;

import mygames.task2_leagueoftanks.tankmodels.AbstractTank;

public class SelfPropelledGuns extends AbstractTank {
    public int longRangeDamage;
    public int longRange;
    public double probabilityPenetrationLRD;
    public SelfPropelledGuns() {
        this.speed = 1;
        this.damage = 3;
        this.longRangeDamage = 2;
        this.armor = 3;
        this.probabilityPenetration = 0.9;
        this.probabilityPenetrationLRD = 0.55;
        this.shotRange = 4;
        this.longRange = 7;
        this.ammunition = 1;
        this.fuelForFire = 2;
        this.fuelForDrive = 2;
        this.fuelForTurnTurret = 2;
        this.fuelForTurnTank = 2;
        this.name="ПТ САУ";
    }
}
