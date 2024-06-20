package com.aspose.words.groupShape;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.DocumentVisitor;
import com.aspose.words.GroupShape;
import com.aspose.words.HorizontalAlignment;
import com.aspose.words.PaperSize;
import com.aspose.words.RelativeHorizontalPosition;
import com.aspose.words.RelativeVerticalPosition;
import com.aspose.words.Shape;
import com.aspose.words.ShapeType;
import com.aspose.words.VerticalAlignment;
import com.aspose.words.VisitorAction;
import com.aspose.words.WrapType;
import com.aspose.words.utils.ConvertUtil;
import org.junit.Assert;
import org.junit.Test;

import static com.aspose.words.ApiExampleBase.getArtifactsDir;

public class GroupShapeTest {
    @Test
    public void groupOfShapes() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // If you need to create "NonPrimitive" shapes, like SingleCornerSnipped, TopCornersSnipped, DiagonalCornersSnipped,
        // TopCornersOneRoundedOneSnipped, SingleCornerRounded, TopCornersRounded, DiagonalCornersRounded
        // please use DocumentBuilder.InsertShape methods
        Shape balloon = new Shape(doc, ShapeType.BALLOON);
        balloon.setWidth(200.0);
        balloon.setHeight(200.0);
        balloon.setStrokeColor(Color.RED);

        Shape cube = new Shape(doc, ShapeType.CUBE);
        cube.setWidth(100.0);
        cube.setHeight(100.0);
        cube.setStrokeColor(Color.BLUE);

        GroupShape group = new GroupShape(doc);
        group.appendChild(balloon);
        group.appendChild(cube);

        Assert.assertTrue(group.isGroup());
        builder.insertNode(group);

        ShapeInfoPrinter printer = new ShapeInfoPrinter();
        group.accept(printer);

        System.out.println(printer.getText());
        doc.save(getArtifactsDir() + "GroupShape.GroupOfShapes.docx");
    }

    /// <summary>
    /// Visitor that prints shape group contents information to the console.
    /// </summary>
    public static class ShapeInfoPrinter extends DocumentVisitor {
        public ShapeInfoPrinter() {
            mBuilder = new StringBuilder();
        }

        public String getText() {
            return mBuilder.toString();
        }

        public int visitGroupShapeStart(final GroupShape groupShape) {
            mBuilder.append("Shape group started:\r\n");
            return VisitorAction.CONTINUE;
        }

        public int visitGroupShapeEnd(final GroupShape groupShape) {
            mBuilder.append("End of shape group\r\n");
            return VisitorAction.CONTINUE;
        }

        public int visitShapeStart(final Shape shape) {
            mBuilder.append("\tShape - " + shape.getShapeType() + ":\r\n");
            mBuilder.append("\t\tWidth: " + shape.getWidth() + "\r\n");
            mBuilder.append("\t\tHeight: " + shape.getHeight() + "\r\n");
            mBuilder.append("\t\tStroke color: " + shape.getStroke().getColor() + "\r\n");
            mBuilder.append("\t\tFill color: " + shape.getFill().getColor() + "\r\n");
            return VisitorAction.CONTINUE;
        }

        public int visitShapeEnd(final Shape shape) {
            mBuilder.append("\tEnd of shape\r\n");
            return VisitorAction.CONTINUE;
        }

        private StringBuilder mBuilder;
    }

    /**
     * 显示如何创建和使用一组形状（水印）。
     */
    @Test
    public void testInsertGroupShape1() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);
        builder.getPageSetup().setPaperSize(PaperSize.A4);

        double pageWidth = builder.getPageSetup().getPageWidth();
        double pageHeight = builder.getPageSetup().getPageHeight();
        double leftMargin = builder.getPageSetup().getLeftMargin();
        double rightMargin = builder.getPageSetup().getRightMargin();
        double topMargin = builder.getPageSetup().getTopMargin();
        double bottomMargin = builder.getPageSetup().getBottomMargin();
        double editPageWidth = pageWidth - leftMargin - rightMargin;
        double editPageHeight = pageHeight - topMargin - bottomMargin;

        // 每个GroupShape都是顶级的
        GroupShape group = new GroupShape(doc);
        Assert.assertTrue(group.isGroup());
        Assert.assertTrue(group.isTopLevel());

        // 它也是一个浮动形状，所以我们可以独立于文本设置它的坐标
        Assert.assertEquals(group.getWrapType(), WrapType.NONE);

        // 使其成为浮动形状
        group.setWrapType(WrapType.NONE);

        // 顶级形状可以更改此属性
        group.setAnchorLocked(true);

        // 设置形状组的XY坐标及其包含块的大小，如它在页面上显示的那样
        double width = editPageWidth;
        double height = editPageHeight;
        group.setBounds(new Rectangle2D.Float(0, 0, (float) width, (float) height));

        //设置形状组的内部坐标的比例
        //这些值意味着我们之前设置的200x100外部块的右下角
        //将为x=2000和y=1000，或者从左起2000个单位，从上起1000个单位
        group.setCoordSize(new Dimension((int) (width*10), (int) (height*10)));

        //形状组的坐标原点默认为x=0，y=0，即左上角
        //如果我们插入一个子形状，并将其从左侧的距离设置为2000，
        //其原点将位于形状组的右下角
        //我们可以通过设置CoordOrigin属性来偏移坐标原点
        //在这种情况下，我们将原点移动到形状组的中心
        group.setCoordOrigin(new Point(0, 0));

        //用子形状填充形状组
        //首先，插入一个矩形
        double subwidth = (width - 90) * 10;
        double subheight = ConvertUtil.millimeterToPoint(40) * 10;
        double rotation = 360 - 35;
        Shape subShape = createTextShape(doc, "纵20240001", rotation);
        subShape.setWidth(subwidth);
        subShape.setHeight(subheight);

        double aa = (width*10-subwidth)/2;
        double left = aa;
        double top = 0;
        double right = left + subwidth;
        double bottom = top + subheight;
        double xcenter = (right - left + 1) /2 + left;
        double ycenter = (bottom - top + 1) / 2 + top;

        double x1 = aa + subwidth;
        double y1 = 0;

        double x2 = (x1 - xcenter) * Math.cos(rotation) - (y1 - ycenter) * Math.sin(rotation) + xcenter;
        double y2 = (x1 - xcenter) * Math.sin(rotation) + (y1 - ycenter) * Math.cos(rotation) + ycenter;

        System.out.println(x2);
        System.out.println(y2);

        // 将其左上角放置在当前位于其中心的父编组的坐标原点处
        subShape.setLeft(aa);
        subShape.setTop(0-y2);

        // 将矩形添加到组中
        group.appendChild(subShape);

        // 插入三角形
        subShape = createTextShape(doc, "纵20240001", rotation);
        subShape.setWidth(subwidth);
        subShape.setHeight(subheight);

        // 将其原点放置在组的右下角
        subShape.setLeft(aa);
        subShape.setTop(height*10-subheight+y2);

        // 此子形状和父组之间的偏移可以在此处看到
//        Assert.assertEquals(subShape.localToParent(new Point2D.Float(0f, 0f)), new Point2D.Float(1000f, 500f));

        // 将三角形添加到组中
        group.appendChild(subShape);

        // 组形状的子形状不是顶级形状
        Assert.assertFalse(subShape.isTopLevel());

        // 最后，将组插入文档并保存
        builder.insertNode(group);
        doc.save(getArtifactsDir() + "Shape.InsertGroupShape1.docx");
    }

    public static Shape createTextShape(Document document, String watermarkText, double rotation) {
        // Create a watermark shape. This will be a WordArt shape.
        // You are free to try other shape types as watermarks.
        Shape watermark = new Shape(document, ShapeType.TEXT_PLAIN_TEXT);
        // Set up the text of the watermark.
        watermark.getTextPath().setText(watermarkText);
        watermark.getTextPath().setFontFamily("黑体");
        try {
//            watermark.setWidth(500);
//            watermark.setHeight(100);
        } catch (Exception e) {
        }
        // Text will be directed from the bottom-left to the top-right corner.
        watermark.setRotation(rotation);
        // Remove the following two lines if you need a solid black text.
        watermark.getFill().setColor(Color.lightGray); // Try LightGray to get more Word-style watermark
        watermark.setStrokeColor(Color.lightGray); // Try LightGray to get more Word-style watermark
        // Place the watermark in the page center.
        watermark.setRelativeHorizontalPosition(RelativeHorizontalPosition.PAGE);
        watermark.setRelativeVerticalPosition(RelativeVerticalPosition.PAGE);
        watermark.setWrapType(WrapType.NONE);
        watermark.setVerticalAlignment(VerticalAlignment.CENTER);
        watermark.setHorizontalAlignment(HorizontalAlignment.CENTER);
        return watermark;
    }

    @Test
    public void testInsertGroupShape() throws Exception {
        Document doc = new Document();
        DocumentBuilder builder = new DocumentBuilder(doc);

        // 每个GroupShape都是顶级的
        GroupShape group = new GroupShape(doc);
        Assert.assertTrue(group.isGroup());
        Assert.assertTrue(group.isTopLevel());

        // 它也是一个浮动形状，所以我们可以独立于文本设置它的坐标
        Assert.assertEquals(group.getWrapType(), WrapType.NONE);

        // 使其成为浮动形状
        group.setWrapType(WrapType.NONE);

        // 顶级形状可以更改此属性
        group.setAnchorLocked(true);

        // 设置形状组的XY坐标及其包含块的大小，如它在页面上显示的那样
        group.setBounds(new Rectangle2D.Float(100f, 50f, 200f, 100f));

        //设置形状组的内部坐标的比例
        //这些值意味着我们之前设置的200x100外部块的右下角
        //将为x=2000和y=1000，或者从左起2000个单位，从上起1000个单位
        group.setCoordSize(new Dimension(2000, 1000));

        //形状组的坐标原点默认为x=0，y=0，即左上角
        //如果我们插入一个子形状，并将其从左侧的距离设置为2000，
        //其原点将位于形状组的右下角
        //我们可以通过设置CoordOrigin属性来偏移坐标原点
        //在这种情况下，我们将原点移动到形状组的中心
        group.setCoordOrigin(new Point(-1000, -500));

        //用子形状填充形状组
        //首先，插入一个矩形
        Shape subShape = new Shape(doc, ShapeType.RECTANGLE);
        subShape.setWidth(500.0);
        subShape.setHeight(700.0);

        // 将其左上角放置在当前位于其中心的父编组的坐标原点处
        subShape.setLeft(0.0);
        subShape.setTop(0.0);

        // 将矩形添加到组中
        group.appendChild(subShape);

        // 插入三角形
        subShape = new Shape(doc, ShapeType.TRIANGLE);
        subShape.setWidth(400.0);
        subShape.setHeight(400.0);

        // 将其原点放置在组的右下角
        subShape.setLeft(1000.0);
        subShape.setTop(500.0);

        // 此子形状和父组之间的偏移可以在此处看到
        Assert.assertEquals(subShape.localToParent(new Point2D.Float(0f, 0f)), new Point2D.Float(1000f, 500f));

        // 将三角形添加到组中
        group.appendChild(subShape);

        // 组形状的子形状不是顶级形状
        Assert.assertFalse(subShape.isTopLevel());

        // 最后，将组插入文档并保存
        builder.insertNode(group);
        doc.save(getArtifactsDir() + "Shape.InsertGroupShape.docx");
    }
}
