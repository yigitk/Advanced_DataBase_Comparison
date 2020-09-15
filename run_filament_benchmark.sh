#!/bin/bash

set -e

echo "Running small test: 10K nodes"

./filament_postgres_container.sh restart
sleep 10 # give the database time to start up
docker run --rm --name filament-benchmark-10k -e "NUM_EDGES=10000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres rhololkeolke/filament-benchmark
echo "Checking db size"
docker run --rm --name filament-db-size-10k -e "NUM_EDGES=10000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres postgres:9.3 /bin/bash -c 'psql -h $FILAMENT_POSTGRES_PORT_5432_TCP_ADDR -p $FILAMENT_POSTGRES_PORT_5432_TCP_PORT filament filament -c "select t1.datname AS db_name,  pg_size_pretty(pg_database_size(t1.datname)) as db_size from pg_database as t1 WHERE t1.datname = '"'"'filament'"'"';" > /output/db_size_$NUM_EDGES.txt 2>&1'
echo "finished checking db size"


echo "Running medium test: 100k"

./filament_postgres_container.sh restart
sleep 10 # give the database time to start up
docker run --rm --name filament-benchmark-100k -e "NUM_EDGES=100000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres rhololkeolke/filament-benchmark
echo "Checking db size"
docker run --rm --name filament-db-size-100k -e "NUM_EDGES=100000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres postgres:9.3 /bin/bash -c 'psql -h $FILAMENT_POSTGRES_PORT_5432_TCP_ADDR -p $FILAMENT_POSTGRES_PORT_5432_TCP_PORT filament filament -c "select t1.datname AS db_name,  pg_size_pretty(pg_database_size(t1.datname)) as db_size from pg_database as t1 WHERE t1.datname = '"'"'filament'"'"';" > /output/db_size_$NUM_EDGES.txt 2>&1'
echo "finished checking db size"


echo "Running full test: 1 million"
./filament_postgres_container.sh restart
sleep 10 # give the database time to start up
docker run --rm --name filament-benchmark-1mil -e "NUM_EDGES=1000000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres rhololkeolke/filament-benchmark
echo "Checking db size"
docker run --rm --name filament-db-size-1mil -e "NUM_EDGES=1000000" -it -v $HOME/benchmark_outputs:/output --link filament-postgres:filament_postgres postgres:9.3 /bin/bash -c 'psql -h $FILAMENT_POSTGRES_PORT_5432_TCP_ADDR -p $FILAMENT_POSTGRES_PORT_5432_TCP_PORT filament filament -c "select t1.datname AS db_name,  pg_size_pretty(pg_database_size(t1.datname)) as db_size from pg_database as t1 WHERE t1.datname = '"'"'filament'"'"';" > /output/db_size_$NUM_EDGES.txt 2>&1'
echo "finished checking db size"
