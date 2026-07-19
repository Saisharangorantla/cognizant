import ListofPlayers from './ListofPlayers';
import IndianPlayers from './IndianPlayers';

const flag = true;

function App() {
  let content;

  if (flag) {
    content = <ListofPlayers />;
  } else {
    content = <IndianPlayers />;
  }

  return (
    <div>
      <h1>Cricket App</h1>
      {content}
    </div>
  );
}

export default App;
