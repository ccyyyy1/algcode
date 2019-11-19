package com.ex.cy.demo4.alg.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LinkBinTree {
    //层序遍历
    public static class Node {
        Node l;
        Node r;
        int key;
        Object data;

        public Node() {
        }

        public Node(int key) {
            this.key = key;
        }

        public Node(int key, Object data) {
            this.key = key;
            this.data = data;
        }

        @Override
        public String toString() {
            return "" + key;
        }
    }

    int count = 0;
    Node root;

    public int height() {
        if (root == null)
            return -1;
        return height(root);
    }

    public int height(Node n) {
        if (n != null && n.l == null && n.r == null)
            return 0;

        int lh = (n.l != null) ? height(n.l) : -1;
        int rh = (n.r != null) ? height(n.r) : -1;
        int h = lh > rh ? lh : rh;
        h++;
        return h;
    }

    public void add(int k) {
        if (root == null)
            root = new Node(k, count);
        else
            add(root, k);
        count++;
    }

    private void add(Node n, int k) {
        if (n.key > k) {
            if (n.l == null)
                n.l = new Node(k, count);
            else
                add(n.l, k);
        } else { //<= k
            if (n.r == null)
                n.r = new Node(k, count);
            else
                add(n.r, k);
        }
    }


    public List<Node> find(int k) {
        List<Node> l = new ArrayList<>();
        if (root == null)
            return l;
        Node t = root;
        while (t != null) {
            if (t.key > k) {
                t = t.l;
            } else if (t.key < k) { // <=
                t = t.r;
            } else { //==
                l.add(t);
                t = t.r;
            }
        }

        return l;
    }

    public Node delMin(Node n) {
        Node p = n;
        Node pp = null;
        while (p.l != null) {
            pp = p;
            p = p.l;
        }

        Node delMinN = null;
        if (p.r != null) {
            delMinN = delMin(p.r);
            if (p.r == delMinN)
                p.r = null;
        }

        if (pp != null) {
            if (delMinN != null)
                pp.l = delMinN;
            else
                pp.l = null;
        }
        return p;
    }

    //删除第一个key==k的节点
    public Node del(int k) {
        Node p = root;
        Node pp = null;

        while (p != null) {
            if (p.key < k) {
                pp = p;
                p = p.r;
            } else if (p.key > k) {
                pp = p;
                p = p.l;
            } else {
                break;
            }
        }

        if (p.l == null && p.r == null) {   //叶子节点
            if (pp == null) {               //如果是根节点
                root = null;
            } else if (pp.l == p) {         //左
                pp.l = null;
            } else {                        //右
                pp.r = null;
            }
        } else if (p.r != null) {           //中间节点
            Node delMinP = delMin(p.r);     //找到被删节点的右子树中的最小节点,用于填充被删节点的空穴
            if (p.r == delMinP)
                p.r = null;
            if (pp != null) {
                if (pp.r == p)
                    pp.r = delMinP;
                else
                    pp.l = delMinP;
            } else {                        //如果是根节点
                delMinP.l = root.l;
                delMinP.r = root.r;
                root = delMinP;
            }
        } else if (p.l != null) {           //中间节点,只存在左节点,用左节点填充被删节点空穴
            if (pp.l == p) {
                pp.l = p.l;
            } else {
                pp.r = p.l;
            }
            p.l = null;
        }
        return p;
    }

    @Override
    public String toString() {
        String s = "";
        Queue<Node> q1 = new LinkedList<>();//本层
        Queue<Node> q2 = new LinkedList<>();//下一层
        q2.add(root);
        Node q1n;

        while (q2.size() > 0) {
            q1.addAll(q2);
            q2.clear();
            while ((q1n = q1.poll()) != null) {//本层
                s += q1n.key + "(" + q1n.data + ") ";
                if (q1n.l != null)
                    q2.add(q1n.l);
                if (q1n.r != null)
                    q2.add(q1n.r);
            }
            s += "\n";
        }
        return s;
    }

    //    //基于节点的树的层序遍历
//    public void levelOrder1() {
//        int[] a = new int[count + 1];
//        levelOrder1(root, a, 1);
//        for (int n : a) {
//            System.out.println(n);
//        }
//    }
//
//    public void levelOrder1(Node r, int[] a, int i) {
//        if (r == null)
//            return;
//        a[i] = r.key;
//        levelOrder1(r.l, a, i * 2);
//        levelOrder1(r.r, a, i * 2 + 1);
//    }

    public Queue<Node> levelOrderBFS() {
        return levelOrderBFS(root, true, true);
    }

    /**
     * @param r
     * @param l2r 从左到右
     * @param t2b 从上到下
     * @return
     */
    public Queue<Node> levelOrderBFS(Node r, boolean l2r, boolean t2b) {
        //广度优先
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(r);
        Node n;
        int i = 0;
        while (queue.size() > i) {
            n = queue.get(i);
            if (l2r && t2b || !t2b && !l2r) {//从左到右 从上到下 或 从右到左 从下到上
                if (n.l != null)
                    queue.add(n.l);
                if (n.r != null)
                    queue.add(n.r);
            } else {                         //从右到左 从上到下 或 从左到右 从下到上
                if (n.r != null)
                    queue.add(n.r);
                if (n.l != null)
                    queue.add(n.l);
            }
            i++;
        }

        if (!t2b) {                          //从下到上的，将队列逆序
            i = queue.size() - 1;
            while (i >= 0) {
                queue.add(queue.remove(i));
                i--;
            }
        }
        return queue;
    }

    public static void main(String[] args) {
        System.out.println("\nlevelOrder_test==========");
        levelOrder_test();
        System.out.println("\nadd_del_test==========");
        add_del_test();
    }

    private static void add_del_test() {
        LinkBinTree lbt = new LinkBinTree();
        lbt.add(5);
        lbt.add(3);
        lbt.add(6);
        lbt.add(5);
        lbt.add(5);
        Queue<Node> l = lbt.levelOrderBFS();
        Node n;
        while ((n = l.poll()) != null) {
            System.out.print(n.key + " ");
        }
        System.out.println();

        List<Node> list = lbt.find(5);
        for (Node n1 : list) {
            System.out.print(n1.key + " ");
        }
        System.out.println("\n\n");
        System.out.println(lbt.toString());

        System.out.println("del : " + lbt.del(5));
        System.out.println(lbt.toString());
        System.out.println("height " + lbt.height());

        System.out.println("del : " + lbt.del(5));
        System.out.println(lbt.toString());
        System.out.println("height " + lbt.height());

        System.out.println("del : " + lbt.del(5));
        System.out.println(lbt.toString());
        System.out.println("height " + lbt.height());
    }

    private static void levelOrder_test() {
        LinkBinTree linkBinTree = new LinkBinTree();
        linkBinTree.root = new Node(1);
        linkBinTree.root.l = new Node(2);
        linkBinTree.root.r = new Node(3);
        linkBinTree.root.l.l = new Node(4);
        linkBinTree.root.l.r = new Node(5);
        linkBinTree.root.r.l = new Node(6);
        linkBinTree.root.r.r = new Node(7);
        linkBinTree.root.l.l.r = new Node(9);
        linkBinTree.count = 8;

//        linkBinTree.levelOrder1(); //只能处理满二叉树

        Queue<Node> l = linkBinTree.levelOrderBFS();
        Node n;
        while ((n = l.poll()) != null) {
            System.out.print(n.key + " ");
        }
        System.out.println();

        l = linkBinTree.levelOrderBFS(linkBinTree.root, true, false); //4567 23 1
        while ((n = l.poll()) != null) {
            System.out.print(n.key + " ");
        }
        System.out.println();

        l = linkBinTree.levelOrderBFS(linkBinTree.root, false, false); //7654 32 1
        while ((n = l.poll()) != null) {
            System.out.print(n.key + " ");
        }
    }
}
