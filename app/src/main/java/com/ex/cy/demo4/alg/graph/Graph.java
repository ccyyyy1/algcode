package com.ex.cy.demo4.alg.graph;

import android.support.annotation.NonNull;

import com.ex.cy.demo4.alg.graph.pub.Vertex;

import java.util.Iterator;

//无向图
public class Graph {

    Vertex[] vs; //邻接表数组
    int e;

    public Graph(int vCount) {
        vs = new Vertex[vCount];
    }

    public void addEdge(int v, int w) {
        Vertex vwvic = new Vertex(w);
        vwvic.n = vs[v];
        vs[v] = vwvic;

        Vertex wvvic = new Vertex(v);
        wvvic.n = vs[w];
        vs[w] = wvvic;

        e++;
    }

    public int e() {
        return e;
    }

    public int v() {
        return vs.length;
    }

    public static class VertexIt implements Iterable {
        VertexItor vi;

        public VertexIt(Vertex h) {
            vi = new VertexItor(h);
        }

        @NonNull
        @Override
        public Iterator iterator() {
            return vi;
        }
    }

    public static class VertexItor implements Iterator {
        Vertex h;

        public VertexItor(Vertex h) {
            this.h = h;
        }

        @Override
        public boolean hasNext() {
            return h != null;
        }

        @Override
        public Object next() {
            Vertex tmp = h;
            h = h.n;
            return tmp.v;
        }

        @Override
        public void remove() {
            throw new RuntimeException("unsuport!");
        }
    }

    public Iterable<Integer> adj(int v) {
        return new VertexIt(vs[v]);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v(); i++) {
            sb.append(i + " : ");
            for (int w : adj(i)) {
                sb.append(w + " ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
