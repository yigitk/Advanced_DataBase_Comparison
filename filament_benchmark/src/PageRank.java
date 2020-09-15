import org.fgraph.Direction;
import org.fgraph.Edge;
import org.fgraph.Goid;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rhol on 11/29/14.
 */
public class PageRank {

    public Map<Goid, Double> calculatePageRank(DefaultGraph graph, double epsilon, boolean printIterations)
    {
        // get the out degree of each node in the graph
        Map<Goid, Integer> out_degree = new HashMap<Goid, Integer>();
        for(Node n : graph.nodes())
        {
            Collection<Edge> out_edges = n.edges(null, Direction.OUT);
            out_degree.put(n.getId(), out_edges.size());
        }

        double damping_factor = .85;

        // initialize the page rank guesses
        Map<Goid, Double> page_rank = new HashMap<Goid, Double>();
        for(Node n : graph.nodes())
        {
            page_rank.put(n.getId(), 1-damping_factor);
        }
        double difference = Double.MAX_VALUE;

        int iteration = 0;
        while(difference > epsilon) {
            difference = 0;

            for (Node n : graph.nodes()) {
                double this_page_rank = 1 - damping_factor;
                double incoming_summation = 0;
                Collection<Edge> in_edges = n.edges(null, Direction.IN);
                for(Edge e : in_edges)
                {
                    Goid head_id = e.getTail().getId();
                    incoming_summation += page_rank.get(head_id)/out_degree.get(head_id);
                }

                this_page_rank += damping_factor*incoming_summation;

                difference += Math.abs(this_page_rank - page_rank.get(n.getId()));

                page_rank.put(n.getId(), this_page_rank);
            }
            iteration++;
            if(printIterations)
                System.out.println("iteration: " + iteration);
        }

        return page_rank;
    }
}
