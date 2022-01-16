package db

import (
	"context"
	"database/sql"
	"fmt"
)

const (
	HostsTableName     = "hosts"
	PurchasesTableName = "purchases"
)

func (r *Repo) GetPurchasedHosts(ctx context.Context, userID int64) ([]int64, error) {
	stmt := fmt.Sprintf(
		`SELECT host_id FROM %s
		WHERE user_id = ?`,
		PurchasesTableName,
	)
	ret := make([]int64, 0)
	return ret, translateDBError(r.db.SelectContext(ctx, &ret, r.db.Rebind(stmt), userID))
}

func (r *Repo) CreateHostPurchase(ctx context.Context, hostID, userID int64) error {
	tx, err := r.db.BeginTxx(ctx, &sql.TxOptions{})
	if err != nil {
		return err
	}
	defer tx.Rollback()
	stmt := fmt.Sprintf(
		"UPDATE %s SET coins = coins - (SELECT cost FROM %s WHERE id = ?) WHERE id = ?",
		UsersTableName, HostsTableName,
	)
	if _, err = tx.Exec(tx.Rebind(stmt), hostID, userID); err != nil {
		return translateDBError(err)
	}
	stmt = fmt.Sprintf("INSERT INTO %s VALUES (?, ?)", PurchasesTableName)
	if _, err = tx.Exec(tx.Rebind(stmt), hostID, userID); err != nil {
		return translateDBError(err)
	}
	return tx.Commit()
}
