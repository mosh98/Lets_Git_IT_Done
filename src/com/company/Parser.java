package com.company;

import java.io.IOException;

public class Parser implements IParser {
    @Override
    public void open(String fileName) throws IOException, TokenizerException {

    }

    @Override
    public INode parse() throws IOException, TokenizerException, ParserException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }


    class AssignmentNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }

    class ExpressionNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }
    class TermNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }
    class FactorNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }

    class BlockNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }

    class StatementNode implements INode{

        @Override
        public Object evaluate(Object[] args) throws Exception {
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) {

        }
    }


}
