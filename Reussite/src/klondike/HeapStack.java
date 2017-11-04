package klondike;

import java.util.Iterator;
import java.util.List;

import card.Card;

public class HeapStack extends CardStack
{
	private Iterator<Card> iterator; 
	private Card top;

	public HeapStack(List<Card> cards)
	{
		super(cards);
	}
	
	public Card next()
	{
		if (iterator == null || !iterator.hasNext())
			iterator = forwardIterator();
		if (iterator.hasNext())
			top = iterator.next();
		else
			throw new RuntimeException("Heap is empty");
		return top();
	}
	
	public boolean last()
	{
		return !iterator.hasNext();		
	}
	
	public Card top()
	{
		return top;
	}

	public void pop()
	{
		iterator.remove();
	}

	@Override
	public boolean canPush(Card card)
	{
		return true;
	}
}
