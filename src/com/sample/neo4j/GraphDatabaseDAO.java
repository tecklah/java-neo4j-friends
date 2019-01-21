package com.sample.neo4j;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;

public class GraphDatabaseDAO implements AutoCloseable
{
    private final Driver driver;

    public GraphDatabaseDAO( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    public void close() throws Exception
    {
        driver.close();
    }

    public HashMap<String, String> listFriends(final String name, final int distance )
    {
    		HashMap<String, String> friendList = new HashMap<String, String>();
    		
        try ( Session session = driver.session() )
        {
        		StatementResult listOfRecords = session.readTransaction(new TransactionWork<StatementResult>()
            {
            	
                @Override
                public StatementResult execute( Transaction tx )
                {
                    StatementResult result = tx.run( "MATCH (subjectPerson:Person {name:$name})-"
                    		+ "[:IS_FRIENDS_WITH*0.." + distance + "]-"
                    		+ "(relatedFriends:Person) RETURN relatedFriends.name as name", 
                    		parameters( "name", name, "distance",
                    				distance ) );
                    return result;
                }

            } );
            
        		while (listOfRecords.hasNext()) {
        			Record record = listOfRecords.next();
        			for (Map.Entry<String, Object> field : record.asMap().entrySet()) {
        				friendList.put(field.getValue().toString(), field.getValue().toString());
        			}
        		}
        		
        		session.close();
        }
        
        return friendList;
    }
    
    public int addFriend(final String myName, final String friendName )
    {
    		final Map<String, Object> parameters = new HashMap<String, Object>();
    		parameters.put("myName", myName);
    		parameters.put("friendName", friendName);
    		
        try ( Session session = driver.session() )
        {
        		List<Record> listOfRecords = session.writeTransaction( new TransactionWork<List<Record>>()
            {
            	
                @Override
                public List<Record> execute( Transaction tx )
                {
                    StatementResult result = tx.run( "MERGE (me:Person {name: $myName}) " + 
                    		"MERGE (friend:Person {name: $friendName}) " + 
                    		"MERGE (me)-[friendOf:IS_FRIENDS_WITH]-(friend) " + 
                    		"RETURN me, friend, friendOf", parameters );
                    return result.list();
                }

            } );
        		
        		session.close();
        		
        		return listOfRecords.size();
        }
    }
}