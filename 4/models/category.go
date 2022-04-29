package models

import "gorm.io/gorm"

type ProductCategory struct {
	gorm.Model
	ID       uint `gorm:"primaryKey"`
	Name     string
	Products []Product
}
