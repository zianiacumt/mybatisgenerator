package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * mybatis xml 生产类
 * @author chenzhinian
 * @date 20190417
 */
public class SelectByPrimaryKeyElementGenerator extends AbstractXmlElementGenerator {

    public SelectByPrimaryKeyElementGenerator(){
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        String fqjt = "java.util.Map";
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", introspectedTable.getSelectByPrimaryKeyStatementId()));
        answer.addAttribute(new Attribute("parameterType", fqjt));
        answer.addAttribute(new Attribute("resultMap", introspectedTable.getBaseResultMapId()));
        context.getCommentGenerator().addComment(answer);
        answer.addElement(new TextElement("select"));
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "distinct"));
        ifElement.addElement(new TextElement("distinct"));
        answer.addElement(ifElement);

        StringBuilder sb = new StringBuilder();
        if (stringHasValue(introspectedTable.getSelectByExampleQueryId())) {
            sb.append('\'');
            sb.append(introspectedTable.getSelectByExampleQueryId());
            sb.append("' as QUERYID,");
            answer.addElement(new TextElement(sb.toString()));
        }
        answer.addElement(getBaseColumnListElement());

        sb.setLength(0);
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        boolean and = false;
        for(IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()){
            sb.setLength(0);
            if (and) {
                sb.append(" and ");
            } else {
                sb.append("where ");
                and = true;
            }
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        parentElement.addElement(answer);
    }

    @Override
    protected XmlElement getBaseColumnListElement() {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        return answer;
    }


}
