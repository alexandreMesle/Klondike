package tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import card.Card;
import klondike.*;

class HeapStackTest
{
	private List<Card> cards;
	private HeapStack heap ;
	
	@BeforeEach
	void setUp() throws Exception
	{
		cards = Card.getCards();
		heap = new HeapStack(cards);
	}

	@AfterEach
	void tearDown() throws Exception
	{
	}

	@Test
	void iterator()
	{
		int i = 0;
		for (Card card : heap)
		{
			assertEquals(card, cards.get(i));
			assertEquals(heap.top(), cards.get(i));
			i++;
		}
		assertEquals(52, i);
		assertFalse(heap.hasNext());
		assertTrue(heap.isLastCard());
		heap.reset();
		assertTrue(heap.hasNext());
		heap.next();
		assertEquals(heap.top(), cards.get(0));
		heap.pop();
		assertEquals(heap.top(), cards.get(1));
	}
}
