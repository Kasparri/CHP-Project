import java.util.*;

/**
 * Created by Kasper on 24/10/2017.
 */
public class Graph {

    private List<Node> nodes;
    private List<Edge> edges;

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

    public void removeNode(Node node){
        this.nodes.remove(node);
    }

    public void removeNode(int i){
        this.nodes.remove(i);
    }

    public void removeEdge(Edge edge){
        this.edges.remove(edge);
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

    public Set<Edge> pruneSoloNodes() {
        Set<Edge> edges = new HashSet<>();

        while (this.hasSingleNeighbourNodes()){

            for (Node n : this.nodes){
                if (n.getEdges().size() == 1) {
                    Edge singleEdge = n.getEdges().get(0);
                    edges.add(singleEdge);

                    Node neighbour = n.getNeighbour(singleEdge);
                    neighbour.getEdges().remove(singleEdge);


                    this.edges.remove(singleEdge);
                    this.nodes.remove(n);
                }
            }

        }
        return edges;
    }

    private boolean hasSingleNeighbourNodes() {
        for (Node n : this.getNodes()){
            if (n.getEdges().size() == 1){
                return true;
            }
        }
        return false;
    }


    public int[][] createAdjacencyMatrix() {
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
        return adjacencyMatrix;
    }

    public void printAdjacencyMatrix(int[][] adjacencyMatrix){
        for (int i = 0; i < adjacencyMatrix.length; i++){
            for (int j = 0; j < adjacencyMatrix[0].length; j++){
                System.out.print(adjacencyMatrix[i][j]);
            }
            System.out.println();
        }
    }


    public List<Graph> enumerateInitialSpanningTrees() {
        return null;
    }

    private Node getUnvisitedChildNode(Node n) {
        for (Edge edge : n.getEdges()) {
            if (!edge.getNode1().equals(this) && !edge.getNode1().isVisited()){
                return edge.getNode1();
            } else if(!edge.getNode2().isVisited()) {
                return edge.getNode2();
            }
        }
        return null;

    }

    private void clearNodes() {
        for (Node node : this.nodes) {
            node.setVisited(false);
        }
    }


    public void BFS() {
        Queue queue = new LinkedList();
        queue.add(this.nodes.get(0));
        System.out.println(this.nodes.get(0));
        this.nodes.get(0).setVisited(true);
        while(!queue.isEmpty()) {
            Node node = (Node)queue.remove();
            Node child=null;
            while((child=getUnvisitedChildNode(node))!=null) {
                child.setVisited(true);
                System.out.println(child);
                queue.add(child);
            }
        }
        // Clear visited property of nodes
        clearNodes();
    }




}
