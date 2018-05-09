package com.gendb.mapper;

import com.gendb.dto.DataTypeDto;
import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ForeignKeyDto;
import com.gendb.dto.TableDto;
import com.gendb.model.DataType;
import com.gendb.model.Database;
import com.gendb.model.ForeignKey;
import com.gendb.model.Table;
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

  @Mapping(target = "tables", source = "table")
  @Mapping(target = "dbmsName", expression = "java(dto.getDbms().value())")
  Database toModel(final DatabaseDto dto);
}
