import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import javax.management.RuntimeErrorException;

/**
 * TwoWayGraph is an mutable list of nodes with 
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
        lines.add(new ArrayList<>());
        lines.add(new ArrayList<>());
        lines.get(0).add(.51);
        lines.get(0).add(1.);
        lines.get(1).add(.5);
        lines.get(1).add(0.01);
        ArrayList<Double> point1 = new ArrayList<Double>();
        point1.add(0.);
        point1.add(0.);
        ArrayList<Double> point2 = new ArrayList<Double>();
        point2.add(1.);
        point2.add(0.);
        ArrayList<Double> point3 = new ArrayList<Double>();
        point3.add(1.);
        point3.add(0.);
        ArrayList<Double> point4 = new ArrayList<Double>();
        point4.add(10.);
        point4.add(0.);
        ArrayList<Double> point5 = new ArrayList<Double>();
        point5.add(-10.);
        point5.add(0.5);
        ArrayList<Double> startPoint = new ArrayList<Double>();
        startPoint.add(-10.1);
        startPoint.add(0.5);
        ArrayList<Double> endPoint = new ArrayList<Double>();
        endPoint.add(1.1);
        endPoint.add(0.3);
        ArrayList<ArrayList<Double>> nodes = new ArrayList<>();
        nodes.add(point1);
        nodes.add(point2);
        nodes.add(point5);
        nodes.add(point4);

        sort(point3,nodes);
        TwoWayGraph a = new TwoWayGraph(lines, nodes);
        System.out.println(a.children(point1));
        System.out.println(a.PathFind(startPoint, endPoint));
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
     * Find all nodes that are reachable via point.
     * 
     * @param point 
     * @spec.requires point != null, point.size() == 2
     * @return List of nodes that are reachable via point
    */
    public ArrayList<ArrayList<Double>> validNodes(ArrayList<Double> point){
        ArrayList<ArrayList<Double>> goal = new ArrayList<ArrayList<Double>>();
        for(ArrayList<Double> coordinate: coordinates.keySet()){
            LineSegment line = new LineSegment(convertArrayListToDoubleArray(point), 
            convertArrayListToDoubleArray(coordinate));
            if(!obstacles.collision(line)){
                goal.add(coordinate);
            }
        }
        return goal;
    }
    /**
     * Find quickest path from 'start' to 'end'. Start and end must be in graph.
     * 
     * @param start node to start at
     * @param end node to end at
     * @throws IllegalArgumentException start or end are not in TwoWayGraph
     * @spec.requires start != null, end != null, start.size() == 2, end.size() == 2
     * @return quickest path from 'start' to 'end'. This is represented as an ArrayList of 2d coordinates.
     * return null if no path exists
    */
    public ArrayList<ArrayList<Double>> FindPath(ArrayList<Double> start, ArrayList<Double> end){
        if(!coordinates.containsKey(start) || !coordinates.containsKey(end)){
            throw new IllegalArgumentException("Nodes not present in graph!");
        }
        
        // Start at 'start'
        PriorityQueue<Path> active = new PriorityQueue<>();
        Set<ArrayList<Double>> finished = new HashSet<>();
        Map<ArrayList<Double>, Double> distance = new HashMap<>();
        Map<ArrayList<Double>, ArrayList<Double>> prev = new HashMap<>();
        active.add(new Path(start));
        distance.put(start,0.);
        prev.put(start,null);
        
        while(!active.isEmpty()){
            Path current = active.remove();
            ArrayList<Double> point = current.getHead();
            ArrayList<ArrayList<Double>> children = children(point);
            for(int i = 0; i != children.size(); i++){
                ArrayList<Double> child = children.get(i);
                Path newPath = current.addPoint(child);
                if(!distance.containsKey(child)){
                    distance.put(child,null);
                    prev.put(child, null);
                }
                if(distance.get(child) == null || distance.get(child) > newPath.length){
                    distance.put(child, newPath.length);
                    prev.put(child, point);
                }
                if(!finished.contains(child)){
                    active.add(current.addPoint(child));
                }
            }
            finished.add(point);
            if(finished.contains(end)){
                ArrayList<ArrayList<Double>> goal = new ArrayList<>();
                ArrayList<Double> place = end;
                while(place != null){
                    goal.add(place);
                    place = prev.get(place);
                }
                Collections.reverse(goal);
                return goal;
            }
        }
        return null;
    }
    /**
     * Find quickest path from 'start' to 'end'. 'start' and 'end' do not have to be in graph.
     * 
     * @param start node to start at
     * @param end node to end at
     * @spec.requires start != null, end != null, start.size() == 2, end.size() == 2
     * @return quickest path from 'start' to 'end'. This is represented as an ArrayList of 2d coordinates
    */
    public ArrayList<ArrayList<Double>> PathFind(ArrayList<Double> start, ArrayList<Double> end){
        if(start.equals(end)){
            return null;
        }
        Set<ArrayList<Double>> toRemove = new HashSet<>();
        if(!coordinates.containsKey(start)){
            addNode(start);
            toRemove.add(start);
        }
        if(!coordinates.containsKey(end)){
            addNode(end);
            toRemove.add(end);
        }
        ArrayList<ArrayList<Double>> goal = FindPath(start, end);
        if(toRemove.contains(start)){
            removeNode(start);
        }
        if(toRemove.contains(end)){
            removeNode(end);
        }
        return goal;
    }
    /**
     * Sort the given data according to their distance from 'point'.
     * 
     * @param point to find distance from
     * @param data points to be sorted
     * @spec.requires no null inputs. All points are of length 2
    */
    public static void sort(ArrayList<Double> point, ArrayList<ArrayList<Double>> data){
        // Create a custom comparator that compares the distance of two points from the given point
    Comparator<ArrayList<Double>> distanceComparator = new Comparator<ArrayList<Double>>() {
        @Override
        public int compare(ArrayList<Double> o1, ArrayList<Double> o2) {
            double distance1 = Math.sqrt(Math.pow(o1.get(0) - point.get(0), 2) + Math.pow(o1.get(1) - point.get(1), 2));
            double distance2 = Math.sqrt(Math.pow(o2.get(0) - point.get(0), 2) + Math.pow(o2.get(1) - point.get(1), 2));
            return Double.compare(distance1, distance2);
        }
    };
    // Sort the data ArrayList using the custom comparator
    Collections.sort(data, distanceComparator);
    }
    public void draw(){
        obstacles.draw();
    }
    /**
 * Calculate the Euclidean distance between two points.
 */
private double getDistance(ArrayList<Double> p1, ArrayList<Double> p2){
    double x1 = p1.get(0);
    double y1 = p1.get(1);
    double x2 = p2.get(0);
    double y2 = p2.get(1);
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
}
    /*
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
    /*
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

    /*
     * Converts an ArrayList of 2d corrdinates with arrays to an ArrayList of 2d coordinates with ArrayLists.
     * 
     * @param original List of coordinates to be implemented
     * @spec.requires no null inputs, the Double[] in original must have length 2
     * @return  The input coordinates converted into the following format.
     * An ArrayList of ArrayLists of length two containing Double.
    */
    private static ArrayList<ArrayList<Double>> arrayListToDoubleArray(ArrayList<Double[]> original){
    ArrayList<ArrayList<Double>> goal = new ArrayList<>();
    for (Double[] coord : original) {
        ArrayList<Double> newCoord = new ArrayList<>();
        newCoord.add(coord[0]);
        newCoord.add(coord[1]);
        goal.add(newCoord);
    }
    return goal;
    }
    /**
     * Remove node 'node' from TwoWayGraph if it is in the graph. Return true iff node was in graph.
     * 
     * @param node node to be removed
     * @spec.requires node is not null
     * @return true iff node was in graph
    */
    public boolean removeNode(ArrayList<Double> node) {
        // Remove node from graph
        Set<ArrayList<Double>> children = coordinates.remove(node);
        if (children == null) {
            return false;
        }
        // Remove node from children of other nodes
        for (ArrayList<Double> parent : coordinates.keySet()) {
            coordinates.get(parent).remove(node);
        }
        return true;
    }
    /**
     * Insert a 2d point named 'node' into the graph and return true iff it is not already present.
     * 
     * @param node node to insert
     * @spec.modifies this
     * @spec.requires node is not null. Does not form perfectly verticle line with other node
     * @throws IllegalArgumentException iff node.size() != 2
     * @return true iff 'node' is not in TwoWayGraph
     * 
     * Time complexity O(nm) where n is the number of edges and n is the number of nodes in the graph
    */
    public boolean addNode(ArrayList<Double> node){
        if(node.size() != 2){
            throw new IllegalArgumentException("node was not 2d!");
        }
        if(coordinates.containsKey(node)){
            return false;
        }
        Set<ArrayList<Double>> childPoints = new HashSet<>();
        coordinates.put(node, childPoints);
        // For each node
        for(ArrayList<Double> point: coordinates.keySet()){
            // If there is line of sight
            if(lineOfSight(point, node)){
                coordinates.get(point).add(node);
                coordinates.get(node).add(point);
            }
        }
        return true;
    }
    /**
     * Indicate if the two points have line of sight.
     * 
     * @param point1 the first point
     * @param point2 the second point
     * @param spec.requires no null inputs, no perfectly verticle lines
     * @throws IllegalArgumentException iff either point is not if size 2
     * @return true iff the two points have line of sight
     * 
     * Time complexity O(n) where n is the number of edges
    */
    public boolean lineOfSight(ArrayList<Double> point1, ArrayList<Double> point2){
        if(point1.size() != 2 || point2.size() != 2){
            throw new IllegalArgumentException("All coordinates must be 2d.");
        }
        LineSegment line = new LineSegment(convertArrayListToDoubleArray(point1),convertArrayListToDoubleArray(point2));
        return !obstacles.collision(line);
    }
    /**
     * Immutable
     * 
    */
    private class Path implements Comparable<Path>{
        ArrayList<ArrayList<Double>> path;
        Double length;
        Path(ArrayList<Double> point){
            path = new ArrayList<>();
            path.add(point);
            length = 0.;
        }
        private Path(Double len,ArrayList<ArrayList<Double>> path){
            this.path = path;
            length = len;
        }
        public Path addPoint(ArrayList<Double> point){
            LineSegment line = new LineSegment(convertArrayListToDoubleArray(path.get(path.size()-1)), convertArrayListToDoubleArray(point));
            ArrayList<ArrayList<Double>> newPath = new ArrayList(path);
            newPath.add(point);
            LineSegment size = new LineSegment(convertArrayListToDoubleArray(getHead()), convertArrayListToDoubleArray(point));
            return new Path(length+size.magnitude(),newPath);
        }
        public int compareTo(Path other){
            if(length > other.length){
                return 1;
            }if(length < other.length){
                return -1;
            }
            return 0;
        }
        public ArrayList<Double> getHead(){
            return path.get(path.size()-1);
        }
        @Override
        public int hashCode(){
            return path.hashCode();
        }
        @Override
        public boolean equals(Object ob){
            if(!(ob instanceof Path)){
                return false;
            }
            Path other = (Path) ob;
            return path.equals(other.path);
        }
    }
}