package org.libigot;


public abstract class LibigotTask implements Runnable {

    boolean finished = false;
    long endTick, delay;
    int id = -1;
    final boolean repeat, async;
    
    /**
     * Delays a none-repeating sync task
     * 
     * @param delay Ticks till execution
     */
    public LibigotTask(int delay) {
        this(delay, false);
    }
    
    /**
     * Delays a sync task
     * 
     * @param delay Ticks till execution
     * @param repeat true if the task should re-delay itself after execution
     */
    public LibigotTask(int delay, boolean repeat) {
        this(delay, repeat, false);
    }
    
    /**
     * Delays a task
     * 
     * @param delay Ticks till execution
     * @param repeat true if the task should re-delay itself after execution
     * @param async true if the task should run asynchronous
     */
    public LibigotTask(int delay, boolean repeat, boolean async) {
        this.delay = delay;
        this.repeat = repeat;
        this.async = async;
    }
    
    /**
     * Get if the task has finished
     * 
     * @return true if the task has finished its execution or was canceled, false if otherwise
     */
    public boolean hasFinished() {
        return this.finished;
    }
    
    /**
     * Start the scheduling
     */
    public void startScheduling() {
        Libigot.scheduler.addTask(this);
    }
    
    /**
     * Cancel the scheduling
     */
    public void cancel() {
        if(!this.finished) {
            Libigot.scheduler.removeTask(this.id, this.async);
        }
    }
    
    /**
     * Get the remaining delay till (next) execution
     * 
     * @return The remaining delay in ticks
     */
    public long getRemainingDelay() {
        return this.endTick - Libigot.getServer().getCurrentTick();
    }
    
    /**
     * Get if the task is repeating
     * 
     * @return True if the task is repeating, false otherwise
     */
    public boolean isRepeating() {
        return this.repeat;
    }
    
    /**
     * Get if the task is async
     * 
     * @return True if the task is running asynchronous, false otherwise
     */
    public boolean isAsync() {
        return this.async;
    }
}
