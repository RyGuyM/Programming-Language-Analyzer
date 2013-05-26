// Will hold the information on a specific token, the type and the value.
public class Pair
{
	// Field values for the pair object.
	private String value;             // Value of token
	private String token;             // Type of token.
	 
	// Empty Constructor
	public Pair()
	{
	}
	// Constructor to set both value and token.
	public Pair(String v, String t)
	{
		token = v;
		value = t;
	}
	// Setter for the value.
	public void setValue(String v)
	{
		value = v;
	}
	// Setter for the token.
	public void setToken(String t)
	{
		token = t;
	}
	// Getter for the value.
	public String getValue()
	{
		return value;
	}
	// Getter for the token.
	public String getToken()
	{
		return token;
	}
	// To string method for the token.
	public String toString()
	{
		return token + " " + value + "\n";
	}
}