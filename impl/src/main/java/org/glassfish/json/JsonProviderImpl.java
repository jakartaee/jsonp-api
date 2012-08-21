package org.glassfish.json;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonFeature;
import javax.json.JsonObject;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import javax.json.spi.JsonProvider;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jitendra Kotamraju
 */
public class JsonProviderImpl extends JsonProvider {
    private static final Set<JsonFeature> KNOWN_FEATURES = new HashSet<JsonFeature>();
    static {
        KNOWN_FEATURES.add(JsonFeature.PRETTY_PRINTING);
    }

    @Override
    public JsonGenerator createGenerator(Writer writer) {
        return new JsonGeneratorImpl(writer);
    }

    @Override
    public JsonGenerator createGenerator(Writer writer, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonGeneratorImpl(writer, config);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out) {
        return new JsonGeneratorImpl(out);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonGeneratorImpl(out, config);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, String encoding) {
        return new JsonGeneratorImpl(out, encoding);
    }

    @Override
    public JsonGenerator createGenerator(OutputStream out, String encoding, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonGeneratorImpl(out, encoding, config);
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return new JsonParserImpl(reader);
    }

    @Override
    public JsonParser createParser(Reader reader, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonParserImpl(reader, config);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return new JsonParserImpl(in);
    }

    @Override
    public JsonParser createParser(InputStream in, String encoding) {
        return new JsonParserImpl(in, encoding);
    }

    @Override
    public JsonParser createParser(InputStream in, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonParserImpl(in, config);
    }

    @Override
    public JsonParser createParser(InputStream in, String encoding, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonParserImpl(in, encoding, config);
    }

    @Override
    public JsonParser createParser(JsonArray array) {
        return new JsonStructureParser(array);
    }

    @Override
    public JsonParser createParser(JsonArray array, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonStructureParser(array);
    }

    @Override
    public JsonParser createParser(JsonObject object) {
        return new JsonStructureParser(object);
    }

    @Override
    public JsonParser createParser(JsonObject object, JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonStructureParser(object);
    }

    @Override
    public JsonParserFactory createParserFactory() {
        return new JsonParserFactoryImpl();
    }

    @Override
    public JsonParserFactory createParserFactory(JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonParserFactoryImpl(config);
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory() {
        return new JsonGeneratorFactoryImpl();
    }

    @Override
    public JsonGeneratorFactory createGeneratorFactory(JsonConfiguration config) {
        validateConfiguration(config);
        return new JsonGeneratorFactoryImpl(config);
    }

    private static void validateConfiguration(JsonConfiguration config) {
        Set<JsonFeature> unknown = null;
        Iterable<JsonFeature> features = config.getFeatures();
        for(JsonFeature feature : features) {
            if (!KNOWN_FEATURES.contains(feature)) {
                if (unknown == null) {
                    unknown = new HashSet<JsonFeature>();
                }
                unknown.add(feature);
            }
        }
        if (unknown != null && !unknown.isEmpty()) {
            throw new IllegalArgumentException("Specified config contains unknown features :"+unknown);
        }
    }

}
