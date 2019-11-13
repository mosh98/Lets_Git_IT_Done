package com.company;

import java.io.IOException;

public class Parser implements IParser {

    Tokenizer tokenizer = null;

    StringBuilder stringBuilder = null;


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
        return new BlockNode(tokenizer);
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
            String s = String.format("%1$"+tabs+"s", "");
            stringBuilder.append(builder +s +"\n");
        }
    }

    class StatementNode implements INode{

        AssignmentNode assignmentNode = null;
       // can go back to Statement node again
        StatementNode stmt = null;


        public StatementNode(Tokenizer P_Tokenizer) {
            assignmentNode = new AssignmentNode(P_Tokenizer);
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

        public AssignmentNode(Tokenizer P_Tokenizer){




        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {


        }
    }

    class ExpressionNode implements INode{


        public ExpressionNode(Tokenizer P_Tokenizer) {
        }

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

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

        }
    }





}
