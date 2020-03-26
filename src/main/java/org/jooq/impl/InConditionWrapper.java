package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InConditionWrapper implements ConditionInfo {
    private InCondition condition;

    public InConditionWrapper(Condition condition) {
        this.condition = (InCondition) condition;
    }


    @Override
    public List<Field> getFields() {
        try {
            java.lang.reflect.Field fieldProp = InCondition.class.getDeclaredField("field");
            fieldProp.setAccessible(true);
            Field field = (Field) fieldProp.get(condition);

            return Arrays.asList(field);
        } catch (NoSuchFieldException e) {
            //log error
            return Collections.emptyList();
        } catch (IllegalAccessException e) {
            //log error
            return Collections.emptyList();
        }
    }

    @Override
    public List<Object> getExpression() {
        try {
            java.lang.reflect.Field valuesProp = InCondition.class.getDeclaredField("values");
            valuesProp.setAccessible(true);
            Field[] values = (Field[]) valuesProp.get(condition);

            return Arrays.asList(values);
        } catch (NoSuchFieldException e) {
            //log error
            return Collections.emptyList();
        } catch (IllegalAccessException e) {
            //log error
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isUnary() {
        return false;
    }

    @Override
    public Comparator getComparator() {
        try {
            java.lang.reflect.Field comperator = InCondition.class.getDeclaredField("comparator");
            comperator.setAccessible(true);
            return (Comparator) comperator.get(condition);
        } catch (NoSuchFieldException e) {
            //log error
            return null;
        } catch (IllegalAccessException e) {
            //log error
            return null;
        }
    }


}
