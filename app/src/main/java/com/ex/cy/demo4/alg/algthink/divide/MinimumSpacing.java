package com.ex.cy.demo4.alg.algthink.divide;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

public class MinimumSpacing {
    //给定平面上的n个点，求距离最近的两个点的距离。
    //无从下手的话，先分解问题，分解成简单的，逐个分析，然后再合在一起考虑
    //这是个2维的数据，那就先降维到1维分析
    //先考虑在一条数轴上有n个点，求最近距离的2个点的距离
    //
    // ------*--*------*---*--->
    //用分治思想处理
    // 1.分割 2.处理 3.合并 3个步骤
    //
    // 1.分割：
    // 将整个数据[先排序]得到数组s，然后将s从中间一份为二(分割点m下标为, m=(l+r)/2) ,（最小下标+最大下标） / 2，得到左半边s1，和右半边s2
    // ------*--*---|---*---*--->
    //       l      m       r
    //         s1        s2
    //              s
    // 当点的个数n有基数个时，左半边为 s1=s[l,m] ，右半边 为 s2=s(m,r]
    // 例如n=3   l=0 ，r=2   m=2/2 = 1    ,s1 = 0,1 (2个元素）  .s2=2 （1个元素) , s1比s2多一个元素
    // n为偶数时候 = 4
    // m = 0+3/2 = 1 , s1=0,1   s2=2,3  ，s1和s2元素一样多
    //
    // 2.处理：
    // 用递归的方式处理左右两边，先考虑最小情况
    // n = 0 和 n = 1 时
    // 返回 -1 ，代表无法判断2个最近的点是哪两个，因为点的个数不足2个
    // n = 2 时
    // 直接返回 2个点的距离=d
    // n = 3 时
    // 此时不是最小情况，需要继续切分成2份，分治处理，按照1.分割 的方式去分割
    //
    // 3.合并：
    // 从非最小情况开始考虑(n>=3)
    // 因为将s这个数组分割成了2份，每一份会返回该区域的最短距离值（除非不存在，则返回-1，代表该区域只分得0个或1个点，无法判断最小距离）
    // ds1 代表 左侧 这一半返回的最小距离
    // ds2 代表 右侧 这一半返回的最小距离
    // ds 代表当前这个数组应返回的最小距离
    // n=3时
    //         ds
    //        dsgap （处于分割线两段的点的距离）
    //    ds1   (ds2:不存在)
    // ---*---*|---*--->
    //    p2 p1   q1
    //     s1     s2
    //         s
    // 从分治之后的返回值 s1 来看，最短距离的点 只有p1和p2 = d1
    // 而s2 的最短距离为 Integer.MAX_VALUE ，因为只有一个点，所以不存在
    // 那么可能的情况有
    // 1.p1和p2 是最近距离
    // 2.还有一个情况从s数组的分割线的角度考虑，s1未处理，s2也未处理的情况：s1 的最右侧点（p1) 和 s2 的最左侧点(q1) 之间的距离 dsgap，dsgap < d1，那么当前数组s中的最小距离ds = dsgap，否则 ds=d1
    //
    // n=4时
    //         ds
    //        dsgap （处于分割线两段的点的距离）
    //    ds1      ds2
    // ---*---*|--*--*--->
    //    p2 p1   q1 q2
    //     s1     s2
    //         s
    // 先计算 s1 最大序号顶点p1 和 s2 最小序号顶点 q1 之间的距离 dsgap
    // 然后找到 ds1 ，ds2 ， dsgap 最小的值，作为当前数组s 的2点最短距离返回值 ds 并返回
    //
    // 当递归回溯到较高层的时候（n越来越大时）
    // n=5.6.7...更多
    //                  ds
    //                 dsgap （处于分割线两段的点的距离）
    //            ds1      ds2
    // ...--*---*--*---*|--*--*---*--*---->...
    //     p. p3  p2 p1   q1 q2   q3 q4 q.
    //             s1       s2
    //                  s
    // 此时同样需要计算dsgap的值
    // 不用知道s1中 ds1 具体是哪2个顶点之间的最小距离，只需知道ds1是左侧不断分治后，直到最小规模情况时返回的整个 s1 的最短距离
    // 同理 ds2 也是
    // 所以这种情况下找到 s 的 ds 的办法和上面一样
    // 1.根据 s1 的最右侧断点 p1 和 s2 最左侧端点 q1 的距离 dsgap
    // 2.找到 ds1 和 ds2 和 dsgap 的最短距离作为当前s 的最短距离 ds来返回
    //
    // 总结一下
    // 最小规模下
    // n=0，1
    // ds1，ds2 无值，dsgap 无值，ds 为 Integer.MAX_VALUE
    // n=2
    // ds1，ds2 无值，dsgap 无值，ds 为唯一2个点的距离
    // n=3,4,5...
    // ds1，ds2 可能存在有 Integer.MAX_VALUE 的情况（只分得了1一个点的情况）
    // dsgap 有值
    // ds = ds1,ds2,dsgap 的最小值 （Integer.MAX_VALUE 的无效值除外)
    // 代码:

    public static class Point {
        int x;
        int y;

        public Point(int x) {
            this.x = x;
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "{" +
                    "" + x + "," + y +
                    '}';
        }
    }


    private static void ms1D_Demo() {
        //在0~30的范围内，随机生成7个点,并排序
        Point[] points = new Point[7];
        Random r = new Random("996.251.404.go die".hashCode());
        for (int i = 0; i < points.length; i++)
            points[i] = new Point(r.nextInt(30));
        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return lhs.x - rhs.x;
            }
        });
        System.out.println(Arrays.toString(points));

        int ds = minimumSpacing1D(points, 0, points.length - 1);
        System.out.println(ds);
    }

    public static int minimumSpacing1D(Point[] points, int l, int r) {
        //2.处理：处理最小规模
        int len = r - l;
        if (len < 1)                                    //1个点 返回无效值
            return Integer.MAX_VALUE;
        if (len < 2)                                    //2个点 返回这两个点的距离
            return points[r].x - points[l].x;

        //1.分割：此处对3个点及以上处理
        int m = (l + r) >> 1;
        int ds1 = minimumSpacing1D(points, l, m);         //分治左侧
        int ds2 = minimumSpacing1D(points, m + 1, r);  //分治右侧
        //3.合并：找到当前最短距离是多少
        int dsgap = points[m + 1].x - points[m].x;      //计算分割线两边的点距
        int ds = ds1 < ds2 ? ds1 : ds2;
        ds = dsgap < ds ? dsgap : ds;                   //保留最小点距并返回
        return ds;
    }

    //============
    //  考虑2D情况
    //============
    //代码：

    public static void ms2d_Demo() {
        System.out.println("\n======ms2d_Demo======");
        Point[] points = new Point[7];
        Random r = new Random("996.251.404.go die".hashCode());
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(r.nextInt(40), r.nextInt(40));
        }
//        points[0] = new Point(1, 1);
//        points[1] = new Point(1, 5);
//        points[2] = new Point(5, 5);
//        points[3] = new Point(10, 10);
//        points[4] = new Point(11, 11);
//        points[5] = new Point(15, 15);
//        points[6] = new Point(18, 18);

        Arrays.sort(points, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                //先按x排序
                int dx = lhs.x - rhs.x;
                int dy = lhs.y - rhs.y;
                if (dx == 0)
                    return dy;      //x值相同，按照y值小的排在数组靠前
                return dx;          //x值不同，按照x值小的排在数组靠前
            }
        });
        System.out.println(Arrays.toString(points));

        float ds = minimumSpacing2D(points, 0, points.length - 1);
        System.out.println(ds);
    }

    public static float minimumSpacing2D(Point[] points, int l, int r) {
        //2.处理：处理最小规模
        int len = r - l;
        if (len < 1)                                        //1个点 返回无效值
            return Float.POSITIVE_INFINITY;
        if (len < 2) {                                      //2个点 返回这两个点的距离
            float dst = dst2D(points[r], points[l]);
            System.out.println("dst " + dst);
            return dst;
        }

        //1.分割：此处对3个点及以上处理
        int m = (l + r) >> 1;
        float ds1 = minimumSpacing2D(points, l, m);         //分治左侧
        float ds2 = minimumSpacing2D(points, m + 1, r);  //分治右侧
        //3.合并：找到当前最短距离是多少
        float ds = ds1 < ds2 ? ds1 : ds2;

        //根据鸽巢原理，另一边用ds画1个 d*2d的矩形，那么在该矩形内不会超过6个点（如果超过的话，根据ds的定义，会产生矛盾）
        //也就是说一次合并最多用s2内的，以m为轴，以d为距离，在m的右侧，间隔d的地方画一条平行于m的线 p2
        //在p2内任意一点，若存在和[m-ds,m]中有比ds更短距离的匹配，那么不会超过6次匹配
        LinkedList<Point> pointXfromMtods = new LinkedList();
        for (int i = l; i < r; i++) {
            //只对以m:x为中心，+- ds为x范围的点处理,在这个范围内的点，两两比较，试着找到间距小于ds的点对
            if (Math.abs(points[i].x - points[m].x) <= ds)
                pointXfromMtods.add(points[i]);
        }
        //按y值升序排序
        Collections.sort(pointXfromMtods, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                return lhs.y - rhs.y;
            }
        });
        System.out.println("pointXfromMtods " + pointXfromMtods);

        for (int i = 0; i < pointXfromMtods.size(); i++) { //找，的x或y 之间距离小于ds的点对
            for (int j = i + 1; j < pointXfromMtods.size(); j++) {
                int dy = Math.abs(pointXfromMtods.get(j).y - pointXfromMtods.get(i).y);
                if (dy > ds)
                    break;              //若发现一个和[i]点的y值 之差 > ds 的，说明已经超出了范围，进入下一个[i]的遍历检测
                int dx = Math.abs(pointXfromMtods.get(j).x - pointXfromMtods.get(i).x);
                float dsgap = (float) Math.sqrt(dx * dx + dy * dy);
                System.out.println("dsgap " + ds);
                ds = ds < dsgap ? ds : dsgap;
            }
        }

        System.out.println("ds " + ds);
        return ds;
    }


    //返回2d空间中 a，b两点的距离
    public static float dst2D(Point a, Point b) {
        int xx = a.x - b.x;
        xx *= xx;
        int yy = a.y - b.y;
        yy *= yy;
        return (float) Math.sqrt(xx + yy);
    }

    public static float dst(int x, int y) {
        return (float) Math.sqrt(x * x + y * y);
    }

    public static void main(String[] ar) {
        ms1D_Demo();
        ms2d_Demo();
    }
}
