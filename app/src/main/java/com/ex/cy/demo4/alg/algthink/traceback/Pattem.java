package com.ex.cy.demo4.alg.algthink.traceback;

public class Pattem {
    //暴力回溯法，正则表达式匹配,支持？0个或1个匹配，*0个或1个匹配
    boolean mached = false;
    char[] pattern;
    int plen;
    char[] text;
    int tlen;

    public Pattem(char[] pattern) {
        this.pattern = pattern;
        this.plen = pattern.length;
    }

    public boolean match(char[] text) {
        mached = false;
        this.text = text;
        this.tlen = text.length;
        rmatch(0, 0);
        text = null;
        tlen = 0;
        return mached;
    }

    //ti：文本当前匹配字符
    //pj：模式串当前匹配字符
    private void rmatch(int ti, int pj) {
        if (mached)
            return;
        if (pj == plen) {       //模式串遍历完成
            if (ti == tlen)     //text也遍历完成
                mached = true;
            return;
        }

        if (pattern[pj] == '*') {
            for (int k = 0; k <= tlen - ti; k++) { //ti+0 = * 匹配 0 个字符 ， 匹配1个，2个。。。直到匹配text剩余的所有文本
                rmatch(ti + k, pj + 1);
                if (mached)
                    return;
            }
        } else if (pattern[pj] == '?') {            //0 or 1
            rmatch(ti, pj + 1);          //0 ,text不前进，模式串前进1个
            if (mached)
                return;
            rmatch(ti + 1, pj + 1);   //1 ,text 和 模式都前进一个
        } else if (ti < tlen && text[ti] == pattern[pj] || pattern[pj] == '.') {  //单个纯文本匹配 ，或text遍历完成
            rmatch(ti + 1, pj + 1);   //1 text和模式都前进一个
        }
    }

    public static void main(String[] ar) {
        Pattem p = new Pattem("a.b?*".toCharArray());
        String txt = "acb";
        System.out.println("" + p.match(txt.toCharArray()));
    }
}
