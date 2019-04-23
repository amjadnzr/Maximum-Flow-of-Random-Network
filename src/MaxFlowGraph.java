public interface MaxFlowGraph {

    void genrateEdgesWithCapacity(); // used to generate vertices count and label

    void generateNamesForVertices(); // used to generate edges with capacity

    void printGraph(); // printing graph in console

    void showGraphUi(); // displaying ui related group

    int maxFlow();   // calculate max flow

    void deleteAllVertices();  // delete all vertices
}
