package com.aspose.words.shape;

import java.awt.Color;
import java.io.File;

import com.aspose.words.ApiExampleBase;
import com.aspose.words.DashStyle;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.EndCap;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.JoinStyle;
import com.aspose.words.OoxmlCompliance;
import com.aspose.words.OoxmlSaveOptions;
import com.aspose.words.Paragraph;
import com.aspose.words.ParagraphAlignment;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.SaveFormat;
import com.aspose.words.Shape;
import com.aspose.words.ShapeLineStyle;
import com.aspose.words.ShapeType;
import com.aspose.words.Stroke;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.WrapType;
import top.tobak.common.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class ShapeTest {

    @Test
    public void testName() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.write("Content in textbox");
        Shape textBox = builder.insertShape(ShapeType.TEXT_BOX, 120, 70);
        double size = builder.getFont().getSize();
        builder.moveTo(textBox.getLastParagraph());
        builder.getFont().setSize(8);
        builder.write("Content in textbox");
        textBox.setWrapType(WrapType.INLINE);
        textBox.setHorizontalAlignment(HorizontalAlignment.DEFAULT);
        textBox.setVerticalAlignment(VerticalAlignment.DEFAULT);
        textBox.getStroke().setOn(false);
        textBox.setZOrder(2);
        textBox.getLastParagraph().getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
        builder.moveTo(textBox.getParentNode());
        builder.getFont().setSize(size);
        builder.writeln();
        builder.writeln("ending");
        builder.writeln("11111");
        builder.writeln("11111");
        OoxmlSaveOptions saveOptions = new OoxmlSaveOptions(SaveFormat.DOCX);
        saveOptions.setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
        doc.save(ApiExampleBase.getArtifactsDir() + "Shape.CreateTextBox.docx", saveOptions);
        FileUtils.openTaskBar(new File(ApiExampleBase.getArtifactsDir()));
    }

    @Test
    public void testCreateTextBox() throws Exception {
        Document doc = new Document();

        DocumentBuilder builder = new DocumentBuilder(doc);


        builder.write("Content in textbox");

        // Append the textbox to the first paragraph in the body
        // Create a new shape of type TextBox
        Shape textBox = builder.insertShape(ShapeType.TEXT_BOX, 120, 70);
        builder.moveTo(textBox.getLastParagraph());
        builder.write("Content in textbox");
        // Set some settings of the textbox itself
        // Set the wrap of the textbox to inline
        textBox.setWrapType(WrapType.INLINE);
        // Set the horizontal and vertical alignment of the text inside the shape
        textBox.setHorizontalAlignment(HorizontalAlignment.DEFAULT);
        textBox.setVerticalAlignment(VerticalAlignment.DEFAULT);
//        textBox.getFill().setColor(Color.GREEN);
//        textBox.getFill().setOn(true);
//        textBox.getFill().setOpacity(1);
        Stroke stroke = textBox.getStroke();
        stroke.setOn(false);
        stroke.setWeight(5.0);
        stroke.setColor(Color.RED);
        stroke.setDashStyle(DashStyle.SHORT_DASH_DOT_DOT);
        stroke.setJoinStyle(JoinStyle.MITER);
        stroke.setEndCap(EndCap.SQUARE);
        stroke.setLineStyle(ShapeLineStyle.TRIPLE);

        // Set the textbox height and width
//        textBox.setHeight(50.0);
//        textBox.setWidth(60.0);

        // Set the textbox in front of other shapes with a lower ZOrder
        textBox.setZOrder(2);

        // Let's create a new paragraph for the textbox manually and align it in the center
        // Make sure we add the new nodes to the textbox as well
        Paragraph para = new Paragraph(doc);
        textBox.appendChild(para);
        para.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);

        // Add some text to the paragraph
//        para.appendChild(new Run(doc, "Content in textbox"));
        builder.moveToDocumentEnd();
        builder.writeln("11111");
        builder.writeln("11111");
        builder.writeln("11111");

//        doc.getFirstSection().getBody().getFirstParagraph().appendChild(textBox);
        OoxmlSaveOptions saveOptions = new OoxmlSaveOptions(SaveFormat.DOCX);
        saveOptions.setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);
        // Save the output
        doc.save(ApiExampleBase.getArtifactsDir() + "Shape.CreateTextBox.docx", saveOptions);
        FileUtils.openTaskBar(new File(ApiExampleBase.getArtifactsDir()));
    }

    @Test
    public void testShapeInsertion() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // There are two ways of shape insertion
        // These methods allow inserting DML shape into the document model
        // Document must be saved in the format, which supports DML shapes, otherwise, such nodes will be converted
        // to VML shape, while document saving

        // 1. Free-floating shape insertion
        Shape freeFloatingShape = builder.insertShape(ShapeType.TEXT_BOX, RelativeHorizontalPosition.PAGE, 100.0, RelativeVerticalPosition.PAGE, 100.0, 50.0, 50.0, WrapType.NONE);
        freeFloatingShape.setRotation(30.0);
        freeFloatingShape.getTextPath().setText("freeFloatingShape");
        // 2. Inline shape insertion
        Shape inlineShape = builder.insertShape(ShapeType.TEXT_BOX, 50.0, 50.0);
        inlineShape.setRotation(30.0);
        inlineShape.getTextPath().setText("inlineShape");

        // If you need to create "NonPrimitive" shapes, like SingleCornerSnipped, TopCornersSnipped, DiagonalCornersSnipped,
        // TopCornersOneRoundedOneSnipped, SingleCornerRounded, TopCornersRounded, DiagonalCornersRounded
        // please save the document with "Strict" or "Transitional" compliance which allows saving shape as DML
        OoxmlSaveOptions saveOptions = new OoxmlSaveOptions(SaveFormat.DOCX);
        saveOptions.setCompliance(OoxmlCompliance.ISO_29500_2008_TRANSITIONAL);

        doc.save(ApiExampleBase.getArtifactsDir() + "Shape.ShapeInsertion.docx", saveOptions);
    }

    @Test
    public void testInsert() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // Insert a cube and set its name
        Shape shape = builder.insertShape(ShapeType.CUBE, 150.0, 150.0);
        shape.setName("MyCube");

        // We can also set the alt text like this
        // This text will be found in Format AutoShape > Alt Text
        shape.setAlternativeText("Alt text for MyCube.");

        // Insert a text box
        shape = builder.insertShape(ShapeType.TEXT_BOX, 300.0, 50.0);
        shape.getFont().setName("Times New Roman");

        // Move the builder into the text box and write text
        builder.moveTo(shape.getLastParagraph());
        builder.write("Hello world!");

        // Move the builder out of the text box back into the main document
        builder.moveTo(shape.getParentParagraph());

        // Insert a shape with an image
        shape = builder.insertImage(ApiExampleBase.getImageDir() + "Logo.jpg");
        Assert.assertTrue(shape.canHaveImage());
        Assert.assertTrue(shape.hasImage());

        // Rotate the image
        shape.setRotation(45.0);

        doc.save(ApiExampleBase.getArtifactsDir() + "Shape.Insert.docx");
    }

    @Test
    public void testName1() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // Create a new shape of type Rectangle
        Shape rectangle = new Shape(doc, ShapeType.RECTANGLE);

        // Change stroke properties
        Stroke stroke = rectangle.getStroke();
        stroke.setOn(true);
        stroke.setWeight(5.0);
        stroke.setColor(Color.RED);
        stroke.setDashStyle(DashStyle.SHORT_DASH_DOT_DOT);
        stroke.setJoinStyle(JoinStyle.MITER);
        stroke.setEndCap(EndCap.SQUARE);
        stroke.setLineStyle(ShapeLineStyle.TRIPLE);

        // Insert shape object
        builder.insertNode(rectangle);
    }
}
