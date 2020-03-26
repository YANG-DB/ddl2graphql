package org.jooq.impl;

import java.util.List;

public interface QueryInfo {
    /**
     *
     * @return
     */
    List<TableInfo> getTables();

    /**
     *
     * @return
     */
    ConditionInfo getWhereCondition();
}
