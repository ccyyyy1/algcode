package com.ex.cy.demo4.alg.string.cmp;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//多模式串匹配算法(TrieTree是）, AC自动机
//一个主串，同时和多个模式串比较
//相反，单模式串匹配算法，如BF,RK,BM,KMP 是 一个主串 和 一个模式串比较
//TrieTree + KMP中匹配失败时，用好前缀的缓存，尽量让模式串向后多滑动几位的思想（AC自动机)
// 好前/后缀 多向后滑动几位，需要在  1尽量向后滑过不匹配的情况，2滑动到刚好部分匹配，3过度滑动而错误匹配机会，4匹配的前缀/后缀子串完全不匹配  这4种情况种找到折中且高效的平衡点
//类比: BF - KMP , TrieTree - AC自动机

//本质:
//TrieTree(多模式串） + KMP的好前缀next[]一次尽量后移多位模式串 = 多模式串下的fail指针思想

//重点有2个
//1.fail指针的建立
//2.利用fail指针的查找功能

//难点是每个节点的fail指针的构建，用层序遍历，默认fail指向root，只有root的fail可以为null(其他节点的fail要么为同字符的底层节点，
// 要么为root)，当前节点（pc）的fail，从父节点的fail处开始回退(p=p.fail,然后又是 p=p.fail 直到p是root则停止通过fail节点退回)，
// 直到root节点，若在此回退过程中p节点存在一个child的字符为当前节点（pc）相同的字符，那么p节点被确定下来，pc的fail节点为这个p节点字符相同的child节点

//构建失败指针的时候用 层序遍历 这颗树（层序遍历的叫法，只不过是在树这种特例的无向图中，广度优先(BFS)的特定叫法,特点是使用了队列）
public class ACTree {
    //MultParttenCmp
    public static final int CHAR_SPACE = 26;

    public static class ACNode {//16+4  +247=267
        char data;//2
        ACNode fail;//8
        int len;//4
        boolean isEnd;//1
        ACNode[] childs;//232字节

        public ACNode(char data) {
            this.data = data;
            childs = new ACNode[CHAR_SPACE];    //16+4+4+ 8*26
        }
    }

    ACNode root;
    boolean isAfterInsertInitFailPoint = false;     //在最后一次插入数据后是否有更新fail pointer的缓存

    public ACTree() {
        this.root = new ACNode('/');
    }

    public void insert(char[] str) {
        ACNode p = root;
        for (int i = 0; i < str.length; i++) {
            char c = (char) (str[i] - 'a');
            if (p.childs[c] == null)
                p.childs[c] = new ACNode(c);
            p = p.childs[c];
        }
        p.isEnd = true;
        p.len = str.length;
        isAfterInsertInitFailPoint = false;
    }

    //构建AC自动机
    //一共4个主要指针 p,pc,q,qc
    //p 是当前节点， pc 是当前节点 child 节点 ， q是p的fail节点(也可以是q的fail节点，一路fail到root，fail到null)， qc是 q的child节点(用于找到和pc字符相同的节点，找到的话用pc.fail 指向 qc)
    //找qc.data == pc.data 的 qc，通过q=q.fail一直到root，目的是找到pc的fail应该指向的另外一个比当前字符串更短的字符串的最长匹配前缀 pc.fail = qc，若不存在这样的qc，让 pc.fail = root
    public void initFailPointer() {
        Queue<ACNode> queue = new LinkedList<>(); //层序遍历，广度优先
        root.fail = null;
        queue.add(root);
        while (!queue.isEmpty()) {
            ACNode p = queue.remove();
            for (int cn = 0; cn < CHAR_SPACE; cn++) {//1.q = 遍历当前p的所有有效child进行处理
                ACNode pc = p.childs[cn];       //2.qc = q遍历中的有效child
                if (pc == null)                 //当前child不存在，跳过处理
                    continue;
                if (p == root)
                    pc.fail = root;             //3.第二层(也就是root的child)pc.fail 默认指向root
                else {                          //4.其他层pc.fail指向p.fail.child[pc.data] 或 root,不能为null //从q是第二层开始，也就是qc是第三层起，找p的fail节点(也就是q节点)的child(也就是qc节点) 与当前pc的字符相同的节点（与pc节点字符相同的qc节点）作为pc的fail节点.
                    ACNode q = p.fail;          //p.fail 是和p字符相匹配的节点，那么对于当前pc节点的字符，可能会再次匹配p.fail的串中的字符下一个字符(也就是和pc匹配,若匹配说明q.child[pc.data] 的项不会为null)
                    while (q != null) {         //q从p.fail ，到q=q.fail ,直到 q=root, q=null，表示没有任何一个qc和pc字符相同，则会 q==null 退出循环，这种情况下pc.fail 默认 = root (类似KMP的next[i] = -1的情况，表示字符前缀需要从头开始匹配)
                        ACNode qc = q.childs[cn];
                        if (qc != null) {       //找到 pc.data == qc.data 的 qc节点，让当前pc的fail 指向他
                            pc.fail = qc;
                            break;              //找到了, 退出当前循环，准备对queue的下一个节点处理
                        }
                        q = q.fail;             //当前q节点的childs不含有字符等于pc字符的child，继续让q返回到当前q的fail节点（走回头路),往前回溯，类似KMP的 k=next[k-1]，回溯到前一个最长匹配前缀的最大匹配下标
                    }
                    if (q == null)
                        pc.fail = root;
                }
                queue.add(pc);
            }
        }
        isAfterInsertInitFailPoint = true;      //已经更新好fail pointer了
    }

    public static class SearchResultData {
        char[] chars;
        int offset;

        public SearchResultData() {
        }

        public SearchResultData(char[] chars, int offset, int len) {
            this.offset = offset;
            this.chars = new char[len];
            for (int i = offset; i < offset + len; i++) {
                this.chars[i - offset] = chars[i];
            }
        }

        @Override
        public String toString() {
            return "{" +
                    "chars=" + Arrays.toString(chars) +
                    ", offset=" + offset +
                    ", len=" + chars.length +
                    '}';
        }
    }

    //给主串a，用AC自动机内的多个模式串，去匹配a串（主串）,将所有符合匹配规则的模式串输出返回,适合一次扫描查找多个单词
    public List<SearchResultData> find(char[] a) {
        if (isAfterInsertInitFailPoint == false)
            initFailPointer();
//        List<String> res = new LinkedList<>();
        List<SearchResultData> res2 = new LinkedList<>();
        ACNode p = root;
        for (int i = 0; i < a.length; i++) {           //a[i] 为a串的当前比对字符
            int c = (a[i] - 'a');
            if (c < 0 || c > CHAR_SPACE - 1) {         //不属于字典内的字符跳过处理,重置p为root状态
                p = root;
                continue;
            }
            while (p.childs[c] == null && p != root) { //通过fail指针，退回到一个child有c的节点，或者这种节点一个都不存在，则最终会退回到root
                p = p.fail;
            }
            p = p.childs[c];                           //p指向目标字符c 的节点
            if (p == null)
                p = root;                              //当前字符，在AC自动机里不存在,表示连root都没有这个字符的child节点,p就为null了

            ACNode tmp = p;                            //双重分身术,让p的替身(一胎tmp韭菜)去做单词输出,p呆在原地保持发型不乱 //此时 p 要么指向 字符c的child，要么指向root
            while (tmp != root) {
                if (tmp.isEnd)
                    res2.add(new SearchResultData(a, i - tmp.len + 1, tmp.len));
//                    res.add(new String(a, i - tmp.len + 1, tmp.len));
                tmp = tmp.fail;                        //一直fail到 root为止，看以当前字符c相关的节点，有没有是isEnd的（代表已经匹配到了完整的词尾，那么用上一条语句输出它）
            }
        }
//        return res;
        return res2;
    }


    public char[] replace(char[] a, char replaceTo) {
        if (isAfterInsertInitFailPoint == false)
            initFailPointer();
        ACNode p = root;
        int skip = 0;
        char[] res = new char[a.length];
        for (int i = 0; i < a.length; i++)
            res[i] = a[i];

        for (int i = 0; i < a.length; i++) {
            int c = a[i] - 'a';
            if (c < 0 || c > 25) { //跳过不在字典范围内的字符,并归零p到root状态
                skip++;
                p = root;
                continue;
            }
            while (p.childs[c] == null && p != root) {  //通过fail回退，找到有c字符作为child的父节点，直到root
                p = p.fail;
            }
            p = p.childs[c];
            if (p == null)
                p = root;

            ACNode tmp = p;
            while (tmp != root) {
                if (tmp.isEnd) {
                    //将匹配的单词换成 *
                    for (int j = i - tmp.len + 1; j <= i; j++) {
                        res[j] = replaceTo;
                    }
                }
                tmp = tmp.fail;
            }
        }
        return res;
    }


    public static void main(String[] ar) {
//        ascii();
        demo1();
        demo2();
    }

    private static void demo1() {
        ACTree acTree = new ACTree();
        acTree.insert("abcd".toCharArray());
        acTree.insert("bcd".toCharArray());
        acTree.insert("c".toCharArray());

        char[] a = "abcdccbccbcd".toCharArray();
        acTree.initFailPointer();
        List<SearchResultData> list = acTree.find(a);

        System.out.println("======demo1======");
        System.out.println("" + list);
        System.out.println(new String(a));
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).offset; j++)
                System.out.print(' ');
            System.out.println(new String(list.get(i).chars));
        }
        System.out.println("" + new String(acTree.replace(a, '*')));
    }

    private static void demo2() {
        ACTree acTree1 = new ACTree();
        acTree1.insert("stupid".toCharArray());
        acTree1.insert("shit".toCharArray());
        acTree1.insert("suck".toCharArray());
        acTree1.insert("sucker".toCharArray());
        acTree1.insert("dumb".toCharArray());
        acTree1.insert("ass".toCharArray());

        acTree1.initFailPointer();
        char[] a2 = "you are stupid sucker! asshour#￥%！#%！@#￥".toCharArray();

        System.out.println("\n======demo2======");
        System.out.println("替换前:" + new String(a2));

        List<SearchResultData> list = acTree1.find(a2);
        for (int i = 0; i < list.size(); i++) {
            System.out.print("替换掉:");
            for (int j = 0; j < list.get(i).offset; j++)
                System.out.print(' ');   //list.get(i).offset
            System.out.println(new String(list.get(i).chars));
        }
        System.out.println("替换后:" + new String(acTree1.replace(a2, '*')));
    }

    public static void ascii() {
        for (int i = 0; i < 256; i++) {
            System.out.print((char) i);
            if ((i & (16 - 1)) == 0)
                System.out.println();
        }
        System.out.println((int) ' ');
    }
}
