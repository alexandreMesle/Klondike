package klondike;

public class Pick implements Action 
{
	private HeapStack heap;
	
	public Pick(HeapStack heap) 
	{
		this.heap = heap;
	}

	@Override
	public void up() 
	{
		if (!heap.hasNext())
			heap.reset();
		heap.next();
	}

	@Override
	public void down() 
	{
		//TODO implements down for Pick
	}
}
