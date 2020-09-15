import org.fgraph.*;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by rhol on 11/29/14.
 */
public class SSSP {

    public class Solution {
        public Map<Goid, Goid> previous;
        public Map<Goid, Integer> distance;
        public Node sourceNode;

        public Solution(Map<Goid, Goid> previous, Map<Goid, Integer> distance, Node sourceNode)
        {
            this.previous = previous;
            this.distance = distance;
            this.sourceNode = sourceNode;
        }
    }

    public class NodePair {

        public Node node;
        public Integer distance;

        public NodePair(Node n, Integer distance)
        {
            this.node = n;
            this.distance = distance;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof NodePair) {
                NodePair other = (NodePair) object;
                if (other.node.getId() == this.node.getId())
                    return true;
            } else if(object instanceof Node) {
                Node other = (Node) object;
                if(other.getId() == this.node.getId())
                    return true;
                return false;
            } else if(object instanceof Goid) {
                Goid other = (Goid)object;
                if(other == this.node.getId())
                    return true;
                return false;
            }

            return false;
        }
    }

    public class DistanceComparator implements Comparator<NodePair> {

        @Override
        public int compare(NodePair x, NodePair y) {
            if(x.distance > y.distance)
            {
                return 1;
            }
            else if(x.distance < y.distance)
            {
                return -1;
            }
            return 0;
        }
    }

    public Solution SSSP(DefaultGraph graph, Node sourceNode)
    {
        Map<Goid, Integer> distances = new HashMap<Goid, Integer>();
        // backpointers to previous node in the path
        // null if end of path
        Map<Goid, Goid> previous = new HashMap<Goid, Goid>();
        // priority queue of distances
        Map<Goid, NodePair> nodes = new HashMap<Goid, NodePair>();
        PriorityQueue<NodePair> nodeQueue = new PriorityQueue<NodePair>(100000, new DistanceComparator());
        for(Node n : graph.nodes())
        {
            if(n.getId().equals(sourceNode.getId()))
            {
                distances.put(n.getId(), 0);
                NodePair nodePair = new NodePair(n, 0);
                nodes.put(n.getId(), nodePair);
                nodeQueue.add(nodePair);
                previous.put(n.getId(), null);
            }
            else {
                distances.put(n.getId(), Integer.MAX_VALUE);

                NodePair nodePair = new NodePair(n, Integer.MAX_VALUE);
                nodes.put(n.getId(), nodePair);
                // initialize with infinity for all nodes
                nodeQueue.add(nodePair);

                // currently no path so no backpointers
                previous.put(n.getId(), null);
            }
        }

        while(nodeQueue.size() != 0)
        {
            NodePair minDistNode = nodeQueue.poll();

            // get the outgoing edge neighbors
            Collection<Edge> out_edges = minDistNode.node.edges(null, Direction.OUT);
            for(Edge e : out_edges)
            {
                int alt_dist = minDistNode.distance + 1;
                if(alt_dist < distances.get(e.getHead().getId()))
                {
                    distances.put(e.getHead().getId(), alt_dist);
                    previous.put(e.getHead().getId(), minDistNode.node.getId());

                    // update in priority queue

                    NodePair nodePair = nodes.get(e.getHead().getId());
                    nodePair.distance = alt_dist;
                    nodeQueue.remove(nodePair);
                    nodeQueue.add(nodePair);
                }
            }

        }

        return new Solution(previous, distances, sourceNode);
    }

}
