package simpleml.classify.ext;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author sitfoxfly
 */
public class ExtUtils {

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
    final FileInputStream in = new FileInputStream(inFile);
    final T instance = load(clazz, in);
    in.close();
    return instance;
  }

  public static void save(ExternalizableModel model, OutputStream out) throws IOException {
    model.save(out);
  }

  public static void save(ExternalizableModel model, File outFile) throws IOException {
    final FileOutputStream out = new FileOutputStream(outFile);
    save(model, out);
    out.close();
  }

  public static <T extends ExternalizableModel> T trySaveAndLoad(T model) throws IOException, LoadException {
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    save(model, out);
    out.close();
    final ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    @SuppressWarnings("unchecked")
    final T instance = (T) load(model.getClass(), in);
    return instance;
  }

  private ExtUtils() {
    throw new AssertionError();
  }

}
