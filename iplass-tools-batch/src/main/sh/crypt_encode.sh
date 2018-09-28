#!/bin/sh

# ----------------------------------------------------
# include environmental settings
# ----------------------------------------------------
. ./tools_env.sh

# ----------------------------------------------------
# app environmental settings
# ----------------------------------------------------

# Comment out the following settings when not using the property file.
export CRYPT_CONFIG_FILE=/crypt.properties

# ----------------------------------------------------
# app arguments
# (Depend on the situation, please change values)
# ----------------------------------------------------

# To specify the file of the character string to be encrypted, make the following definition effective.
#export FILE_MODE=-file

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

# ----------------------------------------------------
# app settings
# ----------------------------------------------------

# APP Class
export EXEC_APP=org.iplass.mtp.tools.batch.crypt.Encoder

# App Arguments
export APP_ARGS=$FILE_MODE

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
if [ -z ${CRYPT_CONFIG_FILE} ]; then
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} ${EXEC_APP} ${APP_ARGS}
else
java -cp ${EXEC_CLASS_PATH} -D${SYS_ENV} -Dmtp.config.crypt=${CRYPT_CONFIG_FILE} ${EXEC_APP} ${APP_ARGS}
fi

if [ $a -ne 1 ]
then
echo "Please press any key..."

#wait
read wait
fi