import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.Objects;

/**
 * This class creates a Bomb object which can be collected and shot, which is a subclass of Weapons
 */

public class Bomb extends Weapons{
    private final Image BOMB= new Image("res/level-1/bomb.png");
    private final int RANGE = 50;
    private int frameCount = 0;

    /**
     * Constructor method to create a new bomb object
     */
    public Bomb(){
        super();
    }

    /**
     * renders the bomb
     */
    @Override
    public void renderWeapon(){
        BOMB.draw(getWeaponX(), getWeaponY());
    }

    /** getter method for the Rectangle bounded by the bomb
     * @return Rectangle bounded by the bomb
     */
    @Override
    public Rectangle getBox(){
        return BOMB.getBoundingBoxAt(new Point(getWeaponX(), getWeaponY()));
    }

    /** renders the bomb on the beak of the bird, when it acquires it
     * @param x this parameter is the current x value of the bird
     * @param y this parameter is the current y value of the bird
     */
    public void renderHasWeapon(double x, double y){
        setWeaponX(x+BOMB.getWidth());
        setWeaponY(y);
        BOMB.draw(x+BOMB.getWidth(), y);
    }

    /** getter method for the bomb image width
     * @return the width of the bomb image
     */
    @Override
    public double getWeaponWidth() {
        return BOMB.getWidth();
    }

    /** this method checks and renders the shooting of a bomb held by a bird
     * @param hit_target this parameter is true if the bomb has hit a target and false otherwise
     * @return returns true if the range has not been reached, returns false if the range has been reached,
     * or the bomb has hit the target.
     */
    @Override
    public boolean shoot(boolean hit_target){
        if(hit_target){
            frameCount = 0;
            return false;
        }
        setWeaponX(getWeaponX() + getWEAPON_INITIAL_SPEED());
        renderWeapon();
        frameCount++;
        if(frameCount == RANGE){
            frameCount = 0;
            return false;
        }
        return true;
    }

}
