## -*- docker-image-name: "rhololkeolke/dex-benchmark" -*-

FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-7-*
ADD dex.jar /dex.jar
ADD benchmark_script.sh /benchmark_script.sh

CMD ./benchmark_script.sh
