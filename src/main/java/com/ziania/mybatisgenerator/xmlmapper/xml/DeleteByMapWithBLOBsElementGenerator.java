package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class DeleteByMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete");
        answer.addAttribute(new Attribute("id", "deleteByCond"));
        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder("delete from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());

        answer.addElement(new TextElement(sb.toString()));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", "Map_Where_Clause"));

        answer.addElement(includeElement);

        parentElement.addElement(answer);
    }
}
