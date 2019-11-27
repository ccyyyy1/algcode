package com.ex.cy.demo4.alg.graph.tree;

import java.util.ArrayDeque;
import java.util.Queue;

public class BinTree {
    public static class Node {
        int key;
        Node l;
        Node r;

        public Node(int key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return "{" +
                    "key=" + key +
                    ", l=" + l +
                    ", r=" + r +
                    '}';
        }
    }

    Node root;

    //左右反转二叉树
    public void reverse() {
        reverse(root);
    }

    private void reverse(Node n) {
        if (n == null)
            return;
        Node tmp = n.l;
        n.l = n.r;
        n.r = tmp;
        reverse(n.l);
        reverse(n.r);
    }

    public void info() {
        Queue<Node> queue = new ArrayDeque();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node n = queue.poll();
            System.out.print(n.key);
            if (n.l != null)
                queue.add(n.l);
            if (n.r != null)
                queue.add(n.r);
        }
    }

    public static void main(String[] args) {
        BinTree bt = new BinTree();
        bt.root = new Node(1);
        bt.root.l = new Node(2);
        bt.root.r = new Node(3);
        bt.root.l.l = new Node(4);
        bt.root.r.l = new Node(6);
        bt.info();
        System.out.println();

        bt.reverse();
        bt.info();
    }
}
