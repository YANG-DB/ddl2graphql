package com.yangdb.ddl2graphql;

import org.jooq.*;
import org.jooq.impl.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * see https://www.jooq.org/doc/3.13/manual/sql-building/sql-interpreter/
 */
public class DMLQueryJoinClauseParsingTest {
    public static final String QUERY_INNER_JOIN_CLAUSE = "SELECT *\n" +
            "FROM AUTHOR\n" +
            "JOIN BOOK ON BOOK.AUTHOR_ID = AUTHOR.ID";

    public static final String QUERY_LEFT_OUTER_JOIN_CLAUSE= "SELECT *\n" +
            "FROM AUTHOR\n" +
            "LEFT OUTER JOIN (\n" +
            "  BOOK JOIN BOOK_TO_BOOK_STORE\n" +
            "       ON BOOK_TO_BOOK_STORE.BOOK_ID = BOOK.ID\n" +
            ")\n" +
            "ON BOOK.AUTHOR_ID = AUTHOR.ID";

    public static final String QUERY_FULL_OUTER_JOIN_CLAUSE= "SELECT *\n" +
            "FROM AUTHOR\n" +
            "FULL OUTER JOIN (\n" +
            "  BOOK JOIN BOOK_TO_BOOK_STORE\n" +
            "       ON BOOK_TO_BOOK_STORE.BOOK_ID = BOOK.ID\n" +
            ")\n" +
            "ON BOOK.AUTHOR_ID = AUTHOR.ID";

    public static final String QUERY_EXISTS_JOIN_CLAUSE = "SELECT * \n" +
            "FROM AUTHOR\n" +
            "WHERE EXISTS (\n" +
            "  SELECT 1 FROM BOOK WHERE AUTHOR.ID = BOOK.AUTHOR_ID\n" +
            ")";

    private Parser parser;


    @Before
    public void setUp() throws Exception {
        DSLContext ctx =new DefaultDSLContext(SQLDialect.DEFAULT);
        parser = ctx.parser();
    }

    @Test
    public void testSchemaDDLParsingExistsJoinClause(){
        Query query = parser.parseQuery(QUERY_EXISTS_JOIN_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertTrue(selectQuery.getSelect().isEmpty());

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getType().name(),"TABLE");
        Assert.assertFalse(table.isJoinTable());

        ConditionInfo condition = SQLParser.parseQuery(selectQuery).get().getWhereCondition();
        Assert.assertTrue(condition instanceof ExistsConditionWrapper);

        Assert.assertTrue(condition.getFields().isEmpty() );
        Assert.assertTrue(condition.getExpression().get(0) instanceof SelectQuery );

        Optional<QueryInfo> innerQuery = SQLParser.parseQuery((SelectQuery) condition.getExpression().get(0));
        Assert.assertEquals(innerQuery.get().getTables().size(), 1);
        TableInfo tableInfo = innerQuery.get().getTables().get(0);
        Assert.assertEquals(tableInfo.getName(), "BOOK");

        ConditionInfo conditionInfo = innerQuery.get().getWhereCondition();
        Assert.assertEquals(((Field)conditionInfo.getExpression().get(0)).getName(), "AUTHOR_ID");
        Assert.assertEquals(((Field)conditionInfo.getExpression().get(0)).getQualifiedName().toString(), "BOOK.AUTHOR_ID");
        Assert.assertEquals((conditionInfo.getFields().get(0)).getName(), "ID");
        Assert.assertEquals((conditionInfo.getFields().get(0)).getQualifiedName().toString(), "AUTHOR.ID");
        Assert.assertEquals(conditionInfo.getComparator(), Comparator.EQUALS);

    }
    @Test
    public void testSchemaDDLParsingLeftOuterJoinClause(){
        Query query = parser.parseQuery(QUERY_LEFT_OUTER_JOIN_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertTrue(selectQuery.getSelect().isEmpty());

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getType().name(),"EXPRESSION");
        Assert.assertTrue(table.isJoinTable());
        Assert.assertEquals(table.getJoinType().get(),JoinType.LEFT_OUTER_JOIN);

        TableInfo lhs = (TableInfo) table.getLeftHandSelection().get();
        Assert.assertEquals(lhs.getName(),"AUTHOR");
        Assert.assertEquals( lhs.getType().name(),"TABLE");

        TableInfo rhs = (TableInfo) table.getRightHandSelection().get();
        Assert.assertTrue(rhs.isJoinTable());
        Assert.assertEquals(rhs.getName(),"join");
        Assert.assertEquals( rhs.getType().name(),"EXPRESSION");

        Assert.assertEquals( ((TableInfo) rhs.getLeftHandSelection().get()).getType().name(),"TABLE");
        Assert.assertEquals( ((TableInfo) rhs.getLeftHandSelection().get()).getName(),"BOOK");

        Assert.assertEquals( ((TableInfo) rhs.getRightHandSelection().get()).getType().name(),"TABLE");
        Assert.assertEquals( ((TableInfo) rhs.getRightHandSelection().get()).getName(),"BOOK_TO_BOOK_STORE");


        ConditionInfo condition = (ConditionInfo) table.getCondition().get();
        Assert.assertTrue(condition instanceof CompareConditionWrapper);
        Assert.assertEquals(condition.getFields().get(0).getName(),"AUTHOR_ID");
        Assert.assertEquals(((Field)condition.getExpression().get(0)).getName(),"ID");
        Assert.assertEquals(condition.getComparator(), Comparator.EQUALS);


    }
    @Test
    public void testSchemaDDLParsingInnerJoinClause(){
        Query query = parser.parseQuery(QUERY_INNER_JOIN_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertTrue(selectQuery.getSelect().isEmpty());

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getType().name(),"EXPRESSION");
        Assert.assertTrue(table.isJoinTable());
        Assert.assertEquals(table.getJoinType().get(),JoinType.JOIN);

        TableInfo lhs = (TableInfo) table.getLeftHandSelection().get();
        Assert.assertEquals(lhs.getName(),"AUTHOR");
        Assert.assertEquals( lhs.getType().name(),"TABLE");

        TableInfo rhs = (TableInfo) table.getRightHandSelection().get();
        Assert.assertEquals(rhs.getName(),"BOOK");
        Assert.assertEquals( rhs.getType().name(),"TABLE");

        ConditionInfo condition = (ConditionInfo) table.getCondition().get();
        Assert.assertTrue(condition instanceof CompareConditionWrapper);
        Assert.assertEquals(condition.getFields().get(0).getName(),"AUTHOR_ID");
        Assert.assertEquals(((Field)condition.getExpression().get(0)).getName(),"ID");
        Assert.assertEquals(condition.getComparator(), Comparator.EQUALS);


    }
    @Test
    public void testSchemaDDLParsingFullOuterJoinClause(){
        Query query = parser.parseQuery(QUERY_FULL_OUTER_JOIN_CLAUSE);
        Assert.assertTrue(query instanceof SelectQuery<?>);
        SelectQuery selectQuery = (SelectQuery) query;

        Assert.assertTrue(selectQuery.getSelect().isEmpty());

        List<TableInfo> tables = SQLParser.parseTable(selectQuery);
        Assert.assertEquals(tables.size(),1);

        TableInfo table = tables.get(0);
        Assert.assertEquals(table.getType().name(),"EXPRESSION");
        Assert.assertTrue(table.isJoinTable());
        Assert.assertEquals(table.getJoinType().get(),JoinType.FULL_OUTER_JOIN);

        TableInfo lhs = (TableInfo) table.getLeftHandSelection().get();
        Assert.assertEquals(lhs.getName(),"AUTHOR");
        Assert.assertEquals( lhs.getType().name(),"TABLE");

        TableInfo rhs = (TableInfo) table.getRightHandSelection().get();
        Assert.assertTrue(rhs.isJoinTable());
        Assert.assertEquals(rhs.getName(),"join");
        Assert.assertEquals( rhs.getType().name(),"EXPRESSION");

        Assert.assertEquals( ((TableInfo) rhs.getLeftHandSelection().get()).getType().name(),"TABLE");
        Assert.assertEquals( ((TableInfo) rhs.getLeftHandSelection().get()).getName(),"BOOK");

        Assert.assertEquals( ((TableInfo) rhs.getRightHandSelection().get()).getType().name(),"TABLE");
        Assert.assertEquals( ((TableInfo) rhs.getRightHandSelection().get()).getName(),"BOOK_TO_BOOK_STORE");


        ConditionInfo condition = (ConditionInfo) table.getCondition().get();
        Assert.assertTrue(condition instanceof CompareConditionWrapper);
        Assert.assertEquals(condition.getFields().get(0).getName(),"AUTHOR_ID");
        Assert.assertEquals(((Field)condition.getExpression().get(0)).getName(),"ID");
        Assert.assertEquals(condition.getComparator(), Comparator.EQUALS);


    }

}
