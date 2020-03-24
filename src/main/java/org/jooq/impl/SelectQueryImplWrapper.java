package org.jooq.impl;

import org.jooq.Condition;
import org.jooq.SelectQuery;
import org.jooq.Table;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SelectQueryImplWrapper {
    private SelectQueryImpl selectQuery;

    public SelectQueryImplWrapper(SelectQuery selectQuery) {
        this.selectQuery = (SelectQueryImpl) selectQuery;
    }

    public List<Table> getTables() {
        return StreamSupport.stream(this.selectQuery.getFrom().spliterator(),true).map(p->(Table)p).collect(Collectors.toList());
    }

    public Condition getWhereCondition() {
        return this.selectQuery.getWhere().getWhere();
    }
}
