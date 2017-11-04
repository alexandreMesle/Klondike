package klondike;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import card.*;

public abstract class CardStack implements Iterable<Card>
{
	private Stack<Card> cards;
	
	public CardStack()
	{
		cards = new Stack<>();
	}
	
	public CardStack(List<Card> cards)
	{
		this();
		if (cards != null)
			this.cards.addAll(cards);
	}
	
	public abstract boolean canPush(Card card);
	
	public void push(Card card)
	{
		if (canPush(card))
			this.cards.push(card);
		else
			throw new RuntimeException(card.toString() + " can't be pushed !");
	}
	
	protected void push(List<Card> cards)
	{
		this.cards.addAll(cards);
	}
	
	protected void pop()
	{
		cards.pop();
	}
	
	public boolean isEmpty()
	{
		return cards.isEmpty();
	}
	
	public Card top()
	{
		if (!isEmpty())
			return cards.peek();
		else
			throw new RuntimeException("No Top");
	}
	
	public void moveTop(CardStack destinationStack)
	{
		Card card = top();
		if (destinationStack.canPush(card))
		{
			pop();
			destinationStack.push(card);
		}
		else
			throw new RuntimeException("Top cannot be pushed");
	}
	
	public List<Card> getCards()
	{
		return Collections.unmodifiableList(cards);
	}
		
	@Override
	public Iterator<Card> iterator()
	{
		return backwardIterator();
	}
	
	protected Iterator<Card> backwardIterator()
	{
		List<Card> cards = new ArrayList<>(this.cards);
		Collections.reverse(cards);
		return Collections.unmodifiableList(cards).iterator();
	}

	protected Iterator<Card> forwardIterator()
	{
		return cards.iterator();
	}
	
	@Override
	public String toString()
	{
		String res = "";
		for (Card card : this)
			res += card.toString() + "\n";		
		return res;
	}
}