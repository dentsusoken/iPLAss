#!/bin/sh

# ----------------------------------------------------
# include environmental settings
# ----------------------------------------------------
. ./tools_env.sh

# ----------------------------------------------------
# app arguments
# (Depend on the situation, please change values)
# ----------------------------------------------------

# Execute Mode (WIZARD or SILENT)
export EXEC_MODE=WIZARD

# Tenant Id (if value is -1, specified by wizard or entity config)
export TENANT_ID=-1

# entity export config file name (please set your entity-exp-config file)
export ENTITY_CONFIG=./../conf/entity-exp-config.properties

# ----------------------------------------------------
# shell arguments
# ----------------------------------------------------

# Entity Name (if value is empty, specified by wizard or entity config)
export ENTITY_NAME="$1"

# export directory (if value is empty, specified by wizard or entity config)
export EXPORT_DIR="$2"

# export binary data (if value is empty, specified by wizard or entity config)
export EXPORT_BINARY_DATA="$3"

if [ "${ENTITY_NAME}" = "" ]
then
export EXPORT_DIR=
export EXPORT_BINARY_DATA=
fi

if [ "${EXPORT_DIR}" = "" ]
then
export EXPORT_BINARY_DATA=
fi

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.entity.EntityExport

# App Arguments
export APP_ARGS="${EXEC_MODE} ${TENANT_ID}"

# entity config
export ENTITY_CONFIG_ARG=entity.config=${ENTITY_CONFIG}

# ----------------------------------------------------
# confirm
# ----------------------------------------------------

echo
echo execute ${EXEC_APP}. config file is ${SERVICE_CONFIG_NAME}.
echo
echo EXEC_CLASS_PATH : ${EXEC_CLASS_PATH}
echo SYS_ENV         : ${SYS_ENV}

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo
echo "Please press any key..."

#wait
read wait
fi

# ----------------------------------------------------
# execute
# ----------------------------------------------------

# execute tool
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -D${ENTITY_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS} "${ENTITY_NAME}" "${EXPORT_DIR}" "${EXPORT_BINARY_DATA}"

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo "Please press any key..."

#wait
read wait
fi
