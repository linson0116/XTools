package com.linson.xtools.app02.utils;

import android.util.Xml;

import com.linson.xtools.app02.domain.SmsInfo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/27.
 */
public class XmlUtils {
    public static void createSmsInfo(File file, String[] arrs) {
        Document doc = DocumentHelper.createDocument();
        //建立根节点
        Element smssElement = doc.addElement("smss");
        //建立子节点
        Element smsElement = smssElement.addElement("sms");
        //添加子节点中的节点并设置内容
        Element number = smsElement.addElement("number");
        number.setText(arrs[0]);
        Element body = smsElement.addElement("body");
        body.setText(arrs[1]);
        Element date = smsElement.addElement("date");
        date.setText(arrs[2]);
        //输出文件
        try {
            //OutputFormat format = new OutputFormat("    ", true, "UTF-8");
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");

            XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
            writer.write(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addSmsInfo(File file, String[] arrs) {
        SAXReader reader = new SAXReader();
        reader.setStripWhitespaceText(true);
        reader.setMergeAdjacentText(true);
        try {
            Document document = reader.read(file);
            //得到根节点
            Element smssElement = document.getRootElement();
            //添加子节点
            Element sms = smssElement.addElement("sms");

            Element number = sms.addElement("number");
            number.setText(arrs[0]);
            Element body = sms.addElement("body");
            body.setText(arrs[1]);
            Element date = sms.addElement("date");
            date.setText(arrs[2]);

            OutputFormat format = new OutputFormat("	", true, "UTF-8");
            FileWriter fw = new FileWriter(file);
            XMLWriter writer = new XMLWriter(fw, format);
            writer.write(document);
            fw.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<SmsInfo> getSmsList(InputStream is) {
        ArrayList<SmsInfo> list = new ArrayList<>();
        XmlPullParser pullParser = Xml.newPullParser();
        try {
            pullParser.setInput(is, "utf-8");
            int type = pullParser.getEventType();
            SmsInfo smsInfo = null;
            while (type != XmlPullParser.END_DOCUMENT) {
                String name = pullParser.getName();
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("sms")) {
                            smsInfo = new SmsInfo();
                        } else if (name.equals("number")) {
                            smsInfo.setNumber(pullParser.nextText());
                        } else if (name.equals("body")) {
                            smsInfo.setBody(pullParser.nextText());
                        } else if (name.equals("date")) {
                            smsInfo.setDate(pullParser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("sms")) {
                            list.add(smsInfo);
                        }
                        break;
                }
                type = pullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }
}
