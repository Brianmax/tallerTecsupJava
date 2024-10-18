package org.ravn.moviescatalogchallenge.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ravn.moviescatalogchallenge.aggregate.response.MovieResponse;
import org.ravn.moviescatalogchallenge.exceptions.JsonConversionException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    public static String getLoggedUser(HttpServletRequest request) {
        return request.getUserPrincipal().getName();
    }
    public static String convertToJson(List<MovieResponse> movieResponses) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(movieResponses);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error " + e.getMessage();
            logger.error(errorMessage, e);
            throw new JsonConversionException(errorMessage, e);
        }
    }
    public static <T> T convertFromJson(String json, Class<T> valueType) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            String errorMessage = String.format("Convert JSON error %s: %s", valueType.getSimpleName(), e.getMessage());
            logger.error(errorMessage, e);
            throw new JsonConversionException(errorMessage, e);
        }
    }

    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && email.matches("^(.+)@(.+)$");
    }
}
