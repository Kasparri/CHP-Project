import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Main {


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
            fileName = "test01.uwg";
            System.out.println("Using default fileName: " + fileName);
        }

        Graph graph = loadUVW(fileName);
        System.out.println(graph);

        int B;
        try {
            B = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            B = 100; // Default value
            System.out.println("Using default B value: " + B);
        }

        System.out.println(findAllSpanningTrees(graph).size());

        //Graph initial = graph.findInitialSpanningTree();
        //System.out.println("Initial: " + initial + " with weight: " + initial.getGraphWeight());



//        // TESTING ENV START
//
//        Edge e1 = new Edge(0,1,10);
//        Edge e2 = new Edge(0,2,10);
//        Edge e3 = new Edge(2,3,10);
//        Edge e4 = new Edge(4,5,10);
//        Edge e5 = new Edge(4,6,10);
//        Edge e6 = new Edge(6,7,10);
//        List<Edge> edges = new ArrayList<>();
//        edges.add(e1);
//        edges.add(e2);
//        edges.add(e3);
//        edges.add(e4);
//        edges.add(e5);
//        edges.add(e6);
//
//        Set<Integer> nodes = findConnectedNodes(0,edges);
//        Set<Integer> nodes2 = findConnectedNodes(4,edges);
//        System.out.println(nodes.size());
//        System.out.println(nodes2.size());
//
//
//        System.out.println(findConnectedNodes(0,initial.getEdges()).size());
//
//        // TESTING ENV END


    }

    public static List<Graph> findAllSpanningTrees(Graph originalGraph) {

        List<Graph> spanningTrees = new ArrayList<>();

        Graph initialSpanningTree = originalGraph.findInitialSpanningTree();
        spanningTrees.add(initialSpanningTree);

        int k = originalGraph.getN() - 2;

        findChildren(initialSpanningTree, k, spanningTrees, originalGraph, initialSpanningTree);

        return spanningTrees;
    }


    static void findChildren(Graph ptree, int k, List<Graph> spanningTrees, Graph originalGraph, Graph initalTree) {

        if (k != -1 && k < Min(initalTree,ptree)) {

            Edge ek = ptree.getEdge(k);

            for (Edge gEdge : entr(ptree, ek, originalGraph)) {


                // TODO: stuff
                // cTree is pTree without edge k with edge g.
                // Finding the fundamental cut associated with (T \ f) = C*(T\f)

                // New graph Tc without ek with edge
                Graph cTree = ptree.makeCopy();

                cTree.setEdge(k, gEdge);

                //cTree.removeEdge(ek);
                //cTree.addEdge(gEdge);


                // This is a spanning tree
                spanningTrees.add(cTree);
                findChildren(cTree, k - 1, spanningTrees, originalGraph, initalTree);


            }
            findChildren(ptree, k - 1, spanningTrees, originalGraph, initalTree);
        }
        return;
    }

    public static List<Edge> entr(Graph tree, Edge edge, Graph originalGraph) {
        List<Edge> edges;

        // Fundamental cut set problem

        int src = edge.getSrc();

        List<Edge> newEdges = new ArrayList<>(tree.getEdges());
        newEdges.remove(edge);

        Set<Integer> subgraph1 = findConnectedNodes(src,newEdges); // Nodes in subgraph 1
        Set<Integer> subgraph2 = findConnectedNodes(subgraph1,tree.getNodes()); // Nodes in subgraph 2

        edges = getEdgesBetweenSubgraphs(subgraph1,subgraph2, originalGraph);
        edges.remove(edge); // Removing ek

        return edges;
    }

    private static List<Edge> getEdgesBetweenSubgraphs(Set<Integer> subgraph1, Set<Integer> subgraph2, Graph originalGraph) {
        List<Edge> gEdges = new ArrayList<>();

        for (Edge edge : originalGraph.getEdges()){

            if ( (subgraph1.contains(edge.getSrc()) && subgraph2.contains(edge.getDest()))
                    || ( subgraph1.contains(edge.getDest()) && subgraph2.contains(edge.getSrc()))
                    && edge.getWeight() != Graph.NAN_EDGE){
                gEdges.add(edge);
            }
        }
        return gEdges;
    }

    public static Set<Integer> findConnectedNodes(Set<Integer> notThese, List<Integer> totalList){
        Set<Integer> nodes = new HashSet<>();
        for (int i : totalList){
            if (!notThese.contains(i)){
                nodes.add(i);
            }
        }
        return nodes;
    }

    public static Set<Integer> findConnectedNodes(int initialNode, List<Edge> edges){
        Set<Integer> nodes = new HashSet<>();

        nodes.add(initialNode);

        int before = nodes.size();
        int after = 0;
        while (before != after){
            before = nodes.size();
            for (Edge e : edges){
                e.addToSetIfUnion(nodes);
            }
            after = nodes.size();
        }
        return nodes;
    }

    public static int Min(Graph initialTree, Graph currentTree){
        for (int i = 0; i < initialTree.getEdges().size(); i++){
            if (!currentTree.getEdges().contains(initialTree.getEdge(i))){
                return i;
            }
        }
        return initialTree.getEdges().size()+1;
    }


}
