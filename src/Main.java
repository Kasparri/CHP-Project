/**
 * Created by Kasper on 31/10/2017.
 */
public class Main {

    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        try {
            String fileName = "TestFile4";
            Graph graph = Graph.loadUWG(fileName);

            graph.findAllSpanningTrees();
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
}