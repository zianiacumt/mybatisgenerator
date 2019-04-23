package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;

public class BatchInsertGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", "batchInsert"));
        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));
        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder("insert into ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("(");
        Iterator<IntrospectedColumn> iterator = this.introspectedTable.getAllColumns().iterator();
        IntrospectedColumn introspectedColumn;
        XmlElement forEachElement = new XmlElement("foreach");
        forEachElement.addAttribute(new Attribute("collection", "batchInsertList"));
        forEachElement.addAttribute(new Attribute("item", "item"));
        forEachElement.addAttribute(new Attribute("index", "index"));
        forEachElement.addAttribute(new Attribute("separator", ","));
        StringBuilder forEachSb = new StringBuilder("(");
        while (iterator.hasNext()) {
             introspectedColumn = (IntrospectedColumn)iterator.next();
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            forEachSb.append("#{item.").append(introspectedColumn.getJavaProperty()).append(", jdbcType=");
            forEachSb.append(introspectedColumn.getJdbcTypeName()).append("}");
            if (iterator.hasNext()) {
               sb.append(",");
                forEachSb.append(",");
            }
        }
        sb.append(")");
        forEachSb.append(")");

        answer.addElement(new TextElement(sb.toString()));

        sb.setLength(0);
        sb.append("values");
        answer.addElement(new TextElement(sb.toString()));

        forEachElement.addElement(new TextElement(forEachSb.toString()));
        answer.addElement(forEachElement);

        parentElement.addElement(answer);
    }

}
