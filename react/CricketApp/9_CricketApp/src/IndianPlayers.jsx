const teamPlayers = [
  'Rohit Sharma',
  'Shubman Gill',
  'Virat Kohli',
  'Shreyas Iyer',
  'KL Rahul',
  'Ravindra Jadeja',
  'Hardik Pandya',
  'Axar Patel',
];

const oddTeamPlayers = teamPlayers.filter((_, index) => index % 2 === 0);
const evenTeamPlayers = teamPlayers.filter((_, index) => index % 2 !== 0);

const [firstOdd, secondOdd, ...restOdd] = oddTeamPlayers;
const [firstEven, secondEven, ...restEven] = evenTeamPlayers;

const T20players = ['Suryakumar Yadav', 'Ishan Kishan', 'Arshdeep Singh'];
const RanjiTrophyPlayers = ['Sarfaraz Khan', 'Yash Dhull', 'Rajat Patidar'];
const mergedPlayers = [...T20players, ...RanjiTrophyPlayers];

function IndianPlayers() {
  return (
    <div>
      <h2>Odd Position Players</h2>
      <p>{firstOdd}, {secondOdd}, {restOdd.join(', ')}</p>

      <h2>Even Position Players</h2>
      <p>{firstEven}, {secondEven}, {restEven.join(', ')}</p>

      <h2>Merged Squad (T20 + Ranji Trophy)</h2>
      <p>{mergedPlayers.join(', ')}</p>
    </div>
  );
}

export default IndianPlayers;
