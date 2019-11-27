package com.ex.cy.demo4.alg.string;

//字符串匹配
public class StringCmp {
    //约定：A主串长 n ，B模式串 长m。要求：在A串中找到B串匹配的下标
    //
    //1.BF burst force 暴力比较，逐个字符比较
    //2.RK 以hash算法为主，比较字符串

    //BF  时间复杂度 O(n*m)
    //在A中进行 A[0,n-m] 范围内的 n-m+1 次的 m个字符比较
    //平时最容易理解的方式
    public static int BF(char[] a, char[] b) {
        for (int i = 0; i < a.length - b.length + 1; i++) {
            for (int j = 0; j < b.length; j++) {
                if (a[i + j] != b[j])
                    break;
                else if (j + 1 == b.length)
                    return i;
            }
        }
        return -1;
    }

    //在A串中 从一个起始位置i，选取 A[i]~A[i+m] 共m个字符，计算其hash值(h1)  i+m 不超过A的最大长度，也就是 i = [0,n-m] 范围
    //hash算法为 字符的取值个数，如只考虑a-z，共26个字母，算作R进制（26进制），那么一个3个字符的字符串的hash为 a1*R^2 + a2*R^1 + a3*R^0  (a1为高位，a3为低位)
    //随着 i自增1，向后移动一个起始位置后，还是做同样的hash计算(h2)
    //此时发现有规律 h2=（h1-a1*R^m-1）* R + a4*R^0
    //所以把A扫一遍计算 hash，在对每次算到的hash和 hashB 比较，一致说明有字符串匹配，如果考虑hash碰撞，则再次挨个比较字符即可（BF算法）
    //时间复杂度 O(n)
    public static int RK(char[] a, char[] b) {
        int m = b.length;
        RKinit(m);
        long hashB = RKHash(b, 0, m);
        long[] hashA = new long[a.length - b.length + 1];

        hashA[0] = RKHash(a, 0, m);
        if (hashA[0] == hashB)
            return 0;
        for (int i = 1; i < hashA.length; i++) {
            hashA[i] = (hashA[i - 1] - (a[i - 1] * RpowN[m - 1])) * R + a[i + m - 1];
            if (hashA[i] == hashB)
                return i;
        }
        return -1;
    }

    static final int R = 256; //256进制 ASCII码表范围
    static long[] RpowN = new long[4];
    static boolean inited;

    private static void RKinit(int blen) {  //初始化R的N次方的缓存表
        long[] tmp;
        int startI = 2;
        if (blen > RpowN.length) {
            tmp = new long[blen];
            startI = RpowN.length;
            RpowN = tmp;
            System.arraycopy(RpowN, 0, tmp, 0, RpowN.length);
            inited = false;
        }
        if (inited == false) {
            RpowN[0] = 1;
            RpowN[1] = R;
            for (int i = startI; i < RpowN.length; i++)
                RpowN[i] = (long) Math.pow(R, i);
            inited = true;
        }
    }

    public static long RKHash(char[] a, int off, int len) {
        long hashR = 0;
        for (int i = 0; i < len; i++) {        //从高位到低位 a1*R^len + a2*R^len-1 + ....alen*R^0
            hashR += RpowN[len - i - 1] * a[off + i];
        }
        return hashR;
    }

    //BM算法：从B串和A串尾部开始比较，希望一次将B串向后滑动尽量多的位数，
    //       以跳过不匹配的情况，理想情况的时间复杂度是 O(n/m)
    //
    // A        A----------*==---------
    // B            B---*==               *为在A中的坏字符，*在B中的相同位置
    //                                    *右边的部分，2个=号表示匹配到的好后缀
    // B向右滑动        B---*==            向右滑动了2位，让B坏字符位置对齐在A中相同坏字符的位置
    //
    // 控制B向右滑动多少位，有2个主要策略
    //坏字符：在B中寻找最靠右（下标最大）的相同字符下标
    //好后缀：在B中寻找最靠右（下标最大）的相同好后缀，及好后缀子串的下标
    //       (例如好后缀是abcd,那么好后缀子串有bcd,cd,d这3个(从右侧开始框选))

    //在坏字符方式中，用于以hash方式快速找到某个坏字符在b串中的最大下标，用于做b串的快速向右滑动
    private static int[] BMInitBadCharTable(char b[]) {
        int[] bcoff = new int[256];                 //只考虑ascii 的256个字符的情况 ,bcoff[ascii] = 该字符在b中最大下标值
        for (int i = 0; i < bcoff.length; i++)
            bcoff[i] = -1;
        for (int i = 0; i < b.length; i++) {
            bcoff[b[i]] = i;
        }
        return bcoff;
    }

    //若b串只有1个字符，那么推荐用BF算法，因为判断效果都只能是一位一位的后移比对，
    //BM代码所需的机器周期应该会比BF算法更多,更耗时，但是稍微复杂一点的情况BM算法效率更高
    public static long BM(char[] a, char[] b) {
        int[] bctab = BMInitBadCharTable(b);        //坏字符缓存初始化，记录b串中每个字符最后出现的位置下标
        BMInitGoodSuffix(b);                        //好后缀缓存初始化，记录b串中每个后缀子串最后出现的位置下标
        int i = 0;                                  //1.以i为B串的头部对齐A串的下标
        int nsubmP1 = a.length - b.length + 1;      //i 指向 遍历a[0,n-m+1]
        while (i < nsubmP1) {
            int j;                                  //指向b串尾部 到 头部
            for (j = b.length - 1; j >= 0; j--) {   //2.从b串尾部开始和a串尾部比较，找到第一个坏字符
                if (b[j] != a[i + j])
                    break;                          //找到了第一个坏字符，在b串中的下标=j
            }
            if (j < 0)                              //j<0表示不存在任何一个坏字符，表示和B串完全匹配成功，直接返回 在a串中的第一个匹配的下标i
                return i;

            int k = b.length - 1 - j;               //获取匹配成功的好后缀长度 ,取值范围[1, b.len-1],至少1个字符 至 最大长度-1 个
            int badOff = j - bctab[a[i + j]];       //获取坏字符向后滑动量
            int goodOff = 0;                        //好后缀默认向后滑动 0

            if (j < b.length - 1)                   //如果有好后缀的话，也就是说坏字符j不是第一个在b串中出现，那么使用好后缀匹配规则
                goodOff = getGoodOff(b.length, j, k);
            int off = Math.max(goodOff, badOff);
            i = i + off;
        }
        return -1;
    }

    //m=b串长度，j=坏字符在b串中下标，k=好后缀匹配长度
    private static int getGoodOff(int m, int j, int k) {
        int goodOff;
        goodOff = m;
        //优先从已匹配好的k位后缀子串中的1位开始找，直到k位，先找prefix是否匹配，
        // 因为prefix匹配的话一次可以让b串往后滑最多的位数
        if (suffix[k] != -1)
            goodOff = j - suffix[k] + 1;
        else {
            for (int r = j + 2; r < k; r++) {       //m-r = 好后缀位子串位数, r=b串长度-好后缀子串位数,从位数多找到位数少
                if (prefix[m - r]) {                // b ----j--|---------------- m长
                    goodOff = r;                    //  \-r-->/ \- 好后缀 k-2 --/  起始应该是-1，再多减了1是因为 prefix是从下标1开始的
                    break;                          //             m-r
                }
            }
        }
        return goodOff;
    }

    static boolean[] prefix;                        //下标k=后缀子串字符个数,k个字符的后缀子串是否出现在B串头部
    static int[] suffix;                            //下标k=后缀子串字符个数,k个字符的后缀子串出现在B串的最靠右（最大）下标
    private static void BMInitGoodSuffix(char[] b) {//初始化 b串的好后缀匹配缓存，类似于 坏字符做缓存的目的，用于加速匹配过程，直接用 k（后缀长度）查找 b串中匹配的好后缀最大下标值
        //初始化                                     //例如 b = abcde
        prefix = new boolean[b.length + 1];         //从[1] 开始
        suffix = new int[b.length + 1];             //[1] = e ,[2] = de, [3] = cde
        for (int i = 0; i < suffix.length; i++) {
            prefix[i] = false;
            suffix[i] = -1;
        }
        int m = b.length;

        //下标为 好后缀的 长度 ，如果 模式串B 为 abcde ，那么当从后往前计算字串长度时 ,
        // [1] = e ,[2] = de, [3] = cde = cde ... [b.length-1] = 整个b串
        // （虽然不会使用到这种情况，因为如果整个b串都被匹配上了，
        // 说明已经完成了字符串匹配，不会再考虑B串向右滑动的情况了）
        for (int i = 0; i < m - 1; i++) {            //b[0,m-1] 作为前缀子串的长度， 每个前缀子串和 与其等长到1长度的 后缀子串比较
            int j = i;                               //指向当前前缀子串的比较字符，从尾部到头部
            int k = 0;                               //当前和前缀子串比较的 尾部子串长度， 从0~j （也就是从1个字符开始比较，直到和当前前缀子串长度一致为止）
            while (j <= 0 && b[j] == b[m - 1 - k]) { //1.如果当前长度的整个前缀子串的每个字符未完全匹配 并且 2.当前前缀子串的单独一个字符和 后缀子串 从右到左挨个比较
                j--;                                 //说明while条件判断成功，前缀子串比较字符 向左移动1个字符，准备下一个字符的比较
                k++;                                 //后缀子串 长度+1，相当于  b[m - 1 - k] 指向的字符 从b串尾向左移动一个，准备比较下一个字符
                suffix[k] = j + 1;                   //当前k长度的后缀子串 最后一次的匹配下标， 若之后也有匹配k长度的后缀子串出现，那么 下标值较大的会覆盖之前的
            }
            if (j < 0)                               //说明当前长度的前缀子串 和同长度的 后缀子串 每个字符完全匹配，那么k长度的后缀子串就被判定为出现在前缀中
                prefix[k] = true;
        }
    }

    public static void main(String[] args) {
        char[] a = "abcdefg".toCharArray();
        char[] b = "def".toCharArray();
//        System.out.println("BF(a,b) " + BF(a, b));
//        System.out.println("RK(a, b) " + RK(a, b));
        System.out.println("BM(a, b) " + BM(a, b));
    }
}
