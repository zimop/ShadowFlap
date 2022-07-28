import bagel.*;
import bagel.util.Rectangle;

/**
 *  Weapons super class, which defines a weapon object,
 *  which is spawned between pipes.
 *  this weapon object can be picked up by the bird,
 *  and can be thrown at pipes destroying them
 *  or not.
 */
public abstract class Weapons {
    private final int WEAPON_INITIAL_SPEED = 5;
    private final double TIME_SCALE_INCREMENT = 1.5;
    private int timeScale;
    private double weaponSpeed;
    private double weaponX = Window.getWidth() + weaponSpeed;
    private double weaponY;


    /**
     * this is the constructor for the WEAPONS class
     * sets, the weaponSpeed with a random position
     */
    public Weapons(){
        weaponSpeed = WEAPON_INITIAL_SPEED;
        setY();
    }

    /**
     * abstract method to draw a weapon
     */

    public abstract void renderWeapon();

    /**
     * weapon is picked up by the bird,
     * and renders the weapon at the birds beak
     * @param x this parameter is the current x value of the bird
     * @param y this parameter is the current y value of the bird
     */
    public abstract void renderHasWeapon(double x, double y);

    /**
     * This method updates the rendering of the weapons
     */
    public void update(){
        weaponX -= weaponSpeed;
        renderWeapon();

    }

    /** this method renders the shooting of a weapon held by a bird
     * @param hit_target this parameter is true if the weapon has hit a target and false otherwise
     * @return returns true if the range has not been reached, returns false if the range has been reached,
     * or the weapon has hit the target.
     */
   public abstract boolean shoot(boolean hit_target);

    /**
     * this method sets the timescale to whichever timeScale is reported, then sets the appropriate weaponSpeed
     * @param time this parameter is the value of the timeScale(e.g 1, 2 ...)
     */
    public void setTimeScale(int time){
        timeScale = time;
        setWeaponSpeed();
    }

    /** getter method for the X coordinate of the weapon
     * @return X coordinate of the weapon
     */
    public double getWeaponX(){return weaponX;}

    /** getter method for the Y coordinate of the weapon
     * @return Y coordinate of the weapon
     */
    public double getWeaponY() {
        return weaponY;
    }

    /** Setter method for the Y value, which sets a random position between 100- 500 for the Y value
     * of the weapon when it spawns
     */
    public void setY(){
        int random = 100 + (int)(Math.random() * ((500 - 100) + 1));
        setWeaponY(random);
    }

    /** getter method for the image width of the weapon
     * @return the width of the weapon image from the associated weapon
     */
    public abstract double getWeaponWidth();

    /** setter method for the speed of the weapon with the appropriate timescale
     */
    public void setWeaponSpeed(){
        weaponSpeed = WEAPON_INITIAL_SPEED*Math.pow(TIME_SCALE_INCREMENT, timeScale-1);
    }

    /** setter method for the y coordinate of the weapon
     * @param weaponY this is the Y coordinate of the weapon
     */
    public void setWeaponY(double weaponY) {
        this.weaponY = weaponY;
    }

    /** setter method for the X coordinate of the weapon
     * @param weaponX this is the X coordinate of the weapon
     */
    public void setWeaponX(double weaponX) {
        this.weaponX = weaponX;
    }

    /** getter method for the initial speed of the weapon
     * @return the weapon initial speed constant
     */
    public int getWEAPON_INITIAL_SPEED(){
        return WEAPON_INITIAL_SPEED;
    }

    /** getter method for the Rectangle of the weapon
     * @return this is a Rectangle object representing the rectangle bounded by the image
     */
    public abstract Rectangle getBox();
}
