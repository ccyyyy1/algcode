package com.ex.cy.demo4.alg.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BFSPathTest { //广度优先，记录路径
    Graph g;
    boolean[] marked;
    int[] fromVertex;
    int s;

    public BFSPathTest(Graph g, int s) { //s 起点
        this.g = g;
        this.s = s;
        marked = new boolean[g.v()];
        fromVertex = new int[g.v()];
        Arrays.fill(fromVertex, -1);
        bfs(s);
    }

    //广度优先
    private void bfs(int s) {
        Queue<Integer> queue = new LinkedList();
        queue.add(s);
        marked[s] = true;
        fromVertex[s] = s;
        Integer n;
        while ((n = queue.poll()) != null) {
            System.out.println("v " + n);
            for (int w : g.adj(n)) {
                if (!marked[w]) {
                    marked[w] = true;
                    fromVertex[w] = n;
                    System.out.println("    add  +" + w);
                    queue.add(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
        //fromVertex[v] != -1; 不用这个 因为不考虑其他连通子图，只考虑和s相关的连通图
    }

    public List<Integer> pathTo(int v) {
        List l = new ArrayList();
        if(!hasPathTo(v))
            return l;

        Stack stack = new Stack();
        stack.push(v);
        while (v != s) {
            stack.push(fromVertex[v]);
            v = fromVertex[v];
        }
//        stack.push(s);

        while (!stack.empty()) {
            l.add(stack.pop());
        }
        return l;
    }

    public static void main(String[] args) {
        Graph g = new Graph(7);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(3, 0);

        g.addEdge(4, 5);
        System.out.println("Vertex : edgeTo");
        System.out.println(g.toString());

        BFSPathTest pg = new BFSPathTest(g, 1);
        for (int v = 0; v < g.v(); v++) {
            System.out.println("hasPathTo from 1 to " + v + " :" + pg.hasPathTo(v));
            System.out.println("    pathTo " + pg.pathTo(v));
        }
    }
}
