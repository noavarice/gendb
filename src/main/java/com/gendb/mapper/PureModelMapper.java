package com.gendb.mapper;

import com.gendb.generation.generator.impl.ForeignKeyGenerator;
import com.gendb.model.pure.Column;
import com.gendb.model.pure.ConcreteDistributionInterval;
import com.gendb.model.pure.DataType;
import com.gendb.model.pure.Database;
import com.gendb.model.pure.DistributionInterval;
import com.gendb.model.pure.ForeignKey;
import com.gendb.model.pure.SupportedDbms;
import com.gendb.model.pure.Table;
import com.gendb.model.validating.ValidatingColumn;
import com.gendb.model.validating.ValidatingDataType;
import com.gendb.model.validating.ValidatingDatabase;
import com.gendb.model.validating.ValidatingDistribution;
import com.gendb.model.validating.ValidatingDistributionPoint;
import com.gendb.model.validating.ValidatingTable;
import com.gendb.model.validating.ValidatingValueOrder;
import com.gendb.util.Fn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(imports = SupportedDbms.class)
public abstract class PureModelMapper {

  @Mapping(target = "minColumn", ignore = true)
  abstract DataType toModel(final ValidatingDataType type);

  List<DistributionInterval> mapDistribution(final ValidatingDistribution distribution) {
    if (distribution == null) {
      return Collections.emptyList();
    }

    final List<ValidatingDistributionPoint> points = distribution.getPoints();
    final List<DistributionInterval> result = new ArrayList<>(points.size() + 1);
    double prevPoint = distribution.getFirstPoint();
    for (final ValidatingDistributionPoint point: points) {
      final double nextPoint = point.getPoint();
      result.add(new DistributionInterval(prevPoint, nextPoint, point.getPercentage()));
      prevPoint = nextPoint;
    }

    return result;
  }

  long getCount(final DistributionInterval interval, final int rowsCount) {
    return Math.round(interval.getPercentage() * rowsCount / 100);
  }

  @Mapping(target = "count", expression = "java(getCount(interval, rowsCount))")
  abstract ConcreteDistributionInterval toModel(final DistributionInterval interval, final int rowsCount);

  public List<ConcreteDistributionInterval> mapDistribution(
      final List<DistributionInterval> distribution,
      final int rowsCount) {
    if (distribution == null) {
      return null;
    }

    return new ArrayList<>(Fn.map(distribution, interval -> this.toModel(interval, rowsCount)));
  }

  @Mapping(target = "table", ignore = true)
  @Mapping(target = "distributionIntervals", expression = "java(mapDistribution(column.getDistribution()))")
  abstract Column toModel(final ValidatingColumn column);

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
      table.getColumns().forEach(column -> column.setTable(table));
      for (final ForeignKey fk: table.getForeignKeys()) {
        final Column fkColumn = new Column();
        fkColumn.setName(fk.getColumnName());
        final DataType type = new DataType();
        type.setName("int");
        type.setNullable(false);
        type.setMin(1d);
        type.setMax((double)nameToTable.get(fk.getTargetTable()).getRowsCount());
        type.setHandlerClass(ForeignKeyGenerator.class.getCanonicalName());
        fkColumn.setType(type);
        fkColumn.setTable(table);
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
  @Mapping(target = "dbmsName", expression = "java(SupportedDbms.fromName(database.getDbms()))")
  public abstract Database toModel(final ValidatingDatabase database);

  @AfterMapping
  void addReverseLink(@MappingTarget final Database db) {
    Table.database = db;
  }
}
