package org.glassfish.json;

import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jitendra Kotamraju
 */
class JsonWriterFactoryImpl implements JsonWriterFactory {
    private final Map<String, ?> config;

    JsonWriterFactoryImpl(Map<String, ?> config) {
        boolean prettyPrinting = config != null
                && JsonProviderImpl.isPrettyPrintingEnabled(config);
        Map<String, Object> providerConfig = new HashMap<String, Object>();
        if (prettyPrinting) {
            providerConfig.put(JsonGenerator.PRETTY_PRINTING, true);
        }
        this.config = Collections.unmodifiableMap(providerConfig);
    }

    @Override
    public JsonWriter createWriter(Writer writer) {
        return new JsonWriterImpl(writer, config);
    }

    @Override
    public JsonWriter createWriter(OutputStream out) {
        return new JsonWriterImpl(out, config);
    }

    @Override
    public JsonWriter createWriter(OutputStream out, Charset charset) {
        return new JsonWriterImpl(out, charset, config);
    }

    @Override
    public Map<String, ?> getConfigInUse() {
        return config;
    }
}
