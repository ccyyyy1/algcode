package com.ex.cy.demo4.alg.graph.ewdi;

import com.ex.cy.demo4.alg.graph.pub.Edge;

import java.util.LinkedList;
import java.util.List;

//加权有向图
public class EdgeWeightedDigraph {
    int v;  //顶点数量
    int e;  //边的数量
    LinkedList<Edge>[] edges; //[顶点id]=边[]   //边的集合

    public EdgeWeightedDigraph(int vCount) {
        this.v = vCount;
        edges = new LinkedList[vCount];
        for (int i = 0; i < v; i++)
            edges[i] = new LinkedList<>();
    }

    public void addEdge(int v, int w, float weight) {
        Edge edge = new Edge(v, w, weight);
        edges[v].add(edge);
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

    public static void main(String[] a) {
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(5);

        ewd.addEdge(0, 1, 1);
        ewd.addEdge(0, 2, 1);
        ewd.addEdge(1, 3, 1);
        ewd.addEdge(2, 3, 1);
        ewd.addEdge(3, 4, 4);

        System.out.println(ewd);
    }
}
