package de.funksem.pdfwrapper.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public final class LangUtils
{
    private static final String EXTENSION_TMP = FilenameUtils.EXTENSION_SEPARATOR_STR + "tmp";
    public static final String EXTENSION_PDF = FilenameUtils.EXTENSION_SEPARATOR_STR + "pdf";
    private static final String PDF_HEADER = "%PDF-1.4\n";
    private static final int PDF_HEADER_LEN = PDF_HEADER.length();

    private LangUtils()
    {
    }

    //    public static void main(String[] args) throws IOException
    //    {
    //        System.out.println("Start");
    //        String fileName = "c:/temp/aaa.txt";
    //        createFile(fileName, PDF_HEADER + "KVWL is so great.");
    //        skipBeginData(fileName, PDF_HEADER_LEN);
    //        //            wrapToPDF("c:/work/test-1.zip", "c:/temp");
    //        System.out.println("Done");
    //    }

    public static void wrapToPDF(final String sourceFileName, final String destDirectory) throws IOException
    {
        // neue Datei erzeugen mit initialen String
        String tempFileName = FilenameUtils.concat(destDirectory,
            MoreFiles.addExtension(sourceFileName, "pdf"));
        Path pdfPath = createFile(tempFileName, PDF_HEADER);

        // Kopiere ..
        try (FileChannel inChannel = new FileInputStream(sourceFileName).getChannel();
            FileChannel outChannel = new FileOutputStream(pdfPath.toFile(), true).getChannel())
        {
            outChannel.transferFrom(inChannel, PDF_HEADER_LEN, inChannel.size());
        }
    }

    public static Path createFile(String fileName, String initString) throws IOException
    {
        Path newFile = Paths.get(fileName);
        List<String> lines = new ArrayList<>();
        lines.add(initString);
        Files.write(newFile, lines, Charset.defaultCharset());
        return newFile;
    }

    private static void skipBeginData(final String fileName, final long skipFromBeginning)
        throws FileNotFoundException, IOException
    {
        File sourceFile = MoreFiles.renameFile(fileName, fileName + EXTENSION_TMP);

        // Bytes loeschen und in Datei schreiben
        try (FileChannel inChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel outChannel = new FileOutputStream(fileName, true).getChannel())
        {
            inChannel.position(skipFromBeginning);
            outChannel.transferFrom(inChannel, 0L, inChannel.size() - skipFromBeginning);
        }
        // Quelldatei loeschen
        sourceFile.delete();
    }

    public static void removePDFHeader(final String fileName) throws FileNotFoundException, IOException
    {
        if (StringUtils.isBlank(fileName))
        {
            throw new IllegalArgumentException();
        }
        skipBeginData(fileName, PDF_HEADER_LEN);
    }
}
