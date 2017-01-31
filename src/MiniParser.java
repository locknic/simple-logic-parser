import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
		tokenMap.put('\t', TokenType.TAB);
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
		ignoreList.add(TokenType.TAB);
		return Collections.unmodifiableList(ignoreList);
	}

	public static boolean isLetter(char character)
	{
		return character >= 'a' && character <= 'z';
	}

	public static TokenManager buildTokenManagerFromFile(String inputPath) throws IOException
	{
		TokenManager tokens = new TokenManager();
		int lineCounter = 0;

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
							tokens.addToken(new Token(token, chars[loc], lineCounter));
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
							tokens.addToken(new Token(token, word, lineCounter));
						}
						else
						{
							tokens.addToken(new Token(TokenType.IDENTIFIER, word, lineCounter));
						}
					}
					else
					{
						tokens.addToken(new Token(TokenType.INVALID, chars[loc], lineCounter));
					}
					loc++;
				}
				
				lineCounter++;
			}
		}
		return tokens;
	}

	public static boolean checkExpression(TokenManager tokenManager)
	{
		if (tokenManager.compareNextToken(TokenType.IDENTIFIER))
		{
			return true;
		}
		else if (tokenManager.compareNextToken(TokenType.KEYWORD_TRUE) || tokenManager.compareNextToken(TokenType.KEYWORD_FALSE))
		{
			return true;
		}
		else if (tokenManager.compareNextToken(TokenType.NOT))
		{
			if (checkExpression(tokenManager))
			{
				return true;
			}
		}
		else if (tokenManager.compareNextToken(TokenType.LEFT_PARENTHESIS))
		{
			if (checkExpression(tokenManager))
			{
				if (tokenManager.compareNextToken(TokenType.RIGHT_PARENTHESIS))
				{
					return true;
				}
			}
		}

		return false;
	}

	public static boolean checkStatement(TokenManager tokenManager)
	{
		if (tokenManager.compareNextToken(TokenType.IDENTIFIER))
		{
			if (tokenManager.compareNextToken(TokenType.EQUALS))
			{
				if (checkExpression(tokenManager))
				{
					if (tokenManager.compareNextToken(TokenType.SEMICOLON))
					{
						return true;
					}
				}
			}
		}
		else if (tokenManager.compareNextToken(TokenType.KEYWORD_IF)) 
		{
			if (tokenManager.compareNextToken(TokenType.LEFT_PARENTHESIS))
			{
				if (checkExpression(tokenManager))
				{
					if (tokenManager.compareNextToken(TokenType.RIGHT_PARENTHESIS))
					{
						if (checkStatement(tokenManager))
						{
							if (tokenManager.compareNextToken(TokenType.KEYWORD_ELSE))
							{
								if (checkStatement(tokenManager))
								{
									return true;
								}
							}
						}
					}
				}
			}
		}
		else if (tokenManager.compareNextToken(TokenType.LEFT_BRACE))
		{
			while (!tokenManager.compareNextToken(TokenType.RIGHT_BRACE))
			{
				if (!checkStatement(tokenManager))
				{
					return false;
				}
			}
			return true;
		}
		
		return false;
	}

	public static boolean checkValidity(String inputPath) throws IOException
	{
		TokenManager tokens = buildTokenManagerFromFile(inputPath);
		if (!tokens.isValid())
		{
			return false;
		}
		while (tokens.hasNextToken())
		{
			if (!checkStatement(tokens))
			{
				return false;
			}
		}
		return true;
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
