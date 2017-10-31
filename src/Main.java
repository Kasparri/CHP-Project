import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

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

        if (graph.isMirrorable(graph.getEdges(), B)) {
            System.out.println("The graph is mirrorable");
        } else {
            System.out.println("Not mirrorable");
        }

        graph.getNodes().get(0).digit();
        System.out.println("Neighbours:");
        for (Node n : graph.getNodes()) {
            System.out.println(n.getNeighboursByNumber());
        }


    }

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
                Node node = new Node(i);
                graph.addNode(node);
            }

            for (int i = 0; i < m; i++) {

                Node node1 = graph.getNode(Integer.parseInt(sc.next()) - 1);
                Node node2 = graph.getNode(Integer.parseInt(sc.next()) - 1);
                int weight = Integer.parseInt(sc.next());

                Edge edge = new Edge(node1, node2, weight);
                graph.addEdge(edge);
                node1.getEdges().add((edge));
                node2.getEdges().add((edge));
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

}
