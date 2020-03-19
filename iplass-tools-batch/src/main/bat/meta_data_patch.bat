@echo off

REM change current directry (move to bat file place)
cd /D %~dp0

REM ----------------------------------------------------
REM include environmental settings
REM ----------------------------------------------------
call .\tools_env.bat

REM ----------------------------------------------------
REM app arguments
REM (Depend on the situation, please change values)
REM ----------------------------------------------------

REM Execute Mode (WIZARD or SILENT)
set EXEC_MODE=WIZARD

REM Tenant Id (if value is -1, specified by wizard)
set TENANT_ID=-1

REM Old metadata file (if value is '_empty_', specified by wizard)
set OLD_META_DATA_FILE=_empty_

REM New metadata file (if value is '_empty_', specified by wizard)
set NEW_META_DATA_FILE=_empty_

REM User Id [Optional] (if value is '_empty_', no specify)
set USER_ID=_empty_

REM Password [Optional] (if value is '_empty_', no specify)
set PASSWORD=_empty_

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.metadata.MetaDataPatch

REM App Arguments
set APP_ARGS=%EXEC_MODE% %TENANT_ID% %OLD_META_DATA_FILE% %NEW_META_DATA_FILE% %USER_ID% %PASSWORD%

REM ----------------------------------------------------
REM confirm
REM ----------------------------------------------------

echo.
echo execute %EXEC_APP%. (config file is %SERVICE_CONFIG_NAME%)
echo.
echo EXEC_CLASS_PATH : %EXEC_CLASS_PATH%
echo SYS_ENV         : %SYS_ENV%
echo.

if "%EXEC_MODE%" == "SILENT" goto EXECUTE

pause

REM ----------------------------------------------------
REM execute
REM ----------------------------------------------------

:EXECUTE

REM execute tool
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -D%LANG_ENV% %EXEC_APP% %APP_ARGS%

if "%EXEC_MODE%" == "SILENT" goto END

pause

:END
