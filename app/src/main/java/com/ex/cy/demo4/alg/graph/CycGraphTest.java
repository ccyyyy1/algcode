package com.ex.cy.demo4.alg.graph;

public class CycGraphTest { //无向图， 环检测 ，假设不存在自环或平行边
    Graph g;
    boolean[] marked; //是否已访问过
    boolean hasCyc;
//    int[] fromVertex;

    public CycGraphTest(Graph g) {
        this.g = g;
        marked = new boolean[g.v()];
//        fromVertex = new int[g.v()];
//        Arrays.fill(fromVertex, -1);
        hasCyc = false;

        for (int s = 0; s < g.v(); s++) {
            if (!marked[s]) {
                dfs(s, s);
            }
        }
    }

    private void dfs(int v, int fromV) {
        marked[v] = true;
//        fromVertex[v] = fromV;
        for (Integer w : g.adj(v)) {
            if (!marked[w]) {
                dfs(w, v);
            } else if (fromV != w) { //不是来的点(无向图)，  // && fromVertex[w] != v  访问者也不是我(说明被从其他路径访问过)
                hasCyc = true;
            }
        }
    }

    public boolean isHasCyc() {
        return hasCyc;
    }

    public static void main(String[] args) {
        Graph g = new Graph(6);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(3, 4);
        System.out.println("Vertex : edgeTo");
        System.out.println(g.toString());

        CycGraphTest cc = new CycGraphTest(g);
        System.out.println("isHasCyc: " + cc.isHasCyc());
    }
}
