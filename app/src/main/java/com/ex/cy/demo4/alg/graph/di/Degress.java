package com.ex.cy.demo4.alg.graph.di;

import java.util.ArrayList;
import java.util.List;

//计算一个有向图的 任意顶点 v的 出度，入度
public class Degress {
    int[] inDegress;
    int[] outDegress;
    List<Integer> startPoints = new ArrayList<>();
    List<Integer> endPoints = new ArrayList<>();
    boolean isMap; //是否是映射？ 允许有自环，但每个点的出度都为1   ; o:1->2->3->1  x:1->2->3

    public Degress(Digraph dg) {
        inDegress = new int[dg.v()];
        outDegress = new int[dg.v()];
        isMap = true;
        for (int u = 0; u < dg.v(); u++) { //O(V+E)   space=(2V)
            for (int v : dg.adj(u)) {
                inDegress[v]++;
                outDegress[u]++;
            }
            if (isMap)
                isMap = outDegress[u] == 1;
        }
        for (int u = 0; u < dg.v(); u++) {
            if (outDegress[u] == 0)
                endPoints.add(u);
            if (inDegress[u] == 0)
                startPoints.add(u);
        }
    }

    int inDegree(int v) {
        return inDegress[v];
    }

    int outDegree(int v) {
        return outDegress[v];
    }

    //起点
    Iterable<Integer> sources() {
        return startPoints;
    }

    //终点
    Iterable<Integer> sinks() {
        return endPoints;
    }

    boolean isMap() {
        return isMap;
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(4);
        digraph.addEdge(0, 1);
        digraph.addEdge(1, 2);
        digraph.addEdge(2, 3);
        digraph.addEdge(3, 0);
        Degress degress = new Degress(digraph);
        System.out.println("degress.isMap " + degress.isMap());
        System.out.println("startPoints " + degress.sources());

        Digraph d2 = new Digraph(3);
        d2.addEdge(0, 1);
        d2.addEdge(1, 2);
        Degress de2 = new Degress(d2);
        System.out.println("degress.isMap " + de2.isMap());
        System.out.println("startPoints " + de2.sources());
        System.out.println("startPoints " + de2.sinks());
    }
}
