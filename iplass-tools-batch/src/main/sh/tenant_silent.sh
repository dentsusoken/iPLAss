#!/bin/sh

# ----------------------------------------------------
# include environmental settings
# ----------------------------------------------------
. ./tools_env.sh

# ----------------------------------------------------
# app arguments
# (Depend on the situation, please change values)
# ----------------------------------------------------

# Execute Mode
export EXEC_MODE=SILENT

# Tenant config file name (please set your tenant-create-config file)
export TENANT_CONFIG=./../conf/tenant-create-config.properties

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.tenant.TenantBatch

# App Arguments
export APP_ARGS="${EXEC_MODE}"

# Silent mode tenant config
export TENANT_CONFIG_ARG=tenant.config=${TENANT_CONFIG}

# ----------------------------------------------------
# confirm
# ----------------------------------------------------

echo
echo execute ${EXEC_APP}. config file is ${SERVICE_CONFIG_NAME}.
echo
echo EXEC_CLASS_PATH : ${EXEC_CLASS_PATH}
echo SYS_ENV         : ${SYS_ENV}
echo

# ----------------------------------------------------
# execute
# ----------------------------------------------------

# execute tool
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -D${LANG_ENV} -D${TENANT_CONFIG_ARG} ${EXEC_APP} ${APP_ARGS}

