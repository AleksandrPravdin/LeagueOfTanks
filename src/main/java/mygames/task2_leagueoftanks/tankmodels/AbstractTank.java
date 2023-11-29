package mygames.task2_leagueoftanks.tankmodels;

public class AbstractTank {
    public int armor;
    public int damage;
    public int speed;
    public int shotRange;
    public double probabilityPenetration;
    public int ammunition;
    public int fuelForFire;
    public int fuelForDrive;
    public int fuelForTurnTurret;
    public int fuelForTurnTank;


    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getShotRange() {
        return shotRange;
    }

    public void setShotRange(int shotRange) {
        this.shotRange = shotRange;
    }

    public double getProbabilityPenetration() {
        return probabilityPenetration;
    }

    public void setProbabilityPenetration(double probabilityPenetration) {
        this.probabilityPenetration = probabilityPenetration;
    }

    public int getAmmunition() {
        return ammunition;
    }

    public void setAmmunition(int ammunition) {
        this.ammunition = ammunition;
    }

    public int getFuelForFire() {
        return fuelForFire;
    }

    public void setFuelForFire(int fuelForFire) {
        this.fuelForFire = fuelForFire;
    }

    public int getFuelForDrive() {
        return fuelForDrive;
    }

    public void setFuelForDrive(int fuelForDrive) {
        this.fuelForDrive = fuelForDrive;
    }

    public int getFuelForTurnTurret() {
        return fuelForTurnTurret;
    }

    public void setFuelForTurnTurret(int fuelForTurnTurret) {
        this.fuelForTurnTurret = fuelForTurnTurret;
    }

    public int getFuelForTurnTank() {
        return fuelForTurnTank;
    }

    public void setFuelForTurnTank(int fuelForTurnTank) {
        this.fuelForTurnTank = fuelForTurnTank;
    }
}
