import java.util.HashSet;
import java.util.Set;

/**
 * Created by Kasper on 24/10/2017.
 */
public class Node {

    Set<Edge> edges;
    int number;


    public Node(){
        this.edges = new HashSet<>();
    }

    public Node(int number){
        this.number = number;
        this.edges = new HashSet<>();
    }

    public Node(Set<Edge> edges){
        this.edges = edges;
    }

    public Node(int number, Set<Edge> edges){
        this.number = number;
        this.edges = edges;
    }

    public String toString(){
        return  "v" + this.number;
    }

}
