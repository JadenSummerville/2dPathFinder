import java.util.concurrent.ExecutionException;
/**
 * An Agent is mutable and has a location in a max and min range. The location must fall in the range.
 * Agent has a velocity, level of friction, and max speed. Note that higher speed
 * multipliers caused by low friction are taken into account.
*/
public class Agent {
    private double position_x;
    private double position_y;

    private double velocity_x;
    private double velocity_y;

    private final double max_x;
    private final double min_x;
    private final double max_y;
    private final double min_y;

    private final double friction;
    private final double speed;
    /**
     * Set agent position, speed, and friction
     * 
     * @param position_x x coordinate of agent
     * @param position_y y coordinate of agent
     * @param max_x maximum x value
     * @param min_x minimum x value
     * @param max_y maximum y value
     * @param min_y minimum y value
     * @param friction friction of agent
     * @param speed max speed of agent
     * @spec.requires no NaN inputs
     * @throws IllegalArgumentException position is not within specified range or 1<=friction<0
    */
    public Agent(double position_x, double position_y, double max_x, double min_x, double max_y, double min_y
    , double friction, double speed)throws ExecutionException, IllegalArgumentException{
        if(position_x > max_x || position_x < min_x || position_y > max_y || position_y < min_y){
            throw new IllegalArgumentException("Out of bounds error!", null);
        }
        if(friction < 0 || friction >= 1){
            throw new IllegalArgumentException("Error, 0 < Friction <= 1 not met.", null);
        }
        this.position_x = position_x;
        this.position_y = position_y;
        this.max_x = max_x;
        this.min_x = min_x;
        this.max_y = max_y;
        this.min_y = min_y;
        this.velocity_x = 0;
        this.velocity_y = 0;
        this.friction = friction;
        this.speed = speed;
        checkRep();
    }
    private void checkRep() throws ExecutionException{
        if(true){//turn on when debugging and turn off otherwise
            if(position_x == Double.NaN || position_y == Double.NaN ||
            max_x == Double.NaN || min_x == Double.NaN || max_y == Double.NaN
            || min_y == Double.NaN || velocity_x == Double.NaN ||
            velocity_x == Double.NaN || velocity_y == Double.NaN ||
            friction == Double.NaN || speed == Double.NaN){
                throw new ExecutionException("NaN parameter!", null);
            }
            if(position_x > max_x || position_x < min_x || position_y > max_y || position_y < min_y){
                throw new ExecutionException("Out of bounds error!", null);
            }
        }
    }
    public static void main(String[] args)throws  ExecutionException, IllegalArgumentException{
        Agent a = new Agent(50,50,100,0,100,0,.5,2);
        for(int i = 0; i < 100; i++){
            a.aimAt(0,3);
            a.move();
            System.out.println("X:"+a.x()+" Y:"+a.y());
        }
        
    }
    /**
     * Increment position by velocity and
     * 
     * @spec.modifies this
    */
    public void move()throws ExecutionException{
        checkRep();
        position_x += frictionMove(velocity_x);
        position_y += frictionMove(velocity_y);
        correct();
        velocity_x = velocity_x*friction;
        velocity_y = velocity_y*friction;
        checkRep();
    }
    /**
     * Normalizes speed according to friction and requested speed.
     * 
     * @param velocity magnitude of value to correct
     * @return Normalized speed value
    */
    private double frictionMove(double velocity){
        return velocity*(1-friction)*speed;
    }
    /**
     * Helper method set location to inside allowed space.
    */
    private void correct()throws ExecutionException{
        if(position_x>max_x){
            position_x = max_x;
            velocity_x *= -1;
        }else if(position_x<min_x){
            position_x = min_x;
            velocity_x *= -1;
        }
        if(position_y>max_y){
            position_y = max_y;
            velocity_y *= -1;
        }else if(position_y<min_y){
            position_y = min_y;
            velocity_y *= -1;
        }
        checkRep();
    }
    /**
     * Aims at input coordinate. Ignors magnitude of inputs.
     * 
     * @param x target x coordinate
     * @param y target y coordinate
     * @throws IllegalArgumentException NaN input
    */
    public void aimAt(double x, double y)throws ExecutionException, IllegalArgumentException{
        checkRep();
        if(x == Double.NaN || y == Double.NaN){
            throw new IllegalArgumentException("Aiming at NaN coordinates!");
        }
        //Find required displacement
        double x_V = x-position_x;
        double y_V = y-position_y;

        double hypotenus = Math.pow(x_V*x_V+y_V*y_V,.5);
        //If we are close enough or there then stop
        if(hypotenus < 5){
            return;
        }
        // Increment and normalize velocities
        velocity_x += x_V/hypotenus;
        velocity_y += y_V/hypotenus;
        checkRep();
    }
    public double x(){
        return position_x;
    }
    public double y(){
        return position_y;
    }
}