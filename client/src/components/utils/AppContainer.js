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
import {Container, Row} from "react-bootstrap";

//Components
import Navbar from "../ui-core/Navbar/Navbar";

const AppContainer = ({ ...props }) => {

    return (
        <Container fluid className='app-container'>
            <Row>
                <Navbar/>
            </Row>
            <Row className='mt-4 p-0'>
                {props.children}
            </Row>
        </Container >
    );
}

export default AppContainer;