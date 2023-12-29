package mygames.task2_leagueoftanks.tankmodels.typeoftanks;

import mygames.task2_leagueoftanks.tankmodels.AbstractTank;

public class HeavyTank extends AbstractTank {
    public HeavyTank() {
        this.speed = 2;
        this.damage = 2;
        this.armor = 6;
        this.probabilityPenetration = 0.8;
        this.shotRange = 5;
        this.ammunition = 2;
        this.fuelForFire = 2;
        this.fuelForDrive = 2;
        this.fuelForTurnTurret = 1;
        this.fuelForTurnTank = 2;
        this.name="Тяжёлый танк";
    }
}
