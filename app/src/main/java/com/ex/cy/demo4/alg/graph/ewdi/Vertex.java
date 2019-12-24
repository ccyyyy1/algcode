package com.ex.cy.demo4.alg.graph.ewdi;

public class Vertex {
    public int v;       //顶点id
    public float x, y;  //坐标

    public Vertex(int v) {
        this.v = v;
    }

    public Vertex(int v, float x, float y) {
        this.v = v;
        this.x = x;
        this.y = y;
    }

    public int getV() {
        return v;
    }

    @Override
    public String toString() {
        return "Ver{" +
                "v=" + v +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    //    public Vertex getN() {
//        return n;
//    }
}