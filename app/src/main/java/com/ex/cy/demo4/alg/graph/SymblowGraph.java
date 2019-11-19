package com.ex.cy.demo4.alg.graph;

import java.util.HashMap;
import java.util.List;

public class SymblowGraph {
    //字符串 转 下标
    //下标 转 字符串

    List<String> syms;
    HashMap<String, Integer> sym2Index; //字符串树会不会更好？
    Graph g;

    public SymblowGraph(List<String> syms) {
        this.syms = syms;
        sym2Index = new HashMap();
        int i = 0;
        for (String sym : syms) {
            sym2Index.put(sym, i++);
        }
        g = new Graph(syms.size());
    }

    public void addEdge(String a, String b) {
        int u = getIndex(a);
        int v = getIndex(b);
        g.addEdge(u, v);
    }

    public int getIndex(String sym) {
        return sym2Index.get(sym);
    }

    public String getSymblow(int i) {
        return syms.get(i);
    }

    public Graph getGraph() {
        return g;
    }
}
