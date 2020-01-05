package com.ex.cy.demo4.alg.algthink.dynamic;

import java.util.Arrays;

//最长递增子序列 O(n^2)
public class IncSubSeq {

    public static String incSubSeq(String str) {
        char[] stra = str.toCharArray();
        int[] len = new int[stra.length]; //state,有效长度
        int[] fromIndex = new int[stra.length]; //只记录最长递增子序列的最后一个索引
//        List<Integer>[] fromIndexMuti = new List[stra.length]; //记录所有的子序列的前继字符索引号
//        for (int i = 0; i < fromIndexMuti.length; i++)
//            fromIndexMuti[i] = new LinkedList<>();
        for (int i = 0; i < fromIndex.length; i++)
            fromIndex[i] = -1;

        //动态规划
        int globalMaxLenIndex = -1;
        int globalMaxLen = 0;
        for (int i = 1; i < stra.length; i++) { //以i为结尾的子串
            int effectMaxLength = 0;
            int effectMaxLengthIndex = -1;

            //找到i之前，和i相比，所有比i小的字符的最大有效长度
            for (int j = i - 1; j >= 0; j--) {
                if (stra[i] > stra[j]) {
                    if (effectMaxLength < len[j] + 1) {
                        effectMaxLength = len[j] + 1;
                        effectMaxLengthIndex = j;
                    }
                }
            }

            if (effectMaxLengthIndex != -1) {
//                fromIndexMuti[i].add(effectMaxLengthIndex);//记录以i结尾的子串内，最后一个字符是i结尾的所有子串中最长的子串，i的前继字符索引
                fromIndex[i] = effectMaxLengthIndex;
                if (globalMaxLen < effectMaxLength) {
                    globalMaxLen = effectMaxLength;
                    globalMaxLenIndex = i;
                }
            }
            len[i] = effectMaxLength;
        }

        System.out.println("len[] " + Arrays.toString(len));
        System.out.println(Arrays.toString(fromIndex));

        //根据前继下标，组合得到最长子序列字符串
        StringBuilder sb = new StringBuilder(); //以堆栈方式 FILO
        for (int i = globalMaxLenIndex; i >= 0; ) {
            sb.insert(0, stra[i]);
            i = fromIndex[i];
        }
        System.out.println("最长子序列长度：" + (globalMaxLen + 1));
        System.out.println("最长子序列最尾字符下标：" + globalMaxLenIndex);
        System.out.println("最长子序列字符串：" + sb);

        return sb.toString();
    }

    //只要长度
    public static int incSubSeqLen(String str) {
        char[] stra = str.toCharArray();
        int[] len = new int[stra.length]; //state,有效长度

        //动态规划
        for (int i = 1; i < stra.length; i++) { //以i为结尾的子串
            int effectMaxLength = 0;

            //找到i之前，和i相比，所有比i小的字符的最大有效长度
            for (int j = i - 1; j >= 0; j--) {
                if (stra[i] > stra[j]) {
                    effectMaxLength = Math.max(effectMaxLength, len[j] + 1);
                }
            }
            len[i] = effectMaxLength;
        }

        //找到最长的
        int maxlen = 0;
        for (int i = 0; i < stra.length; i++)
            maxlen = Math.max(maxlen, len[i]);

        System.out.println("maxlen " + (maxlen+1));
        return maxlen;
    }

    public static void main(String[] s) {
        IncSubSeq.incSubSeqLen("121383952");
        IncSubSeq.incSubSeq("121383952");
        System.out.println();
        IncSubSeq.incSubSeqLen("esf54g131gs5d1g534164535wy");
        IncSubSeq.incSubSeq("esf54g131gs5d1g534164535wy");
    }
}
