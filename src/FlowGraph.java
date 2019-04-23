import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class FlowGraph implements MaxFlowGraph {

    private int[][] edges;
    private int vertices;
    private String[] vertexList;

    public FlowGraph(int vertices) {
        this.vertices = vertices;
        this.edges = new int[vertices][vertices];
        this.vertexList = new String[vertices];
    }

    /*
     * method used to generate label for vertices
     * */
    @Override
    public void generateNamesForVertices() {
        String alphabet = "abcdefghij";

        // source node
        vertexList[0] = "s";
        vertexList[vertices - 1] = "t";

        for (int r = 1; r < vertices - 1; r++) {
            String vertexName = Character.toString(alphabet.charAt(r - 1));
            insertVertex(r, vertexName);

        }

    }

    // insert vertex method
    public void insertVertex(int index, String vertexName) {
        vertexList[index] = vertexName;
    }


    // delete vertices method
    @Override
    public void deleteAllVertices() {

        // removing the vertex label
        for (int i = 0; i < vertices; i++) {
            vertexList[i] = "";
        }

        // removing the values from the 2d matrix
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                edges[i][j] = 0;
            }
        }


    }

    /*
     * method used to generate edges with capacity
     * */
    @Override
    public void genrateEdgesWithCapacity() {
        Random rand = new Random();

        for (int i = 0; i < vertices; i++) {

            if (i != vertices - 1) {
                for (int j = 0; j < vertices; j++) {
                    if (i != j) {
                        // System.out.println(i+" "+ j);
                        edges[i][j] = 0;
                    } else {
                        // System.out.println("Same"+i+" "+ j);
                        edges[i][j] = -1;
                    }
                }
            } else {
                // Sink have no edges outwards
                // Source have no edges inwards
                for (int j = 0; j < vertices; j++) {
                    edges[i][j] = -1;
                    edges[j][0] = -1;
                }
            }
        }

        for (int i = 0; i < vertices; i++) {

            int noEdge = rand.nextInt(vertices - 1) + 1;  // No. of Edges
            for (int j = 0; j < noEdge; j++) {
                int edgePos;    // holds the y position
                // do while will disable a node having and edge towards itself
                do {
                    edgePos = rand.nextInt(vertices);
                   // System.out.println("Hi");
                } while (edgePos == 0 || i == edgePos);

                // Avoids loop within two vertex
                if (edges[i][edgePos] != -1) {
                    edges[i][edgePos] = rand.nextInt(16) + 5;
                    edges[edgePos][i] = -1;
                }
            }
        }
    }

    /*
     * method used to print graph to console
     * */
    @Override
    public void printGraph() {
        // printing the labels row side
        for (int i = 0; i < vertices; i++) {
            System.out.print(" " + vertexList[i] + " ");
        }
        System.out.println();

        // printing the label column and the capacities relevant
        for (int i = 0; i < vertices; i++) {

            System.out.print(vertexList[i] + " ");

            for (int j = 0; j < vertices; j++) {
                System.out.print(" " + edges[i][j] + " ");
            }
            System.out.println();
        }

    }

    /*
    * method used to display ui relavant to the graph
    * */
    @Override
    public void showGraphUi() {
        Graph graph = new SingleGraph("Network Flow");

        // Adding Node to the gui
        for (int i = 0; i < vertices; i++) {
            Node n = graph.addNode(vertexList[i]);
            n.addAttribute("ui.label", vertexList[i]);
        }

        // Adding edges
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                if (i != j && edges[i][j] > 0) {
                    String edge = vertexList[i] + vertexList[j];
                    Edge e = graph.addEdge(edge, vertexList[i], vertexList[j], true);
                    e.addAttribute("ui.label", edges[i][j]);
                }
            }
        }
        graph.display();
    }


    // search vertex method
    public boolean bfSearch(int rG[][], int source, int sink, int[] path) {

        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        path[source] = -1;
        visited[source] = true;

        while (queue.size() != 0) {
            int startVerex = queue.poll();

            for (int i = 0; i < vertices; i++) {

                if (!visited[i] && rG[startVerex][i] > 0) {
                    visited[i] = true;
                    path[i] = startVerex;
                    queue.add(i);
                }
            }
        }


        return (visited[sink]);
    }

    /*
      Reference:
    *  Ford-Fulkerson Algorithm
    *  Edmonds-Karp Algorithm
    * */
    public int maxFlow() {
        int source = 0;
        int sink = vertices - 1;

        int[][] residual = new int[vertices][vertices];


        // Filling the capacity of the edges graph onto the residual graph
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                residual[i][j] = edges[i][j];
            }
        }

        int[] path = new int[vertices];

        int max_flow = 0;

        while (bfSearch(residual, source, sink, path)) {
            // identifying the paths
            //  System.out.println("Happenning");
            int min_Value = Integer.MAX_VALUE;
            int endVertex = vertices - 1;
            int startVertex = path[endVertex];

            while (path[endVertex] != -1) {
                // System.out.println("Ha ha");
                if (min_Value > residual[startVertex][endVertex]) {
                    min_Value = residual[startVertex][endVertex];
                }
                endVertex = startVertex;
                startVertex = path[endVertex];

            }

            // update the residual graph
            // reverse edges where flow has occured
            max_flow += min_Value;

            int endV = vertices - 1;
            int startV = path[endV];
            while (path[endV] != -1) {
                // System.out.println("He he");
                // reversing (updation of the residual matrix)
                residual[startV][endV] -= min_Value;
                residual[endV][startV] += min_Value;

                endV = startV;
                startV = path[endV];

            }

        }

        return max_flow;
    }
}


