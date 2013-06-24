package uk.ac.dotrural.quality.mink.assessment.sparql;

import java.util.ArrayList;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;

import uk.ac.dotrural.quality.mink.observation.Sighting;

public class GetSightings {
	
	private String endpoint = "http://dtp-126.sncs.abdn.ac.uk:8080/openrdf-sesame/repositories/MinkApp";

	public ArrayList<Sighting> getSightings()
	{
		ArrayList<Sighting> sightings = new ArrayList<Sighting>();
		ResultSet rs = queryForSightings();
		while(rs.hasNext())
		{
			QuerySolution qs = rs.next();
			
			Resource obs = qs.getResource("obs");
			Resource group = qs.getResource("group");
			Resource agent = qs.getResource("agent");
			
			Literal rTime = qs.getLiteral("rTime");
			Literal riverName = qs.getLiteral("riverName");
			Literal x = qs.getLiteral("x");
			Literal y = qs.getLiteral("y");
			Literal count = qs.getLiteral("count");
			Literal status = qs.getLiteral("status");
			Literal sTime = qs.getLiteral("sTime");
			
			String cCode = "unknown";
			String cName = "unknown";
			
			if(qs.contains("agentName"))
				cName = qs.getLiteral("agentName").getLexicalForm();
			else
				cCode = agent.getLocalName();
			
			Sighting s = new Sighting(
							obs.getLocalName().substring(11),
							rTime.getLexicalForm(),
							group.getLocalName(),
							cCode,
							cName,
							"unknown",
							"",
							"",
							riverName.getLexicalForm(),
							x.getLexicalForm(),
							y.getLexicalForm(),
							count.getLexicalForm(),
							status.getLexicalForm(),
							null,
							null,
							sTime.getLexicalForm()
						);
			sightings.add(s);
			
		}
		return sightings;
	}
	
	private ResultSet queryForSightings()
	{
		String query = "PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> " +
					   "PREFIX mink: <http://dtp-126.sncs.abdn.ac.uk/mink#> " +
					   "PREFIX foaf: <http://xmlns.com/foaf/0.1/> " + 
					   "PREFIX prov: <http://www.w3.org/NS/prov-o/> " +
					   "SELECT * WHERE { " + 
							"?obs ssn:featureOfInterest ?foi . " +  
							"?obs mink:x_coord ?x . " + 
							"?obs mink:y_coord ?y . " +
							"?obs prov:wasAttributedTo ?agent . " +    
						    "?obs ssn:observationResultTime ?rTime . " +  
						    "?obs ssn:observationSamplingTime ?sTime . " + 
							"?obs ssn:observationResult ?result . " +
							"?result ssn:hasValue ?value . " +
							"?value mink:count ?count . " + 
							"?value mink:status ?status . " + 
							"?foi mink:name ?riverName . " + 
							"?agent foaf:member ?group . " +
							"OPTIONAL { " + 
								"?agent foaf:name ?agentName . " + 	
							"}" + 
						"}";	
		
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);
		return qe.execSelect();
	}
	
}
