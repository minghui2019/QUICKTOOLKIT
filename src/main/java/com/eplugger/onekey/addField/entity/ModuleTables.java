package com.eplugger.onekey.addField.entity;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAliasType;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@XStreamAliasType("ModuleTables")
public class ModuleTables {
	@XStreamImplicit
	List<ModuleTable> moduleTables = new ArrayList<ModuleTable>();
}
