//import Scanner;

public class AdaBaby {

		public static void main(String[] args) {
			for (int i = 0; i <= 80; i++){
				System.out.print("_");	
			}
			System.out.println("\nAdaBaby (ITEC 460, Spring 2020) Jeffrey Smith");
			System.out.println("______________________________________________________");

			if (args.length > 0) {
				System.out.println("   Reading source file " + args[0]);
				Scanner scanner = new Scanner(args[0]);
				System.out.println("   Parsing source file " + args[0]);
				Parser parser = new Parser(scanner);
				parser.Parse();

				if (parser.errors.count == 1)
					System.out.println("-- 1 error dectected");
				else
					System.out.println("-- " + parser.errors.count + " errors dectected");

			}
			else
				System.out.println("Syntax: JavaParser <java source file>");

		}

} // JavaParser
