package com.yangdb.ddl2graphql;

import org.jooq.*;
import org.jooq.impl.*;

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
    public static List<TableInfo> parseTable(Query query) throws ParserException {
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
    public static Optional<QueryInfo> parseQuery(Query query) {
        if(query instanceof SelectQuery<?>) {
            return Optional.ofNullable(new SelectQueryImplWrapper((SelectQuery) query));
        }
        //default non matching query
        return Optional.empty();
    }


}
