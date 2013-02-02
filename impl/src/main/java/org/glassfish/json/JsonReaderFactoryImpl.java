package org.glassfish.json;

import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonReaderFactoryImpl implements JsonReaderFactory {
    private final Map<String, ?> config = Collections.emptyMap();

    @Override
    public JsonReader createReader(Reader reader) {
        return new JsonReaderImpl(reader);
    }

    @Override
    public JsonReader createReader(InputStream in) {
        return new JsonReaderImpl(in);
    }

    @Override
    public JsonReader createReader(InputStream in, Charset charset) {
        return new JsonReaderImpl(in, charset);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }
}
