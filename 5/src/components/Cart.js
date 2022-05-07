import React, { useContext } from "react";
import ShopContext from "../contexts/ShopContext";

function Cart() {
  const { useCart } = useContext(ShopContext);
  const result = useCart.Cart.reduce(
    (total, currentValue) => (total = total + currentValue.Price),
    0
  );

  return (
    <div>
      <table>
        <tr>
          <th>Name</th>
          <th>Price</th>
          <th>Actions</th>
        </tr>
        {useCart.Cart.map((p, id) => (
          <tr>
            <th>{p.Name}</th>
            <th>{p.Price}</th>
            <th>
              <button onClick={() => useCart.removeProduct(p)}>Remove</button>
            </th>
          </tr>
        ))}
      </table>
      Products price: {result}
    </div>
  );
}

export default Cart;
