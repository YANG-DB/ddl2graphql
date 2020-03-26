package com.yangdb.ddl2graphql;

import org.jooq.*;
import org.jooq.Comparator;
import org.jooq.impl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * see https://www.jooq.org/doc/3.13/manual/sql-building/sql-interpreter/
 */
public class DMLQueryWhereClauseParsingTest {
    public static final String QUERY_WHERE_COMPARATOR_CLAUSE = "select category_id, category_name as name from production.categories " +
            "  where category_id > 100 order by category_id ";

    public static final String QUERY_WHERE_IN_CLAUSE= "select category_id, category_name as name from production.categories " +
            "  where category_id in(100,101,102) order by category_id ";

    private Parser parser;


    @Before
    public void setUp() throws Exception {
        DSLContext ctx =new DefaultDSLContext(SQLDialect.DEFAULT);
        parser = ctx.parser();
    }

    @Test
    public void testSchemaDDLParsingWhereComparatorClause(){
        Query query = parser.parseQuery(QUERY_WHERE_COMPARATOR_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertEquals(selectQuery.getSelect().size(),2);
        Assert.assertEquals(selectQuery.getSelect().stream().map(Object::toString).collect(Collectors.toSet()),
                new HashSet<>(Arrays.asList("CATEGORY_ID","NAME")));

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getName(),"CATEGORIES");
        Assert.assertEquals(table.getSchema().getName(),"PRODUCTION");

        ConditionInfo condition = SQLParser.parseQuery(selectQuery).get().getWhereCondition();
        Assert.assertTrue(condition instanceof CompareConditionWrapper);
        Assert.assertEquals(condition.getFields().get(0).getName(),"CATEGORY_ID");
        Assert.assertEquals(((Field)condition.getExpression().get(0)).getName(),"100");
        Assert.assertEquals(condition.getComparator(), Comparator.GREATER);


    }

    @Test
    public void testSchemaDDLParsingWhereInClause(){
        Query query = parser.parseQuery(QUERY_WHERE_IN_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertEquals(selectQuery.getSelect().size(),2);
        Assert.assertEquals(selectQuery.getSelect().stream().map(Object::toString).collect(Collectors.toSet()),
                new HashSet<>(Arrays.asList("CATEGORY_ID","NAME")));

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getName(),"CATEGORIES");
        Assert.assertEquals(table.getSchema().getName(),"PRODUCTION");

        ConditionInfo condition = SQLParser.parseQuery(selectQuery).get().getWhereCondition();
        Assert.assertTrue(condition instanceof InConditionWrapper);
        Assert.assertEquals(condition.getFields().get(0).getName(),"CATEGORY_ID");
        Assert.assertEquals(condition.getExpression().size(),3);
        Assert.assertEquals(condition.getExpression().stream().map(p->((Field)p).getName()).collect(Collectors.toList())
                ,Arrays.asList("100","101","102"));
        Assert.assertEquals(condition.getComparator(), Comparator.IN);


    }
}
