package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;


/**
 * mybatis xml 生产类
 * @author chenzhinian
 * @date 20190417
 */
public class BaseColumnListElementGenerator extends AbstractXmlElementGenerator {

    public BaseColumnListElementGenerator() {

    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBaseColumnListId()));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        Iterator iterator = this.introspectedTable.getNonBLOBColumns().iterator();
        while(iterator.hasNext()) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase((IntrospectedColumn)iterator.next()));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
            if (sb.length() > 80) {
                answer.addElement(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }
        if (sb.length() > 0) {
            answer.addElement(new TextElement(sb.toString()));
        }
        if (this.context.getPlugins().sqlMapBaseColumnListElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

}
