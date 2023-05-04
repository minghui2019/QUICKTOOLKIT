package com.aspose.words.headerFooter;

import java.text.MessageFormat;
import java.util.Calendar;

import com.aspose.words.ApiExampleBase;
import com.aspose.words.Document;
import com.aspose.words.FieldType;
import com.aspose.words.HeaderFooter;
import com.aspose.words.HeaderFooterType;
import com.aspose.words.ImportFormatMode;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.Run;
import org.junit.Assert;
import org.junit.Test;

public class HeaderFooterTest {

    @Test
    public void testHeaderFooterCreate() throws Exception {
        Document doc = new Document();

        HeaderFooter header = new HeaderFooter(doc, HeaderFooterType.HEADER_PRIMARY);
        doc.getFirstSection().getHeadersFooters().add(header);

        // Add a paragraph with text to the footer
        Paragraph para = header.appendParagraph("My header");

        Assert.assertTrue(header.isHeader());
        Assert.assertTrue(para.isEndOfHeaderFooter());

        HeaderFooter footer = new HeaderFooter(doc, HeaderFooterType.FOOTER_PRIMARY);
        doc.getFirstSection().getHeadersFooters().add(footer);

        // Add a paragraph with text to the footer
        para = footer.appendParagraph("My footer");

        Assert.assertFalse(footer.isHeader());
        Assert.assertTrue(para.isEndOfHeaderFooter());

        Assert.assertEquals(para.getParentStory(), footer);
        Assert.assertEquals(para.getParentSection(), footer.getParentSection());
        Assert.assertEquals(header.getParentSection(), footer.getParentSection());

        doc.save(ApiExampleBase.getArtifactsDir() + "HeaderFooter.HeaderFooterCreate.docx");
    }

    @Test
    public void testHeaderFooterCreate1() throws Exception {
        Document doc = new Document();

        HeaderFooter header = new HeaderFooter(doc, HeaderFooterType.HEADER_PRIMARY);
        doc.getFirstSection().getHeadersFooters().add(header);

        // Add a paragraph with text to the footer
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        Paragraph para = header.appendParagraph(MessageFormat.format("Copyright (C) {0} by Aspose Pty Ltd.", currentYear));

        Assert.assertTrue(header.isHeader());
        Assert.assertTrue(para.isEndOfHeaderFooter());

        HeaderFooter footer = new HeaderFooter(doc, HeaderFooterType.FOOTER_PRIMARY);
        doc.getFirstSection().getHeadersFooters().add(footer);

        // Add a paragraph with text to the footer
        Paragraph paragraph = new Paragraph(doc);
        paragraph.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);

        paragraph.appendChild(new Run(doc, "共"));
//        paragraph.appendField(FieldType.FIELD_NUM_PAGES, true);// 总页码
//        paragraph.appendChild(new Run(doc, "页，第"));
        paragraph.appendField(FieldType.FIELD_PAGE, true);// 当前页码
//        paragraph.appendChild(new Run(doc, "页"));
        footer.appendChild(paragraph);

        para = paragraph;

        Assert.assertFalse(footer.isHeader());
        Assert.assertTrue(para.isEndOfHeaderFooter());

        Assert.assertEquals(para.getParentStory(), footer);
        Assert.assertEquals(para.getParentSection(), footer.getParentSection());
        Assert.assertEquals(header.getParentSection(), footer.getParentSection());

        doc.save(ApiExampleBase.getArtifactsDir() + "HeaderFooter.HeaderFooterCreate1.docx");
        Document doc1 = new Document();
        doc1.cleanup();
        doc1.appendDocument(doc, ImportFormatMode.KEEP_SOURCE_FORMATTING);
        doc1.save(ApiExampleBase.getArtifactsDir() + "HeaderFooter.HeaderFooterCreate1.pdf");
    }
}
