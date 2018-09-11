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

# Tenant Id (if value is -1, specified by wizard or silent package config)
export TENANT_ID=-1

# if silent mode, package export config file name (please set your package-exp-config file)
export PACK_CONFIG=./../conf/pack-exp-config.properties

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.pack.PackageExport

# App Arguments
export APP_ARGS="${EXEC_MODE} ${TENANT_ID} ${LANG}"

# Silent mode package config
export PACK_CONFIG_ARG=pack.config=${PACK_CONFIG}

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${PACK_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS}

if [ "${EXEC_MODE}" = "WIZARD" ]
then

echo "Please press any key..."

#wait
read wait

fi
