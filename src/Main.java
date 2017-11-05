import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Main {


    private static int currentB = Integer.MAX_VALUE;


    public static void main(String[] args) {

        String fileName;
        try {
            fileName = args[0];
        } catch (ArrayIndexOutOfBoundsException ex) {
            fileName = "test03.uwg";
            System.out.println("Using default fileName: " + fileName);
        }

        System.out.println();

        Graph graph = new Graph(fileName);

        graph.findAllSpanningTrees();
        System.out.println("Amount of spanning trees: " + graph.getNumberOfSpanningTrees());

        System.out.println();
        System.out.println("Lowest B:" + Graph.getCurrentB());
        System.out.println("Optimal tree: " + graph.getBestTree());
        System.out.println(graph.toGraphviz());
        System.out.println(graph.getBestTree().toGraphviz());
    }
}