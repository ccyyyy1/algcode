package com.ex.cy.demo4.alg.graph.ewg;

import com.ex.cy.demo4.alg.graph.pub.Edge;
import com.ex.cy.demo4.alg.heap.BinHeap2;

import java.util.LinkedList;
import java.util.List;

//Prim算法的 最小生成树
//时间 ElogE                              E为遍历原图中每条边，logE为优先队列(二叉堆)找到最小权重边的平均成本
//空间 V-1条Edge + V个顶点
public class LazyPrimMst implements Mst {
    EdgeWeightedGraph orgEwg;    //原始加权图
    EdgeWeightedGraph singleEwg; //只有最小生成树的加权图
    List<Edge> edges;            //最小生成树的边
    boolean[] marked;            //顶点的访问
    float weightSum = Float.POSITIVE_INFINITY;

    public LazyPrimMst(EdgeWeightedGraph ewg) {
        this.orgEwg = ewg;
        edges = new LinkedList<>();
        marked = new boolean[ewg.v];
        weightSum = 0;

        //只考虑一个连通图的情况
        //改进：排除有多个子图的情况
        //假设 ewg 是连通的

        BinHeap2<Edge> pqedges = new BinHeap2<Edge>();
        //最小生成树的性质：边数 = 图的顶点数量-1
        //1.
//        int v = 0;
//        for (; edges.size() < ewg.v() && v != -1; ) {
//            v = visit(pqedges, v);
//        }

        //2.
        visit2(pqedges, 0);
        while (!pqedges.isEmpty()) {
            Edge e = pqedges.pop();
            int v = e.either();
            int w = e.other(v);
            if (marked[v] && marked[w])                      //已失效的横切边不处理
                continue;
            edges.add(e);                                   //将权重最小的加入到MST中
            if (!marked[v]) visit2(pqedges, v);
            if (!marked[w]) visit2(pqedges, w);
        }

        //将找到的最小生成树转换为一个 EWG对象
        singleEwg = new EdgeWeightedGraph(orgEwg.v());
        for (Edge e : edges) {
            singleEwg.addEdge(e);
        }
        this.orgEwg = null;
    }

    private void visit2(BinHeap2<Edge> pqedges, int v) {
        marked[v] = true;                                   //将访问顶点加入MST中
        for (Edge e : orgEwg.adj(v))
            if (!marked[e.other(v)]) pqedges.add(e);
    }

    //1.将关注顶点 周围的边加入 横切边集合
    //2.找到横切边集合中权重最小的边
    //3.将改边的对面顶点作为下一个关注顶点返回
    private int visit(BinHeap2<Edge> pqedges, int v) {
        marked[v] = true;                                   //将当前关注点加入最小生成树
        for (Edge e : orgEwg.adj(v)) {                      //加入关注顶点的边到优先队列, 横切边集合
            if (!marked[e.other(v)])                        //只加入未失效的横切边
                pqedges.add(e);
        }

        Edge tmpe = null;
        while (tmpe == null && !pqedges.isEmpty()) {
            tmpe = pqedges.pop();
            if (marked[tmpe.either()] && marked[tmpe.other(tmpe.either())])
                tmpe = null;                                //失效的横切边
        }
        if (tmpe == null)                                   //没有足够的边
            return -1;

        edges.add(tmpe);                                    //将最小权重的边加入到最小生成树
        if (!marked[tmpe.either()])                         //从最小权重的边里，找到未探索的对面顶点作为新的关注点
            v = tmpe.either();
        else
            v = tmpe.other(tmpe.either());
        return v;
    }

    @Override
    public Iterable<Edge> edges() {
        return edges;
    }

    @Override
    public float weight() {
        if (weightSum == Float.POSITIVE_INFINITY && edges.size() > 0) {
            weightSum = 0;
            for (Edge e : edges) {
                weightSum += e.weight();
            }
        }
        return weightSum;
    }

    public EdgeWeightedGraph getSingleEWGraph() { //只保留最小生成树的加权有向图
        return singleEwg;
    }

    public static void main(String[] args) {
        // 村口         二狗子家
        // 0--------------1
        // |\            /|
        // | \   你家    / |
        // |  -----2----  |
        // |              |
        // +---------3----+
        //          希望小学

        EdgeWeightedGraph ewg = new EdgeWeightedGraph(4);
        ewg.addEdge(0, 1, 2, "二麻二麻路");
        ewg.addEdge(0, 2, 3, "挨打巷西段");
        ewg.addEdge(0, 3, 4, "挨打巷东段");
        ewg.addEdge(1, 2, 3.5f, "恶犬巷");
        ewg.addEdge(1, 3, 2.5f, "希望之路");
        System.out.println(ewg);
        System.out.println("=======");

        LazyPrimMst lp = new LazyPrimMst(ewg);
        System.out.println("最小生成树权重总和(村里主干道总长度): " + lp.weight());
        for (Edge e : lp.edges()) {
            System.out.println(e.either() + "和" + e.other(e.either()) + "之间的路[" + e.name + "], 路长:" + e.weight());
        }
        System.out.println("\n=======");
        System.out.println(lp.getSingleEWGraph());
    }
}
