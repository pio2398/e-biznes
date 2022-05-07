import React from "react";
import { ShopContextProvider } from "./contexts/ShopContext";

import Products from "./components/Products";
import Cart from "./components/Cart";
import Checkout from "./components/Checkout";

const App = () => (
  <ShopContextProvider>
    <Products />
    <Cart />
    <Checkout />
  </ShopContextProvider>
);

export default App;
