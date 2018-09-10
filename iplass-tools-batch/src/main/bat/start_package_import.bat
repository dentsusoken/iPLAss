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

REM Tenant Id (if value is -1, specified by wizard or silent package config)
set TENANT_ID=-1

REM if silent mode, package export config file name (please set your package-imp-config file)
set PACK_CONFIG=./../conf/pack-imp-config.properties

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.pack.PackageImport

REM App Arguments
set APP_ARGS=%EXEC_MODE% %TENANT_ID% %LANG%

REM Silent mode package config
set PACK_CONFIG_ARG=pack.config=%PACK_CONFIG%

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
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -D%PACK_CONFIG_ARG% %EXEC_APP% %APP_ARGS%

if "%EXEC_MODE%" == "SILENT" goto END

pause

:END