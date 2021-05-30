package br.com.munif.tools.dinodocs.model;

import java.util.Objects;

public class Inheritance {


    private Class superClass;
    private Class subClaAClass;

    public Inheritance() {

    }

    public Inheritance(Class superClass, Class subClaAClass) {
        this.superClass = superClass;
        this.subClaAClass = subClaAClass;
    }

    public Class getSuperClass() {
        return superClass;
    }

    public void setSuperClass(Class superClass) {
        this.superClass = superClass;
    }

    public Class getSubClaAClass() {
        return subClaAClass;
    }

    public void setSubClaAClass(Class subClaAClass) {
        this.subClaAClass = subClaAClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inheritance that = (Inheritance) o;
        return Objects.equals(superClass, that.superClass) && Objects.equals(subClaAClass, that.subClaAClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(superClass, subClaAClass);
    }

    @Override
    public String toString() {
        return "Inheritance{" +
                "superClass=" + superClass +
                ", subClaAClass=" + subClaAClass +
                '}';
    }
}
