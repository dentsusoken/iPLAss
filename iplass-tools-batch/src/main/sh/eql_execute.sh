#!/bin/sh

# ----------------------------------------------------
# include environmental settings
# ----------------------------------------------------
. ./tools_env.sh

# ----------------------------------------------------
# app arguments
# (Depend on the situation, please change values)
# ----------------------------------------------------

# Tenant Id
export TENANT_ID=1

# User Id
export USER_ID=

# Password
export PASSWORD=

# Search all version (if value true, Search all versions)
export SEARCH_ALL_VERSION=false

# Execute mode
#   BATCH    : Batch mode
#   INTERACT : Interact mode
export EXEC_MODE=BATCH

# EQL execute mode
#   ONLY_EXEC          : Only execute
#   ONLY_COUNT         : Only count
#   SHOW_SEARCH_RESULT : Show search result
export EQL_EXEC_MODE=ONLY_EXEC

# Charset
export CHARSET=UTF-8

# EQL statement (must be enclosed in double quotes)
export EQL="$1"

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.entity.EQLExecutor

# App Arguments
export APP_ARGS="${TENANT_ID} ${SEARCH_ALL_VERSION} ${EXEC_MODE} ${EQL_EXEC_MODE} ${USER_ID} ${PASSWORD}"

# ----------------------------------------------------
# confirm
# ----------------------------------------------------

echo
echo execute ${EXEC_APP}. config file is ${SERVICE_CONFIG_NAME}.
echo
echo EXEC_CLASS_PATH : ${EXEC_CLASS_PATH}
echo SYS_ENV         : ${SYS_ENV}
echo
echo "Please press any key..."

#wait
read wait

# ----------------------------------------------------
# execute
# ----------------------------------------------------

# execute tool
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -Dfile.encoding=${CHARSET} ${EXEC_APP} "${EQL}" ${APP_ARGS}

