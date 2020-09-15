import org.fgraph.Edge;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by rhol on 11/29/14.
 */
public class RandomInsertsDeletes {

    public void randomInsertsDeletes(Connection conn, DefaultGraph graph, int numOperations)
    {
        for(int i=0; i<2*numOperations; i++) {
            String uuid = UUID.randomUUID().toString();

            if(i % 2 == 0)
            {
                for(Edge e : graph.edges())
                {
                    // delete this edge
                    e.delete();

                    break;
                }
            } else {
                Node randomNode = graph.newNode();
                randomNode.put(UUID.randomUUID().toString(), "");
            }
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
    }
}
