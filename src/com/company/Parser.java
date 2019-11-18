package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;

public class Parser implements IParser {

    public Tokenizer tokenizer = null;

    int counter = 0;


    public Object[] data = new Object[10];

    public boolean flag = false;

    public StringBuilder stringBuilder = new StringBuilder();


    public ArrayList<Character> database = new ArrayList<>();
    double x = 0;

    public double eval() {

        if(flag = false){
            return Double.parseDouble(null);
        }

        String tempString = stringBuilder.toString();

        StringBuilder tempBuilder = new StringBuilder();
        ArrayList<Character> tempArr = new ArrayList<>();

        for (int i = 0; i < tempString.length(); i++) {
            char thisChar = tempString.charAt(i);
            if (Character.isDigit(thisChar)) {
                tempArr.add(thisChar);
            } else if (thisChar == '+') {
                tempArr.add(thisChar);
            } else if (thisChar == '-') {
                tempArr.add(thisChar);
            } else if (thisChar == '/') {
                tempArr.add(thisChar);
            } else if (thisChar == '*') {
                tempArr.add(thisChar);
            } else if (thisChar == '(') {
                tempArr.add(thisChar);
            } else if (thisChar == ')') {
                tempArr.add(thisChar);
            } else if (thisChar == '.') {
                tempArr.add('.');
            }

        }
        for (Character c : tempArr) {
            tempBuilder.append(c);
        }

        String str = tempBuilder.toString();

        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public String add_indents(int tabs) {
        String BLACK_SPACE = "";
        for (int i = 0; i < tabs; i++) {
            BLACK_SPACE = BLACK_SPACE + "\t";
        }
        return BLACK_SPACE;
    }

    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        tokenizer = new Tokenizer();
        tokenizer.open(fileName);
        tokenizer.moveNext();


    }

    @Override
    public INode parse() throws IOException, TokenizerException, ParserException {
        if (tokenizer == null) {
            throw new IOException("No open file.");
        }
        return new AssignmentNode();
    }

    @Override
    public void close() throws IOException {
        if (tokenizer != null)
            tokenizer.close();
    }


    class BlockNode implements INode {

        StatementNode stmnt = null;


        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(flag = true){

                return eval();

            }
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append("BlockNode\n");
            tabs +=1;
            if (tokenizer.current().token() == Token.LEFT_CURLY) {
                builder.append(tokenizer.current().toString());
                tokenizer.moveNext();
                stmnt = new StatementNode();
                stmnt.buildString(builder, (tabs+1));
                builder.append(tokenizer.current().toString());
            } else {throw new TokenizerException("Error: No left curl");}
           // flag = true;
        }

    }

    class StatementNode implements INode {

        AssignmentNode assignmentNode = null;
        StatementNode stmt = null;

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(assignmentNode != null){
                stmt = new StatementNode();
            }
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, ParserException, TokenizerException {
            builder.append(add_indents(tabs-1) + "StatementNode\n");
            if(tokenizer.current().token() == Token.IDENT){
                assignmentNode = new AssignmentNode();
                assignmentNode.buildString(builder, (tabs+1));
                tokenizer.moveNext();
                stmt = new StatementNode();
                stmt.buildString(builder, (tabs+1));
            }
        }
    }


    class AssignmentNode implements INode {


        ExpressionNode ex = null;
        String lexeme = null;


        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(ex != null){
                ex.evaluate(data);
                return data[0];
            }
            return "Hellloooo";



        /*    int i =0;
            while(tokenizer.current().token() != Token.EOF){
                if(tokenizer.current().token() != Token.IDENT && tokenizer.current().token() != Token.ASSIGN_OP && tokenizer.current().token() != Token.SEMICOLON){
                    data[i] = tokenizer.current().toString();
                    i++;
                }

            }
            ex.evaluate(data); */

        }


        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append(add_indents(tabs-1)+"AssigmentNode\n");
           // tabs += 1;
            if (tokenizer.current().token() == Token.IDENT) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                if (tokenizer.current().token() == Token.ASSIGN_OP) {
                    builder.append(add_indents(tabs) + tokenizer.current().toString());
                    tokenizer.moveNext();
                    ex = new ExpressionNode();
                    ex.buildString(builder, (tabs + 1));
                    if (tokenizer.current().token() == Token.SEMICOLON) {
                        builder.append(add_indents(tabs) + tokenizer.current().toString());
                       // stringBuilder.append(builder);
                        flag= true;




                    } else {
                        throw new ParserException("Could not find a Semicolon");
                    }
                } else {
                    throw new ParserException("Could not find a Assign operatior");
                }
            } else {
                throw new ParserException("Could not find Identifier");
            }
        }
    }

    class ExpressionNode implements INode {

        TermNode termNode = null;
        ExpressionNode en = null;
        Tokenizer token = tokenizer;
        char operator = ',';

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if(en !=null){
                en.evaluate(args);
            }
            termNode.evaluate(args);
            if(operator == '+'){
                args[counter-2] = (double)args[counter-1] + (double)args[counter-2];
                counter--;
            }
            else if(operator == '-'){
                args[counter-2] = (double)args[counter-1] - (double)args[counter-2];
                counter--;
            }
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "ExpressionNode\n");
            termNode = new TermNode();
            termNode.buildString(builder, (tabs + 1));
            if (tokenizer.current().token() == Token.ADD_OP) {
                operator = '+';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                en = new ExpressionNode();
                en.buildString(builder, tabs + 1);
            } else if (tokenizer.current().token() == Token.SUB_OP) {
                operator ='-';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                en = new ExpressionNode();
                en.buildString(builder, tabs + 1);
            }
        }
    }

    class TermNode implements INode {

        FactorNode fn = null;

        TermNode tn = null;

        char operator = ',';


        @Override
        public Object evaluate(Object[] args) throws Exception {

            if(tn != null){
                 tn.evaluate(args);
            }
            fn.evaluate(args);
                if(operator == '*'){
                    args[counter-2] = (double)args[counter-1] * (double)args[counter-2];
                    counter--;
                }else if(operator == '/'){
                    args[counter-2] = (double)args[counter-1] / (double)args[counter-2];
                    counter--;
                }
                return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "TermNode\n");
            fn = new FactorNode();
            fn.buildString(builder, (tabs + 1));
            if (tokenizer.current().token() == Token.DIV_OP) {
                operator = '/';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                tn = new TermNode();
                tn.buildString(builder, (tabs + 1));
            } else if (tokenizer.current().token() == Token.MULT_OP) {
                operator = '*';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                tn = new TermNode();
                tn.buildString(builder, tabs + 1);
            }
        }
    }

    class FactorNode implements INode {

        ExpressionNode ex = null;
        double num;
        char var = ',';

        @Override
        public Object evaluate(Object[] args) throws Exception {
                if(ex == null){
                    if(var == ','){
                        args[counter] = num;
                        counter++;
                        return num;
                    }else{

                        args[counter] = var;
                        counter++;
                        return var;
                    }
                }
                ex.evaluate(args);
                return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "FactorNode\n");

            if (tokenizer.current().token() == Token.INT_LIT) {
                num = (double)tokenizer.current().value();
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
            } else if (tokenizer.current().token() == Token.IDENT) {
                var = (char)tokenizer.current().value();
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
            } else if (tokenizer.current().token() == Token.LEFT_PAREN) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                ex = new ExpressionNode();
                ex.buildString(builder, tabs + 1);
                if (tokenizer.current().token() == Token.RIGHT_PAREN) {
                    builder.append(add_indents(tabs) + tokenizer.current().toString());
                    tokenizer.moveNext();
                } else {
                    throw new TokenizerException("couldnt find right_paren");
                }
            }
        }
    }


}
