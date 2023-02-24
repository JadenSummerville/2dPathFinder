import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SuperAgent{
    Agent agent;
    TwoWayGraph graph;
    ArrayList<ArrayList<Double>> path;
    SuperAgent() throws IllegalArgumentException, ExecutionException{
        agent = new Agent(50, 50, 100, 0, 100, 0, .5, 1);
        path = null;

        ArrayList<ArrayList<Double>> lines = new ArrayList<>();
        lines.add(new ArrayList<>());
        lines.add(new ArrayList<>());
        lines.get(0).add(9.);
        lines.get(0).add(100.);
        lines.get(1).add(9.5);
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

        graph = new TwoWayGraph(lines, nodes);
    }
    public static void main(String[] args) throws IllegalArgumentException, ExecutionException {
        SuperAgent a = new SuperAgent();
        ArrayList<Double> target = new ArrayList<>();
        target.add(2.);
        target.add(6.);
        a.goTo(target);
        for(int i = 0; i != 100; i++){
            a.move();
            System.out.println(a.at()[0]+","+a.at()[1]);
        }
    }
    public void goTo(ArrayList<Double> target){
        ArrayList<Double> start = new ArrayList<>();
        start.add(agent.x());
        start.add(agent.y());
        path = graph.PathFind(start, target);
    }
    public void draw(){
        graph.draw();
    }
    public void move() throws ExecutionException{
        if(path == null){
            agent.move();
            return;
        }
        Double[] point = TwoWayGraph.convertArrayListToDoubleArray(path.get(0));
        agent.aimAt(point[0], point[1]);
        agent.move();
        Double[] point2 = at();
        LineSegment distance = new LineSegment(point, point2);
        if(distance.magnitude() < 5){
            path.remove(0);
        }
        if(path.size() == 0){
            path = null;
        }
    }
    public Double[] at(){
        Double[] location = new Double[2];
        location[0] = agent.x();
        location[1] = agent.y();
        return location;
    }
}