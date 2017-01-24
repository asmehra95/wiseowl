#/bin/sh
export MEM_ARGS="-Xms512m -Xmx512m"
RUN_CMD="java -cp ../target/WiseOwl-0.0.1-SNAPSHOT.jar com.wiseowl.WiseOwl.indexing.FeedData"
echo $RUN_CMD
exec $RUN_CMD

