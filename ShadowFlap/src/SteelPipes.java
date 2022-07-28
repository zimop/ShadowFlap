import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/** this class creates a Steel pipes object, which inherits the PipeSet class
 * These pipes can shoot flames.
 *
 */
public class SteelPipes extends PipeSet{
    private final Image STEEL_PIPE = new Image("res/level-1/steelPipe.png");
    private final Image FLAME = new Image("res/level-1/flame.png");
    private final int PIPE_CENTRE_MAX = 500;
    private final int PIPE_CENTRE_MIN = 100;
    private final int FLAME_ALIVE_FRAMES = 30;                           // how long the flame lasts
    private final int FLAME_SPAWN_RATE = 20;                            // how often flames spawn
    private double topFlameY = getTop_pipe_y()+FLAME.getHeight()/2.0+getWINDOW_SIZE_Y()/2.0-10;    //demo video values (10 pixels extra)
    private double bottomFlameY = getBottom_pipe_y()-FLAME.getHeight()/2.0-getWINDOW_SIZE_Y()/2.0+10;
    private int countFlameAlive = 0;    // counter variable for the number of frames passed for the flames to stay alive
    private int countSpawnRate = 0;     // counter variable for the number of frames passed for the flames to respawn


    /**
     * Constructor method for the steelPipes class
     */
    public SteelPipes(){
        super();
    }

    /** this method updates the current state of the Steel pipes
     *
     */
    @Override
    public void update(){
        setPipeX();
        renderPipeSet();

    }

    /**
     * renders the pipeSet, by drawing
     */
    @Override
    public void renderPipeSet(){
        STEEL_PIPE.draw(getX(), getTop_pipe_y());
        STEEL_PIPE.draw(getX(), getBottom_pipe_y(), getROTATOR());
    }

    /** attempts to render a flame, at the correct flame rate, and for how long it is alive
     * @return returns if the flame is still alive or not
     */

    public boolean flameRender(){
        if(countSpawnRate == FLAME_SPAWN_RATE || countFlameAlive > 0) {
            FLAME.draw(getX(), topFlameY);
            FLAME.draw(getX(), bottomFlameY, getROTATOR());
            countSpawnRate = 0;
            countFlameAlive++;
            if(countFlameAlive == FLAME_ALIVE_FRAMES){
                countFlameAlive = 0;
            }
            return true;
        }else {
            countSpawnRate++;
            return false;
        }
    }

    /** getter method for the rectangle bounded by the top pipes flame
     * @return Rectangle object bounded by the top pipes flame
     */
    public Rectangle getFlameTopBox(){
        return FLAME.getBoundingBoxAt(new Point(getX(), topFlameY));
    }

    /** getter method for the rectangle bounded by the bottom pipes flame
     * @return Rectangle object bounded by the bottom pipes flame
     */
    public Rectangle getFlameBottomBox(){
        return FLAME.getBoundingBoxAt(new Point(getX(), bottomFlameY));
    }

    /** getter method for the rectangle bounded by the top pipe
     * @return Rectangle object bounded by the top pipe
     */
    @Override
    public Rectangle getTopBox() {
        return STEEL_PIPE.getBoundingBoxAt(new Point(getX(), getTop_pipe_y()));
    }

    /** getter method for the rectangle bounded by the bottom pipe
     * @return Rectangle object bounded by the bottom pipe
     */
    @Override
    public Rectangle getBottomBox() {
        return STEEL_PIPE.getBoundingBoxAt(new Point(getX(), getBottom_pipe_y()));

    }

    /** Sets a the pipeSet centre position to a value between 100-500
     * @return the offset value so that the pipeset spawns at the correct centre value
     */
    @Override
    public double setRandomizePositions(){
        double random = PIPE_CENTRE_MIN + (Math.random() * ((PIPE_CENTRE_MAX - PIPE_CENTRE_MIN) + 1));
        double addExtra = (getWINDOW_SIZE_Y()/2.0- random);
        return addExtra;
    }

    /** this method checks if a weapon has destroyed a pipeset on contact
     * @param weapon the weapon which has hit the pipeSet
     * @return true if destroyed, false if not
     */
    public boolean isDestroyed(Weapons weapon){
        return weapon instanceof Bomb;
    }
}
