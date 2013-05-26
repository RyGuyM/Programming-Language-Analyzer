// This class is used to hold reference objects of any type T. Contains a single value.
public class RefObject <T>
{
	// Holds the value of a single generic T
	private T value;
	
	// Empty constructor for the refernce object.
	public RefObject()
	{
	}
	// Constructor that accepts an object T value for the value.
	public RefObject(T value)
	{
		this.value = value;
	}
	// Setter for the ref object.
	public void setValue(T value)
	{
		this.value = value;
	}
	// Getter for the ref object.
	public T getValue()
	{
		return value;
	}
}
