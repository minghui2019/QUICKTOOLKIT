package com.aspose.words.border;

import java.awt.Color;

import com.aspose.words.Border;
import com.aspose.words.BorderType;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.LineStyle;
import com.aspose.words.PageBorderAppliesTo;
import com.aspose.words.PageBorderDistanceFrom;
import com.aspose.words.PageSetup;
import org.junit.Test;

import static com.aspose.words.ApiExampleBase.getArtifactsDir;

public class BorderTest {

    @Test
    public void testLineStyle() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        builder.getFont().getBorder().setColor(Color.GREEN);
        builder.getFont().getBorder().setLineWidth(2.5);
        builder.getFont().getBorder().setLineStyle(LineStyle.DASH_DOT_STROKER);

        builder.write("Text surrounded by green border.");

        doc.save(getArtifactsDir() + "Border.FontBorder.docx");
    }

    @Test
    public void testDistanceFromText() throws Exception {
        Document doc = new Document();

        PageSetup pageSetup = doc.getSections().get(0).getPageSetup();
        pageSetup.setBorderAlwaysInFront(false);
        pageSetup.setBorderDistanceFrom(PageBorderDistanceFrom.PAGE_EDGE);
        pageSetup.setBorderAppliesTo(PageBorderAppliesTo.FIRST_PAGE);

        Border border = pageSetup.getBorders().getByBorderType(BorderType.TOP);
        border.setLineStyle(LineStyle.SINGLE);
        border.setLineWidth(30.0);
        border.setColor(Color.BLUE);
        border.setDistanceFromText(0.0);

        doc.save(getArtifactsDir() + "PageSetup.PageBorderProperties.docx");
    }

    /**
     * 边框线
     * @throws Exception
     */
    @Test
    public void testParagraphTopBorder() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        Border topBorder = builder.getParagraphFormat().getBorders().getByBorderType(BorderType.TOP);
        topBorder.setColor(Color.RED);
        topBorder.setLineWidth(4.0d);
        topBorder.setLineStyle(LineStyle.DASH_SMALL_GAP);

        builder.writeln("Text with a red top border.");

        doc.save(getArtifactsDir() + "Border.ParagraphTopBorder.docx");
    }
}
