import java.util.*;

class compile
{
	public static void main(String[] args)
	{
	Scanner scanner = new Scanner(args[0]);
	Parser parser = new Parser(scanner);
	parser.Parse();
	System.out.println(parser.errors.count + " errors detected");
	}	
}
