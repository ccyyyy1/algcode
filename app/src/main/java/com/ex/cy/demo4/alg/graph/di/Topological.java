package com.ex.cy.demo4.alg.graph.di;

import java.util.LinkedList;
import java.util.List;

//拓扑排序
//无环有向图 是 拓扑排序的前提

//拓扑排序后，顶点所依赖的前驱节点必定都先出现再他前面（这种序列叫做 拓扑序列）
//只要满足上述条件的排序输出，都是拓扑序列（所以一个图往往会有多个拓扑序）（这种排序过程叫做 拓扑排序）
//
//拓扑排序再生活中的应用，比如任务的依赖关系，被依赖的基层任务应该先完成，比如穿几件衣服，一定是先把穿在里面的衣服穿了以后，再穿外面的外套
public class Topological {
    Digraph dg;
    DirectedCycle dc;
    DFOrder dfo;
    Iterable<Integer> order;                //顶点的拓扑顺序

    public Topological(Digraph dg) {
        this.dg = dg;
        dc = new DirectedCycle(dg);
        if (!dc.hasCycle()) {
            dfo = new DFOrder(dg);
            order = dfo.reversePost();      //拓扑排序会用到深度优先
        }
        this.dg = null;
    }

    public boolean isDAG() {                //是不是
        return null != order;
    }

    public Iterable<Integer> getOrder() {
        return order;
    }

    public static void main(String[] args) {
        List<String> books = new LinkedList<>();
        books.add("出生");
        books.add("语文");
        books.add("数学");
        books.add("手工");
        books.add("音乐");
        books.add("美术");
        books.add("体育");
        books.add("考试");
        books.add("被资本家剥削");
        books.add("资本家被政治家剥削");
        books.add("你孩子被你生出来垫背");
        SymblowDigraph sd = new SymblowDigraph(books);
        sd.addEdge("出生", "语文");
        sd.addEdge("语文", "数学");
        sd.addEdge("语文", "手工");
        sd.addEdge("手工", "音乐");
        sd.addEdge("手工", "美术");
        sd.addEdge("体育", "考试");
        sd.addEdge("数学", "考试");
        sd.addEdge("考试", "被资本家剥削");
        sd.addEdge("被资本家剥削", "资本家被政治家剥削");
        sd.addEdge("资本家被政治家剥削", "你孩子被你生出来垫背");

        Digraph dg = sd.getGraph();
        Topological top = new Topological(dg);
        if (top.isDAG()) {
            System.out.println("拓扑序列，优先级高的在低行.");
            for (int i : top.getOrder()) {
                System.out.println(" " + sd.getSymblow(i));
            }
        }
    }
}
