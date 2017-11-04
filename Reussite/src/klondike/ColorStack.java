package klondike;

import card.Card;

public class ColorStack extends CardStack
{
	@Override
	public boolean canPush(Card card)
	{
		if (isEmpty())
			return card.getValue() == 1;
		return top().getColor() == card.getColor() 
				&& top().getValue() + 1 == card.getValue();
	}

}
