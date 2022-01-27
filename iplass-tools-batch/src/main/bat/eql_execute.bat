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

REM Tenant Id
set TENANT_ID=1

REM Search all version (if value true, Search all versions)
set SEARCH_ALL_VERSION=false

REM Execute mode
REM   BATCH    : Batch mode
REM   INTERACT : Interact mode
set EXEC_MODE=BATCH

REM EQL execute mode
REM   ONLY_EXEC          : Only execute
REM   ONLY_COUNT         : Only count
REM   SHOW_SEARCH_RESULT : Show search result
REM   CSV_EXPORT : Export result at csv file
set EQL_EXEC_MODE=ONLY_EXEC

REM User Id [Optional] (if value is '_empty_', no specify)
set USER_ID=_empty_

REM Password [Optional] (if value is '_empty_', no specify)
set PASSWORD=_empty_

REM Export dir [Optional] (if value is '_empty_', no specify)
set EXPORT_DIR=_empty_

REM File name [Optional] (if value is '_empty_', no specify)
set FILE_NAME=_empty_

REM EQL statement (must be enclosed in double quotes)
set EQL=%~1

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.entity.EQLExecutor

REM App Arguments
set APP_ARGS="%EQL%" %TENANT_ID% %SEARCH_ALL_VERSION% %EXEC_MODE% %EQL_EXEC_MODE% %USER_ID% %PASSWORD% %EXPORT_DIR% %FILE_NAME% 

REM ----------------------------------------------------
REM confirm
REM ----------------------------------------------------

echo.
echo execute %EXEC_APP%. (config file is %SERVICE_CONFIG_NAME%)
echo.
echo EXEC_CLASS_PATH : %EXEC_CLASS_PATH%
echo SYS_ENV         : %SYS_ENV%
echo.

pause
echo.

REM ----------------------------------------------------
REM execute
REM ----------------------------------------------------

REM execute tool
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -D%LANG_ENV% %EXEC_APP% %APP_ARGS%

