import React, { useContext } from "react";

import ShopContext from "../contexts/ShopContext";

function Products() {
  const { products, useCart } = useContext(ShopContext);
  return (
    <div>
      <div className="products">
        <b>Products:</b>
        <table>
          <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Actions</th>
          </tr>

          {products.map((p) => (
            <tr>
              <th>{p.Name}</th>
              <th>{p.Price}</th>
              <th>
                <button onClick={() => useCart.addProduct(p)}>Add</button>
              </th>
            </tr>
          ))}
        </table>
      </div>
    </div>
  );
}

export default Products;
