package edu.gt.gtri.neo4jtesting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.UniqueFactory;
import org.neo4j.tooling.GlobalGraphOperations;

public class Test {

	public static GraphDatabaseService graphDb;
	private static UniqueFactory<Node> factoryForFilm;
	private static UniqueFactory<Node> factoryForPerson;
	
	public static enum RelTypes implements RelationshipType {
		TYPE, INTERLINK
	}
	public static void R(String result) {
	    System.out.println("RSLT: " + result);
	}

	public static UniqueFactory<Node> getUniqueFactory() {
		if (factoryForFilm == null) {
			factoryForFilm = new UniqueFactory.UniqueNodeFactory(graphDb, "id") {
				@Override
				protected void initialize(Node created,
						Map<String, Object> properties) {
					created.setProperty("id", properties.get("id"));
				}
			};
		}
		return factoryForFilm;
	}
	
	public static UniqueFactory<Node> getUniqueFactoryForPerson() {
		if (factoryForPerson == null) {
			factoryForPerson = new UniqueFactory.UniqueNodeFactory(graphDb, "personId") {
				@Override
				protected void initialize(Node created,
						Map<String, Object> properties) {
					created.setProperty("personId", properties.get("personId"));
				}
			};
		}
		return factoryForPerson;
	}

	private static void registerShutdownHook(final GraphDatabaseService graphDb) {
		// Registers a shutdown hook for the Neo4j instance so that it
		// shuts down nicely when the VM exits (even if you "Ctrl-C" the
		// running application).
		try {
			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					graphDb.shutdown();
				}
			});
		} catch (Exception e) {
			// _logger.log (Level.SEVERE, "Unable to Shut Down Database :",e);
		}
	}

	public static void main(String[] args) {

		ArrayList<String> typeOfRelation = new ArrayList<String>();
		typeOfRelation.add("Actor");
		typeOfRelation.add("Director");
		typeOfRelation.add("editor");
		typeOfRelation.add("location");
		typeOfRelation.add("producer");
		typeOfRelation.add("writer");
		typeOfRelation.add("musicContributor");
		
		
		
		Map<Integer,Person> mapWithPerson = new HashMap<Integer,Person>();
		for(int i = 1; i<500;i++){
			Person person = new Person();
			person.setPersonId(1);
			person.setLabel("kamal"+i);
			person.setName("CZ"+i);
			person.setPage("page"+i);
			mapWithPerson.put(i, person);
		}
		Map<Integer,Film> mapWithFilm = new HashMap<Integer,Film>();
		for(int i=1;i<500;i++){
			Film film = new Film();
			film.setDate("11/12/2004"+i);
			film.setFilmSubject("Magic"+i);
			film.setTitle("Harry Potter"+i);
			film.setId(i);	
			film.setLabel("label"+i);
			film.setLanguage("EN"+i);
			film.setPage("page"+i);
			film.setPerformance("per"+i);
			film.setProductionCompany("compan"+i);
			film.setRuntime("10:1"+i);
			mapWithFilm.put(i,film);
		}
		
		long startTime, endTime;
		long nv = 0, ne = 0;
		long[] off = null, ind = null, wgt = null;
		long na = 0;
		long[] actions = null;
		Node[] nodesForFilm = null;
		Node[] nodesForPerson = null;

		System.out.println("Up and running...");
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("./neo4j.db");
		registerShutdownHook(graphDb);

		System.out.println("Loading graph into DB...");
		{
			startTime = System.nanoTime();

			nodesForFilm = new Node[mapWithFilm.size()+1];
			nodesForPerson = new Node[mapWithPerson.size()+1];
			
		     Transaction tx = graphDb.beginTx();
		try{
//			factoryForFilm = Test.getUniqueFactory();
		     //add all film node
		     Set<Integer> keySet = mapWithFilm.keySet();
		     for(Integer key : keySet){
		    	 Film film1 = mapWithFilm.get(key);
		    	 
		    	 nodesForFilm[key] = graphDb.createNode(); //getUniqueFactory().getOrCreate("id",film1.getId());
		 		//graphDb.createNode();
		    	 if(film1.getDate()!=null){
		    		 nodesForFilm[key].setProperty("date",film1.getDate());
		    	 }
		    	 if(film1.getFilmSubject()!=null){
		    		 nodesForFilm[key].setProperty("filmSubject",film1.getFilmSubject());
		    	 }
		    	 
		    	 if(film1.getLanguage()!=null){
		    		 nodesForFilm[key].setProperty("language",film1.getLanguage());
		    	 }
		    	 if(film1.getId()!=0){
		    		 nodesForFilm[key].setProperty("id",film1.getId());
		    	 }
		     }
		     //create all person node
		     
		    keySet = mapWithPerson.keySet();
		     for(Integer key : keySet){
		    	 Person person = mapWithPerson.get(key);
		    	 nodesForPerson[key] = graphDb.createNode();//getUniqueFactoryForPerson().getOrCreate("personId",person.getPersonId());
		 		//graphDb.createNode();
		    	 if(person.getLabel()!=null){
		    		 nodesForPerson[key].setProperty("label",person.getLabel());
		    	 }
		    	 if(person.getName()!=null){
		    		 nodesForPerson[key].setProperty("name",person.getName());
		    	 }
		    	 
		    	 if(person.getPage()!=null){
		    		 nodesForPerson[key].setProperty("page",person.getPage());
		    	 }
		    	 if(person.getPersonId()!=0){
		    		 nodesForPerson[key].setProperty("id",person.getPersonId());
		    	 }
		     }
		     
		     
		     
		     //add relation b/w node and person
		    for(int i=1; i < nodesForFilm.length ; i++){
		    	for(int j=1;j<nodesForFilm.length ; j++){
		    		if(i!=j){
		    		 Relationship rel= nodesForFilm[i].createRelationshipTo(nodesForFilm[j], RelTypes.TYPE);
		    		    rel.setProperty("weight", i*j);
		    		    Collections.shuffle(typeOfRelation);
		    		    rel.setProperty("relationType", typeOfRelation.get(0));
		    		}
		    	}
		    }
		tx.success();
    } catch (Exception e) {
	tx.failure();
    } finally {
	tx.finish();
    }
    endTime = System.nanoTime();
  }
		double sv_time = (((double)(endTime - startTime))/1e9);
	    R("\"sv\": {");
	    R("\"name\":\"neo4j-std\",");
	    R("\"time\":" +  sv_time);
	    R("},");
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");

//	    double build_time = (((double)(endTime - startTime))/1e9);
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");
	
	    System.out.println("BFS...");
	    {
	    	Transaction tx = graphDb.beginTx();
	      startTime = System.nanoTime();

	      HashMap<Node, Boolean> found = new HashMap<Node, Boolean>(nodesForFilm.length-1);
	      HashMap<Node, Integer> distance = new HashMap<Node, Integer>(nodesForFilm.length-1);
	      for(int v = 1; v < nodesForFilm.length; v++) {
	    	  found.put(nodesForFilm[v], false);
	    	  distance.put(nodesForFilm[v], -1);
	      }
	      found.put(nodesForFilm[1], true);
	      distance.put(nodesForFilm[1], 0);

	      LinkedList<Node> q = new LinkedList<Node>();
	      q.add(nodesForFilm[1]);
	      
	      while(!q.isEmpty()) {
		Node v = q.poll();
	//	System.out.println(v.getProperty("id"));
		for(Relationship rel : v.getRelationships()) {
		  Node endNode = rel.getEndNode();
		  if(!found.get(endNode)) {
		    found.put(endNode, true);
		    distance.put(endNode, distance.get(v) + 1);
		    q.offer(endNode);
		  }
		}
	  }
	      tx.finish();
	      endTime = System.nanoTime();

	      int depth = 0;
	      for(int d : distance.values()) {
		if(d > depth)
		  depth = d;
	      }
//	      System.out.println("\tDepth... " + depth);
	    
	    }
	    double sssp_time = (((double)(endTime - startTime))/1e9);
	    R("\"sssp\": {");
	    R("\"name\":\"neo4j-std\",");
	    R("\"time\":" +  sssp_time);
	    R("},");
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");
	    
	    
	    
	    System.out.println("Page Rank...");
	    {
	      Transaction tx = graphDb.beginTx();
	      double epsilon = 1e-8;
	      double dampingfactor = 0.85;
	      int maxiter = 100;

	      startTime = System.nanoTime();
	      HashMap<Node, Double> pr = new HashMap<Node, Double>(nodesForFilm.length-1);
	      HashMap<Node, Double> prTmp = new HashMap<Node, Double>(nodesForFilm.length-1);

	      for(Node v : GlobalGraphOperations.at(graphDb).getAllNodes()) {
	    	  pr.put(v, 1.0/(nodesForFilm.length-1));
	      }

	      int iter = maxiter;
	      double delta = 1;

	      while(delta > epsilon && iter > 0) {
	    	  for(Node v : GlobalGraphOperations.at(graphDb).getAllNodes()) {
	    		  double myPrTmp = 0;
	    		  for(Relationship rel : v.getRelationships()) {
	    			  Node endNode = rel.getEndNode();
	    			  myPrTmp += pr.get(endNode) / ((double)countDegree(endNode));
	    		  }
	    	prTmp.put(v, myPrTmp);
		}

		for(Node v : GlobalGraphOperations.at(graphDb).getAllNodes()) {
		  prTmp.put(v, prTmp.get(v) * dampingfactor + ((1.0-dampingfactor) / ((double)nv)));
		}

		delta = 0;
		for(Node v : GlobalGraphOperations.at(graphDb).getAllNodes()) {
		  double mydelta = prTmp.get(v) - pr.get(v);

		  if(mydelta < 0)
		    mydelta = -mydelta;

		  delta += mydelta;
		  pr.put(v, prTmp.get(v));
		}

		//System.out.println("\tIteration " + (maxiter - iter) + " delta " + delta);
		iter--;
	      }

	      endTime = System.nanoTime();
tx.finish();
	      System.out.println("\tIterations... " + (maxiter - iter));
	      
	    }
	    double pr_time = (((double)(endTime - startTime))/1e9);
	    R("\"pr\": {");
	    R("\"name\":\"neo4j-std\",");
	    R("\"time\":" +  pr_time);
	    R("},");
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");

    
	    
	    // ScheduledExecutorService executor =
		// Executors.newSingleThreadScheduledExecutor();

		/*
		 * executor.schedule(new Runnable() {
		 * 
		 * @Override public void run() { System.out.println("run the thread");
		 * 
		 * } }, 5, TimeUnit.SECONDS);
		 */
		// executor
		// scheduleAtFixedRate

		/*
		 * java.util.Timer timer = new java.util.Timer();
		 * 
		 * timer.scheduleAtFixedRate( new java.util.TimerTask() {
		 * 
		 * @Override public void run() { System.out.println(new Date()
		 * +"run the"); } }, 0, 5000);
		 * 
		 * 
		 * timer.cancel();
		 */
		/*
		 * Timer timer = new Timer(3000, new ActionListener() {
		 * 
		 * @Override public void actionPerformed(ActionEvent arg0) {
		 * System.out.println("run"); } }); timer.setRepeats(false); // Only
		 * execute once timer.start();
		 */
	}
	 public static int countDegree(Node v) {
		    int count = 0;
		    for(Relationship r : v.getRelationships()) {
		      count++;
		    }
		    return count;
	}
}
