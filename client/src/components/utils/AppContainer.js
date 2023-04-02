/*
* --------------------------------------------------------------------
*
* Package:         client
* Module:          src/components/utils
* File:            AppContainer.js
*
* --------------------------------------------------------------------
*/

//Imports
import {Container} from "react-bootstrap";

//Components
import Navbar from "../ui-core/Navbar/Navbar";

const AppContainer = ({ ...props }) => {

    return (
        <Container fluid className='app-container bg-primary'>
            <Navbar/>
        </Container >
    );
}

export default AppContainer;