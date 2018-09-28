#!/bin/sh

# ----------------------------------------------------
# common environmental settings
# (Depend on the situation, please change values)
# ----------------------------------------------------

# config file name (Please set your service-config file name)
export SERVICE_CONFIG_NAME=mtp-service-config.xml

# WebApp HOME directory
export WEB_APP_HOME=/app/tomcat6/webapps

# WebApp Name
export WEB_APP_NAME=xxx



# config file path (Please set the directory that a resource file is stored)
export MTP_RESOURCE_PATH=${WEB_APP_HOME}/${WEB_APP_NAME}/WEB-INF/classes

# library path (Please set the directory that a jar file is stored）
export MTP_LIB_PATH=${WEB_APP_HOME}/${WEB_APP_NAME}/WEB-INF/lib

# source build class path (for develop. Please set the pass that compilation class of the source is stored)
export MTP_SOURCE_CLASS_PATH=

# language (Please set the language.  system(Java VM Default) or en or ja)
export LANG=system



# ----------------------------------------------------
# system settings
# ----------------------------------------------------

# class path
export EXEC_CLASS_PATH="./*:./../lib/*:./../conf:${MTP_LIB_PATH}/*:${MTP_RESOURCE_PATH}:${MTP_SOURCE_CLASS_PATH}"

# system config
export SYS_ENV=mtp.config=/${SERVICE_CONFIG_NAME}

# batch resource language
export LANG_ENV=batch.language=${LANG}
