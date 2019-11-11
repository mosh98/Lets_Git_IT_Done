package com.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Tokenizer implements ITokenizer {

    private Scanner scanner = null;
    private Lexeme current = null;
    private Lexeme next = null;


    //All datasets
  private HashMap<Character,Token> operators = new HashMap<>();
  private HashSet<Integer> int_Set = new HashSet<>();
  private HashSet<Character> id_Set = new HashSet<>();

  //Constructor
    public Tokenizer() {
        for (int i =0; i<10; i++){
            int_Set.add(i);
        }

        id_Set.add('a');
        id_Set.add('b');
        id_Set.add('c');
        id_Set.add('d');
        id_Set.add('e');
        id_Set.add('f');
        id_Set.add('g');
        id_Set.add('h');
        id_Set.add('i');
        id_Set.add('j');
        id_Set.add('k');
        id_Set.add('l');
        id_Set.add('m');
        id_Set.add('n');
        id_Set.add('o');
        id_Set.add('p');
        id_Set.add('q');
        id_Set.add('r');
        id_Set.add('s');
        id_Set.add('t');
        id_Set.add('u');
        id_Set.add('v');
        id_Set.add('w');
        id_Set.add('x');
        id_Set.add('y');
        id_Set.add('z');


        //adding operators with their tokens
        operators.put()

    }

    /**
     * Opens a file for tokenizing.
     */
    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        scanner = new Scanner();
        scanner.open(fileName);
        scanner.moveNext();
        next = extractLexeme();

    }

    private void consumeWhiteSpaces() throws IOException {
        while (Character.isWhitespace(scanner.current())){
            scanner.moveNext();
        }
    }


    /**
     * Returns the current token in the stream.
     */
    @Override
    public Lexeme current() {
        return null;
    }


    /**
     * Moves current to the next token in the stream.
     */
    @Override
    public void moveNext() throws IOException, TokenizerException {

    }


    private Lexeme extractLexeme() throws IOException, TokenizerException{


        return null;
    }

    @Override
    public void close() throws IOException {
        if (scanner != null)
            scanner.close();
    }
}
