package db

import (
	"anekdotas"
	"context"
	"fmt"
)

const CategoryTableName = "categories"

type CategoryRecord struct {
	ID   int64  `db:"id"`
	Name string `db:"name"`
}

func (r *Repo) GetCategories(ctx context.Context) ([]*anekdotas.Category, error) {
	stmt := fmt.Sprintf("SELECT id, name FROM %s", CategoryTableName)
	records := make([]*CategoryRecord, 0)
	if err := r.db.SelectContext(ctx, &records, stmt); err != nil {
		return nil, err
	}
	return deriveFmapCRecordToModel(func(record *CategoryRecord) *anekdotas.Category {
		return &anekdotas.Category{
			ID:   record.ID,
			Name: record.Name,
		}
	}, records), nil
}
