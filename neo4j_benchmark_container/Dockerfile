## -*- docker-image-name: "rhololkeolke/neo4j-benchmark" -*-

FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-7-*
ADD neo4j.jar /neo4j.jar
ADD benchmark_script.sh /benchmark_script.sh

CMD ./benchmark_script.sh
