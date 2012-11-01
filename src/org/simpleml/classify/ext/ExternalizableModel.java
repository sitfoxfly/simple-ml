package org.simpleml.classify.ext;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author sitfoxfly
 */
public interface ExternalizableModel {

    public void save(OutputStream out) throws IOException;

}
