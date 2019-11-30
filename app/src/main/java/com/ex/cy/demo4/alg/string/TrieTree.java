package com.ex.cy.demo4.alg.string;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class TrieTree {
    private static final int CHAR_SPACE = 256; //ascii

    public static class TTNode {
        char data;
        boolean isEnd;
        TTNode[] childs;

        public TTNode(char data) {
            this.data = data;
            childs = new TTNode[CHAR_SPACE];
        }
    }

    TTNode root;

    public TrieTree() {
        this.root = new TTNode(' ');
    }

    public void insert(char[] a) {
        TTNode p = root;
        for (int i = 0; i < a.length; i++) {
            char c = a[i];
            if (p.childs[c] == null)
                p.childs[c] = new TTNode(c);
            p = p.childs[c];
        }
        p.isEnd = true;
    }

    public boolean find(char[] a) {
        TTNode p = root;
        for (int i = 0; i < a.length; i++) {
            char c = a[i];
            if (p.childs[c] == null)
                return false;
            p = p.childs[c];
        }
        if (p.isEnd == true)
            return true;
        return false;
    }

    //找到和给出前缀匹配的 的单词
    public List<String> suffix(char[] a) {
        List<String> res = new LinkedList<>();
        Stack<TTNode> route = new Stack();  //记录深度优先遍历的当前堆栈，直到子节点，用于生成字符串

        TTNode p = root;
        for (int i = 0; i < a.length; i++) {
            char c = a[i];
            if (p.childs[c] == null)
                return res;
            route.push(p);
            p = p.childs[c];
        }//找到前缀的最后一个字符的节点

        genSuffix(p, res, route);
//        Stack<TTNode> stack = new Stack();  //bfs 深度优先
//        Stack<TTNode> route = new Stack();  //记录深度优先遍历的当前堆栈，直到子节点，用于生成字符串
//        addAliveChildTo(p, stack);
//        while (!stack.isEmpty()) {
//            p = stack.pop();
//            route.push(p);
//            if (!p.isEnd)
//                addAliveChildTo(p, stack);
//            else {
//                char[] chars = new char[route.size()];
//                for (int ri = 0; ri < route.size(); ri++)
//                    chars[ri] = route.elementAt(ri).data;
//                res.add(new String(chars));
//            }
//        }
        return res;
    }

    private void genSuffix(TTNode p, List<String> res, Stack<TTNode> route) {
        route.push(p);
        if (p.isEnd) {
            char[] chars = new char[route.size()];
            for (int i = 0; i < route.size(); i++)
                chars[i] = route.get(i).data;
            res.add(new String(chars));
        } else {
            for (int i = 0; i < CHAR_SPACE; i++) {
                if (p.childs[i] != null)
                    genSuffix(p.childs[i], res, route);
            }
        }
        route.pop();
    }

    private void addAliveChildTo(TTNode p, Stack<TTNode> stack) {
        for (int asc = 0; asc < CHAR_SPACE; asc++) {
            if (p.childs[asc] != null)
                stack.push(p.childs[asc]);
        }
    }


    public static void main(String[] a) {
        TrieTree tt = new TrieTree();
        tt.insert("hello".toCharArray());
        tt.insert("hella".toCharArray());
        tt.insert("her".toCharArray());
        tt.insert("how".toCharArray());
        tt.insert("hill".toCharArray());
        tt.insert("abc".toCharArray());

        System.out.println(tt.find("her".toCharArray()));
        System.out.println("  + " + tt.suffix("".toCharArray()));
        System.out.println("he + " + tt.suffix("he".toCharArray()));
        System.out.println("abc + " + tt.suffix("abc".toCharArray()));
    }
}
