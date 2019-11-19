package com.ex.cy.demo4.alg;

public interface ISkipList {
    int getLevel();
    int getCount();
    void add(SkipList.Node n);
    SkipList.Node find(int key);
}
