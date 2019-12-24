package com.ex.cy.demo4.alg.graph.di;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

//拓扑排序
//无环有向图 是 拓扑排序的前提

//拓扑排序后，顶点所依赖的前驱节点必定都先出现再他前面（这种序列叫做 拓扑序列）
//只要满足上述条件的排序输出，都是拓扑序列（所以一个图往往会有多个拓扑序）（这种排序过程叫做 拓扑排序）
//
//拓扑排序再生活中的应用，比如任务的依赖关系，被依赖的基层任务应该先完成，比如穿几件衣服，一定是先把穿在里面的衣服穿了以后，再穿外面的外套
public class TopologicalDFSAndCyc {
    Digraph dg;
    Stack<Integer> reversePostorder;  //顶点的逆后序
    LinkedList<Integer> order;        //顶点的拓扑顺序
    boolean[] inStack;
    boolean[] marked;

    public TopologicalDFSAndCyc(Digraph dg) { //拓扑排序会用到深度优先的逆后序
        this.dg = dg;
        inStack = new boolean[dg.v()];
        marked = new boolean[dg.v()];
        reversePostorder = new Stack<>();    //看是否存在环（dfs调用顺序的堆栈上出现了重复,则说明存在环,那么该引用赋为null)
        //也可以先对 dg 求原图的逆图
        //在逆图的基础上，求深度优先的 后序输出 （post） ； 但这里用堆栈，直接在原图上用深度优先，求逆后序（reversPostOrder），效果一样

        for (int v = 0; v < dg.v(); v++) {
            if (!marked[v])
                dfs(v);
        }

        if (reversePostorder != null) {
            order = new LinkedList<>();
            while (!reversePostorder.isEmpty())
                order.add(reversePostorder.pop()); //将逆后序输出为正向
        }

        this.dg = null;
    }

    private void dfs(int v) {
        inStack[v] = true;
        marked[v] = true;
        for (int u : dg.adj(v)) {
            if (reversePostorder == null)   //已经检测到环存在了，跳出循环
                break;
            if (inStack[u]) {               //dfs调用轨迹上存在相同节点，说明存在环
                reversePostorder = null;
            } else if (!marked[u]) {
                dfs(u);                     //对未访问过的顶点访问
            }
        }
        if (reversePostorder != null)       //还未检测到环，则加入当前顶点到逆后序
            reversePostorder.push(v);
        inStack[v] = false;
    }

    public boolean isDAG() {                 //是有向无环图吗？(没有环存在吗？)
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

        //1.
        Digraph dg = sd.getGraph();
        TopologicalDFSAndCyc top = new TopologicalDFSAndCyc(dg);
        if (top.isDAG()) {
            System.out.println("拓扑序列. 被依赖深度高的靠近顶行");
            for (int i : top.getOrder()) {
                System.out.println(" " + sd.getSymblow(i));
            }
        }

        //2.加入一个环，看输出
        sd.addEdge("c.c", "a.c");
        dg = sd.getGraph();
        top = new TopologicalDFSAndCyc(dg);
        System.out.println("top.isDAG() " + top.isDAG());
        if (top.isDAG()) {
            System.out.println("拓扑序列. 被依赖深度高的靠近顶行");
            for (int i : top.getOrder()) {
                System.out.println(" " + sd.getSymblow(i));
            }
        }
    }
}


