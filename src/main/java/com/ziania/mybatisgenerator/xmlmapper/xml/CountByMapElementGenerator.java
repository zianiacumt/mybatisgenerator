package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * mybatis xml 生产类
 * @author chenzhinian
 * @date 20190417
 */
public class CountByMapElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "countByCond"));
        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder("select count(1) from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        sb.setLength(0);

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "Map_Where_Clause"));
        answer.addElement(includeElement);

        parentElement.addElement(answer);
    }

}
