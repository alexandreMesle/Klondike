package commandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static java.lang.Math.*;

import card.Card;
import klondike.AlternateColorStack;
import klondike.CardStack;
import klondike.ColorStack;
import klondike.HeapStack;
import klondike.Klondike;


public class CommandLine
{
	private static final int
			NB_ADDITIONNAL_ROWS= 7,
			NB_COLS = max(2 * Klondike.NB_STACKS + 3, 17),
			COLOR_ROW = 2,
			COLOR_COL = 2,
			HEAP_ROW = 2,
			HEAP_COL = 14,
			STACK_ROW = 4,
			STACK_COL = 2,
			SELECTOR_ROW = -2;
		
	private static final String 
			NOTHING = "    ",
			EMPTY_STACK = "....",
			VERTICAL_BORDER = "|  |",
			HIDDEN_CARD = "????",
			HORIZONTAL_BORDER = "----",
			HEAP_SELECTOR = "(.) ",
			HEAP_PICKER = "(+) ",
			HEAP_ARROW = " <- ",
			SELECTED_STACK = "[X] ";				
	
	private static final char
			HEAP_CHAR = '.',
			PICK_CHAR = '+',
			EXIT_CHAR = 'q',
			UNDO_CHAR = 'u',
			REDO_CHAR = 'r';

	private int nbRows;
	private Klondike klondike;
	private List<List<String>> board ;
	private Scanner scanner = new Scanner(System.in);


	public CommandLine(Klondike klondike)
	{
		this.klondike = klondike;
		play();
	}

	private char getChoice(String display)
	{
		System.out.println(display);
		return scanner.next().charAt(0);
	}
	
	private void play()
	{
		char choice = 'a';
		do
		{
			try
			{
				
				klondike.unselectSource();
				displayBoard();
				if (klondike.canFinish())
				{
					System.out.println("You have won !");
//					finish();
					choice = EXIT_CHAR;
				}
				else
				{
					choice = getChoice("Source : ");
					if (choice == PICK_CHAR)
						klondike.pickCard();
					else if(choice == UNDO_CHAR)
						klondike.undo();
					else if(choice == REDO_CHAR)
						klondike.redo();
					else
					{
						klondike.selectSource(getSelectedStack(choice));
						displayBoard();		
						choice = getChoice("Cible ? ");
						klondike.move(getSelectedStack(choice));			
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Erreur de saisie");
//				e.printStackTrace();
				try {Thread.sleep(5000);} catch (InterruptedException e1) {}
			}
		}
		while(choice != EXIT_CHAR); 
	}

	private CardStack getSelectedStack(char choice)
	{
		if (choice >= '1' && choice <= (char)Klondike.NB_STACKS + '1')
			return klondike.getAlternateColorStack((int)(choice - '1'));
		if (choice == HEAP_CHAR)
			return klondike.getHeap();		
		return klondike.getColorStack(choice - 'a');		
	}
	
	private String stringOfCard(Card card)
	{
		String res = "";
		switch(card.getValue())
		{
			case 13 : res += "K "; break; 
			case 12 : res += "Q "; break; 
			case 11 : res += "J "; break; 
			case 10 : res += "10"; break; 
			default : res += card.getValue() + " "; 
		}
		res += card.getColor().getName().substring(0, 2);		
		return res;
	}

	private String getSelectorString(CardStack stack, String selector)
	{
		String selectorString = NOTHING;
		if (stack == klondike.getSelectedSource())
			selectorString = SELECTED_STACK;
		else if (klondike.canSelect(stack))
			selectorString = selector;
		return selectorString;
	}
	
	private String getNextCardString(HeapStack heap)
	{
		return (klondike.hasSelectedSource()) ? NOTHING : HEAP_PICKER;
	}
	
	private void initMatrix()
	{
		board = new ArrayList<>();
		for (int i = 0 ; i < nbRows ; i++)
		{
			List<String> line = new ArrayList<>();
			if (i == 0 || i == nbRows - 1)
				for (int j = 0 ; j < NB_COLS; j++)
					line.add(HORIZONTAL_BORDER);				
			else
			{
				line.add(VERTICAL_BORDER);
				for (int j = 0 ; j < NB_COLS - 2; j++)
					line.add(NOTHING);
				line.add(VERTICAL_BORDER);
			}
			board.add(line);
		}		
	}

	private void refreshMatrix()
	{
		if (board == null || nbRows != board.size())
			initMatrix();
		for (int i = 1 ; i < nbRows -1 ; i++)
			for (int j = 1 ; j < NB_COLS - 1; j++)
				board.get(i).set(j, NOTHING);
	}
	
	private void finish()
	{
		if (klondike.canFinish())
			while(klondike.finish())
			{
				refreshBoard();
				try {Thread.sleep(1000);} catch (InterruptedException e) {}
			}
		System.out.println("Gagné !");
	}
	
	private void refreshBoard()
	{
		nbRows = klondike.getMaxStackSize() + NB_ADDITIONNAL_ROWS;
		refreshMatrix();
		printColorStacks();
		printAlternateColorStacks();
		printHeap();
	}

	private void printHeap()
	{
		HeapStack heap = klondike.getHeap();
		if (!heap.isEmpty())
		{
			// TODO bug quand on utilise la dernière carte de la pile : le sommet de pile reste affiché
			board.get(HEAP_ROW).set(HEAP_COL - 3, getSelectorString(heap, HEAP_SELECTOR));
			board.get(HEAP_ROW).set(HEAP_COL - 2, (klondike.getHeap().isLastCard()) ? HIDDEN_CARD : stringOfCard(heap.top()));
			board.get(HEAP_ROW).set(HEAP_COL - 1, (klondike.getHeap().isLastCard()) ? NOTHING : HEAP_ARROW);
			board.get(HEAP_ROW - 1).set(HEAP_COL - 1, getNextCardString(heap));
			board.get(HEAP_ROW).set(HEAP_COL, (klondike.getHeap().isLastCard()) ? EMPTY_STACK : HIDDEN_CARD);
		}
		else
		{
			board.get(HEAP_ROW).set(HEAP_COL, NOTHING);			
		}
	}

	private void printAlternateColorStack(AlternateColorStack stack, int col, String selector, int maxStackSize)
	{
		int row = STACK_ROW,
			selectorRow = board.size() + SELECTOR_ROW;
		if (!stack.isEmpty())
		{
			for (Card card : stack.getCards())
			{
				String str =  (row < STACK_ROW + stack.getNumberOfHiddenCards()) 
						? HIDDEN_CARD 
						: stringOfCard(card);
				board.get(row).set(col, str);	
				row++;
			}
		}
		else
		{
			board.get(row).set(col, EMPTY_STACK);
			board.get(selectorRow).set(col, NOTHING);
			row++;
		}
		while (row < STACK_ROW + maxStackSize)
		{
			board.get(row).set(col, NOTHING);	
			row++;
		}
		board.get(selectorRow).set(col, getSelectorString(stack, selector));
	}
	
	private void printAlternateColorStacks()
	{
		int maxStackSize = klondike.getMaxStackSize();				
		for (int index = 0 ; index < Klondike.NB_STACKS ; index++)
			printAlternateColorStack(klondike.getAlternateColorStack(index)
					, STACK_COL + 2*index, "(" + (index + 1) + ") ", maxStackSize);
	}

	
	// TODO permettre de prendre une carte dans une ColorStack
	private void printColorStacks()
	{
		int column = COLOR_COL;
		for (int i = 0 ; i < 4 ; i++)
		{
			ColorStack stack = klondike.getColorStack(i);
			String card = EMPTY_STACK;
			if (!stack.isEmpty())
				card = stringOfCard(stack.top());
			String selectorString = getSelectorString(stack, "(" + (char)('a' + i) + ") ");
			board.get(COLOR_ROW - 1).set(column, selectorString);
			board.get(COLOR_ROW).set(column, card);
			column += 2;
		}
	}

	private void displayBoard()
	{
		System.out.println(this);
	}

	@Override
	public String toString()
	{
		refreshBoard();
		String res = "";
		for (List<String> line : board)
		{
			for (String cell : line)
				res += cell;
			res += "\n";
		}
		return res;
	}

	public static void main(String[] args)
	{
		new CommandLine(new Klondike());
	}

}
