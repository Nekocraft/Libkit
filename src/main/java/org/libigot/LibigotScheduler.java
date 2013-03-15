package org.libigot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class LibigotScheduler {

    protected final Map<Integer, LibigotTask> tasks = new HashMap<Integer, LibigotTask>();
    protected final Map<Integer, LibigotTask> asyncTasks = Collections.synchronizedMap(new HashMap<Integer, LibigotTask>());
    protected boolean ticking = false;
    protected final HashMap<Integer, Boolean> toRemove = new HashMap<Integer, Boolean>();
    protected final ArrayList<LibigotTask> toAdd = new ArrayList<LibigotTask>();

    protected LibigotScheduler() {}

    void addTask(LibigotTask task) {
        if(!ticking) {
            add(task, Libigot.getServer().getCurrentTick() + task.delay);
        } else {
            toAdd.add(task);
        }
    }

    void removeTask(int id, boolean async) {
        if(!ticking) {
            remove(id, async);
        } else {
            toRemove.put(id, async);
        }
    }

    private int getFreeId() {
        int id = 1;
        while(this.tasks.containsKey(id) || this.asyncTasks.containsKey(id)) {
            id++;
        }
        return id;
    }

    protected long getEndTick(LibigotTask task) {
        return task.endTick;
    }

    protected long getDelay(LibigotTask task) {
        return task.delay;
    }

    protected void setEndTick(LibigotTask task, long tick) {
        task.endTick = tick;
    }

    protected void setFinished(LibigotTask task) {
        task.finished = true;
        task.id = -1;
    }

    protected void add(LibigotTask task, long endTick) {
        task.id = getFreeId();
        task.endTick = endTick;
        if(!task.async) {
            tasks.put(task.id, task);
        } else {
            asyncTasks.put(task.id, task);
        }
    }

    protected void remove(int id, boolean async) {
        if(!async) {
            this.tasks.remove(id);
        } else {
            this.asyncTasks.remove(id);
        }
    }
}
