import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 24/10/2017.
 */
public class Graph {

    List<Node> nodes;
    List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public Graph(List<Node> nodes, List<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public boolean isMirrorable(List<Edge> originalEdges, int B) {

        List<Edge> mirrorEdges = new ArrayList<>();

        // Finding mirror edges
        for (int i = 0; i< this.edges.size(); i++){
            mirrorEdges.add(originalEdges.get(getMirrorIndex(originalEdges.size(),i)));
        }

        int sum = 0;
        for (Edge e : mirrorEdges) {
            sum += e.getWeight();
        }

        return sum <= B;
    }

    private int getMirrorIndex(int n, int i) {
        return n-1-i;
    }


    public void createAdjacencyMatrix() {

        int[][] adjacencyMatrix = new int[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++){
            List<Integer> neighbours = nodes.get(i).getNeighboursByNumber();
            for (int j = 0; j < nodes.size(); j++){
                if (neighbours.contains(j)) {
                    adjacencyMatrix[i][j] = 1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }

    }




}
