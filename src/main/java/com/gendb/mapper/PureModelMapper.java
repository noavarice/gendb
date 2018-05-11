package com.gendb.mapper;

import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.ForeignKey;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingDataType;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class PureModelMapper {

  @Mapping(target = "minColumn", ignore = true)
  abstract DataType toModel(final ValidatingDataType type);

  @Mapping(target = "columnTypes", ignore = true)
  @Mapping(target = "columnGenerationOrder", ignore = true)
  abstract Table toModel(final ValidatingTable table);

  List<Table> mapTables(final ValidatingDatabase dto) {
    final Map<String, ValidatingTable> nameToTable = dto.getTables().stream()
      .collect(Collectors.toMap(ValidatingTable::getName, Function.identity()));
    final List<Table> result = dto.getTables().stream()
      .map(this::toModel)
      .collect(Collectors.toList());
    for (final Table table: result) {
      for (final ForeignKey fk: table.getForeignKeys()) {
        final Column fkColumn = new Column();
        fkColumn.setName(fk.getColumnName());
        final DataType type = new DataType();
        type.setName("int");
        type.setUnsigned(true);
        type.setNullable(false);
        type.setMin(1d);
        type.setMax((double)nameToTable.get(fk.getTargetTable()).getRowsCount());
        fkColumn.setType(type);
        table.getColumns().add(fkColumn);
      }

      final Map<String, Column> nameToColumn = table.getColumns().stream()
        .collect(Collectors.toMap(Column::getName, Function.identity()));
      for (final ValidatingValueOrder order: nameToTable.get(table.getName()).getValueOrders()) {
        final List<String> cols = order.getColumns();
        for (int i = 1; i < cols.size(); ++i) {
          final Column minColumn = nameToColumn.get(cols.get(i - 1));
          nameToColumn.get(cols.get(i)).getType().setMinColumn(minColumn);
        }
      }
    }

    return result;
  }

  @Mapping(target = "tables", expression = "java(mapTables(database))")
  public abstract Database toModel(final ValidatingDatabase database);
}
