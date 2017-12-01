package klondike;

import java.util.Stack;

class History
{
	private Stack<Action> before  = new Stack<>(),
			after = new Stack<>();
	
	boolean canUndo()
	{
		return !before.isEmpty();
	}
	
	boolean canRedo()
	{
		return !after.isEmpty();
	}
	
	void run(Action action)
	{
		action.up();
		after.clear();
		before.push(action);
		System.out.println(action + " pushed");
	}
	
	void undo()
	{
		Action action = before.peek();
		action.down();
		before.pop();
		after.push(action);
	}

	void redo()
	{
		Action action = after.peek();
		action.up();
		after.pop();
		before.push(action);		
	}

}
