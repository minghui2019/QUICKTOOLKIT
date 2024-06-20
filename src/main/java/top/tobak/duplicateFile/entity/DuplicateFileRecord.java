package top.tobak.duplicateFile.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.tobak.poi.excel.ExcelProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateFileRecord {
    @ExcelProperty(value = "group", colIndex = 1, clz = Integer.class)
    private int group;
    @ExcelProperty(value = "sharedFolder", colIndex = 2)
    private String sharedFolder;
    @ExcelProperty(value = "file", colIndex = 3)
    private String file;
    @ExcelProperty(value = "size", colIndex = 4, clz = Long.class)
    private long size;
    @ExcelProperty(value = "modifiedTime", colIndex = 5, clz = Date.class)
    private Date modifiedTime;
}
