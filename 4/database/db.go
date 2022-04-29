package database

import (
	"os"
	"project/models"
	"gorm.io/driver/sqlite"
	"gorm.io/gorm"
)

var Database *gorm.DB = nil

func Init() {

	if _, err := os.Stat("./local.db"); err == nil {
		os.Remove("./local.db")
	}

	db, err := gorm.Open(sqlite.Open("local.db"), &gorm.Config{})
	if err != nil {
		panic("failed to connect database")
	}

	db.AutoMigrate(&models.Cart{}, &models.ProductCategory{}, &models.Order{}, &models.User{},
		&models.Product{})

	p1 := models.Product{Barcode: "1234", Price: 1234}
	p2 := models.Product{Barcode: "4321", Price: 4321}
	p3 := models.Product{Barcode: "4444", Price: 4111}
	for _, p := range []*models.Product{&p1, &p2, &p3} {
		db.Create(p)
	}

	u1 := models.User{Name: "qwert", Username: "uqwert1"}
	u2 := models.User{Name: "qwert2", Username: "uqwert2"}
	u3 := models.User{Name: "qwert3", Username: "uqwert3"}
	for _, p := range []*models.User{&u1, &u2, &u3} {
		db.Create(p)
	}

	o1 := models.Order{Product: p1, User: u1}
	o2 := models.Order{Product: p2, User: u2}
	o3 := models.Order{Product: p3, User: u3}

	for _, p := range []*models.Order{&o1, &o2, &o3} {
		db.Create(p)
	}

	c1 := models.ProductCategory{Products: []models.Product{p1, p2}, Name: "test"}

	for _, p := range []*models.ProductCategory{&c1} {
		db.Create(p)
	}

	uc1 := models.Cart{Products: []models.Product{p1, p2}, User: u1}

	for _, p := range []*models.Cart{&uc1} {
		db.Create(p)
	}

	Database = db

}
