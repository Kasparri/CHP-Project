import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Main {


    private static int currentB = Integer.MAX_VALUE;
    private static Graph currentTree = null;
    private static int numberOfSpanningTrees = 0;


    private static Graph loadUVW(String fileName) {

        Graph graph = new Graph();
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

            int n = Integer.parseInt(sc.next()); // Number of verticies
            int m = Integer.parseInt(sc.next()); // Number of edges

            for (int i = 0; i < n; i++) {
                graph.addNode(i);
            }

            for (int i = 0; i < m; i++) {

                int src = graph.getNode(Integer.parseInt(sc.next()) - 1);
                int dest = graph.getNode(Integer.parseInt(sc.next()) - 1);
                int weight = Integer.parseInt(sc.next());

                Edge edge = new Edge(src, dest, weight);
                graph.addEdge(edge);

                //src.addToSuccessors(dest);
                //dest.addToSuccessors(src);

            }

            if (sc.hasNext()) {
                throw new Exception("It's no good");
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }


    public static void main(String[] args) {

        String fileName;
        try {
            fileName = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            fileName = "test03.uwg";
            System.out.println("Using default fileName: " + fileName);
        }

        System.out.println();

        Graph graph = loadUVW(fileName);

        findAllSpanningTrees(graph);
        System.out.println("Amount of spanning trees: " + numberOfSpanningTrees);

        System.out.println();
        System.out.println("Lowest B:" + currentB);
        System.out.println("Optimal tree: " + currentTree);

    }

    public static void findAllSpanningTrees(Graph originalGraph) {

        Graph initialSpanningTree = originalGraph.findInitialSpanningTree();

        Collections.sort(initialSpanningTree.getEdges());
        Collections.sort(initialSpanningTree.getNodes());

        checkB(initialSpanningTree);

        int k = originalGraph.getN() - 1 - 1; // One more for 0-indexed

        findChildren(initialSpanningTree, k, originalGraph);

    }


    static void findChildren(Graph ptree, int k, Graph originalGraph) {

        if (k != -1) {

            Edge ek = ptree.getEdge(k);

            for (Edge gEdge : entr(ptree, ek, originalGraph)) {

                // New graph Tc without ek with edge
                Graph cTree = ptree.makeCopy();

                cTree.setEdge(k, gEdge);

                // This is a spanning tree
                //spanningTrees.add(cTree);

                checkB(cTree);

                findChildren(cTree, k - 1, originalGraph);

            }
            findChildren(ptree, k - 1, originalGraph);
        }
        return;
    }

    public static List<Edge> entr(Graph tree, Edge edge, Graph originalGraph) {
        List<Edge> edges;

        // Fundamental cut set problem
        int src = edge.getSrc();

        List<Edge> newEdges = new ArrayList<>(tree.getEdges());
        newEdges.remove(edge);

        Set<Integer> subgraph1 = findConnectedNodes(src, newEdges); // Nodes in subgraph 1
        Set<Integer> subgraph2 = findConnectedNodes(subgraph1, tree.getNodes()); // Nodes in subgraph 2

        edges = getEdgesBetweenSubgraphs(subgraph1, subgraph2, originalGraph);
        edges.remove(edge); // Removing ek from the list

        return edges;
    }

    private static List<Edge> getEdgesBetweenSubgraphs(Set<Integer> subgraph1, Set<Integer> subgraph2, Graph originalGraph) {
        List<Edge> gEdges = new ArrayList<>();

        for (Edge edge : originalGraph.getEdges()) {
            if ((subgraph1.contains(edge.getSrc()) && subgraph2.contains(edge.getDest()))
                    || (subgraph1.contains(edge.getDest()) && subgraph2.contains(edge.getSrc()))
                    && edge.getWeight() != Graph.NAN_EDGE) {
                gEdges.add(edge);
            }
        }
        return gEdges;
    }

    public static Set<Integer> findConnectedNodes(Set<Integer> notThese, List<Integer> totalList) {
        Set<Integer> nodes = new HashSet<>();
        for (int i : totalList) {
            if (!notThese.contains(i)) {
                nodes.add(i);
            }
        }
        return nodes;
    }

    public static Set<Integer> findConnectedNodes(int initialNode, List<Edge> edges) {
        Set<Integer> nodes = new HashSet<>();
        nodes.add(initialNode);

        int before = nodes.size();
        int after = 0;
        while (before != after) {
            before = nodes.size();
            for (Edge e : edges) {
                e.addToSetIfUnion(nodes);
            }
            after = nodes.size();
        }
        return nodes;
    }

    public static void checkB(Graph tree){
        int temp = currentB;
        currentB = tree.getBValue(currentB);
        if (currentB < temp){
            currentTree = tree;
        }
        numberOfSpanningTrees++;
    }

}