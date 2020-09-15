#!/bin/bash

set -e 

echo $FILAMENT_POSTGRES_PORT_5432_TCP_ADDR
echo $FILAMENT_POSTGRES_PORT_5432_TCP_PORT
echo $NUM_EDGES

echo "Starting benchmarks"
java -jar /filament_benchmark_jar/filament_benchmark.jar $FILAMENT_POSTGRES_PORT_5432_TCP_ADDR $FILAMENT_POSTGRES_PORT_5432_TCP_PORT filament /raw_data.nt $NUM_EDGES > /output/benchmark_outputs_$NUM_EDGES.txt 2>&1
echo "Finished benchmarks"
