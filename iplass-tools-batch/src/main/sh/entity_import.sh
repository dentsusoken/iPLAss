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

# entity import config file name (please set your entity-imp-config file)
export ENTITY_CONFIG=./../conf/entity-imp-config.properties

# ----------------------------------------------------
# shell arguments
# ----------------------------------------------------

# Entity Name (if value is empty, specified by wizard or entity config)
export ENTITY_NAME="$1"

# import file (if value is empty, specified by wizard or entity config)
export FILE="$2"

# import binary data (if value is empty, specified by wizard or entity config)
export IMORT_BINARY_DATA="$3"

if [ "${ENTITY_NAME}" = "" ]
then
export FILE=
export IMPORT_BINARY_DATA=
fi

if [ "${FILE}" = "" ]
then
export IMPORT_BINARY_DATA=
fi

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.entity.EntityImport

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -D${ENTITY_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS} "${ENTITY_NAME}" "${FILE}"

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo "Please press any key..."

#wait
read wait
fi
