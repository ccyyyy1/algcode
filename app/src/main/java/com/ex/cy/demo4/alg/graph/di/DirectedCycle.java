package com.ex.cy.demo4.alg.graph.di;

import java.util.Arrays;
import java.util.Stack;

//是否存再环，这个环路径是
public class DirectedCycle {
    Digraph dg;
    boolean[] marked;
    int[] fromVertex;
    boolean[] onStack;
    Stack<Integer> cycleStack;
    int s;


    public DirectedCycle(Digraph dg) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        onStack = new boolean[dg.v()];
        fromVertex = new int[dg.v()];
        Arrays.fill(fromVertex, -1);
        for (int v = 0; v < dg.v(); v++)
            if (!marked[v])
                dfs(v);
        this.dg = null;
    }

    /**
     * @param dg
     * @param s
     * @deprecated use DirectedCycle(Digraph dg) version
     */
    public DirectedCycle(Digraph dg, int s) {
        this.s = s;
        this.dg = dg;
        marked = new boolean[dg.v()];
        onStack = new boolean[dg.v()];
        fromVertex = new int[dg.v()];
        Arrays.fill(fromVertex, -1);
        dfs(s);
    }

    private void dfs(int u) {   //自环？
        marked[u] = true;
        onStack[u] = true;
        for (int v : dg.adj(u)) {
            if (hasCycle())
                break;
            if (!marked[v]) {
                fromVertex[v] = u;
                dfs(v);
            } else if (onStack[v]) {
                cycleStack = new Stack<>();
                cycleStack.push(u);         //自己
//                if(v != u)  //单点自环 不 加入前驱节点
                for (int p = fromVertex[u]; p != v && p != -1; p = fromVertex[p])
                    cycleStack.push(p);     //自己的前驱
                cycleStack.push(v);         //所碰到的环头
            }
        }
        onStack[u] = false;
    }

    public boolean hasCycle() {
        return null != cycleStack;
    }

    public Iterable<Integer> getCycle() {
//        List<Integer> list = new LinkedList<>();
//        while (cycleStack.size() > 0)
//            list.add(cycleStack.pop());
//        return list;
        return cycleStack;
    }

    public static void main(String[] args) {
        Digraph dg = new Digraph(7);
        dg.addEdge(0, 1);
        dg.addEdge(1, 2);
        dg.addEdge(2, 3);
        dg.addEdge(3, 4);
        dg.addEdge(4, 1);

        DirectedCycle dc = new DirectedCycle(dg, 0);
        System.out.println("dc.hasCycle() " + dc.hasCycle());
        System.out.println("cycle: " + dc.getCycle());

        //第2个子图里的 6->6 自环.
        dg.addEdge(5, 6);
        dg.addEdge(6, 6);
        DirectedCycle dc2 = new DirectedCycle(dg, 5);
        System.out.println("dc.hasCycle() " + dc2.hasCycle());
        System.out.println("cycle: " + dc2.getCycle());

        DirectedCycle dc3 = new DirectedCycle(dg);
        System.out.println("dc.hasCycle() " + dc3.hasCycle());
        System.out.println("cycle: " + dc3.getCycle());
    }
}
