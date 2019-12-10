package com.ex.cy.demo4.alg.algthink.dynamic;

import java.util.LinkedList;
import java.util.List;

//2个字符串之间的编辑距离
//1.莱文氏距离（不同字符的编辑最少编辑次数）
//2.最长公共子串（相同公共字符的最大长度）？？？
public class EditDst {
    //莱文氏距离
    //逐个比较a，b2个字符串中的字符是否相等
    //若不相等，那么通过往a或b中虚拟的进行 新增字符，删除字符，替换为相同字符 这3个操作，将指向a的下标i，或指向b的下标j，向后直接略过1位字符（此时需要增加编辑次数 +1）
    //比较完所有字符后，将所需编辑的最小次数作为莱文氏距离返回

    //动态规划，莱文氏距离
    //时间 O(n*w)
    //空间 O(n*w)
    //状态转移公式  : s[i][j] = min(s[i-1][j-1],s[i-1][j],s[i][j-1]) + (a[i]==b[j] ? 0:1 );
    public static int lwsDP(char[] a, int alen, char[] b, int blen) {
        int[][] state = new int[alen][blen]; //[i][j]=编辑次数  i->a[i],j->b[j]
        //第一个字母
        state[0][0] = a[0] == b[0] ? 0 : 1;
        //第一列(b[0]和所有a的单个字母比较)
        for (int i = 1; i < alen; i++) {
            state[i][0] = state[i - 1][0] + (a[i] == b[0] ? 0 : 1);
        }
        //第一行(a[0]和所有b的单个字母比较)
        for (int j = 1; j < blen; j++) {
            state[0][j] = state[0][j - 1] + (a[0] == b[j] ? 0 : 1);
        }
        //其余
        for (int i = 1; i < alen; i++) {
            for (int j = 1; j < blen; j++) {
                int prvMin = state[i - 1][j - 1];
                prvMin = prvMin < state[i - 1][j] ? prvMin : state[i - 1][j];
                prvMin = prvMin < state[i][j - 1] ? prvMin : state[i][j - 1];
                state[i][j] = prvMin + (a[i] == b[j] ? 0 : 1);
            }
        }
        return state[alen - 1][blen - 1];
    }


    //动态规划，最长公共子串
    //时间 O(n*w)
    //空间 O(n*w)
    //1. a[i]==b[j] 时，最大公共子串数值+1，进入下一状态 i++ j++
    //2. a[i]!=b[j] 时，最大公共子串数值不变，进入下一状态
    // 2.1. i++ j不变 ，相当于a中当前第i个字b符被虚拟删掉（略过），或相当于在b中虚拟插入一个a[i]相同的字符(并不需要真的插入)，然后考察下一字符：i++ j不变
    // 2.2. i不变 j++ , 相当于b中当前第j个字符被虚拟删掉（略过），或相当于在a中虚拟插入一个b[j]相同的字符(并不需要真的插入)，然后考察下一字符：i不变 j++
    //
    //和上面莱文氏距离的代码非常相似,只改了10个字符
    //状态转移公式  : s[i][j] = max(s[i-1][j-1],s[i-1][j],s[i][j-1]) + (a[i]==b[j] ? 1:0 );
    public static int lcsDP(char[] a, int alen, char[] b, int blen) {
        int[][] state = new int[alen][blen]; //[i][j]=编辑次数  i->a[i],j->b[j]
        //第一个字母
        state[0][0] = a[0] == b[0] ? 1 : 0;
        //第一列(b[0]和所有a的单个字母比较)
        for (int i = 1; i < alen; i++) {
            state[i][0] = state[i - 1][0] + (a[i] == b[0] ? 1 : 0);
        }
        //第一行(a[0]和所有b的单个字母比较)
        for (int j = 1; j < blen; j++) {
            state[0][j] = state[0][j - 1] + (a[0] == b[j] ? 1 : 0);
        }
        //其余
        for (int i = 1; i < alen; i++) {
            for (int j = 1; j < blen; j++) {
                int prvMax = state[i - 1][j - 1];
                prvMax = prvMax > state[i - 1][j] ? prvMax : state[i - 1][j];
                prvMax = prvMax > state[i][j - 1] ? prvMax : state[i][j - 1];
                state[i][j] = prvMax + (a[i] == b[j] ? 1 : 0);
            }
        }
        return state[alen - 1][blen - 1];
    }

    //纠错，对输入单词a，找到词库中与之对应最接近的单词(莱文氏距离最小的),若莱文氏距离同样小，选最大公共子串值最大的
    //思考：可不可以结合TrieTree的多模式串思想，在一次匹配中，直接找到最接近的(莱文氏距离最小的)一个字符串?
    public static char[] errorCorrection(char[] a, List<char[]> list) {
        int minlws = Integer.MAX_VALUE;
        int maxlcs = Integer.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < list.size(); i++) {
            int tmplws = lwsDP(a, a.length, list.get(i), list.get(i).length);
            int tmplcs = lcsDP(a, a.length, list.get(i), list.get(i).length);
//            System.out.println("错误纠正 最小差异度：" + tmplws + " , 最大相同度：" + tmplcs + " - " + new String(a) + " - " + new String(list.get(i)));
            if (tmplws < minlws) {
                minlws = tmplws;
                index = i;
            }
            if(tmplcs > maxlcs) {
                maxlcs = tmplcs;
                index = i;
            }
        }
        if (index != -1) {
            System.out.println("错误纠正 最小差异度：" + minlws + " , 最大相同度：" + maxlcs);
            return list.get(index);
        }
        return "".toCharArray();
    }

    public static void main(String[] ar) {
        char[] a = "abcdef".toCharArray();
        char[] b = "abdxf".toCharArray();

        //1.莱文氏距离（差异度）
        System.out.println("===== 莱文氏距离（差异度） =====");
        int lws = lwsDP(a, a.length, b, b.length);
        System.out.println("莱文氏距离 lws ：" + lws);

        //2.最大公共子串（相同度）
        System.out.println("\n===== 最大公共子串（相同度） =====");
        int lcs = lcsDP(a, a.length, b, b.length);
        System.out.println("最大公共子串 lcs ：" + lcs);

        //3.单词错误纠正
        System.out.println("\n===== 单词错误纠正 =====");
        List<char[]> list = new LinkedList<>();
        list.add("hello".toCharArray());
        list.add("here".toCharArray());
        list.add("hex".toCharArray());
        list.add("her".toCharArray());
        list.add("hill".toCharArray());
        list.add("毛子".toCharArray());
        list.add("毛熊子".toCharArray());
        list.add("熊孩子".toCharArray());
        char[] input1 = "herro".toCharArray();
        System.out.println("输入 [" + new String(input1) + "] 被纠正为 [" + new String(errorCorrection(input1, list)) + "]");
        input1 = "h".toCharArray();
        System.out.println("输入 [" + new String(input1) + "] 被纠正为 [" + new String(errorCorrection(input1, list)) + "]");
        input1 = "子".toCharArray();
        System.out.println("输入 [" + new String(input1) + "] 被纠正为 [" + new String(errorCorrection(input1, list)) + "]");
        input1 = "熊".toCharArray();
        System.out.println("输入 [" + new String(input1) + "] 被纠正为 [" + new String(errorCorrection(input1, list)) + "]");
    }
}
