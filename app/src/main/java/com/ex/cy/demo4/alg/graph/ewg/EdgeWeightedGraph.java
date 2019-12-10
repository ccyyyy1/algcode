package com.ex.cy.demo4.alg.graph.ewg;

import com.ex.cy.demo4.alg.graph.pub.Edge;

import java.util.LinkedList;
import java.util.List;

//加权无向图
public class EdgeWeightedGraph {
    LinkedList<Edge>[] edges;               //边的集合
    final int v;                            //顶点数量
    int e;                                  //边的数量

    public EdgeWeightedGraph(int v) {
        this.v = v;
        this.e = 0;
        edges = new LinkedList[v];
        for (int i = 0; i < v; i++)
            edges[i] = new LinkedList<>();
    }

    public void addEdge(int v, int w, float weight) {
        addEdge(new Edge(v, w, weight));
    }
    public void addEdge(int v, int w, float weight, String name) {
        addEdge(new Edge(v, w, weight, name));
    }

    public void addEdge(Edge e) {           //添加一条边，在无向图中等于向2边顶点添加边(互相连通)
        edges[e.v].add(e);
        edges[e.w].add(e);
        this.e++;
    }

    public Iterable<Edge> adj(int v) {
        return edges[v];
    }

    //返回所有边的集合
    public Iterable<Edge> edges() {
        List<Edge> es = new LinkedList<>();
        for (int v = 0; v < edges.length; v++) {
            for (Edge e : adj(v)) {
                if (e.other(v) > v)           //加入顺序： 顶点序号逆序
                    es.add(e);
            }
        }
        return es;
    }

    public int v() {//顶点数
        return v;
    }

    public int e() {//边数
        return e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int v = 0; v < v(); v++) {
            sb.append(v);
            sb.append(": ");
            for (Edge e : adj(v)) {
                sb.append(e.toString());
                sb.append(", ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(4);
        ewg.addEdge(0, 1, 1);
        ewg.addEdge(0, 2, 2);
        ewg.addEdge(0, 3, 3);
        ewg.addEdge(1, 2, 3);
        System.out.println(ewg);
    }
}
