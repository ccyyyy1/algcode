package com.ex.cy.demo4.alg.graph.pub;

public class Vertex {
    public int v;    //顶点id
    public Vertex n; //linked list  next  ，链表next节点，表示该顶点可达的下一个顶点，若有多个，表示该顶点可达多个相邻顶点,直到null

    public Vertex(int v) {
        this.v = v;
    }

    public int getV() {
        return v;
    }

//    public Vertex getN() {
//        return n;
//    }
}
