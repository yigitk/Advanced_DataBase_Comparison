#!/bin/bash

set -e

echo "Starting benchmarks"
java -Xmx1024m -jar /neo4j.jar $NUM_EDGES > /output/neo4j_benchmark_outputs_$NUM_EDGES.txt 2>&1
ls -lh > /output/neo4j_db_size_$NUM_EDGES.txt 2>&1
echo "Finished benchmarks"
