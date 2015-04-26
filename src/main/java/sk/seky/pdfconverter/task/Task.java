package sk.seky.pdfconverter.task;

import org.junit.runner.notification.RunListener;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Seky on 25. 4. 2015.
 * Abstraktna trieda reprezentujuca Job.
 */
@RunListener.ThreadSafe
public abstract class Task implements Runnable {
    private final int id;  // unikatne id ulohy
    private final ReadWriteLock lock = new ReentrantReadWriteLock(); // lock pre status
    private String status;  // status ulohy

    /**
     * Vytvor task.
     * @param id UID tasku
     */
    protected Task(int id) {
        this.id = id;
        status = "Created task.";
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        String temp;
        try {
            lock.readLock().lock();
            temp = status;
        } finally {
            lock.readLock().unlock();
        }
        return temp;
    }

    protected void setStatus(String status) {
        try {
            lock.writeLock().lock();
            this.status = status;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
