package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class Parser implements IParser {

    public Tokenizer tokenizer = null;

    public StringBuilder stringBuilder = new StringBuilder();

    public ArrayList<Character> database = new ArrayList<>();

    public void add_indents(int tabs) {
        final String BLACK_SPACE = "  ";
        for (int i = 0; i < tabs; i++) {
            stringBuilder.append(BLACK_SPACE);
        }
    }

    @Override
    public void open(String fileName) throws IOException, TokenizerException {
        tokenizer = new Tokenizer();
        tokenizer.open(fileName);
        tokenizer.moveNext();


    }

    @Override
    public INode parse() throws IOException, TokenizerException, ParserException {
        if (tokenizer == null)
            throw new IOException("No open file.");

        return new AssignmentNode(tokenizer);
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
            stringBuilder.append(builder + "\n");
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

        public AssignmentNode(Tokenizer P_Tokenizer) throws IOException, TokenizerException, ParserException {

            //stringBuilder.append("AssigmentNode ");

            if (P_Tokenizer.current().token() == Token.IDENT) {

                stringBuilder.append(P_Tokenizer.current().toString());

                P_Tokenizer.moveNext();

                if (P_Tokenizer.current().token() == Token.ASSIGN_OP) {
                    stringBuilder.append(P_Tokenizer.current().toString());
                    P_Tokenizer.moveNext();
                    ex = new ExpressionNode(P_Tokenizer);

                    if (P_Tokenizer.current().token() == Token.SEMICOLON) {
                        stringBuilder.append(P_Tokenizer.current().toString());

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

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            //add_indents(tabs);

            builder.append(stringBuilder);

        }
    }

    class ExpressionNode implements INode {

        TermNode termNode = null;
        ExpressionNode en = null;

        public ExpressionNode(Tokenizer P_tokenizer) throws IOException, TokenizerException {

           // stringBuilder.append("ExpressionNode");
            termNode = new TermNode(P_tokenizer);
            if (P_tokenizer.current().token() == Token.ADD_OP) {
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
                en = new ExpressionNode(P_tokenizer);
            } else if (P_tokenizer.current().token() == Token.SUB_OP) {
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
                en = new ExpressionNode(P_tokenizer);
            }



        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            add_indents(tabs);
            stringBuilder.append(builder);
        }
    }

    class TermNode implements INode {

        FactorNode fn = null;

        TermNode tn = null;

        boolean temp = true;

        public TermNode(Tokenizer P_tokenizer) throws IOException, TokenizerException {
            //stringBuilder.append("TermNode");
            fn = new FactorNode(P_tokenizer);
            if (P_tokenizer.current().token() == Token.DIV_OP) {
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
                tn = new TermNode(P_tokenizer);
            } else if (P_tokenizer.current().token() == Token.MULT_OP) {
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
                tn = new TermNode(P_tokenizer);
            }




        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            add_indents(tabs);
            stringBuilder.append(builder);
        }
    }

    class FactorNode implements INode {

        ExpressionNode ex = null;


        public FactorNode(Tokenizer P_tokenizer) throws IOException, TokenizerException {
            //stringBuilder.append("FactorNode");

            if (P_tokenizer.current().token() == Token.INT_LIT) {
                //stringBuilder.append(P_tokenizer.current());
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
            } else if (P_tokenizer.current().token() == Token.IDENT) {
                stringBuilder.append(P_tokenizer.current().toString());
                //stringBuilder.append(P_tokenizer.current());
                P_tokenizer.moveNext();
            } else if (P_tokenizer.current().token() == Token.LEFT_PAREN) {
                stringBuilder.append(P_tokenizer.current().toString());
                P_tokenizer.moveNext();
                ex = new ExpressionNode(P_tokenizer);
                if(P_tokenizer.current().token() == Token.RIGHT_PAREN){
                    stringBuilder.append(P_tokenizer.current().toString());
                    P_tokenizer.moveNext();
                } else{
                    throw new TokenizerException("couldnt find right_paren");
                }
            }


            database.add((char) tokenizer.current().value());


        }

        @Override
        public Object evaluate(Object[] args) throws Exception {


            return tokenizer.current().value();
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }



}
