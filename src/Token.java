
public class Token
{
	private TokenType type;
	private String image;
	
	public Token(TokenType type, String image)
	{
		this.type = type;
		this.image = image;
	}
	
	public Token(TokenType type, char image)
	{
		this(type, image + "");
	}
	
	public TokenType getType()
	{
		return type;
	}
	
	public String getImage()
	{
		return image;
	}
	
	public boolean isValid()
	{
		return type != TokenType.INVALID;
	}
	
}
