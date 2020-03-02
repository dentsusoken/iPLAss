#!/bin/sh

# ----------------------------------------------------
# common environmental settings
# (Depend on the situation, please change values)
# ----------------------------------------------------

# config file name (Please set your service-config file name)
export SERVICE_CONFIG_NAME=mtp-service-config.xml

# config file path (Please set the directory that a resource file is stored)
export MTP_RESOURCE_PATH=./../conf

# library path (Please set the directory that a jar file is stored）
export MTP_LIB_PATH=./../lib

# language (Please set the language.  system(Java VM Default) or en or ja)
export LANG=system



# ----------------------------------------------------
# system settings
# ----------------------------------------------------

# class path
export EXEC_CLASS_PATH="./*:./../lib/*:./../conf:${MTP_LIB_PATH}/*:${MTP_RESOURCE_PATH}"

# system config
export SYS_ENV=mtp.config=/${SERVICE_CONFIG_NAME}

# batch resource language
export LANG_ENV=batch.language=${LANG}
