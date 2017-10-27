package card;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Color
{
	private String name;
	
	private boolean red;
	
	private static Set<Color> colors;
	
	static 
	{
		colors = new HashSet<>();
		colors.add(new Color("Coeur", true));
		colors.add(new Color("Carreau", true));
		colors.add(new Color("Pique", false));
		colors.add(new Color("Trefle", false));
	}
	
	private Color(String name, boolean red)
	{
		this.name = name;
		this.red = red;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isRed()
	{
		return red;
	}
	
	public boolean isBlack()
	{
		return !isRed();
	}
	
	public static Set<Color> getColors()
	{
		return Collections.unmodifiableSet(colors);
	}
	
	@Override
	public String toString()
	{
		return getName();
	}
}
