package commandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import card.Card;
import card.Color;
import klondike.AlternateColorStack;
import klondike.CardStack;
import klondike.ColorStack;
import klondike.HeapStack;
import klondike.Klondike;

public class CommandLine
{
	private static final int
			MAX_STACK_SIZE = 13,
			NB_COLS = 2 * Klondike.NB_STACKS + 3,
			NB_ROWS = MAX_STACK_SIZE + 5,
			COLOR_ROW = 2,
			COLOR_COL = 2,
			HEAP_ROW = 2,
			HEAP_COL = 14,
			STACK_ROW = 4,
			STACK_COL = 2,
			SELECTOR_ROW = MAX_STACK_SIZE + 3,
			SELECTOR_COL = 2;
		
	private static final String 
			EMPTY_STACK = "....",
			NOTHING = "    ",
			VERTICAL_BORDER = "|  |",
			HIDDEN_CARD = "????",
			HORIZONTAL_BORDER = "----",
			HEAP_SELECTOR = "(y) ",
			HEAP_PICKER = "(n)w",
			SELECTED_STACK = "[X] ";				
	
	private static final char
			HEAP_CHAR = 'y',
			PICK_CHAR = 'n',
			EXIT_CHAR = 'q';
	
	private Klondike klondike;
	private List<List<String>> board = new ArrayList<>();
	private CardStack sourceStack = null;

	public CommandLine(Klondike klondike)
	{
		this.klondike = klondike;
		initBoard();
		play();
	}

	
	private void initBoard()
	{
		for (int i = 0 ; i < NB_ROWS ; i++)
		{
			List<String> line = new ArrayList<>();
			if (i == 0 || i == NB_ROWS - 1)
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
		String res = "" + card.getValue();
		if(card.getValue() < 10)
			res += " ";
		res += card.getColor().getName().substring(0, 2);		
		return res;
	}

	private String getSelectorString(CardStack stack, String selector)
	{
		String selectorString = NOTHING;
		if (stack == sourceStack)
			selectorString = SELECTED_STACK;
		if (klondike.canMove(sourceStack, stack))
			selectorString = selector;
		return selectorString;
	}
	
	private void play()
	{
		Scanner scanner = new Scanner(System.in);
		char choice = 'a';
		do
		{
			try
			{
				sourceStack = null;
				displayBoard();
				System.out.println("Source ? ");
				choice = scanner.next().charAt(0);
				if (choice == PICK_CHAR)
					klondike.pickCard();
				else
				{
					sourceStack = getSelectedStack(choice);
					displayBoard();		
					System.out.println("Cible ? ");
					choice = scanner.next().charAt(0);		
					CardStack destinationStack = getSelectedStack(choice);
					sourceStack.move(destinationStack);			
				}	
			}
			catch(Exception e)
			{
				System.out.println("Erreur de saisie");
//				e.printStackTrace();
			}
		}
		while(choice != EXIT_CHAR); 
		scanner.close();
	}


	@Override
	public String toString()
	{
		setBoard();
		String res = "";
		for (List<String> line : board)
		{
			for (String cell : line)
				res += cell;
			res += "\n";
		}
		return res;
	}
	
	
	
	private void setBoard()
	{
		printColorStacks();
		printAlternateColorStacks();
		printHeap();
	}

	private void printHeap()
	{
		HeapStack heap = klondike.getHeap();
		if (!heap.isEmpty())
		{
			board.get(HEAP_ROW).set(HEAP_COL, stringOfCard(heap.top()));
			board.get(HEAP_ROW).set(HEAP_COL - 1, getSelectorString(heap, HEAP_SELECTOR));
			board.get(HEAP_ROW - 1).set(HEAP_COL, (sourceStack == null) ? HEAP_PICKER : NOTHING);
		}
		else
		{
			board.get(HEAP_ROW).set(HEAP_COL, NOTHING);			
		}
	}

	private void printAlternateColorStacks()
	{
		int col = 0;			
		for (int index = 0 ; index < Klondike.NB_STACKS ; index++)
		{
			int row = 0;
			AlternateColorStack stack = klondike.getAlternateColorStack(index);
			if (!stack.isEmpty())
			{
				for (Card card : stack.getCards())
				{
					String str =  (row < stack.getNumberOfHiddenCards()) ? HIDDEN_CARD : stringOfCard(card);
					board.get(STACK_ROW + row).set(STACK_COL + 2*col, str);	
					row++;
				}
			}
			else
			{
				board.get(STACK_ROW + row).set(STACK_COL, EMPTY_STACK);
				board.get(SELECTOR_ROW).set(SELECTOR_COL + 2*col, NOTHING);
			}
			while (row < MAX_STACK_SIZE)
			{
				board.get(STACK_ROW + row).set(STACK_COL + 2*col, NOTHING);	
				row++;					
			}
			board.get(SELECTOR_ROW).set(SELECTOR_COL + 2*col, getSelectorString(stack, "(" + (col+1) + ") "));
			col++;
		}		
	}

	private void printColorStacks()
	{
		int column = COLOR_COL;
		for (int i = 0 ; i < 4 ; i++)
		{
			ColorStack stack = klondike.getColorStack(i);
			String card = EMPTY_STACK;
			if (!stack.isEmpty())
				card = stringOfCard(stack.top());
			String selectorString = (sourceStack == null) 
					? NOTHING 
					: getSelectorString(stack, "(" + (char)('a' + i) + ") ");
			board.get(COLOR_ROW - 1).set(column, selectorString);
			board.get(COLOR_ROW).set(column, card);
			column += 2;
		}
	}

	private void displayBoard()
	{
		System.out.println(this);
	}

	public static void main(String[] args)
	{
		new CommandLine(new Klondike());
	}
}
