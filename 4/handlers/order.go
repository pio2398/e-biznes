package handlers

import (
	"net/http"
	"project/database"
	"project/models"
	"github.com/labstack/echo/v4"
	"gorm.io/gorm/clause"
)

func GetOrder(c echo.Context) error {
	var order []models.Order
	database.Database.Find(&order)
	database.Database.Preload(clause.Associations).Find(&order)
	return c.JSON(http.StatusOK, order)
}

func CreateOrder(c echo.Context) error {
	order := new(models.Order)
	if err := c.Bind(order); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	database.Database.Create(order)
	return c.JSON(http.StatusOK, order)
}
