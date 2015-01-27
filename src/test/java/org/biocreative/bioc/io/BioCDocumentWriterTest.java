package org.biocreative.bioc.io;

import static org.biocreative.bioc.testing.BioCAssert.assertEquivalentTo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.biocreative.bioc.BioCCollection;
import org.biocreative.bioc.BioCDocument;
import org.biocreative.bioc.io.BioCCollectionReader;
import org.biocreative.bioc.io.BioCFactory;
import org.biocreative.bioc.io.standard.JdkStrategy;

public class BioCDocumentWriterTest {

  private static final String XML_FILENAME = "xml/PMID-8557975-simplified-sentences.xml";

  @Rule
  public TemporaryFolder testFolder = new TemporaryFolder();

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  
  @Test
  public void test_success_jdk()
      throws Exception {
    test_success(new JdkStrategy());
  }

  private void test_success(BioCXMLStrategy strategy)
      throws Exception {
    // read
    BioCCollectionReader reader = BioCFactory.newFactory(strategy)
        .createBioCCollectionReader(
            new InputStreamReader(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(XML_FILENAME)));
    BioCCollection collection = reader.readCollection();
    String dtd = reader.getDTD();
    reader.close();

    // write
    File tmpFile = testFolder.newFile();
    BioCDocumentWriter writer = BioCFactory.newFactory(strategy)
        .createBioCDocumentWriter(new FileWriter(tmpFile));
    writer.setDTD(dtd);
    writer.writeBeginCollectionInfo(collection);
    
    for(BioCDocument document: collection.getDocuments()) {
      writer.writeDocument(document);
    }
    
    writer.close();

    // test
    assertEquivalentTo(
        new InputStreamReader(Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(XML_FILENAME)),
        new FileReader(tmpFile));
  }
}
