import org.fgraph.Goid;
import org.fgraph.GraphObject;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by rhol on 11/29/14.
 */
public class FilamentBenchmark {

    public static void main(String[] args)
    {
        String jdbcUrl = "jdbc:postgresql://" + args[0] + ":" + args[1] + "/" + args[2];
        String user = "filament";
        String password = "filament";

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            System.err.println("Failed to open connection to Postgres database. " + e.getMessage());
            System.exit(1);
        }

        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Failed to set database connection to manual commit");
            try {
                conn.close();
            } catch (SQLException e1) {
                System.err.println("Failed to close connection. " + e.getMessage());
            }
            System.exit(1);
        }

        DefaultGraph graph = DefaultGraph.create(new SqlStoreFactory(conn));

        int maxEdges = Integer.parseInt(args[4]);

        System.out.println("Starting data loading");
        long startTime = System.nanoTime();
        DataLoading dataLoading = new DataLoading();
        dataLoading.loadData(conn, graph, args[3], maxEdges);
        long endTime = System.nanoTime();

        System.out.println("Loaded " + graph.nodes().size() + " nodes and " + graph.edges().size() + " edges into DB");

        long loadTime = endTime - startTime;
        System.out.println("Took " + loadTime + " ns to load " + maxEdges);

        startTime = System.nanoTime();
        PageRank pageRank = new PageRank();
        Map<Goid, Double> ranks = pageRank.calculatePageRank(graph, 1.0e-3, false);
        endTime = System.nanoTime();

        long pageRankTime = endTime - startTime;
        System.out.println("Took " + pageRankTime +  " ns to run page rank algorithm");

        startTime = System.nanoTime();
        GraphObject graphObject = graph.findObject("<http://data.linkedmdb.org/film/31626>", "");
        Node sourceNode = null;
        if(graphObject instanceof Node)
        {
            sourceNode = (Node)graphObject;
        }

        if(sourceNode == null) {
            System.err.println("Error finding specified source node");
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Failed to close connection. " + e.getMessage());
                System.exit(1);
            }
            System.exit(1);
        }

        SSSP sssp = new SSSP();
        SSSP.Solution solution = sssp.SSSP(graph, sourceNode);
        endTime = System.nanoTime();

        long ssspTime = endTime - startTime;
        System.out.println("Took " + ssspTime +  " ns to run sssp algorithm");

        startTime = System.nanoTime();
        RandomInsertsDeletes randomInsertsDeletes = new RandomInsertsDeletes();
        randomInsertsDeletes.randomInsertsDeletes(conn, graph, 1000);
        endTime = System.nanoTime();

        long insertsDeletesTime = endTime - startTime;
        System.out.println("Took " + insertsDeletesTime + " ns to perform " + 1000 + " random insertions and deletions");

        try {
            conn.close();
        } catch (SQLException e) {
            System.err.println("Failed to close connection. " + e.getMessage());
            System.exit(1);
        }
    }
}
