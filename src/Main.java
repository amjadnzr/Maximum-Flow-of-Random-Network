
import java.util.Random;

public class Main {


    public static void main(String[] args) {
        Random random = new Random();  // used to randomize int values
        MaxFlowGraph flowGraph = new FlowGraph(random.nextInt(7) + 6); // max:12 min:6
        flowGraph.generateNamesForVertices();  // used to generate vertices count and label
        flowGraph.genrateEdgesWithCapacity();  // used to generate edges with capacity
        flowGraph.printGraph(); // printing graph in console
        flowGraph.showGraphUi(); // displaying ui related group

        int max_flow = flowGraph.maxFlow();  // finding the max flow
        System.out.println("Maximum Flow of the grapgh: " + max_flow);
        flowGraph.deleteAllVertices(); // delete all vertices


    }


}
