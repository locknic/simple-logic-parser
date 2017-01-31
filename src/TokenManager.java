import java.util.LinkedList;

public class TokenManager
{
	private LinkedList<Token> tokens;
	private boolean valid;
	
	public TokenManager() 
	{
		tokens = new LinkedList<Token>();
		valid = true;
	}
	
	public boolean isValid()
	{
		return valid;
	}
	
	public void addToken(Token token)
	{
		if (!token.isValid())
		{
			valid = false;
		}
		
		tokens.add(token);
	}
	
	public boolean hasNextToken()
	{
		return !tokens.isEmpty();
	}
	
	public Token getNextToken()
	{
		return tokens.poll();
	}
	
	public Token peekNextToken()
	{
		return tokens.peek();
	}
	
	public boolean compareNextToken (TokenType type)
	{
		if (hasNextToken())
		{
//			PUT THIS BACK IN TO PRINT OUT WHERE THE PROGRAM IS BREAKING
//			System.out.println(peekNextToken().getBeginLine() + ", " + peekNextToken().getImage() + ", " + peekNextToken().getType());
			if (peekNextToken().getType() == type)
			{
				tokens.removeFirst();
				return true;
			}
		}
		return false;
	}

}
