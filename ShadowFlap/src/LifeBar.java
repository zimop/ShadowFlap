import bagel.DrawOptions;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for a LifeBar object, which can lose lives, and gain lives(level up), and is attached to a bird
 */
public class LifeBar {
    private final Image LIFE_FULL = new Image("res/level/fullLife.png");
    private final Image LIFE_EMPTY = new Image("res/level/noLife.png");
    private final int LEVEL_0_LIVES = 3;
    private final int SPACE_BETWEEN_LIVES = 50;
    private final Point LIFE_BAR_START_RENDER = new Point(100, 15);
    private final int LEVEL_1_LIVES = 6;
    private final int[] LIFE_LEVEL = {LEVEL_0_LIVES, LEVEL_1_LIVES};
    private int lives;

    /**
     * Constructor method for the LifeBar class
     */
    public LifeBar(){
        lives = LEVEL_0_LIVES;
    }

    /** this method is responsible for rendering the lifebar, with full and empty hearts
     * @param level the current level of the game
     */
    public void renderLives(int level) {
        double heart_x = LIFE_BAR_START_RENDER.x;
        for (int i = 0; i < lives; i++) {
            LIFE_FULL.drawFromTopLeft(heart_x, LIFE_BAR_START_RENDER.y);
            heart_x = heart_x+ SPACE_BETWEEN_LIVES;
        }
        for (int j = lives; j< LIFE_LEVEL[level]; j++) {
            LIFE_EMPTY.drawFromTopLeft(heart_x, LIFE_BAR_START_RENDER.y);
            heart_x = heart_x+ SPACE_BETWEEN_LIVES;
        }
    }

    /**
     * this method decreases the life bar by 1
     */
    public void lifeLost(){
        lives--;
    }

    /** this method checks if the whole life-bar is empty or not
     * @return true if life-bar is empty, false otherwise
     */
    public boolean isEmpty(){
        if (lives == 0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * levels up the lifebar, to 6 full hearts
     */
    public void levelUp(){
        lives = LEVEL_1_LIVES;
    }
}
