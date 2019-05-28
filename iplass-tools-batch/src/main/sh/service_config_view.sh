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

# Mode
#   PARSE_ONLY : Parse only mode
#   PARSE_LOAD : Parse and service loading mode
export MODE=PARSE_ONLY

# Output file name (If not specified, output to the console)
export OUT_FILE=

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.config.ServiceConfigViewer

# App Arguments
export APP_ARGS="-m ${MODE} -o ${OUT_FILE}"

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