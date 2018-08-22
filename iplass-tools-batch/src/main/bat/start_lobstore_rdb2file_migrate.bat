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

REM Tenant Id (if value is -1, all tenant data is target)
set TENANT_ID=-1

REM Root Directory
set ROOT_DIR=D:\tmp\fileLobStore

REM Migrate Target ("ALL" is Binary and LongText, "BINARY" is Binary only, "LONGTEXT" is LongText only)
set MIGRATE_TARGET=ALL

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.lob.LobStoreMigrator

REM App Arguments
set APP_ARGS=R2F %MIGRATE_TARGET% %ROOT_DIR% %TENANT_ID%

REM ----------------------------------------------------
REM confirm
REM ----------------------------------------------------

echo
echo execute %EXEC_APP%. (config file is %SERVICE_CONFIG_NAME%)
echo
echo EXEC_CLASS_PATH : %EXEC_CLASS_PATH%
echo SYS_ENV         : %SYS_ENV%
echo

REM pause

REM ----------------------------------------------------
REM execute
REM ----------------------------------------------------

REM execute tool
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% %EXEC_APP% %APP_ARGS%

REM pause
