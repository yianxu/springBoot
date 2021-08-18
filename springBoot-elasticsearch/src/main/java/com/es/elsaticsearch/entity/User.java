package com.es.elsaticsearch.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author : Latent
 * @createdDate : 2020/5/31
 * @updatedDate
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class User {
    private Integer id;

    private String name;

    private Integer age;

    public User(String name,Integer age){
    }

}
