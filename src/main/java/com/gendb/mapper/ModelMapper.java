package com.gendb.mapper;

import com.gendb.dto.DataTypeDto;
import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ForeignKeyDto;
import com.gendb.dto.TableDto;
import com.gendb.model.Column;
import com.gendb.model.DataType;
import com.gendb.model.Database;
import com.gendb.model.ForeignKey;
import com.gendb.model.Table;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ModelMapper {

  default ForeignKey toModel(final ForeignKeyDto dto) {
    final ForeignKey result = new ForeignKey();
    result.setTargetTable(dto.getReferences());
    if (dto.getColumnName() == null || dto.getColumnName().isEmpty()) {
      result.setColumnName(dto.getReferences() + "_fk");
    } else {
      result.setColumnName(dto.getColumnName());
    }

    return result;
  }

  @Mapping(target = "name", expression = "java(dto.getName().value())")
  @Mapping(target = "handlerClass", source = "handler")
  DataType toModel(final DataTypeDto dto);

  @Mapping(target = "columnTypes", ignore = true)
  @Mapping(target = "columns", source = "column")
  @Mapping(target = "rowsCount", source = "rows")
  @Mapping(target = "foreignKeys", source = "foreignKey")
  Table toModel(final TableDto dto);

  default List<Table> mapTables(final DatabaseDto dto) {
    final Map<String, Integer> tableToRowsCount = dto.getTable().stream()
      .collect(Collectors.toMap(TableDto::getName, TableDto::getRows));
    final List<Table> result = dto.getTable().stream()
      .map(this::toModel)
      .collect(Collectors.toList());
    for (final Table table: result) {
      for (final ForeignKey fk: table.getForeignKeys()) {
        if (!tableToRowsCount.containsKey(fk.getTargetTable())) {
          continue;
        }

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

  @Mapping(target = "tables", expression = "java(mapTables(dto))")
  @Mapping(target = "dbmsName", expression = "java(dto.getDbms().value())")
  Database toModel(final DatabaseDto dto);
}
