package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public interface API {

    public static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    public static final String URL = "http://129.148.25.183:3939";
}
