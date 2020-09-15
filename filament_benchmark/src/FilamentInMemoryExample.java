import org.fgraph.Edge;
import org.fgraph.Node;
import org.fgraph.base.DefaultGraph;
import org.fgraph.tstore.mem.HashTripleStore;

public class FilamentInMemoryExample {

    public static void main(String[] args) {
        DefaultGraph graph = DefaultGraph.create(HashTripleStore.FACTORY);

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
    }
}
