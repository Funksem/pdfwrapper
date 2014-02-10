package de.funksem.pdfwrapper;

enum Mode
{
    ZIP, ZIPANDWRAP, UNWRAP;

    // CHECKSTYLE:OFF
    // Viele Return Anweisungen sind hier ok 
    static Mode fromString(String string)
    {
        switch (string.toLowerCase())
        {
            case "zip":
                return ZIP;
            case "zipandwrap":
                return ZIPANDWRAP;
            case "unwrap":
                return UNWRAP;
            default:
                return null;
        }
    }
    // CHECKSTYLE:ON
}
