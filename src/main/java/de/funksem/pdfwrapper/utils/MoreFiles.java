package de.funksem.pdfwrapper.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

public class MoreFiles
{

    public static void del(String fileName)
    {
        if (StringUtils.isBlank(fileName))
        {
            return;
        }

        try
        {
            Files.deleteIfExists(Paths.get(fileName));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String addExtension(final String fileName, final String extension)
    {
        String baseFileName = FilenameUtils.getName(fileName);
        if (baseFileName == null)
        {
            return null;
        }
        return baseFileName + FilenameUtils.EXTENSION_SEPARATOR_STR + extension;
    }

    public static String removeExtension(final String fileName, final String extension)
    {
        if (FilenameUtils.isExtension(fileName, "pdf"))
        {
            return FilenameUtils.removeExtension(fileName);
        }
        return null;
    }

    /**
     * @return renamed {@link File} object, error returns <code>null</code>
     */
    public static File renameFile(String srcFile, String destFile)
    {
        if (StringUtils.isBlank(srcFile) || StringUtils.isBlank(destFile))
        {
            return null;
        }

        File oldfile = new File(srcFile);
        File newfile = new File(destFile);

        if (oldfile.renameTo(newfile))
        {
            return newfile;
        }
        return null;
    }

    public static List<Path> listPdfFiles(Path dir) throws IOException
    {
        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.pdf"))
        {
            for (Path entry : stream)
            {
                result.add(entry);
            }
        }
        catch (DirectoryIteratorException ex)
        {
            // I/O error encounted during the iteration, the cause is an IOException
            throw ex.getCause();
        }
        return result;
    }
}
