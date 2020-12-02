package com.moyisuiying.neo4j;

import org.neo4j.ogm.annotation.NodeEntity;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Property;

/**
 * Classname:Person
 *
 * @description:
 * @author: 陌意随影
 * @Date: 2020-11-29 23:26
 * @Version: 1.0
 **/
@NodeEntity
public class Person {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Property("born")
    private int birthyear;
}