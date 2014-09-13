package debug.model;

import java.util.*;

public class DetailComparator implements Comparator
{
	public int compare( Object o1, Object o2 )
	{
		if (o1 instanceof DetailModel && o2 instanceof DetailModel )
		{
			DetailModel d1 = (DetailModel) o1;
			DetailModel d2 = (DetailModel) o2;
			
			return d1.getUID() - d2.getUID();
		}
		else
		{
			return -1;
		}
		
	}
}
