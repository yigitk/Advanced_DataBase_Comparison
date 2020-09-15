## -*- docker-image-name: "rhololkeolke/filament-benchmark" -*-

FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-7-*
ADD filament_benchmark_jar /filament_benchmark_jar
ADD dump.nt /raw_data.nt
ADD benchmark_script.sh /benchmark_script.sh

CMD ./benchmark_script.sh
