package edu.csc413.tankgame.model;

/**
 * Model class representing a shell that has been fired by a tank. A shell has a position and an angle, as well as a
 * speed. Shells by default should be unable to turn and only move forward.
 */
// TODO: Notice that Shell has a lot in common with Tank. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public class Shell extends GameEntity{
    private static final String SHELL_ID_PREFIX = "shell-";
    private static final double MOVEMENT_SPEED = 4.0;
    private static long uniqueId = 0L;

    public Shell(double x, double y, double angle) {
        super(x,y,angle);
        super.setID(getUniqueId());
        super.setSpeed(MOVEMENT_SPEED);
    }

    private String getUniqueId() {
        return SHELL_ID_PREFIX + uniqueId++;
    }

    public void shoot(GameState gamestate){
        super.moveForward();
    }

    /*
    Not necessary, just working with ideas for the move update.
     */


}
