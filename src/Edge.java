import java.util.Set;

/**
 * Created by Kasper on 31/10/2017.
 */
public class Edge implements Comparable<Edge> {

    private int src;
    private int dest;
    private int weight;

    public Edge(int src, int dest, int weight) {
        if (src < dest) {
            this.src = src;
            this.dest = dest;
        } else {
            this.src = dest;
            this.dest = src;
        }
        this.weight = weight;
    }

    public int getSrc() {
        return src;
    }

    public int getDest() {
        return dest;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;
        if(weight != edge.weight) {
            return false;
        }
        if(src == edge.src && dest == edge.dest) {
            return true;
        }
        if(src == edge.dest && dest == edge.src ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = src;
        result = 31 * result + dest;
        result = 31 * result + weight;
        return result;
    }

    /**
     * @param nodes If either the src or dest is contained in the edge, it will add the other
     *              If neither the src or dest is contained, nothing is added to the set.
     */

    public void addToSetIfUnion(Set<Integer> nodes) {
        if (nodes.contains(this.src)) {
            nodes.add(this.dest);
        } else if (nodes.contains(this.dest)) {
            nodes.add(this.src);
        }
    }

    @Override
    public String toString() {
        return "(" + src + "," + dest + ")";
    }

    @Override
    public int compareTo(Edge o) {
        return this.src - o.src;
    }
}
