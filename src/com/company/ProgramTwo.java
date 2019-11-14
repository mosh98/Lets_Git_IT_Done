package com.company;

import java.io.IOException;

public class ProgramTwo{


    public static void main(String[] args) throws IOException, TokenizerException, ParserException {
        String inputFileName = "/Users/moslehmahamud/Documents/IntelliJIdea/src/com/company/program1.txt";
        Tokenizer t = new Tokenizer();


            t.open(inputFileName);


            t.close();

    }

}
