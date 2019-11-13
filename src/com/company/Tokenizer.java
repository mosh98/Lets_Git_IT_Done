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




  //Constructor
    public Tokenizer() {

        //adding operators with their tokens

        operators.put('=',Token.ASSIGN_OP);
        operators.put('/',Token.DIV_OP);
        operators.put('*',Token.MULT_OP);
        operators.put('-',Token.SUB_OP);
        operators.put('(',Token.LEFT_PAREN);
        operators.put(')',Token.RIGHT_PAREN);
        operators.put('{',Token.LEFT_CURLY);
        operators.put('}',Token.RIGHT_CURLY);
        operators.put(';',Token.SEMICOLON);
        operators.put('+',Token.ADD_OP);

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
     * @Returns the current token in the stream.
     */
    @Override
    public Lexeme current() {
        return current;
    }


    /**
     * Moves current to the next token in the stream.
     */
    @Override
    public void moveNext() throws IOException, TokenizerException {
        if (scanner == null)
            throw new IOException("No open file.");
        current = next;
        if (next.token() != Token.EOF)
            next = extractLexeme();
    }

    /**
     *
     * @return returns Lexemes with their designated Tokens
     * @throws IOException
     * @throws TokenizerException
     */

    private Lexeme extractLexeme() throws IOException, TokenizerException {

        consumeWhiteSpaces();

        Character ch = scanner.current();
        StringBuilder strBuilder = new StringBuilder();
        String lexemeString;

        if(ch == null)
            return new Lexeme(ch,Token.NULL);

        if (ch == Scanner.EOF)
            return new Lexeme(ch, Token.EOF);//checks if the it is end of file or empty then it just stops.

        else if (Character.isDigit(ch)) {
            while (Character.isDigit(scanner.current())) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
            }
            lexemeString = strBuilder.toString();
            return new Lexeme(lexemeString, Token.INT_LIT);

        } else if (Character.isLetter(ch)) {
            while (Character.isLetter(scanner.current())) {
                strBuilder.append(scanner.current());
                scanner.moveNext();
            }
            lexemeString = strBuilder.toString();
            return new Lexeme(lexemeString, Token.IDENT);

        } else if (operators.containsKey(ch)) {
            scanner.moveNext();
                return new Lexeme(ch,operators.get(ch));
        } else {
            throw new TokenizerException("Unknown character: " + ch);
        }

    }

    /**
     * Closes the file and releases any system resources associated with it.
     */

    @Override
    public void close() throws IOException {
        if (scanner != null)
            scanner.close();
    }
}
