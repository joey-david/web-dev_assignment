package fr.univlyon1.m1if.m1if03.resas_api.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * DTO de type UserResponse (voir doc OpenAPI).
 * @param users Liste de liens vers les utilisateurs
 */
@JacksonXmlRootElement(localName = "users")
//@JsonSerialize(using = ArrayList.class)
public record UsersResponseDto(
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "link")
        @JsonUnwrapped
        List<LinkDto> users) {
}
