package org.glassfish.json;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * @author Jitendra Kotamraju
 */
public class JsonGeneratorFactoryImpl implements JsonGeneratorFactory {

    public JsonGeneratorFactoryImpl() {

    }

    public JsonGeneratorFactoryImpl(JsonConfiguration config) {

    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new JsonGeneratorImpl(writer);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, String encoding) {
        return new JsonGeneratorImpl(out, encoding);
    }

}
