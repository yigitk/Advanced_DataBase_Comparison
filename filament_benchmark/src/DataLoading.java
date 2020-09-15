import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.sql.SqlStoreFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by rhol on 11/24/14.
 */
public class DataLoading {

    public void loadData(Connection conn, DefaultGraph graph, String filePath, int maxEdges)
    {
        int numEdges = 0;

        System.out.println("Loading " + maxEdges + " into database");

        Map<String, Node> nodes = new HashMap<String, Node>();
        final String[] ignore_properties = {"link_source", "linkage_run", "linkid", "rdf-schema", "linkage_score", "link_target", "link_type", "rdf-syntax-ns" };

        try {
            Reader in = new FileReader(filePath);
            CSVFormat dump_format = CSVFormat.newFormat(' ');
            Iterable<CSVRecord> records = dump_format.parse(in);

            int i=0;
            for (CSVRecord record : records) {
                if(i % 1000 == 0) {

                    conn.commit();
                }

                i++;

                String node1_name = record.get(0);
                String edge_name = record.get(1);
                String node2_name = record.get(2);

                boolean ignore_this = false;
                for(String ignored_prop : ignore_properties)
                {
                    if(edge_name.contains(ignored_prop))
                    {
                        ignore_this = true;
                        break;
                    }
                }

                if(ignore_this)
                    continue;

                Node n1 = nodes.get(node1_name);
                if(n1 == null) {
                    n1 = graph.newNode();
                    n1.add(node1_name, "");
                    nodes.put(node1_name, n1);
                }

                Node n2 = nodes.get(node2_name);
                if(n2 == null) {
                    n2 = graph.newNode();
                    n2.add(node2_name, "");
                    nodes.put(node2_name, n2);
                }

                graph.addEdge(n1, n2, edge_name);
                numEdges++;

                if(numEdges % (int)(.1*maxEdges) == 0)
                {
                    System.out.println("" + ((double)numEdges/(double)maxEdges)*100.0 + "%");
                }

                if(numEdges == maxEdges) {
                    System.out.println("Loaded " + numEdges + " into database");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to parse CSV" + e.getMessage());
            try {
                conn.close();
            } catch (SQLException e1) {
                System.err.println("Failed to close connection. " + e.getMessage());
            }
            System.exit(1);
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
