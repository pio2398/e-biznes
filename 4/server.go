package main

import (
	"fmt"
	"net/http"
	"project/database"
	"project/handlers"

	"github.com/labstack/echo/v4"
)

func main() {

	// database.db_init()
	database.Init()
	fmt.Print(database.Database)

	e := echo.New()
	e.GET("/", func(c echo.Context) error {
		return c.JSON(http.StatusOK, e.Routes())
	})

	e.GET("/cart", func(c echo.Context) error {
		return handlers.GetCart(c)
	})
	e.POST("/cart", func(c echo.Context) error {
		return handlers.CreateCart(c)
	})

	e.GET("/category", func(c echo.Context) error {
		return handlers.GetCategory(c)
	})
	e.POST("/category", func(c echo.Context) error {
		return handlers.GetCategory(c)
	})

	e.GET("/order", func(c echo.Context) error {
		return handlers.GetOrder(c)
	})
	e.POST("/order", func(c echo.Context) error {
		return handlers.GetOrder(c)
	})

	e.GET("/product", func(c echo.Context) error {
		return handlers.GetProduct(c)
	})
	e.POST("/product", func(c echo.Context) error {
		return handlers.CreateProduct(c)
	})

	e.GET("/user", func(c echo.Context) error {
		return handlers.GetUser(c)
	})
	e.POST("/user", func(c echo.Context) error {
		return handlers.GetUser(c)
	})

	e.Logger.Fatal(e.Start(":8888"))
}
