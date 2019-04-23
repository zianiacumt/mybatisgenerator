package com.ziania.mybatisgenerator.javaclient;

import com.ziania.mybatisgenerator.xmlmapper.MyXmlMapperGenerator;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.JavaClientGeneratorConfiguration;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.*;

import static com.ziania.mybatisgenerator.constants.Constants.*;
import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * java mapper 生成器
 * @author chenzhinian
 * @date 20190418
 */
public class MyJavaClientGenerator extends AbstractJavaClientGenerator {

    public MyJavaClientGenerator() {
        super(true);
    }

    public MyJavaClientGenerator(boolean requiresXMLGenerator) {
        super(requiresXMLGenerator);
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new MyXmlMapperGenerator();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        this.progressCallback.startTask(Messages.getString("Progress.17",
                this.introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = this.context.getCommentGenerator();
        //接口全路径
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.calculateJavaClientImplementationPackage());
        stringBuilder.append(".interfaces.I");
        stringBuilder.append(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        stringBuilder.append("Service");
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(stringBuilder.toString());
        stringBuilder.setLength(0);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        String rootInterface = this.introspectedTable.getTableConfigurationProperty("rootInterface");
        if (!stringHasValue(rootInterface)) {
            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
        }
        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }

        //接口实现类全路径
        stringBuilder.append(this.calculateJavaClientImplementationPackage());
        stringBuilder.append(".impls.");
        stringBuilder.append(this.introspectedTable.getFullyQualifiedTable().getDomainObjectName());
        stringBuilder.append("ServiceImpl");
        type = new FullyQualifiedJavaType(stringBuilder.toString());
        stringBuilder.setLength(0);
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        //implements
        topLevelClass.addImportedType(interfaze.getType());
        topLevelClass.addSuperInterface(interfaze.getType());
        type = new FullyQualifiedJavaType(extendsService);
        //extends
        topLevelClass.setSuperClass(type);
        topLevelClass.addImportedType(type);

        //注解
        topLevelClass.addAnnotation("@Repository");
        topLevelClass.addImportedType(new FullyQualifiedJavaType(annotation_class));

        //以下是涉及的方法
        this.addCountByCond(interfaze, topLevelClass);
        this.addSelectByCond(interfaze, topLevelClass);
        this.addSelectByPrimaryKey(interfaze, topLevelClass);
        this.addDeleteByCond(interfaze, topLevelClass);
        this.addDeleteByPrimaryKey(interfaze, topLevelClass);
        this.addUpdateByPrimaryKey(interfaze, topLevelClass);
        this.addInsertSelective(interfaze, topLevelClass);
        this.addBatchInsertSelective(interfaze, topLevelClass);

        List<CompilationUnit> answer = new ArrayList();
        answer.add(interfaze);
        answer.add(topLevelClass);
        return answer;
    }

    /**
     * batch insert method
     * @param interfaze
     * @param topLevelClass
     */
    public void addBatchInsertSelective(Interface interfaze, TopLevelClass topLevelClass){
        Method implMethod = this.getMethod(method_insertBatch);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int insertCount = getBaseDao().insertSelective(\"" + nameSpace + "." + method_insertBatch + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"insertCount\", insertCount);");
        implMethod.addBodyLine("return returnMap;");

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * insert method
     * @param interfaze
     * @param topLevelClass
     */
    public void addInsertSelective(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_insertSelective);
        Method implMethod = this.getMethod(method_insertSelective);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int insertCount = getBaseDao().insertSelective(\"" + nameSpace + "." + method_insertSelective + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"insertCount\", insertCount);");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * updateByPrimaryKey method
     * @param interfaze
     * @param topLevelClass
     */
    public void addUpdateByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_updateByPrimaryKey);
        Method implMethod = this.getMethod(method_updateByPrimaryKey);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int updateCount = getBaseDao().updateByPrimaryKey(\"" + nameSpace + "." + method_updateByPrimaryKey + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"updateCount\", updateCount);");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * deleteByPrimaryKey method
     * @param interfaze
     * @param topLevelClass
     */
    public void addDeleteByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_deleteByPrimaryKey);
        Method implMethod = this.getMethod(method_deleteByPrimaryKey);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int deleteCount = getBaseDao().deleteByPrimaryKey(\"" + nameSpace + "." + method_deleteByPrimaryKey + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"deleteCount\", deleteCount);");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * deleteByCond method
     * @param interfaze
     * @param topLevelClass
     */
    public void addDeleteByCond(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_deleteByCond);
        Method implMethod = this.getMethod(method_deleteByCond);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int deleteCount = getBaseDao().deleteByCond(\"" + nameSpace + "." + method_deleteByCond + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"deleteCount\", deleteCount);");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * selectByPrimaryKey method
     * @param interfaze
     * @param topLevelClass
     */
    public void addSelectByPrimaryKey(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_selectByPrimaryKey);
        Method implMethod = this.getMethod(method_selectByPrimaryKey);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("Map<String, Object> objectMap = (Map<String, Object>)getBaseDao().selectByPrimaryKey(\"" + nameSpace + "." + method_selectByPrimaryKey + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"bean\", objectMap);");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * selectByCond method
     * @param interfaze
     * @param topLevelClass
     */
    public void addSelectByCond(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_selectByCond);
        Method implMethod = this.getMethod(method_selectByCond);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        implMethod.addBodyLine("Map<String, Object> countMap = this.countByCond(params);");
        implMethod.addBodyLine("if (MapUtils.getInteger(countMap, \"total\") > 0) {");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("List<Map<String, Object>> queryList = getBaseDao().selectByCond(\"" + nameSpace + "." + method_selectByCond + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"total\", MapUtils.getInteger(countMap, \"total\"));");
        implMethod.addBodyLine("returnMap.put(\"beans\", queryList);");
        implMethod.addBodyLine("}");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    /**
     * countByCond method
     * @param interfaze
     * @param topLevelClass
     */
    private void addCountByCond(Interface interfaze, TopLevelClass topLevelClass) {
        Method interfaceMethod = this.getMethod(method_countByCond);
        Method implMethod = this.getMethod(method_countByCond);
        String objectName = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
        implMethod.addBodyLine("Map<String, Object> returnMap = new HashMap<>();");
        implMethod.addBodyLine("if (MapUtils.isNotEmpty(params)) {");
        implMethod.addBodyLine("params.put(" + objectName + ".TABLE_NAME, " + objectName + ".TABLE_NAME);");
        String nameSpace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        implMethod.addBodyLine("int total = getBaseDao().queryCount(\"" + nameSpace + "." + method_countByCond + "\", params);");
        implMethod.addBodyLine("returnMap.put(\"total\", total);");
        implMethod.addBodyLine("} else {");
        implMethod.addBodyLine("returnMap.put(\"total\", 0);");
        implMethod.addBodyLine("}");
        implMethod.addBodyLine("return returnMap;");

        interfaze.addMethod(interfaceMethod);
        interfaze.addImportedTypes(this.getImporedTypes(true));

        topLevelClass.addMethod(implMethod);
        topLevelClass.addImportedTypes(this.getImporedTypes(false));
    }

    private Set<FullyQualifiedJavaType> getImporedTypes(boolean isInterface){
        Set<FullyQualifiedJavaType> importedTypes = new HashSet<FullyQualifiedJavaType>();
        importedTypes.add(new FullyQualifiedJavaType(List.class.getName()));
        importedTypes.add(new FullyQualifiedJavaType(Map.class.getName()));
        importedTypes.add(new FullyQualifiedJavaType(methodException));
        if (!isInterface) {
            importedTypes.add(new FullyQualifiedJavaType("org.apache.commons.collections.MapUtils"));
            String targetPackage = this.context.getJavaModelGeneratorConfiguration().getTargetPackage();
            String subPackage = this.introspectedTable.getFullyQualifiedTable().getDomainObjectName();
            importedTypes.add(new FullyQualifiedJavaType(targetPackage + ".domain." + subPackage));
            importedTypes.add(new FullyQualifiedJavaType(HashMap.class.getName()));
        }
        return importedTypes;
    }

    private Method getMethod(String methodNm){
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(methodNm);
        Parameter parameter = new Parameter(new FullyQualifiedJavaType(Map.class.getName()), "params");
        method.addParameter(parameter);
        method.addException(new FullyQualifiedJavaType(methodException));
        method.setReturnType(new FullyQualifiedJavaType(Map.class.getName()));
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
        return method;
    }

    private String calculateJavaClientImplementationPackage(){
        JavaClientGeneratorConfiguration configuration = this.context.getJavaClientGeneratorConfiguration();
        if (configuration == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        String implementationPackage = configuration.getImplementationPackage();
        if (stringHasValue(implementationPackage)) {
            sb.append(implementationPackage);
        } else {
            sb.append(configuration.getTargetPackage());
        }
        return sb.toString();
    }

}