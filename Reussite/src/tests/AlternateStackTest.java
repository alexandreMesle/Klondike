package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import card.Card;
import card.Color;
import klondike.AlternateColorStack;

class AlternateStackTest
{
	private List<Card> cards;
	private AlternateColorStack stack ;
	
	@BeforeEach
	void setUp() throws Exception
	{
		cards = Card.getCards();
	}

	@AfterEach
	void tearDown() throws Exception
	{
	}
	
	@Test
	void initOneCard()
	{
		Card top = cards.get(0);
		stack = new AlternateColorStack(cards, 1);
		assertEquals(stack.top(), top);
		assertEquals(cards.size(), 51);
	}

	@Test
	void initTwoCards()
	{
		Card top = cards.get(1);
		stack = new AlternateColorStack(cards, 2);
		assertEquals(stack.top(), top);
		assertEquals(cards.size(), 50);
	}

	@Test
	void push()
	{
		Collections.sort(cards);
		Map<Color, AlternateColorStack> stacks = new HashMap<>();
		for (int i = 1 ; i <= 4 ; i++)
		{
			AlternateColorStack stack = new AlternateColorStack(cards, 1);
			stacks.put(stack.top().getColor(), stack);
			assertEquals(cards.size(), 52 - i);
		}
		assertEquals(stacks.size(), 4);
		Iterator<Card> iterator = cards.iterator();
		Card card = iterator.next();
		while(iterator.hasNext())
		{
			for(Color color : stacks.keySet())
			{
				AlternateColorStack stack = stacks.get(color);
				boolean can = stack.top().isRed() != card.isRed()
						&& stack.top().getValue() + 1 == card.getValue();
				if(stack.canPush(card))
				{
					assertTrue(can, stack.top() + " | " + card);
					stack.push(card);
					iterator.remove();
					break;
				}
				else
					assertFalse(can, stack.top() + " | " + card);
			}
			card = iterator.next();
		}
	}
}
