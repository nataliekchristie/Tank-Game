package edu.csc413.tankgame.model;

public class GameEntity {
    private String id;
    private double x;
    private double y;
    private double angle;
    private double speed;
    private boolean up,down,right,left,shot = false;
    private double turnSpeed;
    private Shell shell;

    public GameEntity(double x, double y, double angle){
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public GameEntity(String id, double x, double y, double angle){
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public String getId(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed(){
        return speed;
    }

    public void setTurnSpeed(double turnSpeed){
        this.turnSpeed = turnSpeed;
    }

    public String getID(){
        return id;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getAngle(){
        return angle;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public void setAngle(double angle){
        this.angle = angle;
    }

    protected void moveForward() {
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
    }


    protected void moveBackward() {
        double x = getX();
        double y = getY();
        x -= getSpeed() * Math.cos(getAngle());
        y -= getSpeed() * Math.sin(getAngle());
        setX(x);
        setY(y);
    }


    protected void turnLeft() {
        double angle = getAngle();
        angle -= turnSpeed;
        setAngle(angle);
    }

    protected void turnRight() {
        double angle = getAngle();
        angle += turnSpeed;
        setAngle(angle);
    }



    public void move(GameState gamestate){
        if(up){
            moveForward();
        }
        else if(down){
            moveBackward();
        }
        else if(right){
            turnRight();
        }
        else if(left){
            turnLeft();
        }
    }

    public void switchUpOn(){
        up = true;
    }

    public void switchDownOn(){
        down = true;
    }

    public void switchLeftOn(){
        left = true;
    }

    public void switchRightOn(){
        right = true;
    }
    public void switchOff(){
        up = false;
        down = false;
        left = false;
        right = false;
    }

    public void createShell() {
    }

}
