package com.ex.cy.demo4.alg.disjoint;

//不相交集ADT (抽象数据类型）并查集（不相交集合）
//一般用于集合运算，但有限制
//用树，这种结构组成，有多个树（=森林）
//属于同一颗数的元素，表示处于同一个集合中
//主要支持2个操作.
//1. Find操作，找到给定元素所属的集合编号
//2. Union操作，给出2个元素，将他们纳入同一个集合中
//常见使用场景：元素a和b是否属于同一集合?
//find(a) == find(b) ：true 表示a和b属于同一集合
//但注意：因为是不相交集，所以一个元素在同一时刻，只能最多属于一个集合中，不能存在同时属于两个集合中的情况（因为这是存在相交集的情况）
public class Disjoint {
    //    int setSize;//集合个数
    //
    //正数：存储下标作为所属于的集合，下标是元素编号，也是集合编号,某2个元素是否属于相同集合是指这2个元素所指向的最终集合编号是否一致
    //负数：表示当前集合树的深度,-1表示树深度为1,该元素只属于自己这个集合,用于 按高度求并
    int[] set;

    public Disjoint(int setSize) { //集合大小
//        this.setSize = setSize;
        set = new int[setSize];
        for (int i = 0; i < setSize; i++)
            set[i] = -1; //-1表示单独属于一个集合
    }


    //查找元素所属集合的编号
    public int find(int elementType) {
        if (check(elementType)) return elementType;

        if (set[elementType] <= 0)                  //集合树根：该元素不和其他元素没有处于同一集合，返回该元素本身作为集合类型
            return elementType;
        //set[elementType] = find(set[elementType]) 是为了压缩路径(减少深度)，每次查找时，将深度过深的树，顺着所属路径给扁平化，节点直接指向树根
        return set[elementType] = find(set[elementType]);  //集合子树：顺着该元素所属的集合类型 作为元素类型，递归的找到树根（最终所属的用作该集合统一标识的 集合类型）
    }

    //对2个元素求并
    // setRoot2 -> setRoot1
    public void union(int setRoot1, int setRoot2) {
        if (check(setRoot1) || check(setRoot2)) return;
        //求并方式有3种
        //1. 普通求并方式                        ，缺点是树深可能会退化成单链表 N
//        set[setRoot2] = setRoot1;
        //2. 按大小求并
        //3. 按高度求并                          ，保证所有树的最大深度最多为 logN
        //树根节点挂载原则:每次求并2个树的时候，把深度小的树接入深度大的树，当2个树深度一样的时候，作为根的树深度+1
        if (set[setRoot1] < set[setRoot2])      //root1 更深
            set[setRoot2] = setRoot1;           //将浅树 root2的根指向root1
        else {
            if (set[setRoot1] == set[setRoot2])
                set[setRoot2]--;                //两树一样深，让root2 深度加1，接下来让root1指向root2
            set[setRoot1] = setRoot2;           //root2更深，将root1 指向 root2
        }
    }

    public boolean isSameUnion(int a, int b) {
        if (check(a) || check(b)) return false;
        return find(a) == find(b);
    }


    private boolean check(int setRoot2) {
        return setRoot2 > set.length || setRoot2 < 0;
    }

    public static void main(String[] a) {
        Disjoint disjoint = new Disjoint(5);
        int 张三 = 0;        //嗯，java默认支持Unicode作为源码中的字符集
        int 李四 = 1;
        int 赵家六 = 2;
        int 热爱大自然一类人 = 3;
        int 热爱老大哥一类人 = 4;
        disjoint.union(热爱大自然一类人, 张三);
        disjoint.union(热爱老大哥一类人, 赵家六);
        disjoint.union(赵家六, 李四);

        System.out.println("张三 和 李四 是一类人？" + disjoint.isSameUnion(张三, 李四));
        System.out.println("张三 是 热爱大自然一类人？" + disjoint.isSameUnion(张三, 热爱大自然一类人));
        System.out.println("李四 是 热爱大自然一类人？" + disjoint.isSameUnion(李四, 热爱大自然一类人));
        System.out.println("李四 是 热爱老大哥一类人？" + disjoint.isSameUnion(李四, 热爱老大哥一类人));
        System.out.println("赵家六 是 热爱老大哥一类人？" + disjoint.isSameUnion(赵家六, 热爱老大哥一类人));
        System.out.println("赵家六 和 李四 是一类人？" + disjoint.isSameUnion(赵家六, 李四));
    }
}
