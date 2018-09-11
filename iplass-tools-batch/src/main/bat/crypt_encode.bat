@echo off

REM change current directry (move to bat file place)
cd /D %~dp0

REM ----------------------------------------------------
REM include environmental settings
REM ----------------------------------------------------
call .\tools_env.bat

REM ----------------------------------------------------
REM app environmental settings
REM ----------------------------------------------------

REM Comment out the following settings when not using the property file.
set CRYPT_CONFIG_FILE=/crypt.properties

REM ----------------------------------------------------
REM app arguments
REM (Depend on the situation, please change values)
REM ----------------------------------------------------

REM To specify the file of the character string to be encrypted, make the following settings effective.
REM set FILE_MODE=-file

REM ----------------------------------------------------
REM app settings
REM ----------------------------------------------------

REM APP class
set EXEC_APP=org.iplass.mtp.tools.batch.crypt.Encoder

REM App Arguments
set APP_ARGS=%FILE_MODE%

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
if defined CRYPT_CONFIG_FILE (
	java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% -Dmtp.config.crypt=%CRYPT_CONFIG_FILE% %EXEC_APP% %APP_ARGS%
) else (
	java -cp %EXEC_CLASS_PATH% -D%SYS_ENV% %EXEC_APP% %APP_ARGS%
)

REM pause
