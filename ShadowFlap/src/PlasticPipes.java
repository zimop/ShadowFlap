/**
 * this class creates PlasticPipe Objects which is a subclass of the Pipeset class
 */
public class PlasticPipes extends PipeSet{
    private final int PIPE_CENTRE_MAX = 500;
    private final int PIPE_CENTRE_MIN = 100;
    /**
     * Constructor method for a PlasticPipe object
     */
    public PlasticPipes(){
        super();
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
        return (weapon instanceof Bomb || weapon instanceof Rock);
    }
}
