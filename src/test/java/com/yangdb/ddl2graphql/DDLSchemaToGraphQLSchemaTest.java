package com.yangdb.ddl2graphql;

import org.jooq.DSLContext;
import org.jooq.Meta;
import org.jooq.Named;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultDSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sun.misc.IOUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * see https://www.jooq.org/doc/3.13/manual/sql-building/sql-interpreter/
 */
public class DDLSchemaToGraphQLSchemaTest {
    private Meta meta,meta1;


    @Before
    public void setUp() throws Exception {
        DSLContext ctx =new DefaultDSLContext(SQLDialect.DEFAULT);
        String ddl = new String(IOUtils.readAllBytes(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream("ddl/bike_shop.ddl"))));
        
        meta = ctx.meta(ddl);

    }

    @Test
    public void testSchemaDDLParsing(){
       Assert.assertEquals(new HashSet<>(Arrays.asList("SALES","PRODUCTION")),
               meta.getSchemas().stream().map(Named::getName).filter(s->!s.isEmpty()).collect(Collectors.toSet()));
       Assert.assertEquals(0,
               meta.getCatalogs().stream().map(Named::getName).filter(s->!s.isEmpty()).collect(Collectors.toSet()).size());
       Assert.assertEquals(0,
               meta.getIndexes().stream().map(Named::getName).filter(s->!s.isEmpty()).collect(Collectors.toSet()).size());
       Assert.assertEquals(new HashSet<>(Arrays.asList("primary key (CUSTOMER_ID)",
                        "primary key (STORE_ID)",
                        "primary key (CATEGORY_ID)",
                        "primary key (ORDER_ID)",
                        "primary key (\n" +
                                "  STORE_ID, \n" +
                                "  PRODUCT_ID\n" +
                                ")",
                        "primary key (STAFF_ID)",
                        "primary key (PRODUCT_ID)",
                        "primary key (BRAND_ID)",
                        "primary key (\n" +
                                "  ORDER_ID, \n" +
                                "  ITEM_ID\n" +
                                ")")),
               meta.getPrimaryKeys().stream().map(Named::toString).filter(s->!s.isEmpty()).collect(Collectors.toSet()));
       Assert.assertEquals(0,
               meta.getSequences().stream().map(Named::toString).filter(s->!s.isEmpty()).collect(Collectors.toSet()).size());
       Assert.assertEquals(new HashSet<>(Arrays.asList("PRODUCTION.CATEGORIES","SALES.ORDER_ITEMS","PRODUCTION.BRANDS","SALES.STORES","PRODUCTION.PRODUCTS","PRODUCTION.STOCKS","SALES.CUSTOMERS","SALES.ORDERS","SALES.STAFFS")),
               meta.getTables().stream().map(Named::toString).filter(s->!s.isEmpty()).collect(Collectors.toSet()));
    }
}
