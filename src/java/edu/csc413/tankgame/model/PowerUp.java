package edu.csc413.tankgame.model;

public class PowerUp extends GameEntity {

    //This power up allows you to shoot shells rapidly for a set amount of time.

    public PowerUp(String id, double x, double y, double angle) {
        super(id, x, y, angle);

    }

    @Override
    public double getXBound(){
        super.setXSize(24.0);
        return super.getXBound();
    }

    @Override
    public double getYBound(){
        super.setYSize(24.0);
        return super.getYBound();
    }

}
