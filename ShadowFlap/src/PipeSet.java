import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

/** This class defines a PipeSet object, represents a set of pipes
 * which spawn when the game is active
 * used the project 1 solution
 */
public class PipeSet {
    private final Image PIPE_IMAGE = new Image("res/level/plasticPipe.png");
    private final int PIPE_GAP = 168;
    private final int PIPE_INITIAL_SPEED = 5;
    private final double WINDOW_SIZE_Y = 768;
    private final double TIME_SCALE_INCREMENT = 1.5;
    private int timeScale;
    private int[] RANDOM_POSITIONS_LEVEL_0 = {100,300,500};
    private double pipeSpeed = PIPE_INITIAL_SPEED;
    private final double top_pipe_y;
    private final double bottom_pipe_y;
    private final DrawOptions ROTATOR = new DrawOptions().setRotation(Math.PI);
    private double pipeX = Window.getWidth()+pipeSpeed;

    /** Constructor for a pipeset object
     *
     */
    public PipeSet() {
        double addExtra = setRandomizePositions();
        top_pipe_y = -PIPE_GAP/2.0 - addExtra + PIPE_GAP/2.0;
        bottom_pipe_y = Window.getHeight() + PIPE_GAP/2.0 - addExtra + PIPE_GAP/2.0;
        pipeSpeed = PIPE_INITIAL_SPEED*Math.pow(TIME_SCALE_INCREMENT, timeScale-1);
    }

    /**
     * this method draws the pipeSet when the game is active
     */
    public void renderPipeSet() {
        PIPE_IMAGE.draw(pipeX, top_pipe_y);
        PIPE_IMAGE.draw(pipeX, bottom_pipe_y, ROTATOR);
    }

    /** this method updates the position of the PipeSet, since it moves from left to right
     *
     */
    public void update() {
        setPipeX();
        renderPipeSet();
    }

    /** getter method for the Rectangle bounded by the top pipe.
     * @return Rectangle object bounded by the top pipe.
     */
    public Rectangle getTopBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, top_pipe_y));

    }

    /** getter method for the Rectangle bounded by the bottom pipe.
     * @return Rectangle object bounded by the bottom pipe.
     */
    public Rectangle getBottomBox() {
        return PIPE_IMAGE.getBoundingBoxAt(new Point(pipeX, bottom_pipe_y));

    }

    /** getter method for the X coordinate of the pipeset
     * @return x coordinate of the pipeSet
     */
    public double getX(){
        return pipeX;
    }

    /** getter method for the Y coordinate of the top pipe
     * @return y coordinate of the top pipe
     */
    public double getTop_pipe_y() {
        return top_pipe_y;
    }

    /** setter method for the X coordinate of the pipeSet
     */
    public void setPipeX(){
        pipeX -= pipeSpeed;
    }

    /** getter method for the Y coordinate of the bottom pipe
     * @return y coordinate of the bottom pipe
     */
    public double getBottom_pipe_y(){
        return bottom_pipe_y;
    }


    /** getter method for the DrawOptions Rotator of the bottom pipe
     * @return DrawOptions object that rotates the pipe
     */
    public DrawOptions getROTATOR() {
        return ROTATOR;
    }

    /** this method helps to set the centre Y position of the pipeset to a random number in ENUM(100, 300, 500)
     * @return offset value to help achieve the Y positioning correctly
     */
    public double setRandomizePositions(){
        int random = 0 + (int)(Math.random() * ((2 - 0) + 1));
        int space = RANDOM_POSITIONS_LEVEL_0[random];
        double addExtra = (WINDOW_SIZE_Y/2.0-space);
        return addExtra;
    }

    /** getter method For the height of the window
     * @return height of the window
     */
    public double getWINDOW_SIZE_Y(){ return WINDOW_SIZE_Y;}

    /** setter method For the speed of the pipe with the correct timescale
     */
    public void setPipeSpeed(){
        pipeSpeed = PIPE_INITIAL_SPEED*Math.pow(TIME_SCALE_INCREMENT, timeScale-1);
    }

    /** setter method for the timescale
     * @param time the new timescale it must be set to
     */
    public void setTimeScale(int time){
        timeScale = time;
        setPipeSpeed();
    }

    /** getter method for the width of the pipe image
     * @return the width of the pipe image
     */
    public double getPipeWidth(){
        return PIPE_IMAGE.getWidth();
    }

    /** this method checks if the pipeSet is destroyed or not (overriden in subclasses)
     * @param weapon the weapon which has hit the pipeSet
     * @return True if pipe is destroyed, false if not
     */
    public boolean isDestroyed(Weapons weapon){
        return weapon instanceof Rock;
    }

}
