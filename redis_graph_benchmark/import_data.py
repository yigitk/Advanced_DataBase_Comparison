"""Populates a redis instance with RDF data from a dump."""

if __name__ == '__main__':
    import argparse
    import redis
    import redis_graph
    import csv

    parser = argparse.ArgumentParser(description="Load the specified data RDF triple file into the redis database")
    parser.add_argument('--host', dest='host', type=str, default='127.0.0.1', help='Redis server address')
    parser.add_argument('-p', dest='port', type=str, default='6379', help='Redis server port')
    parser.add_argument('triple_file', type=str, help='path to the RDF triple file')

    args = parser.parse_args()

    redis_graph.SYSTEMS['default'] = redis.StrictRedis(args.host, args.port)

    subjects = set()
    objects = set()
    predicates = set()
    with open(args.triple_file) as triple_file:
        triple_reader = csv.reader(triple_file, delimiter=' ')
        for triple in triple_reader:
            redis_graph.add_edge(from_node=triple[0], to_node=triple[2])
            redis_graph.set_edge_value("%s_%s" % (triple[0], triple[2]), triple[1])

            subjects.add(triple[0])
            objects.add(triple[2])
            objects.add(triple[1])

    print "Added %d subjects %d objects and %d predicates" % (len(subjects), len(objects), len(predicates))