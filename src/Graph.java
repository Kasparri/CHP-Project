import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Graph {


    public static final int NAN_EDGE = -1;

    private int N; // Number of vertices
    private int M; // Number of edges

    private List<Integer> nodes;
    private List<Edge> edges;

    private int[][] adjacencyMatrix;

    public Graph(List<Integer> nodes, List<Edge> edges){
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.N = 0;
        this.M = 0;
    }

    public int getNode(int index) {
        return getNodes().get(index);
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    public int getN() {
        return N;
    }

    public void addNode(int node) {
        nodes.add(node);
        N++;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
        M++;
    }

    public Edge getEdge(int index) {
        return this.edges.get(index);
    }

    public List<Edge> getEdges() {
        return this.edges;
    }


    public int[][] fillAdjacencyMatrix(){
        adjacencyMatrix = new int[N][N];

        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                adjacencyMatrix[i][j] = NAN_EDGE;
            }
        }
        for (Edge e : edges){
            adjacencyMatrix[e.getSrc()][e.getDest()] = e.getWeight();
            adjacencyMatrix[e.getDest()][e.getSrc()] = e.getWeight();
        }

        return adjacencyMatrix;
    }

    public List<Integer> findSuccessors(int node){
        List<Integer> successors = new ArrayList<>();
        for (int i = 0; i < N; i++){
            int weight = adjacencyMatrix[node][i];
            if (weight != NAN_EDGE){
                successors.add(i);
            }
        }
        return successors;
    }


    public Edge findEdge(int parent, int successor) {
        for (Edge edge : edges) {
            if ( (edge.getSrc() == parent && edge.getDest() == successor)
                    || (edge.getSrc() == successor && edge.getDest() == parent) ){
                return edge;
            }
        }
        return null;
    }



    public Graph findInitialSpanningTree() {

        // DFS Version

        Graph initialTree = new Graph();

        //List<Node> refs = new ArrayList<>();

        Stack<Integer> open_set = new Stack<>();

        List<Integer> closed_set = new ArrayList<>();

        // Start state
        int start = nodes.get(0);
        open_set.add(start);

        initialTree.addNode(start);

        int parent;

        fillAdjacencyMatrix();

        while (!open_set.isEmpty()) {
            parent = open_set.pop();

            if (closed_set.size() == N - 1) {
                return initialTree;
            }

            for (int successor : findSuccessors(parent) ) {

                if (closed_set.contains(successor)) {
                    continue;
                }

                if (!open_set.contains(successor)) {
                    initialTree.addNode(successor);
                    initialTree.addEdge(findEdge(parent, successor));

                    open_set.add(successor);
                }
            }
            closed_set.add(parent);
        }
        return null;
    }


    public int getGraphWeight(){
        int sum = 0;
        for (Edge edge : this.edges){
            sum += edge.getWeight();
        }
        return sum;
    }




    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Graph graph = (Graph) o;

        if (N != graph.N) return false;
        if (M != graph.M) return false;
        if (nodes != null ? !nodes.equals(graph.nodes) : graph.nodes != null) return false;
        return edges != null ? edges.equals(graph.edges) : graph.edges == null;
    }

    @Override
    public int hashCode() {
        int result = N;
        result = 31 * result + M;
        result = 31 * result + (nodes != null ? nodes.hashCode() : 0);
        result = 31 * result + (edges != null ? edges.hashCode() : 0);
        return result;
    }

    public void removeEdge(Edge edge) {
        this.edges.remove(edge);
        M--;
    }

    public void setEdge(int k, Edge gEdge) {
        this.edges.set(k,gEdge);
    }

    public Graph makeCopy(){
        return new Graph(new ArrayList<>(this.nodes), new ArrayList<>(this.edges));
    }


    public boolean checkIfSpanningTree(int N){

        return this.nodes.size() == N && this.edges.size() == N-1 ;

    }

    public int getMirrorWeight(List<Edge> edges) {

        int total_weight = 0;
        for (Edge edge : this.edges){
            for (int i = 0; i < edges.size(); i++){
                if (edge == edges.get(i)){
                    total_weight += edges.get(edges.size()-1-i).getWeight();
                }
            }
        }

        return total_weight;
    }
}
