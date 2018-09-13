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

# Tenant Id (if value is -1, specified by wizard or silent metadata config)
export TENANT_ID=-1

# import file (if value is 'empty', specified by wizard or silent metadata config)
export FILE=empty

# if silent mode, metadata import config file name (please set your meta-imp-config file)
export META_CONFIG=./../conf/meta-imp-config.properties

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.metadataporting.MetaDataImport

# App Arguments
export APP_ARGS="${EXEC_MODE} ${TENANT_ID} ${FILE} ${LANG}"

# Silent mode metadata config
export META_CONFIG_ARG=meta.config=${META_CONFIG}

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${META_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS}

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo "Please press any key..."

#wait
read wait
fi
