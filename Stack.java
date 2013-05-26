import java.util.*;

public class Stack<T>
{
	//Field for array stack; integer for the top and array.
	private int top;
	private ArrayList<T> stack;
	
	/* A constructor that takes an integer value and creates
	 * a stack of that size and sets the top at 0.
	 */
	public Stack()
	{
		top = 0;
		stack = new ArrayList<T>();
	}
	// Tells the user is the list is empty or not.
	public boolean empty()
	{
		return top == 0;
	}
	/* Allows the addition of an integer value to
	 * a stack and adjusts the top to the next spot.
	 */
	public void push(T value)
	{
		stack.add(value);
		top++;
	}
	/* Allows the removal of an integer value from a 
	 * stack and adjusts the top to the spot below.
	 * Checks if the stack is empty first.
	 */
	public T pop()
	{
		if(empty())
			throw new IllegalStateException();
		else
		{
			top--;
			return stack.remove(top);
		}
	}
	/* Allows user to peek at the value in the position
	 * right before the top. Does not remove this value like
	 * pop does.
	 */
	public T peek()
	{
		if(empty())
			throw new IllegalStateException();
		else
		{
			return stack.get(top - 1);
		}
	}	
	public void drop()
	{
		
		top--;			
		stack.remove(top);
		
	}
	public void dup()
	{
		
		push(stack.get(top - 1));
		
	}
	public void swap()
	{
		
		T tempOne = pop();
		T tempTwo = pop();
		push(tempOne);
		push(tempTwo);
	
	}
	
	public void rot()
	{
		T tempOne = pop();
		T tempTwo = pop();
		T tempThree = pop();
		push(tempOne);
		push(tempThree);
		push(tempTwo);
	}
	// Prints out the contents of a stack if there are any values to print out.
	public void printStack()
	{
		if(empty())
		{
			System.out.println("Error: stack is empty, no values to print out.");
		}
		else
		{
			for(int i = stack.size()-1; i >= 0; i--)
			{
				System.out.println(stack.get(i));
			}
		}
	}
}
