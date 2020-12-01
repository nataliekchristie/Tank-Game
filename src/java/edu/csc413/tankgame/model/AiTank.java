package edu.csc413.tankgame.model;

public class AiTank extends Tank {

    int count = 0;

    public AiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

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
    @Override
    public void move(GameState gameState){
        generateMovement();
        super.move(gameState);
    }
}
