package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.Collections;
import java.util.List;

import static org.jooq.impl.ConditionUtils.NoConditionInfo.INSTANCE;

public abstract class ConditionUtils {


    public static ConditionInfo condition(Condition condition) {
        if(condition instanceof ConditionProviderImpl)
            condition = ((ConditionProviderImpl)condition).getWhere();

        if(condition instanceof CompareCondition)
            return new CompareConditionWrapper(condition);

        if(condition instanceof InCondition<?>)
            return new InConditionWrapper(condition);

        if(condition instanceof ExistsCondition)
            return new ExistsConditionWrapper(condition);

        //default none matching condition
        return INSTANCE;
    }

    public static class NoConditionInfo implements ConditionInfo {

        public static NoConditionInfo INSTANCE = new NoConditionInfo();

        private NoConditionInfo() {}

        @Override
        public List<Field> getFields() {
            return Collections.emptyList();
        }

        @Override
        public List<Object> getExpression() {
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
    }
}
