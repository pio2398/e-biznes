import React, { useEffect, useState } from "react";
import useCart from "../hooks/useCart";

export const ShopContext = React.createContext([]);

export const ShopContextProvider = (props) => {
  const [products, setProducts] = useState([]);
  const UseCart = useCart({ products, setProducts });

  async function getProducts() {
    const response = await fetch(
      process.env.REACT_APP_BACKEND_URL + "/products"
    ).then((response) => response.json());
    return response;
  }

  useEffect(() => {
    getProducts().then((products) => {
      setProducts(products);
    });
  }, []);

  return (
    <ShopContext.Provider value={{ products: products, useCart: UseCart }}>
      {props.children}
    </ShopContext.Provider>
  );
};

export default ShopContext;
