package com.company;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;

public class Parser implements IParser {

    public Tokenizer tokenizer = null;

    public Object[] data = new Object[10];

    public boolean flag = false;

    public StringBuilder stringBuilder = new StringBuilder();
    public ArrayList<Character> database = new ArrayList<>();
    double x = 0;



    public Object evaluateCalculate(StringBuilder builder) throws ScriptException {

        StringBuilder tempBuilder = new StringBuilder();
        ArrayList<Character> tempArr = new ArrayList<>();
        String tempString = builder.toString();

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

        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");

        System.out.println(engine.eval(tempBuilder.toString()));
        return  engine.eval(tempBuilder.toString());
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

        public BlockNode(Tokenizer P_Tokenizer) throws IOException, TokenizerException {
            if (P_Tokenizer.current().token() == Token.LEFT_CURLY) {
                while (P_Tokenizer.current().token() != Token.RIGHT_CURLY) {
                    P_Tokenizer.moveNext();
                    stmnt = new StatementNode(P_Tokenizer);
                }
            }
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {

            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            add_indents(tabs);
            builder.append(builder + "\n");
        }
    }

    class StatementNode implements INode {

        AssignmentNode assignmentNode = null;
        // can go back to Statement node again
        StatementNode stmt = null;


        public StatementNode(Tokenizer P_Tokenizer) {

            /***
             * go through the string and find the = symbol
             *if found move next and
             * */
            // assignmentNode = new AssignmentNode(P_Tokenizer);
            if (P_Tokenizer.current().token() != Token.EOF) {
                stmt = new StatementNode(P_Tokenizer);
            }

        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }


    class AssignmentNode implements INode {


        ExpressionNode ex = null;
        String lexeme = null;


        @Override
        public Object evaluate(Object[] args) throws Exception {

            if(flag = true){
                return evaluateCalculate(stringBuilder);

            }

            return null;
        }


        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append("AssigmentNode\n");
            tabs += 1;
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
                        stringBuilder.append(builder);
                        flag= true;
                        try {
                            x = (double) evaluateCalculate(stringBuilder);

                        }catch (ScriptException e){

                        }

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


        @Override
        public Object evaluate(Object[] args) throws Exception {
            termNode.evaluate(data);
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "ExpressionNode\n");
            termNode = new TermNode();
            termNode.buildString(builder, (tabs + 1));
            if (tokenizer.current().token() == Token.ADD_OP) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                en = new ExpressionNode();
                en.buildString(builder, tabs + 1);
            } else if (tokenizer.current().token() == Token.SUB_OP) {
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


        @Override
        public Object evaluate(Object[] args) throws Exception {

            fn.evaluate(data);
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "TermNode\n");
            fn = new FactorNode();
            fn.buildString(builder, (tabs + 1));
            if (tokenizer.current().token() == Token.DIV_OP) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                tn = new TermNode();
                tn.buildString(builder, (tabs + 1));
            } else if (tokenizer.current().token() == Token.MULT_OP) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                tn = new TermNode();
                tn.buildString(builder, tabs + 1);
            }
        }
    }

    class FactorNode implements INode {

        ExpressionNode ex = null;
        boolean temp = true;

        @Override
        public Object evaluate(Object[] args) throws Exception {
            while (temp) {
                if (tokenizer.current().token() == Token.LEFT_PAREN) {

                }
                tokenizer.moveNext();
            }
            return tokenizer.current().value();
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "FactorNode\n");

            if (tokenizer.current().token() == Token.INT_LIT) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
            } else if (tokenizer.current().token() == Token.IDENT) {
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
