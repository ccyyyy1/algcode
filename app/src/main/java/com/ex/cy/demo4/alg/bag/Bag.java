package com.ex.cy.demo4.alg.bag;

public class Bag<T extends BaseItem> {
    T head;
    T tail;

    public void add(T t){
        if(tail != null)
            tail.next = t;
        else{
            head = t;
            tail = t;
        }
    }

    public void del(T t){

    }
}
