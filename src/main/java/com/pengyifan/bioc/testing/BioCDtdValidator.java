package com.pengyifan.bioc.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.custommonkey.xmlunit.Validator;
import org.custommonkey.xmlunit.exceptions.ConfigurationException;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;
import org.xml.sax.SAXException;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

/**
 * Validate the BioC file via the command line.
 */
public class BioCDtdValidator {

  @Option(name = "-h", help=true, usage = "print this help")
  private boolean help = false;

  @Option(name = "-dtd", required = true, usage = "set DTD file")
  private String dtdFilename;

  @Argument
  private List<String> arguments = Lists.newArrayList();

  /**
   * BioCValidation [options...] arguments...
   * 
   * @param args arguments
   */
  public static void main(String[] args) {
    new BioCDtdValidator().doMain(args);
  }

  @VisibleForTesting
  String getDtdFilename() {
    return dtdFilename;
  }

  @VisibleForTesting
  boolean getHelp() {
    return help;
  }

  @VisibleForTesting
  List<String> getArguments() {
    return arguments;
  }

  public void doMain(String[] args) {

    ParserProperties.defaults()
        .withUsageWidth(80);
    CmdLineParser parser = new CmdLineParser(this);

    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      // if there's a problem in the command line,
      // you'll get this exception. this will report
      // an error message.
      System.err.println(e.getMessage());
      System.err.println("java BioCValidation [options...] arguments...");
      // print the list of available options
      parser.printUsage(System.err);
      System.err.println();
      return;
    }

    if (help) {
      System.err.println("java BioCValidation [options...] arguments...");
      // print the list of available options
      parser.printUsage(System.err);
      System.err.println();
      return;
    }

    File dtdFile = new File(dtdFilename);
    if (!dtdFile.exists()) {
      System.err.println("cannot find the DTD file");
      return;
    }

    if (arguments.isEmpty()) {
      System.err.println("No argument is given");
      return;
    }

    for (String biocFilename : arguments) {
      File biocFile = new File(biocFilename);
      if (!biocFile.exists()) {
        System.err.println("cannot read bioc file: " + biocFilename);
      } else {
        try {
          assertAndPrintDtdValid(
              new FileReader(biocFile),
              dtdFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        System.out.printf("testing %s: PASSED\n", biocFilename);
      }
    }
  }
  
  /**
   * Asserts that a BioC file is valid based on the given DTD file. If the BioC
   * file is invalid, prints the error message.
   * 
   * @param reader BioC file reader stream
   * @param dtdFilename the absolute URI of the BioC DTD file
   */
  public void assertAndPrintDtdValid(Reader reader, String dtdFilename) {
    try {
      Validator v = new Validator(reader, dtdFilename);
      v.assertIsValid();
    } catch (ConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (AssertionFailedError e) {
      System.out.println(e.getMessage());
    }
  }
}