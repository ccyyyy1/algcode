package com.ex.cy.demo4.alg.string.cmp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

//空间消耗比较多，特别是childs，根据数组大小来
//否则牺牲性能，用链表，跳表，跳针来实现（skip pointer）
//Trie树比起做精确查找，一般用在单词补全方面更多，比如输入法的单词补全，浏览器中的高频词汇优先补全提示，IDE中的Api函数后缀自动补全
public class TrieTree {
    private static final int CHAR_SPACE = 26; //a-z

    public static class TTNode {
        char data;
        boolean isEnd;
        TTNode[] childs;
        boolean reUsed;

        public TTNode(char data) {
            this.data = data;
            childs = new TTNode[CHAR_SPACE];
        }
    }

    TTNode root;

    public TrieTree() {
        this.root = new TTNode('/');
    }

    //统计信息
    int orgWordLetterCount;         //若直接单独存储，所需的字母个数
    int nodeLetterCount;            //Trie树节点总数(根节点不计数)
    int samePrefixReuseCount;       //相同字母前缀共用(复用)次数
    int singleSuffixLetterCount;    //不同字母后缀个数（单独使用）

    int wordCount;                  //单词（词条)总个数

    //被共用的前缀的字符个数
    public int getSamePrefixCount() {
        return nodeLetterCount - singleSuffixLetterCount;
    }

    //被复用的前缀的所有字母，总共被复用了多少次
    public int getSamePrefixReuseCont() {
        return samePrefixReuseCount;
    }

    //与直接存储相比，有多少个字符被共用，以节约空间，
    public int get节约LetterCount() {
        return orgWordLetterCount - nodeLetterCount; // = samePrefixReuseCount,相同前缀的节点，被复用一次说明就少存储1个相同字符
    }

    public int getOrgWordLetterCount() {
        return orgWordLetterCount;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void insert(char[] a) {
        TTNode p = root;
        orgWordLetterCount += a.length;
        for (int i = 0; i < a.length; i++) {
            char c = (char) (a[i] - 'a');
            if (p.childs[c] == null) {
                p.childs[c] = new TTNode(c);
                singleSuffixLetterCount++;
                nodeLetterCount++;
            } else {
                samePrefixReuseCount++;
                if (!p.childs[c].reUsed) {
                    singleSuffixLetterCount--;
                    p.childs[c].reUsed ^= true;
                }
            }
            p = p.childs[c];
        }
        p.isEnd = true;
        wordCount++;
    }

    //查找命中的最坏时间复杂度 O(k)  ，k=要被查找的单词的字符个数
    //查找未命中需要不到k次检查
    public boolean find(char[] a) {
        TTNode p = root;
        for (int i = 0; i < a.length; i++) {
            char c = (char) (a[i] - 'a');
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
            char c = (char) (a[i] - 'a');
            if (p.childs[c] == null)
                return res;
            route.push(p);
            p = p.childs[c];
        }//找到前缀的最后一个字符的节点

        genSuffix(p, res, route);
        return res;
    }

    private void genSuffix(TTNode p, List<String> res, Stack<TTNode> route) {
        route.push(p);
        if (p.isEnd) {
            char[] chars = new char[route.size() - 1]; //-1 :root 的 / 不加入
            for (int i = 1; i < route.size(); i++)
                chars[i - 1] = (char) (route.get(i).data + 'a');
            res.add(new String(chars));
        }
        for (int i = 0; i < CHAR_SPACE; i++) {
            if (p.childs[i] != null)
                genSuffix(p.childs[i], res, route);
        }
        route.pop();
    }

    private void addAliveChildTo(TTNode p, Stack<TTNode> stack) {
        for (int asc = 0; asc < CHAR_SPACE; asc++) {
            if (p.childs[asc] != null)
                stack.push(p.childs[asc]);
        }
    }

    public static void main(String[] a) throws IOException {
//        for (char c = '0'; c < 'z'; c++) {
//            System.out.println((int) c + " " + c);
//        }
        TrieTree tt = new TrieTree();
        tt.insert("hello".toCharArray());
        tt.insert("hella".toCharArray());
        tt.insert("he".toCharArray());
        tt.insert("her".toCharArray());
        tt.insert("how".toCharArray());
        tt.insert("hill".toCharArray());
        tt.insert("abc".toCharArray());
        tt.insert("print".toCharArray());
        tt.insert("printf".toCharArray());
        tt.insert("println".toCharArray());

        System.out.println(tt.find("her".toCharArray()));
        System.out.println("  + " + tt.suffix("".toCharArray()));
        System.out.println("he + " + tt.suffix("he".toCharArray()));
        System.out.println("abc + " + tt.suffix("abc".toCharArray()));
        System.out.println("pri + " + tt.suffix("pri".toCharArray()));

        //统计
        //随机生成1万个单词，每个5~15字符长度,加入trie树，看统计信息
        Random r = new Random(1234);
        TrieTree t1 = new TrieTree();
        for (int count = 0; count < 10000; count++) {       //1万个单词
            char[] word = new char[5 + r.nextInt(10)];   //单词长度 5~15
            for (int i = 0; i < word.length; i++) {
                word[i] = (char) (r.nextInt(26) + 'a');
            }
            t1.insert(word);
        }

        System.out.println();
        System.out.println("单词个数 " + t1.getWordCount());
        System.out.println("直接存储单词的字符个数 " + t1.getOrgWordLetterCount());
        System.out.println("Trie树节点个数 " + t1.nodeLetterCount);
        System.out.println("Trie树未复用节点个数 " + t1.singleSuffixLetterCount);
        System.out.println("Trie树已复用节点个数 " + t1.getSamePrefixCount());
        System.out.println("Trie树复用节点的复用次数 " + t1.getSamePrefixReuseCont());
        System.out.println("比直接存储节约了多少个字母的单独存储(和上面数据一致，因为复用次数+1，等于又节约了一次额外相同前缀字母的存储) " + t1.get节约LetterCount());
        System.out.println("复用度 " + ((float)t1.getSamePrefixReuseCont() / t1.getOrgWordLetterCount()));
        System.out.println("被复用的前缀字母，平均被复用多少次 " + ((float)t1.getSamePrefixReuseCont() / t1.getSamePrefixCount()));
    }
}
