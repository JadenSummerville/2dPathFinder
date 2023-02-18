import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.RuntimeErrorException;
/**
 * TwoWayGraph is an immutable list of nodes
*/
public class TwoWayGraph {
    private final Map<ArrayList<Double>,Set<ArrayList<Double>>> coordinates;
    private final ObstacleCourse obstacles;
/**
 * Contruct new TwoWayGraph
 * 
 * @param lines the coordinates of the ObstecleCourse. Coordinates come in pairs for each LineSegment.
 * @param nodes the coordinates of the nodes.
 * @throws IllegalArgumentExeption iff lines.size()%2 == 1 || nodes.size() < 2
 * @spec.requires no null values and no perfectly verticle lines and Coordinates have length 2
*/
    public TwoWayGraph(ArrayList<ArrayList<Double>> lines, ArrayList<ArrayList<Double>> nodes){
        if(nodes.size() < 2){
            throw new IllegalArgumentException("Must have two or more nodes in ArrayList");
        }
        coordinates = new HashMap<>();
        obstacles = new ObstacleCourse(doubleArrayToArrayList(lines));
        // Add every node to the graph
        for(int i = 0; i != nodes.size(); i++){
            Set<ArrayList<Double>> set = new HashSet<>();
            coordinates.put(nodes.get(i), set);
        }
        //System.out.println(coordinates.get());
        // Add edges
        Set<ArrayList<Double>> elements = coordinates.keySet();
        for(ArrayList<Double> node1: elements){
            for(ArrayList<Double> node2: elements){
                if(!node1.equals(node2)){
                    if(!obstacles.collision(new LineSegment(convertArrayListToDoubleArray(node1), convertArrayListToDoubleArray(node2)))){
                        coordinates.get(node1).add(node2);
                    }
                }
            }
        }
    }
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> lines = new ArrayList<>();
        ArrayList<Double> point1 = new ArrayList<Double>();
        point1.add(0.);
        point1.add(0.);
        ArrayList<Double> point2 = new ArrayList<Double>();
        point2.add(1.);
        point2.add(0.);
        ArrayList<Double> point3 = new ArrayList<Double>();
        point3.add(1.);
        point3.add(0.);
        ArrayList<ArrayList<Double>> nodes = new ArrayList<>();
        nodes.add(point1);
        nodes.add(point2);
        TwoWayGraph a = new TwoWayGraph(lines, nodes);
        System.out.println(a.children(point3));
    }    
    /**
     * List child nodes of 'node'
     * 
     * @param node parent node
     * @spec.requires no null inputs
     * @return children of 'node'
    */
    public ArrayList<ArrayList<Double>> children(ArrayList<Double> node){
        ArrayList<ArrayList<Double>> goal = new ArrayList<>();
        Set<ArrayList<Double>> children = coordinates.get(node);
        for(ArrayList<Double> child: children){
            goal.add(child);
        }
        return goal;
    }
    /**
     * Take an ArrayList of ArrayLists of Doubles and convert them to an ArrayList of Double[] of length 2.
    * Specific for 2d coordinates.
    *
    * @param original the ArrayList of ArrayLists of Doubles to convert
    * @spec.requires original is not null and each coordinate has length 2
    * @return an ArrayList of Double[] of length 2 containing the coordinates
    */
    private static ArrayList<Double[]> doubleArrayToArrayList(ArrayList<ArrayList<Double>> original) {
        ArrayList<Double[]> goal = new ArrayList<>();
        for (int i = 0; i < original.size(); i++) {
            ArrayList<Double> coordinate = original.get(i);
            Double[] newCoordinate = convertArrayListToDoubleArray(coordinate);
            goal.add(newCoordinate);
        }
        return goal;
    }
    /**
     * Take an ArrayList of 2 Doubles and return a Double[].
     * Specific for 2d coordinates.
    *
    * @param original the original ArrayList to convert
    * @spec.requires original is not null and has length 2
    * @return a Double[] of length 2 containing the coordinates
    */
    private static Double[] convertArrayListToDoubleArray(ArrayList<Double> original) {
        Double[] coordinates = new Double[2];
        coordinates[0] = original.get(0);
        coordinates[1] = original.get(1);
        return coordinates;
}

    /**
     * Converts an ArrayList of 2d corrdinates with arrays to an ArrayList of 2d coordinates with ArrayLists.
     * 
     * @param original List of coordinates to be implemented
     * @spec.requires no null inputs, the Double[] in original must have length 2
     * @return  The input coordinates converted into the following format.
     * An ArrayList of ArrayLists of length two containing Double.
    */
    private static ArrayList<ArrayList<Double>> arrayListToDoubleArray(ArrayList<Double[]> original){
    ArrayList<ArrayList<Double>> result = new ArrayList<>();
    for (Double[] coord : original) {
        ArrayList<Double> newCoord = new ArrayList<>();
        newCoord.add(coord[0]);
        newCoord.add(coord[1]);
        result.add(newCoord);
    }
    return result;
    }
}