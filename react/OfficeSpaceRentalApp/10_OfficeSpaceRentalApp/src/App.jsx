const featuredOffice = {
  name: 'Skyline Business Suite',
  rent: 75000,
  address: 'MG Road, Bengaluru',
};

const officeSpaces = [
  { id: 1, name: 'Riverside Workhub', rent: 45000, address: 'Banjara Hills, Hyderabad' },
  { id: 2, name: 'Skyline Business Suite', rent: 75000, address: 'MG Road, Bengaluru' },
  { id: 3, name: 'Greenfield Co-work', rent: 52000, address: 'Powai, Mumbai' },
  { id: 4, name: 'Metro Corporate Park', rent: 89000, address: 'Connaught Place, Delhi' },
];

function App() {
  return (
    <div>
      <h1>Office Space Rentals</h1>
      <img src="https://placehold.co/500x250?text=Office+Space" alt="office space" width="500" height="250" />

      <h2>{featuredOffice.name}</h2>
      <p>Rent: {featuredOffice.rent}</p>
      <p>Address: {featuredOffice.address}</p>

      <h2>Available Listings</h2>
      <ul>
        {officeSpaces.map((office) => (
          <li key={office.id}>
            {office.name} - {office.address} -{' '}
            <span style={{ color: office.rent < 60000 ? 'red' : 'green' }}>
              {office.rent}
            </span>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;
