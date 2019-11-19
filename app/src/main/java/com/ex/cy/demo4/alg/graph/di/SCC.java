package com.ex.cy.demo4.alg.graph.di;

//strong CC 强连通子图
//找到一个有向图中的强连通子图
//什么是强连通子图？
//1.什么是连通图？ 无向图中，随便从一个节点开始，可以到达的任何一个节点，这些节点所在的同一个图，就是一个连通图（一串珠子手镯，拿起一个珠子，其他相联的都被拎起，没有被拎起的不算在同一个连通图内）
//2.什么是强连通？ 有向图中，一个顶点能到达另外的顶点，而被到达的顶点也能到达起始的顶点，这些顶点之间是强连通的，比如一个环。
//3.什么是强连通子图？ 有向图中，按强连通性判定一些节点之间可以互相到达，这些节点之间形成一个连通子图。比如权贵阶级圈子，圈子里的任何顶点可以互相到达，可以控制中产阶级圈子，而中产阶级圈子可以控制无产阶级圈子，每个圈子都是一个强连通子图；从反向看，无产阶级圈子里的任意顶点无法走向中产阶级圈子，那么无产阶级的所有顶点之间没有任何一条路径可以控制中产阶级，此时无产阶级顶点之间就是一个强连通子图

import java.util.LinkedList;
import java.util.List;

public class SCC {
    Digraph dg;
    boolean[] marked;
    int[] id;
    int subccCount;

    public SCC(Digraph dg) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        id = new int[dg.v()];

        DFOrder dfOrder = new DFOrder(dg.reverse());
        Iterable<Integer> rpIt = dfOrder.reversePost();
        for (int s : rpIt) {
            System.out.println("rpit " + s);
            if (!marked[s]) {
                dfs(s);
                subccCount++;
            }
        }
        this.dg = null;
    }

    private void dfs(int u) {
        marked[u] = true;
        id[u] = subccCount;
        for (int v : dg.adj(u))
            if (!marked[v])
                dfs(v);
    }

    //这TM，不看别人写的，打死我也想不到,怎么回事？
//    private int dfs(int u, int w ,int cc) {
//        onStack[u] = true;
//        marked[u] = true;
//        subCC[u] = cc;
//        int resCC = cc;
//        for (int v : dg.adj(u)) {
//            if (!marked[v]) {
//                int tmpRescc = dfs(v, u,resCC + 1);
//                resCC = tmpRescc > resCC ? tmpRescc : resCC; // max?
//            } else if (onStack[v] || (subCC[w] > 0 && (subCC[v] == subCC[w]))) {
//                resCC = Math.min(subCC[u], subCC[v]);
//                subCC[u] = resCC;
//            }
//        }
//        onStack[u] = false;
//        return resCC;
//    }

    public int getSubccCount() {
        return subccCount;
    }

    public int id(int v) {
        return id[v];
    }

    public boolean stronglyConnected(int u, int v) {
        return id[u] == id[v];
    }

    public static void main(String[] args) {
        Digraph digraph = new Digraph(3);
        digraph.addEdge(0, 1);
        digraph.addEdge(1, 0);
        digraph.addEdge(1, 2);
        SCC scc = new SCC(digraph);
        for (int i = 0; i < digraph.v(); i++) {
            System.out.println(i + ": " + scc.id[i]);
        }

        checkTheWaterMeter();
    }

    public static void checkTheWaterMeter() {
        List<String> peop = new LinkedList<>();
        peop.add("维尼");
        peop.add("赵家家丁");
        peop.add("赵家真理部");
        peop.add("赵家传话筒");

        peop.add("血汗工厂老板");
        peop.add("学校校长");
        peop.add("规划院");

        peop.add("人力资源");
        peop.add("屁民");
        peop.add("野猴子");

        SymblowDigraph sd;
        sd = new SymblowDigraph(peop);
        sd.addEdge("维尼", "赵家家丁");
        sd.addEdge("维尼", "赵家真理部");
        sd.addEdge("赵家真理部", "赵家家丁");
        sd.addEdge("赵家家丁", "赵家传话筒");
        sd.addEdge("赵家真理部", "赵家传话筒");
        sd.addEdge("赵家传话筒", "赵家真理部");

        sd.addEdge("赵家真理部", "学校校长");
        sd.addEdge("赵家传话筒", "血汗工厂老板");
        sd.addEdge("赵家传话筒", "规划院");

        sd.addEdge("血汗工厂老板", "人力资源");
        sd.addEdge("人力资源", "野猴子");
        sd.addEdge("人力资源", "屁民");
        sd.addEdge("屁民", "人力资源");
        sd.addEdge("屁民", "野猴子");
        sd.addEdge("野猴子", "屁民");

        Digraph dg = sd.getGraph();
        SCC scc = new SCC(dg);
        System.out.println("scc.getSubccCount(); " + scc.getSubccCount());

        for (int i = 0; i < dg.v(); i++) {
            System.out.println(i + " ("+sd.getSymblow(i)+") " + ": " + scc.id[i]);
        }
    }
}
