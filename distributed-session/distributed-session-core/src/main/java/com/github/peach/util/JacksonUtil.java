package com.github.peach.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.JavaType;

/**
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-3-18$
 * 
 */
public abstract class JacksonUtil {
    // can reuse, share globally
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper(); 
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    public static final String obj2Str(Object o) throws JsonGenerationException, JsonMappingException, IOException {
        return mapper.writeValueAsString(o);
    }
    
    public static final String safeObj2Str(Object o) {
        String result = "";
        try{
            result = obj2Str(o);
        }catch(Throwable e) {
           //ignore
        }
        
        return result;
    }
    
    public static final void writeObj(OutputStream out, Object value) throws JsonGenerationException, JsonMappingException, IOException {
        mapper.writeValue(out, value);
    }
    
    public static final <T> T str2Obj(String s, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(s, valueType);
    }
    
    public static final <T> List<T> str2List(String s,Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, valueType);
        return mapper.readValue(s, javaType);
    }
    
    public static final <T> T readObj(InputStream in, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
        return mapper.readValue(in, valueType);
    }
    
    @SuppressWarnings("unchecked")
    public static final <T> T readObj(InputStream in, JavaType valueType) throws JsonParseException, JsonMappingException, IOException {
        return (T)mapper.readValue(in, valueType);
    }
    
}
