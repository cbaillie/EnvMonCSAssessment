package uk.ac.dotrural.quality.mink.assessment.stats;

import java.util.ArrayList;
import java.util.Collections;

public class Stats {
	
	public static Long calculateAverage(ArrayList<Long> numbers)
	{
		Long total = 0L;
		for(int i=0;i<numbers.size();i++)
		{
			Long l = (Long)numbers.get(i);
			total += l;
		}
		return total/numbers.size();
	}
	
	public static double calculateMedian(ArrayList<Long> numbers)
	{
		double median = -1;
		Collections.sort(numbers);
		
		if(numbers.size() % 2 == 0)
		{
			int first = (numbers.size() / 2)-1;
			int second = first+1;
			long total= numbers.get(first) + numbers.get(second);
			median = total / 2.0;
		}			
		else
			median = numbers.get(numbers.size()/2);
		
		return median;
	}

}
