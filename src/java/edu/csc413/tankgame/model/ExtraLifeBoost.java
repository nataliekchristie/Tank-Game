package edu.csc413.tankgame.model;

public class ExtraLifeBoost extends GameEntity{

    //Extra life boosts are yellow hearts that appear at random.
    //The code is written such that there can only be two on screen at a time to avoid an excess.

    private static final String NEW_LIFE_ID_PREFIX = "newlife-";
    private static long uniqueId = 0L;



    public ExtraLifeBoost(String id, double x, double y, double angle) {
        super(id, x, y, angle);
        setID(getUniqueId());
    }

    @Override
    public double getXBound(){
        super.setXSize(25.0);
        return super.getXBound();
    }

    @Override
    public double getYBound(){
        super.setYSize(27.0);
        return super.getYBound();
    }

    private String getUniqueId() {
        return NEW_LIFE_ID_PREFIX + uniqueId++;
    }

    public void move(){
    }



}
