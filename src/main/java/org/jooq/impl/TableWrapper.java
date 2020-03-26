package org.jooq.impl;

import org.jooq.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.jooq.impl.ConditionUtils.condition;

public class TableWrapper implements TableInfo {
    private Table table;

    public static TableWrapper of(Table table) {
        return new TableWrapper(table);
    }

    public TableWrapper(Table table) {
        this.table = table;
    }

    @Override
    public Catalog getCatalog() {
        return table.getCatalog();
    }

    @Override
    public Schema getSchema() {
        return table.getSchema();
    }

    @Override
    public TableOptions.TableType getType() {
        return table.getType();
    }

    @Override
    public TableOptions getOptions() {
        return table.getOptions();
    }

    @Override
    public UniqueKey getPrimaryKey() {
        return table.getPrimaryKey();
    }

    @Override
    public List<Index> getIndexes() {
        return table.getIndexes();
    }

    @Override
    public List<UniqueKey> getKeys() {
        return table.getKeys();
    }

    @Override
    public List<ForeignKey> getReferences() {
        return table.getReferences();
    }

    @Override
    public List<Check> getChecks() {
        return table.getChecks();
    }

    @Override
    public boolean isJoinTable() {
        return table instanceof JoinTable;
    }


    @Override
    public Optional<TableInfo> getLeftHandSelection() {
        return Optional.ofNullable(isJoinTable() ? TableWrapper.of(asJoinTable().lhs) : null);
    }

    @Override
    public Optional<TableInfo> getRightHandSelection() {
        return Optional.ofNullable(isJoinTable() ? TableWrapper.of(asJoinTable().rhs) : null);

    }

    @Override
    public Optional<JoinType> getJoinType() {
        return Optional.ofNullable(isJoinTable() ? asJoinTable().type : null);
    }

    @Override
    public Optional<ConditionInfo> getCondition() {
        return Optional.ofNullable(isJoinTable() ? condition(asJoinTable().condition) : null);
    }

    @Override
    public Optional<List<TableField>> getUsing() {
        return Optional.ofNullable(isJoinTable() ?  asJoinTable().using.stream().map(f->(TableField)f).collect(Collectors.toList()) : null);
    }

    @Override
    public List<ForeignKey> getReferencesTo(Table other) {
        return table.getReferencesTo(other);
    }

    @Override
    public List<ForeignKey> getReferencesFrom(Table other) {
        return table.getReferencesFrom(other);
    }

    @Override
    public String getName() {
        return table.getName();
    }

    @Override
    public Name getQualifiedName() {
        return table.getQualifiedName();
    }

    @Override
    public Name getUnqualifiedName() {
        return table.getUnqualifiedName();
    }

    @Override
    public String getComment() {
        return table.getComment();
    }


    private JoinTable asJoinTable() {
        return (JoinTable) table;
    }

}
