package com.abledenthusiast.emento.scheduling;


import com.abledenthusiast.emento.scheduling.tasks.ExecutionTask;

public interface Scheduler {
    boolean schedule(ExecutionTask task);
}
