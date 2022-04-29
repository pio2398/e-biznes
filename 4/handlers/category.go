package handlers

import (
	"net/http"
	"project/database"
	"project/models"
	"github.com/labstack/echo/v4"
)

func GetCategory(c echo.Context) error {
	var category []models.ProductCategory
	database.Database.Find(&category)
	return c.JSON(http.StatusOK, category)
}

func CreateCategory(c echo.Context) error {
	category := new(models.ProductCategory)
	if err := c.Bind(category); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	database.Database.Create(category)
	return c.JSON(http.StatusOK, category)
}
