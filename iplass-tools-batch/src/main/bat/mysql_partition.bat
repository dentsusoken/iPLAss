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

REM Execute Mode
set EXEC_MODE=CREATE


REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.partition.MySQLPartitionBatch

REM App Arguments
set APP_ARGS=%EXEC_MODE%

REM ----------------------------------------------------
REM confirm
REM ----------------------------------------------------

echo
echo execute %EXEC_APP%. (config file is %SERVICE_CONFIG_NAME%)
echo
echo EXEC_CLASS_PATH : %EXEC_CLASS_PATH%
echo SYS_ENV         : %SYS_ENV%
echo

pause

REM ----------------------------------------------------
REM execute
REM ----------------------------------------------------

REM execute tool
java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -D%LANG_ENV% %EXEC_APP% %APP_ARGS%

pause
