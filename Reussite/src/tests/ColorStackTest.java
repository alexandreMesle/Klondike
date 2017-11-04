package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import card.Card;
import klondike.ColorStack;

class ColorStackTest
{
	private List<Card> cards;
	private ColorStack stack ;
	
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
	void test() throws Exception
	{
		cards.removeIf( (t) -> t.getColor() != cards.get(0).getColor());
		Collections.sort(cards);
		stack = new ColorStack();
		for (Card card : cards)
		{
			stack.push(card);
			assertEquals(card, stack.top());
		}
		try
		{
			stack.push(cards.get(0));
			assertTrue(false);
		}
		catch(RuntimeException e){}
		setUp();
		cards.removeIf( (t) -> t.getColor() == cards.get(0).getColor());
		try
		{
			stack.push(cards.get(0));
			assertTrue(false);
		}
		catch(RuntimeException e){}
	}
}
