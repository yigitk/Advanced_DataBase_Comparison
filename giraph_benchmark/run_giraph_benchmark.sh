#!/bin/bash

set -e

NUM_WORKERS=$1

echo "Running small dataset: 10k"

time ./convert_data_to_giraph.py dump.nt 10000 10k_data_${NUM_WORKERS}_workers.txt
# check if file already exists. If so delete it
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/input/10k_data_${NUM_WORKERS}_workers.txt); then
	$HADOOP_HOME/bin/hadoop dfs -rm /user/hduser/input/10k_data_${NUM_WORKERS}_workers.txt
fi
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/pagerank_10k_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/pagerank_10k_${NUM_WORKERS}_workers
fi
if $($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/shortestpaths_10k_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/shortestpaths_10k_${NUM_WORKERS}_workers
fi
time $HADOOP_HOME/bin/hadoop dfs -copyFromLocal 10k_data_${NUM_WORKERS}_workers.txt /user/hduser/input/10k_data_${NUM_WORKERS}_workers.txt

echo "Running page rank"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimplePageRankComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/10k_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/pagerank_10k_${NUM_WORKERS}_workers -w $NUM_WORKERS -mc org.apache.giraph.examples.SimplePageRankComputation\$SimplePageRankMasterCompute

echo "Running SSSP"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimpleShortestPathsComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/10k_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/shortestpaths_10k_${NUM_WORKERS}_workers -w $NUM_WORKERS

echo "Running medium dataset: 100k"
time ./convert_data_to_giraph.py dump.nt 100000 100k_data_${NUM_WORKERS}_workers.txt
# check if file already exists. If so delete it
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/input/100k_data_${NUM_WORKERS}_workers.txt); then
	$HADOOP_HOME/bin/hadoop dfs -rm /user/hduser/input/100k_data_${NUM_WORKERS}_workers.txt
fi
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/pagerank_100k_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/pagerank_100k_${NUM_WORKERS}_workers
fi
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/shortestpaths_100k_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/shortestpaths_100k_${NUM_WORKERS}_workers
fi
time $HADOOP_HOME/bin/hadoop dfs -copyFromLocal 100k_data_${NUM_WORKERS}_workers.txt /user/hduser/input/100k_data_${NUM_WORKERS}_workers.txt

echo "Running page rank"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimplePageRankComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/100k_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/pagerank_100k_${NUM_WORKERS}_workers -w $NUM_WORKERS -mc org.apache.giraph.examples.SimplePageRankComputation\$SimplePageRankMasterCompute

echo "Running SSSP"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimpleShortestPathsComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/100k_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/shortestpaths_100k_${NUM_WORKERS}_workers -w $NUM_WORKERS

echo "Running large dataset: 1mil"
time ./convert_data_to_giraph.py dump.nt 1000000 1mil_data_${NUM_WORKERS}_workers.txt
# check if file already exists. If so delete it
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/input/1mil_data_${NUM_WORKERS}_workers.txt); then
	$HADOOP_HOME/bin/hadoop dfs -rm /user/hduser/input/1mil_data_${NUM_WORKERS}_workers.txt
fi
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/pagerank_1mil_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/pagerank_1mil_${NUM_WORKERS}_workers
fi
if ($HADOOP_HOME/bin/hadoop dfs -test -e /user/hduser/output/shortestpaths_1mil_${NUM_WORKERS}_workers); then
	$HADOOP_HOME/bin/hadoop dfs -rmr /user/hduser/output/shortestpaths_1mil_${NUM_WORKERS}_workers
fi
time $HADOOP_HOME/bin/hadoop dfs -copyFromLocal 1mil_data_${NUM_WORKERS}_workers.txt /user/hduser/input/1mil_data_${NUM_WORKERS}_workers.txt

echo "Running page rank"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimplePageRankComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/1mil_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/pagerank_1mil_${NUM_WORKERS}_workers -w $NUM_WORKERS -mc org.apache.giraph.examples.SimplePageRankComputation\$SimplePageRankMasterCompute

echo "Running SSSP"
time $HADOOP_HOME/bin/hadoop jar $GIRAPH_HOME/giraph-examples/target/giraph-examples-1.2.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner org.apache.giraph.examples.SimpleShortestPathsComputation -vif org.apache.giraph.io.formats.JsonLongDoubleFloatDoubleVertexInputFormat -vip /user/hduser/input/1mil_data_${NUM_WORKERS}_workers.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output/shortestpaths_1mil_${NUM_WORKERS}_workers -w $NUM_WORKERS

