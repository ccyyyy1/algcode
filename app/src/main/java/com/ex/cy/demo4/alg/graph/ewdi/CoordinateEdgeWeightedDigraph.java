package com.ex.cy.demo4.alg.graph.ewdi;

import java.util.LinkedList;
import java.util.List;

//带坐标的加权有向图
public class CoordinateEdgeWeightedDigraph {
    int v;  //顶点数量
    int e;  //边的数量
    LinkedList<Edge>[] edges; //[顶点id]=边[]   //边的集合
    Vertex[] vertices;

    public CoordinateEdgeWeightedDigraph(int vCount) {
        this.v = vCount;
        edges = new LinkedList[vCount];
        vertices = new Vertex[vCount];
        for (int i = 0; i < v; i++)
            edges[i] = new LinkedList<>();
    }

    //加入顶点
    //v 顶点下标 （从0开始到 vCount) , x,y 坐标
    public Vertex addVertex(int v, float x, float y) {
        Vertex ve = new Vertex(v, x, y);
        vertices[v] = ve;
        return ve;
    }

    public Vertex getVertex(int v) {
        return vertices[v];
    }

    //加入边
    //从 v 点到 w 点，权重
    public void addEdge(int v, int w, float weight) {
        Edge edge = new Edge(v, w, weight);
        edges[v].add(edge);
    }

    //加入边
    //从 v 点到 w 点，权重默认为基于笛卡尔坐标系中的两点间欧几里得距离
    public void addEdge(int v, int w) {
        float dx = vertices[v].x - vertices[w].x;
        float dy = vertices[v].y - vertices[w].y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        Edge edge = new Edge(v, w, dist);
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
            sb.append(vertices[v]);
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
        CoordinateEdgeWeightedDigraph cewd = new CoordinateEdgeWeightedDigraph(5);
        cewd.addVertex(0, 0, 0);
        cewd.addVertex(1, 1, 0);
        cewd.addVertex(2, 0, 1);
        cewd.addVertex(3, 1, 1);
        cewd.addVertex(4, 5, 5);

        cewd.addEdge(0, 1);
        cewd.addEdge(0, 2);
        cewd.addEdge(1, 3);
        cewd.addEdge(2, 3);
        cewd.addEdge(3, 4);

        System.out.println(cewd);
    }
}
