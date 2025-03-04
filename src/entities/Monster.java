package entities;

import game.GameController;

public abstract class Monster extends Character {
    protected int blood;
    protected int damage;
    protected int boxWidth;
    protected int boxHeight;
    protected int plusToCenX;
    protected int plusToCenY;
    
    public Monster(double x, double y, int blood, int damage, int speed) {
        super(x, y, speed);
        this.blood = Math.max(blood, 1);
        this.damage = Math.max(damage, 1);
    }
    
    public abstract void update();
    public abstract void staggerAnimation();
    public abstract void playDeathAnimation();
    
    public boolean getAttack(int damage) {
    	// Center
    	double cenBoxX = this.getLayoutX() + (this.plusToCenX * GameController.getScale());
    	double cenBoxY = this.getLayoutY() + (this.plusToCenY * GameController.getScale());
    
    	// Rectangle box
		double rectX1 = cenBoxX - (this.boxWidth * GameController.getScale());
		double rectY1 = cenBoxY - (this.boxHeight * GameController.getScale());
		double rectX2 = cenBoxX + (this.boxWidth * GameController.getScale());
		double rectY2 = cenBoxY + (this.boxHeight * GameController.getScale());
	
	    // Given external point (x2, y2)
		double playerFootX = GameController.getGamePane().getPlayer().getX() + (8 + 24 - 8) * GameController.getScale();
		double playerFootY = GameController.getGamePane().getPlayer().getY() + (32) * GameController.getScale();
		
	    // Compute the closest point on the rectangle
		double closestX = Math.max(rectX1, Math.min(playerFootX, rectX2));
		double closestY = Math.max(rectY1, Math.min(playerFootY, rectY2));
	    
//	    double distance = Math.sqrt(Math.pow(closestX - playerFootX, 2) + Math.pow(closestY - playerFootY, 2));
	    
	    
	    double radius = 12 * GameController.getScale();

	    
	    if(isInRange(playerFootX, playerFootY, closestX, closestY, radius)) {
	        boolean inFront = false;	        
	        switch (GameController.getGamePane().getPlayer().getLastDirection()) {
	            case "up":
	                inFront = (cenBoxY < playerFootY);
	                break;
	            case "down":
	                inFront = (cenBoxY > playerFootY);
	                break;
	            case "left":
	                inFront = (cenBoxX < playerFootX);
	                break;
	            case "right":
	                inFront = (cenBoxX > playerFootX);
	                break;
	            default:
	                inFront = false;
	        }
	        if (inFront) {
	        	staggerAnimation();
	            if (this.blood - damage <= 0) {
	            	playDeathAnimation();
	                return true;
	            } else {
	                this.blood = this.blood - damage;
	            }
	        }
	    }
	    return false;

    }
    
    public static boolean isInRange(double x0, double y0, double x, double y, double radius) {
        return Math.pow(x - x0, 2) + Math.pow(y - y0, 2) <= Math.pow(radius, 2);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = Math.max(damage, 1);
    }
    
    
}
