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
	private static final Map<Character, String> TOKEN_MAP = createTokenMap();
	private static final Map<String, String> KEYWORD_MAP = createKeywordMap();
	
	private static final List<String> IGNORE_LIST = createIgnoreList();
	
	private static Map<Character, String> createTokenMap()
	{
		Map<Character, String> tokenMap = new HashMap<Character, String>();
		tokenMap.put(' ', "SPACE");
		tokenMap.put('\n', "NEWLINE");
		tokenMap.put('{', "LEFT-BRACE");
		tokenMap.put('}', "RIGHT-BRACE");
		tokenMap.put('(', "LEFT-PARENTHESIS");
		tokenMap.put(')', "RIGHT-PARENTHESIS");
		tokenMap.put('=', "EQUALS");
		tokenMap.put('!', "NOT");
		tokenMap.put(';', "SEMICOLON");
		return Collections.unmodifiableMap(tokenMap);
	}

	private static Map<String, String> createKeywordMap()
	{
		Map<String, String> keywordMap = new HashMap<String, String>();
		keywordMap.put("if", "KEYWORD-IF");
		keywordMap.put("else", "KEYWORD-ELSE");
		keywordMap.put("true", "KEYWORD-TRUE");
		keywordMap.put("false", "KEYWORD-FALSE");
		return Collections.unmodifiableMap(keywordMap);
	}
	
	private static List<String> createIgnoreList()
	{
		List<String> ignoreList = new ArrayList<String>();
		ignoreList.add("SPACE");
		ignoreList.add("NEWLINE");
		return Collections.unmodifiableList(ignoreList);
	}

	public static boolean isLetter(char character)
	{
		return character >= 'a' && character <= 'z';
	}

	public static List<String> getTokensFromFile(String inputPath) throws IOException
	{
		List<String> tokens = new ArrayList<String>();

		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(inputPath)))
		{
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
			{
				char[] chars = line.toCharArray();
				int loc = 0;

				while (loc < line.length())
				{
					String token;
					if ((token = TOKEN_MAP.get(chars[loc])) != null)
					{
						if (!IGNORE_LIST.contains(token))
						{
							tokens.add(token);
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
							tokens.add(token);
						}
						else
						{
							tokens.add("IDENTIFIER(" + word + ")");
						}
					}
					else
					{
						tokens.add("INVALID(" + chars[loc] + ")");
					}
					loc++;
				}
			}
		}
		return tokens;
	}

	public static boolean checkValidity(String inputPath) throws IOException
	{	
		List<String> tokens = getTokensFromFile(inputPath);
		for (String token : tokens)
		{
			System.out.println(token);
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
