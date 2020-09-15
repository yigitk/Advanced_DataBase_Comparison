import org.fgraph.Goid;
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
public class PageRankTestDB {

    public static void main(String[] args) {
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

        Node page_a = graph.newNode();
        page_a.put("page a", "");
        Node page_b = graph.newNode();
        page_b.put("page b", "");
        Node page_c = graph.newNode();
        page_c.put("page c", "");
        Node page_d = graph.newNode();
        page_d.put("page d", "");

        graph.addEdge(page_a, page_b, "");
        graph.addEdge(page_a, page_c, "");
        graph.addEdge(page_b, page_c, "");
        graph.addEdge(page_c, page_a, "");
        graph.addEdge(page_d, page_c, "");

//        graph.addEdge(page_a, page_b, "");
//        graph.addEdge(page_b, page_a, "");


        try {
            conn.commit();
        } catch (SQLException e) {
            System.err.println("Failed to commit nodes. " + e.getMessage());
            try {
                conn.close();
            } catch (SQLException e1) {
                System.err.println("Failed to close connection. " + e.getMessage());
            }
            System.exit(1);
        }

        PageRank pageRank = new PageRank();

        Map<Goid, Double> page_rank = pageRank.calculatePageRank(graph, 1.0e-3, true);

        System.out.println("Page ranks are");

        for(Node n : graph.nodes())
        {
            System.out.println("Node " + n.toString() + " has page rank " + page_rank.get(n.getId()));
        }

        try {
            conn.close();
        } catch(SQLException e) {
            System.err.println("Failed to close connection. " + e.getMessage());
            System.exit(1);
        }
    }
}
