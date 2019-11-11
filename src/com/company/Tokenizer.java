package com.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Tokenizer implements ITokenizer {

   final HashMap<String,String> tokenDictionary;

    {
        tokenDictionary = new HashMap<>();
        tokenDictionary.put("{","block");
        tokenDictionary.put("}","block");

        //id
        tokenDictionary.put("a","id");
        tokenDictionary.put("b","id");
        tokenDictionary.put("c","id");
        tokenDictionary.put("d","id");
        tokenDictionary.put("e","id");
        tokenDictionary.put("f","id");
        tokenDictionary.put("g","id");
        tokenDictionary.put("h","id");
        tokenDictionary.put("i","id");
        tokenDictionary.put("j","id");
        tokenDictionary.put("k","id");
        tokenDictionary.put("l","id");
        tokenDictionary.put("m","id");
        tokenDictionary.put("n","id");
        tokenDictionary.put("o","id");
        tokenDictionary.put("p","id");
        tokenDictionary.put("q","id");
        tokenDictionary.put("r","id");
        tokenDictionary.put("s","id");
        tokenDictionary.put("t","id");
        tokenDictionary.put("u","id");
        tokenDictionary.put("v","id");
        tokenDictionary.put("w","id");
        tokenDictionary.put("x","id");
        tokenDictionary.put("y","id");
        tokenDictionary.put("z","id");

        //int

        tokenDictionary.put("0","int");
        tokenDictionary.put("1","int");
        tokenDictionary.put("2","int");
        tokenDictionary.put("3","int");
        tokenDictionary.put("4","int");
        tokenDictionary.put("5","int");
        tokenDictionary.put("6","int");
        tokenDictionary.put("7","int");
        tokenDictionary.put("8","int");
        tokenDictionary.put("9","int");
    }


    public Map<String,String> getHasmap (){
        return tokenDictionary;
    }

    @Override
    public void open(String fileName) throws IOException, TokenizerException {


    }

    @Override
    public Lexeme current() {
        return null;
    }

    @Override
    public void moveNext() throws IOException, TokenizerException {

    }

    @Override
    public void close() throws IOException {

    }
}
