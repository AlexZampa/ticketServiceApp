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
        <Routes>
          <Route index path={'/'} element={<View.Home/>}/>
            <Route path='/profile' element={<View.Profile/>} />
            <Route path='/editProfile' element={<View.EditProfile/>} />
            <Route path='/newProfile' element={<View.EditProfile/>} />
            <Route path='*' element={<View.Error/>}/>
        </Routes>

      </AppContainer>
  );
}

export default App;
