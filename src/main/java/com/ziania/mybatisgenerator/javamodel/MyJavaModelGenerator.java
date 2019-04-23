package com.ziania.mybatisgenerator.javamodel;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * java model 生成器
 * @author chenzhinian
 * @date 20190418
 */
public class MyJavaModelGenerator extends AbstractJavaGenerator {

    public MyJavaModelGenerator(){
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.8", table.toString()));
        CommentGenerator commentGenerator = this.context.getCommentGenerator();

        //Model
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);

        //if extends
        FullyQualifiedJavaType superClass = this.getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        commentGenerator.addModelClassComment(topLevelClass, this.introspectedTable);

        // if need constructor
        if (this.introspectedTable.isConstructorBased()) {
            //this.addParameterizedConstructor(topLevelClass);
            if (!this.introspectedTable.isImmutable()) {
                this.addDefaultConstructor(topLevelClass);
            }
        }

        String rootClass = getRootClass();
        //primaryKeyColumns
        for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
            //如果父类中包含则不再生成
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            Field field = this.getStaticField(introspectedColumn, this.context, this.introspectedTable, true);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());
        }
        //other colums not include primaryKeyColumns and blobcolumns
        List<IntrospectedColumn> introspectedColumnsList = this.getClassColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumnsList) {
            //如果父类中包含则不再生成
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }
            Field field = this.getStaticField(introspectedColumn, this.context, this.introspectedTable, false);
            topLevelClass.addField(field);
            topLevelClass.addImportedType(field.getType());
        }

        String tableNm = this.introspectedTable.getFullyQualifiedTableNameAtRuntime();
        Field field = new Field();
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        field.setType(FullyQualifiedJavaType.getStringInstance());
        field.setName("TABLE_NAME");
        field.setInitializationString("\"" + tableNm + "\"");
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());

        List<CompilationUnit> answer = new ArrayList();
        answer.add(topLevelClass);
        return answer;
    }

    private Field getStaticField(IntrospectedColumn introspectedColumn, Context context, IntrospectedTable introspectedTable, boolean isprimaryKey) {
        Field field = new Field();
        field.setVisibility(JavaVisibility.PUBLIC);
        field.setStatic(true);
        field.setFinal(true);
        String columnNm = introspectedColumn.getActualColumnName();
        columnNm = isprimaryKey? "PK_" + columnNm: columnNm;
        field.setName(columnNm);
        String javaPropertyNm = introspectedColumn.getJavaProperty();
        field.setInitializationString("\"" + javaPropertyNm + "\"");
        field.setType(FullyQualifiedJavaType.getStringInstance());
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        return field;
    }

    /**
     * 获取所有列，不含主键和BLOB
     * @return
     */
    private List<IntrospectedColumn> getClassColumns(){
        List<IntrospectedColumn> introspectedColumnList;
        if (this.includePrimaryKeyColumns()) {
            if (this.includelobColumns()) {
                introspectedColumnList = introspectedTable.getAllColumns();
            } else {
                introspectedColumnList = introspectedTable.getNonBLOBColumns();
            }
        } else {
            if (this.includelobColumns()) {
                introspectedColumnList = introspectedTable.getNonPrimaryKeyColumns();
            } else {
                introspectedColumnList = introspectedTable.getBaseColumns();
            }
        }
        return introspectedColumnList;
    }

    private boolean includePrimaryKeyColumns(){
        return !this.introspectedTable.getRules().generatePrimaryKeyClass()
                && this.introspectedTable.hasPrimaryKeyColumns();
    }

    private boolean includelobColumns(){
        return this.introspectedTable.getRules().generateRecordWithBLOBsClass()
                && this.introspectedTable.hasBLOBColumns();
    }

    /**
     * model 文件全路径 targetPackage+subPackages+domain+Model
     * @return
     */
    private String getBaseRecordType() {
        StringBuilder sb = new StringBuilder();
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = this.context.getJavaModelGeneratorConfiguration();
        String targetPackage = javaModelGeneratorConfiguration.getTargetPackage();
        sb.append(targetPackage);
        sb.append(this.introspectedTable.getFullyQualifiedTable().getSubPackageForModel(this.isSubPackagesEnable(javaModelGeneratorConfiguration)));
        sb.append(".domain.");
        sb.append(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        return sb.toString();
    }

    private boolean isSubPackagesEnable(JavaModelGeneratorConfiguration configuration) {
        String enableSubPackages = configuration.getProperty(PropertyRegistry.ANY_ENABLE_SUB_PACKAGES);
        return isTrue(enableSubPackages);
    }

    private FullyQualifiedJavaType getSuperClass() {
        String rootClass = this.getRootClass();
        FullyQualifiedJavaType superClass;
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }
        return superClass;
    }

    private boolean isConstructorBased() {
//        if (this.isImmutable()) {
//            return true;
//        } else {
//            Properties properties;
//            if (this.context.getProperties().containsKey("constructorBased")) {
//                properties = super.tableConfiguration.getProperties();
//            } else {
//                properties = this.context.getJavaModelGeneratorConfiguration().getProperties();
//            }
//            return StringUtility.isTrue(properties.getProperty("constructorBased"));
//        }
        return false;
    }

}