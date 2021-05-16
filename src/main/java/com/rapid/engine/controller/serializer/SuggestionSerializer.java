package com.rapid.engine.controller.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.rapid.engine.entity.Suggestion;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;

public class SuggestionSerializer extends StdSerializer<Suggestion> {

  static final org.slf4j.Logger logger = LoggerFactory.getLogger(SuggestionSerializer.class);

  public SuggestionSerializer(Class<Suggestion> suggestionClass) {
    super(suggestionClass);
  }

  public static String serialize(Collection<Suggestion> suggestions, String query) {
    StringBuilder str = new StringBuilder();
    String serializedSuggestions = "";
    try {
      ObjectMapper mapper = new ObjectMapper();
      SimpleModule mod = new SimpleModule("SuggestionSerializer");
      mod.addSerializer(new SuggestionSerializer(Suggestion.class));
      mapper.registerModule(mod);
      serializedSuggestions = mapper.writeValueAsString(suggestions);
    } catch (JsonProcessingException ex) {
      logger.error("Error serializing suggestions {}", suggestions);
    }

    str.append("{")
        .append("\"terms\"").append(":").append("[\"").append(query).append("\"]")
        .append(",")
        .append("\"results\"").append(":").append(serializedSuggestions)
        .append("}");

    return str.toString();
  }


  @Override
  public void serialize(Suggestion suggestion, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    throws IOException {
    jsonGenerator.writeStartArray();
    jsonGenerator.writeString(suggestion.getTarget());
    jsonGenerator.writeString(suggestion.getSuggestion());
    jsonGenerator.writeEndArray();
  }
}
