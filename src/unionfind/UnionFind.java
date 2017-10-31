/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unionfind;

/**
 *
 * @author bowen
 */
public class UnionFind {
    private int[] parent; //Parents of the set, becomes root after path compression of find(x).
    private int[] rank; //Rank of the set, only correct when used with roots.
    private int[] size; //Size of the set, only correct when used with roots.
    
    private int count; //Number of disjoint components
    
    private boolean usePathCompression;
    private long parentOperations = 0L;
    
    private static final boolean COUNT_OPERATIONS = true;
    
    public UnionFind(int n, boolean usePathCompression) {
        if (n < 1) {
            throw new IllegalArgumentException("Size of the disjoint set cannot be empty.");
        }
        
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        
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
            parentOperations++;
        }
        return parent[i];
    }
    
    public void setParent(int i, int v) {
        if (COUNT_OPERATIONS) {
            parentOperations++;
        }
        parent[i] = v;
    }
    
    /**
     * Union of two disjoint sets.
     * 
     * @param x
     * @param y
     * @return rank of new set, -1 if the union fails.
     */
    public int union(int x, int y) {
        if (x == y) {
            return -1;
        }
        
        int xroot = find(x);
        int yroot = find(y);
        
        if (xroot == yroot) {
            return -1;
        }
        
        int xrootRank = rank[xroot];
        int yrootRank = rank[yroot];
        
        int newSize = size[xroot] + size[yroot];
        
        count--;
        
        if (xrootRank < yrootRank) {
            //parent[xroot] = yroot;
            setParent(xroot, yroot);
            size[yroot] = newSize;
            return yrootRank;
        } else if (xrootRank > yrootRank) {
            //parent[yroot] = xroot;
            setParent(yroot, xroot);
            size[xroot] = newSize; 
            return xrootRank;
        } else {
            //parent[yroot] = xroot;
            setParent(yroot, xroot);
            int newRank = rank[xroot] + 1;
            rank[xroot] = newRank;
            size[xroot] = newSize;
            return newRank;
        }
        
    }
    
    public int find(int x) {
        if (getParent(x) != x) {
            if (usePathCompression) {
                setParent(x, find(getParent(x)));
                //parent[x] = find(parent[x]);
            } else {
                return find(getParent(x));
            }
        }
        return getParent(x);
    }
    
    public int size(int x) {
        return size[find(x)];
    }
    
    public int components() {
        return count;
    }
    
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }
    
    public boolean isEverythingConnected() {
        return count == 1;
    }
    
}
