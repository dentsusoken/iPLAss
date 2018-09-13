@echo off

REM ----------------------------------------------------
REM common environmental settings
REM (Depend on the situation, please change values)
REM ----------------------------------------------------

REM config file name (Please set your service-config file name)
set SERVICE_CONFIG_NAME=mtp-service-config.xml

REM config file path (Please set the directory that a resource file is stored)
set MTP_RESOURCE_PATH=.\..\src\main\resources

REM library path (Please set the directory that a jar file is stored�j
set MTP_LIB_PATH=.\..\WebContent\WEB-INF\lib

REM source build class path (for develop. Please set the pass that compilation class of the source is stored)
set MTP_SOURCE_CLASS_PATH=.\..\build\classes

REM Language (Please set the language.  system(Java VM Default) or en or ja)
set LANG=system


REM ----------------------------------------------------
REM system settings
REM ----------------------------------------------------

REM class path
set EXEC_CLASS_PATH=.\*;.\..\lib\*;.\..\conf;%MTP_LIB_PATH%\*;%MTP_RESOURCE_PATH%;%MTP_SOURCE_CLASS_PATH%;

REM system config
set SYS_ENV=mtp.config=/%SERVICE_CONFIG_NAME%

REM batch resource language
set LANG_ENV=batch.language=%LANG%
