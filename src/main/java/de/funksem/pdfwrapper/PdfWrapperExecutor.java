package de.funksem.pdfwrapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import de.funksem.pdfwrapper.utils.LangUtils;
import de.funksem.pdfwrapper.utils.MoreFiles;
import de.funksem.pdfwrapper.utils.ZipUtils;

public final class PdfWrapperExecutor
{
    private static final String DATE_PATTERN = "yyyy-MM-dd__HH-mm-ss";
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN, Locale.US);

    private PdfWrapperExecutor()
    {
    }

    public static void zipAndWrap(final String sourceDirectory, final String destDirectory)
    {
        if (StringUtils.isBlank(sourceDirectory))
        {
            throw new IllegalArgumentException();
        }

        String tempFile = null;
        try
        {
            tempFile = FilenameUtils.concat(Defines.SYSTEM_TEMP_DIR, getZipFileName());
            ZipUtils.zip(tempFile, sourceDirectory);
            LangUtils.wrapToPDF(tempFile, destDirectory);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            MoreFiles.del(tempFile);
        }
    }

    public static void unwrap(final String sourcePdfFile) throws FileNotFoundException, IOException
    {
        if (StringUtils.isBlank(sourcePdfFile))
        {
            throw new IllegalArgumentException();
        }

        Path sourcePath = Paths.get(sourcePdfFile);
        if (Files.exists(sourcePath))
        {
            List<Path> listPdfFiles = null;

            if (Files.isDirectory(sourcePath))
            {
                listPdfFiles = MoreFiles.listPdfFiles(sourcePath);
            }
            else if (Files.isRegularFile(sourcePath))
            {
                listPdfFiles = new ArrayList<>(Arrays.asList(sourcePath));
            }
            else
            {
                System.err.println("Verzeichnis oder Datei existiert nicht: " + sourcePdfFile);
                return;
            }

            for (Path path : listPdfFiles)
            {
                System.out.println("Unwrap .. " + path.getFileName());
                LangUtils.removePDFHeader(path.toString());
                MoreFiles.renameFile(path.toString(), MoreFiles.removeExtension(path.toString(), "pdf"));
            }
        }
    }

    public static void zip(final String sourceDirectory, final String destDirectory)
    {
        if (StringUtils.isBlank(sourceDirectory))
        {
            throw new IllegalArgumentException();
        }
        try
        {
            String zipFile = FilenameUtils.concat(destDirectory, getZipFileName());
            ZipUtils.zip(zipFile, sourceDirectory);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String getZipFileName()
    {
        return DATE_FORMAT.format(new Date()) + "_pdfwrapper" + FilenameUtils.EXTENSION_SEPARATOR_STR + "zip";
    }
}
