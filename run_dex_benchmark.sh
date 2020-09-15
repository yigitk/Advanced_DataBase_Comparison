#!/bin/bash

set -e

echo "Running small test: 10k nodes"

docker run --rm --name dex-benchmark-10k -e "NUM_EDGES=10000" -it -v $HOME/benchmark_outputs:/output rhololkeolke/dex-benchmark

echo "Running medium test: 100k nodes"

docker run --rm --name dex-benchmark-100k -e "NUM_EDGES=100000" -it -v $HOME/benchmark_outputs:/output rhololkeolke/dex-benchmark

echo "Running large test: 1mil nodes"

docker run --rm --name dex-benchmark-1mil -e "NUM_EDGES=1000000" -it -v $HOME/benchmark_outputs:/output rhololkeolke/dex-benchmark
