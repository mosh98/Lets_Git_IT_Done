package com.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class Parser implements IParser {

    public Tokenizer tokenizer = null;
    public Object[] data = new Object[10];
    public HashMap<Character, Double> variableMap = new HashMap<>();
    int counter = 0;
    Stack<Character> operatorStack = new Stack();

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
        return new BlockNode();
    }

    @Override
    public void close() throws IOException {
        if (tokenizer != null)
            tokenizer.close();
    }


    class BlockNode implements INode {

        StatementNode stmntNode = null;

        @Override
        public Object evaluate(Object[] args) throws Exception {

            stmntNode.evaluate(data);

            StringBuilder sb = new StringBuilder();
            variableMap.forEach((k,v) -> sb.append(k + " = " + v +"\n"));


            return sb;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append("BlockNode\n");
            tabs += 1;
            if (tokenizer.current().token() == Token.LEFT_CURLY) {
                builder.append(tokenizer.current().toString());
                tokenizer.moveNext();
                stmntNode = new StatementNode();
                stmntNode.buildString(builder, (tabs + 1));
                builder.append(tokenizer.current().toString());
            } else {
                throw new TokenizerException("Error: No left curl");
            }
        }

    }

    class StatementNode implements INode {

        AssignmentNode assignmentNode = null;
        StatementNode stmntNode = null;

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if (assignmentNode != null) {
                assignmentNode.evaluate(data);
            }
            if (stmntNode != null) {
                stmntNode.evaluate(data);
            }
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, ParserException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "StatementNode\n");
            if (tokenizer.current().token() == Token.IDENT) {
                assignmentNode = new AssignmentNode();
                assignmentNode.buildString(builder, (tabs + 1));
                tokenizer.moveNext();
                stmntNode = new StatementNode();
                stmntNode.buildString(builder, (tabs + 1));
            }
        }
    }


    class AssignmentNode implements INode {


        ExpressionNode expressionNode = null;
        char ident;

        @Override
        public Object evaluate(Object[] args) throws Exception {

            expressionNode.evaluate(data);
            variableMap.put(ident, (double) data[0]);
            counter = 0;
            return null;
        }


        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append(add_indents(tabs - 1) + "AssigmentNode\n");
            if (tokenizer.current().token() == Token.IDENT) {
                ident = tokenizer.current().value().toString().charAt(0);
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                if (tokenizer.current().token() == Token.ASSIGN_OP) {
                    builder.append(add_indents(tabs) + tokenizer.current().toString());
                    tokenizer.moveNext();
                    expressionNode = new ExpressionNode();
                    expressionNode.buildString(builder, (tabs + 1));
                    if (tokenizer.current().token() == Token.SEMICOLON) {
                        builder.append(add_indents(tabs) + tokenizer.current().toString());
                    } else {
                        throw new ParserException("Could not find a Semicolon");
                    }
                } else {
                    throw new ParserException("Could not find a Assign operator");
                }
            } else {
                throw new ParserException("Could not find Identifier");
            }
        }
    }

    class ExpressionNode implements INode {

        TermNode termNode = null;
        ExpressionNode expressionNode = null;
        Tokenizer token = tokenizer;
        char operator = '!';

        @Override
        public Object evaluate(Object[] args) throws Exception {
            termNode.evaluate(args);

            //Checks if the Stack is empty
            if (!operatorStack.isEmpty()) {

                //Checks if there is a parenthesis, if there is it will remove it from the stack
                if (operatorStack.peek() == '(') {
                    operatorStack.pop();

                } else if (operatorStack.peek() == '+') {
                    operatorStack.pop();
                    args[counter - 2] = (double) args[counter - 2] + (double) args[counter - 1];
                    counter--;
                } else if (operatorStack.peek() == '-') {
                    operatorStack.pop();
                    args[counter - 2] = (double) args[counter - 2] - (double) args[counter - 1];
                    counter--;
                }
            }
            //Check if this node has an operator, if it does it will add it to the stack
            if (operator != '!') {
                operatorStack.push(operator);
            }

            //Checks if the there is a child expression Node, if there is it will calls it's evaluate method
            if (expressionNode != null) {
                expressionNode.evaluate(args);
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
                expressionNode = new ExpressionNode();
                expressionNode.buildString(builder, tabs + 1);
            } else if (tokenizer.current().token() == Token.SUB_OP) {
                operator = '-';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                expressionNode = new ExpressionNode();
                expressionNode.buildString(builder, tabs + 1);
            }
        }
    }

    class TermNode implements INode {

        FactorNode factorNode = null;

        TermNode termNode = null;

        char operator = '!';


        @Override
        public Object evaluate(Object[] args) throws Exception {
            factorNode.evaluate(args);

            //Checks if the stack is empty and if the top last operation was a + or -, if not if will proceed and do either multiplication or division operation.
            if (!operatorStack.isEmpty() && operatorStack.peek() != '+' && operatorStack.peek() != '-') {
                if (operatorStack.peek() == '*') {
                    operatorStack.pop();
                    args[counter - 2] = (double) args[counter - 2] * (double) args[counter - 1];
                    counter--;
                } else if (operatorStack.peek() == '/') {
                    operatorStack.pop();
                    args[counter - 2] = (double) args[counter - 2] / (double) args[counter - 1];
                    counter--;
                }

            }
            //Check if this node has an operator, if it does it will add it to the stack
            if (operator != '!') {
                operatorStack.push(operator);
            }
            //Checks if the there is a child expression Node, if there is it will calls it's evaluate method

            if (termNode != null) {
                termNode.evaluate(args);
            }

            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "TermNode\n");
            factorNode = new FactorNode();
            factorNode.buildString(builder, (tabs + 1));
            if (tokenizer.current().token() == Token.DIV_OP) {
                operator = '/';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                termNode = new TermNode();
                termNode.buildString(builder, (tabs + 1));
            } else if (tokenizer.current().token() == Token.MULT_OP) {
                operator = '*';
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                termNode = new TermNode();
                termNode.buildString(builder, tabs + 1);
            }
        }
    }

    class FactorNode implements INode {

        ExpressionNode expressionNode = null;
        double number;
        char variable = '!';

        @Override
        public Object evaluate(Object[] args) throws Exception {
            if (expressionNode == null) {
                if (variable == '!') {
                    args[counter] = number;
                    counter++;
                    return number;
                } else {
                    args[counter] = variableMap.get(variable);
                    counter++;
                    return variable;
                }
            }

            //If expressionNode isn't null that means that there is a expression within parenthesis, so we add a parenthesis to the stack.
            operatorStack.push('(');
            expressionNode.evaluate(args);
            return null;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException {
            builder.append(add_indents(tabs - 1) + "FactorNode\n");

            if (tokenizer.current().token() == Token.INT_LIT) {
                number = (double) tokenizer.current().value();
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
            } else if (tokenizer.current().token() == Token.IDENT) {
                variable = tokenizer.current().value().toString().charAt(0);
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
            } else if (tokenizer.current().token() == Token.LEFT_PAREN) {
                builder.append(add_indents(tabs) + tokenizer.current().toString());
                tokenizer.moveNext();
                expressionNode = new ExpressionNode();
                expressionNode.buildString(builder, tabs + 1);
                if (tokenizer.current().token() == Token.RIGHT_PAREN) {
                    builder.append(add_indents(tabs) + tokenizer.current().toString());
                    tokenizer.moveNext();
                } else {
                    throw new TokenizerException("Error: Couldn't find right_paren");
                }
            }
        }
    }
}
