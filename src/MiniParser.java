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

	private static Map<Character, String> createTokenMap()
	{
		Map<Character, String> map = new HashMap<Character, String>();
		map.put(' ', "SPACE");
		map.put('\n', "NEWLINE");
		map.put('{', "LEFT-BRACE");
		map.put('}', "RIGHT-BRACE");
		map.put('(', "LEFT-PARENTHESIS");
		map.put(')', "RIGHT-PARENTHESIS");
		map.put('=', "EQUALS");
		map.put('!', "NOT");
		map.put(';', "SEMICOLON");
		return Collections.unmodifiableMap(map);
	}

	private static Map<String, String> createKeywordMap()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("if", "KEYWORD-IF");
		map.put("else", "KEYWORD-ELSE");
		map.put("true", "KEYWORD-TRUE");
		map.put("false", "KEYWORD-FALSE");
		return Collections.unmodifiableMap(map);
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
						tokens.add(token);
					}
					else
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
