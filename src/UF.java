public class UF {

	// Implementation from https://algs4.cs.princeton.edu/15uf/UF.java.html

	private int[] parent;  // parent[i] = parent of i
	private int[] rank;   // rank[i] = rank of subtree rooted at i (never more than 31)

	/**
	 * Initializes an empty union–find data structure with {@code n} sites
	 * {@code 0} through {@code n-1}. Each site is initially in its own
	 * component.
	 *
	 * @param n the number of sites
	 * @throws IllegalArgumentException if {@code n < 0}
	 */
	public UF(int n) {
		if (n < 0) throw new IllegalArgumentException();
		parent = new int[n+1];
		rank = new int[n+1];
		for (int i = 0; i < n+1; i++) {
			parent[i] = i;
			rank[i] = 1;
		}
	}

	/**
	 * Returns the component identifier for the component containing site {@code p}.
	 *
	 * @param p the integer representing one site
	 * @return the component identifier for the component containing site {@code p}
	 * @throws IllegalArgumentException unless {@code 0 <= p < n}
	 */
	public int find(int p) {
		validate(p);
		while (p != parent[p]) {
			parent[p] = parent[parent[p]];    // path compression by halving
			p = parent[p];
		}
		return p;
	}


	/**
	 * Returns true if the the two sites are in the same component.
	 *
	 * @param p the integer representing one site
	 * @param q the integer representing the other site
	 * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
	 * {@code false} otherwise
	 * @throws IllegalArgumentException unless
	 *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
	 */
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	/**
	 * Merges the component containing site {@code p} with the
	 * the component containing site {@code q}.
	 *
	 * @param p the integer representing one site
	 * @param q the integer representing the other site
	 * @throws IllegalArgumentException unless
	 *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
	 */
	public void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);
		if (rootP == rootQ) return;

		// make root of smaller rank point to root of larger rank
		if (rank[rootP] < rank[rootQ]) {
			parent[rootP] = rootQ;
			rank[rootQ] += rank[rootP];
		} else {
			parent[rootQ] = rootP;
			rank[rootP] += rank[rootQ];
		}
	}

	// validate that p is a valid index
	private void validate(int p) {
		int n = parent.length;
		if (p < 0 || p >= n) {
			throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
		}
	}
}