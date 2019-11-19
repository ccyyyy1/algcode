package com.ex.cy.demo4.alg.graph.di;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

//基于深度优先的顶点排序
public class DFOrder {
    Digraph dg;
    boolean[] marked;
    Queue<Integer> pre;             //前序
    Queue<Integer> post;            //后序
    Stack<Integer> reversePost;     //逆后序

    public DFOrder(Digraph dg) {
        this.dg = dg;
        marked = new boolean[dg.v()];
        pre = new ArrayDeque<>();
        post = new ArrayDeque<>();
        reversePost = new Stack<>();
        for (int u = 0; u < dg.v(); u++) {
            if (!marked[u]) {
                dfs(u);
            }
        }
        this.dg = null;
    }

    private void dfs(int u) {
        pre.add(u);
        marked[u] = true;
        for (int v : dg.adj(u)) {
            if (!marked[v])
                dfs(v);
        }
        post.add(u);
        reversePost.add(u);
    }

    public Iterable<Integer> pre() {
        return pre;
    }

    public Iterable<Integer> post() {
        return post;
    }

    public Iterable<Integer> reversePost() {
        List<Integer> rpList = new ArrayList<>();
        while(reversePost.size() > 0){
            rpList.add(reversePost.pop());
        }
        return rpList;
    }

    public static void main(String[] args) {
    }
}
