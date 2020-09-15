import org.fgraph.Edge;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FilamentPostgresExample {

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

        Node n1 = graph.newNode();
        Node n2 = graph.newNode();
        Node n3 = graph.newNode();
        Node n4 = graph.newNode();

        graph.addEdge( n1, n2, "Demo" );
        graph.addEdge( n1, n3, "Demo" );
        graph.addEdge( n1, n4, "Demo" );

        for( Edge e : n1.edges("Demo") ) {
            System.out.println( "Connects to:" + e.otherEnd(n1) );
        }

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

        try {
            conn.close();
        } catch(SQLException e) {
            System.err.println("Failed to close connection. " + e.getMessage());
            System.exit(1);
        }
    }
}