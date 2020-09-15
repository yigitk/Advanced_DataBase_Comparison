package com.test;

import java.util.HashMap;

import com.sparsity.sparksee.algorithms.TraversalBFS;
import com.sparsity.sparksee.gdb.AttributeKind;
import com.sparsity.sparksee.gdb.DataType;
import com.sparsity.sparksee.gdb.Database;
import com.sparsity.sparksee.gdb.EdgesDirection;
import com.sparsity.sparksee.gdb.Graph;
import com.sparsity.sparksee.gdb.Objects;
import com.sparsity.sparksee.gdb.ObjectsIterator;
import com.sparsity.sparksee.gdb.Session;
import com.sparsity.sparksee.gdb.Sparksee;
import com.sparsity.sparksee.gdb.SparkseeConfig;
import com.sparsity.sparksee.gdb.Value;

public class Test {
	 public static void R(String result) {
		    System.out.println("RSLT: " + result);
		  }
	public static void main(String[] args) throws Exception {
		long startTime, endTime;
		SparkseeConfig cfg = new SparkseeConfig();
        Sparksee sparksee = new Sparksee(cfg);
        Database db = sparksee.create("./sparkseedb.db", "HelloSparksee1");
        Session sess = db.newSession();
        Graph g = sess.getGraph();

        int nodesForFilmCount = 50000;
        int nodesForPersonCount = 50000;
		long[] nodesForFilm = new long[nodesForFilmCount];
		long[] nodesForPerson = new long[nodesForPersonCount];
		int movieType ,castType;
     // 
    	// SCHEMA
    	//
        System.out.println("Loading graph into DB...");
		{
			startTime = System.nanoTime();
    	
            // Add a node type for the movies, with a unique identifier and two indexed attributes
            movieType = g.newNodeType("MOVIE");
            int movieIdType = g.newAttribute(movieType, "id", DataType.Integer, AttributeKind.Unique);
            int movieDateType = g.newAttribute(movieType, "date", DataType.String, AttributeKind.Basic);
            int movieFilmSubjectType = g.newAttribute(movieType, "filmSubject", DataType.String, AttributeKind.Basic);
            int movieLabelType = g.newAttribute(movieType, "label", DataType.String, AttributeKind.Basic);
            int movieLanguageType = g.newAttribute(movieType, "language", DataType.String, AttributeKind.Basic);
            int moviePageType = g.newAttribute(movieType, "page", DataType.String, AttributeKind.Basic);
           // int movielanguageType = g.newAttribute(movieType, "language", DataType.String, AttributeKind.Basic);
            int moviePerformanceType = g.newAttribute(movieType, "performance", DataType.String, AttributeKind.Basic);
            int movieProductionCompanyType = g.newAttribute(movieType, "productionCompany", DataType.String, AttributeKind.Basic);
            int movieRuntimeType = g.newAttribute(movieType, "runtime", DataType.String, AttributeKind.Basic);
            int movieTitleType = g.newAttribute(movieType, "title", DataType.String, AttributeKind.Basic);
            
    	// Add a node type for the people, with a unique identifier and an indexed attribute
            int peopleType = g.newNodeType("PEOPLE");
            int peopleIdType = g.newAttribute(peopleType, "personId", DataType.Integer, AttributeKind.Unique);
            int peopleNameType = g.newAttribute(peopleType, "name", DataType.String, AttributeKind.Basic);
            int peoplePageType = g.newAttribute(peopleType, "page", DataType.String, AttributeKind.Basic);
            int peopleLabelType = g.newAttribute(peopleType, "label", DataType.String, AttributeKind.Basic);
            
             castType = g.newEdgeType("Type", false, false);
            int relationType = g.newAttribute(castType, "relationType", DataType.String, AttributeKind.Basic);
            int weight = g.newAttribute(castType, "weight", DataType.String, AttributeKind.Basic);
            
            
           //
            // DATA
        	//
			
    		
            // Add some MOVIE nodes
            for(int i = 1; i<nodesForFilmCount;i++){
            	Value value = new Value();
            	nodesForFilm[i] = g.newNode(movieType);
            	g.setAttribute(nodesForFilm[i], movieIdType, value.setInteger(i));
            	g.setAttribute(nodesForFilm[i], movieTitleType,value.setString("Lost in Translation"+i));
            	g.setAttribute(nodesForFilm[i], movieDateType, value.setString(""));
            	g.setAttribute(nodesForFilm[i],movieFilmSubjectType , value.setString(""+i));
            	g.setAttribute(nodesForFilm[i], movieLabelType, value.setString(""+i));
            	g.setAttribute(nodesForFilm[i], movieLanguageType, value.setString(""+i));
            	g.setAttribute(nodesForFilm[i], moviePageType, value.setString(""+i));
    		}
            
   /*
            for(int i = 1; i<nodesForPersonCount;i++){
            	Value value = new Value();
            	nodesForPerson[i] = g.newNode(peopleType);
            	g.setAttribute(nodesForPerson[i], peopleIdType, value.setInteger(i));
            	g.setAttribute(nodesForPerson[i], peopleLabelType,value.setString("Lost in Translation"+i));
            	g.setAttribute(nodesForPerson[i], peopleNameType, value.setString("pageName"+i));
            	g.setAttribute(nodesForPerson[i],peoplePageType , value.setString("pageType"+i));
            	
            }
            
   */         //add relation 
            for(int i=1; i < nodesForFilmCount ; i++){
		    	for(int j=1;j< nodesForFilmCount ; j++){
		    		if(i!=j){
		    			Value value = new Value();
		    			long anEdge;
		    			anEdge = g.newEdge(castType, nodesForFilm[i], nodesForFilm[j]);
		    			g.setAttribute(anEdge, relationType, value.setString("abc"));
		    			g.setAttribute(anEdge, weight, value.setString(i*j+""));
		    		}
		    	}
		    }
            endTime = System.nanoTime();
		}// end the load data into DB
		  double build_time = (((double)(endTime - startTime))/1e9);
		R("\"build\": {");
		    R("\"name\":\"devx-std\",");
		    R("\"time\":" +  build_time);
		    R("},");

		System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");
            
         //add BFS
		System.out.println("BFS...");
	    {
	      startTime = System.nanoTime();
  //       System.out.println("Traversal BFS");
         // Create a new BFS traversal from the node "startingNode"
         TraversalBFS bfs = new TraversalBFS(sess,nodesForFilm[1]);
         // Allow the use of all the node types
         bfs.addAllNodeTypes();
         // Allow the use of all the edge types but only in outgoing direction
         bfs.addAllEdgeTypes(EdgesDirection.Outgoing);
         // Limit the depth to 3 hops from the starting node
       //  bfs.setMaximumHops(3);
         // Get the nodes
         while (bfs.hasNext())
         {
         long nodeid = bfs.next();
         int depth = bfs.getCurrentDepth();
         //System.out.println("Node "+nodeid+" at depth "+depth+".");
         }
         // Close the traversal
         bfs.close();
         endTime = System.nanoTime();
      }
	    
	    
	    double sssp_time = (((double)(endTime - startTime))/1e9);
	    R("\"sssp\": {");
	    R("\"name\":\"devx-std\",");
	    R("\"time\":" +  sssp_time);
	    R("},");
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");
	    
	    
	    
	    
	    System.out.println("Page Rank...");
	    {
	      double epsilon = 1e-8;
	      double dampingfactor = 0.85;
	      int maxiter = 9;

	      startTime = System.nanoTime();

	      HashMap<Long, Double> pr = new HashMap<Long, Double>(nodesForFilm.length-1);
	      HashMap<Long, Double> prTmp = new HashMap<Long, Double>(nodesForFilm.length-1);
	      	Objects obj= g.select(movieType);
	      	{
	      	ObjectsIterator iterator =   obj.iterator();
	      	
	      	 while(iterator.hasNext()) {
	      	    pr.put(iterator.next(),1.0/(nodesForFilm.length-1));
	      	 }
	      	iterator = null; 
	      	}    
	      	
	      int iter = maxiter;
	      double delta = 1;

	      while(delta > epsilon && iter > 0) {
	    		ObjectsIterator iterator1 =   obj.iterator();
		      	 while(iterator1.hasNext()) {
		      		double myPrTmp = 0;
		      	    long v = iterator1.next();
		      	    Objects neighborObjects = g.neighbors(v,castType, EdgesDirection.Outgoing);
		      	    ObjectsIterator  neighbor = neighborObjects.iterator();
		      	    	while(neighbor.hasNext()) {
		      	    		long neigh = neighbor.next();
		      	    		myPrTmp += pr.get(neigh) / ((double) g.degree(neigh, castType, EdgesDirection.Outgoing));
		      	    		
		      	    	}
		      	    	prTmp.put(v, myPrTmp);
		      	    	neighbor = null;
		      	    	iter--;
		      	 }
		      	
		      	}
	      ObjectsIterator iterator =   obj.iterator();
	      	
	      	 while(iterator.hasNext()) {
	      		 long v =iterator.next();
//	      	    pr.put(iterator.next(),1.0/(nodesForFilm.length-1));
	      	  prTmp.put(v, prTmp.get(v) * dampingfactor + ((1.0-dampingfactor) / ((double)nodesForFilmCount-1)));
	      	
	      	 }
	      //	iterator.remove();
		delta = 0;
		iterator =   obj.iterator();
     	 while(iterator.hasNext()) {
     		 long v = iterator.next();
     		double mydelta = prTmp.get(v) - pr.get(v);
     		if(mydelta < 0)
  		    mydelta = -mydelta;
     		delta += mydelta;
     		pr.put(v, prTmp.get(v));
     		
     	 }
     //	iterator.remove();
		//System.out.println("\tIteration " + (maxiter - iter) + " delta " + delta);
		
		 endTime = System.nanoTime();
		  System.out.println("\tIterations... " + (maxiter - iter));
	   }

	    double pr_time = (((double)(endTime - startTime))/1e9);
	    R("\"pr\": {");
	    R("\"name\":\"devx-std\",");
	    R("\"time\":" +  pr_time);
	    R("},");
	    System.out.println("\tDone... " + (((double)(endTime - startTime))/1e9) + "\n");
	    }
}
