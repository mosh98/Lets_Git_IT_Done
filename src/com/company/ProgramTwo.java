package com.company;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ProgramTwo{


    public static void main(String[] args) throws IOException, TokenizerException, ParserException {
        String inputFileName = "/Users/moslehmahamud/Documents/IntelliJIdea/src/com/company/program1.txt";
            Parser parser = new Parser();
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream("/Users/moslehmahamud/Documents/IntelliJIdea/src/com/company/test.txt");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        OutputStreamWriter writer = new OutputStreamWriter(stream,"UTF-8");

    try {
        parser.open(inputFileName);
        parser.parse();


        while (parser.tokenizer.current().token() != Token.EOF ){
            System.out.println(parser.stringBuilder.toString());
        }


        parser.close();

    }catch (Exception exception){
        System.out.println(exception + "#&% ");
    }


    }

}
