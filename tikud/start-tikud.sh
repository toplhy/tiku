#!/bin/sh

start(){
    cd /data/war/
    pkill -9 -f "tikud.war"
    sleep 2
    nohup java -jar tikud.war >> /data/war/nohup-tikud.out 2>&1 &
    echo "start tikud success"
}

stop(){
    pkill -9 -f "tikud.war"
    echo "stop tikud success"
}


case $1 in
   start)
      start
   ;;
   stop)
      stop
   ;;
   *)
      echo "Usage: {start|stop}"
   ;;
esac

exit 0