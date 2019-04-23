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
public class SelectByMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "selectByCond"));
        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));
        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder("select");
        answer.addElement(new TextElement(sb.toString()));
        XmlElement includeBaseColumns = new XmlElement("include");
        includeBaseColumns.addAttribute(new Attribute("refid", this.introspectedTable.getBaseColumnListId()));

        answer.addElement(includeBaseColumns);

        sb.setLength(0);
        sb.append(" from ");
        sb.append(this.getIntrospectedTable().getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeWhereCluse = new XmlElement("include");
        includeWhereCluse.addAttribute(new Attribute("refid", "Map_Where_Clause"));
        answer.addElement(includeWhereCluse);

        parentElement.addElement(answer);
    }

}
