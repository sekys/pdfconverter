package sk.seky.pdfconverter.task;

import org.apache.log4j.Logger;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Seky on 25. 4. 2015.
 * Uloha pre konvertovanie PDF do ZIP archivu, ktory obsahuje obrazky.
 * Obrazky stran su pomenovane 1.png,
 */
public final class PdfToImageTask extends Task {
    private static final Logger LOGGER = Logger.getLogger(PdfToImageTask.class.getName());
    private final File file;

    public PdfToImageTask(int id, File file) {
        super(id);
        this.file = file;
    }

    private static File outputFilePath(File name) {
        return new File(name.getAbsolutePath() + ".zip");
    }

    /**
     * Process document, convert it to the set of images.
     *
     * @param doc PDF document
     * @param zos output stream
     * @throws IOException
     */
    private void processDocument(PDDocument doc, ZipOutputStream zos) throws IOException {
        List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
        int index = 0;
        for (PDPage page : pages) {
            index++;
            String zipFileName = Integer.toString(index) + ".png";
            zos.putNextEntry(new ZipEntry(zipFileName));
            ImageIO.write(page.convertToImage(BufferedImage.TYPE_INT_RGB, 300), "png", zos);
            zos.closeEntry();
            setStatus("Processed " + zipFileName + " of document '" + file.getName());
        }
        setStatus("Done.");
    }

    @Override
    public void run() {
        ZipOutputStream zos = null;
        PDDocument document = null;
        try {
            // Open input stream
            try {
                document = PDDocument.load(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                setStatus("Cannot open requested file.");
                return;
            }

            // Open output stream
            try {
                zos = new ZipOutputStream(new FileOutputStream(outputFilePath(file)));
            } catch (FileNotFoundException e) {
                setStatus("Cannot create new file.");
                return;
            }

            // Process document
            processDocument(document, zos);
        } catch (IOException e) {
            String errorMsg = "Error at processing file.";
            setStatus(errorMsg);
            LOGGER.error(errorMsg, e);
        } finally {
            // Uvolni pouzite resources
            IOUtils.closeQuietly(zos);
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
