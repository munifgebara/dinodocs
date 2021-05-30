/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.munif.tools.dinodocs.model;

import br.com.munif.tools.dinodocs.App;
import br.com.munif.tools.dinodocs.GebUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Entity;

/**
 *
 * @author mgebara
 */
public class ClassForFreemaker {

    private Class innerClass;
    private final List<Field> toOneAssociationFields;
    private final List<Field> toManyAssociationFields;
    private final List<Field> instanceFields;

    public ClassForFreemaker(Class innerClass) {
        this.innerClass = innerClass;
        List<Class> classes = App.project.getClasses();
        List<Field> allFields = GebUtils.getAllFields(innerClass);
        this.toOneAssociationFields = allFields.stream().filter(field -> (!Modifier.isStatic(field.getModifiers())) &&  classes.contains(field)).collect(Collectors.toList());
        allFields.removeAll(toOneAssociationFields);
        this.toManyAssociationFields = GebUtils.getAllFields(innerClass).stream().filter(field -> (!Modifier.isStatic(field.getModifiers())) && classes.contains(field.getGenericType())).collect(Collectors.toList());
        allFields.removeAll(toOneAssociationFields);
        this.instanceFields = allFields;
        if (!(this.toOneAssociationFields.isEmpty()&&this.toManyAssociationFields.isEmpty())){
            System.out.println(innerClass.getSimpleName()+" tem associacoes "+(isHeir()?" tem heranca" : ""));
            System.out.println(this.toOneAssociationFields+ " "+this.toManyAssociationFields+"\n");
        }

    }
    
    public boolean isHeir(){
        return App.project.getClasses().contains(this.innerClass.getSuperclass());
    }

    public Class getInnerClass() {
        return innerClass;
    }

    public List<Field> getToOneAssociationFields() {
        return toOneAssociationFields;
    }

    public List<Field> getToManyAssociationFields() {
        return toManyAssociationFields;
    }

    public List<Field> getInstanceFields() {
        return instanceFields;
    }

    public String getInfo() {
        return "ClassForFreemaker{" + "innerClass=" + innerClass + ", toOneAssociationFields=" + toOneAssociationFields + ", toManyAssociationFields=" + toManyAssociationFields + ", instanceFields=" + instanceFields + '}';
    }

    @Override
    public String toString() {
        return innerClass.toString();
    }

    public String toGenericString() {
        return innerClass.toGenericString();
    }

    public String getSimpleName() {
        return innerClass.getSimpleName();
    }

    @Override
    public int hashCode() {
        return innerClass.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return innerClass.equals(obj);
    }

}
