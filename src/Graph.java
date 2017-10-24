import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Kasper on 24/10/2017.
 */
public class Graph {

    List<Node> nodes;
    Set<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new HashSet<>();
    }

    public Graph(List<Node> nodes, Set<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                '}';
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(Set<Edge> edges) {
        this.edges = edges;
    }

    public void addNode(Node node){
        this.nodes.add(node);
    }

    public void addEdge(Edge edge) {
        this.edges.add(edge);
    }

    public Node getNode(int index) {
        return nodes.get(index);
    }

    public List getNodes() {
        return nodes;
    }

    public Set getEdges() {
        return edges;
    }
}
