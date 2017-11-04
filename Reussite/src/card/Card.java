package card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Card implements Comparable<Card>
{
	private Color color;
	private Figure figure;
	private static Set<Card> cards;
	
	static 
	{
		cards = new HashSet<>();
		for (Color color : Color.getColors())
			for(Figure figure : Figure.getFigure())
				cards.add(new Card(color, figure));
	}

	private Card(Color color, Figure figure)
	{
		this.color = color;
		this.figure = figure;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public Figure getFigure()
	{
		return figure;
	}
	
	@Override
	public int compareTo(Card card)
	{
		return getFigure().compareTo(card.getFigure());
	}
	
	public static List<Card> getCards()
	{
		List<Card> cards = new ArrayList<>(Card.cards);
		Collections.shuffle(cards);
		return cards;
	}

	@Override
	public String toString()
	{
		return getFigure() + " de " + getColor();
	}
	
	public static void main(String[] args)
	{
		for (Card card : getCards())
			System.out.println(card);
	}
	
	public boolean isRed()
	{
		return color.isRed();
	}
	
	public int getValue()
	{
		return getFigure().getValue();
	}
}
