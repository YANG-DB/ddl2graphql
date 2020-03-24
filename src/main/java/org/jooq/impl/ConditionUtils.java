package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Collections;
import java.util.List;

public abstract class ConditionUtils {
    public static ConditionInfo condition(Condition condition) {
        if(condition instanceof CompareCondition)
            return new CompareConditionWrapper(condition);

        if(condition instanceof InCondition<?>)
            return new InConditionWrapper(condition);

        //default none matching condition
        return new ConditionInfo() {
            @Override
            public List<Field> getFields() {
                return Collections.emptyList();
            }

            @Override
            public List<Field> getExpression() {
                return Collections.emptyList();
            }

            @Override
            public boolean isUnary() {
                return false;
            }

            @Override
            public Comparator getComparator() {
                return null;
            }
        };
    }

}
