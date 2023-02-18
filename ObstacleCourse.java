import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * An ObstacleCourse is a mutable collection of LineSegments that act as walls.
*/
public class ObstacleCourse{
    private final Set<LineSegment> walls;
    /**
     * Constructs a new linSegment
    */
    ObstacleCourse(){
        walls = new HashSet<LineSegment>();
    }
    /**
     * Constructs a new linSegment
     * 
     * @param coordinates Coordinate of LineSegments. Coordinates come in pairs for each LineSegment.
     * @requires no null data
     * @throws IllegalArgumentException odd number of coordinates input
    */
    ObstacleCourse(ArrayList<Double[]> coordintes){
        if(coordintes.size()%2 != 0){
            throw new IllegalArgumentException("Odd number of coordinates input");
        }
        walls = new HashSet<LineSegment>();
        for(int i = 0; i != coordintes.size(); i+=2){
            Double[] tail = coordintes.get(i);
            Double[] head = coordintes.get(i+1);
            LineSegment line = new LineSegment(head, tail);
            walls.add(line);
        }
    }
    public static void main(String[] args) {
        ObstacleCourse ob = new ObstacleCourse();
        Double[] A = new Double[2];
        A[0]=0.;
        A[1]=0.;
        Double[] B = new Double[2];
        B[0]=1.;
        B[1]=0.;
        LineSegment a = new LineSegment(A, B);
        A[0]=1.;
        A[1]=.5;

        B[0]=0.;
        B[1]=-1.;
        LineSegment b = new LineSegment(A, B);
        ob.addWall(a);
        ob.addWall(b);
        System.out.println(ob.getLineSegmentOfCollision(b).findSlope()+" "+ob.collisionAt(b)[1]);
    }
    /**
     * Adds a new LineSegment to the obstacle course
     * 
     * @param wall wall to be added
     * @spec.modifies this
     * @spec.requires wall != null
    */
    public void addWall(LineSegment wall){
        LineSegment new_wall = wall.clone();
        walls.add(new_wall);
    }
    /**
     * Checks to see if the input LineSegment collides with any inner LineSegments
     * 
     * @param input line segment to check
     * @spec.requires input != null
     * @return true iff input colides with atleast one wall in the obstacle course
    */
    public boolean collision(LineSegment input){
        if(getCollision(input) == null){
            return false;
        }
        return true;
    }
    /**
     * Returns the coordinate of collision between the input tail and the closest
    */
    public Double[] collisionAt(LineSegment input){
        LineSegment collision = getLineSegmentOfCollision(input);
        if(collision == null){
            return null;
        }
        return collision.collisionAt(input);
    }
    /**
     * Returns a lineSegment
    */
    private LineSegment getCollision(LineSegment input){
        for(LineSegment line: walls){
            if(line.collision(input)){
                return line;
            }
        }
        return null;
    }
    /**
     * Returns that closest coliding LineSegment. If non exisits return null
     * 
     * @param input line segment to colide with
     * @spec.requires no null inputs
     * @return colliding LineSegment unless non exists then null
    */
    public LineSegment getLineSegmentOfCollision(LineSegment input){
        double min_range = -1;
        LineSegment collision = null;
        for(LineSegment line: walls){
            if(line.collision(input)){
                Double[] c1 = line.collisionAt(input);
                Double[] c2 = new Double[2];
                c2[0] = input.tailX();
                c2[1] = input.tailY();
                LineSegment colide = new LineSegment(c1, c2);
                double magnitude = colide.magnitude();
                if(magnitude<min_range || min_range == -1){
                    min_range = magnitude;
                    collision = line;
                }
            }
        }
        if(collision == null){
            return null;
        }
        return collision;
    }
    public void draw(){
        for(LineSegment line: walls){
            line.draw();
        }
    }
}