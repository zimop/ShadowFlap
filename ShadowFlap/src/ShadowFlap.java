import bagel.*;
import bagel.util.Rectangle;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.ArrayList;

/**
 * SWEN20003 Project 2, Semester 2, 2021
 *
 * @author Zimo peng
 * used the solution to project1
 * this class runs the shadowFlap game which inherits the AbstractGame class
 */
//start of solution project1
public class ShadowFlap extends AbstractGame {
    private final Image BACKGROUND_IMAGE = new Image("res/level-0/background.png");
    private final Image BACKGROUND_IMAGE_LEVEL_1 = new Image("res/level-1/background.png");
    private final String INSTRUCTION_MSG = "PRESS SPACE TO START";
    private final String SHOOTING_MSG = "PRESS S TO SHOOT";
    private final String GAME_OVER_MSG = "GAME OVER!";
    private final String CONGRATS_MSG = "CONGRATULATIONS!";
    private final int SHOOT_MSG_OFFSET = 68;
    private final String SCORE_MSG = "SCORE: ";
    private final String LEVEL_UP_MSG = "LEVEL-UP!";
    private final String FINAL_SCORE_MSG = "FINAL SCORE: ";
    private final int FONT_SIZE = 48;
    private final Font FONT = new Font("res/font/slkscr.ttf", FONT_SIZE);
    private final int SCORE_MSG_OFFSET = 75;
    private final int WIN_SCORE_LEVEL_0 = 10;
    private final int WIN_SCORE_LEVEL_1 = 30;
    private final double INITIAL_SPAWNING_RATE = 100;
    private final double TIME_SCALE_INCREMENT = 1.5;
    private final int LEVEL_UP_SCREEN_FRAMES = 150;
    private Bird bird;
    private ArrayList<PipeSet> pipeSet = new ArrayList<>();
    private ArrayList<Weapons> weapons = new ArrayList<>();
    private final int FIRST_ELEM = 0;
    private int score;                          // the current score of the game
    private boolean gameOn;
    private int collision;
    private int weaponCollision;                /* stores an int that if weaponCollision > 0
                                                 then a bird has collided with a weapon (acquired)*/

    private int pipe_weapon_collision = 0;      // stores an int that if > 0 then a pipe has collided with a weapon
    private boolean weapon_hit_target = false;
    private boolean win;
    private int level;
    private int timeScale;
    private int pipeFrameCount;
    private int currPipeIndex = 0;              // the index of the closest incoming pipe.
    private int levelUpFrameRate = 0;           // keeps count of how many frames have passed for the level up screen
    private double spawning_rate;               // current spawning rate of pipes (changes with timescale)
    private boolean gameOver;
    private Image currentImage;
    private int currentWinScore;                // the score needed to win, changes with level 0 or level 1
    private boolean levelUp;                    // true if the bird has passed level 0
    private int weaponFrameCount = 0;           // keeps count of the frames passed, used for the spawning of weapons
    private int weaponOffset = 0;               // used to offset the spawning of weapons, so that they do not overlap with pipes
    private boolean shot = false;               // variable to check if a bird has shot a weapon or not
    private boolean weaponCollided = false;     // boolean to check if weapon has collided with a pipe
    private boolean stillShooting = true;       // boolean to check if a weapon is still shooting within its range
    private Weapons shotWeapon;                 // Weapon object which stores the weapon that was shot


    /**
     * The constructor for the ShadowFlap Class.
     */
    public ShadowFlap() {
        super(1024, 768, "ShadowFlap");
        bird = new Bird();
        timeScale = 1;
        PipeSet pipes = new PipeSet();
        pipes.setTimeScale(timeScale);
        pipeSet.add(pipes);
        score = 0;
        gameOn = false;
        collision = 0;
        win = false;
        level = 0;
        gameOver = false;
        spawning_rate = INITIAL_SPAWNING_RATE;
        currentImage = BACKGROUND_IMAGE;
        currentWinScore = WIN_SCORE_LEVEL_0;
        levelUp = false;

    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowFlap game = new ShadowFlap();
        game.run();
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        currentImage.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        boolean collided = false;
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        // game has not started
        if (!gameOn) {
            renderInstructionScreen(input, level);
        }

        // game in level up screen
        if (levelUp) {
            renderLevelUpScreen();
            if (levelUpFrameRate == LEVEL_UP_SCREEN_FRAMES) {
                levelUp = false;
                levelUp();
                gameOn = false;
            } else {
                levelUpFrameRate++;
            }
        }

        // game level x won
        if (win) {
            if (level == 0) {
                levelUp = true;
                win = false;
            } else {
                renderWinScreen();
            }
        }

        // game has been lost
        if (gameOver) {
            renderGameOverScreen();
        }

        // game is active
        if (!gameOver && !levelUp && gameOn && collision == 0 && !win && !birdOutOfBound()) {
            Rectangle birdBox = bird.getBox();
            pipeSpawning(birdBox);
            weaponSpawning(birdBox);
            bird.update(input, level);
            updateScore();
        }

        // timescale has been increased
        if (input.wasPressed(Keys.L) && timeScale < 5) {
            boolean increase = true;
            changeTimeScale(increase);
        }

        // timescale is decreased
        if (input.wasPressed(Keys.K) && timeScale > 1) {
            boolean increase = false;
            changeTimeScale(increase);
        }

        // collision occurs
        if (collision > 0) {
            boolean dead = bird.birdLoseLife();
            collided = true;
            if (dead) {
                gameOver = true;
            } else {
                currPipeIndex++;
            }
            collision = 0;
        }

        // if weapon and bird collide, and bird does not have a weapon, then the bird acquires the weapon

        if (weaponCollision > 0 && !bird.hasWeapon()) {
            weaponCollided = true;
            bird.acquireWeapon(weapons.get(0));
        }

        // if weapon is out of bounds , or if weapon has collided, then remove the weapon
        // the size != 0 is used to prevent a ArrayList index out of bounds

        if ((weapons.size() != 0 && weaponOutOfBound(weapons.get(FIRST_ELEM))) || weaponCollided) {
            weapons.remove(FIRST_ELEM);
            weaponCollided = false;
            weaponCollision = 0;
        }

        //out of bounds occurs
        if (birdOutOfBound()) {
            boolean dead = bird.birdLoseLife();
            if (dead) {
                gameOver = true;
            } else {
                bird.birdRespawn();
            }
        }

        //removes pipe if collision occurs
        if ((pipeSet.size() != 0 && pipeOutOfBound(pipeSet.get(FIRST_ELEM))) || collided) {
            pipeSet.remove(FIRST_ELEM);
            currPipeIndex--;
        }

        //shoot weapon
        if (input.wasPressed(Keys.S) && bird.hasWeapon()) {
            shot = true;
            shotWeapon = bird.shootWeapon();
        }

        // if weapon is shot and game is not over, then render the weapon until its range is reached
        if (shot && !gameOver) {
            stillShooting = shotWeapon.shoot(weapon_hit_target);
            if (!stillShooting) {
                shot = false;
                weapon_hit_target = false;
            }
        }

        //spawn new pipe when necessary
        if (pipeFrameCount == spawning_rate) {
            PipeSet pipes;
            if (level == 0) {
                pipes = new PipeSet();
            } else {
                pipes = randomPipe();

            }
            pipeSet.add(pipes);
            pipeFrameCount = 0;
            weaponFrameCount = 1;
        }

        // waits some frames after a pipe spawns, then adds the weapon in
        if (level == 1 && gameOn && weaponFrameCount > 0 && pipeSet.size() != 0) {
            if (weaponFrameCount >= spawning_rate / 2.0) {
                Weapons new_weapon = randomWeapon();
                PipeSet temp = pipeSet.get(pipeSet.size()-1);

                /*makes sure weapon does not overlap with a pipe when spawned*/
                if(detectCollision(new_weapon.getBox(), temp.getTopBox(), temp.getBottomBox()) == 0) {
                    weapons.add(new_weapon);
                }
                weaponFrameCount = 0;
            } else {
                weaponFrameCount++;
            }
        }

    }

    /**
     * This method checks if the centre of the bird has gone out of bounds( above the frame , or below the frame)
     *
     * @return True if the bird has gone out of bounds, False if it has not
     */
    public boolean birdOutOfBound() {
        return (bird.getY() > Window.getHeight()) || (bird.getY() < 0);
    }

    /**
     * checks if the right side of the chosen pipe has left the frame's left border.
     *
     * @param pipe the pipe in question
     * @return True if the pipe is out of bounds, and false if it is not
     */
    public boolean pipeOutOfBound(PipeSet pipe) {
        return (pipe.getX() <= -(pipe.getPipeWidth() / 2));
    }

    /**
     * checks if the right side of the chosen pipe has left the frame's left border.
     *
     * @param weapon the weapon in question
     * @return True if the weapon is out of bounds, and false if it is not
     */
    public boolean weaponOutOfBound(Weapons weapon) {
        return (weapon.getWeaponX() <= -(weapon.getWeaponWidth() / 2));
    }

    /**
     * This method changes the timescale of the game when the L key is pressed, for pipes and for weapons
     * if the frame Count for spawning the pipes
     * has already reached the "new frame rate" when the timescale is changed
     *
     * @param increase True if you want to increase the timeScale, and False if you want to decrease
     */
    public void changeTimeScale(boolean increase) {
        if (increase) {
            timeScale++;
        } else {
            timeScale--;
        }
        spawning_rate = Math.ceil(INITIAL_SPAWNING_RATE / Math.pow(TIME_SCALE_INCREMENT, timeScale - 1));

        /*if the frame Count for spawning the pipes
          has already reached the "new frame rate" when the
          timescale is changed, then in the current frame,
          a new pipe will spawn*/

        if (pipeFrameCount > spawning_rate) {
            pipeFrameCount = (int) (spawning_rate);
        }
    }


    /**
     * this method renders the Instruction Screen, using drawString
     *
     * @param input input from the keyboard, used to check if the space has been pressed
     * @param level either 1 or 0, with each rendering a different instruction screen
     */
    public void renderInstructionScreen(Input input, int level) {
        // paint the instruction on screen
        FONT.drawString(INSTRUCTION_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(INSTRUCTION_MSG) / 2.0)), (Window.getHeight() / 2.0 + (FONT_SIZE / 2.0)));
        if (input.wasPressed(Keys.SPACE)) {
            gameOn = true;
        }
        if (level == 1) {
            FONT.drawString(SHOOTING_MSG, (Window.getWidth() / 2.0 - (FONT.getWidth(INSTRUCTION_MSG) / 2.0)), (Window.getHeight() / 2.0 + (FONT_SIZE / 2.0)) + SHOOT_MSG_OFFSET);
        }
    }

    /**
     * This method creates one random pipe from the classes ENUM( SteelPipes, PlasticPipes)
     *
     * @return the random pipe object (ENUM( SteelPipes, PlasticPipes))
     */
    public PipeSet randomPipe() {
        int random = 0 + (int) (Math.random() * ((1 - 0) + 1));
        if (random == 0) {
            return new SteelPipes();
        } else {
            return new PlasticPipes();
        }
    }

    /**
     * This method creates one random weapon from the classes ENUM( Rock, Bomb)
     *
     * @return the random weapon object (ENUM( Rock, Bomb))
     */
    public Weapons randomWeapon() {
        int random = 0 + (int) (Math.random() * ((1 - 0) + 1));
        if (random == 0) {
            return new Rock();
        } else {
            return new Bomb();
        }
    }

    /**
     * this method is responsible for spawning all the pipes in the pipes ArrayList, Additionally:
     * - it checks if there is a collision between the pipes and the bird
     * - it checks if there is a collision between the pipes and a weapon
     * - if a pipe is a steelPipe, it renders the flames according to the right frame rates, and checks the
     * collision between a bird and the flames.
     *
     * @param birdBox the rectangle bounded by the bird image
     */
    public void pipeSpawning(Rectangle birdBox) {
        pipeFrameCount++;
        for (PipeSet pipe : pipeSet) {
            pipe.setTimeScale(timeScale);
            pipe.update();
            Rectangle topPipeBox = pipe.getTopBox();
            Rectangle bottomPipeBox = pipe.getBottomBox();
            collision = collision + detectCollision(birdBox, topPipeBox, bottomPipeBox);
            if (pipe instanceof SteelPipes) {
                boolean isRendered = ((SteelPipes) pipe).flameRender();
                if (isRendered) {
                    Rectangle topFlame = ((SteelPipes) pipe).getFlameTopBox();
                    Rectangle bottomFlame = ((SteelPipes) pipe).getFlameBottomBox();
                    collision = collision + flameCollision(birdBox, topFlame, bottomFlame);
                }
            }
            if (shot) {
                pipe_weapon_collision = pipe_weapon_collision + detectCollision(shotWeapon.getBox(), topPipeBox, bottomPipeBox);
                if (pipe_weapon_collision > 0) {
                    pipe_weapon_collision = 0;
                    weapon_hit_target = true;
                    if (pipe.isDestroyed(shotWeapon)) {
                        pipeSet.remove(pipe);
                        score++;
                    }
                }
            }
        }
    }

    /**
     * This method is responsible for spawning weapons, and checks the collision between a bird and a weapon
     *
     * @param birdBox the rectangle bounded by the bird image
     */
    public void weaponSpawning(Rectangle birdBox) {
        if (level == 1) {
            for (Weapons weapon : weapons) {
                weapon.setTimeScale(timeScale);
                weapon.update();
                Rectangle weaponBox = weapon.getBox();
                weaponCollision = weaponCollision + weaponCollision(birdBox, weaponBox);
            }
        }
    }




    /** this method is responsible for rendering the game over screen
     */
    public void renderGameOverScreen() {
        FONT.drawString(GAME_OVER_MSG, (Window.getWidth()/2.0-(FONT.getWidth(GAME_OVER_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }

    /** this method is responsible for rendering the game win screen
     *
     */
    public void renderWinScreen() {
        FONT.drawString(CONGRATS_MSG, (Window.getWidth()/2.0-(FONT.getWidth(CONGRATS_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
        String finalScoreMsg = FINAL_SCORE_MSG + score;
        FONT.drawString(finalScoreMsg, (Window.getWidth()/2.0-(FONT.getWidth(finalScoreMsg)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0))+SCORE_MSG_OFFSET);
    }


    /** this method is responsible for levelling up the game, from level 0 to level 1
     */
    public void levelUp(){
        level++;
        bird.levelUp();
        currentImage = BACKGROUND_IMAGE_LEVEL_1;
        currentWinScore = WIN_SCORE_LEVEL_1;
        pipeSet.clear();
        currPipeIndex = 0;
        pipeFrameCount = (int)spawning_rate;
    }

    /** renders the level up screen
     */
    public void renderLevelUpScreen(){
        FONT.drawString(LEVEL_UP_MSG, (Window.getWidth()/2.0-(FONT.getWidth(LEVEL_UP_MSG)/2.0)), (Window.getHeight()/2.0+(FONT_SIZE/2.0)));
    }

    /** this method detects a collision between a bird and the pipes, or a weapon and the pipes
     * @param bird_or_weapon_Box the rectangle bounded by a bird or a weapon
     * @param topPipeBox the rectangle bounded by the topPipe
     * @param bottomPipeBox the rectangle bounded by the bottomPipe
     * @return if a collision occurs, 1 is returned, else 0 is returned
     */
    public int detectCollision(Rectangle bird_or_weapon_Box, Rectangle topPipeBox, Rectangle bottomPipeBox) {
        // check for collision
        if(bird_or_weapon_Box.intersects(topPipeBox) ||
                bird_or_weapon_Box.intersects(bottomPipeBox)){
            return 1;
        }
        else{
            return 0;
        }
    }


    /** this method checks if a bird collides with a flame
     * @param birdBox the rectangle bounded by the bird
     * @param topFlame the rectangle bounded by the topPipe
     * @param bottomFlame the rectangle bounded by the bottomPipe
     * @return if a collision occurs, 1 is returned, else 0 is returned
     */
    public int flameCollision(Rectangle birdBox, Rectangle topFlame, Rectangle bottomFlame){
        if(birdBox.intersects(topFlame) ||
                birdBox.intersects(bottomFlame)){
            return 1;
        }
        else{
            return 0;
        }
    }


    /** this method checks if a bird collides with a weapon
     * @param birdBox the rectangle bounded by the bird
     * @param weapon the rectangle bounded by the weapon
     * @return if a collision occurs, 1 is returned, else 0 is returned
     */
    public int weaponCollision(Rectangle birdBox, Rectangle weapon){
        if(birdBox.intersects(weapon)){
            return 1;
        }
        else{
            return 0;
        }
    }

    /** this method updates the score of the bird whenever the centre of the bird passes the right side of the pipes
     * also checks if the player has won the game.
     */
    public void updateScore() {
        if (pipeSet.size() != currPipeIndex && bird.getX() > pipeSet.get(currPipeIndex).getTopBox().right()) {
            score += 1;
            currPipeIndex++;
        }
        String scoreMsg = SCORE_MSG + score;
        FONT.drawString(scoreMsg, 100, 100);

        // detect win
        if (score == currentWinScore) {
            win = true;
        }
    }
//end of solution project1
}
