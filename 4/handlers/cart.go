package handlers

import (
	"net/http"
	"project/database"
	"project/models"
	"github.com/labstack/echo/v4"
)

func GetCart(c echo.Context) error {
	var cart []models.Cart
	database.Database.Find(&cart)
	return c.JSON(http.StatusOK, cart)
}

func CreateCart(c echo.Context) error {
	cart := new(models.Cart)
	if err := c.Bind(cart); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	database.Database.Create(cart)
	return c.JSON(http.StatusOK, cart)

}
