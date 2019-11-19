package com.ex.cy.demo4.alg.graph;

public class ColorGraphTest { //是否是二分图 （顶点只由2个颜色标记，且相邻的顶点必须不同色)
    boolean[] colors;
    boolean[] marked;
    boolean isBinGraph;
    Graph g;

    public ColorGraphTest(Graph g) {
        this.g = g;
        colors = new boolean[g.v()];
        marked = new boolean[g.v()];
        isBinGraph = true;

        for (int s = 0; s < g.v(); s++) {
            if(!marked[s]) //跑完所有子图
                dfs(s, s);
        }
    }

    public void dfs(int v, int fromV) {
        marked[v] = true;
        colors[v] = !colors[fromV];
        for (int w : g.adj(v)) {
            if (!marked[w]) {
                dfs(w, v);
            } else if (colors[v] == colors[w]) { //存在相邻的2个已访问过的点 的颜色 一致，说明不是二分图
                isBinGraph = false;
                System.out.println("v-w " + v + "-" + w + " colors v-w  " + colors[v] + "-" + colors[w]);
            }
        }
    }

    public boolean isBinGraph() {
        return isBinGraph;
    }

    public static void main(String[] args) {
        Graph g = new Graph(6);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);

        g.addEdge(3, 4);
        System.out.println("Vertex : edgeTo");
        System.out.println(g.toString());

        ColorGraphTest ct = new ColorGraphTest(g);
        System.out.println("isBinGraph: " + ct.isBinGraph());
    }
}
