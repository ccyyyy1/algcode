package com.ex.cy.demo4.alg.graph.di;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//拓扑排序
//无环有向图 是 拓扑排序的前提

//拓扑排序后，顶点所依赖的前驱节点必定都先出现再他前面（这种序列叫做 拓扑序列）
//只要满足上述条件的排序输出，都是拓扑序列（所以一个图往往会有多个拓扑序）（这种排序过程叫做 拓扑排序）
//
//拓扑排序再生活中的应用，比如任务的依赖关系，被依赖的基层任务应该先完成，比如穿几件衣服，一定是先把穿在里面的衣服穿了以后，再穿外面的外套
public class TopologicalKahn {
    Digraph dg;
    List<Integer> order;                //顶点的拓扑顺序
    int[] inDegree;

    public TopologicalKahn(Digraph dg) {
        this.dg = dg;
        //Kahn 算法，获取拓扑排序
        //1.统计所有顶点入度
        //2.将入度为0的作为起点，加入到queue
        //3.从queue中取出顶点，直到队列为空，将该顶点所指向的顶点入度-1，如果入度=0，则加入队列，循环第三步

        //存在环时：输出的顶点数量少于有向图中的顶点数量，或到最后结束循环时，还存在有入度不为0的顶点

        order = new LinkedList<>();

        //1.
        inDegree = new int[dg.v()];
        for (int u = 0; u < dg.v(); u++) {
            for (int w : dg.adj(u)) {
                inDegree[w]++;
            }
        }

        //2.队列中按 成为0入度 的顺序加入顶点
        Queue<Integer> queue = new LinkedList<>();
        for (int u = 0; u < dg.v(); u++)
            if (inDegree[u] == 0)
                queue.add(u);

        //3.
        while (!queue.isEmpty()) {
            int u = queue.remove();
            order.add(u);
            for (int w : dg.adj(u)) {
                inDegree[w]--;
                if (inDegree[w] == 0)   //若入度不为0，说明还有其他指向该点的边
                    queue.add(w);
            }
        }

        //4.若最终输出的入度为0的顶点个数小于 原来有向图中顶点个数，说明存在环
        if(order.size() < dg.v())
            order = null;

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
        books.add("a.c");
        books.add("b.c");
        books.add("c.c");
        books.add("d.c");
        SymblowDigraph sd = new SymblowDigraph(books);
        sd.addEdge("a.c", "b.c");
        sd.addEdge("b.c", "c.c");
        sd.addEdge("d.c", "b.c");


        //编译时 a文件被b文件依赖，b文件被c文件依赖，b文件被d文件依赖
        //那么哪个文件被先编译？ 被依赖的最多的那个文件（a或d）应该被先编译。 如何得到正确的编译顺序？
        // a.c -> b.c -> c.c
        // d.c ->

        Digraph dg = sd.getGraph();
        TopologicalKahn top = new TopologicalKahn(dg);
        if (top.isDAG()) {
            System.out.println("拓扑序列. 文件优先编译顺序（被依赖深度高的先被编译）");
            for (int i : top.getOrder()) {
                System.out.println(" " + sd.getSymblow(i));
            }
        }
    }
}
