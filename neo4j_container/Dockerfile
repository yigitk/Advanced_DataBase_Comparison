## -*- docker-image-name: "rhololkeolke/neo4j" -*-

FROM ubuntu
RUN apt-get update && apt-get install -y openjdk-7-* wget lsof
RUN wget -O neo4j.tar.gz http://neo4j.com/artifact.php?name=neo4j-community-2.1.6-unix.tar.gz
RUN tar -xzf neo4j.tar.gz && mv neo4j-* neo4j

EXPOSE 7474

CMD ["/neo4j/bin/neo4j", "console"]
