package com.company;

import java.io.*;

public class Program {

	public static void main(String[] args) throws IOException {
		String inputFileName = "C:/Users/imran/Desktop/program1.txt";
		String outputFileName = "C:\\Users\\imran\\Desktop\\Parser\\Parser\\src\\com\\company\\test";
		IParser parser = null;
		INode root = null; // Root of the parse tree.
		StringBuilder builder;
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream("C:\\Users\\imran\\Desktop\\Parser\\Parser\\src\\com\\company\\test");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		OutputStreamWriter writer = new OutputStreamWriter(stream,"UTF-8");

		try {
			try {
				/*if (args.length < 2)
					throw new Exception("Incorrect number of parameters to program.");
				inputFileName = args[0];
				outputFileName = args[1]; */

				parser = new Parser();
				parser.open(inputFileName);
				root = parser.parse();
				builder = new StringBuilder();
				builder.append("PARSE TREE:\n");
				root.buildString(builder, 0);
				builder.append("\nEVALUATION:t4sr\n");
				builder.append(root.evaluate(null));

				stream = new FileOutputStream(outputFileName);
				writer = new OutputStreamWriter(stream);
				writer.write(builder.toString());
			}
			catch (Exception exception) {
				System.out.println("EXCEPTION: " + exception);
			}
			finally {
				if (parser != null)
					parser.close();
				if (writer != null)
					writer.close();
				if (stream != null)
					stream.close();
			}
		}
		catch (Exception exception) {
			System.out.println("EXCEPTION: " + exception);
		}
	}
}
