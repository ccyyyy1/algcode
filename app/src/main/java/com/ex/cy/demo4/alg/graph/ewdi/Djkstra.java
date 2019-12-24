package com.ex.cy.demo4.alg.graph.ewdi;

import com.ex.cy.demo4.alg.graph.pub.Edge;
import com.ex.cy.demo4.alg.heap.IndexMinPQ;

import java.util.Stack;

public class Djkstra {

/*
单源最短路径

时间复杂度 O(ElogV) ,主要取决于优先队列的实现
空间复杂度 O(V)

djkstr 和普通的 广度优先非常相似，唯一多考虑了一点：边有不同的权重（不再一直是1了）
基于普通广度优先思想，到达某个顶点的最短距离 = 到达这个顶点要经历的边的个数
djkstr的目的和普通广度优先算法一样，希望对周围能到达的顶点，再最早的时刻对其进行访问（得到访问该顶点的最小成本）
那么问题是，加上边的权重之后，我们该如何将问题化为普通广度优先的思考方式呢？

一个办法是：
我们可以把一条加权的边想象成包含了匿名中间节点的数条边组成
例如
有一张图

a-(4)->b
 \-(3)-^

一共有2条从a到b的边

权重为 3的边 从a点到b点可以看成
a-0-0-b (0为匿名顶点）
同时
a到b点还有另外一条边，权重是4，可以看成这样
a-0-0-0-b

那么完整的图为

a-0-0-0-b
 \-0-0--^

此时按照普通广度优先的思想，从a点出发，能先走到b点的一定是到达b点的最小成本，是哪条边呢？
是权重小的那条边

所以我们在普通广度优先算法的基础上，将一个普通的队列，替换成一个优先队列
每当遇到周围可探索顶点的时候，我们计算一下，从当前顶点的成本 + 到达待探索顶点的边的成本 = 从当前点探索该点的成本
然后将待探索顶点和探索该点的成本放入优先队列（若到达该点的路径有多个，那么我们始终在优先队列中保留成本低的那个）
下次从优先队列中取出的顶点，就是我们该探索的下一个顶点，也就是能最先到达，成本最低的那个顶点

题外：
类似的思想，在加权无向图的最小生成树的Prim即时版中也有使用（优先队列），只不过它不累加探索成本到待探索点上，而是直接
以探索某个点的最短路径的成本作为优先队列的排序条件（这样能确保下一个探索的点，是通过当前已知的成本最低的边走过去的（一种贪心思想））

另外还有一个常用的最短路径算法：贝尔曼福特，

* */

    EdgeWeightedDigraph ewd;
    IndexMinPQ<Float> minPQ;
    float[] cost;         //到达顶点v的成本
    Edge[] fromEdge;      //记录顶点V是从哪条边探索过来的,除了顶点，每个顶点有一条
    int s;

    public Djkstra(EdgeWeightedDigraph ewd, int s) {
        this.ewd = ewd;
        this.s = s;
        minPQ = new IndexMinPQ<>(ewd.v());
        cost = new float[ewd.v()];
        fromEdge = new Edge[ewd.v()];
        for (int i = 0; i < cost.length; i++)
            cost[i] = Integer.MAX_VALUE;
        dj();
        this.ewd = null;
    }

    private void dj() {
        minPQ.insert(s + 1, 0f);
        while (!minPQ.isEmpty()) {
            int v = minPQ.topIndex() - 1;         //当前要探索的顶点
            float vcost = minPQ.delTop();         //到达当前探索顶点的总成本
            cost[v] = vcost;

            for (Edge e : ewd.adj(v)) {            //将周围顶点入队
                int w = e.other(v);                //当前顶点的相邻顶点
                if (cost[w] != Integer.MAX_VALUE)  //已探索过的不再次探索
                    continue;
                if (minPQ.contain(w + 1)) {     //若已经存在于优先队列中，保留探索总成本小的
                    if (minPQ.get(w + 1) > cost[v] + e.weight()) {
                        minPQ.change(w + 1, cost[w] + e.weight());
                        fromEdge[w] = e;
                    }
                } else {
                    minPQ.insert(w + 1, cost[v] + e.weight());
                    fromEdge[w] = e;                //第一次探索到该顶点
                }
            }
        }
    }

    //从起点到v点的总成本
    public float toVCost(int v) {
        return cost[v];
    }

    //从起点，是从哪条边到达v点的
    public Edge toVEdge(int v) {
        return fromEdge[v];
    }

    //点到v点的路径
    public Stack<Edge> toVEdges(int v) {
        Stack<Edge> edges = new Stack<>();
        Edge e = fromEdge[v];
        int toV = v;
        while (e != null) {
            edges.push(e);
            toV = e.other(toV);     //到达当前点的来源点
            e = fromEdge[toV];
        }
        return edges;
    }

    public static void main(String[] args) {
        /*
         *        (2)
         *     1 ---- 3
         * (2)/|\     |
         *   / | \(3) |
         * 0   |  \   |(2)
         *   \ |(1)\  |
         * (1)\|    \ |
         *     2 ---- 4
         *        (4)
         * */

        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(5);
        ewd.addEdge(0, 1, 2);
        ewd.addEdge(0, 2, 1);
        ewd.addEdge(1, 2, 1);
        ewd.addEdge(1, 3, 2);
        ewd.addEdge(1, 4, 3);
        ewd.addEdge(2, 4, 4);
        ewd.addEdge(3, 4, 2);
        System.out.println(ewd);

        int s = 0;
        int w = 4;
        Djkstra dj = new Djkstra(ewd, s);
        System.out.println("from " + s + " to " + w + "'s path: ");
        Stack<Edge> edges = dj.toVEdges(w);
        while (!edges.empty()) {
            Edge e = edges.pop();
            System.out.print(e + " , ");
        }
        System.out.println();
        System.out.println("total cost : " + dj.toVCost(w));
    }
}
