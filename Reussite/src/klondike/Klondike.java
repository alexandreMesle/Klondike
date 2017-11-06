package klondike;

import java.util.ArrayList;
import java.util.List;

import card.*;

public class Klondike
{
	public final static int NB_STACKS = 7; 
	private HeapStack heap;
	private List<AlternateColorStack> alternateColorStacks = new ArrayList<>();
	private List<ColorStack> colorStacks = new ArrayList<>();

	public Klondike(List<Card> cards)
	{
		for (int i = 0 ; i < NB_STACKS ; i++)
			alternateColorStacks.add(new AlternateColorStack(cards, i + 1));
		Color.getColors().forEach((e) -> colorStacks.add(new ColorStack()));
		heap = new HeapStack(cards);
		heap.hasNext();
	}	

	public Klondike()
	{
		this(Card.getCards());
	}
	
	public HeapStack getHeap()
	{
		return heap;
	}
	
	public ColorStack getColorStack(int index)
	{
		return colorStacks.get(index);
	}
	
	public AlternateColorStack getAlternateColorStack(int index)
	{
		return alternateColorStacks.get(index);
	}
	
	public boolean canMove(CardStack source, CardStack destination)
	{
		return source == null || source.canMove(destination);
	}
	
	public void pickCard()
	{
		if (!heap.hasNext())
		{
			heap.reset();
			heap.hasNext();
		}
	}
}

