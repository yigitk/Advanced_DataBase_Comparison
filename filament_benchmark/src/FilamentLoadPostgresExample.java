import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FilamentLoadPostgresExample {

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

        for(Node n : graph.nodes()) {
            System.out.println("Node: " + n.toString());
        }

        try {
            conn.close();
        } catch(SQLException e) {
            System.err.println("Failed to close connection. " + e.getMessage());
            System.exit(1);
        }
    }
}
