#!/bin/bash

set -e

echo "Starting benchmarks"
java -Xmx1024m -jar /dex.jar $NUM_EDGES > /output/dex_benchmark_outputs_$NUM_EDGES.txt 2>&1
ls -lh > /output/dex_db_size_$NUM_EDGES.txt 2>&1
echo "Finished benchmarks"
