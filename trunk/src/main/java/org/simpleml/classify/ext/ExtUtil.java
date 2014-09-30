package org.simpleml.classify.ext;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sitfoxfly
 */
public class ExtUtil {

  private static final String LOAD_METHOD_NAME = "load";

  public static <T extends ExternalizableModel> T load(Class<T> clazz, InputStream in) throws IOException, LoadException {
    try {
      final Method loadMethod = clazz.getMethod(LOAD_METHOD_NAME, InputStream.class);
      return clazz.cast(loadMethod.invoke(null, in));
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
      throw new LoadException(e);
    }
  }

  public static <T extends ExternalizableModel> T load(Class<T> clazz, File inFile) throws IOException, LoadException {
    FileInputStream in = new FileInputStream(inFile);
    T instance = load(clazz, in);
    in.close();
    return instance;
  }

  public static void save(ExternalizableModel model, OutputStream out) throws IOException {
    model.save(out);
  }

  public static void save(ExternalizableModel model, File outFile) throws IOException {
    FileOutputStream out = new FileOutputStream(outFile);
    save(model, out);
    out.close();
  }

  public static <T extends ExternalizableModel> T trySaveAndLoad(T model) throws IOException, LoadException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    save(model, out);
    out.close();
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    @SuppressWarnings("unchecked")
    T instance = (T) load(model.getClass(), in);
    return instance;
  }

  private ExtUtil() {
    throw new AssertionError();
  }

}
