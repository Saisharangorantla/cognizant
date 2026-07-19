import React from 'react';

class CountPeople extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      entrycount: 0,
      exitcount: 0,
    };
  }

  UpdateEntry = () => {
    this.setState({ entrycount: this.state.entrycount + 1 });
  };

  UpdateExit = () => {
    this.setState({ exitcount: this.state.exitcount + 1 });
  };

  render() {
    return (
      <div>
        <h1>Mall Visitor Counter</h1>
        <p>People Entered: {this.state.entrycount}</p>
        <p>People Exited: {this.state.exitcount}</p>
        <button onClick={this.UpdateEntry}>Login</button>
        <button onClick={this.UpdateExit}>Exit</button>
      </div>
    );
  }
}

export default CountPeople;
