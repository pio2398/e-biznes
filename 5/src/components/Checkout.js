import React, { useContext } from "react";
import ShopContext from "../contexts/ShopContext";
import { sendProducts } from "../client/RestClient";

function Checkout() {
  const { useCart } = useContext(ShopContext);

  function buyProducts() {
    sendProducts([useCart.Cart]);
    useCart.setCart([]);
  }

  return (
    <div>
      <button onClick={buyProducts}>Buy product</button>
    </div>
  );
}

export default Checkout;
