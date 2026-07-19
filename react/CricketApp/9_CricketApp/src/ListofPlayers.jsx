const players = [
  { name: 'Rohit Sharma', score: 88 },
  { name: 'Shubman Gill', score: 45 },
  { name: 'Virat Kohli', score: 102 },
  { name: 'Shreyas Iyer', score: 34 },
  { name: 'KL Rahul', score: 67 },
  { name: 'Ravindra Jadeja', score: 29 },
  { name: 'Hardik Pandya', score: 58 },
  { name: 'Axar Patel', score: 15 },
  { name: 'Kuldeep Yadav', score: 8 },
  { name: 'Mohammed Shami', score: 4 },
  { name: 'Jasprit Bumrah', score: 2 },
];

const lowScorers = players.filter((player) => player.score < 70);

function ListofPlayers() {
  return (
    <div>
      <h2>All Players</h2>
      <ul>
        {players.map((player, index) => (
          <li key={index}>
            {player.name} - {player.score}
          </li>
        ))}
      </ul>

      <h2>Players With Score Below 70</h2>
      <ul>
        {lowScorers.map((player, index) => (
          <li key={index}>
            {player.name} - {player.score}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ListofPlayers;
