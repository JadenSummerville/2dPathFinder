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
        Agent n = new Agent(1, 1, 100, 0, 100, 0, .5, 1);
        Double[] A = new Double[2];
        A[0]=0.;
        A[1]=1.;
        Double[] B = new Double[2];
        B[0]=1.;
        B[1]=0.;
        LineSegment b = new LineSegment(A, B);
        n.aimAt(0, 0);
        n.bounce(b);
        n.move();
        System.out.println(n.position_x+" "+n.position_y);
        n.move();
        System.out.println(n.position_x+" "+n.position_y);
        /*
        Agent a = new Agent(50,50,100,0,100,0,.5,2);
        for(int i = 0; i < 100; i++){
            a.aimAt(0,3);
            a.move();
            System.out.println("X:"+a.x()+" Y:"+a.y());
        }
        */
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
    public LineSegment projectedMovement(){
        Double[] A = new Double[2];
        A[0]=position_x;
        A[1]=position_y;
        Double[] B = new Double[2];
        B[0]=position_x + frictionMove(velocity_x);
        B[1]=position_y + frictionMove(velocity_y);
        LineSegment goal = new LineSegment(A, B);
        return goal;
    }
    public void halt(){
        velocity_x = 0;
        velocity_y = 0;
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
        if(hypotenus == 0){
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
    /**
     * Bounce off input line.
     * 
     * @param input line to bounce with
     * @spec.modifies this
     * @spec.requires input != null
    */
    public void bounce(LineSegment input)throws IllegalArgumentException, ExecutionException{
        checkRep();
        double magnitude = Math.pow(velocity_x*velocity_x+velocity_y*velocity_y, 0.5);
        double angle1;
        double angle2;
        angle1 = slopeToAngle(input.findSlope());
        if(velocity_x == 0){
            angle2 = Math.PI/2;
        }else{
            angle2 = slopeToAngle(velocity_y/velocity_x);
            if(velocity_x<0){
                angle2 += Math.PI;
            }
        }
        double angle_new = -angle2+2*angle1;
        velocity_x=magnitude*Math.cos(angle_new);
        velocity_y=magnitude*Math.sin(angle_new);
        //System.out.println(angle2+" "+angle1+" "+angle_new);
        checkRep();
    }
    private static double[] polarToCart(double angle, double magnitude){
        double[] goal = new double[2];
        goal[0] = magnitude*Math.cos(angle);
        goal[1] = magnitude*Math.sin(angle);
        return goal;
    }
    private static double slopeToAngle(double slope){
        return Math.atan(slope);
    }
}