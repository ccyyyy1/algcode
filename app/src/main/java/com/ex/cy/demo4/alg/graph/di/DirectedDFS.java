package com.ex.cy.demo4.alg.graph.di;

//有向图可达性
public class DirectedDFS {
    Digraph dg;
    boolean[] marked;

    public DirectedDFS(Digraph dg, int s) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        dfs(s);
        this.dg = null;
    }

    public DirectedDFS(Digraph dg, Iterable<Integer> s) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        for (int u : s) {
            if (!marked[u]) {
                dfs(u);
            }
        }
    }

    private void dfs(int u) {
        marked[u] = true;
        for (int v : dg.adj(u)) {
            if (!marked[v])
                dfs(v);
        }
    }

    //是否可达 v点
    public boolean isMarked(int v) {
        return marked[v];
    }
}
