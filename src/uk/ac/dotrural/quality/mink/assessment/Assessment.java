package uk.ac.dotrural.quality.mink.assessment;

import java.util.ArrayList;
import uk.ac.dotrural.quality.mink.assessment.sparql.GetSightings;
import uk.ac.dotrural.quality.mink.assessment.stats.Stats;
import uk.ac.dotrural.quality.mink.observation.Sighting;

public class Assessment {
	
	public static void main(String[] args)
	{
		new Assessment();
	}
	
	public Assessment()
	{
		ArrayList<Sighting> sightings = new GetSightings().getSightings();
		
		Sighting s = (Sighting)sightings.get(0);
		s.toOntModel().write(System.out);
		
	}
}
