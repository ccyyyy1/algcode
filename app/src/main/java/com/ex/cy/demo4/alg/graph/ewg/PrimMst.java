package com.ex.cy.demo4.alg.graph.ewg;

import com.ex.cy.demo4.alg.graph.pub.Edge;
import com.ex.cy.demo4.alg.heap.IndexMinPQ;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
//TODO 闭眼写 红删，动态规划,面试时打动企业主，为主子创造更多利益，成为工具人！我需要996的福报来拯救我。

//即时prim算法 可对加权无向图生成最小生成树
//时间 ElogV
//空间 3V+（V-1）
//允许平行边,自环（不起作用?），负权重
public class PrimMst implements Mst {
    EdgeWeightedGraph ewg;
    boolean[] marked;                       //记录已经被访问的，加入到最小生成树中的顶点；标记为true说明其edges 和 weights 被纳入最小生成树
    float[] weights;                        //从最小生成树到顶点v的最小权重边的权重值
    IndexMinPQ<Float> impq;                 //小根索引二叉堆，记录最多V-1个，从最小生成树到目标顶点的边的权重；index：目标顶点索引，val：最小权重边的权重
    Edge[] edges;                           //属于最小生成树中的边的集合，有V-1条
    float weightSum = Float.POSITIVE_INFINITY;//最小生成树的边的权重总和

    public PrimMst(EdgeWeightedGraph ewg) {
        this.ewg = ewg;
        marked = new boolean[ewg.v()];
        weights = new float[ewg.v()];
        edges = new Edge[ewg.v()];           //最小生成树中最多有 v-1 条边,为了方便直接用顶点号做下标索引号直接申请v个元素（0号下标的边不会被更新）
        impq = new IndexMinPQ<>(ewg.v());    //每个顶点对应一条最小生成树到它的边
        Arrays.fill(weights, Float.POSITIVE_INFINITY);

        visit(0, 0);              //从0开始访问
        while (!impq.isEmpty()) {            //从优先队列中取出权重最小的边，以及从最小生成树通过该边所到达的顶点号
            visit(impq.topIndex() - 1, impq.delTop());
        }
        this.ewg = null;
    }

    //对顶点v进行访问，并给出从最小生成树到达该顶点的边的最小权重
    private void visit(int v, float weight) {
        marked[v] = true;
        weights[v] = weight;
        for (Edge e : ewg.adj(v)) {
            int w = e.other(v);
            if (marked[w] || weights[w] < e.weight())   //已经在mst中，则跳过 或 未在mst中，但存在另一条到达该点的权重更小的边 也跳过(contain)
                continue;
            if (impq.contain(w + 1))                 //优先队列中存在到达该点的边，将权重更新为更小的
                impq.change(w + 1, e.weight());
            else
                impq.insert(w + 1, e.weight());      //首次遇到该顶点，将到达该顶点的边的权重和顶点号加入优先队列
            edges[w] = e;
            weights[w] = e.weight();
        }
    }

    @Override
    public Iterable<Edge> edges() {
        List<Edge> list = new LinkedList<>();
        for (int v = 1; v < edges.length; v++) {
            list.add(edges[v]);
        }
        return list;
    }

    @Override
    public float weight() {
        if (weightSum == Float.POSITIVE_INFINITY && edges.length > 0) {
            weightSum = 0;
            for (float f : weights) {
                weightSum += f;
            }
        }
        return weightSum;
    }

    public static void main(String[] args) {
//        demo1();
        demo2();
    }

    private static void demo2() {
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

        PrimMst lp = new PrimMst(ewg);
        System.out.println("最小生成树权重总和(村里主干道总长度): " + lp.weight());
        for (Edge e : lp.edges()) {
            System.out.println(e.either() + "和" + e.other(e.either()) + "之间的路[" + e.name + "], 路长:" + e.weight());
        }
        System.out.println("\n=======");
    }

    private static void demo1() {
        EdgeWeightedGraph ewg = new EdgeWeightedGraph(4);
        ewg.addEdge(0, 1, 991);
        ewg.addEdge(0, 2, 991);
        ewg.addEdge(0, 3, 991);
        ewg.addEdge(1, 2, 130);
        ewg.addEdge(1, 3, 130);
        ewg.addEdge(2, 3, 1);
        PrimMst pmst = new PrimMst(ewg);
        System.out.println(ewg.edges());
        System.out.println(pmst.edges());

        EdgeWeightedGraph ewg2 = new EdgeWeightedGraph(4);
        ewg2.addEdge(0, 1, 991);
        ewg2.addEdge(1, 1, -10);
        ewg2.addEdge(1, 2, -1);
        ewg2.addEdge(2, 3, 991);
        ewg2.addEdge(2, 3, 1);
        ewg2.addEdge(0, 3, -2);
        PrimMst pmst2 = new PrimMst(ewg2);
        System.out.println(ewg2.edges());
        System.out.println(pmst2.edges());
    }
}
