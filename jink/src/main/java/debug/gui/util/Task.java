package debug.gui.util;

/** Uses a SwingWorker to perform a time-consuming (and utterly fake) task. */

public abstract class Task
{
    protected int lengthOfTask = 0;
    protected int current = 0;
    protected String status = "";

    /**
     * Called from ProgressBarDemo to start the task.
     */
    protected abstract void go();

    /**
     * Called from ProgressBarDemo to find out how much work needs
     * to be done.
     */
    int getLengthOfTask() {
        return lengthOfTask;
    }

    /**
     * Called from ProgressBarDemo to find out how much has been done.
     */
    int getCurrent() {
        return current;
    }

    void stop() {
        current = lengthOfTask;
    }

    /**
     * Called from ProgressBarDemo to find out if the task has completed.
     */
    boolean done() {
        if (current >= lengthOfTask)
            return true;
        else
            return false;
    }

    String getMessage() {
        return status;
    }
}
