package entities;

public abstract class Monster extends Character {
    protected int blood;
    protected int damage;

    public Monster(double x, double y, int blood, int damage, int speed) {
        super(x, y, speed);
        this.blood = Math.max(blood, 1);
        this.damage = Math.max(damage, 1);
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = Math.max(blood, 1);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(damage, 1);
    }
}
