package com.wiseowl.WiseOwl.query;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class TestFocus {
	public static void main(String args[]) throws IOException
	{
		
		String question="Who is Abraham Lincoln?";
		FocusNoun fn=new FocusNoun();
		String fnn[]=fn.getFocusNoun(question);
		System.out.println(fnn[0]+fnn[1]);
		
	}
}
