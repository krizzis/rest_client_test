import com.google.gson.*;

import java.lang.reflect.Type;

    public class ValueDeserializer implements JsonDeserializer<Value> {

        public Value deserialize(JsonElement jsonElement, Type type,
                                 JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException {

            try{
                return new Gson().fromJson(jsonElement,Value.class);
            }

            catch (Exception e){

            }
            return new Value(jsonElement.toString());
        }
    }

