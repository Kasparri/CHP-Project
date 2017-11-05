import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Graph {

    private int N; // Number of vertices
    private int M; // Number of edges

    private List<Integer> nodes;
    private List<Edge> edges;

    private Tree initialSpanningTree;

    private int numberOfSpanningTrees = 0;
    private static int currentB = Integer.MAX_VALUE;
    private static Tree bestTree = null;


    public Graph(int N) {
        this.N = N;
        this.nodes = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            nodes.add(i);
        }
        this.edges = new ArrayList<>();
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

    protected void addEdge(Edge edge) {
        addEdge(edge.getSrc(), edge.getDest(), edge.getWeight());
    }

    public void addEdge(int v1, int v2, int weight) {
        Edge e = new Edge(v1, v2, weight);
        edges.add(e);
        M++;
    }

    public void removeEdge(int src, int dest) {
        for (Edge e : edges) {
            if (e.getSrc() == src && e.getDest() == dest) {
                edges.remove(e);
                break;
            }
        }
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    void findAllSpanningTrees() {
        initialSpanningTree = findInitialSpanningTree();
        checkB(initialSpanningTree);

        int k = this.M - 1 - 1; // One more for 0-indexed
        findChildren(initialSpanningTree, k);
    }


    private Tree findInitialSpanningTree() {
        Tree initialSpanningTree = new Tree(this.N);

        // Adding edges when they don't create a cycle (i.e getting a minimal spanning tree)
        for (Edge e : this.edges) {
            initialSpanningTree.addEdge(e);
        }
        return initialSpanningTree;
    }

    private void findChildren(Tree ptree, int k) {
        if (k != -1) {
            Edge ek = edges.get(k);

            for (Edge gEdge : entr(ptree, ek)) {

                // New graph Tc without ek with edge
                Tree cTree = ptree.copy();

                cTree.removeEdge(ek.getSrc(), ek.getDest());
                cTree.addEdge(gEdge);

                checkB(cTree);

                findChildren(cTree, k - 1);

            }
            findChildren(ptree, k - 1);
        }
    }

    private List<Edge> entr(Tree tree, Edge ek) {
        // Fundamental cut problem
        List<Edge> gEdges = new ArrayList<>();

        Tree treeCopy = tree.copy();
        Tree initialCopy = initialSpanningTree.copy();

        treeCopy.removeEdge(ek.getSrc(), ek.getDest());
        initialCopy.removeEdge(ek.getSrc(), ek.getDest());

        for (Edge e : edges) {
            if (!treeCopy.connected(e.getSrc(), e.getDest()) && !initialCopy.connected(e.getSrc(), e.getDest()) && !e.equals(ek)) {
                gEdges.add(e);
            }
        }

        return gEdges;
    }

    private int getGraphWeight() {
        int sum = 0;
        for (Edge edge : this.edges) {
            sum += edge.getWeight();
        }
        return sum;
    }

    @Override
    public String toString() {
        return edges.toString();
    }

    protected int getMirrorWeight(List<Edge> edges, Graph originalGraph) {
        int total_weight = 0;

        for (Edge e : edges) {
            int weight = originalGraph.getMirrorEdgeWeight(e);
            total_weight += originalGraph.getMirrorEdgeWeight(e);
        }
        return total_weight;
    }

    protected int getMirrorEdgeWeight(Edge e) {
        for (int i = 0; i < this.edges.size(); i++) {
            Edge ef = edges.get(i);
            if (edges.get(i).equals(e)) {
                return this.edges.get(this.edges.size() - 1 - i).getWeight();
            }
        }
        return Integer.MIN_VALUE;
    }

    protected int getBValue(int currentB, Graph originalGraph) {
        int B = this.getGraphWeight();
        int mirrorB = getMirrorWeight(edges, originalGraph);
        int maxB = Integer.max(B, mirrorB);

        if (maxB < currentB) {
            currentB = maxB;
        }
        return currentB;
    }

    protected void checkB(Tree tree) {
        int temp = currentB;
        currentB = tree.getBValue(currentB, this);
        if (currentB < temp) {
            bestTree = tree;
        }
        numberOfSpanningTrees++;
    }

    public int getNumberOfSpanningTrees() {
        return numberOfSpanningTrees;
    }


    public static int getCurrentB() {
        return currentB;
    }

    public static Graph getBestTree() {
        return bestTree;
    }

}
