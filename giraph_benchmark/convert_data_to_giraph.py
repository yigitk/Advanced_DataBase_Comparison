#!/usr/bin/env python

import re, csv, sys

exclude_patterns = re.compile('(link_source|linkage_run|linkid|rdf-schema|linkage_score|link_target|link_type|rdf-syntax-ns)')

if __name__ == '__main__':
    max_edges = int(sys.argv[2])

    nodes = {}

    num_edges = 0
    print "Loading %d edges" % max_edges
    with open(sys.argv[1], 'r') as dump_file:
        reader = csv.reader(dump_file, delimiter=' ')
        for row in reader:
            if re.search(exclude_patterns, row[1]):
                continue
            if row[0] not in nodes:
                node0 = {}
                node0['id'] = len(nodes)
                node0['value'] = 0
                node0['edges'] = []
                nodes[row[0]] = node0
            else:
                node0 = nodes[row[0]]
            if row[2] not in nodes:
                node1 = {}
                node1['id'] = len(nodes)
                node1['value'] = 0
                node1['edges'] = []
                nodes[row[2]] = node1
            else:
                node1 = nodes[row[2]]

            node0['edges'].append((node1['id'], 1))

            num_edges += 1


            if(num_edges % (int)(.1*max_edges) == 0):
                print "%3.2f%%" % ((float(num_edges)/float(max_edges))*100.0)

            if num_edges == max_edges:
                print "Loaded %d edges and %d vertices" % (num_edges, len(nodes))
                break

    print "Writing data to output_file in Giraph format"
    with open(sys.argv[3], 'w') as output_file:
        for i, node in enumerate(nodes):
            if i != 0:
                output_file.write("\n")
            output_file.write("[%d, %d, [" % (nodes[node]['id'], nodes[node]['value']))
            for j, edge in enumerate(nodes[node]['edges']):
                if j != 0:
                    output_file.write(", ")
                output_file.write("[%d, %d]" % (edge[0], edge[1]))
            output_file.write("]]")
        
    print "Finished"
