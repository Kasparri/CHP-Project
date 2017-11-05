/**
 * Created by Kasper on 31/10/2017.
 */
@SuppressWarnings("WeakerAccess")
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
        return weight == edge.weight && (src == edge.src && dest == edge.dest || src == edge.dest && dest == edge.src);
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
