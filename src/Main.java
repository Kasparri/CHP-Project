import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Main {

    static Graph originalGraph = null;
    public static List<Edge> originalEdges = null;
    static Graph prunedGraph = null;

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        try {
            String fileName = "TestFile4";
            originalGraph = Graph.loadUWG(fileName);
            originalEdges = originalGraph.getEdges();

            List<Edge> removedEdges = null;
            removedEdges = pruneGraph(originalGraph);

            System.out.println(removedEdges);
            prunedGraph.findAllSpanningTrees();


            Graph best = Graph.getBestTree();

            System.out.println(originalGraph.getWeight(removedEdges));
            System.out.println(originalGraph.getMirrorEdgesWeight(removedEdges));

            System.out.println(originalGraph.getWeight(best.getEdges()));
            System.out.println(originalGraph.getMirrorEdgesWeight(best.getEdges()));



            Graph.getBestTree().checkTree(Graph.getBestTree());



            System.out.println("Amount of spanning trees: " + Graph.getNumberOfSpanningTrees());
            System.out.println("Lowest B:" + Graph.getCurrentB());
            System.out.println("Optimal tree: " + Graph.getBestTree());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("\nMilliseconds: " + totalTime);
    }


    public static List<Edge> pruneGraph(Graph g){

        prunedGraph = g.copy();

        List<Edge> removedEdges = new ArrayList<>();

        int[][] adjacencyMatrix = createAdjacencyMatrix(prunedGraph);

        for (int i = 0; i < adjacencyMatrix.length; i++){
            List<Edge> edges = new ArrayList<>();
            for (int j = 0; j < adjacencyMatrix[i].length; j++){
                if (adjacencyMatrix[i][j] != -1){
                    int index = prunedGraph.getEdges().indexOf(new Edge(i,j,adjacencyMatrix[i][j]));
                    edges.add(prunedGraph.getEdges().get(index));
                }
            }
            if (edges.size() == 1){
                Edge e = edges.get(0);
                prunedGraph.removeEdge(e.getSrc(),e.getDest());
                adjacencyMatrix[e.getSrc()][e.getDest()] = -1;
                adjacencyMatrix[e.getDest()][e.getSrc()] = -1;
                removedEdges.add(e);
            }
        }

        return removedEdges;
    }



    public static int[][] createAdjacencyMatrix(Graph g){
        int N = g.getN();
        int[][] adjacencyMatrix = new int[N][N];

        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                adjacencyMatrix[i][j] = -1;
            }
        }
        for (Edge e : g.getEdges()){
            adjacencyMatrix[e.getSrc()][e.getDest()] = e.getWeight();
            adjacencyMatrix[e.getDest()][e.getSrc()] = e.getWeight();
        }
        return adjacencyMatrix;
    }



}