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
        if (!dc.hasCycle()) {               //若存在环，则不计算拓扑排序
            dfo = new DFOrder(dg);
            order = dfo.reversePost();      //拓扑排序会用到深度优先
        }
        this.dg = null;
    }

    public boolean isDAG() {                //是有向无环图吗？
        return null != order;
    }

    public Iterable<Integer> getOrder() {
        return order;
    }

    public static void main(String[] args) {
        List<String> books = new LinkedList<>();
        books.add("小鱼");
        books.add("泥巴");
        books.add("赵家六");
        books.add("虾子");
        books.add("大鱼");
        books.add("牧羊犬");
        books.add("饲料");
        books.add("廉价劳动力");
        SymblowDigraph sd = new SymblowDigraph(books);
        sd.addEdge("泥巴", "虾子");
        sd.addEdge("虾子", "小鱼");
        sd.addEdge("饲料", "小鱼");
        sd.addEdge("小鱼", "大鱼");
        sd.addEdge("小鱼", "廉价劳动力");
        sd.addEdge("小鱼", "牧羊犬");
        sd.addEdge("大鱼", "牧羊犬");
        sd.addEdge("廉价劳动力", "牧羊犬");
        sd.addEdge("大鱼", "赵家六");
        sd.addEdge("牧羊犬", "赵家六");
        sd.addEdge("廉价劳动力", "赵家六");
        //泥巴被虾子吃，虾子被小鱼吃，饲料被小鱼吃，小鱼被大鱼吃，小鱼被牧羊犬吃，小鱼被廉价劳动力吃，大鱼被牧羊犬吃，大鱼被赵家六吃，廉价劳动力被牧羊犬吃，牧羊犬被赵家六吃，廉价劳动力被赵家六吃
        //那么整个食物链的低端（被依赖）到顶端的关系是？

        Digraph dg = sd.getGraph();
        Topological top = new Topological(dg);
        if (top.isDAG()) {
            System.out.println("拓扑序列. 被依赖的靠近顶行(并行被依赖的话，前后顺序不重要)");
            for (int i : top.getOrder()) {
                System.out.println(" " + sd.getSymblow(i));
            }
        }
    }
}
