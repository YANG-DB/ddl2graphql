package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Select;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExistsConditionWrapper implements ConditionInfo {
    private ExistsCondition condition;

    public ExistsConditionWrapper(Condition condition) {
        this.condition = (ExistsCondition) condition;
    }

    @Override
    public List<Field> getFields() {
        return Collections.emptyList();
    }


    @Override
    public List<Object> getExpression() {
        try {
            java.lang.reflect.Field valuesProp = ExistsCondition.class.getDeclaredField("query");
            valuesProp.setAccessible(true);
            Select values = (Select) valuesProp.get(condition);
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
        return true;
    }


    @Override
    public Comparator getComparator() {
        try {
            java.lang.reflect.Field valuesProp = ExistsCondition.class.getDeclaredField("exists");
            valuesProp.setAccessible(true);
            Boolean exists = (Boolean) valuesProp.get(this);
            return exists ? Comparator.EXISTS : Comparator.NOT_EXISTS;
        } catch (NoSuchFieldException e) {
            //log error
            return null;
        } catch (IllegalAccessException e) {
            //log error
            return null;
        }

    }


}
