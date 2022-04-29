package models

import "gorm.io/gorm"

type Cart struct {
	gorm.Model
	UserID   int
	User     User
	Products []Product `gorm:"many2many:cart_users;"`
}
