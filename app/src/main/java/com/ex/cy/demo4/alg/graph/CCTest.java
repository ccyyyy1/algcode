package com.ex.cy.demo4.alg.graph;

public class CCTest { //连通图, 连通子图判断
    Graph g;
    boolean[] marked; //是否已访问过
    int[] id; //每个顶点对应的连通图id
    int count; //连通图个数

    public CCTest(Graph g) {
        this.g = g;
        marked = new boolean[g.v()];
        id = new int[g.v()];
        count = 0;

        for (int s = 0; s < g.v(); s++) {
            if (!marked[s]) {
                dfs(s);
                count++;
            }
        }
    }

    private void dfs(int v) {
        marked[v] = true;
        id[v] = count;
        for (Integer w : g.adj(v)) {
            if (!marked[w]) {
                dfs(w);
            }
        }
    }

    public int unionId(int v) {
        return id[v];
    }

    public int count() {
        return count;
    }

    public static void main(String[] args) {
        Graph g = new Graph(5);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        System.out.println("Vertex : edgeTo");
        System.out.println(g.toString());

        CCTest cc = new CCTest(g);
        System.out.println("unionCount : " + cc.count());
        System.out.println("Vertex : unionId");
        for (int v = 0; v < g.v(); v++)
            System.out.println(v + " : " + cc.unionId(v));
    }
}
