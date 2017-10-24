/**
 * Created by Kasper on 24/10/2017.
 */
public class Edge {

    Node node1;
    Node node2;
    int weight;


    public Edge(Node node1, Node node2, int weight) {
        this.node1 = node1;
        this.node2 = node2;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                node1 +
                ", " + node2 +
                ", " + weight +
                '}';
    }
}
