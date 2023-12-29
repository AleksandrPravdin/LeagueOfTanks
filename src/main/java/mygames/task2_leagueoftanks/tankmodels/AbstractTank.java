package mygames.task2_leagueoftanks.tankmodels;

public class AbstractTank {
    protected String name;
    protected int armor;
    protected int damage;
    protected int speed;
    protected int shotRange;
    protected double probabilityPenetration;
    protected int ammunition;
    protected int fuelForFire;
    protected int fuelForDrive;
    protected int fuelForTurnTurret;
    protected int fuelForTurnTank;

    public String getName() {
        return name;
    }
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
