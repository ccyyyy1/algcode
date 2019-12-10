package com.ex.cy.demo4.alg.graph.di;

import com.ex.cy.demo4.alg.graph.Graph;
import com.ex.cy.demo4.alg.graph.pub.Vertex;

import java.util.Iterator;

//有向图
//内存：s
// 填充=4，32机器字长
// Digraph : 16+4+                          （对象头 + 填充）
// int e : 4
// Vertex[] vs : 24+8*V+ (16+4+8+4)*E +     （对象头 + 数组长 + 填充 + 对象引用*V个顶点 + E个边*(对象头+int v + 对象引用 + 填充)）
// = 20 + 4 + 24+8V + 32E
// = 48+8V+32E
public class Digraph {

    Vertex[] vs; //邻接表数组
    int e;

    public Digraph(int vCount) {
        vs = new Vertex[vCount];
    }

    public Digraph(Graph g) {
        vs = new Vertex[g.v()];
        for (int u = 0; u < g.v(); u++) {
            for (int v : g.adj(u))
                addEdge(u, v);
        }
    }

    public void addEdge(int v, int w) {
        Vertex vwvic = new Vertex(w);
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
        VertexItor vi;

        public VertexIt(Vertex h) {
            vi = new VertexItor(h);
        }

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
