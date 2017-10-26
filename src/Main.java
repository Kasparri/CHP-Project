import java.io.File;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        Graph graph = loadUVW();
        System.out.println(graph);

        int B;
        try {
            B = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException ex){
            B = 100; // Default value
        }


        if (graph.isMirrorable(graph.edges,B)) {
            System.out.println("The graph is mirrorable");
        } else {
            System.out.println("Not mirrorable");
        }

        graph.getNodes().get(0).digit();
        for (Node n : graph.getNodes()){
            System.out.println(n.getNeighboursByNumber());
        }

        graph.createAdjacencyMatrix();


    }


    private static Graph loadUVW() {

        Graph graph = new Graph();

        try {

            String fileName = "src/testFile.txt";
            File file = new File(fileName);

            Scanner sc = new Scanner(file);

            int n = Integer.parseInt( sc.next()) ; // Number of verticies
            int m = Integer.parseInt( sc.next() ); // Number of edges

            for (int i = 0; i < n; i++){
                Node node = new Node(i);
                graph.addNode(node);
            }

            for (int i = 0; i < m; i++){

                Node node1 = graph.getNode(Integer.parseInt(sc.next())-1);
                Node node2 = graph.getNode(Integer.parseInt(sc.next())-1);
                int weight = Integer.parseInt(sc.next());

                Edge edge = new Edge(node1,node2,weight);
                graph.addEdge(edge);
                node1.getEdges().add((edge));
                node2.getEdges().add((edge));
            }

            if (sc.hasNext()){
                throw new Exception("It's no good");
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return graph;
    }








}
