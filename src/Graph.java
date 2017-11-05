import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
@SuppressWarnings("WeakerAccess")
public class Graph {

    private int N; // Number of vertices
    private int M; // Number of edges
    private int weight;

    private List<Edge> edges;

    private SpanningTree initialSpanningTree;

    private static int numberOfSpanningTrees = 0;
    private static int currentB = Integer.MAX_VALUE;
    private static SpanningTree bestTree = null;

    public Graph(int N) {
        this.N = N;
        this.edges = new ArrayList<>();
        this.weight = 0;
    }

    public static Graph loadUWG(String fileName) throws FileNotFoundException {
        String path = "src/";
        Scanner sc = new Scanner(new File(path + fileName + ".uwg"));

        int N = Integer.parseInt(sc.next()); // Number of verticies
        Graph graph = new Graph(N);

        int M = Integer.parseInt(sc.next()); // Number of edges
        for (int i = 0; i < M; i++) {
            int src = Integer.parseInt(sc.next()) - 1;
            int dest = Integer.parseInt(sc.next()) - 1;
            int weight = Integer.parseInt(sc.next());

            Edge edge = new Edge(src, dest, weight);
            graph.addEdge(edge);
        }

        sc.close();
        return graph;
    }

    protected int getN() {
        return N;
    }

    public static int getNumberOfSpanningTrees() {
        return numberOfSpanningTrees;
    }

    public static int getCurrentB() {
        return currentB;
    }

    public static Graph getBestTree() {
        return bestTree;
    }

    public int getWeight(){
        return this.weight;
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    protected void addEdge(Edge edge) {
        addEdge(edge.getSrc(), edge.getDest(), edge.getWeight());
    }

    public void addEdge(int v1, int v2, int weight) {
        Edge e = new Edge(v1, v2, weight);
        edges.add(e);
        M++;
        this.weight += weight;
    }

    public void removeEdge(int src, int dest) {
        for (Edge e : edges) {
            if (e.getSrc() == src && e.getDest() == dest) {
                edges.remove(e);
                weight-= e.getWeight();
                break;
            }
        }
    }

    public void findAllSpanningTrees() {
        initialSpanningTree = findInitialSpanningTree();
        checkTree(initialSpanningTree);

        int k = this.M - 1 - 1; // One more for 0-indexed
        findChildren(initialSpanningTree, k);
    }

    private SpanningTree findInitialSpanningTree() {
        SpanningTree initialSpanningTree = new SpanningTree(this.N);

        // Adding edges when they don't create a cycle (i.e getting a minimal spanning tree)
        for (Edge e : this.edges) {
            initialSpanningTree.addEdge(e);
        }
        return initialSpanningTree;
    }

    private void findChildren(SpanningTree ptree, int k) {
        if (k != -1) {
            Edge ek = edges.get(k);

            for (Edge gEdge : entr(ptree, ek)) {

                // New graph Tc without ek with edge
                SpanningTree cTree = ptree.copy();

                cTree.removeEdge(ek.getSrc(), ek.getDest());
                cTree.addEdge(gEdge);

                checkTree(cTree);

                findChildren(cTree, k - 1);

            }
            findChildren(ptree, k - 1);
        }
    }

    private List<Edge> entr(SpanningTree tree, Edge ek) {
        // Fundamental cut problem
        List<Edge> gEdges = new ArrayList<>();

        SpanningTree treeCopy = tree.copy();
        SpanningTree initialCopy = initialSpanningTree.copy();

        treeCopy.removeEdge(ek.getSrc(), ek.getDest());
        initialCopy.removeEdge(ek.getSrc(), ek.getDest());

        for (Edge e : edges) {
            if ( !treeCopy.connected(e.getSrc(), e.getDest())
                    && !initialCopy.connected(e.getSrc(), e.getDest())
                    && !e.equals(ek)) {
                gEdges.add(e);
            }
        }
        return gEdges;
    }

    @Override
    public String toString() {
        return edges.toString();
    }

    private int getMirrorWeight(List<Edge> edges, Graph originalGraph) {
        int total_weight = 0;

        for (Edge e : edges) {
            int weight = originalGraph.getMirrorEdgeWeight(e);
            total_weight += weight;
        }
        return total_weight;
    }

    protected int getMirrorEdgeWeight(Edge e) {
        for (int i = 0; i < this.edges.size(); i++) {
            if (edges.get(i).equals(e)) {
                return this.edges.get(this.edges.size() - 1 - i).getWeight();
            }
        }
        System.out.println("No mirror edge found");
        return Integer.MIN_VALUE;
    }

    protected int getBValue(int currentB, Graph originalGraph) {
        int B = this.getWeight();
        int mirrorB = getMirrorWeight(edges, originalGraph);
        int maxB = Integer.max(B, mirrorB);

        if (maxB < currentB) {
            currentB = maxB;
        }
        return currentB;
    }

    private void checkTree(SpanningTree tree) {
        int temp = currentB;
        currentB = tree.getBValue(currentB, this);
        if (currentB < temp) {
            bestTree = tree;
        }
        numberOfSpanningTrees++;
    }
}