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
	private HeapStack stack ;
	
	@BeforeEach
	void setUp() throws Exception
	{
		cards = Card.getCards();
		stack = new HeapStack(cards);
	}

	@AfterEach
	void tearDown() throws Exception
	{
	}

	@Test
	void iterator()
	{
		assertEquals(stack.next(), cards.get(0));
		assertEquals(stack.top(), cards.get(0));
		assertEquals(stack.next(), cards.get(1));
		assertEquals(stack.top(), cards.get(1));
		stack.pop();
		assertEquals(stack.next(), cards.get(2));
		assertEquals(stack.top(), cards.get(2));
		while(!stack.last())
			stack.next();
		assertEquals(stack.top(), cards.get(cards.size() - 1));
		assertEquals(stack.next(), cards.get(0));
	}
	
//	@Test
//	void uncover()
//	{
//		List<Card> cards = Card.getCards();
//		Card randomCard = cards.get(0);
//		stack.push(randomCard);
//		assertTrue(stack.top() == randomCard);
//		randomCard = cards.get(1);
//		stack.push(randomCard);
//		assertTrue(stack.top() == randomCard);
//	}
//
//	@Test
//	void constructor()
//	{
//		List<Card> cards = Card.getCards();
//		stack = new TestStack(cards);
//		assertTrue(stack.getCards().equals(cards));
//	}
}
