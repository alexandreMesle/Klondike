package klondike;

class Move implements Action
{
	CardStack from, to;
	
	public Move(CardStack from, CardStack to)
	{
		this.from = from;
		this.to= to;
	}
	
	@Override
	public void up()
	{
		from.move(to);
	}
	
	@Override
	public void down()
	{
		to.move(from);
	}
	
	@Override
	public String toString()
	{
		return "Move from " +  from + " to " + to;
	}
}
