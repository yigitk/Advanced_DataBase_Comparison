import org.fgraph.Goid;
import org.fgraph.GraphObject;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by rhol on 11/29/14.
 */
public class SSSPTest {

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

//        Node node_a = graph.newNode();
//        node_a.put("node_a", "");
//        Node node_b = graph.newNode();
//        node_b.put("node_b", "");
//        Node node_c = graph.newNode();
//        node_c.put("node_c", "");
//        Node node_d = graph.newNode();
//        node_d.put("node_d", "");
//        Node node_e = graph.newNode();
//        node_e.put("node_e", "");
//
//        graph.addEdge(node_a, node_b, "");
//        graph.addEdge(node_a, node_c, "");
//        graph.addEdge(node_b, node_c, "");
//        graph.addEdge(node_c, node_a, "");
//        graph.addEdge(node_c, node_d, "");
//        graph.addEdge(node_c, node_e, "");
//        graph.addEdge(node_d, node_a, "");
//        graph.addEdge(node_e, node_d, "");
//
//        try {
//            conn.commit();
//        } catch (SQLException e) {
//            System.err.println("Failed to commit nodes. " + e.getMessage());
//            try {
//                conn.close();
//            } catch (SQLException e1) {
//                System.err.println("Failed to close connection. " + e.getMessage());
//            }
//            System.exit(1);
//        }

        //GraphObject graphObject = graph.findObject("<http://data.linkedmdb.org/film/31626>", "");
        GraphObject graphObject = graph.findObject("node_a", "");
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

        for(Node n : graph.nodes()) {
            Goid prevId = solution.previous.get(n.getId());
            if (prevId != null) {
                System.out.println("Node: " + n.toString() + " distance: " + solution.distance.get(n.getId()) + " previous: " + graph.getNode(solution.previous.get(n.getId())).toString());
            } else {
                System.out.println("Node: " + n.toString() + " distance: " + solution.distance.get(n.getId()) + " previous: null");
            }
        }

        try {
            conn.close();
        } catch(SQLException e) {
            System.err.println("Failed to close connection. " + e.getMessage());
            System.exit(1);
        }
    }
}
