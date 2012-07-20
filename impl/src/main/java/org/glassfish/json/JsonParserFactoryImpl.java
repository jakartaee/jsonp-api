package org.glassfish.json;

import javax.json.JsonArray;
import javax.json.JsonConfiguration;
import javax.json.JsonObject;
import javax.json.stream.JsonParserFactory;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.Reader;

/**
 * @author Jitendra Kotamraju
 */
public class JsonParserFactoryImpl implements JsonParserFactory {

    public JsonParserFactoryImpl() {
    }

    public JsonParserFactoryImpl(JsonConfiguration config) {
    }

    @Override
    public JsonParser createParser(Reader reader) {
        return new JsonParserImpl(reader);
    }

    @Override
    public JsonParser createParser(InputStream in) {
        return new JsonParserImpl(in);
    }

    @Override
    public JsonParser createParser(JsonArray array) {
        return new JsonParserImpl(array);
    }

    @Override
    public JsonParser createParser(JsonObject object) {
        return new JsonParserImpl(object);
    }
}
