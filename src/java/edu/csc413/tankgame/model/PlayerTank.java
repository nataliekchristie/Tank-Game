package edu.csc413.tankgame.model;


public class PlayerTank extends Tank{

    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
        setLives(4);
        // doing more so the tanks don't immediately kill the player
    }
 // set lower cooldown to compensate for how easily the cushion tank can hit you.
    @Override
    public void resetCoolDown(){
        setCoolDownValue(300);
    }


}
