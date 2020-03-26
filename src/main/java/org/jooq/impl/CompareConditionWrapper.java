package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Arrays;
import java.util.List;

public class CompareConditionWrapper implements ConditionInfo {
    private CompareCondition condition;

    public CompareConditionWrapper(Condition condition) {
        this.condition = (CompareCondition) condition;
    }

    @Override
    public List<Field> getFields() {
        return Arrays.asList(condition.field1);
    }

    @Override
    public List<Object> getExpression() {
        return Arrays.asList(condition.field2);
    }

    @Override
    public boolean isUnary() {
        return false;
    }


    @Override
    public Comparator getComparator() {
        return condition.comparator;
    }


}
