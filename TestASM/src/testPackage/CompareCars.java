package testPackage;

import java.util.List;

import org.esfinge.comparison.CompareException;
import org.esfinge.comparison.ComparisonComponent;
import org.esfinge.comparison.difference.Difference;

public class CompareCars {
	
	public static void main(String[] args) throws CompareException {
		Car car1 = new Car();
		Car car2 = new Car();
		
		car1.setColor("black");
		car1.setYear(1975);
		
		car2.setColor("white");
		car2.setYear(1975);
		
		ComparisonComponent c  = new ComparisonComponent();
		List<Difference> difs = c.compare(car1, car2);
		
		 for(Difference d : difs){
	            System.out.println(d);
	        }
		
	}

}


