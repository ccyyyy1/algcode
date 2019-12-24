package com.ex.cy.demo4.alg.graph.ewdi;

import android.support.annotation.NonNull;

public class Edge implements Comparable<Edge> {
    public int v;
    public int w;
    public float weight;
    public String name;

    //from - to
    public Edge(int v, int w, float weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public Edge(int v, int w, float weight, String name) {
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.name = name;
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
