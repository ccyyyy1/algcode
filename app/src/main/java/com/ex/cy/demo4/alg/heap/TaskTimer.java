package com.ex.cy.demo4.alg.heap;

import java.util.concurrent.atomic.AtomicBoolean;

public class TaskTimer {
    BinHeap priorityQueue = new BinHeap(); //PriorityBlockingQueue

    public interface TaskChangeSubscriber {
        void onGetNextTask(long time);

        void onTask(long time);
    }

    TaskChangeSubscriber taskChangeSubscriber;
    Thread workThread;

    public class TaskCheck implements Runnable {
        AtomicBoolean doRun = new AtomicBoolean(true);

        @Override
        public void run() {
            while (doRun.get()) {
                if (priorityQueue.getCount() > 0) {  //PriorityBlockingQueue
                    try {
                        if (taskChangeSubscriber != null)
                            taskChangeSubscriber.onGetNextTask(priorityQueue.top());
                        Thread.sleep(priorityQueue.top() - System.currentTimeMillis());
                        if (priorityQueue.top() - System.currentTimeMillis() <= 0 && taskChangeSubscriber != null) {
                            taskChangeSubscriber.onTask(priorityQueue.pop());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    TaskCheck taskCheck;

    public TaskTimer() {
    }

    public void start() {
        if (workThread != null) {
            taskCheck.doRun.set(false);
        }
        taskCheck = new TaskCheck();
        workThread = new Thread(taskCheck);
        workThread.start();
    }

    public void stop() {
        if (workThread != null) {
            taskCheck.doRun.set(false);
        }
        taskCheck = null;
        workThread = null;
    }

    public void addTask(long time) {
        System.out.println("addTask : " + time);
        priorityQueue.add(time);
    }
    //取消任务？ 堆的删除算法?

    public TaskChangeSubscriber getTaskChangeSubscriber() {
        return taskChangeSubscriber;
    }

    public void setTaskChangeSubscriber(TaskChangeSubscriber taskChangeSubscriber) {
        this.taskChangeSubscriber = taskChangeSubscriber;
    }

    public static void main(String[] args) throws InterruptedException {
        TaskTimer t = new TaskTimer();
        t.setTaskChangeSubscriber(new TaskChangeSubscriber() {
            @Override
            public void onGetNextTask(long time) {
                System.out.println("TaskTimer.onGetNextTask " + time);
            }

            @Override
            public void onTask(long time) {
                System.out.println("TaskTimer.onTask " + time);
            }
        });
        t.addTask(System.currentTimeMillis() + 3000);
        t.start();

        Thread.sleep(3000*2);
        t.addTask(System.currentTimeMillis() + 3000);

        Thread.sleep(1500);
        t.addTask(System.currentTimeMillis() + 3000);
    }
}
