import logo from './logo.svg';
import './App.css';

import AppContainer from "./components/utils/AppContainer";
import {Route,Routes,useLocation,useNavigate} from "react-router-dom";
import * as View from './views/index'
function App() {

  const location = useLocation();
  const navigate = useNavigate();

  return (

      <AppContainer>
        <Routes location={location} key={location.pathname}>
          <Route path={'/'} element={View.Home}/>
        </Routes>

      </AppContainer>
  );
}

export default App;
