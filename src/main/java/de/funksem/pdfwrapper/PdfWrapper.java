package de.funksem.pdfwrapper;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Hello world!
 * 
 */
public class PdfWrapper
{
    private static Options options = new Options();

    //CHECKSTYLE:OFF
    static
    {
        options.addOption("h", "help", false, "Anzeige der Hilfe");
        options.addOption("f", "sourcePdfFile", true, "Unwrap Datei oder Verzeichnis");
        options.addOption("d", "destdir", true, "Zielverzeichnis für die gezippte Datei");
        options.addOption("s", "sourcedir", true, "Quellverzeichnis mit den zu zippenden Dateien");
    }

    // CHEKCSTYLE:ON
    /**
     * ++++++++++++++ M A I N ++++++++++++++
     */
    public static void main(String[] args)
    {
        try
        {
            CommandLine cli = parseCommandLine(args);
            callRightMethod(cli);
        }
        catch (Exception e)
        {
            System.err.println("Unbekannter Fehler - " + e);
            e.printStackTrace();
        }
    }

    private static CommandLine parseCommandLine(String[] args)
    {
        CommandLine cli = null;
        CommandLineParser parser = new PosixParser();
        try
        {
            cli = parser.parse(options, args);
        }
        catch (ParseException e)
        {
            System.err.println("Konnte Kommandozeile nicht parsen: " + e.getMessage());
            System.exit(1);
        }

        if ((cli == null) || cli.hasOption('h') || checkCommandLine(cli))
        {
            showHelpAndExit();
        }
        return cli;
    }

    private static void callRightMethod(CommandLine cli) throws FileNotFoundException, IOException
    {
        Mode mode = getMode(cli);

        System.out.println("Starting with mode = " + mode);
        final String sourceDirectory = cli.getOptionValue('s');
        final String destDirectory = cli.getOptionValue('d');
        final String sourcePdfFileOrDir = cli.getOptionValue('f');
        System.out.println("Quellverzeichnis   = " + sourceDirectory);
        System.out.println("Zielverzeichnis    = " + destDirectory);
        System.out.println("PDF Dateiname      = " + sourcePdfFileOrDir);

        switch (mode)
        {
            case ZIPANDWRAP:
                PdfWrapperExecutor.zipAndWrap(sourceDirectory, destDirectory);
            break;
            case ZIP:
                PdfWrapperExecutor.zip(sourceDirectory, destDirectory);
            break;
            case UNWRAP:
                PdfWrapperExecutor.unwrap(sourcePdfFileOrDir);
            break;
            default:
                System.err.println("Mode wurde nicht erkannt");
            break;
        }
    }

    private static boolean checkCommandLine(CommandLine cli)
    {
        boolean error = false;
        Mode mode = getMode(cli);
        if (mode == null)
        {
            System.err.println("Es wurde kein Modus angegeben");
            error = true;
        }

        if ((mode == Mode.ZIPANDWRAP) || (mode == Mode.ZIP))
        {
            if (!cli.hasOption('d'))
            {
                System.out.println("Es ist kein Zielverzeichnis angegeben");
                error = true;
            }

            if (!cli.hasOption('s'))
            {
                System.out.println("Es ist kein Quellverzeichnis angegeben");
                error = true;
            }
        }

        if (mode == Mode.UNWRAP)
        {
            if (!cli.hasOption('f'))
            {
                System.out.println("Es ist kein Dateiname angegeben");
                error = true;
            }
        }

        return error;
    }

    private static Mode getMode(CommandLine cli)
    {
        String[] args = cli.getArgs();
        if (args.length > 0)
        {
            return Mode.fromString(args[0]);
        }
        return null;
    }

    private static void showHelpAndExit()
    {
        System.out.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("pdfwrapper <MODUS> <OPTIONS>" + Defines.LINE_SEPARATOR, options);
        System.out.println();
        System.exit(0);
    }
}
