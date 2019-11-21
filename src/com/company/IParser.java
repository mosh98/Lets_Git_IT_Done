package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IParser {
	/**
	 * Opens a file for parsing.
	 */
	void open(String fileName) throws IOException, TokenizerException;
	
	/**
	 * Parses a program from file returning a parse tree (the root node of a parse tree).
	 */
	INode parse() throws Exception;

	/**
	 * Closes the file and releases any system resources associated with it.
	 */
	public void close() throws IOException ;
}