package com.ex.cy.demo4.alg;

import java.util.Random;
import java.util.Scanner;

public class SkipList {
    static final int MAX_LEVEL = 16;
    Random r = new Random();
    int count = 0;
    Node dummy;

    public SkipList() {
        dummy = new Node();
    }

    //字符串，引用，数组 内存消耗
    public static class Node {                                  //对象头16字节 + 引用8字节 + 4/8字节填充(机器字长)
        int maxLevel = 0;
        Node[] next = new Node[MAX_LEVEL];                      //16+4+4+4 + 8*N   =  28 + 8N  = 28+128
        int key = -1;
        Object data;

        public Node() {                                         //1个node至少消耗内存 16+4+4+156+4+8 = 182 字节
        }

        public Node(int maxLevel, int key, Object data) {
            this.maxLevel = maxLevel;
            this.key = key;
            this.data = data;
        }

        @Override
        public String toString() {
            String s1 = "{" +
                    "key=" + key +
                    ", maxLevel=" + maxLevel +
                    ", next=[";
            int i = 0;
            for (Node o : next) {
                s1 += (o == null ? "n\t" : o.key + "\t");
                if (i == maxLevel)
                    s1 += "|";
                s1 += "\t";
                i++;
            }
            s1 += "]}";
            return s1;
        }
    }

    public Node find(int key) {
        Node cur = dummy;
        int lv = cur.maxLevel;
        System.out.println("find key=" + key);
        while (lv >= 0) {
            System.out.println("find now :" + cur + " lv : " + lv);
            if (cur.next[lv] == null || cur.next[lv].key > key) {
                lv--;                                           //down
                System.out.println("down     :" + (lv > 0 ? "" + cur.next[lv] : lv));
            } else if (cur.next[lv].key < key) {
                cur = cur.next[lv];                             //forward
                System.out.println("forward  :" + cur);
            } else {
                System.out.println("equ      :" + cur.next[lv]);
                return cur.next[lv];                            //==
            }
        }
        return null;
    }

    public void insert(int key) {
        int newLevel = randomLevel();
        System.out.println("insert(" + key + ") newLevel " + newLevel);
        System.out.println("insert(" + key + ") dummy.maxLevel " + dummy.maxLevel);
        dummy.maxLevel = newLevel > dummy.maxLevel ? newLevel : dummy.maxLevel;
        int level = dummy.maxLevel < newLevel ? dummy.maxLevel : newLevel;
        System.out.println("insert(" + key + ") level " + level);
        Node cur = dummy;
        Node[] levelLastNodes = new Node[level + 1];

        while (level >= 0) {
            if (cur.next[level] == null || cur.next[level].key > key) {
                levelLastNodes[level] = cur;                    //down
                level--;
            } else if (cur.next[level].key < key) {
                cur = cur.next[level];                          //forward
                levelLastNodes[level] = cur;
            } else {                                            //==
                break;
            }
        }

        Node newNode = new Node(newLevel, key, null);
        for (int lv = 0; lv < levelLastNodes.length; lv++) {
            newNode.next[lv] = levelLastNodes[lv].next[lv];
            levelLastNodes[lv].next[lv] = newNode;
        }
        count++;
    }

    public void del(int key) {
        int level = dummy.maxLevel;
        Node cur = dummy;
        Node[] levelLastNodes = new Node[dummy.maxLevel + 1];   //记录被删除节点的同层前继节点,用于删除时重新链接后续节点
        Node delNode = null;

        while (level >= 0) {                                    //寻找到被删除节点，并记录前继节点
            if (cur.next[level] == null || cur.next[level].key > key) {
                levelLastNodes[level] = cur;                    //down
                level--;
            } else if (cur.next[level].key < key) {
                cur = cur.next[level];                          //forward
                levelLastNodes[level] = cur;
            } else {                                            //==
                levelLastNodes[level] = cur;
                level--;
            }
        }

        if (cur.next[0] != null && cur.next[0].key == key) {
            delNode = cur.next[0];
            for (int lv = 0; lv < levelLastNodes.length; lv++) { //log
                System.out.println("levelLastNodes " + lv + "\t:" + levelLastNodes[lv]);
            }
            for (int lv = delNode.maxLevel; lv >= 0; lv--) {     //del node
                levelLastNodes[lv].next[lv] = delNode.next[lv];  //使用前继节点后接被删节点的后继节点
                if (levelLastNodes[lv] == dummy && dummy.next[lv] == null) {
                    dummy.maxLevel--;                            //当删除的是在最高层有索引的唯一节点,那么降低全局最大高度
                }
            }
            count--;
        }
        System.out.println("del\t\t:" + delNode);
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node n = dummy;
        while (n != null) {
            sb.append(n);
            sb.append('\n');
            n = n.next[0];
        }
        return sb.toString();
    }

    //50% 1层，25% 2层 ， 12.5% 3层 ...
    //next,next/2,next/4,... 最高16层  next/2^16 = next/65536
    protected int randomLevel() {
        int l = 0;
        while (r.nextBoolean() && l < MAX_LEVEL) {
            l++;
        }
        return l;
    }

    public static void main(String args[]) {
        SkipList sl = new SkipList();
        for (int i = 1; i <= 5; i++)                //插入10，20，30.。。90
            sl.insert(i * 10);
        System.out.println(sl.toString());

        System.out.println(sl.find(40));
        System.out.println();
        System.out.println(sl.find(41));
        System.out.println();

        sl.del(9999);
        System.out.println(sl.toString());
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Please input action(1=insert,2=del,3=find,0=quit): ");
            int action = scanner.nextInt();
            if (action == 0)
                break;
            System.out.println("Please input key to del:(0 to quit) ");
            int key = scanner.nextInt();

            switch (action) {
                case 1:
                    sl.insert(key);
                    break;
                case 2:
                    sl.del(key);
                    break;
                case 3:
                    System.out.println(sl.find(key));
                    break;
                default:
                    break;
            }
            System.out.println(sl.toString());
            System.out.println();
        } while (true);
    }

}
