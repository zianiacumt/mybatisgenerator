package com.ziania.mybatisgenerator.xmlmapper;

import com.ziania.mybatisgenerator.xmlmapper.xml.*;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 * mybatis xml 生成器
 * @author chenzhinian
 * @date 20190417
 */
public class MyXmlMapperGenerator extends AbstractXmlGenerator {

    public MyXmlMapperGenerator() {

    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);

        this.addResultMapWithBLOBsElement(answer);
        this.addBaseColumnListElement(answer);
        this.addMapWhereCaluseElement(answer);

        this.addCountByMapElement(answer);
        this.addSelectByPrimaryKeyElement(answer);
        this.addSelectByMapWithBLOBsElement(answer);

        this.addDeleteByPrimaryKeyElement(answer);
        this.addDeleteByMapWithBLOBsElement(answer);
        this.addUpdateByPrimaryKeySelectiveElement(answer);

        this.addInsertSelectiveElement(answer);
        this.addBatchInsertGenerator(answer);
        return answer;
    }

    private void addDeleteByMapWithBLOBsElement(XmlElement parentElement){
        AbstractXmlElementGenerator xmlGenerator = new DeleteByMapWithBLOBsElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addMapWhereCaluseElement(XmlElement parentElement){
        AbstractXmlElementGenerator xmlGenerator = new MapWhereCaluseElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addBatchInsertGenerator(XmlElement parentElement){
        AbstractXmlElementGenerator xmlGenerator = new BatchInsertGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addCountByMapElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new CountByMapElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addSelectByMapWithBLOBsElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new SelectByMapWithBLOBsElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new UpdateByPrimaryKeySelectiveElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addInsertSelectiveElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new InsertSelectiveElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new DeleteByPrimaryKeyElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new SelectByPrimaryKeyElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addResultMapWithBLOBsElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new ResultMapWithBLOBsElementGenerator(false);
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    private void addBaseColumnListElement(XmlElement parentElement) {
        AbstractXmlElementGenerator xmlGenerator = new BaseColumnListElementGenerator();
        this.initializeAndExecuteGenerator(xmlGenerator, parentElement);
    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(this.context);
        elementGenerator.setIntrospectedTable(this.introspectedTable);
        elementGenerator.setProgressCallback(this.progressCallback);
        elementGenerator.setWarnings(this.warnings);
        elementGenerator.addElements(parentElement);
    }

    @Override
    public Document getDocument() {
        Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        document.setRootElement(this.getSqlMapElement());
        if (!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
            document = null;
        }
        return document;
    }

}