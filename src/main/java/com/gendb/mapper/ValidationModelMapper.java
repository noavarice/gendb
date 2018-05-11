package com.gendb.mapper;

import com.gendb.dto.DataTypeDto;
import com.gendb.dto.DatabaseDto;
import com.gendb.dto.ForeignKeyDto;
import com.gendb.dto.TableDto;
import com.gendb.model.validating.ValidatingDataType;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.validating.ValidatingForeignKey;
import com.gendb.model.validating.ValidatingTable;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ValidationModelMapper {

  default ValidatingForeignKey toValidationModel(final ForeignKeyDto dto) {
    final ValidatingForeignKey result = new ValidatingForeignKey();
    result.setTargetTable(dto.getReferences());
    if (dto.getColumnName() == null || dto.getColumnName().isEmpty()) {
      result.setColumnName(dto.getReferences() + "_fk");
    } else {
      result.setColumnName(dto.getColumnName());
    }

    return result;
  }

  List<ValidatingForeignKey> toKeyList(final List<ForeignKeyDto> dtoList);

  @Mapping(target = "name", expression = "java(dto.getName().value())")
  @Mapping(target = "handlerClass", source = "handler")
  ValidatingDataType toValidationModel(final DataTypeDto dto);

  @Mapping(target = "columns", source = "column")
  @Mapping(target = "rowsCount", source = "rows")
  @Mapping(target = "foreignKeys", source = "foreignKey")
  ValidatingTable toValidationModel(final TableDto dto);

  @Mapping(target = "tables", source = "table")
  @Mapping(target = "dbmsName", expression = "java(dto.getDbms().value())")
  ValidatingDatabase toValidationModel(final DatabaseDto dto);
}
