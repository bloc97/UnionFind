package unionfind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * IFT2015 Devoir 2, Bowen Peng et Lifeng Wan
 * @author bowen, lifeng
 */
public class Main {
    
    private static final int RANDOMSEED = 42;
    
    private static final int ITERATIONS = 5;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Union count, Array reads, Array writes, Array operations");
        System.out.println("Running " + ITERATIONS + " iterations.");
        
        for (int pow = 1; pow<25; pow++) {
        
            int n = (int)Math.pow(2, pow);
            System.out.println("Pow = " + pow + ", No path compression.");
            System.out.println(Arrays.deepToString(testUnionFind(n, ITERATIONS, false)));
            
            System.out.println("Pow = " + pow + ", Full path compression.");
            System.out.println(Arrays.deepToString(testUnionFind(n, ITERATIONS, true)));
        }
        
        
    }
    
    public static long[][] testUnionFind(int n, int iters, boolean usePathCompression) {
        long[][] counts = new long[4][iters];
        Random random = new Random(RANDOMSEED + n); //Deterministic PRNG, get the same results for easy debugging.
        
        UnionFind u = new UnionFind(n, usePathCompression);
            
        for (int i=0; i<iters; i++) {
            
            //Create the adjacency list
            List<HashSet<Integer>> adjacencyList = new ArrayList<>(n); //Usng a hash set is faster than a LinkedList
            for (int k=0; k<n; k++) {
                adjacencyList.add(k, new HashSet<>((int)Math.log(n)));
            }
            
            int count = 0;
            
            while(!u.isEverythingConnected()) { //While graph is not connected
                int x = random.nextInt(n); //Get random vertex
                int y = random.nextInt(n);
                
                if (!adjacencyList.get(x).contains(y)) { //If vertex does not exist
                    adjacencyList.get(x).add(y); //Add into adjacency list
                    adjacencyList.get(y).add(x);
                    u.union(x, y); //Union
                    count++; //Union count +1
                }
                
            }
            //Save the union count and the operation count
            counts[0][i] = count;
            counts[1][i] = u.getGetParentOperations();
            counts[2][i] = u.getSetParentOperations();
            counts[3][i] = u.getAllParentOperations();
            
            u.reset(); //Reuse the object by resetting the UnionFind instance
            //Force the GC to clear the memory right now, to prevent using too much heap
            for (int k=0; k<n; k++) {
                adjacencyList.get(k).clear();
            }
            adjacencyList.clear();
        }
        return counts;
    }
    
    
}
