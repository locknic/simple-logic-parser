import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MiniParser
{
	private static final Map<Character, TokenType> TOKEN_MAP = createTokenMap();
	private static final Map<String, TokenType> KEYWORD_MAP = createKeywordMap();

	private static final List<TokenType> IGNORE_LIST = createIgnoreList();

	private static Map<Character, TokenType> createTokenMap()
	{
		Map<Character, TokenType> tokenMap = new HashMap<Character, TokenType>();
		tokenMap.put(' ', TokenType.SPACE);
		tokenMap.put('\n', TokenType.NEWLINE);
		tokenMap.put('{', TokenType.LEFT_BRACE);
		tokenMap.put('}', TokenType.RIGHT_BRACE);
		tokenMap.put('(', TokenType.LEFT_PARENTHESIS);
		tokenMap.put(')', TokenType.RIGHT_PARENTHESIS);
		tokenMap.put('=', TokenType.EQUALS);
		tokenMap.put('!', TokenType.NOT);
		tokenMap.put(';', TokenType.SEMICOLON);
		return Collections.unmodifiableMap(tokenMap);
	}

	private static Map<String, TokenType> createKeywordMap()
	{
		Map<String, TokenType> keywordMap = new HashMap<String, TokenType>();
		keywordMap.put("if", TokenType.KEYWORD_IF);
		keywordMap.put("else", TokenType.KEYWORD_ELSE);
		keywordMap.put("true", TokenType.KEYWORD_TRUE);
		keywordMap.put("false", TokenType.KEYWORD_FALSE);
		return Collections.unmodifiableMap(keywordMap);
	}

	private static List<TokenType> createIgnoreList()
	{
		List<TokenType> ignoreList = new ArrayList<TokenType>();
		ignoreList.add(TokenType.SPACE);
		ignoreList.add(TokenType.NEWLINE);
		return Collections.unmodifiableList(ignoreList);
	}

	public static boolean isLetter(char character)
	{
		return character >= 'a' && character <= 'z';
	}

	public static List<Token> getTokensFromFile(String inputPath) throws IOException
	{
		List<Token> tokens = new LinkedList<Token>();

		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(inputPath)))
		{
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
			{
				char[] chars = line.toCharArray();
				int loc = 0;

				while (loc < line.length())
				{
					TokenType token;
					if ((token = TOKEN_MAP.get(chars[loc])) != null)
					{
						if (!IGNORE_LIST.contains(token))
						{
							tokens.add(new Token(token, chars[loc]));
						}
					}
					else if (isLetter(chars[loc]))
					{
						String word = chars[loc] + "";
						while (loc + 1 < line.length() && isLetter(chars[loc + 1]))
						{
							loc++;
							word += chars[loc];
						}

						if ((token = KEYWORD_MAP.get(word)) != null)
						{
							tokens.add(new Token(token, word));
						}
						else
						{
							tokens.add(new Token(TokenType.IDENTIFIER, word));
						}
					}
					else
					{
						tokens.add(new Token(TokenType.INVALID, chars[loc]));
					}
					loc++;
				}
			}
		}
		return tokens;
	}

	public static boolean checkIfStatement(Token token)
	{
		if (token.getType() == TokenType.KEYWORD_IF)
		{
			
		}
		return false;
	}

	public static boolean checkValidity(String inputPath) throws IOException
	{
		List<Token> tokens = getTokensFromFile(inputPath);
		for (Token token : tokens)
		{
			System.out.println(token.getType().toString() + ", " + token.getImage());
		}
		return false;
	}

	public static void main(String[] args)
	{
		try
		{
			System.out.println(checkValidity(args[0]) ? "valid program" : "invalid program");
		}
		catch (IndexOutOfBoundsException | IOException e)
		{
			System.out.println("Please enter a valid input file as a command line parameter.");
			e.printStackTrace();
		}
	}
}
