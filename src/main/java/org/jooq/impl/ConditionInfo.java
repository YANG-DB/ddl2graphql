package org.jooq.impl;

import org.jooq.Comparator;
import org.jooq.Field;

import java.util.List;

public interface ConditionInfo {
    List<Field> getFields();

    List<Field> getExpression();

    boolean isUnary();

    Comparator getComparator();
}
