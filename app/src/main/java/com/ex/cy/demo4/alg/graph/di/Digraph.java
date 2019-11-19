package com.ex.cy.demo4.alg.graph.di;

import android.support.annotation.NonNull;

import com.ex.cy.demo4.alg.graph.Graph;

import java.util.Iterator;

//有向图
//内存：
// 填充=4，32机器字长
// Digraph : 16+4+                                  （对象头 + 填充）
// int e : 4
// Digraph.Vertex[] vs : 24+8*V+ (16+4+8+4)*E +     （对象头 + 数组长 + 填充 + 对象引用*V个顶点 + E个边*(对象头+int v + 对象引用 + 填充)）
// = 20 + 4 + 24+8V + 32E
// = 48+8V+32E
public class Digraph {
    public static class Vertex {
        public int v;
        public Digraph.Vertex n;

        public Vertex(int v) {
            this.v = v;
        }
    }

    Digraph.Vertex[] vs; //邻接表数组
    int e;

    public Digraph(int vCount) {
        vs = new Digraph.Vertex[vCount];
    }

    public Digraph(Graph g) {
        vs = new Digraph.Vertex[g.v()];
        for (int u = 0; u < g.v(); u++) {
            for (int v : g.adj(u))
                addEdge(u, v);
        }
    }

    public void addEdge(int v, int w) {
        Digraph.Vertex vwvic = new Digraph.Vertex(w);
        vwvic.n = vs[v];
        vs[v] = vwvic;
        e++;
    }

    public boolean hasEdge(int u, int v) {
        for (int w : adj(u)) {
            if (w == v)
                return true;
        }
        return false;
    }

    public int e() {
        return e;
    }

    public int v() {
        return vs.length;
    }

    //反向有向图
    public Digraph reverse() {
        Digraph rd = new Digraph(v());
        for (int u = 0; u < v(); u++) {
            for (int v : adj(u))
                rd.addEdge(v, u);
        }
        return rd;
    }

    public static class VertexIt implements Iterable {
        Digraph.VertexItor vi;

        public VertexIt(Digraph.Vertex h) {
            vi = new Digraph.VertexItor(h);
        }

        @NonNull
        @Override
        public Iterator iterator() {
            return vi;
        }
    }

    public static class VertexItor implements Iterator {
        Digraph.Vertex h;

        public VertexItor(Digraph.Vertex h) {
            this.h = h;
        }

        @Override
        public boolean hasNext() {
            return h != null;
        }

        @Override
        public Object next() {
            Digraph.Vertex tmp = h;
            h = h.n;
            return tmp.v;
        }

        @Override
        public void remove() {
            throw new RuntimeException("unsuport!");
        }
    }

    public Iterable<Integer> adj(int v) {
        return new Digraph.VertexIt(vs[v]);
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
