package com.ex.cy.demo4.alg.graph.ewdi;

import com.ex.cy.demo4.alg.heap.IndexMinPQ;

import java.util.LinkedList;
import java.util.List;

//A* 算法
// 属于一种启发式搜索算法
// 和Djkstra有相似之处（和BFS)
//不同点在于，将周围顶点放入优先队列后的出队条件，不再是让g(v)（从起点到该点距离，v是被考察的顶点） 最小的先出队列
//而是让 g(v) + h(v) 最小的先出队
//h(v) 为 启发函数给出的值，用来估计v点到终点的估计距离，一般有1.欧几里得距离(sqrt((gx-vx)^2 + (gy-vy)^2)) 2.曼哈顿距离(abs(gx-vx)+abs(gy-vy)) （方格子，笛卡尔坐标系,只能上下左右）
//所以每次优先出队的是最接近到达终点最短距离的点（贪心，并不一定是最优解）
//循环退出条件是：只要遍历到终点则退出循环

public class AStart {

    CoordinateEdgeWeightedDigraph cewd;
    IndexMinPQ<Float> impq;
    ASVertex[] vertices;
    int s;
    int g;
    Edge[] fromEdge;      //记录顶点V是从哪条边探索过来的,除了顶点，每个顶点有一条

    //有向无环图，起点，终点
    public AStart(CoordinateEdgeWeightedDigraph cewd, int s, int g) {
        this.cewd = cewd;
        this.s = s;
        this.g = g;
        vertices = new ASVertex[cewd.v()];
        impq = new IndexMinPQ<>(cewd.v());  //最多有全图顶点，都被放进去过
        fromEdge = new Edge[cewd.v()];
        for (int i = 0; i < cewd.v(); i++) {
            vertices[i] = new ASVertex(i, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        }
        vertices[s].g = 0;
        vertices[s].h = 0;

        astartSearch();
    }

    //启发函数
    //估计从一个点到终点的曼哈顿距离
    public float heuristicFunc(Vertex v1, Vertex v2) {
        //欧几里得距离
//        float dx = v2.x - v1.x;
//        float dy = v2.y - v2.y;
//        float dist = (float) Math.sqrt(dx * dx + dy * dy);
//        return dist;

        //曼哈顿距离(省去求平方，开根号，效率会比较快）
        return Math.abs(v2.x - v1.x) + Math.abs(v2.y - v1.y);
    }

    public void astartSearch() {
        impq.insert(s + 1, 0f); //v顶点索引, g(v)+h(v) 起点到该点距离 + 该点到终点估计距离

        while (!impq.isEmpty()) {
            int v = impq.topIndex() - 1;
            float vdst = impq.delTop();
            Vertex fromV = cewd.getVertex(v);
            vertices[v].h = vdst;

            if (v == g)    //当前已到达终点
                break;     //停止探索

            for (Edge e : cewd.adj(v)) {
                int w = e.other(v);
                if (vertices[w].h != Float.POSITIVE_INFINITY)    //已探索过则跳过
                    continue;

                Vertex toW = cewd.getVertex(w);
                //到下一个顶点w的实际距离
                float gDist = vertices[v].g + e.weight;
                float heuDist = gDist + heuristicFunc(toW, fromV);

                if (impq.contain(w + 1) && impq.get(w + 1) < heuDist)
                    continue;                         //包含到w的边，但距离未缩短，则跳过当前这条边

                //新增或修改
                fromEdge[w] = e;
                vertices[w].g = gDist;
                if (!impq.contain(w + 1)) {               //优先队列中不存在，则直接插入新的顶点
                    impq.insert(w + 1, heuDist);
                } else if (impq.get(w + 1) > heuDist) {   //优先队列中存在，但以当前顶点v作为相邻点距离更小
                    impq.change(w + 1, heuDist);          //更新距离
                }
            }
        }
    }

    //返回从起点到终点的路径的权重总和
    public float getCost() {
        return vertices[g].g;
    }

    //获取从起点到终点的路径
    //并不能像djkstra找到全局最优的最短路径，因为没有遍历其他可能的路径，因为第一次探索到终点就结束循环了
    //但是优点是：找了个一个近似最优解，并用了较少的顶点探索次数，倾向性的往终点方向探索；因此可以用于很大的图中，不必遍历完所有顶点，即可找到一条近似最优解的路径，在时间效率和解的近似最优上达到了一种平衡
    public List<Edge> getEdge() {
        List<Edge> edges = new LinkedList<>();

        int v = g;
        Edge e = fromEdge[v];
        while (e != null) {
            edges.add(0, e);
            v = e.other(v);
            e = fromEdge[v];
        }
        return edges;
    }

    public static void main(String[] a) {
        CoordinateEdgeWeightedDigraph cewd = new CoordinateEdgeWeightedDigraph(6);
        //加入顶点
        //顶点索引， x y 坐标
        cewd.addVertex(0, 0, 0);
        cewd.addVertex(1, 1, 0.5f);
        cewd.addVertex(2, 0, 1);
        cewd.addVertex(3, 1, 1);
        cewd.addVertex(4, 5, 5);
        cewd.addVertex(5, 6, 6);

        //加入有向边，权重为两点之间的欧几里得距离
        cewd.addEdge(0, 1);
        cewd.addEdge(0, 2);
        cewd.addEdge(1, 3);
        cewd.addEdge(2, 3);
        cewd.addEdge(3, 4);

        System.out.println(cewd);

        //测试1
        AStart aStart = new AStart(cewd, 0, 4);
        System.out.println(aStart.getEdge());
        System.out.println("Cost: " + aStart.getCost());

        //测试2 不可达顶点
        AStart aStart2 = new AStart(cewd, 0, 5);
        System.out.println(aStart2.getEdge());
        System.out.println("Cost: " + aStart2.getCost());
    }
}
