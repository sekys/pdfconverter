package sk.seky.pdfconverter.controller;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import sk.seky.pdfconverter.task.PdfToImageTask;
import sk.seky.pdfconverter.task.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Seky on 25. 4. 2015.
 * Aplikacia na prerobenie PDF suboru na mnozinu obrazkov, ktore su zabalene v ZIP archive.
 */
public final class PdfConverter {
    private static final Logger LOGGER = Logger.getLogger(PdfConverter.class.getName());
    private final MyThreadPoolExecutor executor;
    private int lastId;

    public PdfConverter() {
        executor = new MyThreadPoolExecutor(1, 100);
        lastId = 0;
    }

    public void addFiles(String[] args) {
        // Pridaj Task do thread poolu a zoznamu uloh pre sledovanie plnenia
        List<Task> jobs = new ArrayList<Task>(args.length);
        for (String arg : args) {
            lastId++;
            Task task = new PdfToImageTask(lastId, new File(arg));
            jobs.add(task);
            executor.submit(task);
        }

        // Kazdu 2 sekundu vypis status zadanych uloh
        executor.shutdown();
        while (!executor.isTerminated()) {
            System.out.println("Completed tasks " + executor.getCompletedTaskCount());
            for (Task job : jobs) {
                System.out.println("Job " + job.getId() + " status: " + job.getStatus());
            }
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
                LOGGER.error("sleep", e);
            }
        }


    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        PdfConverter converter = new PdfConverter();
        converter.addFiles(args);
    }
}
