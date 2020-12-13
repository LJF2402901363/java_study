package com.moyisuiying.neo4j.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

import java.util.List;
import java.util.ArrayList;

/**
 * Classname:Role
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-30 08:28
 * @Version: 1.0
 **/
    @RelationshipEntity(type = "ACTED_IN")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Role {
        @Id
        @GeneratedValue
        private Long id;
        private List<String> roles = new ArrayList<>();
        @StartNode
        @JsonIgnoreProperties({"actedIn", "directed"})
        private Person person;
        @EndNode
        @JsonIgnoreProperties({"actors", "directors"})
        private Movie movie;
    }
