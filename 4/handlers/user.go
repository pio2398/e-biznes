package handlers

import (
	"net/http"
	"project/database"
	"project/models"
	"github.com/labstack/echo/v4"
)

func GetUser(c echo.Context) error {
	var users []models.User
	database.Database.Find(&users)
	return c.JSON(http.StatusOK, users)
}

func CreateUser(c echo.Context) error {
	user := new(models.User)
	if err := c.Bind(user); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	database.Database.Create(user)
	return c.JSON(http.StatusOK, user)

}
