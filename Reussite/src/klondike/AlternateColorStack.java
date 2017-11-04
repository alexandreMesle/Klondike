package klondike;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import card.Card;

public class AlternateColorStack extends CardStack
{
	private int numberOfHiddenCards;
	private boolean pushHiddenCards = true;
	
	public AlternateColorStack(List<Card> source, int nbCards)
	{
		Iterator<Card> iterator = source.iterator();
		Card card = iterator.next();
		int i = 1;
		while(iterator.hasNext() && i <= nbCards)
		{
			push(card);
			iterator.remove();
			card = iterator.next();
			i++;
		}
		numberOfHiddenCards = nbCards - 1;
		pushHiddenCards = false;
	}
	
	public void uncover()
	{
		if(numberOfHiddenCards != 0)
			numberOfHiddenCards--;
		else
			throw new RuntimeException("No Card to uncover");
	}
	
	protected void pop()
	{
		super.pop();
		uncover();
	}
	
	public List<Card> getShown()
	{
		List<Card> cards = getCards();
		cards = cards.subList(numberOfHiddenCards, cards.size());
		cards = Collections.unmodifiableList(cards);
		return cards;
	}
	
	private List<Card> bulkTop(Card bottomCard)
	{
		List<Card> cards = new Stack<>();
		for (Card card : getShown())
		{
			cards.add(card);
			if (card == bottomCard)
				return cards;
		}
		return null;
	}
	
	public void bulkMove(CardStack destinationStack)
	{
		for(Card card : getShown())
			if (destinationStack.canPush(card))
			{
				List<Card> cards = bulkTop(card);
				destinationStack.push(cards);
				return;
			}
	}
	
	@Override
	public boolean canPush(Card card)
	{
		return pushHiddenCards || 
				(top().isRed() != card.isRed() 
				&& top().getValue() + 1 == card.getValue()
				);
	}
}
