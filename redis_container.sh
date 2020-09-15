#!/bin/bash

set -e

if [ "$#" -lt 1 ]; then
	>&2 echo "ERROR: No option provided. Valid options are (status|stop|start|restart|kill|delete)"
	exit 1
fi

# ${1,,} converts argument 1 to lowercase
option=${1,,}
if [[ $option == "status" ]]; then

	if docker ps -a | grep redis-graph &> /dev/null ; then
		if docker ps -a -f status=exited | grep redis-graph &> /dev/null; then
			echo "Container redis-graph exists, but has stopped"
		else
			echo "Container redis-graph exists and is running"
		fi
	else
		echo "Container redis-graph does not exist"
	fi
elif [[ $option == "stop" ]]; then

	set +e

	docker kill redis-graph &> /dev/null
	docker rm redis-graph &> /dev/null

	set -e

	if docker ps -a | grep redis-graph &> /dev/null; then
		>&2 echo "Failed to delete redis-graph container"
		exit 1
	fi

elif [[ $option == "kill" ]]; then

	set +e
	docker kill redis-graph &> /dev/null
	set -e

elif [[ $option == "delete" ]]; then

	docker rm redis-graph

elif [[ $option == "start" ]]; then

	if docker ps -a | grep redis-graph &> /dev/null ; then
		>&2 echo "redis-graph container already exists. Either use restart option or kill/delete then try again"
		exit 1
	fi

	if [ "$#" -gt 1 ]; then
		echo "Storing redis data at $(readlink -f $2)"
		mkdir -p $(readlink -f $2)
		docker run --name redis-graph -v $(readlink -f $2):/data -p 6379 -d redis redis-server --appendonly yes
	else
		docker run --name redis-graph -p 6379 -d redis
	fi

	echo "redis-graph running on port $(docker port redis-graph 6379)"

elif [[ $option == "restart" ]]; then

	set +e
	docker kill redis-graph &> /dev/null
	docker rm redis-graph &> /dev/null
	set -e

	if [ "$#" -gt 1 ]; then
		echo "Storing redis data at $(readlink -f $2)"
		mkdir -p $(readlink -f $2)
		docker run --name redis-graph -v $(readlink -f $2):/data -p 6379 -d redis redis-server --appendonly yes
	else
		docker run --name redis-graph -p 6379 -d redis
	fi

	echo "redis-graph running on port $(docker port redis-graph 6379)"

else
	>&2 echo "ERROR: invalid option: $option. Valid options are (status|stop|start|restart|kill|delete)"
	exit 1
fi
