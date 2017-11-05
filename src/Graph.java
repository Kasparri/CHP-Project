import java.io.File;
import java.io.FileNotFoundException;
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
    private int numberOfSpanningTrees = 0;
    private static int currentB = Integer.MAX_VALUE;
    private static Tree bestTree = null;
    private Tree t0;

    private int[][] adjacencyMatrix;

    Graph(int N) {
        this.N = N;
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }
    Graph(String fileName) {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
        String path = "src/";
        File file = new File(path + fileName);
        Scanner sc = null;

        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException ex) {
            file = new File(path + "test01.uwg");
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            int TempN = Integer.parseInt(sc.next()); // Number of verticies
            int TempM = Integer.parseInt(sc.next()); // Number of edges

            for (int i = 0; i < TempN; i++) {
                this.addNode(i);
                N++;
            }

            for (int i = 0; i < TempM; i++) {

                int src = this.getNode(Integer.parseInt(sc.next()) - 1);
                int dest = this.getNode(Integer.parseInt(sc.next()) - 1);
                int weight = Integer.parseInt(sc.next());

                Edge edge = new Edge(src, dest, weight);
                this.addEdge(edge);

            }

            if (sc.hasNext()) {
                throw new Exception("It's no good");
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getNode(int index) {
        return getNodes().get(index);
    }

    List<Integer> getNodes() {
        return nodes;
    }

    int getN() {
        return N;
    }

    void addNode(int node) {
        nodes.add(node);
    }

    void addEdge(Edge edge) {
        addEdge(edge.getSrc(),edge.getDest(),edge.getWeight());
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

    Edge getEdge(int index) {
        return this.edges.get(index);
    }

    List<Edge> getEdges() {
        return this.edges;
    }


    private int[][] fillAdjacencyMatrix() {
        adjacencyMatrix = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                adjacencyMatrix[i][j] = NAN_EDGE;
            }
        }
        for (Edge e : edges) {
            adjacencyMatrix[e.getSrc()][e.getDest()] = e.getWeight();
            adjacencyMatrix[e.getDest()][e.getSrc()] = e.getWeight();
        }
        return adjacencyMatrix;
    }

    private List<Integer> findSuccessors(int node) {
        List<Integer> successors = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            int weight = adjacencyMatrix[node][i];
            if (weight != NAN_EDGE) {
                successors.add(i);
            }
        }
        return successors;
    }


    private Edge findEdge(int parent, int successor) {
        for (Edge edge : edges) {
            if ((edge.getSrc() == parent && edge.getDest() == successor)
                    || (edge.getSrc() == successor && edge.getDest() == parent)) {
                return edge;
            }
        }
        return null;
    }


    private Tree findInitialSpanningTree() {

        // DFS Version
        Tree initialTree = new Tree(N);

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
            for (int successor : findSuccessors(parent)) {
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

    void findAllSpanningTrees() {

        findFirstSpanningTree();

        //Collections.sort(t0.getEdges());
        //Collections.sort(t0.getNodes());

        checkB(t0);

        int k = getM() - 1 - 1; // One more for 0-indexed

        findChildren(t0, k);
    }


    private void findFirstSpanningTree() {
        t0 = new Tree(N);
//		t0.addEdge(edges.get(noOfEdges-1));
        for (Edge e : edges) {
            t0.addEdge(e);
        }
        for (int n : nodes) {
            t0.addNode(n);
        }
    }

    private void findChildren(Tree ptree, int k) {

        if (k != -1) {
            Edge ek = edges.get(k);

            for (Edge gEdge : entr(ptree, k)) {

                // New graph Tc without ek with edge
                Tree cTree = (Tree) ptree.clone();

                cTree.removeEdge(ek.getSrc(),ek.getDest());
                cTree.addEdge(gEdge);

                // This is a spanning tree
                //spanningTrees.add(cTree);

                checkB(cTree);

                findChildren(cTree, k - 1);

            }
            findChildren(ptree, k - 1);
        }
    }

    private List<Edge> entr(Tree tp, int k) {
        // Entr(T^p, e_k) = C*(T^p\e_k) intersect C*(T^0\e_k)
        // C* = fundamental cut

        List<Edge> commonCutEdges = new LinkedList<Edge>();

        Edge ek = edges.get(k);
        int v1 = ek.getSrc();
        int v2 = ek.getDest();
        Tree tp_ = (Tree) tp.clone();
        Tree t0_ = (Tree) t0.clone();
        tp_.removeEdge(v1, v2);
        t0_.removeEdge(v1, v2);

        for (Edge e : edges) {
            if (!tp_.connected(e.getSrc(), e.getDest()) && !t0_.connected(e.getSrc(), e.getDest()) && !e.equals(ek)) {
                commonCutEdges.add(e);
            }
        }

        return commonCutEdges;
    }

    int getGraphWeight() {
        int sum = 0;
        for (Edge edge : this.edges) {
            sum += edge.getWeight();
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Graph{" +
                nodes.size() + " vertices" +
                ", edges=" + edges +
                '}';
    }

    void setEdge(int k, Edge gEdge) {
        this.edges.set(k, gEdge);
    }

    int getMirrorWeight(List<Edge> edges, Graph originalGraph) {
        int total_weight = 0;

        for (Edge e : edges) {
            int weight = originalGraph.getMirrorEdgeWeight(e);
            total_weight += originalGraph.getMirrorEdgeWeight(e);
        }
        return total_weight;
    }

    private int getMirrorEdgeWeight(Edge e) {
        for (int i = 0; i < this.edges.size(); i++) {
            Edge ef = edges.get(i);
            if (edges.get(i).equals(e)) {
                return this.edges.get(this.edges.size() - 1 - i).getWeight();
            }
        }
        return Integer.MIN_VALUE;
    }

    int getBValue(int currentB, Graph originalGraph) {
        int B = this.getGraphWeight();
        int mirrorB = getMirrorWeight(edges, originalGraph);
        int maxB = Integer.max(B, mirrorB);

        if (maxB < currentB) {
            currentB = maxB;
        }
        return currentB;
    }

    void checkB(Tree tree) {
        int temp = currentB;
        currentB = tree.getBValue(currentB, this);
        if (currentB < temp) {
            bestTree = tree;
        }
        numberOfSpanningTrees++;
    }

    int getNumberOfSpanningTrees() {
        return numberOfSpanningTrees;
    }
    String toGraphviz() {
        String s = "graph g {\n" +
                "	rankdir=LR;\n" +
                "	size=\"8,5\"\n" +
                "	node [shape = circle];\n";

        for (Edge e : edges) {
            s += "	" + e.getSrc() + " -- " + e.getDest() + "[ label = " + e.getWeight() + " ];\n";
        }
        s += "}";

        return s;
    }

    static int getCurrentB() {
        return currentB;
    }
    static Graph getBestTree() {
        return bestTree;
    }

    public int getM() {
        return M;
    }
}
