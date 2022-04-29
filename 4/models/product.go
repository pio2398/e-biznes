package models

import "gorm.io/gorm"

type Product struct {
	gorm.Model
	ID                uint `gorm:"primaryKey"`
	Barcode           string
	Price             float32
	ProductCategoryID int
}
