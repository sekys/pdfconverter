
package sk.seky.pdfconverter.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Trieda sa o manazovanie thread poolu.
 *
 * @author Lukas Sekerak
 */
public final class MyThreadPoolExecutor extends ThreadPoolExecutor {

    public MyThreadPoolExecutor(int CPUScaleFactor, int bufferCapacity) {
        super(MaxThreads() * CPUScaleFactor,
            MaxThreads() * CPUScaleFactor,
            1,
            TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(bufferCapacity, false),
            new ThreadPoolExecutor.CallerRunsPolicy());
    }

    protected static int MaxThreads() {
        return Runtime.getRuntime().availableProcessors();
    }
}
