package com.yangdb.ddl2graphql;

import org.jooq.*;
import org.jooq.impl.ConditionInfo;
import org.jooq.impl.ParserException;
import org.jooq.impl.SelectQueryImplWrapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.ConditionUtils.condition;

public final class SQLParser {

    /**
     *
     * @param query
     * @return
     * @throws ParserException
     */
    public static List<Table> parseTable(Query query) throws ParserException {
        if(query instanceof SelectQuery<?>) {
            return new SelectQueryImplWrapper((SelectQuery)query).getTables();
        }
        //default non matching query
        return Collections.emptyList();
    }

    /**
     *
     * @param query
     * @return
     * @throws ParserException
     */
    public static Optional<ConditionInfo> parseWhere(Query query) throws ParserException {
        if(query instanceof SelectQuery<?>) {
            return Optional.ofNullable(condition(new SelectQueryImplWrapper((SelectQuery) query).getWhereCondition()));
        }
        //default non matching query
        return Optional.empty();
    }

}
