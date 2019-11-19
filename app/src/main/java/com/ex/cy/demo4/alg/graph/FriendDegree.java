package com.ex.cy.demo4.alg.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FriendDegree {
    Graph g;
    int s;
    int[] degrees;
    boolean[] marked;
    int maxDegree;
    LinkedList<Integer> maxDegrees = new LinkedList<>();

    public FriendDegree(Graph g, int s) {
        this.s = s;
        this.g = g;
        degrees = new int[g.v()];
        marked = new boolean[g.v()];
        bfs(s);
    }

    public void bfs(int s) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        marked[s] = true;
        int u;
        while (queue.size() > 0) {
            u = queue.remove();
            for (int v : g.adj(u)) {
                if (marked[v])
                    continue;
                queue.add(v);
                marked[u] = true;
                degrees[v] = degrees[u] + 1;

                //额外
                if (degrees[v] > maxDegree) {
                    maxDegree = degrees[v];
                    maxDegrees.clear();
                    maxDegrees.add(v);
                } else if (degrees[v] == maxDegree) {
                    maxDegrees.add(v);
                }
            }
        }
    }

    public int degressTo(int v) {
        return degrees[v];
    }

    public List<Integer> degreeEq(int deg) {
        List<Integer> res = new LinkedList<Integer>();
        for (int i = 0; i < degrees.length; i++) {
            if (degrees[i] == deg)
                res.add(i);
        }
        return res;
    }

    public int getMaxDegree() {
        return maxDegree;
    }

    public LinkedList<Integer> getMaxDegrees() {
        return maxDegrees;
    }

    public static void main(String[] args) {
        String[] names = new String[]{"张三", "李四", "王五", "赵家人", "钱七", "孙八"};
        ArrayList<String> nl = new ArrayList<String>();
        for (String s : names) {
            nl.add(s);
        }
        SymblowGraph sg = new SymblowGraph(nl);
        sg.addEdge("张三", "李四");
        sg.addEdge("张三", "王五");
        sg.addEdge("王五", "钱七");
        sg.addEdge("孙八", "赵家人");
        sg.addEdge("钱七", "赵家人");//藏在深处的赵家人

        Graph g = sg.getGraph();
        String sname = "张三";
        String toname = "赵家人";

        System.out.println("------------------------");
        System.out.println("|六度关系之藏在深处的赵家人|");
        System.out.println("------------------------");

        BFSPathTest zhang2zhaoP = new BFSPathTest(g, sg.getIndex(sname));
        System.out.println("" + sname + " 能认识 " + toname + "吗？ :" + zhang2zhaoP.hasPathTo(sg.getIndex(toname)));

        FriendDegree zhang2zhao = new FriendDegree(g, sg.getIndex(sname));
        int deg = zhang2zhao.degressTo(sg.getIndex(toname));
        System.out.println("隔着多少人？ :" + sname + " -> " + toname + " = " + deg);

        System.out.println("按顺序他们是(" + sname + ") :");
        List<Integer> plist = zhang2zhaoP.pathTo(sg.getIndex(toname));
        for (int i : plist) {
            System.out.print(sg.getSymblow(i) + " -> ");
        }

        FriendDegree zhaoFD = new FriendDegree(g, sg.getIndex(toname));
        System.out.println("\n距离 " + toname + " 最近的人有那些？");
        List<Integer> d1list = zhaoFD.degreeEq(1);
        for (int i : d1list) {
            System.out.print(sg.getSymblow(i) + ", ");
        }

        System.out.println("\n和 " + toname + " 关系最远的有几度？ " + zhaoFD.getMaxDegree());
        System.out.println("他们是 : ");
        List<Integer> mdlist = zhaoFD.getMaxDegrees();
        for (int i : mdlist) {
            System.out.print(sg.getSymblow(i) + "");
            System.out.println("\n\t" + sg.getSymblow(i) + " 如何认识赵家人? ");
            BFSPathTest far2zhao = new BFSPathTest(g, i);
            List<Integer> p2z = far2zhao.pathTo(sg.getIndex(toname));
            for(int j : p2z){
                System.out.print(sg.getSymblow(j) + " -> ");
            }
        }
    }
}
