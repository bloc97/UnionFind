package unionfind;

/**
 * IFT2015 Devoir 2, Bowen Peng et Lifeng Wan
 * @author bowen, lifeng
 */
public class UnionFind {
    private int[] parent; //Parents of the set, becomes root after path compression of find(x).
    private int[] rank; //Rank of the set, only guaranteed to be correct when used with roots.
    private int[] size; //Size of the set, only guaranteed to be correct when used with roots.
    
    private int count; //Number of disjoint components
    
    private boolean usePathCompression;
    private long getParentOperations = 0L;
    private long setParentOperations = 0L;
    
    private static final boolean COUNT_OPERATIONS = true;
    
    public UnionFind(int n, boolean usePathCompression) {
        if (n < 1) {
            throw new IllegalArgumentException("Number of disjoint sets cannot be empty.");
        }
        
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        
        //Initialise all nodes as root node, n sets of size 1, each set's rank as 0.
        for (int i=0; i<n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
        count = n;
        this.usePathCompression = usePathCompression;
    }
    
    public int getParent(int i) {
        if (COUNT_OPERATIONS) {
            getParentOperations++;
        }
        return parent[i];
    }
    
    public void setParent(int i, int v) {
        if (COUNT_OPERATIONS) {
            setParentOperations++;
        }
        parent[i] = v;
    }

    /**
     * Resets the UnionFind instance
     */
    public void reset() {
        int n = parent.length;
        for (int i=0; i<n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }
        count = n;
        getParentOperations = 0;
        setParentOperations = 0;
    }
    
    public long getGetParentOperations() {
        return getParentOperations;
    }
    public long getSetParentOperations() {
        return setParentOperations;
    }
    public long getAllParentOperations() {
        return getParentOperations + setParentOperations;
    }
    
    /**
     * Union of two disjoint sets.
     * 
     * @param x
     * @param y
     * @return rank of new set, -1 if the union fails.
     */
    public int union(int x, int y) {
        if (x == y) { //Cannot connect two element that are identical
            return -1;
        }
        
        int xroot = find(x);
        int yroot = find(y);
        
        if (xroot == yroot) { //If they are already from the same set, return -1
            return -1;
        }
        
        int xrootRank = rank[xroot];
        int yrootRank = rank[yroot];
        
        //New size of set is the sum of the size of the two disjoint sets
        int newSize = size[xroot] + size[yroot];
        
        count--;
        
        //Union based on the rank, smaller set goes into larger set
        if (xrootRank < yrootRank) {
            setParent(xroot, yroot);
            size[yroot] = newSize;
            return yrootRank;
        } else if (xrootRank > yrootRank) {
            setParent(yroot, xroot);
            size[xroot] = newSize; 
            return xrootRank;
        } else { //If both rank are the same, create new root with bigger rank
            setParent(yroot, xroot);
            int newRank = rank[xroot] + 1;
            rank[xroot] = newRank;
            size[xroot] = newSize;
            return newRank;
        }
        
    }
    
    /**
     * Find the root of the current element's set.
     * @param x
     * @return root element.
     */
    public int find(int x) {
        if (usePathCompression) { //Use path compression
            int xParent = getParent(x); //Get the current parent
            if (xParent != x) { //If the parent isn't itself (this is not a root node)
                int newxParent = find(xParent); //Find the root of the parent
                if (xParent != newxParent) { //If the current parent is not equal to the root
                    setParent(x, newxParent); //Set the parent as the root
                }
                return newxParent; //Return the root node
            } else {
                return xParent; //Return itself, since it is already a root node
            }
        } else {
            int xParent = getParent(x); //Get the current parent
            int thisX = x; //Get the current element
            
            while(xParent != thisX) { //While we are not in a root node
                thisX = xParent; //Set thisX as the parent
                xParent = getParent(thisX); //Set xParent as the parent of thisX
            }
            return xParent; //Return the root node
        }
    }
    
    /**
     * @param x
     * @return size of the set belonging to the element.
     */
    public int size(int x) {
        return size[find(x)];
    }
    
    public int components() {
        return count;
    }
    
    /**
     * Checks if two elements are in the same set
     * 
     * @param x
     * @param y
     * @return true if both elements are from the same set.
     */
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }
    
    /**
     * Checks if the graph is a connected graph
     * 
     * @param x
     * @param y
     * @return true if the graph is connected
     */
    public boolean isEverythingConnected() {
        return count == 1; //After making n amounts of unions of disjoint sets, the graph should be connected.
    }
    
}
