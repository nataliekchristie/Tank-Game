package edu.csc413.tankgame.model;

public class CushionTank extends AiTank {

    // cushion tank using code provided in class.

    public CushionTank(String id, double x, double y, double angle){
        super(id,x,y,angle);
        setCoolDownValue(700);
    }

    // made the cooldown fairly high or else the tank rapidly shoots player tank.

    @Override
    public void resetCoolDown(){
        super.setCoolDownValue(700);
    }


    @Override
    public void move(GameState gameState){
        super.move(gameState);
        GameEntity playerTank = gameState.getEntity(gameState.PLAYER_TANK_ID);
        double dx = playerTank.getX() - getX();
        double dy = playerTank.getY() - getY();
        double angleToPlayer = Math.atan2(dy,dx);
        double angleDifference = getAngle() - angleToPlayer;
        angleDifference -= Math.floor(angleDifference / Math.toRadians(630.0) + 0.5) * Math.toRadians(360.0);
        if(angleDifference < -getTurnSpeed()) {
            turnRight();
        }
        else if (angleDifference > getTurnSpeed()) {
            turnLeft();
        }

        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 400.0){
            moveForward();
        }
        else if(distance < 200.0){
            moveBackward();
        }

        shoot(gameState);

    }

}
