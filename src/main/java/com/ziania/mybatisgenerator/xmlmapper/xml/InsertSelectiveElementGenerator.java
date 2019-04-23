package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * mybatis xml 生产类
 * @author chenzhinian
 * @date 20190417
 */
public class InsertSelectiveElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        String fqjt = "java.util.Map";
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertSelectiveStatementId()));
        answer.addAttribute(new Attribute("parameterType", fqjt));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement insertTrimEle = new XmlElement("trim");
        insertTrimEle.addAttribute(new Attribute("prefix", "("));
        insertTrimEle.addAttribute(new Attribute("suffix", ")"));
        insertTrimEle.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(insertTrimEle);

        XmlElement valuesTrimEle = new XmlElement("trim");
        valuesTrimEle.addAttribute(new Attribute("prefix", "values ("));
        valuesTrimEle.addAttribute(new Attribute("suffix", ")"));
        valuesTrimEle.addAttribute(new Attribute("suffixOverrides", ","));
        answer.addElement(valuesTrimEle);

        StringBuilder insertSb = new StringBuilder();
        StringBuilder valuesSb = new StringBuilder();
        for (IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
            insertSb.setLength(0);
            valuesSb.setLength(0);
            XmlElement insertNotNullEle = new XmlElement("if");
            XmlElement valuesNotNullEle = new XmlElement("if");
            insertSb.append(introspectedColumn.getJavaProperty());
            valuesSb.append(introspectedColumn.getJavaProperty());
            if (introspectedColumn.isJdbcCharacterColumn()) {
                insertSb.append(" != null and ");
                insertSb.append(introspectedColumn.getJavaProperty());
                insertSb.append(" != ''");
                valuesSb.append(" != null and ");
                valuesSb.append(introspectedColumn.getJavaProperty());
                valuesSb.append(" != ''");
            } else {
                insertSb.append("!= null");
                valuesSb.append("!= null");
            }
            insertNotNullEle.addAttribute(new Attribute("test", insertSb.toString()));
            valuesNotNullEle.addAttribute(new Attribute("test", valuesSb.toString()));

            insertSb.setLength(0);
            insertSb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            insertSb.append(",");

            valuesSb.setLength(0);
            valuesSb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            valuesSb.append(",");

            insertNotNullEle.addElement(new TextElement(insertSb.toString()));
            valuesNotNullEle.addElement(new TextElement(valuesSb.toString()));

            insertTrimEle.addElement(insertNotNullEle);
            valuesTrimEle.addElement(valuesNotNullEle);
        }

        parentElement.addElement(answer);
    }

}
