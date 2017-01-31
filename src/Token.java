
public class Token
{
	private TokenType type;
	private String image;
	
	private int beginLine;
	
	public Token(TokenType type, String image, int beginLine)
	{
		this.type = type;
		this.image = image;
		this.beginLine = beginLine;
	}
	
	public Token(TokenType type, char image, int beginLine)
	{
		this(type, image + "", beginLine);
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
	
	public int getBeginLine()
	{
		return beginLine;
	}
	
}
