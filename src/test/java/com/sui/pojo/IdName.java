package com.sui.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Structure containing ID and Name properties
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdName {
    public int id;
    public String name;
}