package org.jooq.impl;

import org.jooq.*;

import java.util.List;
import java.util.Optional;

public interface TableInfo<R extends Record> extends Named {
    /**
     * Get the table catalog.
     */
    Catalog getCatalog();

    /**
     * Get the table schema.
     */
    Schema getSchema();

    /**
     * Get the table type.
     */
    TableOptions.TableType getType();

    /**
     * Get the table options.
     */
    TableOptions getOptions();

    /**
     * Retrieve the table's primary key
     *
     * @return The primary key. This is never <code>null</code> for an updatable
     * table.
     */
    UniqueKey getPrimaryKey();

    /**
     * Retrieve all of the table's indexes.
     *
     * @return All indexes. This is never <code>null</code>. This method returns
     * an unmodifiable list.
     */
    List<Index> getIndexes();

    /**
     * Retrieve all of the table's unique keys.
     *
     * @return All keys. This is never <code>null</code>. This is never empty
     * for a {@link Table} with a {@link Table#getPrimaryKey()}. This
     * method returns an unmodifiable list.
     */
    List<UniqueKey> getKeys();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of a specific table, referencing
     * a this table.
     *
     * @param <O>   The other table's record type
     * @param other The other table of the foreign key relationship
     * @return Some other table's <code>FOREIGN KEY</code>'s towards an this
     * table. This is never <code>null</code>. This method returns an
     * unmodifiable list.
     */
    <O extends Record> List<ForeignKey<O, R>> getReferencesFrom(Table<O> other);

    /**
     * Get the list of <code>FOREIGN KEY</code>'s of this table
     *
     * @return This table's <code>FOREIGN KEY</code>'s. This is never
     * <code>null</code>.
     */
    List<ForeignKey<R, ?>> getReferences();

    /**
     * Get a list of <code>FOREIGN KEY</code>'s of this table, referencing a
     * specific table.
     *
     * @param <O>   The other table's record type
     * @param other The other table of the foreign key relationship
     * @return This table's <code>FOREIGN KEY</code>'s towards an other table.
     * This is never <code>null</code>.
     */
    <O extends Record> List<ForeignKey<R, O>> getReferencesTo(Table<O> other);

    /**
     * Get a list of <code>CHECK</code> constraints of this table.
     */
    List<Check<R>> getChecks();


    /**
     * @return
     */
    boolean isJoinTable();

    Optional<TableInfo> getLeftHandSelection();

    Optional<TableInfo> getRightHandSelection();

    Optional<JoinType> getJoinType();

    Optional<ConditionInfo> getCondition();

    Optional<List<TableField>> getUsing();

}
