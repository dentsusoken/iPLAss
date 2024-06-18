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

# if silent mode, metadata export config file name (please set your meta-exp-config file)
export META_CONFIG=./../conf/meta-exp-rdb-to-file-config.properties

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.metadata.MetaDataExportRdbToFile

# App Arguments
export APP_ARGS="${EXEC_MODE}"

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -D${META_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS}

if [ "${EXEC_MODE}" = "WIZARD" ]
then

echo "Please press any key..."

#wait
read wait

fi
