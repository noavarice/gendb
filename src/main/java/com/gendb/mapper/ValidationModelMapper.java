package com.gendb.mapper;

import com.gendb.dto.DataTypeDto;
import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ForeignKeyDto;
import com.gendb.dto.TableDto;
import com.gendb.dto.ValueOrderDto;
import com.gendb.model.validating.ValidatingDataType;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.validating.ValidatingForeignKey;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ValidationModelMapper {

  @Mapping(target = "targetTable", source = "references")
  ValidatingForeignKey toModel(final ForeignKeyDto dtoList);

  @Mapping(target = "name", expression = "java(dto.getName().value())")
  @Mapping(target = "handlerClass", source = "handler")
  ValidatingDataType toValidationModel(final DataTypeDto dto);

  @Mapping(target = "columns", source = "column")
  ValidatingValueOrder toValidationModel(final ValueOrderDto dto);

  @Mapping(target = "columns", source = "column")
  @Mapping(target = "rowsCount", source = "rows")
  @Mapping(target = "foreignKeys", source = "foreignKey")
  @Mapping(target = "valueOrders", source = "valueOrder")
  ValidatingTable toValidationModel(final TableDto dto);

  @Mapping(target = "tables", source = "table")
  @Mapping(target = "dbmsName", expression = "java(dto.getDbms().value())")
  ValidatingDatabase toValidationModel(final DatabaseDto dto);
}
