package com.ex.cy.demo4.alg.graph;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

public class DFSTest {
    Graph g;
    int maxRadias;
    boolean[] marked;
    Set<Integer> farthestVertexs;
    int s;
    //    int[] fromVertex;

    public DFSTest(Graph g, int s) {
    }

    public DFSTest(Graph g, int s, int maxRadias) {
        this.s = s;
        this.g = g;
        this.maxRadias = maxRadias;
//        fromVertex = new int[g.v()];
        marked = new boolean[g.v()];
        farthestVertexs = new LinkedHashSet<>();
        dfs(s, maxRadias);
    }

    public static class Pair<K, V> {
        public K k;
        public V v;

        public Pair() {
        }

        public Pair(K k, V v) {
            this.k = k;
            this.v = v;
        }
    }

    private void dfs(int s, int step) {
        //Key 节点，value 剩余步长
        Stack<Pair<Integer, Integer>> stack = new Stack<>();
        stack.push(new Pair<Integer, Integer>(s, step));
        marked[s] = true;
        while (stack.size() > 0) {
            Pair<Integer, Integer> v = stack.pop();
            if (v.v < 1) {         //到达最大半径边缘，不再遍历周边新节点
                farthestVertexs.add(v.k);
                continue;
            }
            for (int w : g.adj(v.k)) {
                if (marked[w])
                    continue;
                marked[w] = true;
//                fromVertex[w] = v.k;
                stack.push(new Pair<Integer, Integer>(w, v.v - 1));
            }
        }
    }

    //限定步数后，能到达的最远边界
    public static void main(String[] args) {
        Graph g = new Graph(15);
        g.addEdge(0, 1);  //0-1-2*-3-4
        g.addEdge(1, 2);  //  |
        g.addEdge(2, 3);  //  5*-6
        g.addEdge(3, 4);
        g.addEdge(1, 5);
        g.addEdge(5, 6);
        DFSTest dfsTest = new DFSTest(g, 0, 2);
        System.out.println(dfsTest.farthestVertexs); //2,5

        //0-1-2-3*-4
        //  |-7-8*
        //  5-6*
        g.addEdge(1,7);
        g.addEdge(7,8);
        DFSTest dfsTest2 = new DFSTest(g, 0, 3);
        System.out.println(dfsTest2.farthestVertexs); //3,8,6
    }
}
