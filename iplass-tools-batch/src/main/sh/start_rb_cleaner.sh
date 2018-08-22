#!/bin/sh

# ----------------------------------------------------
# include environmental settings
# ----------------------------------------------------
. ./tools_env.sh

# ----------------------------------------------------
# app arguments
# (Depend on the situation, please change values)
# ----------------------------------------------------

# Silent Mode
OPT=`getopt "s" $@`
set -- $OPT

a=0

while [ -- != "$1" ]; do
    case $1 in
        -s)
            a=1
            ;;
    esac
    shift
done

# Tenant Id (if value is -1, all tenant data is target)
export TENANT_ID=-1



# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.cleaner.RbCleaner

# App Arguments
export APP_ARGS="${TENANT_ID}"

# ----------------------------------------------------
# confirm
# ----------------------------------------------------

echo
echo execute ${EXEC_APP}. config file is ${SERVICE_CONFIG_NAME}.
echo
echo EXEC_CLASS_PATH    : ${EXEC_CLASS_PATH}
echo SYS_ENV            : ${SYS_ENV}
if [ $a -ne 1 ]
then
echo "Please press any key..."

#wait
read wait
fi

# ----------------------------------------------------
# execute
# ----------------------------------------------------

# execute tool
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} ${EXEC_APP} ${APP_ARGS}

if [ $a -ne 1 ]
then
echo "Please press any key..."

#wait
read wait
fi