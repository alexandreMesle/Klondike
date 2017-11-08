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
	private CardStack selectedStack;

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
	
	public void select(CardStack stack)
	{
		if (stack==null)
			throw new RuntimeException("Can't select null stack");
		selectedStack = stack;
	}
	
	public void unselectStack()
	{
		selectedStack = null;
	}
	
	public boolean stackSelected ()
	{
		return selectedStack == null;
	}
	
	public CardStack getSelectedStack ()
	{
		return selectedStack;
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
	
	public boolean canMove(CardStack destination)
	{
		return stackSelected() || getSelectedStack().canMove(destination);
	}
	
	public void move(CardStack destination)
	{
		getSelectedStack().move(destination);
		unselectStack();
	}
	
	public int getMaxStackSize()
	{
		return alternateColorStacks
				.stream()
				.map((stack) -> stack.getCards())
				.max((x, y) -> x.size() - y.size())
				.get().size();
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

