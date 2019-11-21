package com.company;

import org.w3c.dom.Node;
import org.w3c.dom.css.CSSFontFaceRule;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Stack;

public class Parser implements IParser {

    public Tokenizer t = null;
    public Object[] data = new Object[10];
    public HashMap<String, Double> variableMap = new HashMap<>();
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
        t = new Tokenizer();
        t.open(fileName);
        t.moveNext();


    }

    @Override
    public INode parse() throws Exception {
        if (t == null) {
            throw new IOException("No open file.");
        }
        return new BlockNode();
    }

    @Override
    public void close() throws IOException {
        if (t != null)
            t.close();
    }


    class BlockNode implements INode {

        StatementNode statementNode = null;

        BlockNode() throws Exception {
            if (t.current().token() == Token.LEFT_CURLY) {
                t.moveNext();
                statementNode = new StatementNode(t);
            } else {
                throw new TokenizerException("Error: No left curl");
            }
        }


        @Override
        public Object evaluate(Object[] args) throws Exception {

            statementNode.evaluate(data);

            StringBuilder sb = new StringBuilder();
            DecimalFormat df = new DecimalFormat("#.#");
            variableMap.forEach((k,v) -> sb.append(k + " = " + df.format(v) +"\n"));
            return sb;
        }

        @Override
        public void buildString(StringBuilder builder, int tabs) throws TokenizerException, ParserException, IOException {
            builder.append("BlockNode\n");
            tabs += 1;
            builder.append(t.current().toString());
            statementNode.buildString(builder, (tabs + 1));
            builder.append(t.current().toString());





        /*    builder.append("BlockNode\n");
            tabs += 1;
            if (t.current().token() == Token.LEFT_CURLY) {
                builder.append(t.current().toString());
                t.moveNext();
                statementNode = new StatementNode();
                statementNode.buildString(builder, (tabs + 1));
                builder.append(t.current().toString());
            } else {
                throw new TokenizerException("Error: No left curl");
            } */
        }

    }

    class StatementNode implements INode {

        AssignmentNode assignmentNode = null;
        StatementNode stmntNode = null;

        StatementNode(Tokenizer t) throws Exception {
            if(t.current().token() == Token.IDENT){
                assignmentNode = new AssignmentNode(t);
                t.moveNext();
                stmntNode = new StatementNode(t);
            }
        }



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
            builder.append("\t".repeat(tabs - 1) + "StatementNode\n");
            if(assignmentNode != null)
                assignmentNode.buildString(builder, (tabs+1));
            if(stmntNode != null) {
                stmntNode.buildString(builder, (tabs + 1));
            }


         /*   builder.append(add_indents(tabs - 1) + "StatementNode\n");
            if (t.current().token() == Token.IDENT) {
                assignmentNode = new AssignmentNode();
                assignmentNode.buildString(builder, (tabs + 1));
                t.moveNext();
                stmntNode = new StatementNode();
                stmntNode.buildString(builder, (tabs + 1));
            } */
        }
    }


    class AssignmentNode implements INode {


        ExpressionNode expressionNode = null;
        Lexeme identNode;
        Lexeme assignNode;
        String ident;

        AssignmentNode(Tokenizer t) throws Exception {
            if (t.current().token() == Token.IDENT) {
                ident = t.current().value().toString();
                identNode = t.current();
                t.moveNext();
                if (t.current().token() == Token.ASSIGN_OP) {
                    assignNode = t.current();
                    t.moveNext();
                    expressionNode = new ExpressionNode(t);
                    if (t.current().token() != Token.SEMICOLON) {
                        throw new ParserException("Could not find a Semicolon");
                    }
                } else {
                    throw new ParserException("Could not find a Assign operator");
                }
            } else {
                throw new ParserException("Could not find Identifier");
            }
        }


        @Override
        public Object evaluate(Object[] args) throws Exception {

            expressionNode.evaluate(data);
            if(ident != null)
                variableMap.put(ident, (double) data[0]);
            counter = 0;
            return null;
        }


        @Override
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException, ParserException {
            builder.append("\t".repeat(tabs - 1) + "AssigmentNode\n");
            builder.append("\t".repeat(tabs) + identNode);
            builder.append("\t".repeat(tabs) + assignNode);
            expressionNode.buildString(builder, (tabs+1));
            builder.append("\t".repeat(tabs) + "SEMICOLON ;" + "\n");




            /*builder.append(add_indents(tabs - 1) + "AssigmentNode\n");
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
            } */
        }
    }

    class ExpressionNode implements INode {

        TermNode termNode = null;
        ExpressionNode expressionNode = null;
        char operator = '!';
        Lexeme thisNode;

        ExpressionNode(Tokenizer t) throws Exception, ParserException,TokenizerException,IOException{
            termNode = new TermNode(t);
            switch(t.current().token()) {
                case ADD_OP:
                    operator = '+';
                    thisNode = t.current();
                    t.moveNext();
                    expressionNode = new ExpressionNode(t);
                    break;
                case SUB_OP:
                    operator = '-';
                    thisNode = t.current();
                    t.moveNext();
                    expressionNode = new ExpressionNode(t);
                    break;
            }
        }


        // This method wil first call it's child TermNode's evalutate method.
        // Thereafter it will check if the operator stack is empty or not.
        // If it's empty then it means
        @Override
        public Object evaluate(Object[] args) throws Exception {
            termNode.evaluate(args);

            //Checks if the Stack is empty, if it's not that means that there is a
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
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException, ParserException {
            builder.append("\t".repeat(tabs-1) + "ExpressionNode\n");
            termNode.buildString(builder, (tabs + 1));
            if(expressionNode != null){
                builder.append("\t".repeat(tabs)+ thisNode.toString());
                expressionNode.buildString(builder, tabs+1);
            }







            /*
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
            } */
        }
    }

    class TermNode implements INode {

        FactorNode factorNode = null;

        TermNode termNode = null;

        Lexeme thisNode = null;

        char operator = '!';


        TermNode(Tokenizer t) throws Exception, ParserException, TokenizerException, IOException{
            factorNode = new FactorNode(t);
            switch(t.current().token()) {
                case DIV_OP:
                    thisNode = t.current();
                    operator = '/';
                    t.moveNext();
                    termNode = new TermNode(t);
                    break;
                case MULT_OP:
                    thisNode = t.current();
                    operator = '*';
                    t.moveNext();
                    termNode = new TermNode(t);
                    break;
            }
        }


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
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException, ParserException {
            builder.append("\t".repeat(tabs - 1) + "TermNode\n");
            factorNode.buildString(builder, (tabs+1));
            if(termNode != null){
                builder.append("\t".repeat(tabs)+thisNode.toString());
                termNode.buildString(builder,tabs+1);
            }









/*
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
            }  */
        }
    }

    class FactorNode implements INode {

        ExpressionNode expressionNode = null;
        double number;
        String variable;
        Lexeme thisNode = null;

        FactorNode(Tokenizer t) throws Exception {
            switch(t.current().token()) {
                case INT_LIT:
                    thisNode = t.current();
                    number = (double)t.current().value();
                    t.moveNext();
                    break;
                case IDENT:
                    thisNode = t.current();
                    System.out.println(t.current());
                    variable = t.current().value().toString();
                    t.moveNext();
                    break;
                case LEFT_PAREN:
                    thisNode = t.current();
                    t.moveNext();
                    expressionNode = new ExpressionNode(t);
                    if(t.current().token() == Token.RIGHT_PAREN){
                        t.moveNext();
                    }else {throw new ParserException("There is no ')' after an Expression!");}
                    break;
            }
        }
        @Override
        public Object evaluate(Object[] args) throws Exception {
            if (expressionNode == null) {
                if (variable == null) {
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
        public void buildString(StringBuilder builder, int tabs) throws IOException, TokenizerException, ParserException {
          builder.append("\t".repeat(tabs-1) + "FactorNode\n");
          switch(thisNode.token()){
              case LEFT_PAREN:
                  builder.append("\t".repeat(tabs) + thisNode.toString() );
                  expressionNode.buildString(builder,(tabs+1));
                  builder.append("\t".repeat(tabs) + "RIGHT_PAREN )\n");
                  break;
              default:
                  builder.append("\t".repeat(tabs) + thisNode.toString());
                  break;


          }



         /*   builder.append(add_indents(tabs - 1) + "FactorNode\n");
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
            } */
        }
    }
}
