:: ---------------------------------------------------------------------
:: JAP COURSE - SCRIPT
:: SCRIPT CST8221 - Fall 2023
:: ---------------------------------------------------------------------
:: Begin of Script (Labs - W24)
:: ---------------------------------------------------------------------

CLS

:: LOCAL VARIABLES ....................................................

:: If your code needs no external libraries, remove all references to LIBDIR
:: in this script.

SET LIBDIR=lib
SET SRCDIR=src
SET BINDIR=bin
SET BINERR=labs-javac.err
SET JARNAME=BattleshipGame.jar
SET JAROUT=labs-jar.out
SET JARERR=labs-jar.err
SET DOCDIR=doc
SET DOCERR=labs-javadoc.err
SET MAINCLASSSRC=%SRCDIR%\test\Main.java
SET MAINCLASSBIN=test.Main

@echo off

ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"
ECHO "@                                                                   @"
ECHO "@                   #       @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@                  ##       @  A L G O N Q U I N  C O L L E G E  @  @"
ECHO "@                ##  #      @    JAVA APPLICATION PROGRAMMING    @  @"
ECHO "@             ###    ##     @         F A L L  -  2 0 2 3        @  @"
ECHO "@          ###    ##        @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  @"
ECHO "@        ###    ##                                                  @"
ECHO "@        ##    ###                 ###                              @"
ECHO "@         ##    ###                ###                              @"
ECHO "@           ##    ##               ###   #####  ##     ##  #####    @"
ECHO "@         (     (      ((((()      ###       ## ###   ###      ##   @"
ECHO "@     ((((     ((((((((     ()     ###   ######  ###  ##   ######   @"
ECHO "@        ((                ()      ###  ##   ##   ## ##   ##   ##   @"
ECHO "@         ((((((((((( ((()         ###   ######    ###     ######   @"
ECHO "@         ((         ((           ###                               @"
ECHO "@          (((((((((((                                              @"
ECHO "@   (((                      ((                                     @"
ECHO "@    ((((((((((((((((((((() ))                                      @"
ECHO "@         ((((((((((((((((()                                        @"
ECHO "@                                                                   @"
ECHO "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"

ECHO "[LABS SCRIPT ---------------------]"

ECHO "1. Compiling ......................"
javac -Xlint -cp ".;%SRCDIR%;%LIBDIR%/*" %MAINCLASSSRC% -d %BINDIR% 2> %BINERR%

ECHO "2. Copying resources .............."
xcopy logo.png %BINDIR% /Y
xcopy hit.png %BINDIR% /Y
xcopy miss.png %BINDIR% /Y
xcopy bow_east.png %BINDIR% /Y
xcopy bow_west.png %BINDIR% /Y
xcopy bow_north.png %BINDIR% /Y
xcopy bow_south.png %BINDIR% /Y
xcopy midhull_horiz.png %BINDIR% /Y
xcopy midhull_vert.png %BINDIR% /Y

ECHO "3. Creating Jar ..................."
cd %BINDIR%
jar cvfe %JARNAME% %MAINCLASSBIN% . > ../%JAROUT% 2> ../%JARERR%

ECHO "4. Creating Javadoc ..............."
cd ..
javadoc -cp ".;%BINDIR%;%LIBDIR%/*" -d %DOCDIR% -sourcepath %SRCDIR% -subpackages . 2> %DOCERR%

cd %BINDIR%
ECHO "5. Running Jar ...................."
start java -jar %JARNAME%
cd ..

ECHO "[END OF SCRIPT -------------------]"
ECHO "                                   "
@echo on

:: ---------------------------------------------------------------------
:: End of Script (Labs - W24)
:: ---------------------------------------------------------------------
-----------------
