package org.biocreative.bioc.io.standard;

import java.io.Reader;
import java.io.Writer;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.biocreative.bioc.io.BioCCollectionReader;
import org.biocreative.bioc.io.BioCCollectionWriter;
import org.biocreative.bioc.io.BioCDocumentReader;
import org.biocreative.bioc.io.BioCDocumentWriter;
import org.biocreative.bioc.io.BioCXMLStrategy;

public class JdkStrategy implements BioCXMLStrategy {

  @Override
  public BioCCollectionWriter createBioCCollectionWriter(Writer out)
      throws XMLStreamException {
    return new BioCCollectionWriterImpl(out);
  }

  @Override
  public BioCDocumentWriter createBioCDocumentWriter(Writer out)
      throws XMLStreamException {
    return new BioCDocumentWriterImpl(out);
  }

  @Override
  public BioCCollectionReader createBioCCollectionReader(Reader in)
      throws XMLStreamException {
    try {
      return new BioCCollectionReaderImpl(in);
    } catch (FactoryConfigurationError e) {
      throw new XMLStreamException(e);
    }
  }

  @Override
  public BioCDocumentReader createBioCDocumentReader(Reader in)
      throws FactoryConfigurationError, XMLStreamException {
    return new BioCDocumentReaderImpl(in);
  }

}
