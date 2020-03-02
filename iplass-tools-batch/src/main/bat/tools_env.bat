@echo off

REM ----------------------------------------------------
REM common environmental settings
REM (Depend on the situation, please change values)
REM ----------------------------------------------------

REM config file name (Please set your service-config file name)
set SERVICE_CONFIG_NAME=mtp-service-config.xml

REM config file path (Please set the directory that a resource file is stored)
set MTP_RESOURCE_PATH=.\..\conf

REM library path (Please set the directory that a jar file is stored)
set MTP_LIB_PATH=.\..\lib

REM Language (Please set the language.  system(Java VM Default) or en or ja)
set LANG=system


REM ----------------------------------------------------
REM system settings
REM ----------------------------------------------------

REM class path
set EXEC_CLASS_PATH=.\*;.\..\lib\*;.\..\conf;%MTP_LIB_PATH%\*;%MTP_RESOURCE_PATH%;

REM system config
set SYS_ENV=mtp.config=/%SERVICE_CONFIG_NAME%

REM batch resource language
set LANG_ENV=batch.language=%LANG%
