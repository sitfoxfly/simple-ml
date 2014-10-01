package simpleml.read;

import simpleml.struct.LabeledVector;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author mtkachenko
 */
public abstract class Parser {

  private boolean initialized;
  private URL source;

  public URL getSource() {
    return source;
  }

  public void setSource(File source) {
    try {
      setSource(source.toURI().toURL());
    } catch (MalformedURLException e) {
      throw new RuntimeException("wrong file path: " + source);
    }
  }

  public void setSource(String source) {
    try {
      setSource(new URL(source));
    } catch (MalformedURLException e) {
      try {
        setSource(new File(source).toURI().toURL());
      } catch (MalformedURLException e1) {
        throw new RuntimeException("wrong file path: " + source);
      }
    }
  }

  public void setSource(URL source) {
    this.source = source;
    initialized = false;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public void checkInitialization() {
    if (!initialized) {
      throw new IllegalStateException("reader is not initialized!");
    }
  }

  protected abstract void init() throws IOException;

  public final void initialize() throws IOException {
    if (isInitialized()) {
      return;
    }
    try {
      init();
      initialized = true;
    } catch (IOException e) {
      initialized = false;
      throw e;
    }
  }

  public abstract int numClasses();

  public abstract int numFeatures();

  public abstract List<LabeledVector> readAll() throws IOException;

}
