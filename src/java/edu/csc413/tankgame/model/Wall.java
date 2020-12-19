package edu.csc413.tankgame.model;

public class Wall extends GameEntity {
    private static final String WALL_ID_PREFIX = "wall-";
    private static long uniqueId = 0L;
    private String imageFile;

    public Wall(double x, double y, double angle) {
        super(x, y, angle);
    }

    public Wall(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    public Wall(double x, double y, double angle, String imageFile) {
        super(x, y, angle);
        super.setID(getUniqueId());
        this.imageFile = imageFile;
    }

    private String getUniqueId() {
        return WALL_ID_PREFIX + uniqueId++;
    }

    public String getImageFile(){
        return imageFile;
    }
    @Override
    public double getXBound(){
        super.setXSize(32.0);
        return super.getXBound();
    }

    @Override
    public double getYBound(){
        super.setYSize(32.0);
        return super.getYBound();
    }

    @Override
    public void move(GameState gameState){

    }

}
