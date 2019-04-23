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
public class UpdateByPrimaryKeySelectiveElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByPrimaryKeyStatementId()));
        answer.addAttribute(new Attribute("parameterType", "java.util.Map"));
        StringBuilder sb = new StringBuilder("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        this.context.getCommentGenerator().addComment(answer);
        answer.addElement(new TextElement(sb.toString()));
        XmlElement setElement = new XmlElement("set");
        answer.addElement(setElement);

        for (IntrospectedColumn introspectedColumn : this.getIntrospectedTable().getNonPrimaryKeyColumns()) {
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty());
            if (introspectedColumn.isJdbcCharacterColumn()) {
                sb.append(" != null and ");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append(" != ''");
            } else {
                sb.append(" != null");
            }
            XmlElement ifSetElement = new XmlElement("if");
            ifSetElement.addAttribute(new Attribute("test", sb.toString()));

            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            sb.append(",");
            ifSetElement.addElement(new TextElement(sb.toString()));
            setElement.addElement(ifSetElement);
        }

        sb.setLength(0);
        boolean and = false;
        for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            if (and) {
                sb.append(" and ");
            } else {
                sb.append("where ");
                and = true;
            }
            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
        }
        answer.addElement(new TextElement(sb.toString()));

        parentElement.addElement(answer);
    }

}
