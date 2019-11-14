package com.company;

import java.io.IOException;

public class Parser implements IParser {


    public Tokenizer tokenizer = null;

    public StringBuilder stringBuilder = null;


    public void add_indents(int tabs){
        final String BLACK_SPACE = "  ";
        for(int i =0; i <tabs; i++){
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



    class BlockNode implements INode{

        StatementNode stmnt = null;

        public BlockNode(Tokenizer P_Tokenizer) throws IOException, TokenizerException {
            if(P_Tokenizer.current().token() == Token.LEFT_CURLY){
                while(P_Tokenizer.current().token() != Token.RIGHT_CURLY){
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
            stringBuilder.append(builder +"\n");
        }
    }

    class StatementNode implements INode{

        AssignmentNode assignmentNode = null;
       // can go back to Statement node again
        StatementNode stmt = null;


        public StatementNode(Tokenizer P_Tokenizer) {

            /***
             * go through the string and find the = symbol
             *if found move next and
             * */
           // assignmentNode = new AssignmentNode(P_Tokenizer);
            if (P_Tokenizer.current().token() != Token.EOF ){
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


     class AssignmentNode implements INode{

        ExpressionNode ex = null;

        public AssignmentNode(Tokenizer P_Tokenizer) throws IOException, TokenizerException {
        StringBuilder temp = new StringBuilder();

       if(P_Tokenizer.current().token() == Token.IDENT){
           String lexeme = P_Tokenizer.current().toString();
           temp.append("AssignmentNode");
           buildString(temp,1);
           temp.append(lexeme);
           buildString(temp,0);

       }
            P_Tokenizer.moveNext();

       while (P_Tokenizer.current().token() != Token.ASSIGN_OP){

           P_Tokenizer.moveNext();

       }
            System.out.print(P_Tokenizer.current().toString() + "\n");
                ex = new ExpressionNode(P_Tokenizer);

        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            //add_indents(tabs);

          //  stringBuilder.append(builder +"\n");

        }
    }

    class ExpressionNode implements INode{

        TermNode termNode = null;


        public ExpressionNode(Tokenizer tokenizer) throws IOException, TokenizerException {

            /**
             * Find term
             * Loop though the P_tokenizer
             * find the first + or - symbol
             *
             */

            termNode = new TermNode(tokenizer);

            while( tokenizer.current().token() !=  (Token.ADD_OP ) ){
                if(tokenizer.current().token() !=  (Token.SUB_OP ))
                        tokenizer.moveNext();
            }

            System.out.println(tokenizer.current().toString());
            tokenizer.moveNext();

            if(tokenizer.current().token() != Token.EOF){
                ExpressionNode x = new ExpressionNode(tokenizer);
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
    class TermNode implements INode{


        public TermNode(Tokenizer P_Tokenizer) {

        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            add_indents(tabs);
            stringBuilder.append(builder +"\n");
        }
    }
    class FactorNode implements INode{

        public FactorNode(Tokenizer P_Tokenizer) {
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {
            add_indents(tabs);
            stringBuilder.append(builder +"\n");
        }
    }





}
