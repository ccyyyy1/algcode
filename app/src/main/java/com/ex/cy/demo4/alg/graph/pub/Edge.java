package com.ex.cy.demo4.alg.graph.pub;

import android.support.annotation.NonNull;

public class Edge extends Vertex implements Comparable<Edge> {
    public int w;
    public float weight;

    //from - to
    public Edge(int v, int w, float weight) {
        super(v);
        this.w = w;
        this.weight = weight;
    }

    public float weight() {
        return weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == this.v) return w;
        else if (vertex == this.w) return v;

        throw new RuntimeException("no such vertex " + vertex + " ,[v:" + v + " w:" + w + "]");
    }

    @Override
    public int compareTo(@NonNull Edge another) {
        if (weight > another.weight)
            return 1;
        else if (weight < another.weight)
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }
}
