package com.ex.cy.demo4.alg.tree;

public class ArrayBinTree {
    //层序遍历
    public static class Node {
        Node l;
        Node r;
        int key;

        @Override
        public String toString() {
            return "" + key;
        }
    }

    Node[] ar = new Node[16];
    int count = 0;

    public void add(int k) {
        Node node = new Node();
        node.key = k;
        ar[count + 1] = node;
        count++;
    }

    public void levelOrder() {
        for (int i = 0; i < count; i++) {
            System.out.println(ar[i + 1]);
        }
    }

    public void lcr() {
        lcr(1);
    }

    private void lcr(int i) {
        if (i > count)
            return;
        lcr(i * 2);
        System.out.println(ar[i]);
        lcr(i * 2 + 1);
    }

    public static void main(String[] args) {
        ArrayBinTree binTree = new ArrayBinTree();
        for (int i = 0; i < 8; i++) {
            binTree.add(i + 1);
        }
        binTree.levelOrder();
        System.out.println();
        binTree.lcr();
    }
}
