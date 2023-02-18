/**
 * LineSegment is an immuable line with a head coordinate and a tail coordinate.
 * Both coordinates are 2 dimensional. Head and tail inclusivity are customizable.
*/
public class LineSegment {
    private final Double[] head;
    private final Double[] tail;
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
    public LineSegment(Double[] start, Double[] end) throws IllegalArgumentException{
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
    public LineSegment(Double[] start, Double[] end, boolean headInclusive,
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
        Double[] a = new Double[2];
        a[0] = -10.;
        a[1] = 0.;
        Double[] b = new Double[2];
        b[0] = 10.;
        b[1] = 0.;
        Double[] c = new Double[2];
        c[0] = -0.1;
        c[1] = -0.1;
        Double[] d = new Double[2];
        d[0] = 0.;
        d[1] = 2.;
        LineSegment x = new LineSegment(a, b, false, false);
        LineSegment y = new LineSegment(c, d,true,true);
        System.out.println(x.collision(y));
        //System.out.println(x.magnitude());
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
     * Returns the point of collision between this LineSegment and the input
     * LineSegment. If lines are paralell return null regardless of collision.
     * 
     * @param other other LineSegment to colide with
     * @requires no null inputs
     * @return array of length 2 with coordinates of collision.
    */
    public Double[] collisionAt(LineSegment other){
        return collisionAt(this, other);
    }
    /**
     * Returns the point of collision between this LineSegment and the input
     * LineSegment. If lines are paralell return null regardless of collision.
     * 
     * @param other other LineSegment to colide with
     * @requires no null inputs
     * @return array of length 2 with coordinates of collision.
    */
    public static Double[] collisionAt(LineSegment lineA, LineSegment lineB){
        Double A_slope = findSlope(lineA);
        Double B_slope = findSlope(lineB);
        if(A_slope == B_slope){
            return null;
        }
        Double A_constant = lineA.headY()-A_slope*lineA.headX();//lineA.y=slope*lineA.x+con
        Double B_constant = lineB.headY()-B_slope*lineB.headX();
        Double[] collision = new Double[2];
        collision[0] = (A_constant-B_constant)/(B_slope-A_slope);//A_slope*x+con_a = B_slope*x+con_B
        collision[1] = A_slope*collision[0]+A_constant;
        return collision;
    }
    /**
     * Checks if LineSegmentA collides with LineSegmentB.
     * No null inputs accepted.
     * 
     * @param LineSegmetA first LineSegmant to compare
     * @param LineSegmetB first LineSegmant to compare
     * @spec.requires other != null
     * @return true iff there is a collision head inclusive, tail exclusive
     * paralell line collisions do not count.
    */
    public static boolean collision(LineSegment lineA, LineSegment lineB){
        Double[] collision = collisionAt(lineA, lineB);
        if(collision == null){
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

        if(lineA.headX() < collision[0] && lineA.tailX() < collision[0]){
            return false;
        }
        if(lineA.headX() > collision[0] && lineA.tailX() > collision[0]){
            return false;
        }
        if(lineA.headY() < collision[1] && lineA.tailY() < collision[1]){
            return false;
        }
        if(lineA.headY() > collision[1] && lineA.tailY() > collision[1]){
            return false;
        }
        //Point colides with line A
        if(lineB.headX() < collision[0] && lineB.tailX() < collision[0]){
            return false;
        }
        if(lineB.headX() > collision[0] && lineB.tailX() > collision[0]){
            return false;
        }
        if(lineB.headY() < collision[1] && lineB.tailY() < collision[1]){
            return false;
        }
        if(lineB.headY() > collision[1] && lineB.tailY() > collision[1]){
            return false;
        }//Point collides with line B
        //Collision has occurred but is it head/tail inclusive?
        if(collision[0] == lineA.headX() && collision[1] == lineA.headY()){
            return lineA.headInclusive;
        }if(collision[0] == lineA.tailX() && collision[1] == lineA.tailY()){
            return lineA.tailInclusive;
        }if(collision[0] == lineB.headX() && collision[1] == lineB.headY()){
            return lineB.headInclusive;
        }if(collision[0] == lineB.tailX() && collision[1] == lineB.tailY()){
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
    /**
     * Returns magnitude of LineSegment
     * 
     * @return magnitude
    */
    public double magnitude(){
        double x = headX()-tailX();
        double y = headY()-tailY();
        double magnitude = Math.pow(x*x+y*y,.5);
        return magnitude;
    }
    /**
     * Standard clone method
     * 
     * @return clone
    */
    @Override
    public LineSegment clone(){
        Double[] pointA = new Double[2];
        pointA[0]=tailX();
        pointA[1]=tailY();
        Double[] pointB = new Double[2];
        pointB[0]=headX();
        pointB[1]=headY();
        LineSegment new_wall = new LineSegment(pointB, pointA);
        return new_wall;
    }
    public void draw(){
        System.out.println("Line drawn");
    }
}