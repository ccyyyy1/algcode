package com.ex.cy.demo4.alg.graph.ewdi;

public class ASVertex {
    public int v;       //顶点id
    public float g;     //从起点到该点的实际距离(与Djkstra中的距离含义一样，加权边的加权总和）
    public float h;     //从该点到终点的[估计]距离 //曼哈顿距离

    public ASVertex(int v) {
        this.v = v;
        h = g = Float.POSITIVE_INFINITY;
    }

    public ASVertex(int v, float g, float h) {
        this.v = v;
        this.g = g;
        this.h = h;
    }

    public int getV() {
        return v;
    }

    @Override
    public String toString() {
        return "Ver{" +
                "v=" + v +
                ", g=" + g +
                ", h=" + h +
                '}';
    }

    //    public Vertex getN() {
//        return n;
//    }
}