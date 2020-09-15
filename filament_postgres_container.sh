#!/bin/bash

set -e

if [ "$#" -lt 1 ]; then
	>&2 echo "ERROR: No option provided. Valid options are (status|stop|start|restart|kill|delete)"
	exit 1
fi

option=$(echo $1 | awk '{print tolower($0)}')
if [[ $option == "status" ]]; then

	if docker ps -a | grep filament-postgres &> /dev/null ; then
		if docker ps -a -f status=exited | grep filament-postgres &> /dev/null; then
			echo "Container filament-postgres exists, but has stopped"
		else
			echo "Container filament-postgres exists and is running"
		fi
	else
		echo "Container filament-postgres does not exist"
	fi
elif [[ $option == "stop" ]]; then

	set +e

	docker kill filament-postgres &> /dev/null
	docker rm filament-postgres &> /dev/null

	set -e

	if docker ps -a | grep filament-postgres &> /dev/null; then
		>&2 echo "Failed to delete filament-postgres container"
		exit 1
	fi

elif [[ $option == "kill" ]]; then

	set +e
	docker kill filament-postgres &> /dev/null
	set -e

elif [[ $option == "delete" ]]; then

	docker rm filament-postgres

elif [[ $option == "start" ]]; then

	if docker ps -a | grep filament-postgres &> /dev/null ; then
		>&2 echo "filament-postgres container already exists. Either use restart option or kill/delete then try again"
		exit 1
	fi

	if [ "$#" -gt 1 ]; then
		echo "Storing redis data at $(readlink -f $2)"
		mkdir -p $(readlink -f $2)
		docker run --name filament-postgres -p 5432 -v $(readlink -f $2):/var/lib/postgresql/data -d rhololkeolke/filament-postgres
	else
		docker run --name filament-postgres -p 5432 -d rhololkeolke/filament-postgres
	fi

	echo "filament-postgres running on port $(docker port filament-postgres 5432)"

elif [[ $option == "restart" ]]; then

	set +e
	docker kill filament-postgres &> /dev/null
	docker rm filament-postgres &> /dev/null
	set -e

	if [ "$#" -gt 1 ]; then
		echo "Storing redis data at $(readlink -f $2)"
		mkdir -p $(readlink -f $2)
		docker run --name filament-postgres -p 5432 -v $(readlink -f $2):/var/lib/postgresql/data -d rhololkeolke/filament-postgres
	else
		docker run --name filament-postgres -p 5432 -d rhololkeolke/filament-postgres
	fi

	echo "filament-postgres running on port $(docker port filament-postgres 5432)"

else
	>&2 echo "ERROR: invalid option: $option. Valid options are (status|stop|start|restart|kill|delete)"
	exit 1
fi
