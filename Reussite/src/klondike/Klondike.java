package klondike;

import java.util.ArrayList;
import java.util.List;

import card.*;

//TODO undo/redo
//TODO fin automatique
//TODO Recommencer

public class Klondike
{
	public final static int NB_STACKS = 6; 
	private HeapStack heap;
	private List<AlternateColorStack> alternateColorStacks = new ArrayList<>();
	private List<ColorStack> colorStacks = new ArrayList<>();
	private CardStack selectedStack;
	private History history; 

	public Klondike(List<Card> cards)
	{
		for (int i = 0 ; i < NB_STACKS ; i++)
			alternateColorStacks.add(new AlternateColorStack(cards, i + 1));
		Color.getColors().forEach((e) -> colorStacks.add(new ColorStack()));
		heap = new HeapStack(cards);
		if(heap.hasNext())
			heap.next();
		history = new History();
		selectedStack = null;
	}	

	public Klondike()
	{
		this(Card.getCards());
	}
	
	public void selectSource(CardStack stack)
	{
		if (stack==null)
			throw new RuntimeException("Can't select null stack");
		selectedStack = stack;
	}
	
	public void unselectSource()
	{
		selectedStack = null;
	}
	
	public boolean hasSelectedSource ()
	{
		return selectedStack != null;
	}
	
	public CardStack getSelectedSource ()
	{
		return selectedStack;
	}

	public HeapStack getHeap()
	{
		return heap;
	}
	
	public ColorStack getColorStack(int index)
	{
		return colorStacks.get(index);
	}
	
	public AlternateColorStack getAlternateColorStack(int index)
	{
		return alternateColorStacks.get(index);
	}
	
	public boolean canSelect(CardStack stack)
	{
		return !hasSelectedSource() && !stack.isEmpty()
				|| canMove(stack);
	}
	
	public boolean canFinish()
	{
		if (!(heap.size() == 1))
			return false;
		for(AlternateColorStack stack : alternateColorStacks)
			if (stack.getNumberOfHiddenCards() != 0)
				return false;
		return true;			
	}

	public boolean isFinished()
	{
		if (!heap.isEmpty())
			return false;
		for(AlternateColorStack stack : alternateColorStacks)
			if (!stack.isEmpty())
				return false;
		return true;			
	}

	public boolean finish()
	{
		for(AlternateColorStack sourceStack : alternateColorStacks)
		{
			selectSource(sourceStack);
			for (ColorStack destination : colorStacks)
				if (canMove(destination))
				{
					move(destination);
					return true;
				}
		}
		if (!heap.isEmpty())
		{
			selectSource(heap);
			for (ColorStack destination : colorStacks)
				if (canMove(destination))
				{
					move(destination);
					return true;
				}
		}
		return false;
	}
	
	public boolean canMove(CardStack destination)
	{
		return hasSelectedSource() && getSelectedSource().canMove(destination);
	}
	
	public void move(CardStack destination)
	{
		history.run(new Move(getSelectedSource(), destination));
		unselectSource();
	}
	
	public void pickCard()
	{
		history.run(new Pick(heap));
	}


	public int getMaxStackSize()
	{
		return alternateColorStacks
				.stream()
				.map((stack) -> stack.getCards())
				.max((x, y) -> x.size() - y.size())
				.get().size();
	}
	
	public boolean canUndo()
	{
		return history.canUndo();
	}
	
	public boolean canRedo()
	{
		return history.canRedo();
	}
	
	public void undo()
	{
		history.undo();
	}
	
	public void redo()
	{
		history.redo();
	}	
}

