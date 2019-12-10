package com.ex.cy.demo4.alg.graph.ewg;

import com.ex.cy.demo4.alg.graph.pub.Edge;

//最小生成树
public interface Mst {
    Iterable<Edge> edges(); //最小生成树的所有的边
    float weight();         //权重
}
