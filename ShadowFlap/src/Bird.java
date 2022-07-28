import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;
import java.lang.Math;

/**
 * used the project 1 solution
 * this class creates a Bird Object, which is can fly up and down, crash and score points, and obtain weapons
 */
//start of solution project1
public class Bird {
    private final Image WING_DOWN_IMAGE = new Image("res/level-0/birdWingDown.png");
    private final Image WING_UP_IMAGE = new Image("res/level-0/birdWingUp.png");
    private final Image WING_UP_IMAGE_LEVEL_1 = new Image("res/level-1/birdWingUp.png");
    private final Image WING_DOWN_IMAGE_LEVEL_1 = new Image("res/level-1/birdWingDown.png");
    private final double X = 200;
    private final double FLY_SIZE = 6;
    private final double FALL_SIZE = 0.4;
    private final double INITIAL_Y = 350;
    private final double Y_TERMINAL_VELOCITY = 10;
    private final double SWITCH_FRAME = 10;        //number of frames between switching of images
    private int frameCount = 0;    //count variable for the number of frames to switch the bird images, to maintain the flapping motion
    private double y;
    private LifeBar lifeBar;
    private double yVelocity;
    private Rectangle boundingBox;
    private Weapons weapon;
    private Image currentImageUp;       //level 1 and 0 have different images, this keeps track of which image to use
    private Image currentImageDown;

    /**
     * Constructor method for the Bird class
     */
    public Bird() {
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
        lifeBar = new LifeBar();
        currentImageUp = WING_UP_IMAGE;
        currentImageDown = WING_DOWN_IMAGE;
        weapon = null;
    }

    /** this method is responsible for updating the state of the bird, and its positioning
     * @param input input of the keyboard
     * @param level current level
     * @return rectangle bounded by the bird
     */
    public Rectangle update(Input input, int level) {
        frameCount += 1;
        lifeBar.renderLives(level);
        if(weapon != null) {
            weapon.renderHasWeapon(X, y);
        }
        if (input.wasPressed(Keys.SPACE)) {
            yVelocity = -FLY_SIZE;
            currentImageDown.draw(X, y);
        }
        else {
            yVelocity = Math.min(yVelocity + FALL_SIZE, Y_TERMINAL_VELOCITY);
            if (frameCount % SWITCH_FRAME == 0) {
                currentImageUp.draw(X, y);
                boundingBox = WING_UP_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
            else {
                currentImageDown.draw(X, y);
                boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
            }
        }
        y += yVelocity;

        return boundingBox;
    }

    /** takes 1 life away
     *  @return true if life is empty, false otherwise
     * */
    public boolean birdLoseLife(){
        lifeBar.lifeLost();
        if(lifeBar.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    /** getter method for the Y coordinate of the bird
     * @return y coordinate of the bird
     */
    public double getY() {
        return y;
    }

    /** getter method for the x coordinate of the bird
     * @return x coordinate of the bird
     */
    public double getX() {
        return X;
    }

    /**
     * this method respawns the bird, at its original location
     * when it goes out of bounds
     */
    public void birdRespawn(){
        y = INITIAL_Y;
        yVelocity = 0;
        boundingBox = WING_DOWN_IMAGE.getBoundingBoxAt(new Point(X, y));
    }


    /**
     * this method levels up the bird
     */
    public void levelUp(){
        currentImageUp = WING_UP_IMAGE_LEVEL_1;
        currentImageDown = WING_DOWN_IMAGE_LEVEL_1;
        lifeBar.levelUp();
    }

    /** shoots the weapon from the bird, then the bird loses the weapon
     * @return the weapon that has been shot
     */
    public Weapons shootWeapon(){
        Weapons temp = weapon;
        loseWeapon();
        return temp;
    }

    /**
     * makes the weapon null
     */
    public void loseWeapon(){
        weapon = null;
    }

    /** this method acquires a new weapon for the bird
     * @param new_weapon the type of weapon that is acquired
     */
    public void acquireWeapon(Weapons new_weapon){
        weapon = new_weapon;
    }

    /** this method checks if a bird has a weapon or not
     * @return true if bird has a weapon, false otherwise
     */
    public boolean hasWeapon(){
        if(weapon ==null){
            return false;
        }
        return true;
    }

    /** getter method for the Rectangle bounded by the bird
     * @return Rectangle bounded by the bird
     */
    public Rectangle getBox() {
        return boundingBox;
    }
    //end of solution project1
}