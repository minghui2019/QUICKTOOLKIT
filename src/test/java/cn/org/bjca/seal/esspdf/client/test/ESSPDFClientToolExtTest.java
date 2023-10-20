package cn.org.bjca.seal.esspdf.client.test;

import java.io.File;
import java.net.URLDecoder;

import cn.org.bjca.seal.esspdf.client.message.ChannelMessage;
import cn.org.bjca.seal.esspdf.client.tools.AnySignClientTool;
import cn.org.bjca.seal.esspdf.client.utils.ClientUtil;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.misc.BASE64Decoder;

/***************************************************************************
 * <pre>统一密码支持平台客户端工具测试类</pre>
 * @文件名称: ESSPDFClientToolExtTest.java
 * @包 路   径：  cn.org.bjca.seal.esspdf.client.test
 * @版权所有：北京数字认证股份有限公司 (C) 2013
 *
 * @类描述:
 * @版本: V2.0
 * @创建人： wangzhijun
 * @创建时间：2019-8-15 下午1:34:10
 ***************************************************************************/
public class ESSPDFClientToolExtTest {

    AnySignClientTool anySignClientTool = null;

    private String resourcesPath;
    String testPdfFilePath;
    String testXMLFilePath;
    String testSignedPdfFilePath;


    @Before
    public void init() throws Exception {
//        String ip = "192.168.126.37";
        String ip = "127.0.0.1";
        int port = 8888;
        anySignClientTool = new AnySignClientTool(ip, port);
        //客户端软负载
//      anySignClientTool = new AnySignClientTool("127.0.0.1:8888","192.168.126.148:8888");
        //连接服务器超时时间，单位是毫秒，默认为5秒，当前为50秒
        anySignClientTool.setTimeout(50000);//连接超时时间设置
        anySignClientTool.setRespTimeout(50000);//响应超时时间

        resourcesPath = URLDecoder.decode(this.getClass().getClassLoader().getResource("").getPath(), "UTF-8");
        testPdfFilePath = resourcesPath + "esspdf-demo/pdf" + File.separator + "test.pdf";
        testXMLFilePath = resourcesPath + "esspdf-demo/xml" + File.separator + "test.xml";
        testSignedPdfFilePath = resourcesPath + "esspdf-demo/out" + File.separator + "signedPDF.pdf";
    }
    
    @Test
	public void testPdfSign() throws Exception {//PDF签章测试
		System.out.println("*******************");
		byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
		String ruleNum = "5E9217F50C3E491D";//签章规则编号
		long begin = System.currentTimeMillis();
		ChannelMessage message = anySignClientTool.pdfSign(ruleNum, pdfBty);
		long end = System.currentTimeMillis();
		System.out.println("********************运行时间:"+(end - begin)/1000f+"s");
		System.out.println("状态码："+message.getStatusCode());
		System.out.println("状态信息："+message.getStatusInfo());
		if("200".equals(message.getStatusCode())){//成功
			ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
		}
	}
    
    @Test
    public void testPdfVerify() throws Exception {//PDF验签测试
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testSignedPdfFilePath));//pdf字节数组
        String orgCode = "123456";//机构代码
        ChannelMessage message = anySignClientTool.pdfVerify(orgCode, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
    }
    
    @Test
    public void testPdfVerifyForNoOrgCode() throws Exception {//PDF验签测试
        System.out.println("******************123");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        ChannelMessage message = anySignClientTool.pdfVerify(pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        } else {
            System.out.println("验证结果：" + new String(message.getBody(), "UTF-8"));
        }
    }
    
    @Test
	public void testGenPDFDocumentAndSign() throws Exception {//根据模版生成PDF文档且签章
		byte[] dataBty = ClientUtil.readFileToByteArray(new File("C:/Users/admin/Desktop/template-test/testData.xml"));//xml模版数据
		String templateNum = "2DA902A856F02819";//模版编号
		String ruleNum = "123456";//签章规则编号
		ChannelMessage message = anySignClientTool.genPDFDocumentAndSign(templateNum, ruleNum, dataBty);
		System.out.println("状态码：" + message.getStatusCode());
		if("200".equals(message.getStatusCode())){//成功
			ClientUtil.writeByteArrayToFile(new File("C:/Users/admin/Desktop/template-test/templat-field-out.pdf"), message.getBody());
		}
	}
    
    @Test
    public void testPdfSignByWord() throws Exception {//word转PDF进行签章测试
        byte[] wordBty = ClientUtil.readFileToByteArray(new File("C:/Users/DSVS接口测试程序使用指南.doc"));//word字节数组
        String ruleNum = "10F70D0942747AB5";//签章规则编号
        long begin = System.currentTimeMillis();
        ChannelMessage message = anySignClientTool.pdfSignByWord(ruleNum, wordBty);
        long end = System.currentTimeMillis();
        System.out.println("********************运行时间:" + (end - begin) / 1000f + "s");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File("C:\\Users\\admin\\Desktop\\pdf to image\\DSVS接口测试程序使用指南.doc"), message.getBody());
        }
    }
    
    @Test
   	public void testPdfSignByTif() throws Exception {//tif转PDF进行签章测试
   		byte[] tifBty = ClientUtil.readFileToByteArray(new File("C:/Users/spirit/Desktop/测试文件/小鸭.tif"));//word字节数组
   		String ruleNum = "53582C51C506201E";//签章规则编号
   		long begin = System.currentTimeMillis();
   		ChannelMessage message = anySignClientTool.pdfSignByTif(ruleNum, tifBty);
   		long end = System.currentTimeMillis();
   		System.out.println("********************运行时间:"+(end - begin)/1000f+"s");
   		System.out.println("状态码："+message.getStatusCode());
   		System.out.println("状态信息："+message.getStatusInfo());
   		if("200".equals(message.getStatusCode())){//成功
   			ClientUtil.writeByteArrayToFile(new File("C:/Users/spirit/Desktop/out_253266.pdf"), message.getBody());
   		}
   	}
    
    @Test
    public void testGenBarcodeAndPdfAndSign() throws Exception {//产生条形码+pdf+签章
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));//pdf字节数组
        String contents = "123456";//new String(ClientUtil.readFileToByteArray(new File("D:/renbaoshou/1111.xml")));
        String barcodeRuleNum = "AB03B44D1702A1C"; 
        String signSealRuleNum = "6FE908DFDEAF8C3C";
        ChannelMessage message = anySignClientTool.genBarcodeAndSign(barcodeRuleNum, signSealRuleNum, contents, pdfBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }
    
    @Test
    public void testBarcodeVerify() throws Exception {//安全二维码验证
        String content = "0C3211A10000000000009959A20150901170046MEQCIHSk9v0fX6ZXirZEycqo6W9nRsgULhqlUA5DQOdaGkTGAiAXqq3Ux3yxn9dzHcSCyN/rS5KJ3WTCXo34N8ZyTsObWw==5YiY5Yag6ZizfDUwMC4wMHzliqnlm7B8MjAxNDAxMjd8546w5Zy6546w6YeR";
        ChannelMessage message = anySignClientTool.barcodeVerify(content);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            String verifyInfo = new String(message.getBody());
            String[] data = verifyInfo.split("&&");
            String certDn = new String(new BASE64Decoder().decodeBuffer(data[0]), "UTF-8");
            System.out.println("**************签名证书主题：" + certDn);
            String signTime = data[1];
            System.out.println("**************签名时间：" + signTime);
            String plainData = new String(new BASE64Decoder().decodeBuffer(data[2]), "UTF-8");
            System.out.println("**************原文数据：" + plainData);
        }
    }
    
    @Test
    public void testPdfMultiPageSign() throws Exception {// PDF多页签章首页签名
        System.out.println("*****************");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
        String ruleNum = "4B680B0282A9B36";
        ChannelMessage message = anySignClientTool.pdfMultiPageSign(ruleNum, pdfBty, "");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }
    
    @Test
    public void testPdfMultiPageSignAll() throws Exception {// PDF多页签章每页签名
        System.out.println("*****************");
        byte[] pdfBty = ClientUtil.readFileToByteArray(new File(testPdfFilePath));
        String ruleNum = "4B680B0282A9B36";
        ChannelMessage message = anySignClientTool.pdfMultiPageSignAll(ruleNum, pdfBty, "");
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息：" + message.getStatusInfo());
        if ("200".equals(message.getStatusCode())) {//成功
            ClientUtil.writeByteArrayToFile(new File(testSignedPdfFilePath), message.getBody());
        }
    }
    
    @Test
	public void testP1DataSign() throws Exception {// P1数据签名
        String polocyNum = "3F89508445039559";
        byte[] pdfBty = "BJCADataSignTest".getBytes();
        long beginTime = System.currentTimeMillis();
        ChannelMessage channelMessage = anySignClientTool.dataP1Sign(polocyNum, pdfBty);
        long endTime = System.currentTimeMillis();
		System.out.println("********************运行时间:" + (endTime - beginTime) / 1000f + "s");
        System.out.println("状态码：" + channelMessage.getStatusCode());
        System.out.println("状态信息：" + channelMessage.getStatusInfo());
        Assert.assertEquals("200", channelMessage.getStatusCode());
        if("200".equals(channelMessage.getStatusCode())){
            ClientUtil.writeByteArrayToFile(new File("D:/out/testP1DataSign"), channelMessage.getBody());
        }
    }
    
	@Test
    public void testP1DataVerify() throws Exception {// P1数据签名验签测试方法
        String polocyNum = "3F89508445039559";
		byte[] pdfBty = "BJCADataSignTest".getBytes();
		long beginTime = System.currentTimeMillis();
        byte[] signData = ClientUtil.readFileToByteArray(new File( "D:/out/testP1DataSign"));
        ChannelMessage channelMessage = anySignClientTool.dataP1Verify(polocyNum, pdfBty,signData);
        long endTime = System.currentTimeMillis();
		System.out.println("********************运行时间:" + (endTime - beginTime) / 1000f + "s");
		System.out.println("状态码：" + channelMessage.getStatusCode());
		System.out.println("状态信息：" + channelMessage.getStatusInfo());
	}
	
    @Test
	public void testGenTSS() throws Exception {// 时间戳签名接口
	    byte[] reqBty = "test123".getBytes();
        ChannelMessage message = anySignClientTool.stf_CreateTSReponse("64A0DF0DF5DA7772", reqBty);//SM2  64A0DF0DF5DA7772  RSA 52178EDD282F07D5
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息："+message.getStatusInfo());
        System.out.println("TSS签名结果：" + new String(message.getBody(),"UTF-8"));
    }

    @Test
    public void testVerifyTSS() throws Exception {// 时间戳签章接口
    	byte[] originalBty = "test123".getBytes();// 原文信息
        // 时间戳签名信息的Base64编码 RSA
        String tssBase64="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";
        ChannelMessage message = anySignClientTool.stf_VerifyTSValidity(tssBase64,originalBty);
        System.out.println("状态码：" + message.getStatusCode());
        System.out.println("状态信息："+message.getStatusInfo());
        if("200".equals(message.getStatusCode())){// 成功
            System.out.println("TSS验证结果：" + new String(message.getBody(),"UTF-8"));
        } else {
            System.out.println("TSS验证结果：" + new String(message.getBody(),"UTF-8"));
        }
    }
    
}
