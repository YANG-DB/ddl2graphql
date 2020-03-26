package org.jooq.impl;

import org.jooq.SelectQuery;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.jooq.impl.ConditionUtils.condition;

public class SelectQueryImplWrapper implements QueryInfo {
    private SelectQueryImpl selectQuery;

    public SelectQueryImplWrapper(SelectQuery selectQuery) {
        this.selectQuery = (SelectQueryImpl) selectQuery;
    }

    @Override
    public List<TableInfo> getTables() {
        return StreamSupport.stream(this.selectQuery.getFrom().spliterator(),true).map(TableWrapper::of).collect(Collectors.toList());
    }

    @Override
    public ConditionInfo getWhereCondition() {
        return condition(this.selectQuery.getWhere().getWhere());
    }
}
