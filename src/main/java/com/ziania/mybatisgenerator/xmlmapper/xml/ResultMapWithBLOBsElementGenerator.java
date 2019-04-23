package com.ziania.mybatisgenerator.xmlmapper.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.List;
import java.util.Map;

/**
 * mybatis xml 生产类
 * @author chenzhinian
 * @date 20190417
 */
public class ResultMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public ResultMapWithBLOBsElementGenerator(boolean isSimple){
        super();
        this.isSimple = isSimple;
    }

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBaseResultMapId()));
        String returnType;
        if (this.isSimple) {
            returnType = this.introspectedTable.getBaseRecordType();
        } else if (this.introspectedTable.getRules().generateBaseRecordClass()) {
            returnType = Map.class.getName();
        } else {
            returnType = this.introspectedTable.getPrimaryKeyType();
        }

        answer.addAttribute(new Attribute("type", returnType));
        this.context.getCommentGenerator().addComment(answer);
        if (this.introspectedTable.isConstructorBased()) {
            this.addResultMapConstructorElements(answer);
        } else {
            this.addResultMapElements(answer);
        }

        if (this.context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }

    private void addResultMapElements(XmlElement answer) {
        List<IntrospectedColumn> introspectedColumnList = this.introspectedTable.getPrimaryKeyColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumnList) {
            XmlElement resultElement = new XmlElement("id");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }

        List<IntrospectedColumn> columns;
        if (this.isSimple) {
            columns = this.introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = this.introspectedTable.getBaseColumns();
        }
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("result");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            answer.addElement(resultElement);
        }
    }

    private void addResultMapConstructorElements(XmlElement answer) {
        XmlElement constructor = new XmlElement("constructor");
        List<IntrospectedColumn> introspectedColumnList = this.introspectedTable.getPrimaryKeyColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumnList) {
            XmlElement resultElement = resultElement = new XmlElement("idArg");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            constructor.addElement(resultElement);
        }

        List<IntrospectedColumn> columns;
        if (this.isSimple) {
            columns = this.introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = this.introspectedTable.getBaseColumns();
        }
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("arg");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
            }
            constructor.addElement(resultElement);
        }
        answer.addElement(constructor);
    }

}