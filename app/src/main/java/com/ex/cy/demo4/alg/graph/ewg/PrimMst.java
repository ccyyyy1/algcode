package com.ex.cy.demo4.alg.graph.ewg;

import com.ex.cy.demo4.alg.graph.pub.Edge;
import com.ex.cy.demo4.alg.heap.IndexMinPQ;

import java.util.Arrays;
//TODO 闭眼写 红删，动态规划,面试时打动企业主，为主子创造更多利益，成为工具人！我需要996的福报来拯救我。

//即时prim算法 最小生成树
//时间 ElogV
//空间 3V+（V-1）
//允许平行边,自环（不起作用?），负权重
public class PrimMst implements MST {
    EdgeWeightedGraph ewg;
    boolean[] marked;           //记录已经被访问的，加入到最小生成树中的顶点；标记为true说明其edges 和 weights 被纳入最小生成树
    float[] weights;            //从最小生成树到顶点v的最小权重边的权重
    IndexMinPQ<Float> impq;     //小根索引二叉堆，记录最多V-1个，从最小生成树到目标顶点的边的权重；index：目标顶点索引，val：最小权重边的权重
    Edge[] edges;               //mst中的边
    float weightSum;

    public PrimMst(EdgeWeightedGraph ewg) {
        this.ewg = ewg;
        marked = new boolean[ewg.v()];
        weights = new float[ewg.v()];
        edges = new Edge[ewg.v()];          //mst中最多有 v-1 条边,为了方便直接用顶点号做下标索引号直接申请v个元素（0号下标的边不会被更新）
        impq = new IndexMinPQ<>(ewg.v());   //每个顶点对应一条mst到它的边
        Arrays.fill(weights, Float.POSITIVE_INFINITY);

        visit(0, 0);            //从0开始访问
        while (!impq.isEmpty()) {
            visit(impq.topIndex() - 1, impq.delTop());
        }
        this.ewg = null;
    }

    private void visit(int v, float weight) {
        marked[v] = true;
        weights[v] = weight;
        for (Edge e : ewg.adj(v)) {
            int w = e.other(v);
            if (marked[w] || weights[w] < e.weight())   //已经在mst中，则跳过 或 未在mst中，但存在另一条到达该点的权重更小的边 也跳过(contain)
                continue;
            if (impq.contain(w + 1))
                impq.change(w + 1, e.weight());
            else
                impq.insert(w + 1, e.weight());
            edges[w] = e;
            weights[w] = e.weight();
        }
    }

    @Override
    public Iterable<Edge> edges() {
        return Arrays.asList(edges);
    }

    @Override
    public float weight() {
        if (weightSum == Float.POSITIVE_INFINITY)
            for (float f : weights)
                weightSum += f;
        return weightSum;
    }

    public static void main(String[] args) {
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
