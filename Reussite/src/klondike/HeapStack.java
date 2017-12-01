package klondike;

import java.util.List;
import java.util.ListIterator;

import card.Card;

public class HeapStack extends CardStack implements ListIterator<Card>
{
	private ListIterator<Card> iterator;
	private Card top;

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
		System.out.println("Popping " + top());
		iterator.remove();
		if (hasNext())
			next();
		else
			top = null;
	}
	
	@Override
	public boolean canPush(Card card)
	{
		return false;
	}

	@Override
	public ListIterator<Card> iterator()
	{
		reset();
		return this;
	}
	
	public void reset()
	{
		iterator = forwardIterator();
	}
	
	@Override
	public boolean hasNext()
	{
		return iterator.hasNext();
	}
	
	@Override
	public Card next()
	{
		top = iterator.next();
		return top();
	}
	
	@Override
	public void remove()
	{
		pop();
	}
	
	public boolean isLastCard()
	{
		return !iterator.hasNext();
	}

	@Override
	public void add(Card card) 
	{
		iterator.add(card);
	}

	@Override
	public boolean hasPrevious() 
	{
		return iterator.hasPrevious();
	}

	@Override
	public int nextIndex() 
	{
		throw new RuntimeException("Don't call this method");
	}

	@Override
	public Card previous() 
	{
		return iterator.previous();
	}

	@Override
	public int previousIndex() 
	{
		throw new RuntimeException("Don't call this method");
	}

	@Override
	public void set(Card arg0) 
	{
		throw new RuntimeException("Don't call this method");
	}
}
