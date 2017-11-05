class SpanningTree extends Graph {
	
	private UF UF;
	
	public SpanningTree(int N) {
		super(N);
		UF = new UF(N);
	}

	public void addEdge(int src, int dest, int weight) {
		if (UF.connected(src, dest)) {
			return; // Don't add if it creates a cycle
		}
		UF.union(src, dest);
		super.addEdge(src, dest, weight);
	}
	
	public void removeEdge(int src, int dest) {
		super.removeEdge(src, dest);
		UF = new UF(this.getN());

		for (Edge e : getEdges()) {
			UF.union(e.getSrc(), e.getDest());
		}
	}
	
	public boolean connected(int src, int dest) {
		return UF.connected(src, dest);
	}

	public SpanningTree copy() {
		SpanningTree g = new SpanningTree(getN());
		for (Edge e : getEdges()) {
			g.addEdge(e);
		}
		return g;
	}
}