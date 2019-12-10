package com.ex.cy.demo4.alg.graph.di;

import java.util.LinkedList;
import java.util.List;

//有向图可达性
public class DirectedDFS {
    Digraph dg;
    boolean[] marked;

    public DirectedDFS(Digraph dg, int s) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        dfs(s);
        this.dg = null;
    }

    public DirectedDFS(Digraph dg, Iterable<Integer> s) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        for (int u : s) {
            if (!marked[u]) {
                dfs(u);
            }
        }
    }

    private void dfs(int u) {
        marked[u] = true;
        for (int v : dg.adj(u)) {
            if (!marked[v])
                dfs(v);
        }
    }

    //是否可达 v点
    public boolean isMarked(int v) {
        return marked[v];
    }

    public static void main(String[] a) {
        //模仿JAVA 的垃圾回收机制
        //通过以GCRoot为起点(0)，探测可达的点（这些对象不被回收），而不可达的点被回收（说明不存在从GCRoot指向他们的引用)
        Digraph digraph = new Digraph(5);
        digraph.addEdge(0, 1);          //0:是GCroot,1:是activity
        digraph.addEdge(1, 2);          //2:是handle
        digraph.addEdge(3, 2);          //3:runnable对象 持有2:handler
//        digraph.addEdge(2,3);                //持有handler 接触持有的 runnable对象引用,  runnable已经不被handle持有
        digraph.addEdge(3, 4);          //3:runnable 对象持有一个非static的内部成员变量 4:Object

        DirectedDFS directedDFS = new DirectedDFS(digraph, 0);
        List<Integer> needGcObj = new LinkedList<>();
        for (int v = 0; v < digraph.v(); v++)
            if (!directedDFS.isMarked(v))
                needGcObj.add(v);

        System.out.println("需要被回收的对象有 :" + needGcObj);
    }
}
