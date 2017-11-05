package klondike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import static java.lang.Math.*;

import card.Card;

public class AlternateColorStack extends CardStack
{
	private int numberOfHiddenCards;
	private boolean pushHiddenCards = true;
	
	public AlternateColorStack(List<Card> source, int nbCards)
	{
		super();
		if (nbCards != 0)
		{
			Iterator<Card> iterator = source.iterator();
			int i = 1;
			while(iterator.hasNext() && i <= nbCards)
			{
				Card card = iterator.next();
				push(card);
				iterator.remove();
				i++;
			}
		}
		numberOfHiddenCards = max(nbCards - 1, 0);
		pushHiddenCards = false;
	}
	
	private void uncover()
	{
		if(numberOfHiddenCards != 0)
			numberOfHiddenCards--;
	}
	
	protected void pop()
	{
		super.pop();
		uncover();
	}
	
	public List<Card> getShown()
	{
		List<Card> cards = getCards();
		cards = new ArrayList<>(cards.subList(numberOfHiddenCards, cards.size()));
		Collections.reverse(cards);
		cards = Collections.unmodifiableList(cards);
		return cards;
	}
	
	private List<Card> bulkTop(CardStack destinationStack)
	{
		List<Card> cards = getCards(), subList = null;
		int nbCards = cards.size();
		for (int i = numberOfHiddenCards ; i < nbCards ; i++)
		{
			if (subList == null && destinationStack.canPush(cards.get(i)))
				subList = new ArrayList<>(cards.subList(i, cards.size()));
			if (subList != null)
				pop();
		}
		return subList;
	}
	
	public void bulkMove(CardStack destinationStack)
	{
		List<Card> cards = bulkTop(destinationStack);
		if (cards == null)
			throw new RuntimeException("Can't bulk move " + getCards().toString() 
					+ " to " + destinationStack.getCards().toString());
		destinationStack.push(cards);
	}
	
	public boolean canBulkMove(CardStack destinationStack)
	{
		for (Card card : getShown())
			if (destinationStack.canPush(card))
				return true;
		return false;		
	}
	
	@Override
	public boolean canPush(Card card)
	{
		return pushHiddenCards 
				|| (isEmpty() 
						&& card.getValue() == 13)
				|| (!isEmpty()
					&& top().isRed() != card.isRed() 
					&& top().getValue() == card.getValue() + 1
					);
	}
}
