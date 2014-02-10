@echo off
rem --- Starting PDFWrapper by tha ---

set MODE=unwrap
set SOURCE_DIR=C:\temp

echo Starting PDFWrapper with Mode: %MODE%

pdfwrapper.bat %MODE% -f %SOURCE_DIR%