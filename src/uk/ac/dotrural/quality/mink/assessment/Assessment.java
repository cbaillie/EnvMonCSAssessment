package uk.ac.dotrural.quality.mink.assessment;

import java.util.ArrayList;

import org.topbraid.spin.statistics.SPINStatistics;

import uk.ac.dotrural.quality.mink.assessment.sparql.GetSightings;
import uk.ac.dotrural.quality.mink.assessment.stats.Stats;
import uk.ac.dotrural.quality.mink.observation.Sighting;
import uk.ac.dotrural.reasoning.reasoner.Reasoner;
import uk.ac.dotrural.reasoning.reasoner.ReasonerResult;

public class Assessment {
	
	private String rules = "http://dtp-126.sncs.abdn.ac.uk/ontologies/MinkApp/Mink.ttl";
	private String notation = "TTL";
	
	ArrayList<Long> accuracy = new ArrayList<Long>();
	ArrayList<Long> completeness = new ArrayList<Long>();
	ArrayList<Long> reputation = new ArrayList<Long>();
	ArrayList<Long> timeliness = new ArrayList<Long>();
	ArrayList<Long> total = new ArrayList<Long>();
	
	public static void main(String[] args)
	{
		new Assessment();
	}
	
	public Assessment()
	{
		ArrayList<Sighting> sightings = new GetSightings().getSightings();
		
		for(int i=0;i<sightings.size();i++)
		{
			Sighting s = (Sighting)sightings.get(i);
			
			Reasoner reasoner = new Reasoner(rules, notation, false);
			ReasonerResult rr = reasoner.performReasoning(s.toOntModel());
			
			calculateStats(rr);
		}
		writeReports();
	}
	
	private void calculateStats(ReasonerResult rr)
	{
		long accuracy = 0, completeness = 0, timeliness = 0, reputation = 0, total = 0;
		ArrayList<SPINStatistics> ssList = rr.statistics;
		
		for(int i=0;i<ssList.size();i++)
		{
			SPINStatistics ss = ssList.get(i);
			String dimension = ss.getContext().getLocalName();
			
			if(dimension.equalsIgnoreCase("accuracy"))
				accuracy += ss.getDuration();
			else if(dimension.equalsIgnoreCase("completeness"))
				completeness += ss.getDuration();
			else if(dimension.equalsIgnoreCase("timeliness"))
				timeliness += ss.getDuration();
			else if(dimension.equalsIgnoreCase("reputation"))
				reputation += ss.getDuration();
			
			total += ss.getDuration();
		}
		this.accuracy.add(accuracy);
		this.completeness.add(completeness);
		this.timeliness.add(timeliness);
		this.reputation.add(reputation);
		this.total.add(total);
	}
	
	private void writeReports()
	{
		log("Total");
		log("=====================");
		log("Average (ms): " + Stats.calculateAverage(total));
		log("Median (ms): " + Stats.calculateMedian(total));
		
		log("");
		log("Individual Metrics");
		log("=====================");
		log("[Accuracy]");
		log("Average (ms): " + Stats.calculateAverage(accuracy));
		log("Median (ms): " + Stats.calculateMedian(accuracy));
		log("");
		log("[Completeness]");
		log("Average (ms): " + Stats.calculateAverage(completeness));
		log("Median (ms): " + Stats.calculateMedian(completeness));
		log("");
		log("[Reputation]");
		log("Average (ms): " + Stats.calculateAverage(reputation));
		log("Median (ms): " + Stats.calculateAverage(reputation));
		log("");
		log("[Timeliness]");
		log("Average (ms): " + Stats.calculateAverage(timeliness));
		log("Median (ms): " + Stats.calculateMedian(timeliness));
	}
	
	private void log(String msg)
	{
		System.out.println(msg);
	}
}
