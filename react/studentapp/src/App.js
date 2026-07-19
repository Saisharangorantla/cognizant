import logo from './logo.svg';
import './App.css';
import Home from './components/home';
import About from './components/About';
import Contact from './components/contact';

function App() {
  return (
    <div className="container">
       <Home/>
       <About/>
       <Contact/>
    </div>
  );
}

export default App;
