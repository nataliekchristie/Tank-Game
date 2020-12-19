package edu.csc413.tankgame.model;

public class AiTank extends Tank {

    // Dumb tank. Points and shoots from same position.

    public AiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
        super.setCoolDownValue(700);
    }

    @Override
    public void resetCoolDown(){
        super.setCoolDownValue(700);
    }

/*
    public void generateMovement(){
        if(count>0){
            count--;
            return;
        }
        int x = 1 + (int)(Math.random() * ((5-1)+ 1));
        switch(x){
            case 1: switchUpOn();
                break;
            case 2: switchDownOn();
                break;
            case 3: switchRightOn();
                break;
            case 4: switchLeftOn();
                break;
            case 5: switchOff();
                break;
        }
        count = 0;
    }
*/
    // ^^ This was just a little sloppily written movement code for when I was playing around.
    // Ultimately I chose to keep it a dumb tank but left here in case I want to play around.

    public void move(GameState gameState){
       super.shoot(gameState);
       // generateMovement();
        super.move(gameState);
    }

}
