package com.aspose.words.cellFormat;


import java.awt.Color;
import java.text.MessageFormat;

import com.aspose.words.ApiExampleBase;
import com.aspose.words.CellVerticalAlignment;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.HeightRule;
import com.aspose.words.LineStyle;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.PreferredWidth;
import com.aspose.words.Table;
import com.aspose.words.TextOrientation;
import com.aspose.words.utils.ConvertUtil;
import org.junit.Assert;
import org.junit.Test;

public class CellFormatTest {
    @Test
    public void testBorderTable() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        double pageWidth = builder.getPageSetup().getPageWidth();
        double leftMargin = builder.getPageSetup().getLeftMargin();
        double rightMargin = builder.getPageSetup().getRightMargin();
        double editableWidth = pageWidth - leftMargin - rightMargin;
        System.out.println(editableWidth);
        System.out.println(ConvertUtil.pointToMillimeter(editableWidth));
        System.out.println(ConvertUtil.pointToMillimeter(editableWidth) / 2);



        // Start building a table
        builder.startTable();

        // Set the appropriate paragraph, cell, and row formatting. The formatting properties are preserved
        // until they are explicitly modified so there's no need to set them for each row or cell
        builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);

        builder.getCellFormat().clearFormatting();
        builder.getCellFormat().setWidth(150.0);
        builder.getCellFormat().setVerticalAlignment(CellVerticalAlignment.CENTER);
        builder.getCellFormat().getShading().setBackgroundPatternColor(Color.GREEN);
        builder.getCellFormat().setWrapText(false);
        builder.getCellFormat().setFitText(true);

        builder.getRowFormat().clearFormatting();
        builder.getRowFormat().setHeightRule(HeightRule.EXACTLY);
        builder.getRowFormat().setHeight(50.0);
        builder.getRowFormat().getBorders().setLineStyle(LineStyle.ENGRAVE_3_D);
        builder.getRowFormat().getBorders().setColor(Color.ORANGE);

        builder.insertCell();
        builder.write("Row 1, Col 1");

        builder.insertCell();
        builder.write("Row 1, Col 2");

        builder.endRow();

        // Remove the shading (clear background)
        builder.getCellFormat().getShading().clearFormatting();

        builder.insertCell();
        builder.write("Row 2, Col 1");

        builder.insertCell();
        builder.write("Row 2, Col 2");

        builder.endRow();

        builder.insertCell();

        // Make the row height bigger so that a vertically oriented text could fit into cells
        builder.getRowFormat().setHeight(150.0);
        builder.getCellFormat().setOrientation(TextOrientation.UPWARD);
        builder.write("Row 3, Col 1");

        builder.insertCell();
        builder.getCellFormat().setOrientation(TextOrientation.DOWNWARD);
        builder.write("Row 3, Col 2");

        builder.endRow();

        builder.endTable();

        doc.save(ApiExampleBase.getArtifactsDir() + "DocumentBuilder.InsertTable.docx");
    }

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
        builder.getCellFormat().setPreferredWidth(PreferredWidth.AUTO);//Width
        builder.getCellFormat().getShading().setBackgroundPatternColor(Color.GREEN);
        builder.writeln("Cell automatically sized. The size of this cell is calculated from the table preferred width.");
        builder.writeln("In this case the cell will fill up the rest of the available space.");

        doc.save(ApiExampleBase.getArtifactsDir() + "DocumentBuilder.InsertCellsWithPreferredWidths.docx");
    }
}
