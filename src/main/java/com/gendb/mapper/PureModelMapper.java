package com.gendb.mapper;

import com.gendb.model.pure.Column;
import com.gendb.model.pure.DataType;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.ForeignKey;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.validating.ValidatingTable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public abstract class PureModelMapper {

  @Mapping(target = "columnTypes", ignore = true)
  abstract Table toModel(final ValidatingTable table);

  List<Table> mapTables(final ValidatingDatabase dto) {
    final Map<String, Integer> tableToRowsCount = dto.getTables().stream()
      .collect(Collectors.toMap(ValidatingTable::getName, ValidatingTable::getRowsCount));
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
        type.setMax((double)tableToRowsCount.get(fk.getTargetTable()));
        fkColumn.setType(type);
        table.getColumns().add(fkColumn);
      }
    }

    return result;
  }

  @Mapping(target = "tables", expression = "java(mapTables(database))")
  public abstract Database toModel(final ValidatingDatabase database);
}
