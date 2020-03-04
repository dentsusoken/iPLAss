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

# Tenant Id (if value is -1, specified by wizard)
export TENANT_ID=-1

# Old metadata file (if value is '_empty_', specified by wizard)
export OLD_META_DATA_FILE=_empty_

# New metadata file (if value is '_empty_', specified by wizard)
export NEW_META_DATA_FILE=_empty_

# User Id [Optional] (if value is '_empty_', no specify)
export USER_ID=_empty_

# Password [Optional] (if value is '_empty_', no specify)
export PASSWORD=_empty_

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.metadata.MetaDataPatch

# App Arguments
export APP_ARGS="${EXEC_MODE} ${TENANT_ID} ${OLD_META_DATA_FILE} ${NEW_META_DATA_FILE} ${USER_ID} ${PASSWORD}"

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} ${EXEC_APP} ${APP_ARGS}

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo "Please press any key..."

#wait
read wait
fi
