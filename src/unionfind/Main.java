/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unionfind;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author bowen
 */
public class Main {
    
    private static int RANDOMSEED = 42;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int pow = 15;
        int n = (int)Math.pow(2, pow);
        int iters = 5;
        System.out.println(n);
        
        System.out.println(Arrays.toString(testUnionFind(n, iters)));
        
        
    }
    
    public static int[] testUnionFind(int n, int iters) {
        int[] counts = new int[iters];
        Random random = new Random(RANDOMSEED);
        
        for (int i=0; i<iters; i++) {
            System.out.println("Iteration " + i + " Started.");
            UnionFind u = new UnionFind(n, true);
            System.out.println("Iteration " + i + " UnionFind created.");
            List<HashSet<Integer>> adjacencyList = new ArrayList<>(n);
            for (int k=0; k<n; k++) {
                adjacencyList.add(k, new HashSet<>());
            }
            System.out.println("Iteration " + i + " Adjacency list created.");
            
            int count = 0;
            
            while(!u.isEverythingConnected()) {
                int x = random.nextInt(n);
                int y = random.nextInt(n);
                
                if (!adjacencyList.get(x).contains(y)) {
                    adjacencyList.get(x).add(y);
                    adjacencyList.get(y).add(x);
                    u.union(x, y);
                    count++;
                }
                
            }
            counts[i] = count;
            System.out.println(u.getParentOperations());
        }
        return counts;
    }
    
    
}
