package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
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
import klondike.HeapStack;

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
		Collections.sort(cards, (card1, card2) -> card2.compareTo(card1));
		Map<Color, AlternateColorStack> stacks = new HashMap<>();
		for (int i = 1 ; i <= 4 ; i++)
		{
			AlternateColorStack stack = new AlternateColorStack(cards, 1);
			stacks.put(stack.top().getColor(), stack);
			assertEquals(cards.size(), 52 - i);
		}
		assertEquals(stacks.size(), 4);
		Iterator<Card> iterator = cards.iterator();
		while(iterator.hasNext())
		{
			Card card = iterator.next();
			for(Color color : stacks.keySet())
			{
				AlternateColorStack stack = stacks.get(color);
				boolean can = stack.top().isRed() != card.isRed()
						&& stack.top().getValue() == card.getValue() + 1 ;
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
		}
	}
	
	@Test
	void moveTopFromHeap()
	{
		Collections.sort(cards, (card1, card2) -> card2.compareTo(card1));
		HeapStack heap = new HeapStack(cards);
		List<AlternateColorStack> stacks = new ArrayList<>();
		assertTrue(heap.getCards().size() == 52, heap.toString());
		for (int i = 1 ; i <= 4 ; i++)
			stacks.add(new AlternateColorStack(null, 0));
		int i = 0;
		heap.reset();
		assertTrue(heap.hasNext());
		heap.next();
		assertEquals(heap.top().getFigure().getValue(), 13);
		for(Card card : heap)
		{
			assertTrue(heap.getCards().size() == 52 - i, heap + " | " + i);
			assertEquals(card, heap.top());
			for(AlternateColorStack stack : stacks)
			{
				boolean can = (stack.isEmpty()) 
						? card.getValue() == 13 
						: (stack.top().isRed() != card.isRed()
							&& stack.top().getValue() == card.getValue() + 1);
				if(stack.canPush(card))
				{
					assertTrue(can, stack + " | " + card);
					heap.move(stack);
					i ++;
					break;
				}
				else
					assertFalse(can, stack + " | " + card);
			}
		}
	}

	@Test
	void bulkMove()
	{
		Collections.sort(cards, (card1, card2) -> card2.compareTo(card1));
		Map<Color, AlternateColorStack> stacks = new HashMap<>();
		for (int i = 1 ; i <= 4 ; i++)
		{
			AlternateColorStack stack = new AlternateColorStack(cards, 1);
			stacks.put(stack.top().getColor(), stack);
			assertEquals(cards.size(), 52 - i);
		}
		HeapStack heap = new HeapStack(cards);
		assertTrue(heap.getCards().size() == 48, heap.toString());
		Color stackColor = cards.get(0).getColor();
		AlternateColorStack stack = stacks.get(stackColor);
		for(Card card : heap)
		{
			assertEquals(card, heap.top());
			boolean can = (stack.isEmpty()) 
					? card.getValue() == 13 
					: (stack.top().isRed() != card.isRed()
						&& stack.top().getValue() == card.getValue() + 1);
			if(stack.canPush(card))
			{
				assertTrue(can, stack + " | " + card);
				heap.move(stack);
			}
			else
				assertFalse(can, stack + " | " + card);
		}
		for (Color color : stacks.keySet())
		{
			boolean can = color != stackColor && color.isRed() == stackColor.isRed();
			AlternateColorStack sameColorStack = stacks.get(color);
			assertEquals(can, stack.canMove(sameColorStack));
			if (can)
			{
				stack.move(sameColorStack);
				assertEquals(13, sameColorStack.getCards().size()); 
				assertEquals(1, stack.getCards().size()); 
			}
		}
	}
	
	@Test
	void moveSingletonStack()
	{
		Collections.sort(cards, (card1, card2) -> card2.compareTo(card1));
		List<AlternateColorStack> stacks = new ArrayList<>();
		for (int i = 1 ; i <= 4 ; i++)
			stacks.add(new AlternateColorStack(cards, 0));
		HeapStack heap = new HeapStack(cards);
		assertTrue(heap.hasNext());
		heap.next();
		heap.move(stacks.get(0));
		stacks.get(0).move(stacks.get(1));
		assertEquals(0, stacks.get(0).getCards().size());
		assertEquals(1, stacks.get(1).getCards().size());
		assertEquals(0, stacks.get(2).getCards().size());
		assertEquals(0, stacks.get(3).getCards().size());
	}

	@Test
	void moveCompleteStack()
	{
		Collections.sort(cards, (card1, card2) -> card2.compareTo(card1));
		List<AlternateColorStack> stacks = new ArrayList<>();
		for (int i = 1 ; i <= 4 ; i++)
			stacks.add(new AlternateColorStack(cards, 0));
		AlternateColorStack stack = stacks.get(0);
		cards
			.stream()
			.filter(card -> stack.canPush(card))
			.forEach(card -> stack.push(card));
		assertEquals(13, stacks.get(0).getCards().size());
		assertEquals(0, stacks.get(1).getCards().size());
		assertEquals(0, stacks.get(2).getCards().size());
		assertEquals(0, stacks.get(3).getCards().size());
		stacks.get(0).move(stacks.get(1));
		assertEquals(0, stacks.get(0).getCards().size());
		assertEquals(13, stacks.get(1).getCards().size());
		assertEquals(0, stacks.get(2).getCards().size());
		assertEquals(0, stacks.get(3).getCards().size());
	}
}
