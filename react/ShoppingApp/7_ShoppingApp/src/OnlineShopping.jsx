import React from 'react';
import Cart from './Cart';

class OnlineShopping extends React.Component {
  constructor(props) {
    super(props);
    this.cartItems = [
      new Cart('Wireless Mouse', 799),
      new Cart('Mechanical Keyboard', 3499),
      new Cart('USB-C Hub', 1299),
      new Cart('Laptop Stand', 1899),
      new Cart('Webcam', 2599),
    ];
  }

  render() {
    return (
      <div>
        <h1>Online Shopping Cart</h1>
        <table border="1" cellPadding="8">
          <thead>
            <tr>
              <th>Item Name</th>
              <th>Price</th>
            </tr>
          </thead>
          <tbody>
            {this.cartItems.map((item, index) => (
              <tr key={index}>
                <td>{item.Itemname}</td>
                <td>{item.Price}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

export default OnlineShopping;
