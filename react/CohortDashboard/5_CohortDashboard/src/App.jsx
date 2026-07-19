import cohortData from './cohortData';
import CohortDetails from './CohortDetails';

function App() {
  return (
    <div>
      <h1>Academy Cohort Dashboard</h1>
      {cohortData.map((cohort) => (
        <CohortDetails key={cohort.id} cohort={cohort} />
      ))}
    </div>
  );
}

export default App;
