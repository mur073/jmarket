package uz.uzumtech.common.error.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class IntegerToHttpStatusConverter extends JsonDeserializer<HttpStatus> {

    @Override
    public HttpStatus deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        return HttpStatus.valueOf(p.getValueAsInt());
    }
}
