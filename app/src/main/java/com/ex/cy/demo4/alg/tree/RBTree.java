package com.ex.cy.demo4.alg.tree;

public class RBTree {
    //红黑树 = 用二叉树实现的 2-3树 ，为了保持深度特性，又具备AVL树的旋转特性
    //红黑树 = 二叉树 + AVL树 + 2-3树
    //2-3树 = 3阶B树

    //要求满足的状态
    //根节点始终是黑色
    //默认的叶子节点为 NIL 节点,空的叶子节点默认算作黑色节点
    //不存在右子红节点，不存在连续2个红节点
    //从一个节点到其任意子节点，包含相同数目的黑色节点

    //1.插入红节点 （2-3树特性)
    //2.判断是否需要旋转，右红子节点转成到左侧  \ -> /   (不存在右子红节点） （为了简化编程,红黑树+AVL树特性）
    //3.判断是否存在连续的2个红节点，旋转成 /\ 姿态， 然后把-4节点 /\ 中间节点爆浆(改为红色)到上层  （左右节点改为黑，中节点改为红(但根节点始终为黑色），然后递归到上层，再次进行 2. 3. 的步骤)   （为了简化编程） （2-3树特性，把一个-4节点爆浆,上浮)
    //  图例： / 和 \ = red节点
    //       左旋转   右旋转           右旋转             改颜色
    //    /  ->   /   ->   /\   ,  /  ->   /\   ,   /\  ->   b/\b
    //    \      /                /
    // 只有子节点是红色的情况才会用旋转
    // 旋转是为了 1.方便编码（各种情况归类到1种）  2.满足深度   3.2-3树 -4节点上浮

    public static class Node {
        public static boolean BLACK = false;
        public static boolean RED = true;
        boolean color;
        int n;                              //子节点数量
        int key;
        Object val;
        Node l;                             //left
        Node r;                             //right

        public Node() {
        }

        public Node(boolean color, int n, int key, Object val) {
            this.color = color;
            this.n = n;
            this.key = key;
            this.val = val;
        }
    }

    Node root;
    int count;

    public void add(int k) {
        add(k, count);
    }

    public void add(int k, Object val) {
        root = add(root, k, val);
        root.color = Node.BLACK;
        count++;
    }

    public Node add(Node n, int k, Object val) {
        if (n == null)
            return new Node(Node.RED, 1, k, count);

        if (n.key > k)
            n.l = add(n.l, k, val);
        else if (n.key < k)
            n.r = add(n.r, k, val);
        else                                //不允许同key存在多个元素
            n.val = val;

        //1.不存在右侧红节点
        //2.不存在2个连续红节点
        //3.< 和 / 型2个红节点转成 /\ 型，然后爆浆
        if (isRed(n.r) && !isRed(n.l))       // \ -> /              !isRed(n.l) 必须要？
            n = rotateLeft(n);
        if (isRed(n.l) && isRed(n.l.l))      // / -> /\
            n = rotateRight(n);
        if (isRed(n.l) && isRed(n.r))        // /\ -> b/\b
            flipColor(n);

        n.n = size(n.l) + size(n.r) + 1;
        return n;
    }

    private int size(Node n) {
        if (n == null)
            return 0;
        return n.n;
    }

    private Node rotateLeft(Node n) {
        //位置
        Node rc = n.r;                      //right child
        n.r = rc.l;
        rc.l = n;
        //颜色
        rc.color = n.color;                 //转上去的节点和原来节点保持一个颜色
        n.color = rc.RED;                   //转下去的改为红色  ？为什么一定要改为红色
        //子节点数量
        rc.n = n.n;
        n.n = 1 + size(n.l) + size(n.r);    //左子节点个数 + 右子节点个数 + 自己
        return rc;
    }

    private Node rotateRight(Node n) {
        //位置
        Node lc = n.l;  //left child
        n.l = lc.r;
        lc.r = n;
        //颜色
        lc.color = n.color;
        n.color = Node.RED;
        //子节点数量
        lc.n = n.n;
        n.n = 1 + size(n.l) + size(n.r);
        return lc;
    }

    private boolean isRed(Node n) {
        if (n == null) return Node.BLACK;
        return n.color;
    }

    private void flipColor(Node n) {
        if (n == null)
            return;
//        if (isRed(n.l) && isRed(n.r)) {
        n.l.color = Node.BLACK;
        n.r.color = Node.BLACK;
        n.color = Node.RED;
//        }
    }
}
