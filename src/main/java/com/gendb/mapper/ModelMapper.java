package com.gendb.mapper;

import com.gendb.dto.ColumnDto;
import com.gendb.dto.DataTypeDto;
import com.gendb.dto.DatabaseDto;
import com.gendb.dto.TableDto;
import com.gendb.model.Column;
import com.gendb.model.DataType;
import com.gendb.model.Database;
import com.gendb.model.Table;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ModelMapper {

  ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

  @Mapping(target = "name", expression = "java(dto.getName().value())")
  DataType toModel(final DataTypeDto dto);

  Column toModel(final ColumnDto dto);

  @Mapping(target = "columns", source = "column")
  @Mapping(target = "id", expression = "java(dto.getId().getColumn())")
  Table toModel(final TableDto dto);

  @Mapping(target = "tables", source = "table")
  @Mapping(target = "dbmsName", expression = "java(dto.getDbms().value())")
  Database toModel(final DatabaseDto dto);
}
