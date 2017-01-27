
public class Token
{
	private String tokenName;
	private String image;
	
	public Token(String tokenName, String image)
	{
		this.tokenName = tokenName;
		this.image = image;
	}
	
	public Token(String tokenName, char image)
	{
		this.tokenName = tokenName;
		this.image = image + "";
	}
	
	public String getTokenName()
	{
		return tokenName;
	}
	public String getImage()
	{
		return image;
	}
	
}
