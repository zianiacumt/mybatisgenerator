package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

public class MapWhereCaluseElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "Map_Where_Clause"));
        this.context.getCommentGenerator().addComment(answer);

        XmlElement whereElement = new XmlElement("where");
        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
            XmlElement ifElement = new XmlElement("if");
            sb.append(introspectedColumn.getJavaProperty());
            if (introspectedColumn.isJdbcCharacterColumn()) {
                sb.append(" != null and ");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != ''");
            } else {
                sb.append(" != null");
            }

            ifElement.addAttribute(new Attribute("test", sb.toString()));
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            ifElement.addElement(new TextElement(sb.toString()));
            sb.setLength(0);

            whereElement.addElement(ifElement);
        }
        answer.addElement(whereElement);

        parentElement.addElement(answer);
    }

}
