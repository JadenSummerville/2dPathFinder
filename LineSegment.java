/**
 * LineSegment is an immuable line with a head coordinate and a tail coordinate.
 * Both coordinates are 2 dimensional. Head and tail inclusivity are customizable.
*/
public class LineSegment {
    private final double[] head;
    private final double[] tail;
    private final boolean headInclusive;
    private final boolean tailInclusive;
    /**
     * Constructs a line segmant starting at 'start' and ending at 'end'.
     * Both points must be 2 dimensional. Start inclusive and end exclusive.
     * 
     * @param start head point
     * @param end tail point
     * @throws IllegalArgumentException non-2 dimensional point inputs
     * @spec.requires no NaN or null values or coordinates. No perfectly verticle LineSegments.
    */
    public LineSegment(double[] start, double[] end) throws IllegalArgumentException{
        if(start.length != 2 || end.length != 2){
            throw new IllegalArgumentException("Coordinates must be 2 dimensional!");
        }
        head = start.clone();
        tail = end.clone();
        headInclusive = true;
        tailInclusive = false;
    }
    /**
     * Constructs a line segmant starting at 'start' and ending at 'end'.
     * Both points must be 2 dimensional
     * 
     * @param start head point
     * @param end tail point
     * @throws IllegalArgumentException non-2 dimensional point input
     * @spec.requires no NaN or null values or coordinates. No perfectly verticle LineSegments.
    */
    public LineSegment(double[] start, double[] end, boolean headInclusive,
     boolean tailInclusive) throws IllegalArgumentException{
        if(start.length != 2 || end.length != 2){
            throw new IllegalArgumentException("Coordinates must be 2 dimensional!");
        }
        head = start.clone();
        tail = end.clone();
        this.headInclusive = headInclusive;
        this.tailInclusive = tailInclusive;
    }
    public static void main(String[] args) throws IllegalArgumentException {
        double[] a = new double[2];
        a[0] = 1;
        a[1] = 0;
        double[] b = new double[2];
        b[0] = -1;
        b[1] = 0;
        double[] c = new double[2];
        c[0] = 0;
        c[1] = 1;
        double[] d = new double[2];
        d[0] = 0;
        d[1] = 2;
        LineSegment x = new LineSegment(a, b, false, false);
        LineSegment y = new LineSegment(c, d, false, false);
        System.out.println(x.collision(y));
    }
    /**
     * Checks if this LineSegment collides with an input LineSegment.
     * No null input accepted
     * 
     * @param other other LineSegmant to compare to
     * @spec.requires other != null
     * @return true iff there is a collision head inclusive, tail exclusive
    */
    public boolean collision(LineSegment other){
        return collision(this,other);
    }
    /**
     * Checks if LineSegmentA collides with LineSegmentB.
     * No null inputs accepted
     * 
     * @param LineSegmetA first LineSegmant to compare
     * @param LineSegmetB first LineSegmant to compare
     * @spec.requires other != null
     * @return true iff there is a collision head inclusive, tail exclusive
     * paralell line collisions do not count.
    */
    public static boolean collision(LineSegment lineA, LineSegment lineB){
        double A_slope = findSlope(lineA);
        double B_slope = findSlope(lineB);
        if(A_slope == B_slope){
            return false;
        }
        //Check for tail collision
        if(lineA.tailX() == lineB.tailX() && lineA.tailY() == lineB.tailY()){
            return lineA.tailInclusive && lineB.tailInclusive;
        }if(lineA.headX() == lineB.headX() && lineA.headY() == lineB.headY()){
            return lineA.headInclusive && lineB.headInclusive;
        }if(lineA.tailX() == lineB.headX() && lineA.tailY() == lineB.headY()){
            return lineA.tailInclusive && lineB.headInclusive;
        }if(lineA.headX() == lineB.tailX() && lineA.headY() == lineB.tailY()){
            return lineA.headInclusive && lineB.tailInclusive;
        }
        double A_constant = lineA.headY()-A_slope*lineA.headX();//lineA.y=slope*lineA.x+con
        double B_constant = lineB.headY()-B_slope*lineB.headX();
        double x_of_collision = (A_constant-B_constant)/(A_slope-B_slope);//(A_slope-B_slope)*x = -con_B+con_a
        double y_of_collision = A_slope*x_of_collision+A_constant;
        if(lineA.headX() < x_of_collision && lineA.tailX() < x_of_collision){
            return false;
        }
        if(lineA.headX() > x_of_collision && lineA.tailX() > x_of_collision){
            return false;
        }
        if(lineA.headY() < y_of_collision && lineA.tailY() < y_of_collision){
            return false;
        }
        if(lineA.headY() > y_of_collision && lineA.tailY() > y_of_collision){
            return false;
        }
        //Collision has occurred but is it head/tail inclusive?
        if(x_of_collision == lineA.headX() && y_of_collision == lineA.headY()){
            return lineA.headInclusive;
        }if(x_of_collision == lineA.tailX() && y_of_collision == lineA.tailY()){
            return lineA.tailInclusive;
        }if(x_of_collision == lineB.headX() && y_of_collision == lineB.headY()){
            return lineB.headInclusive;
        }if(x_of_collision == lineB.tailX() && y_of_collision == lineB.tailY()){
            return lineB.tailInclusive;
        }
        return true;
    }
    /**
     * Finds the slop of this LineSegment
     * 
     * @return slope of this LineSegment
    */
    public double findSlope(){
        return findSlope(this);
    }
    /**
     * Finds the slop of input LineSegment
     * 
     * @param line LineSegment who's slope we will find
     * @spec.requires line != null
     * @return slope of input LineSegment
    */
    public static double findSlope(LineSegment line){
        double x_slope = line.headX()-line.tailX();
        double y_slope = line.headY()-line.tailY();
        return y_slope/x_slope;
    }
    /**
     * Returns x coordinate of head
     * 
     * @return x coordinate of head
    */
    public double headX(){
        return head[0];
    }
    /**
     * Returns y coordinate of head
     * 
     * @return y coordinate of head
    */
    public double headY(){
        return head[1];
    }
    /**
     * Returns x coordinate of tail
     * 
     * @return x coordinate of tail
    */
    public double tailX(){
        return tail[0];
    }
    /**
     * Returns y coordinate of tail
     * 
     * @return y coordinate of tail
    */
    public double tailY(){
        return tail[1];
    }
}