package edu.csc413.tankgame.model;

public class BombTank extends Tank {

       /*
       Tank that will try to collide with you. The tank only has one life, so once it hits you it will
       break and be removed. It takes a life if it hits you.
       I changed up the cushion tank code, but much of the function of this tank relies on GameState.
        */

        public BombTank(String id, double x, double y, double angle){
            super(id,x,y,angle);
            setLives(0);
            setSpeed(0.15);
        }

        @Override
        public void resetCoolDown(){
            super.setCoolDownValue(900);
        }

        // I specify that I do not want it to shoot. It functions to destroy itself.

        @Override
        public void shoot(GameState gameState){

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
            if (distance > 0.0){
                moveForward();
            }
            else if(distance < 0.0){
                moveBackward();
            }
        }

    }


