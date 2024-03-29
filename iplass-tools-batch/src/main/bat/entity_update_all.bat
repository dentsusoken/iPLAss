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

REM ----------------------------------------------------
REM batch arguments
REM ----------------------------------------------------

REM Entity Name (if value is empty, specified by wizard)
set ENTITY_NAME=%~1

REM Update All Values Str (if value is empty, specified by wizard)
set UPDATE_ALL_VALUES_STR="%~2"

REM Where Clause
set WHERE_CLAUSE="%~3"

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.entity.EntityUpdateAll

REM App Arguments
set APP_ARGS=%EXEC_MODE% %TENANT_ID% %ENTITY_NAME% %UPDATE_ALL_VALUES_STR% %WHERE_CLAUSE%

REM ----------------------------------------------------
REM confirm
REM ----------------------------------------------------

echo
echo execute %EXEC_APP%. (config file is %SERVICE_CONFIG_NAME%)
echo
echo EXEC_CLASS_PATH : %EXEC_CLASS_PATH%
echo SYS_ENV         : %SYS_ENV%
echo

if "%EXEC_MODE%" == "SILENT" goto EXECUTE

pause

REM ----------------------------------------------------
REM execute
REM ----------------------------------------------------

:EXECUTE

REM execute tool
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -D%LANG_ENV% -D%ENTITY_CONFIG_ARG% %EXEC_APP% %APP_ARGS%

if "%EXEC_MODE%" == "SILENT" goto END

pause

:END