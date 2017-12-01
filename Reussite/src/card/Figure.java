package card;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Figure implements Comparable<Figure>
{	
	private int value;
	public final static int NB_FIGURES = 10; 
	private static Set<Figure> figures;
	
	static
	{
		figures = new HashSet<>();
		for (int i = 1 ; i <= NB_FIGURES ; i++)
			figures.add(new Figure(i));
	}
	
	private Figure(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}

	@Override
	public String toString()
	{
		switch (value)
		{
		case 13 : return "Roi";
		case 12 : return "Reine";
		case 11 : return "Valet";
		default : return "" + value;
		}
	}
	
	@Override
	public int compareTo(Figure figure)
	{
		return value - figure.value;
	}
	
	public static Set<Figure> getFigure()
	{
		return Collections.unmodifiableSet(figures);
	}
}
