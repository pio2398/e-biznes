import { useState, useEffect } from "react";
import { sendProducts } from "../client/RestClient";

function useCart(props) {
  const [Cart, setCart] = useState([]);
  const [count, setCount] = useState(0);

  useEffect(() => {
    var initialCart = props.products.filter((p) => p.State === "Cart");
    setCart(initialCart);
  }, [props.products]);

  function addProduct(product) {
    let new_product = { ...product };
    setCount(count + 1);
    new_product.count = count;
    setCart([...Cart, new_product]);
    sendProducts([product]);
  }

  function removeProduct(product) {
    const filteredProducts = Cart.filter((p) => product.count !== p.count);
    setCart([...filteredProducts]);
    sendProducts([product]);
  }

  return {
    Cart,
    addProduct,
    removeProduct,
    setCart,
  };
}

export default useCart;
