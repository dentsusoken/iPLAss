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

# ----------------------------------------------------
# shell arguments
# ----------------------------------------------------

# Entity Name (if value is empty, specified by wizard)
export ENTITY_NAME="$1"

# Where Clause
export WHERE_CLAUSE="$2"

# notifyListeners
export NOTIFY_LISTENERS="$3"

# commitLimit
export COMMIT_LIMIT="$4"

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.entity.EntityDeleteAll

# App Arguments
export APP_ARGS="${EXEC_MODE} ${TENANT_ID}"

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
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -D${ENTITY_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS} "${ENTITY_NAME}" "${WHERE_CLAUSE}" "${NOTIFY_LISTENERS}" "${COMMIT_LIMIT}"

if [ "${EXEC_MODE}" = "WIZARD" ]
then
echo "Please press any key..."

#wait
read wait
fi
