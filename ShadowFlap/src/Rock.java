import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * This class creates a Rock object which can be collected and shot, which is a subclass of Weapons
 */
public class Rock extends Weapons{
    private final Image ROCK = new Image("res/level-1/rock.png");
    private final int RANGE = 30;
    private int frameCount = 0;  // counter variable for the number of frames the weapon can shoot for

    /**
     * Constructor method to create a new Rock object
     */
    public Rock(){
        super();
    }

    /**
     * renders the weapon
     */
    @Override
    public void renderWeapon(){
        ROCK.draw(getWeaponX(), getWeaponY());
    }

    /** getter method for the Rectangle bounded by the rock
     * @return Rectangle bounded by the rock
     */
    @Override
    public Rectangle getBox(){
        return ROCK.getBoundingBoxAt(new Point(getWeaponX(), getWeaponY()));
    }

    /** renders the weapon on the beak of the bird, when it acquires it
     * @param x this parameter is the current x value of the bird
     * @param y this parameter is the current y value of the bird
     */
    @Override
    public void renderHasWeapon(double x, double y){
        setWeaponX(x+ROCK.getWidth());
        setWeaponY(y);
        ROCK.draw(getWeaponX(), getWeaponY());
    }

    /** getter method for the rock image width
     * @return the width of the rock image
     */
    @Override
    public double getWeaponWidth() {
        return ROCK.getWidth();
    }

    /** this method checks and renders the shooting of a rock held by a bird
     * @param hit_target this parameter is true if the weapon has hit a target and false otherwise
     * @return returns true if the range has not been reached, returns false if the range has been reached,
     * or the rock has hit the target.
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
        if(frameCount == RANGE){ // if the rock has reached its range
            frameCount = 0;
            return false;
        }
        return true;
    }
}
