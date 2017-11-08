package klondike;

import java.util.Iterator;
import java.util.List;

import card.Card;

public class HeapStack extends CardStack implements Iterator<Card>
{
	private Iterator<Card> iterator;
	private Card top;
	private boolean lastCard;

	public HeapStack(List<Card> cards)
	{
		super(cards);
		reset();
	}
	
	@Override
	public Card top()
	{
		return top;
	}
	
	@Override
	public void pop()
	{
		iterator.remove();
		hasNext();
	}
	
	@Override
	public boolean canPush(Card card)
	{
		return false;
	}

	@Override
	public Iterator<Card> iterator()
	{
		reset();
		return this;
	}
	
	public void reset()
	{
		iterator = forwardIterator();
		lastCard = false;
	}
	
	@Override
	public boolean hasNext()
	{
		if (iterator.hasNext())
		{
			top = iterator.next();
			return true;
		}
		lastCard = true;
		return false;
	}
	
	@Override
	public Card next()
	{
		return top();
	}
	
	@Override
	public void remove()
	{
		pop();
	}
	
	public boolean isLastCard()
	{
		return lastCard;
	}
}
