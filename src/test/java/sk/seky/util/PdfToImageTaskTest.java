package sk.seky.util;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import sk.seky.pdfconverter.task.PdfToImageTask;
import sk.seky.pdfconverter.task.Task;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class PdfToImageTaskTest {
    @Before
    public void setUp() throws Exception {
        BasicConfigurator.configure();
    }

    @org.junit.Test
    public void testRun() throws Exception {
        Task task = new PdfToImageTask(1, new File("data/paper.pdf"));
        task.run();
        assertEquals("Error", "Done.", task.getStatus());
    }
}
