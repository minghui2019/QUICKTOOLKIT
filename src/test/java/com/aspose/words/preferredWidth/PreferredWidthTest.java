package com.aspose.words.preferredWidth;

import java.awt.Color;
import java.text.MessageFormat;

import com.aspose.words.ApiExampleBase;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.PreferredWidth;
import com.aspose.words.Table;
import org.junit.Assert;
import org.junit.Test;

public class PreferredWidthTest {

    @Test
    public void testInsertCellsWithPreferredWidths() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // Insert a table row made up of three cells which have different preferred widths
        Table table = builder.startTable();

        // Insert an absolute sized cell
        builder.insertCell();
        builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPoints(40));
        builder.getCellFormat().getShading().setBackgroundPatternColor(Color.RED);
        builder.writeln("Cell at 40 points width");

        PreferredWidth width = builder.getCellFormat().getPreferredWidth();
        System.out.println(MessageFormat.format("Width \"{0}\": {1}", width.hashCode(), width.toString()));

        // Insert a relative (percent) sized cell
        builder.insertCell();
        builder.getCellFormat().setPreferredWidth(PreferredWidth.fromPercent(20));
        builder.getCellFormat().getShading().setBackgroundPatternColor(Color.BLUE);
        builder.writeln("Cell at 20% width");

        // Each cell had its own PreferredWidth
        Assert.assertFalse(builder.getCellFormat().getPreferredWidth().equals(width));

        width = builder.getCellFormat().getPreferredWidth();
        System.out.println(MessageFormat.format("Width \"{0}\": {1}", width.hashCode(), width.toString()));

        // Insert a auto sized cell
        builder.insertCell();
        builder.getCellFormat().setPreferredWidth(PreferredWidth.AUTO);
        builder.getCellFormat().getShading().setBackgroundPatternColor(Color.GREEN);
        builder.writeln("Cell automatically sized. The size of this cell is calculated from the table preferred width.");
        builder.writeln("In this case the cell will fill up the rest of the available space.");

        doc.save(ApiExampleBase.getArtifactsDir() + "DocumentBuilder.InsertCellsWithPreferredWidths.docx");
    }
}
