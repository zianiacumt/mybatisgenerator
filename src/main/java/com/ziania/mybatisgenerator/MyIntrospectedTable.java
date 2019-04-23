package com.ziania.mybatisgenerator;

import com.ziania.mybatisgenerator.javamodel.MyJavaModelGenerator;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;

import java.util.List;

public class MyIntrospectedTable extends IntrospectedTableMyBatis3Impl {

    public MyIntrospectedTable() {
        super();
    }

    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
        this.calculateJavaModelGenerators(warnings, progressCallback);
        AbstractJavaClientGenerator javaClientGenerator = this.calculateClientGenerators(warnings, progressCallback);
        this.calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    @Override
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaGenerator javaGenerator = new MyJavaModelGenerator();
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.javaModelGenerators.add(javaGenerator);
    }

    @Override
    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        this.clientGenerators.add(javaGenerator);
        return javaGenerator;
    }

    @Override
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (javaClientGenerator == null) {
            if (context.getSqlMapGeneratorConfiguration() != null) {
                xmlMapperGenerator = new XMLMapperGenerator();
            }
        } else {
            xmlMapperGenerator = javaClientGenerator.getMatchedXMLGenerator();
        }
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

}
