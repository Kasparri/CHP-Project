class Tree extends Graph {
	
	private UF UF;
	
	public Tree(int noOfNodes) {
		super(noOfNodes);
		UF = new UF(noOfNodes);
	}

	public void addEdge(int v1, int v2, int weight) {
		if (UF.connected(v1, v2)) {
			return; // Don't add if it creates a cycle
		}
		UF.union(v1, v2);
		
		super.addEdge(v1, v2, weight);
	}
	
	public void removeEdge(int src, int dest) {
		super.removeEdge(src, dest);
		UF = new UF(this.getN());

		for (Edge e : getEdges()) {
			UF.union(e.getSrc(), e.getDest());
		}
	}
	
	public boolean connected(int v1, int v2) {
		return UF.connected(v1, v2);
	}

	
	@Override
	public Object clone() {
		Tree g = new Tree(getN());
		for (Edge e : getEdges()) {
			g.addEdge(e);
		}
		return g;
	}
}