package com.zhengguoqiang.forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * @author zhengguoqiang
 */
public class RecursiveActionExample extends RecursiveAction {
    private String workload = "";
    private static final int THRESHOLD = 4;

    public RecursiveActionExample(String workload) {
        this.workload = workload;
    }

    @Override
    protected void compute() {
        //判断是否满足最小任务粒度
        if (workload.length() > THRESHOLD) {
            //不满足，继续拆解任务
            ForkJoinTask.invokeAll(createSubtasks());
        } else {
            processing(workload);
        }
    }

    /**
     * breaking the task into smaller independent subtasks
     *
     * @return
     */
    private List<RecursiveActionExample> createSubtasks() {
        List<RecursiveActionExample> subtasks = new ArrayList<>();
        String partOne = workload.substring(0, workload.length() / 2);
        String partTwo = workload.substring(workload.length() / 2);
        subtasks.add(new RecursiveActionExample(partOne));
        subtasks.add(new RecursiveActionExample(partTwo));
        return subtasks;
    }

    private void processing(String work) {
        String result = work.toUpperCase();
        System.out.println(("This result - (" + result + ") - was processed by " +
                Thread.currentThread().getName()));
    }
}
