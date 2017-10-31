import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasper on 24/10/2017.
 */
public class Node {

    private List<Edge> edges;

    private int number;
    private boolean visited;


    public Node(){
        this.edges = new ArrayList<>();
    }

    public Node(int number){
        this.number = number;
        this.edges = new ArrayList<>();
    }

    public Node(List<Edge> edges){
        this.edges = edges;
    }

    public Node(int number, List<Edge> edges){
        this.number = number;
        this.edges = edges;
    }

    public String toString(){
        return  "v" + this.number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public List<Integer> getNeighboursByNumber(){

        List<Integer> numbers = new ArrayList<>();

        List<Node> edgeNodes = new ArrayList<>();
        for (Edge edge : edges) {
            if (!edge.getNode1().equals(this)){
                edgeNodes.add(edge.getNode1());
            } else {
                edgeNodes.add(edge.getNode2());
            }
        }

        for (Node n : edgeNodes){
            numbers.add(n.getNumber());
        }
        return numbers;
    }


    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
